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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Unit test for {@link LanguageComparator}.
 */
public class LanguageComparatorTest
{
    private List<String> lang = Arrays.asList( "en", "de_DE", "de", "en_US" );

    /**
     * Tests the standard sorting.
     */
    @Test
    public void testAlphabetical()
    {
        Collections.sort( lang, LanguageComparator.ALPHABETICAL );
        Assert.assertEquals( "[de, de_DE, en, en_US]", lang.toString() );
    }

    /**
     * Tests the sorting to have the variantes at the end.
     */
    @Test
    public void testVariantsLast()
    {
        Assert.assertTrue(
                LanguageComparator.ALPHABETICAL_VARIANTS_LAST.compare( "de", "de" )
                == 0 );
        Assert.assertTrue(
                LanguageComparator.ALPHABETICAL_VARIANTS_LAST.compare( "de", "de_DE" )
                < 0 );
        Assert.assertTrue(
                LanguageComparator.ALPHABETICAL_VARIANTS_LAST.compare( "de_DE", "de" )
                > 0 );
        Assert.assertTrue(
                LanguageComparator.ALPHABETICAL_VARIANTS_LAST.compare( "en_US", "de_DE" )
                > 0 );
        Assert.assertTrue(
                LanguageComparator.ALPHABETICAL_VARIANTS_LAST.compare( "de", "en" )
                < 0 );

        Collections.sort( lang, LanguageComparator.ALPHABETICAL_VARIANTS_LAST );
        Assert.assertEquals( "[de, en, de_DE, en_US]", lang.toString() );
    }
}
