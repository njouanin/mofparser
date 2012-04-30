/**
 * Copyright 2008 EscapeK
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 * 
 * --------------------------------------------------------------------------
 * $Id$
 * --------------------------------------------------------------------------
 */
package org.escapek.mofparser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.escapek.mofparser.decl.ClassDecl;
import org.escapek.mofparser.decl.InstDecl;
import org.escapek.mofparser.decl.InstancePropertyDecl;
import org.escapek.mofparser.decl.PragmaDecl;
import org.escapek.mofparser.decl.PropertyDecl;
import org.escapek.mofparser.decl.Qualifier;
import org.escapek.mofparser.decl.QualifierDecl;
import org.escapek.mofparser.decl.TypeDecl;
import org.escapek.mofparser.exceptions.MOFParserException;
import org.escapek.mofparser.internal.CaseInsensitiveStringStream;
import org.escapek.mofparser.internal.cim23Lexer;
import org.escapek.mofparser.internal.cim23Parser;


/**
 * MOFParser provides methods for parsing a input stream containing a MOF syntax production.
 * Parsing is based on a callback system. Once the parser has been initialized, parsin is ran
 * by passing the parser an event handler instance. Event handler methods will be automatically
 * called by the parser when it detects some MOF language specification, so the handler can easily
 * manage the data. 
 * MOFParser also provides some basic checks on MOF syntax which can be intercepted by the handler.
 * @author nico
 *
 */
public class MOFParser {
	private static String PRAGMA_DECL 		= "CompilerDirective";
	private static String PRAGMA_INCLUDE 	= "include";
	private static String QUALIFIER_DECL 	= "qualifier";
	private static String CLASS_DECL 		= "class";
	private static String TYPE				= "Type";
	private static String ARRAY				= "Array";
	private static String SCOPE				= "Scope";
	private static String FLAVOR			= "Flavor";
	private static String DEFAULT			= "Default";
	private static String QUALIFIERS		= "Qualifiers";
	private static String SUPERCLASS		= "SuperClass";
	private static String REFERENCE 		= "Reference";
	private static String PROPERTY			= "Property";
	private static String ALIAS				= "Alias";
	private static String VALUE				= "Value";
	
	private cim23Lexer lexer;
	
	/**
	 * Default constructor. 
	 * CIM 2.3 lexer is initialized by this constructor.
	 */
	public MOFParser() {
		 lexer = new cim23Lexer();
	}
	
	/**
	 * Parse a MOF specification read from an InputStream.
	 * Parsing events are sent to the given handler instance.
	 * @param iStream stream containing MOF specification to read. This stream can come from a file, a socket or whatever.
	 * @param handler instance of the handler which will receive parsing events.
	 * @throws MOFParserException thrown if an error is detected during parse.
	 * @throws IOException if an error occurs while reading the stream
	 */
	public void parse(InputStream iStream, IContentHandler handler) throws MOFParserException, IOException {
		OutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int numRead=0;
        while((numRead=iStream.read(buf)) != -1){
        	out.write(buf, 0, numRead);
        }
		
		parse(out.toString(), handler);
	}

	/**
	 * Parse a MOF specification read from an InputStream.
	 * Parsing events are sent to the given handler instance.
	 * @param mofContent String containing a MOF specification.
	 * @param handler instance of the handler which will receive parsing events.
	 * @throws MOFParserException thrown if an error is detected during parse.
	 */
	public void parse(String mofContent, IContentHandler handler) throws MOFParserException {
		handler.startDocument();
		lexer.setCharStream(new CaseInsensitiveStringStream(mofContent));
	   	CommonTokenStream tokens = new CommonTokenStream(lexer);
	    cim23Parser parser = new cim23Parser(tokens);
	    
		CommonTree specTree;
	    try {
			specTree = (CommonTree)parser.mofSpecification().getTree();
			//MPL-17
			//Test 'nil' to know is only one production has been parsed
			if(specTree.toString().equalsIgnoreCase("nil")) {
				for(int i=0; i < specTree.getChildCount(); i++) {
					CommonTree production = (CommonTree)specTree.getChild(i);
					parseProduction(production, handler);
				}
			}
			else {
				parseProduction(specTree, handler);
			}
		}
		catch (RecognitionException re) {
			handler.error(new MOFParserException(re));
		}
		
	    handler.endDocument();
	}
	
