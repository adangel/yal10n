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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;

import net.sf.yal10n.analyzer.ResourceAnalyzer;
import net.sf.yal10n.analyzer.ResourceBundle;
import net.sf.yal10n.dashboard.DashboardModel;
import net.sf.yal10n.dashboard.DashboardRenderer;
import net.sf.yal10n.report.ReportRenderer;
import net.sf.yal10n.settings.DashboardConfiguration;
import net.sf.yal10n.settings.Repository;
import net.sf.yal10n.svn.SVNUtil;
import net.sf.yal10n.tmx.TranslationMemoryRenderer;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;

/**
 * This mojo creates a dashboard like overview of all resource bundles.
 */
@Mojo( name = "dashboard", requiresProject = false )
public class DashboardMojo extends BaseMojo
{
    private static final String GROUP_ID = "net.sf.yal10n";
    private static final String ARTIFACT_ID = "yal10n-maven-plugin";

    /** The dashboard renderer. */
    @Component
    private DashboardRenderer dashboardRenderer;
    /** The report renderer. */
    @Component
    private ReportRenderer reportRenderer;
    /** The tmx renderer. */
    @Component
    private TranslationMemoryRenderer tmxRenderer;

    /**
     * Instantiates a new dashboard mojo.
     */
    public DashboardMojo()
    {
        super();
    }

    /**
     * Creates a new DashboardMojo using the given components.
     *
     * @param svn the svn utility to use
     * @param analyzer the analyzer to use
     * @param dashboardRenderer the dashboard renderer
     * @param reportRenderer the report renderer
     * @param tmxRenderer the tmx renderer
     */
    DashboardMojo( SVNUtil svn, ResourceAnalyzer analyzer, DashboardRenderer dashboardRenderer,
            ReportRenderer reportRenderer, TranslationMemoryRenderer tmxRenderer )
    {
        this.svn = svn;
        this.analyzer = analyzer;
        this.dashboardRenderer = dashboardRenderer;
        this.reportRenderer = reportRenderer;
        this.tmxRenderer = tmxRenderer;
    }

    /**
     * {@inheritDoc}
     */
    public final void execute() throws MojoExecutionException,
        MojoFailureException
    {
        DashboardConfiguration config = DashboardConfiguration.readFromFile( yal10nSettings );

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
                svn.checkout( getLog(), svnCheckoutUrl, dstPath );
            }

            analyzer.analyze( getLog(), svnUrl, dstPath, config, repo, repoId );
        }

        List<ResourceBundle> bundles = analyzer.getBundles();
        getLog().info( "Found " + bundles.size() + " bundles:" );

        dashboardRenderer.render( DashboardModel.create( getLog(), config, bundles ), outputDirectory );
        if ( !reportRenderer.prepareOutputDirectory( outputDirectory ) )
        {
            throw new MojoExecutionException( "Couldn't create directory: " + reportRenderer.getReportDirectory() );
        }
        for ( ResourceBundle bundle : bundles )
        {
            getLog().info( "  " + bundle.getLocaleBasePath() );
            reportRenderer.render( bundle.getReport( getLog() ), outputDirectory );
        }
        
        if ( config.isCreateTMX() )
        {
            tmxRenderer.render( bundles, outputDirectory );
            for ( ResourceBundle bundle : bundles )
            {
                tmxRenderer.render( getLog(), bundle, outputDirectory );
            }
        }
        else
        {
            getLog().info( "Skipping TMX creation." );
        }

        OutputStream theme = null;
        try
        {
            theme = new FileOutputStream( FileUtils.normalize( outputDirectory + "/default.css" ) );
            IOUtil.copy( DashboardMojo.class.getResourceAsStream( "/themes/default.css" ), theme );
        }
        catch ( IOException e )
        {
            throw new MojoExecutionException( "Couldn't write stylesheet", e );
        }
        finally
        {
            IOUtil.close( theme );
        }
    }

    /**
     * Determines the version of this plugin.
     * @return the version
     */
    public static String getVersion()
    {
        String result = "unkown version";
        InputStream properties = DashboardMojo.class.getResourceAsStream( 
                "/META-INF/maven/" + GROUP_ID + "/" + ARTIFACT_ID + "/pom.properties" );
        if ( properties != null )
        {
            try
            {
                Properties props = new Properties();
                props.load( properties );
                result = props.getProperty( "version" );
            }
            catch ( IOException e )
            {
                result = "problem determining version";
            }
            finally
            {
                IOUtil.close( properties );
            }
        }
        return result;
    }
}
