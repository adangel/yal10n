package net.sf.yal10n.settings;

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
 * Configures various aspects of messages.properties file checks.
 */
public class CheckConfiguration
{
    private double percentageMissing = 10.0d;
    private boolean checkFileHeaders = false;
    private String fileHeaderRegexp = "";

    /**
     * Determines how many messages are allowed to be missing (missing or not translated)
     * before a minor issue should be raised. The value is in percentage. Default value is 10%.
     * @return the percentage of missing/not-translated messages that are not a problem.
     */
    public double getPercentageMissing()
    {
        return percentageMissing;
    }

    /**
     * Sets how many messages are allowed to be missing (missing or not translated)
     * before a minor issue should be raised. The value is in percentage. Default value is 10%.
     * @param percentageMissing the new value
     */
    public void setPercentageMissing( double percentageMissing )
    {
        this.percentageMissing = percentageMissing;
    }

    /**
     * Whether the file header check is enabled.
     * @return <code>true</code> if the file header check is enabled.
     * @see #getFileHeaderRegexp()
     */
    public boolean isCheckFileHeaders()
    {
        return checkFileHeaders;
    }

    /**
     * Sets whether the file header check is enabled.
     * @param checkFileHeaders <code>true</code> if the file header check is enabled.
     * @see #getFileHeaderRegexp()
     */
    public void setCheckFileHeaders( boolean checkFileHeaders )
    {
        this.checkFileHeaders = checkFileHeaders;
    }

    /**
     * Gets the regular expression used to check the file headers.
     * @return the regular expression
     * @see #isCheckFileHeaders()
     */
    public String getFileHeaderRegexp()
    {
        return fileHeaderRegexp;
    }

    /**
     * Sets the regular expression used to check the file headers.
     * @param fileHeaderRegexp the regular expression
     * @see #isCheckFileHeaders()
     */
    public void setFileHeaderRegexp( String fileHeaderRegexp )
    {
        this.fileHeaderRegexp = fileHeaderRegexp;
    }
}
