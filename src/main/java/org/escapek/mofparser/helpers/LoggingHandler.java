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
package org.escapek.mofparser.helpers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.escapek.mofparser.IContentHandler;
import org.escapek.mofparser.Production;
import org.escapek.mofparser.decl.ClassDecl;
import org.escapek.mofparser.decl.InstDecl;
import org.escapek.mofparser.decl.PragmaDecl;
import org.escapek.mofparser.decl.QualifierDecl;
import org.escapek.mofparser.exceptions.MOFParserException;

/**
 * MOF event handler implementation.
 * This implemntation uses logging facility to dump intercepted events.
 * It should'nt be used in production, but only for debugging and testing.
 * @author nico
 *
 */
public class LoggingHandler implements IContentHandler {
	private String sp = null;
	private static Log logger = LogFactory.getLog(LoggingHandler.class);

	public LoggingHandler() {
		sp = new String();
	}
	
	private void incSp() {
		sp +=" ";
	}
	
	private void decSp() {
		if(sp.length() > 0) {
			sp = sp.substring(0, sp.length() - 1);
		}
		else sp = "";
	}
	
	public void compilerDirective(PragmaDecl decl) {
		logger.info(sp+" pragma: name='" + decl.directive + "' parameter='" + decl.parameter + "'");
	}

	public void endCompilerDirective() {
		decSp();
		logger.info(sp+"END compilerDirective");
	}

	public void endDocument() {
		decSp();
		logger.info(sp+"END document");
	}

	public void endProduction() {
		decSp();
		logger.info(sp+"END production");
	}

	public void error(MOFParserException ex) throws MOFParserException {
		logger.error(sp+" error: message=" + ex.getMessage(), ex);
	}

	public void startCompilerDirective() {
		logger.info(sp+"START compilerDirective");
		incSp();
	}

	public void startDocument() {
		logger.info(sp+"START document");
		incSp();
	}

	public void startProduction(Production productionType) {
		logger.info(sp+"START production");
		incSp();
	}

	public void endQualifierDeclaration() {
		decSp();
		logger.info(sp+"END qualifierDeclaration");
	}

	public void qualifierDeclaration(QualifierDecl decl) {
		logger.info(sp+" qualifierDeclaration: name='" + decl.name + "'");
		logger.info(sp+" qualifierDeclaration: type='" + decl.type.name + "'");
		logger.info(sp+" qualifierDeclaration: refClass='" + decl.type.refClass + "'");
		logger.info(sp+" qualifierDeclaration: arraySize='" + decl.type.arraySize + "'");
		logger.info(sp+" qualifierDeclaration: isArray='" + decl.type.isArray + "'");
		logger.info(sp+" qualifierDeclaration: isRef='" + decl.type.isRef + "'");
		logger.info(sp+" qualifierDeclaration: defaultValue='" + decl.defaultValue + "'");
	}

	public void startQualifierDeclaration() {
		logger.info(sp+"START qualifierDeclaration");
		incSp();
	}

	public void classDeclaration(ClassDecl decl) {
		logger.info(sp+" classDeclaration: name='" + decl.name + "'");
		logger.info(sp+" classDeclaration: parentClass='" + decl.parentClass + "'");
		logger.info(sp+" classDeclaration: qualifierCount='" + decl.qualifiers.size() + "'");
		logger.info(sp+" classDeclaration: propertyCount='" + decl.properties.size() + "'");
	}

	public void endClassDeclaration() {
		decSp();
		logger.info(sp+"END classDeclaration");
	}

	public void startClassDeclaration() {
		logger.info(sp+"START classDeclaration");
		incSp();
	}

	public void include(String name) {
		logger.info(sp+"Need included file '" + name + "'");
	}

	public void endInstanceDeclaration() throws MOFParserException {
		decSp();
		logger.info(sp+"END instanceDeclaration");
	}

	public void instanceDeclaration(InstDecl decl) throws MOFParserException {
		logger.info(sp+" instanceDeclaration: className='" + decl.className + "'");
		logger.info(sp+" instanceDeclaration: alias='" + decl.alias + "'");
	}

	public void startInstanceDeclaration() throws MOFParserException {
		logger.info(sp+"START instanceDeclaration");
		incSp();
	}
}
