package net.sf.yal10n.analyzer;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link SimpleEncodingDetector}.
 */
public class SimpleEncodingDetectorTest
{

    SimpleEncodingDetector detector = new SimpleEncodingDetector();

    /**
     * Sets/Resets the default buffer size.
     */
    @Before
    public void setup()
    {
        detector.setBufferSize( 1024 );
    }

    /**
     * Test to detect UTF8_BOM.
     * @throws Exception any error
     */
    @Test
    public void testUTF8WithBOM() throws Exception
    {
        File f = prepareFile( true, "This is a string with umlauts: äöüß encoded as UTF-8 with BOM", "UTF-8" );
        EncodingResult encodingResult = detector.detectEncoding( f );
        Assert.assertEquals( Encoding.UTF8_BOM, encodingResult.getDetected() );
        Assert.assertNull( encodingResult.getError() );
    }

    /**
     * Test to detect plain UTF-8.
     * @throws Exception any error
     */
    @Test
    public void testUTF8() throws Exception
    {
        File f = prepareFile( false, "This is a string with umlauts: äöüß encoded as UTF-8 without BOM", "UTF-8" );
        EncodingResult encodingResult = detector.detectEncoding( f );
        Assert.assertEquals( Encoding.UTF8, encodingResult.getDetected() );
        Assert.assertNull( encodingResult.getError() );
    }

    /**
     * Test to detect other encoding.
     * @throws Exception any error
     */
    @Test
    public void testOther() throws Exception
    {
        File f = prepareFile( false, "This is a string with umlauts: äöüß encoded as latin-1", "ISO-8859-1" );
        EncodingResult encodingResult = detector.detectEncoding( f );
        Assert.assertEquals( Encoding.OTHER, encodingResult.getDetected() );
        Assert.assertEquals( "MALFORMED[1]", encodingResult.getError() );
        final int expectedErrorPosition = 31;
        Assert.assertEquals( expectedErrorPosition, encodingResult.getErrorPosition() );
    }

    /**
     * Test whether the error position is correctly given.
     * @throws Exception any error
     */
    @Test
    public void testErrors() throws Exception
    {
        File f = prepareFile( false, "This is a file\nwith multiple\nlines äää and umlauts\nbut wrongly encoded",
                "ISO-8859-1" );
        EncodingResult encodingResult = detector.detectEncoding( f );
        Assert.assertEquals( Encoding.OTHER, encodingResult.getDetected() );
        Assert.assertEquals( "MALFORMED[1]", encodingResult.getError() );
        final int expectedErrorPosition = 35;
        Assert.assertEquals( expectedErrorPosition, encodingResult.getErrorPosition() );
        final int expectedLine = 3;
        Assert.assertEquals( expectedLine, encodingResult.getErrorLine() );
        final int expectedColumn = 7;
        Assert.assertEquals( expectedColumn, encodingResult.getErrorColumn() );
    }

    /**
     * Determine the file position of the error if the decoder loop needs
     * to do more than one read - the file size is bigger than the buffer size.
     * @throws Exception any error
     */
    @Test
    public void testErrorsBigFile() throws Exception
    {
        final int fileSize = 1200;
        File f = prepareBigFile( false, "Test File\nwith\nmultiple\nlines\nbut äää wrong encoding",
                "ISO-8859-1", fileSize );
        EncodingResult encodingResult = detector.detectEncoding( f );
        Assert.assertEquals( Encoding.OTHER, encodingResult.getDetected() );
        Assert.assertEquals( "MALFORMED[1]", encodingResult.getError() );
        final int expectedErrorPosition = 34;
        Assert.assertEquals( fileSize + expectedErrorPosition, encodingResult.getErrorPosition() );
        final int expectedLine = 5;
        Assert.assertEquals( expectedLine, encodingResult.getErrorLine() );
        final int expectedColumn = 5;
        Assert.assertEquals( expectedColumn, encodingResult.getErrorColumn() );
    }

