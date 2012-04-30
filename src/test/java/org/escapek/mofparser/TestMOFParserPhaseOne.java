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

import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;

import org.escapek.mofparser.decl.InstDecl;
import org.escapek.mofparser.decl.PragmaDecl;
import org.escapek.mofparser.decl.QualifierDecl;
import org.escapek.mofparser.decl.TypeDecl;
import org.escapek.mofparser.exceptions.MOFParserException;
import org.escapek.mofparser.helpers.DefaultHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestMOFParserPhaseOne {
	private MOFParser parser;
	
	@Before public void setup() {
		parser = new MOFParser();
		assertNotNull(parser);
	}
	
	@Test public void testCompilerDirective() throws MOFParserException {
		String content = "#pragma include (\"qualifiers.mof\")";
		DefaultHandler testHandler = new DefaultHandler();
		parser.parse(content, testHandler);
	}

	@Test public void testCompilerDirectives() throws MOFParserException {
		String content = "#pragma include (\"qualifiers.mof\")\n#pragma include (\"test.mof\")";
		DefaultHandler testHandler = new DefaultHandler();
		parser.parse(content, testHandler);
	}

	@Test public void testParseManagedElement() throws FileNotFoundException, IOException, MOFParserException {
		InputStream is = this.getClass().getResourceAsStream("CIM_ManagedElement.mof" );
		DefaultHandler testHandler = new DefaultHandler();
		parser.parse(is, testHandler);
	}
	
	@Test public void testParseQualifiers() throws FileNotFoundException, IOException, MOFParserException {
		InputStream is = this.getClass().getResourceAsStream("qualifiers.mof" );
		DefaultHandler testHandler = new DefaultHandler();
		parser.parse(is, testHandler);
	}

	@Test
	public void testParseInstance() throws Exception {
		String content = new String();
		content = "instance of Acme_LogicalDisk as $Disk\n" + 
			"{\nDriveLetter = \"C\";\nVolumeLabel = \"myvol\";\nDependent = \"CIM_Service.Name = \\\"mail\\\"\";" + 
			"ip_addresses = { \"1.2.3.4\", \"1.2.3.5\", \"1.2.3.7\" };\nobref1 = $Alias1;\n};";
		DefaultHandler testHandler = new DefaultHandler();
		parser.parse(content, testHandler);
	}

	@Test
	public void generatePragma() {
		PragmaDecl decl = new PragmaDecl(CompilerDirective.include, "test.mof");
		String mof = parser.generateMOF(decl);
		Assert.assertNotNull(mof);
	}

	@Test
	public void generateQualifier() {
		TypeDecl tDecl = new TypeDecl();
		tDecl.name = "boolean";
		QualifierDecl decl = new QualifierDecl();
		decl.name = "Association";
		decl.type = tDecl;
		decl.defaultValue = new ArrayList<String>();
		decl.defaultValue.add("false");
		decl.type.isArray = false;
		decl.scopes = new HashSet<String>();
		decl.scopes.add(ScopeType.ANY.toString());
		decl.scopes.add(ScopeType.ASSOCIATION.toString());
		String mof = parser.generateMOF(decl);
		Assert.assertNotNull(mof);
	}

	@Test
	public void generateInstance() throws Exception {
		String content = new String();
		content = "instance of Acme_LogicalDisk as $Disk\n" + 
			"{\nDriveLetter = \"C\";\nVolumeLabel = \"myvol\";\nDependent = \"CIM_Service.Name = \\\"mail\\\"\";" + 
			"ip_addresses = { \"1.2.3.4\", \"1.2.3.5\", \"1.2.3.7\" };\nobref1 = $Alias1;\n};";
		final MOFParser parser = new MOFParser();
		Assert.assertNotNull(parser);
		DefaultHandler testHandler = new DefaultHandler();
		parser.parse(content, testHandler);
	}
}
