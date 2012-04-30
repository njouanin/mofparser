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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Stores data read from a qualifier type declaration.
 * @author nico
 *
 */
public class QualifierDecl {
	/**
	 * Name of the qualifier type.
	 */
	public String name;
	
	/**
	 * Type declaration of the qualifier value
	 */
	public TypeDecl type;
	
	/**
	 * Default qualifier value
	 */
	public List<String> defaultValue;
	
	/**
	 * List of scopes available for this qualifier declaration
	 */
	public Set<String> scopes;
	
	/**
	 * List of flavors available for this qualifier declaration
	 */
	public Set<String> flavors;
	
	public QualifierDecl() {
		scopes = new HashSet<String>();
		flavors = new HashSet<String>();
		defaultValue = new ArrayList<String>();
	}
}