    /**
     * Verifies that the encoding is correctly detected, even if a 2-byte encoded character
     * is just at the buffer size.
     * @throws Exception any error
     */
    @Test
    public void testEncodingBufferSizeWrap() throws Exception
    {
        detector.setBufferSize( 5 );
        File f = prepareFile( false, "1234äää", "UTF-8" );
        EncodingResult encodingResult = detector.detectEncoding( f );
        Assert.assertEquals( Encoding.UTF8, encodingResult.getDetected() );
    }

    /**
     * Tests a very short file - the file is shorted than the BOM.
     * @throws Exception any error
     */
    @Test
    public void testVeryShortFile() throws Exception
    {
        File f = prepareFile( false, "a", "ISO-8859-1" );
        EncodingResult encodingResult = detector.detectEncoding( f );
        // note: will detect as UTF-8, as for this 1 byte, it's the same
        Assert.assertEquals( Encoding.UTF8, encodingResult.getDetected() );
        Assert.assertNull( encodingResult.getError() );
    }

    /**
     * Tests a file that seems to start with a BOM.
     * @throws Exception any error
     */
    @Test
    public void testMalformedBOM() throws Exception
    {
        File f = prepareFile( new byte[] { SimpleEncodingDetector.UTF8_BOM_BYTES[0], 'a', 'b', 'c' } );
        EncodingResult encodingResult = detector.detectEncoding( f );
        Assert.assertEquals( Encoding.OTHER, encodingResult.getDetected() );
        Assert.assertEquals( "MALFORMED[1]", encodingResult.getError() );
        Assert.assertEquals( 0, encodingResult.getErrorPosition() );

        f = prepareFile( new byte[] { SimpleEncodingDetector.UTF8_BOM_BYTES[0],
                SimpleEncodingDetector.UTF8_BOM_BYTES[1], 'a', 'b', 'c' } );
        encodingResult = detector.detectEncoding( f );
        Assert.assertEquals( Encoding.OTHER, encodingResult.getDetected() );
        Assert.assertEquals( "MALFORMED[2]", encodingResult.getError() );
        Assert.assertEquals( 0, encodingResult.getErrorPosition() );
    }

    /**
     * Expects an exception if the file doesn't exist.
     * @throws Exception any error
     */
    @Test( expected = RuntimeException.class )
    public void testFileNotFound() throws Exception
    {
        detector.detectEncoding( new File( "some file that does not exist" ) );
    }

    /**
     * Empty files should be detected as UTF-8.
     * @throws Exception any error
     */
    @Test
    public void testEmptyFile() throws Exception
    {
        File f = prepareFile( false, "", "UTF-8" );
        EncodingResult result = detector.detectEncoding( f );
        Assert.assertEquals( Encoding.UTF8, result.getDetected() );
    }

    private File prepareFile( boolean withBOM, String text, String encoding ) throws IOException
    {
        File f = File.createTempFile( "yal10n", null );
        f.deleteOnExit();

        FileOutputStream out = new FileOutputStream( f );
        if ( withBOM )
        {
            out.write( SimpleEncodingDetector.UTF8_BOM_BYTES );
        }
        out.write( text.getBytes( encoding ) );
        out.close();
        return f;
    }

    private File prepareBigFile( boolean withBOM, String text, String encoding, long fileSize ) throws IOException
    {
        File f = File.createTempFile( "yal10n", null );
        f.deleteOnExit();

        FileOutputStream out = new FileOutputStream( f );
        if ( withBOM )
        {
            out.write( SimpleEncodingDetector.UTF8_BOM_BYTES );
        }
        for ( long i = 0; i < fileSize; i++ )
        {
            out.write( 'a' );
        }
        out.write( text.getBytes( encoding ) );
        out.close();
        return f;
    }

    private File prepareFile( byte[] bytes ) throws IOException
    {
        File f = File.createTempFile( "yal10n", null );
        f.deleteOnExit();

        FileOutputStream out = new FileOutputStream( f );
        out.write( bytes );
        out.close();
        return f;
    }
}