	private void parseProduction(CommonTree prodTree, IContentHandler handler) throws MOFParserException {
		Production prodType = null;
		String prodStr = prodTree.toString();
		
		if(prodStr.equalsIgnoreCase(CLASS_DECL)) {
			prodType = Production.classDeclaration;
		} else if (prodStr.equalsIgnoreCase(PRAGMA_DECL)) {
			prodType = Production.compilerDirective;
		} else if (prodStr.equalsIgnoreCase("Instance")) {
			prodType = Production.instanceDeclaration;
		} else if (prodStr.equalsIgnoreCase(QUALIFIER_DECL)) {
			prodType = Production.qualifierDeclaration;
		} else {
			handler.error(new MOFParserException(MOFParserException.UNKNOWN_PRODUCTION_TYPE));
			return;
		}
		handler.startProduction(prodType);
		
		if(prodType == Production.compilerDirective) {
			parseCompilerDirective(prodTree, handler);
		}
		else if(prodType == Production.qualifierDeclaration) {
			parseQualifierDeclaration(prodTree, handler);
		}
		else if(prodType == Production.classDeclaration) {
			parseClassDeclaration(prodTree, handler);
		}
		else if(prodType == Production.instanceDeclaration) {
			parseInstanceDeclaration(prodTree, handler);
		}
		
		handler.endProduction();
	}
	
	private void parseCompilerDirective(CommonTree tree, IContentHandler handler) throws MOFParserException {
		handler.startCompilerDirective();
		if(tree.getChildCount() < 1) {
			handler.error(new MOFParserException(
					MOFParserException.INVALID_COMPILER_DIRECTIVE_ARGUMENT_COUNT,
					tree.getChildCount()));
			return;
		}
		CommonTree pragmaTree = (CommonTree)tree.getChild(0);
		String pragmaName = pragmaTree.toString();
		if(pragmaName == null || pragmaName.equals("")) {
			handler.error(new MOFParserException(MOFParserException.INVALID_COMPILER_DIRECTIVE,
					pragmaName));
			return;
		}
		//Check if the pragma directive is one of the supported.
		CompilerDirective directive = null;
		for(CompilerDirective supportedDirectives : CompilerDirective.values()) {
			if(pragmaName.equalsIgnoreCase(supportedDirectives.toString())) {
				directive = supportedDirectives;
				break;
			}
		}
		if(directive == null) {
			handler.error(new MOFParserException(MOFParserException.INVALID_COMPILER_DIRECTIVE,
					pragmaName));
			return;
		}
		CommonTree paramTree = (CommonTree)tree.getChild(1);
		String pragmaParam = null;
		if(paramTree != null) {
			pragmaParam = removeQuotes(paramTree.toString());
		}

		handler.compilerDirective(new PragmaDecl(directive, pragmaParam));
		
		//If pragma is 'include', request included content from content handler
		if(directive.equals(CompilerDirective.include)) {
			handler.include(pragmaParam);
		}
		
		handler.endCompilerDirective();
	}

	private void parseClassDeclaration(CommonTree tree, IContentHandler handler) throws MOFParserException {
		handler.startClassDeclaration();
		if(tree.getChildCount() < 1) {
			handler.error(new MOFParserException(
					MOFParserException.INVALID_CLASS_DECL_ARGUMENT_COUNT,
					tree.getChildCount()));
			return;
		}
		ClassDecl cDecl = new ClassDecl();
		cDecl.name = tree.getChild(0).toString();
		if(cDecl.name == null || cDecl.name.equals("")) {
			handler.error(new MOFParserException(MOFParserException.INVALID_CLASS_NAME,
					cDecl.name));
			return;
		}
		CommonTree cTree = (CommonTree)tree.getChild(0);
		CommonTree sTree = getChild(cTree, SUPERCLASS);
		if(sTree != null && sTree.getChild(0) != null) {
			cDecl.parentClass = sTree.getChild(0).toString();
		}
		
		//Process class qualifiers
//		CommonTree qualifiersTree = getChild(cTree, QUALIFIERS);
//		for(int i=0; i < qualifiersTree.getChildCount(); i++) {
//			CommonTree qualTree = (CommonTree)qualifiersTree.getChild(i);
//			cDecl.qualifiers.add(processQualifier(qualTree, handler));
//		}

		//Process class properties
		for(int i=0; i < cTree.getChildCount(); i++) {
			CommonTree pTree = (CommonTree)cTree.getChild(i);
			if(pTree.toString().equalsIgnoreCase(QUALIFIERS)) {
				for(int j=0; j < pTree.getChildCount(); j++) {
					CommonTree qualTree = (CommonTree)pTree.getChild(j);
					cDecl.qualifiers.add(processQualifier(qualTree, handler));
				}
			}
			if(pTree.toString().equalsIgnoreCase(PROPERTY)) {
				cDecl.properties.add(processProperty(pTree, handler));
			}
		}
		handler.classDeclaration(cDecl);
		handler.endClassDeclaration();
	}
	
