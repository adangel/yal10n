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
import java.util.Map;

/**
 * Contains the data necessary to render a summary report about a bundle.
 */
public class BundleModel implements Comparable<BundleModel>
{

    private String projectName;

    private LanguageModel base;

    private Map<String, LanguageModel> languages = new HashMap<String, LanguageModel>();

    private String relativeReportUrl;

    private String relativeTmxUrl;

    /**
     * Gets the language.
     *
     * @param language the language
     * @return the language
     */
    public LanguageModel getLanguage( String language )
    {
        return languages.get( language );
    }

    /**
     * Gets the project name.
     *
     * @return the project name
     */
    public String getProjectName()
    {
        return projectName;
    }

    /**
     * Sets the project name.
     *
     * @param projectName the new project name
     */
    public void setProjectName( String projectName )
    {
        this.projectName = projectName;
    }

    /**
     * Gets the base.
     *
     * @return the base
     */
    public LanguageModel getBase()
    {
        return base;
    }

    /**
     * Sets the base.
     *
     * @param base the new base
     */
    public void setBase( LanguageModel base )
    {
        this.base = base;
    }

    /**
     * Gets the languages.
     *
     * @return the languages
     */
    public Map<String, LanguageModel> getLanguages()
    {
        return languages;
    }

    /**
     * Gets the relative report url.
     *
     * @return the relative report url
     */
    public String getRelativeReportUrl()
    {
        return relativeReportUrl;
    }

    /**
     * Sets the relative report url.
     *
     * @param relativeReportUrl the new relative report url
     */
    public void setRelativeReportUrl( String relativeReportUrl )
    {
        this.relativeReportUrl = relativeReportUrl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo( BundleModel o )
    {
        return getProjectName().compareToIgnoreCase( o.getProjectName() );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( projectName == null ) ? 0 : projectName.hashCode() );
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
        {
            return true;
        }
        if ( obj == null )
        {
            return false;
        }
        if ( getClass() != obj.getClass() )
        {
            return false;
        }
        BundleModel other = (BundleModel) obj;
        if ( projectName == null )
        {
            if ( other.projectName != null )
            {
                return false;
            }
        }
        else if ( !projectName.equals( other.projectName ) )
        {
            return false;
        }
        return true;
    }

    /**
     * Gets the relative tmx url.
     *
     * @return the relative tmx url
     */
    public String getRelativeTmxUrl()
    {
        return relativeTmxUrl;
    }

    /**
     * Sets the relative tmx url.
     *
     * @param relativeTmxUrl the new relative tmx url
     */
    public void setRelativeTmxUrl( String relativeTmxUrl )
    {
        this.relativeTmxUrl = relativeTmxUrl;
    }

    /**
     * Adds the language.
     *
     * @param lang the lang
     * @param languageModel the language model
     */
    public void addLanguage( String lang, LanguageModel languageModel )
    {
        languages.put( lang, languageModel );
    }
}
