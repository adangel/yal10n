package net.sf.yal10n.dashboard;

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

import java.util.Comparator;

/**
 * Comparators used to sort languages.
 */
public enum LanguageComparator implements Comparator<String>
{
    /**
     * Sorts the languages in alphabetical order.
     * The variants are near their language.
     */
    ALPHABETICAL
    {
        @Override
        public int compare( String o1, String o2 )
        {
            return o1.compareToIgnoreCase( o2 );
        }
    },

    /**
     * Sorts the languages in alphabetical order, but
     * put all variants after the main languages.
     */
    ALPHABETICAL_VARIANTS_LAST
    {
        @Override
        public int compare( String o1, String o2 )
        {
            if ( o1.contains( "_" ) && o2.contains( "_" )
                || !o1.contains( "_" ) && !o2.contains(  "_" ) )
            {
                return o1.compareToIgnoreCase( o2 );
            }
            else if ( o1.contains(  "_" ) && !o2.contains( "_" ) )
            {
                return 1;
            }
            else if ( !o1.contains( "_" ) && o2.contains( "_" ) )
            {
                return -1;
            }
            else
            {
                return o1.compareToIgnoreCase( o2 );
            }
        }
    }
}