	private PropertyDecl processProperty(CommonTree pTree, IContentHandler handler) throws MOFParserException {
		PropertyDecl prop = new PropertyDecl();
		if(pTree.getChild(0) == null) {
			handler.error(new MOFParserException(MOFParserException.INVALID_PROPERTY_NAME, null));
			return null;
		}
		prop.name = pTree.getChild(0).toString();
		if(prop.name == null || prop.name.equals("")) {
			handler.error(new MOFParserException(MOFParserException.INVALID_QUALIFIER_NAME, null));
			return null;
		}
		//Process property type
		CommonTree typeTree = getChild(pTree, TYPE);
		prop.type = processTypeTree(typeTree, handler);

		//Process default value
		CommonTree valueTree = getChild(pTree, DEFAULT);
		if(valueTree != null) {
			prop.value = processValueTree(valueTree, handler);
		}

		//Process property qualifiers
		CommonTree qualifiersTree = getChild(pTree, QUALIFIERS);
		for(int i=0; i < qualifiersTree.getChildCount(); i++) {
			CommonTree qualTree = (CommonTree)qualifiersTree.getChild(i);
			prop.qualifiers.add(processQualifier(qualTree, handler));
		}
		return prop;
	}

	private Qualifier processQualifier(CommonTree qTree, IContentHandler handler) throws MOFParserException {
		Qualifier qual = new Qualifier();
		if(qTree.toString() == null) {
			handler.error(new MOFParserException(MOFParserException.INVALID_QUALIFIER_NAME,null));
			return null;
		}
		//qualifer type set to null, as we don't know the qualifer declaration
		qual.type = null;
		qual.name = qTree.toString();
		if(qual.name == null || qual.name.equals("")) {
			handler.error(new MOFParserException(MOFParserException.INVALID_QUALIFIER_NAME,null));
			return null;
		}
		qual.value = processValueTree(qTree, handler);

		//Process flavors
		CommonTree qualifierFlavorTree = getChild(qTree, FLAVOR);
		if(qualifierFlavorTree != null) {
			for(int i = 0; i < qualifierFlavorTree.getChildCount(); i++) {
				FlavorType fType = getFlavorType(qualifierFlavorTree.getChild(i).toString());
				if(fType != null) {
					qual.flavors.add(fType.toString());
				}
			}
		}
		return qual;
	}
	
	private void parseQualifierDeclaration(CommonTree tree, IContentHandler handler) throws MOFParserException {
		handler.startQualifierDeclaration();
		if(tree.getChildCount() < 2) {
			handler.error(new MOFParserException(
					MOFParserException.INVALID_QUALIFIER_DECL_ARGUMENT_COUNT,
					tree.getChildCount()));
			return;
		}
		QualifierDecl qDecl = new QualifierDecl();
		qDecl.name = tree.getChild(0).toString();
		if(qDecl.name == null || qDecl.name.equals("")) {
			handler.error(new MOFParserException(MOFParserException.INVALID_QUALIFIER_NAME,
					qDecl.name));
			return;
		}
		
		//Process qualifier type
		CommonTree typeTree = getChild(tree, TYPE);
		qDecl.type = processTypeTree(typeTree, handler);

		//Process default value
		CommonTree valueTree = getChild(typeTree, DEFAULT);
		if(valueTree != null) {
			qDecl.defaultValue = processValueTree(valueTree, handler);
		}

		//Process scopes
		CommonTree qualifierScopeTree = getChild(tree, SCOPE);
		if(qualifierScopeTree != null) {
			for(int i = 0; i < qualifierScopeTree.getChildCount(); i++) {
				ScopeType sType = getScopeType(qualifierScopeTree.getChild(i).toString());
				if(sType != null) {
					qDecl.scopes.add(sType.toString());
				}
			}
		}

		//Process flavors
		CommonTree qualifierFlavorTree = getChild(tree, FLAVOR);
		if(qualifierFlavorTree != null) {
			for(int i = 0; i < qualifierFlavorTree.getChildCount(); i++) {
				FlavorType fType = getFlavorType(qualifierFlavorTree.getChild(i).toString());
				if(fType != null) {
					qDecl.flavors.add(fType.toString());
				}
			}
		}

		handler.qualifierDeclaration(qDecl);
		handler.endQualifierDeclaration();
	}
	
