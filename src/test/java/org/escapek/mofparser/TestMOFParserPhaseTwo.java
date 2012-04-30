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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.escapek.mofparser.decl.ClassDecl;
import org.escapek.mofparser.decl.PragmaDecl;
import org.escapek.mofparser.decl.PropertyDecl;
import org.escapek.mofparser.decl.Qualifier;
import org.escapek.mofparser.exceptions.MOFParserException;
import org.escapek.mofparser.helpers.DefaultHandler;
import org.junit.Before;
import org.junit.Test;

public class TestMOFParserPhaseTwo {
	private MOFParser parser;
	
	@Before public void setup() {
		parser = new MOFParser();
		assertNotNull(parser);
	}
	
	@Test public void testPragmaInclude() throws MOFParserException {
		String content = "#pragma include (\"qualifiers.mof\")";
		DefaultHandler testHandler = new DefaultHandler();
		parser.parse(content, testHandler);
		List<PragmaDecl> pragmas = testHandler.getPragmas();
		assertEquals(1, pragmas.size());
		PragmaDecl pDecl = pragmas.get(0);
		assertTrue(pDecl.directive.equals(CompilerDirective.include));
		assertTrue(pDecl.parameter.equalsIgnoreCase("qualifiers.mof"));
	}

	@Test public void testPragmaInstanceLocale() throws MOFParserException {
		String content = "#pragma instancelocale (\"en_US\")";
		DefaultHandler testHandler = new DefaultHandler();
		parser.parse(content, testHandler);
		List<PragmaDecl> pragmas = testHandler.getPragmas();
		assertEquals(1, pragmas.size());
		PragmaDecl pDecl = pragmas.get(0);
		assertTrue(pDecl.directive.equals(CompilerDirective.instancelocale));
		assertTrue(pDecl.parameter.equalsIgnoreCase("en_US"));
	}

	@Test public void testPragmaLocale() throws MOFParserException {
		String content = "#pragma locale (\"en_US\")";
		DefaultHandler testHandler = new DefaultHandler();
		parser.parse(content, testHandler);
		List<PragmaDecl> pragmas = testHandler.getPragmas();
		assertEquals(1, pragmas.size());
		PragmaDecl pDecl = pragmas.get(0);
		assertTrue(pDecl.directive.equals(CompilerDirective.locale));
		assertTrue(pDecl.parameter.equalsIgnoreCase("en_US"));
	}

	@Test public void testPragmaNamespace() throws MOFParserException {
		String content = "#pragma namespace(\"//./root/CIMV2\")";
		DefaultHandler testHandler = new DefaultHandler();
		parser.parse(content, testHandler);
		List<PragmaDecl> pragmas = testHandler.getPragmas();
		assertEquals(1, pragmas.size());
		PragmaDecl pDecl = pragmas.get(0);
		assertTrue(pDecl.directive.equals(CompilerDirective.namespace));
		assertTrue(pDecl.parameter.equalsIgnoreCase("//./root/CIMV2"));
	}
	
	@Test public void testParseManagedElement() throws FileNotFoundException, IOException, MOFParserException {
		InputStream is = this.getClass().getResourceAsStream("CIM_ManagedElement.mof" );
		DefaultHandler testHandler = new DefaultHandler();
		parser.parse(is, testHandler);
		List<ClassDecl> classes = testHandler.getClasses();
		assertEquals(1, classes.size());
		ClassDecl cDecl = classes.get(0);
		assertTrue(cDecl.name.equalsIgnoreCase("CIM_ManagedElement"));
		assertTrue(cDecl.qualifiers.contains(new Qualifier("Abstract")));
		assertTrue(cDecl.qualifiers.contains(new Qualifier("UMLPackagePath")));
		assertTrue(cDecl.qualifiers.contains(new Qualifier("Description")));

		assertTrue(cDecl.properties.contains(new PropertyDecl("Caption")));
		assertTrue(cDecl.properties.contains(new PropertyDecl("Description")));
	}
}
