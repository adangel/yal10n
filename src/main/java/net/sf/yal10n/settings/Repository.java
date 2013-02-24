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

import java.util.List;

/**
 * Configures a single repository. The information is used to checkout the repository and search
 * messages properties file in it.
 */
public class Repository
{
    private String url;
    private ScmType type = ScmType.SVN;
    private String viewvcUrl;
    private List<String> includes;
    private List<String> excludes;

    /**
     * Gets the URL from where the repository can be checked out. Read-only access is necessary.
     * @return the url
     */
    public String getUrl()
    {
        return url;
    }

    /**
     * Sets the URL from where the repository can be checked out. Read-only access is necessary.
     * @param url the url
     */
    public void setUrl( String url )
    {
        this.url = url;
    }

    /**
     * Gets the ant-style include patterns for the messages.properties file that should be used.
     * @return the include patterns.
     */
    public List<String> getIncludes()
    {
        return includes;
    }

    /**
     * Sets the ant-style include patterns for the messages.properties file that should be used.
     * @param includes the include patterns.
     */
    public void setIncludes( List<String> includes )
    {
        this.includes = includes;
    }

    /**
     * Gets the ant-style exclude patterns for files, that should not be recognized as messages files.
     * @return the exclude patterns.
     */
    public List<String> getExcludes()
    {
        return excludes;
    }

    /**
     * Sets the ant-style exclude patterns for files, that should not be recognized as messages files.
     * @param excludes the exclude patterns.
     */
    public void setExcludes( List<String> excludes )
    {
        this.excludes = excludes;
    }

    /**
     * Gets the web-URL, that fits to the SCM URL, to have access to the repository via web.
     * @return the web-URL
     */
    public String getViewvcUrl()
    {
        return viewvcUrl;
    }

    /**
     * Sets the web-URL, that fits to the SCM URL, to have access to the repository via web.
     * @param viewvcUrl the web-URL
     */
    public void setViewvcUrl( String viewvcUrl )
    {
        this.viewvcUrl = viewvcUrl;
    }

    /**
     * Gets the SCM type.
     * @return the SCM type
     */
    public ScmType getType()
    {
        return type;
    }

    /**
     * Sets the SCM type.
     * @param type the SCM type
     */
    public void setType( ScmType type )
    {
        this.type = type;
    }
}