	private TypeDecl processTypeTree(CommonTree typeTree, IContentHandler handler) throws MOFParserException {
		TypeDecl tDecl = new TypeDecl();
		
		if(typeTree == null) {
			handler.error(new MOFParserException(
					MOFParserException.INVALID_TYPE_TREE));
			return null;
		}
		CommonTree dataTypeTree = (CommonTree)typeTree.getChild(0);
		String dataType = dataTypeTree.toString();
		if(dataType == null || dataType.equals("")) {
			handler.error(new MOFParserException(MOFParserException.INVALID_DATA_TYPE,
					dataType));
			return null;
		}
		
		//Check if data type is a supported data type
		tDecl.name = null;
		for(DataType supportedType : DataType.values()) {
			if(dataType.equalsIgnoreCase(supportedType.toString())) {
				tDecl.name = supportedType.toString();
				break;
			}
		}
		if(tDecl.name == null) {
			handler.error(new MOFParserException(MOFParserException.INVALID_COMPILER_DIRECTIVE,
					dataType));
			return null;
		}
		
		if(tDecl.name.toString().equals(DataType.REFERENCE.toString())) {
			//Type is a reference, then read data type child to get the referenced class name
			if(dataTypeTree.getChildCount() > 0) {
				tDecl.refClass = dataTypeTree.getChild(0).toString();
			}
			if(tDecl.refClass == null || tDecl.refClass.equals("")) {
				handler.error(new MOFParserException(MOFParserException.INVALID_CLASS_REF,
						tDecl.refClass));
				return null;
			}
			tDecl.isRef = true;
		}
		else {
			//Type is a class type, then read child to know if it's an array type
			//By default the value type is single sized.
			CommonTree typeChild = null; 
			if(dataTypeTree.getChildCount() > 0) {
				typeChild = (CommonTree)dataTypeTree.getChild(0);
			}
			
			if(typeChild != null && "Array".equalsIgnoreCase(typeChild.toString())) {
				tDecl.isArray = true;
				if(typeChild.getChild(0) != null) {
					//Set array size
					tDecl.arraySize = Integer.parseInt(typeTree.getChild(0).toString());
				}
				else {
					//Value is an array type with unlimited size
					tDecl.arraySize = -1;
				}
			}
		}
		return tDecl;
	}
	private void parseInstanceDeclaration(CommonTree tree, IContentHandler handler) 
			throws MOFParserException {
		handler.startInstanceDeclaration();
		InstDecl iDecl = new InstDecl();
		iDecl.className = tree.getChild(0).toString();
		if(iDecl.className == null || iDecl.className.equals("")) {
			handler.error(new MOFParserException(MOFParserException.INVALID_CLASS_NAME,iDecl.className));
			return;
		}
		CommonTree cTree = (CommonTree)tree.getChild(0);
		CommonTree aliasTree = getChild(cTree, ALIAS);
		iDecl.alias = aliasTree.getChild(0).toString();

		for(int i=0; i < cTree.getChildCount(); i++) {
			CommonTree pTree = (CommonTree)cTree.getChild(i);
			if(pTree.toString().equalsIgnoreCase(PROPERTY)) {
				iDecl.properties.add(processInstProperty(pTree, handler));
			}
		}
		handler.instanceDeclaration(iDecl);
		handler.endInstanceDeclaration();
	}
	
	private InstancePropertyDecl processInstProperty(CommonTree pTree, IContentHandler handler) 
			throws MOFParserException {
		InstancePropertyDecl prop = new InstancePropertyDecl();
		if(pTree.getChild(0) == null) {
			handler.error(new MOFParserException(MOFParserException.INVALID_PROPERTY_NAME, null));
			return null;
		}
		prop.name = pTree.getChild(0).toString();
		if(prop.name == null || prop.name.equals("")) {
			handler.error(new MOFParserException(MOFParserException.INVALID_QUALIFIER_NAME, null));
			return null;
		}

		//Process default value
		CommonTree valueTree = getChild(pTree, VALUE);
		if(valueTree != null) {
			prop.value = processValueTree(valueTree, handler);
			prop.type.arraySize = prop.value.size(); 
			if(prop.type.arraySize > 1) {
				prop.type.isArray = true;
			}
		}
		return prop;
	}

