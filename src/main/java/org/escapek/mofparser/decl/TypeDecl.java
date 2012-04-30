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


/**
 * Stores data read from a type declaration.
 * Type declaration are used in class property declaration, method parameters and Qualifiers declaration.
 * @author nico
 *
 */
public class TypeDecl {
	/**
	 * CIM data type
	 */
	public String name;
	
	/**
	 * true is the data type is a reference to another class. False if it is a classic type.
	 */
	public boolean isRef;
	
	/**
	 * True is the data type is an array. False otherwise.
	 */
	public boolean isArray;
	
	/**
	 * If the data type is a reference, this field stores the referenced class name
	 */
	public String refClass;
	
	/**
	 * Size of the array if type is an array. <code>-1</code> means unlimited size.
	 */
	public int arraySize;

	public TypeDecl() {
		refClass = null;
		isArray = false;
		isRef = false;
	}
	
	public TypeDecl(String typeName) {
		this();
		this.name = typeName;
	}
}
