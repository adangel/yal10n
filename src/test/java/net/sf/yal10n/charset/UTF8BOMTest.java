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

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Unit test for {@link UTF8BOM}.
 */
public class UTF8BOMTest
{

    /**
     * Tests the contains method.
     */
    @Test
    public void testContains()
    {
        UTF8BOM charset = new UTF8BOM();
        Assert.assertTrue( charset.contains( Charset.forName( "ISO-8859-1" ) ) );
    }

    final int bomByte1 = 0xef;
    final int bomByte2 = 0xbb;
    final int bomByte3 = 0xbf;
    final int byteMask = 0xff;
    
    /**
     * Tests the charset with short strings.
     * @throws Exception any error
     */
    @Test
    public void testCharsetShortString() throws Exception
    {

        Charset charset = Charset.forName( "UTF-8-BOM" );
        Assert.assertNotNull( charset );

        byte[] bytes = "A".getBytes( "UTF-8-BOM" );
        Assert.assertEquals( 4, bytes.length );
        Assert.assertEquals( bomByte1, bytes[0] & byteMask );
        Assert.assertEquals( bomByte2, bytes[1] & byteMask );
        Assert.assertEquals( bomByte3, bytes[2] & byteMask );
        Assert.assertEquals( 'A', bytes[3] & byteMask );

        String s = new String( bytes, "UTF-8-BOM" );
        Assert.assertEquals( "A", s );

        s = new String( bytes, 3, 1, "UTF-8-BOM" );
        Assert.assertEquals( "A", s );
    }

    /**
     * Tests the charset without a BOM.
     * @throws Exception any error
     */
    @Test
    public void testWithoutBOM() throws Exception
    {
        byte[] bytes = "test=test".getBytes( "UTF-8" );
        String s = new String( bytes, "UTF-8-BOM" );
        Assert.assertEquals( "test=test", s );
    }

    /**
     * Tests the charset to deal with errors.
     * @throws Exception any error
     */
    @Test
    public void testEncodingProblem() throws Exception
    {
        byte[] bytes = "test=malformed äöü encoding".getBytes( "ISO-8859-1" );
        String s = new String( bytes, "UTF-8-BOM" );
        Assert.assertEquals( "test=malformed ��� encoding", s );
    }

    /**
     * Tests the charset to deal with errors at the beginning.
     * @throws Exception any error
     */
    @Test
    public void testEncodingProblemFirstByte() throws Exception
    {
        byte[] bytes = "äöüaaaaaa".getBytes( "ISO-8859-1" );
        String s = new String( bytes, "UTF-8-BOM" );
        Assert.assertEquals( "��aaaaaa", s );
    }
    
    /**
     * Tests that the BOM is actually written, even if the buffer overflows.
     * @throws Exception any error
     */
    @Test
    public void testEncoderOverflow() throws Exception
    {
        CharsetEncoder encoder = new UTF8BOM().newEncoder();
        CharBuffer in = CharBuffer.wrap( "Test encoder" );
        ByteBuffer smallBuffer = ByteBuffer.allocate( 2 );
        CoderResult result = encoder.encode( in, smallBuffer, false );
        Assert.assertFalse( result.isError() );
        Assert.assertTrue( result.isOverflow() );
        Assert.assertEquals( 0, smallBuffer.position() );
        
        ByteBuffer bigBuffer = ByteBuffer.allocate( in.capacity() + 3 );
        result = encoder.encode( in, bigBuffer, true );
        Assert.assertFalse( result.isError() );
        Assert.assertFalse( result.isOverflow() );
        Assert.assertEquals( bomByte1, bigBuffer.get( 0 ) & byteMask );
        Assert.assertEquals( bomByte2, bigBuffer.get( 1 ) & byteMask );
        Assert.assertEquals( bomByte3, bigBuffer.get( 2 ) & byteMask );
        Assert.assertEquals( 'T', bigBuffer.get( 3 ) & byteMask );
    }
}