	private List<String> processValueTree(CommonTree valueTree, IContentHandler handler) throws MOFParserException {
		List<String> vals = new ArrayList<String>();
		for(int i=0; i < valueTree.getChildCount(); i++) {
			String strValue = valueTree.getChild(i).toString();
			strValue = cleanupString(strValue);
			if(!strValue.equalsIgnoreCase("null")) { 
				vals.add(strValue);
			}
		}
		if(vals.isEmpty()) {
			return null;
		}
		//MPL-11
		return vals;
	}
	
	private String cleanupString(String in) {
		return in.replaceFirst("^\"", "")
			.replaceFirst("\"$", "")	
			.replaceAll("\\\\'", "'")
			.replaceAll("\\\\\"", "\"")
			.replaceAll("\\\\n", "\n");
	}
	
	/**
	 * Return the child subtree, if any, of a tree with then given name
	 * @param root tree to find child in.
	 * @param childName name of the child sub tree to find.
	 * @return the child tree found , or null if nothing found.
	 */
	private CommonTree getChild(CommonTree root, String childName) {
		for(int i=0; i < root.getChildCount(); i++) {
			if(childName.equalsIgnoreCase(root.getChild(i).toString())) {
				return (CommonTree)root.getChild(i);
			}
		}
		return null;
	}

	private String removeQuotes(String str) {
		return str.replaceFirst("^\"", "").replaceFirst("\"$", "");
	}

	/**
	 * Get the flavor type with the given name.
	 * The name is supposed to be in MOF syntax
	 * @param flavorName flavor name to find
	 * @return
	 */
	public FlavorType getFlavorType(String typeName) {
		if(typeName == null) {
			return null;
		}
		
		for(FlavorType type : FlavorType.values()) {
			if(typeName.equalsIgnoreCase(type.toString())) {
				return type;
			}
		}
		return null;
	}

    
	/**
	 * Get the scope type with the given name.
	 * The name is supposed to be in MOF syntax
	 * @param scopeName name of the scope to find
	 * @return
	 */
	public ScopeType getScopeType(String typeName) {
		if(typeName == null) {
			return null;
		}
		
		for(ScopeType type : ScopeType.values()) {
			if(typeName.equalsIgnoreCase(type.toString())) {
				return type;
			}
		}
		return null;
	}
	
	/**
	 * Generate MOF header comment
	 * @return a String containing generator comments
	 */
	public String generateHeader() {
		StringTemplateGroup group = new StringTemplateGroup("group");
		StringTemplate template = group.getInstanceOf("org/escapek/mofparser/internal/templates/header");
		template.setAttribute("date", new Date());
		return template.toString();
	}
	
	/**
	 * Generate a string representing a value declaration.
	 * @param values List of values
	 * @param type expected value type
	 * @return generated string content
	 */
	public String generateValue(List<String> values, TypeDecl type) {
		StringTemplate template=null;
		if(values == null || values.isEmpty()) {
			return "null";
		}
		if(type == null) {
			type = new TypeDecl();
			type.name = DataType.STRING.toString();
			String value = new String();
			for(String v : values) {
				value += v;
				template = new StringTemplate("\"$value$\"");
			}
			template.setAttribute("value", value);
			String ret = template.toString();
			return ret.replaceAll("'", "\\'")
				.replaceAll("\\\"", "\\\"");
		}

		List<String> tmpVal = new ArrayList<String>(values);
		values.clear();
		for(String s : tmpVal) {
			values.add(s.replaceAll("'", "\\'")
			.replaceAll("\\\"", "\\\\\\\""));
		}
		
		if(!type.isArray && !type.isRef) {
			if(DataType.valueOf(type.name.toUpperCase()) == DataType.STRING) {
				template = new StringTemplate("\"$value$\"");
			}
			else {
				template = new StringTemplate("$value$");
			}
			template.setAttribute("value", values.get(0));
		}
		if(type.isArray) { 
			if(DataType.valueOf(type.name) == DataType.STRING) {
				template = new StringTemplate("{\"$value; separator=\"\\\", \\\"\"$\"}");
			}
			else {
				template = new StringTemplate("{$value; separator=, $}");
			}
			template.setAttribute("value", values);
		}
		if(type.isRef) { 
			template = new StringTemplate("$value$");
			template.setAttribute("value", type.refClass);
		}
		return template.toString();
	}

