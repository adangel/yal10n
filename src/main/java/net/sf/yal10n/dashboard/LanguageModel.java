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
import java.util.Map;

/**
 * Contains all the data necessary to render a report about a specific language.
 */
public class LanguageModel
{

    private String svnUrl;
    private String relativeUrl;
    private String svnInfo;
    private String name;
    private String encoding;
    private int countOfMessages = -1;
    private Map<String, String> notTranslatedMessages = new HashMap<String, String>();
    private Map<String, String> missingMessages = new HashMap<String, String>();
    private Map<String, String> additionalMessages = new HashMap<String, String>();
    private String issuesSeverityClass;
    private List<String> scoreLog;

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
     * Gets the score log.
     *
     * @return the score log
     */
    public List<String> getScoreLog()
    {
        return scoreLog;
    }

    /**
     * Sets the score log.
     *
     * @param scoreLog the new score log
     */
    public void setScoreLog( List<String> scoreLog )
    {
        this.scoreLog = scoreLog;
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
     * Gets the issues severity class.
     *
     * @return the issues severity class
     */
    public String getIssuesSeverityClass()
    {
        return issuesSeverityClass;
    }

    /**
     * Sets the issues severity class.
     *
     * @param issuesSeverityClass the new issues severity class
     */
    public void setIssuesSeverityClass( String issuesSeverityClass )
    {
        this.issuesSeverityClass = issuesSeverityClass;
    }
}
