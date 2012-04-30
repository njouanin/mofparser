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
import java.util.List;

import org.escapek.mofparser.DataType;

public class InstancePropertyDecl {
	/**
	 * Name of the property
	 */
	public String name;

	/**
	 * Property value
	 */
	public List<String> value;

	/**
	 * Type declaration of the property
	 */
	public TypeDecl type;

	public InstancePropertyDecl() {
		value = new ArrayList<String>();
		type = new TypeDecl();
		type.name = DataType.STRING.toString();
	}
}