	/**
	 * Generate MOF corresponding to a compiler directive declaration
	 * @param decl compiler directive to generate
	 * @return generated mof.
	 */
	public String generateMOF(PragmaDecl decl) {
		StringTemplateGroup group = new StringTemplateGroup("group");
		StringTemplate template = group.getInstanceOf("org/escapek/mofparser/internal/templates/pragmaDecl");
		template.setAttribute("directive", decl.directive.toString());
		template.setAttribute("param", decl.parameter);
		template.setAttribute("header", generateHeader());
		return template.toString();
	}

	/**
	 * Generate MOF corresponding to a qualifier declaration
	 * @param decl qualifier declaration to generate
	 * @return generated mof.
	 */
	public String generateMOF(QualifierDecl decl) {
		StringTemplateGroup group = new StringTemplateGroup("group");
		StringTemplate template = group.getInstanceOf("org/escapek/mofparser/internal/templates/qualifierDecl");
		template.setAttribute("header", generateHeader());
		template.setAttribute("qualifierName", decl.name);
		template.setAttribute("dataType", decl.type.name);
		template.setAttribute("isarray", decl.type.isArray);
		String defaultValue = null;
		defaultValue = generateValue(decl.defaultValue, decl.type);
		if(defaultValue != null) {
			template.setAttribute("defaultValue", defaultValue);
		}
		template.setAttribute("scopes", decl.scopes);
		template.setAttribute("flavors", decl.flavors);
		if(decl.type.arraySize > 0) {
			template.setAttribute("array", decl.type.arraySize);
		}
		
		return template.toString();
	}
	
	/**
	 * Generate MOF corresponding to a class declaration
	 * @param decl class declaration to generate
	 * @return generated mof.
	 */
	public String generateMOF(ClassDecl decl) {
		StringTemplateGroup group = new StringTemplateGroup("group");
		StringTemplate template = group.getInstanceOf("org/escapek/mofparser/internal/templates/classDecl");
		template.setAttribute("header", generateHeader());
		List qualifiers = new ArrayList();
		for(Qualifier q : decl.qualifiers) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("name", q.name);
			String value = generateValue(q.value, q.type);
			if(!value.equalsIgnoreCase("null")) {
				map.put("value", value);
			}
			qualifiers.add(map);
		}
		template.setAttribute("qualifiers", qualifiers);
		template.setAttribute("className", decl.name);
		template.setAttribute("parentClass", decl.parentClass);

		List properties = new ArrayList();
		for(PropertyDecl p : decl.properties) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", p.name);
			map.put("type", p.type.name);
			
			List pq = new ArrayList();
			for(Qualifier q : p.qualifiers) {
				HashMap<String, String> qm = new HashMap<String, String>();
				qm.put("qname", q.name);
				String value = generateValue(q.value, q.type);
				if(!value.equalsIgnoreCase("null")) {
					qm.put("qvalue", value);
				}
				pq.add(qm);
			}
			map.put("qualifiers", pq);
			properties.add(map);
		}

		template.setAttribute("properties", properties);
		
		return template.toString();
	}
	
	/**
	 * Generate MOF corresponding to a class declaration
	 * @param decl class declaration to generate
	 * @return generated mof.
	 */
	public String generateMOF(InstDecl decl) {
		StringTemplateGroup group = new StringTemplateGroup("group");
		StringTemplate template = group.getInstanceOf("org/escapek/mofparser/internal/templates/instDecl");
		template.setAttribute("header", generateHeader());
		template.setAttribute("className", decl.className);
		template.setAttribute("alias", decl.alias);

		List properties = new ArrayList();
		for(InstancePropertyDecl p : decl.properties) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", p.name);
			String value = generateValue(p.value, p.type);
			if(!value.equalsIgnoreCase("null")) {
				map.put("value", value);
			}
			properties.add(map);
		}
		template.setAttribute("properties", properties);
		return template.toString();
	}
}
