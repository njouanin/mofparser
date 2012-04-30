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

/**
 * Enumerates supported flavors. See <a href="http://www.dmtf.org/standards/published_documents/DSP0004V2.3_final.pdf">
 * CIM specifications</a> for details.
 * @author nico
 *
 */
public enum FlavorType {
	/**
	 * this qualifier is overridable
	 */
	ENABLEOVERRIDE,

	/**
	 * this qualifier cannot be overridden
	 */
	DISABLEOVERRIDE,

	/**
	 * this qualifier applies only to the class in which it is declared
	 */
	RESTRICTED,

	/**
	 * this qualifier is inherited by any subclass
	 */
	TOSUBCLASS,
	
	/**
	 * this qualifier can be specified in multiple locales (language and country
	 * combination).
	 */
	TRANSLATE;
}
