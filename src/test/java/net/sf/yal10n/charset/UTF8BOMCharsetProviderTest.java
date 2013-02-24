package net.sf.yal10n.charset;

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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Unit test for {@link UTF8BOMCharsetProvider}.
 */
public class UTF8BOMCharsetProviderTest
{

    /**
     * Tests the charset for name lookup.
     */
    @Test
    public void testCharsetForName()
    {
        UTF8BOMCharsetProvider provider = new UTF8BOMCharsetProvider();
        Assert.assertNull( provider.charsetForName( "Foo" ) );
        Assert.assertTrue( provider.charsetForName( "UTF-8-BOM" ) instanceof UTF8BOM );
    }

    /**
     * Tests the charset iterator.
     */
    @Test
    public void testCharsetIterator()
    {
        UTF8BOMCharsetProvider provider = new UTF8BOMCharsetProvider();
        Iterator<Charset> charsets = provider.charsets();
        List<Charset> collected = new ArrayList<Charset>();
        while ( charsets.hasNext() )
        {
            collected.add( charsets.next() );
        }

        Assert.assertEquals( 1, collected.size() );
        Assert.assertTrue( collected.get( 0 ) instanceof UTF8BOM );
    }
}
