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
import java.nio.charset.spi.CharsetProvider;
import java.util.Iterator;

/**
 * Implements the charset provider for UTF-8 with BOM.
 * @see UTF8BOM
 */
public class UTF8BOMCharsetProvider extends CharsetProvider
{

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Charset> charsets()
    {
        return new Iterator<Charset>()
        {
            boolean next = true;

            @Override
            public boolean hasNext()
            {
                return next;
            }

            @Override
            public Charset next()
            {
                next = false;
                return new UTF8BOM();
            }

            @Override
            public void remove()
            {
                throw new UnsupportedOperationException();
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Charset charsetForName( String charsetName )
    {
        if ( UTF8BOM.NAME.equalsIgnoreCase( charsetName ) )
        {
            return new UTF8BOM();
        }
        return null;
    }
}
