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

import java.nio.charset.CoderResult;

/**
 * The result of a encoding detection.
 */
public class EncodingResult
{
    private Encoding detected = Encoding.OTHER;
    private CoderResult error;
    private int errorPosition = -1;

    /**
     * Gets the detected encoding.
     * @return the detected encoding.
     */
    public Encoding getDetected()
    {
        return detected;
    }

    /**
     * Sets the encoding, that has been detected.
     * @param detected the detected encoding.
     */
    public void setDetected( Encoding detected )
    {
        this.detected = detected;
    }

    /**
     * Gets the last coding error or <code>null</code> if there was no error.
     * @return the last error or <code>null</code> if there was no error.
     */
    public CoderResult getError()
    {
        return error;
    }

    /**
     * Sets the last coding error.
     * @param error the last coding error.
     */
    public void setError( CoderResult error )
    {
        this.error = error;
    }

    /**
     * Gets the error position if there is an error.
     * @return the error position.
     */
    public int getErrorPosition()
    {
        return errorPosition;
    }

    /**
     * Sets the error position.
     * @param errorPosition the error position.
     */
    public void setErrorPosition( int errorPosition )
    {
        this.errorPosition = errorPosition;
    }
}
