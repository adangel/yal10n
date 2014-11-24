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
import java.util.List;

/**
 * Comparators used to sort languages.
 */
public enum LanguageComparator
{
    /**
     * Sorts the languages in alphabetical order.
     * The variants are near their language.
     */
    ALPHABETICAL
    {
        @Override
        public Comparator<String> build( final List<String> variants )
        {
            return new Comparator<String>()
            {
                @Override
                public int compare( String o1, String o2 )
                {
                    return o1.compareToIgnoreCase( o2 );
                }
            };
        }
    },

    /**
     * Sorts the languages in alphabetical order, but
     * put all variants after the main languages.
     */
    ALPHABETICAL_VARIANTS_LAST
    {
        @Override
        public Comparator<String> build( final List<String> variants )
        {
            return new Comparator<String>()
            {
                @Override
                public int compare( String o1, String o2 )
                {
                    if ( variants.contains( o1 ) && variants.contains( o2 ) )
                    {
                        return o1.compareToIgnoreCase( o2 );
                    }
                    if ( variants.contains( o1 ) && !variants.contains( o2 ) )
                    {
                        if ( o2.contains( "_" ) )
                        {
                            return -1;
                        }
                        else
                        {
                            return o1.compareToIgnoreCase( o2 );
                        }
                    }
                    if ( !variants.contains( o1 ) && variants.contains( o2 ) )
                    {
                        if ( o1.contains( "_" ) )
                        {
                            return 1;
                        }
                        else
                        {
                            return o1.compareToIgnoreCase( o2 );
                        }
                    }

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

                    return o1.compareToIgnoreCase( o2 );
                }
            };
        }
    };

    /**
     * Builds a comparator, that may take the given list of variants
     * into account. This variants might be sorted as usual languages,
     * before other variants.
     * @param variants list of variants
     * @return a comparator
     * @see #ALPHABETICAL_VARIANTS_LAST
     */
    public abstract Comparator<String> build( List<String> variants );
}
