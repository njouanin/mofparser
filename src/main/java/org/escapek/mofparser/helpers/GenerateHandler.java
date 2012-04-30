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

import org.escapek.mofparser.MOFParser;
import org.escapek.mofparser.decl.ClassDecl;
import org.escapek.mofparser.decl.InstDecl;
import org.escapek.mofparser.decl.PragmaDecl;

public class GenerateHandler extends DefaultHandler {
	private MOFParser parser = new MOFParser();
	
	@Override
	public void classDeclaration(ClassDecl decl) {
		super.classDeclaration(decl);
		System.out.println(parser.generateMOF(decl));
	}

	@Override
	public void compilerDirective(PragmaDecl decl) {
		super.compilerDirective(decl);
		System.out.println(parser.generateMOF(decl));
	}

	@Override
	public void instanceDeclaration(InstDecl decl) {
		super.instanceDeclaration(decl);
		System.out.println(parser.generateMOF(decl));
	}
}