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
package org.escapek.mofparser.decl;

import java.util.HashSet;
import java.util.Set;

/**
 * Stores data read from a class declaration.
 * @author nico
 *
 */
public class ClassDecl {
	/**
	 * Name of the class
	 */
	public String name;
	
	/**
	 * parent class name. May be null for top level class
	 */
	public String parentClass;
	
	/**
	 * List of class qualifiers found.
	 */
	public Set<Qualifier> qualifiers;
	
	/**
	 * List of class properties.
	 */
	public Set<PropertyDecl> properties;
	
	public ClassDecl() {
		qualifiers = new HashSet<Qualifier>();
		properties = new HashSet<PropertyDecl>();
	}
}
