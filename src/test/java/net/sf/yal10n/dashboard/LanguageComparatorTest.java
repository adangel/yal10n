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
import java.util.Comparator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link LanguageComparator}.
 */
public class LanguageComparatorTest
{
    private List<String> lang = Arrays.asList( "zh_TW", "zz", "zh_CN", "en", "de_DE", "de", "en_US" );

    /**
     * Tests the standard sorting.
     */
    @Test
    public void testAlphabetical()
    {
        Collections.sort( lang, LanguageComparator.ALPHABETICAL.build( Collections.<String>emptyList() ) );
        Assert.assertEquals( "[de, de_DE, en, en_US, zh_CN, zh_TW, zz]", lang.toString() );
    }

    /**
     * Tests the sorting to have the variantes at the end.
     */
    @Test
    public void testVariantsLast()
    {
        Comparator<String> comparator = LanguageComparator
                .ALPHABETICAL_VARIANTS_LAST.build( Collections.<String>emptyList() );
        Assert.assertTrue( comparator.compare( "de", "de" ) == 0 );
        Assert.assertTrue( comparator.compare( "de", "de_DE" ) < 0 );
        Assert.assertTrue( comparator.compare( "de_DE", "de" ) > 0 );
        Assert.assertTrue( comparator.compare( "en_US", "de_DE" ) > 0 );
        Assert.assertTrue( comparator.compare( "de", "en" ) < 0 );

        Collections.sort( lang, comparator );
        Assert.assertEquals( "[de, en, zz, de_DE, en_US, zh_CN, zh_TW]", lang.toString() );
    }

    /**
     * Tests the sorting to have the variants at the end, but a given list of variants not.
     */
    @Test
    public void testVariantsLastWithIncludes()
    {
        Comparator<String> comparator = LanguageComparator
                .ALPHABETICAL_VARIANTS_LAST.build( Arrays.asList( "zh_CN", "zh_TW" ) );
        Assert.assertTrue( comparator.compare( "de", "de" ) == 0 );
        Assert.assertTrue( comparator.compare( "de", "de_DE" ) < 0 );
        Assert.assertTrue( comparator.compare( "de", "zh_CN" ) < 0 );
        Assert.assertTrue( comparator.compare( "zh_CN", "zh_TW" ) < 0 );
        Assert.assertTrue( comparator.compare( "zh_CN", "de_DE" ) < 0 );
        Assert.assertTrue( comparator.compare( "zh_CN", "en_US" ) < 0 );
        Assert.assertTrue( comparator.compare( "zh_TW", "zh_CN" ) > 0 );
        Assert.assertTrue( comparator.compare( "de_DE", "zh_CN" ) > 0 );
        Assert.assertTrue( comparator.compare( "en_US", "zh_CN" ) > 0 );

        Collections.sort( lang, comparator );
        Assert.assertEquals( "[de, en, zh_CN, zh_TW, zz, de_DE, en_US]", lang.toString() );
    }
}
