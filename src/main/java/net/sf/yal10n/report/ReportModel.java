package net.sf.yal10n.report;

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

import net.sf.yal10n.dashboard.LanguageModel;

/**
 * Model used by velocity to render a report of a single messages file.
 */
public class ReportModel
{

    /** The project name. */
    private String projectName;

    /** The maven coordinates. */
    private String mavenCoordinates;

    /** The relative report url. */
    private String relativeReportUrl;

    /** The generation date. */
    private String generationDate;

    /** The version. */
    private String version;

    /** The base path. */
    private String basePath;

    /** The base name. */
    private String baseName;

    /** The all languages. */
    private List<LanguageModel> allLanguages;

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
     * Gets the all languages.
     *
     * @return the all languages
     */
    public List<LanguageModel> getAllLanguages()
    {
        return allLanguages;
    }

    /**
     * Sets the all languages.
     *
     * @param allLanguages the new all languages
     */
    public void setAllLanguages( List<LanguageModel> allLanguages )
    {
        this.allLanguages = allLanguages;
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
     * Gets the maven coordinates.
     *
     * @return the maven coordinates
     */
    public String getMavenCoordinates()
    {
        return mavenCoordinates;
    }

    /**
     * Sets the maven coordinates.
     *
     * @param mavenCoordinates the new maven coordinates
     */
    public void setMavenCoordinates( String mavenCoordinates )
    {
        this.mavenCoordinates = mavenCoordinates;
    }

    /**
     * Gets the generation date.
     *
     * @return the generation date
     */
    public String getGenerationDate()
    {
        return generationDate;
    }

    /**
     * Sets the generation date.
     *
     * @param generationDate the new generation date
     */
    public void setGenerationDate( String generationDate )
    {
        this.generationDate = generationDate;
    }

    /**
     * Gets the base path.
     *
     * @return the base path
     */
    public String getBasePath()
    {
        return basePath;
    }

    /**
     * Sets the base path.
     *
     * @param basePath the new base path
     */
    public void setBasePath( String basePath )
    {
        this.basePath = basePath;
    }

    /**
     * Gets the base name.
     *
     * @return the base name
     */
    public String getBaseName()
    {
        return baseName;
    }

    /**
     * Sets the base name.
     *
     * @param baseName the new base name
     */
    public void setBaseName( String baseName )
    {
        this.baseName = baseName;
    }

    /**
     * Gets the version.
     *
     * @return the version
     */
    public String getVersion()
    {
        return version;
    }

    /**
     * Sets the version.
     *
     * @param version the new version
     */
    public void setVersion( String version )
    {
        this.version = version;
    }
}
