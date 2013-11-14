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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.yal10n.DashboardMojo;
import net.sf.yal10n.analyzer.ResourceBundle;
import net.sf.yal10n.settings.DashboardConfiguration;

import org.apache.maven.plugin.logging.Log;

/**
 * This is the overall model for rendering the dashboard overview.
 */
public class DashboardModel
{

    private String generationDate;
    private List<String> allLanguages;
    private List<ProjectModel> projects;
    private boolean createTmx;
    private String version;

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
     * Gets the all languages.
     *
     * @return the all languages
     */
    public List<String> getAllLanguages()
    {
        return allLanguages;
    }

    /**
     * Sets the all languages.
     *
     * @param allLanguages the new all languages
     */
    public void setAllLanguages( List<String> allLanguages )
    {
        this.allLanguages = allLanguages;
    }

    /**
     * Creates a Dashboard model based on the given {@link ResourceBundle}s.
     *
     * @param log the log
     * @param config the configuration
     * @param bundles the bundles
     * @return the dashboard model
     */
    public static DashboardModel create( Log log, DashboardConfiguration config, List<ResourceBundle> bundles )
    {
        List<String> languages = config.getLanguages();
        boolean createTmx = config.isCreateTMX();
        Set<String> allLanguages = new HashSet<String>( languages );
        for ( ResourceBundle bundle : bundles )
        {
            allLanguages.addAll( bundle.getLanguages() );
        }
        ArrayList<String> allLanguagesSorted = new ArrayList<String>( allLanguages );
        Collections.sort( allLanguagesSorted, config.getLanguageComparator() );

        Map<String, List<BundleModel>> projects = new HashMap<String, List<BundleModel>>();
        for ( ResourceBundle bundle : bundles )
        {
            List<BundleModel> bundleModels = projects.get( bundle.getRepoId() );
            if ( bundleModels == null )
            {
                bundleModels = new ArrayList<BundleModel>();
                projects.put( bundle.getRepoId(), bundleModels );
            }
            bundleModels.add( bundle.toBundleModel( log, allLanguagesSorted ) );
        }
        List<ProjectModel> projectModels = new ArrayList<ProjectModel>();
        for ( List<BundleModel> bundleModels : projects.values() )
        {
            resolveDuplicatedProjectNames( bundleModels );
            projectModels.add( new ProjectModel( bundleModels ) );
        }

        DashboardModel model = new DashboardModel();
        model.setProjects( projectModels );
        model.setAllLanguages( allLanguagesSorted );
        model.setGenerationDate( new Date().toString() );
        model.setVersion( DashboardMojo.getVersion() );
        model.setCreateTmx( createTmx );

        return model;
    }

    private static void resolveDuplicatedProjectNames( List<BundleModel> bundleModels )
    {
        Collections.sort( bundleModels );
        BundleModel lastBundle = null;
        for ( BundleModel bundle : bundleModels )
        {
            if ( lastBundle != null && lastBundle.getProjectName().equals( bundle.getProjectName() ) )
            {
                lastBundle.setProjectName( lastBundle.getProjectName() + " " + lastBundle.getBundleName() );
                bundle.setProjectName( bundle.getProjectName() + " " + bundle.getBundleName() );
            }
            lastBundle = bundle;
        }
    }

    /**
     * Checks if is creates the tmx.
     *
     * @return true, if is creates the tmx
     */
    public boolean isCreateTmx()
    {
        return createTmx;
    }

    /**
     * Sets the creates the tmx.
     *
     * @param createTmx the new creates the tmx
     */
    public void setCreateTmx( boolean createTmx )
    {
        this.createTmx = createTmx;
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

    /**
     * Gets the projects.
     *
     * @return the projects
     */
    public List<ProjectModel> getProjects()
    {
        return projects;
    }

    /**
     * Sets the projects.
     *
     * @param projects the new projects
     */
    public void setProjects( List<ProjectModel> projects )
    {
        this.projects = projects;
    }
}
