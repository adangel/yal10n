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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.sf.yal10n.dashboard.LanguageComparator;
import net.sf.yal10n.svn.SVNUtil;

import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Configuration for the dashboard-mojo and detect-changes-mojo.
 * The configuration is read as JSON.
 */
public class DashboardConfiguration
{
    private String repoPrefix = "";
    private String mirrorPrefix = "";
    private String viewvcPrefix = "";
    private List<Repository> repositories = new ArrayList<Repository>();
    private List<String> includes = new ArrayList<String>();
    private List<String> excludes = new ArrayList<String>();
    private boolean createTMX;
    private List<String> languages = new ArrayList<String>();
    private LanguageComparator languageComparator = LanguageComparator.ALPHABETICAL_VARIANTS_LAST;
    private CheckConfiguration checks = new CheckConfiguration();
    private Notification notification = new Notification();


    /**
     * Gets the URL prefix, that should be used for every repository url. This is like a base url.
     * @return the prefix
     */
    public String getRepoPrefix()
    {
        return repoPrefix;
    }

    /**
     * Sets the URL prefix, that should be used for every repository url.
     * @param repoPrefix the prefix
     */
    public void setRepoPrefix( String repoPrefix )
    {
        this.repoPrefix = repoPrefix;
    }

    /**
     * Gets the mirror prefix, that should be used when a mirror url is defined in the repositories.
     * @return the mirror prefix
     */
    public String getMirrorPrefix()
    {
        return mirrorPrefix;
    }

    /**
     * Sets the mirror prefix.
     * @param mirrorPrefix the new mirror prefix
     */
    public void setMirrorPrefix( String mirrorPrefix )
    {
        this.mirrorPrefix = mirrorPrefix;
    }

    /**
     * Gets the web-URL prefix, that should be used for every repository url.
     * @return the web-URL prefix
     */
    public String getViewvcPrefix()
    {
        return viewvcPrefix;
    }

    /**
     * Sets the web-URL prefix, that should be used for every repository url.
     * @param viewvcPrefix the web-URL prefix
     */
    public void setViewvcPrefix( String viewvcPrefix )
    {
        this.viewvcPrefix = viewvcPrefix;
    }

    /**
     * Gets the list of configured repositories.
     * @return the repositories
     */
    public List<Repository> getRepositories()
    {
        return repositories;
    }

    /**
     * Sets the list of configured repositories.
     * @param repositories the repositories
     */
    public void setRepositories( List<Repository> repositories )
    {
        this.repositories = repositories;
    }

    /**
     * Gets the global ant-style include patterns that have effect across all repositories.
     * @return the include patterns.
     * @see Repository#getIncludes()
     */
    public List<String> getIncludes()
    {
        return includes;
    }

    /**
     * Sets the global ant-style include patterns that have effect across all repositories.
     * @param includes the include patterns.
     * @see Repository#getIncludes()
     */
    public void setIncludes( List<String> includes )
    {
        this.includes = includes;
    }

    /**
     * Gets the global ant-style exclude patterns that have effect across all repositories.
     * @return the exclude patterns.
     * @see Repository#getExcludes()
     */
    public List<String> getExcludes()
    {
        return excludes;
    }

    /**
     * Sets the global ant-style exclude patterns that have effect across all repositories.
     * @param excludes the exclude patterns.
     * @see Repository#getExcludes()
     */
    public void setExcludes( List<String> excludes )
    {
        this.excludes = excludes;
    }

    /**
     * Whether to create TMX files for download or not.
     * @return <code>true</code> if TMX files should be created.
     */
    public boolean isCreateTMX()
    {
        return createTMX;
    }

    /**
     * Sets whether to create TMX files for download or not.
     * @param createTMX <code>true</code> if TMX files should be created.
     */
    public void setCreateTMX( boolean createTMX )
    {
        this.createTMX = createTMX;
    }

    /**
     * Gets the list of all languages.
     * @return all languages
     */
    public List<String> getLanguages()
    {
        return languages;
    }

    /**
     * Sets the list of all languages.
     * @param languages all languages
     */
    public void setLanguages( List<String> languages )
    {
        this.languages = languages;
    }

    /**
     * Gets the comparator to be used for the languages in the dashboard.
     * Default is: {@link LanguageComparator#ALPHABETICAL_VARIANTS_LAST}
     * @return the language comparator
     */
    public LanguageComparator getLanguageComparator()
    {
        return languageComparator;
    }

    /**
     * Sets the comparator to be used for the languages in the dashboard.
     * @param languageComparator the language comparator
     */
    public void setLanguageComparator( LanguageComparator languageComparator )
    {
        this.languageComparator = languageComparator;
    }

    /**
     * Gets the configuration for the file checks.
     * @return the file check configurations
     */
    public CheckConfiguration getChecks()
    {
        return checks;
    }

    /**
     * Sets the configuration for the file checks.
     * @param checks the file check configurations
     */
    public void setChecks( CheckConfiguration checks )
    {
        this.checks = checks;
    }

    /**
     * Gets the notification configuration
     * @return the notification configuration
     */
    public Notification getNotification()
    {
        return notification;
    }

    /**
     * Sets the notification configuration
     * @param notification the notification configuration
     */
    public void setNotification( Notification notification )
    {
        this.notification = notification;
    }

    /**
     * Lookup a repository configuration by id.
     * @param repoId the repo id
     * @return the {@link Repository} configuration or <code>null</code>
     */
    public Repository getRepositoryById( String repoId )
    {
        Repository result = null;
        for ( Repository r : repositories )
        {
            if ( repoId.equals( SVNUtil.toRepoId( repoPrefix, r.getUrl() ) ) )
            {
                result = r;
                break;
            }
        }
        return result;
    }

    /**
     * Reads the configuration from a JSON file.
     * @param file the file to read from
     * @return the configuration
     */
    public static DashboardConfiguration readFromFile( String file )
    {
        try
        {
            File f = new File( file ).getCanonicalFile();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure( Feature.FAIL_ON_UNKNOWN_PROPERTIES, false );
            DashboardConfiguration config = mapper.reader( DashboardConfiguration.class ).readValue( f );
            return config;
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
    }
}
