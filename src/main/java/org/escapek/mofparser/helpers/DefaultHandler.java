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

import java.util.ArrayList;
import java.util.List;

import org.escapek.mofparser.IContentHandler;
import org.escapek.mofparser.Production;
import org.escapek.mofparser.decl.ClassDecl;
import org.escapek.mofparser.decl.InstDecl;
import org.escapek.mofparser.decl.PragmaDecl;
import org.escapek.mofparser.decl.QualifierDecl;
import org.escapek.mofparser.exceptions.MOFParserException;

/**
 * A default mof parser event handler implementation.
 * This implementation does nothing , but it can be used and subclassed.
 * Parsed elements ares stored as class objects. This can be used for querying parsed elements afterwards
 * @author nico
 *
 */
public class DefaultHandler implements IContentHandler {
	private List<PragmaDecl> pragmas;
	private List<QualifierDecl> qualifiers;
	private List<ClassDecl> classes;
	private List<InstDecl> instances;
	
	public DefaultHandler() {
		pragmas = new ArrayList<PragmaDecl>();
		qualifiers = new ArrayList<QualifierDecl>();
		classes = new ArrayList<ClassDecl>();
		instances = new ArrayList<InstDecl>();
	}

	public void endCompilerDirective() {
	}

	public void endDocument() {
	}

	public void endProduction() {
	}

	public void error(MOFParserException ex) throws MOFParserException {
		//MPL-7
		throw ex;
	}

	public void startCompilerDirective() {
	}

	public void startDocument() {
	}

	public void startProduction(Production productionType) {
	}

	public void compilerDirective(PragmaDecl decl) {
		pragmas.add(decl);
	}

	public void endQualifierDeclaration() {
	}

	public void qualifierDeclaration(QualifierDecl decl) {
		qualifiers.add(decl);
	}

	public void startQualifierDeclaration() {
	}

	public void classDeclaration(ClassDecl decl) {
		classes.add(decl);
	}

	public void endClassDeclaration() {
	}

	public void startClassDeclaration() {
	}

	public void include(String name) {
	}

	public void endInstanceDeclaration() {
	}

	public void instanceDeclaration(InstDecl decl) {
		instances.add(decl);
	}

	public void startInstanceDeclaration() {
	}

	public List<PragmaDecl> getPragmas() {
		return pragmas;
	}

	public void setPragmas(List<PragmaDecl> pragmas) {
		this.pragmas = pragmas;
	}

	public List<QualifierDecl> getQualifiers() {
		return qualifiers;
	}

	public void setQualifiers(List<QualifierDecl> qualifiers) {
		this.qualifiers = qualifiers;
	}

	public List<ClassDecl> getClasses() {
		return classes;
	}

	public void setClasses(List<ClassDecl> classes) {
		this.classes = classes;
	}

	public List<InstDecl> getInstances() {
		return instances;
	}

	public void setInstances(List<InstDecl> instances) {
		this.instances = instances;
	}
}
