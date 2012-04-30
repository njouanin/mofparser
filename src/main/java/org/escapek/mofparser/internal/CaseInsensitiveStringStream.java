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
package org.escapek.mofparser.internal;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;


/**
 * ANTLR Stream which provides case insensitivity.
 * @author nico
 *
 */
public class CaseInsensitiveStringStream extends ANTLRStringStream {

	public CaseInsensitiveStringStream(String arg0) {
		super(arg0);
	}

	@Override
	public int LA(int i) {
		if (i == 0) {
            return 0;
        }

        if (i < 0) {
            i++;
        }

        if (((p + i) - 1) >= n) {
            return (int) CharStream.EOF;
        }

        return Character.toLowerCase(data[(p + i) - 1]);
	}

}
