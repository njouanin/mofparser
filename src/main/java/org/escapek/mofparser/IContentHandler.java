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

import org.escapek.mofparser.decl.ClassDecl;
import org.escapek.mofparser.decl.InstDecl;
import org.escapek.mofparser.decl.PragmaDecl;
import org.escapek.mofparser.decl.QualifierDecl;
import org.escapek.mofparser.exceptions.MOFParserException;

/**
 * Interface defining methods that must be implemented by a mof parse events.
 * These methods are called by MOFParser when it parses MOF declaration.
 * @author nico
 *
 */
public interface IContentHandler {
	/**
	 * Receive notification of the beginning of MOF document parsing
	 */
	public void startDocument() throws MOFParserException;

	/**
	 * Receive notification of the end of MOF document parsing
	 */
	public void endDocument() throws MOFParserException;
	
	/**
	 * Receive notification of a parse error. This method is responsible
	 * of managing the exception, and eventually throw it back.
	 * @param ex exception thrown by the parser
	 * @throws MOFParserException new exception thrown if needed.
	 */
	public void error(MOFParserException ex) throws MOFParserException;

	/**
	 * Receive notification of the beginning of a production parsing
	 * @param productionType production type being parsed
	 */
	public void startProduction(Production productionType) throws MOFParserException;

	/**
	 * Receive notification of the end of a production parsing.
	 */
	public void endProduction() throws MOFParserException;
	
	/**
	 * Receive notification of the beginning of a compiler directive parsing
	 */
	public void startCompilerDirective() throws MOFParserException;
	
	/**
	 * Receive data of a successfully parsed compiler directive.
	 * @param decl compiler directive parsed
	 */
	public void compilerDirective(PragmaDecl decl) throws MOFParserException;
	
	/**
	 * Receive notification of the end of a compiler directive parsing.
	 * This method can be used to clean space after processing the compiler directive received.
	 */
	public void endCompilerDirective() throws MOFParserException;
	
	/**
	 * Receive notification of the beginning of a qualifier declaration parsing
	 */
	public void startQualifierDeclaration() throws MOFParserException;
	
	/**
	 * Receive data of a successfully parsed qualifier declaration.
	 * @param decl qualifier declaration parsed
	 */
	public void qualifierDeclaration(QualifierDecl decl) throws MOFParserException;
	
	/**
	 * Receive notification of the end of a qualifier declaration parsing.
	 * This method can be used to clean space after processing the qualifier declaration received.
	 */
	public void endQualifierDeclaration() throws MOFParserException;

	/**
	 * Receive notification of the beginning of a class declaration parsing
	 */
	public void startClassDeclaration() throws MOFParserException;

	/**
	 * Receive data of a successfully parsed class declaration.
	 * @param decl class declaration parsed
	 */
	public void classDeclaration(ClassDecl decl) throws MOFParserException;

	/**
	 * Receive notification of the end of a class declaration parsing.
	 * This method can be used to clean space after processing the class declaration received.
	 */
	public void endClassDeclaration() throws MOFParserException;

	/**
	 * Receive notification of the beginning of MOF document parsing
	 */
	public void include(String name);

	/**
	 * Receive notification of the beginning of an instance declaration parsing
	 */
	public void startInstanceDeclaration() throws MOFParserException;

	/**
	 * Receive data of a successfully parsed instance declaration.
	 * @param decl instance declaration parsed
	 */
	public void instanceDeclaration(InstDecl decl) throws MOFParserException;

	/**
	 * Receive notification of the end of an instance declaration parsing.
	 * This method can be used to clean space after processing the instance declaration received.
	 */
	public void endInstanceDeclaration() throws MOFParserException;
}
