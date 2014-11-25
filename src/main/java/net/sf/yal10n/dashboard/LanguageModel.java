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

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Contains all the data necessary to render a report about a specific language.
 */
public class LanguageModel
{

    private String svnUrl;
    private String svnCheckoutUrl;
    private String relativeUrl;
    private String svnInfo;
    private String name;
    private String encoding;
    private StatusClass encodingStatus;
    private int countOfDefaultMessages = -1;
    private int countOfMessages = -1;
    private Map<String, String> notTranslatedMessages = new HashMap<String, String>();
    private Map<String, String> missingMessages = new HashMap<String, String>();
    private Map<String, String> additionalMessages = new HashMap<String, String>();
    private StatusClass status;
    private List<String> issues;
    private boolean variant;
    private boolean existing;

    /**
     * Gets the svn url.
     *
     * @return the svn url
     */
    public String getSvnUrl()
    {
        return svnUrl;
    }

    /**
     * Sets the svn url.
     *
     * @param svnUrl the new svn url
     */
    public void setSvnUrl( String svnUrl )
    {
        this.svnUrl = svnUrl;
    }

    /**
     * Gets the svn checkout url.
     *
     * @return the svn checkout url
     */
    public String getSvnCheckoutUrl()
    {
        return svnCheckoutUrl;
    }

    /**
     * Sets the svn checkout url.
     *
     * @param svnCheckoutUrl the new svn checkout url
     */
    public void setSvnCheckoutUrl( String svnCheckoutUrl )
    {
        this.svnCheckoutUrl = svnCheckoutUrl;
    }

    /**
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName( String name )
    {
        this.name = name;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Sets the count of messages.
     *
     * @param count the new count of messages
     */
    public void setCountOfMessages( int count )
    {
        this.countOfMessages = count;
    }

    /**
     * Gets the count of messages.
     *
     * @return the count of messages
     */
    public int getCountOfMessages()
    {
        return countOfMessages;
    }

    /**
     * Sets the encoding.
     *
     * @param encoding the new encoding
     */
    public void setEncoding( String encoding )
    {
        this.encoding = encoding;
    }

    /**
     * Gets the encoding.
     *
     * @return the encoding
     */
    public String getEncoding()
    {
        return this.encoding;
    }

    /**
     * Sets the encoding status.
     *
     * @param encodingStatus the encoding status
     */
    public void setEncodingStatus( StatusClass encodingStatus )
    {
        this.encodingStatus = encodingStatus;
    }

    /**
     * Gets the encoding status.
     *
     * @return the encoding status
     */
    public StatusClass getEncodingStatus()
    {
        return this.encodingStatus;
    }

    /**
     * Gets the count of default messages.
     *
     * @return the count of default messages
     */
    public int getCountOfDefaultMessages()
    {
        return countOfDefaultMessages;
    }

    /**
     * Sets the count of default messages.
     *
     * @param countOfDefaultMessages the count of default messages
     */
    public void setCountOfDefaultMessages( int countOfDefaultMessages )
    {
        this.countOfDefaultMessages = countOfDefaultMessages;
    }

    /**
     * Gets the not translated messages.
     *
     * @return the not translated messages
     */
    public Map<String, String> getNotTranslatedMessages()
    {
        return notTranslatedMessages;
    }

    /**
     * Sets the not translated messages.
     *
     * @param notTranslatedMessages the not translated messages
     */
    public void setNotTranslatedMessages( Map<String, String> notTranslatedMessages )
    {
        this.notTranslatedMessages = notTranslatedMessages;
    }

    /**
     * Gets the missing messages.
     *
     * @return the missing messages
     */
    public Map<String, String> getMissingMessages()
    {
        return missingMessages;
    }

    /**
     * Sets the missing messages.
     *
     * @param missingMessages the missing messages
     */
    public void setMissingMessages( Map<String, String> missingMessages )
    {
        this.missingMessages = missingMessages;
    }

    /**
     * Gets the additional messages.
     *
     * @return the additional messages
     */
    public Map<String, String> getAdditionalMessages()
    {
        return additionalMessages;
    }

    /**
     * Sets the additional messages.
     *
     * @param additionalMessages the additional messages
     */
    public void setAdditionalMessages( Map<String, String> additionalMessages )
    {
        this.additionalMessages = additionalMessages;
    }

    /**
     * Gets the svn info.
     *
     * @return the svn info
     */
    public String getSvnInfo()
    {
        return svnInfo;
    }

    /**
     * Sets the svn info.
     *
     * @param svnInfo the new svn info
     */
    public void setSvnInfo( String svnInfo )
    {
        this.svnInfo = svnInfo;
    }

    /**
     * Gets the issues.
     *
     * @return the issues
     */
    public List<String> getIssues()
    {
        return issues;
    }

    /**
     * Sets the issues.
     *
     * @param issues the issues
     */
    public void setIssues( List<String> issues )
    {
        this.issues = issues;
    }

    /**
     * Gets the relative url.
     *
     * @return the relative url
     */
    public String getRelativeUrl()
    {
        return relativeUrl;
    }

    /**
     * Sets the relative url.
     *
     * @param relativeUrl the new relative url
     */
    public void setRelativeUrl( String relativeUrl )
    {
        this.relativeUrl = relativeUrl;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public StatusClass getStatus()
    {
        return this.status;
    }

    /**
     * Sets the status.
     *
     * @param status the status
     */
    public void setStatus( StatusClass status )
    {
        this.status = status;
    }

    /**
     * Whether this language is a language variant (e.g. de_DE).
     * @return <code>true</code> if this is a variant, <code>false</code> otherwise.
     */
    public boolean isVariant()
    {
        return variant;
    }

    /**
     * Sets whether this language is a variant (e.g. de_DE).
     * @param variant <code>true</code> if this is a variant.
     */
    public void setVariant( boolean variant )
    {
        this.variant = variant;
    }

    /**
     * Whether a file exists for this language or not.
     * @return <code>true</code> if the file exists, <code>false</code> if the file is completely missing.
     */
    public boolean isExisting()
    {
        return existing;
    }

    /**
     * Sets whether a file exists for this language or not.
     * @param existing <code>true</code> if the file exists.
     */
    public void setExisting( boolean existing )
    {
        this.existing = existing;
    }

    /**
     * Gets the string percentage of missing messages.
     * @return percentage of missing messages
     */
    public String getMissingMessagesPercentage()
    {
        return countOfDefaultMessages > -1
            ? String.format( Locale.ENGLISH, "%.2f %%", 100.0 * missingMessages.size() / countOfDefaultMessages )
            : "n/a";
    }

    /**
     * Gets the string percentage of not translated messages.
     * @return percentage of not translated messages
     */
    public String getNotTranslatedMessagesPercentage()
    {
        return countOfDefaultMessages > -1
            ? String.format( Locale.ENGLISH, "%.2f %%", 100.0 * notTranslatedMessages.size() / countOfDefaultMessages )
            : "n/a";
    }

    /**
     * Gets the string percentage of additional messages.
     * @return percentage of additional messages
     */
    public String getAdditionalMessagesPercentage()
    {
        return countOfDefaultMessages > -1
            ? String.format( Locale.ENGLISH, "%.2f %%", 100.0 * additionalMessages.size() / countOfDefaultMessages )
            : "n/a";
    }
}
