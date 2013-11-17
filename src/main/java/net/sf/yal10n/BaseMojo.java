package net.sf.yal10n;

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

import net.sf.yal10n.analyzer.ResourceAnalyzer;
import net.sf.yal10n.settings.DashboardConfiguration;
import net.sf.yal10n.settings.Repository;
import net.sf.yal10n.status.DetectChangesStatus;
import net.sf.yal10n.status.RepoStatus;
import net.sf.yal10n.svn.SVNUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.FileUtils;

/**
 * Base class for the mojos.
 */
public abstract class BaseMojo extends AbstractMojo
{
    /** The configuration of the dashboard is read from this file. The file format is json. */
    @Parameter( required = true, property = "yal10n.settings", defaultValue = "yal10n-settings.json" )
    protected String yal10nSettings;

    /** Whether to update the local workspace checkouts or not. */
    @Parameter( required = true, defaultValue = "${settings.offline}" )
    protected boolean offline;

    /** The directory where the dashboard html files should be created. */
    @Parameter( required = true, property = "yal10n.outputDirectory", defaultValue = "target" )
    protected String outputDirectory;

    /** The svn utility to do checkouts. */
    @Component
    protected SVNUtil svn;
    /** The analyzer that finds resource bundles. */
    @Component
    protected ResourceAnalyzer analyzer;

    /**
     * Instantiates a new base mojo.
     */
    public BaseMojo()
    {
        super();
    }

    /**
     * Sets the settings file name
     *
     * @param yal10nSettings the settings file name to use
     */
    public void setYal10nSettings( String yal10nSettings )
    {
        this.yal10nSettings = yal10nSettings;
    }

    /**
     * Sets the output directory.
     *
     * @param outputDirectory the new output directory
     */
    public void setOutputDirectory( String outputDirectory )
    {
        this.outputDirectory = outputDirectory;
    }

    /**
     * Performs the svn checkout and collects the new revision status.
     * @param config the configuration with all the repositories
     * @param newStatus the new status. Can be <code>null</code>.
     */
    protected void checkout( DashboardConfiguration config, DetectChangesStatus newStatus )
    {
        int repoNumber = 0;
        for ( Repository repo : config.getRepositories() )
        {
            repoNumber++;
            getLog().debug( repoNumber + " url: " + repo.getUrl() );

            String svnUrl = SVNUtil.toCompleteUrl( config.getRepoPrefix(), repo.getUrl() );
            String mirrorUrl = SVNUtil.toCompleteUrl( config.getMirrorPrefix(), repo.getMirrorUrl() );
            String svnCheckoutUrl = StringUtils.isEmpty( mirrorUrl ) ? svnUrl : mirrorUrl;
            String repoId = SVNUtil.toRepoId( config.getRepoPrefix(), repo.getUrl() );

            String dstPath = FileUtils.normalize( outputDirectory + "/checkouts/" + repoId + "/" );
            if ( offline )
            {
                getLog().info( "Offline mode - not updating repo: " + svnUrl );
            }
            else
            {
                String revision = svn.checkout( getLog(), repo.getType(), svnCheckoutUrl, dstPath );

                if ( newStatus != null )
                {
                    RepoStatus status = new RepoStatus();
                    status.setId( repoId );
                    status.setRevision( revision );
                    status.setCompleteRepoUrl( svnUrl );
                    newStatus.getRepos().add( status );
                }
            }

            analyzer.analyze( getLog(), svnCheckoutUrl, dstPath, config, repo, repoId );
        }
    }
}