package net.sf.yal10n.report;

/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.text.StringEscapeUtils;

/**
 * Exposes {@link StringEscapeUtils#escapeHtml4(String)} to be used in VM templates.
 */
public class EscapeTool
{

    /**
     * Escapes the characters in a String using HTML entities.
     *
     * @param s the string to escape
     * @return the escaped string
     * @see StringEscapeUtils#escapeHtml4(String)
     */
    public String html( String s )
    {
        return StringEscapeUtils.escapeHtml4( s );
    }
}
