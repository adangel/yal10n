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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

import org.codehaus.plexus.util.IOUtil;

/**
 * Detects the encoding of a given file.
 */
public class SimpleEncodingDetector
{

    static final byte[] UTF8_BOM_BYTES = new byte[] { (byte) 0xef, (byte) 0xbb, (byte) 0xbf };
    private static final Charset UTF8 = Charset.forName( "UTF-8" );

    private int bufferSize = 1024;

    /**
     * Sets the buffer size to use when reading files.
     * @param bufferSize the buffer size in bytes
     */
    public void setBufferSize( int bufferSize )
    {
        this.bufferSize = bufferSize;
    }

    /**
     * Determines the encoding of the given file.
     * @param f the file to check
     * @return the encoding, never <code>null</code>.
     */
    public EncodingResult detectEncoding( File f )
    {
        EncodingResult encodingResult;

        if ( f.exists() && f.length() == 0 )
        {
            encodingResult = new EncodingResult();
            encodingResult.setDetected( Encoding.UTF8 );
        }
        else
        {
            encodingResult = doDetect( f );
        }
        return encodingResult;
    }

    private EncodingResult doDetect( File f )
    {
        EncodingResult encodingResult = new EncodingResult();
        FileInputStream in = null;
        try
        {
            in = new FileInputStream( f );

            byte[] bom = new byte[3];
            boolean hasBOM = in.read( bom ) == 3 && isBOM( bom );

            FileChannel channel = in.getChannel().position( 0L );
            CharsetDecoder utf8decoder = UTF8.newDecoder();
            utf8decoder.onMalformedInput( CodingErrorAction.REPORT );
            utf8decoder.onUnmappableCharacter( CodingErrorAction.REPORT );

            ByteBuffer buffer = ByteBuffer.allocate( bufferSize );
            CharBuffer out = CharBuffer.allocate( bufferSize );
            CoderResult result = null;
            long decoderPosition = 0;
            while ( channel.read( buffer )  > -1 )
            {
                buffer.flip();
                result = utf8decoder.decode( buffer, out, false );
                decoderPosition += buffer.position();
                buffer.compact();
                if ( result.isError() )
                {
                    break;
                }
                out.flip();
                String decodedString = out.toString();
                int replacementPosition = decodedString.indexOf( utf8decoder.replacement() );
                if ( replacementPosition > -1 )
                {
                    decoderPosition = decoderPosition - decodedString.length() + replacementPosition;
                    result = CoderResult.unmappableForLength( 1 );
                    break;
                }
                out.clear();
            }
            if ( result != null && !result.isError() )
            {
                buffer.flip();
                out.clear();
                result = utf8decoder.decode( buffer, out, true );
                out.clear();
                utf8decoder.flush( out );
            }
            if ( result != null && !result.isError() )
            {
                if ( hasBOM )
                {
                    encodingResult.setDetected( Encoding.UTF8_BOM );
                }
                else
                {
                    encodingResult.setDetected( Encoding.UTF8 );
                }
            }
            else
            {
                encodingResult.setError( String.valueOf( result ) );
                encodingResult.setErrorPosition( decoderPosition );
                determineLineAndColumn( f, encodingResult );
            }
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
        finally
        {
            IOUtil.close( in );
        }
        return encodingResult;
    }

    /**
     * Takes the error position of the given encoding result and
     * determines the line number and column number, where the
     * error occurred.
     * @param f the file
     * @param encodingResult the result
     */
    private void determineLineAndColumn( File f, EncodingResult encodingResult )
    {
        BufferedReader in = null;
        try
        {
            in = new BufferedReader( new InputStreamReader( new FileInputStream( f ), "ISO-8859-1" ) );
            String line;
            long position = 0;
            int lineNumber = 0;
            while ( ( line = in.readLine() ) != null )
            {
                lineNumber++;
                position += line.length() + 1; // assumes 1 byte newline character
                if ( position >= encodingResult.getErrorPosition() )
                {
                    long lineStartPosition = position - line.length() - 1;
                    encodingResult.setErrorLine( lineNumber );
                    int columnPosition = (int) ( encodingResult.getErrorPosition() - lineStartPosition + 1 );
                    encodingResult.setErrorColumn( columnPosition );
                    break;
                }
            }
        }
        catch ( UnsupportedEncodingException e )
        {
            throw new RuntimeException( e );
        }
        catch ( FileNotFoundException e )
        {
            throw new RuntimeException( e );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
        finally
        {
            IOUtil.close( in );
        }
    }

    private boolean isBOM( byte[] bom )
    {
        return bom[0] == UTF8_BOM_BYTES[0] && bom[1] == UTF8_BOM_BYTES[1] && bom[2] == UTF8_BOM_BYTES[2];
    }
}
