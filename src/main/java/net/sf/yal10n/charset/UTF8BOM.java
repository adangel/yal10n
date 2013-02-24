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

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

/**
 * UTF-8 charset implementation that supports an optional BOM (Byte-Order-Mark).
 * Base on the Java UTF-8 charset.
 */
public class UTF8BOM extends Charset
{

    private static final float MAXIMUM_BYTES_PER_CHARACTER = 7.0f;

    private static final float AVERAGE_BYTES_PER_CHARACTER = 1.1f;

    /** Name of the charset - used if you select a charset via a String. */
    public static final String NAME = "UTF-8-BOM";

    private static final Charset UTF8_CHARSET = Charset.forName( "UTF-8" );

    private static final byte[] UTF8_BOM_BYTES = new byte[] { (byte) 0xef, (byte) 0xbb, (byte) 0xbf };

    /**
     * Creates a new UTF8BOM charset.
     */
    protected UTF8BOM()
    {
        super( NAME, null );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains( Charset cs )
    {
        return UTF8_CHARSET.contains( cs );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CharsetDecoder newDecoder()
    {
        return new CharsetDecoder( this, 1.0f, 1.0f )
        {
            private final CharsetDecoder utf8decoder = UTF8_CHARSET.newDecoder()
                    .onMalformedInput( malformedInputAction() ).onUnmappableCharacter( unmappableCharacterAction() );
            private boolean bomDone = false;
            private int bomIndexDone = 0;
            private byte[] bom = new byte[3];

            @Override
            protected CoderResult decodeLoop( ByteBuffer in, CharBuffer out )
            {
                utf8decoder.reset();
                if ( !bomDone )
                {
                    try
                    {
                        while ( bomIndexDone < 3 )
                        {
                            bom[bomIndexDone] = in.get();
                            if ( bom[bomIndexDone] == UTF8_BOM_BYTES[bomIndexDone] )
                            {
                                bomIndexDone++;
                            }
                            else
                            {
                                break;
                            }
                        }
                        bomDone = true;
                        if ( bomIndexDone != 3 )
                        {
                            ByteBuffer in2 = ByteBuffer.allocate( 3 + in.capacity() );
                            in2.mark();
                            in2.put( bom, 0, bomIndexDone + 1 );
                            in2.limit( in2.position() );
                            in2.reset();

                            utf8decoder.decode( in2, out, false );
                            return utf8decoder.decode( in, out, true );
                        }
                    }
                    catch ( BufferUnderflowException e )
                    {
                        return CoderResult.UNDERFLOW;
                    }
                }
                return utf8decoder.decode( in, out, true );
            }

            /**
             * {@inheritDoc}
             */
            @Override
            protected void implReset()
            {
                bomDone = false;
                bomIndexDone = 0;
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CharsetEncoder newEncoder()
    {
        return new CharsetEncoder( this, AVERAGE_BYTES_PER_CHARACTER, MAXIMUM_BYTES_PER_CHARACTER )
        {
            private CharsetEncoder utf8encoder = UTF8_CHARSET.newEncoder().onMalformedInput( CodingErrorAction.REPORT )
                    .onUnmappableCharacter( CodingErrorAction.REPORT );

            private boolean bomDone = false;

            /**
             * {@inheritDoc}
             */
            @Override
            protected CoderResult encodeLoop( CharBuffer in, ByteBuffer out )
            {
                utf8encoder.reset();
                if ( !bomDone )
                {
                    try
                    {
                        out.put( UTF8_BOM_BYTES );
                        bomDone = true;
                    }
                    catch ( BufferOverflowException e )
                    {
                        return CoderResult.OVERFLOW;
                    }
                }
                return utf8encoder.encode( in, out, true );
            }

            /**
             * {@inheritDoc}
             */
            @Override
            protected void implReset()
            {
                bomDone = false;
            }
        };
    }
}
