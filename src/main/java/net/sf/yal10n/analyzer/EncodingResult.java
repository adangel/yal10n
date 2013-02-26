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


/**
 * The result of a encoding detection.
 */
public class EncodingResult
{
    private Encoding detected = Encoding.OTHER;
    private String error;
    private long errorPosition = -1;
    private int errorLine = -1;
    private int errorColumn = -1;

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
    public String getError()
    {
        return error;
    }

    /**
     * Sets the last coding error.
     * @param error the last coding error.
     */
    public void setError( String error )
    {
        this.error = error;
    }

    /**
     * Gets the error position if there is an error.
     * @return the error position.
     */
    public long getErrorPosition()
    {
        return errorPosition;
    }

    /**
     * Sets the error position.
     * @param errorPosition the error position.
     */
    public void setErrorPosition( long errorPosition )
    {
        this.errorPosition = errorPosition;
    }

    /**
     * Gets the error line.
     *
     * @return the error line
     */
    public int getErrorLine()
    {
        return errorLine;
    }

    /**
     * Sets the error line.
     *
     * @param errorLine the new error line
     */
    public void setErrorLine( int errorLine )
    {
        this.errorLine = errorLine;
    }

    /**
     * Gets the error column.
     *
     * @return the error column
     */
    public int getErrorColumn()
    {
        return errorColumn;
    }

    /**
     * Sets the error column.
     *
     * @param errorColumn the new error column
     */
    public void setErrorColumn( int errorColumn )
    {
        this.errorColumn = errorColumn;
    }
}
