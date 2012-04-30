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
 * Stores data read from a class property declaration.
 * @author nico
 *
 */
public class PropertyDecl {
	/**
	 * Name of the property
	 */
	public String name;
	
	/**
	 * Type declaration of the property
	 */
	public TypeDecl type;
	
	/**
	 * Property defaultValue
	 */
	public List<String> value;

	/**
	 * List of qualifiers associated with the property declaration
	 */
	public Set<Qualifier> qualifiers;
	
	public PropertyDecl() {
		qualifiers = new HashSet<Qualifier>();
		value = new ArrayList<String>();
	}

	public PropertyDecl(String propertyName) {
		this();
		this.name = propertyName;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if((obj == null) || (obj.getClass() != this.getClass()))
			return false;
			
		return name.equalsIgnoreCase(((PropertyDecl) obj).name);
	}

	@Override
	public int hashCode() {
		if(name != null) {
			return name.hashCode();
		}
		return super.hashCode();
	}
}
