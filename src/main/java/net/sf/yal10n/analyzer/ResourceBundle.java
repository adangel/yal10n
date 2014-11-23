package net.sf.yal10n.analyzer;

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
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import net.sf.yal10n.DashboardMojo;
import net.sf.yal10n.dashboard.BundleModel;
import net.sf.yal10n.dashboard.LanguageModel;
import net.sf.yal10n.report.ReportModel;
import net.sf.yal10n.settings.CheckConfiguration;
import net.sf.yal10n.settings.DashboardConfiguration;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.StringUtils;

/**
 * A {@link ResourceBundle} represents one bundle of {@link ResourceFile}s.
 */
public class ResourceBundle
{
    static final AtomicInteger COUNTER = new AtomicInteger( -1 );

    private String repoId;
    private String svnUrl;
    private DashboardConfiguration config;
    private String localBasePath;
    private String checkoutDirectory;
    private Map<String, ResourceFile> files = new HashMap<String, ResourceFile>();
    private final int id;

    /**
     * Instantiates a new resource bundle.
     *
     * @param config the config
     * @param svnUrl the svn url
     * @param repoId the repo id
     * @param localBasePath the local base path
     * @param checkoutDirectory the base checkout directory
     */
    public ResourceBundle( DashboardConfiguration config, String svnUrl, String repoId,
            String localBasePath, String checkoutDirectory )
    {
        if ( !localBasePath.startsWith( checkoutDirectory ) )
        {
            throw new IllegalArgumentException( "localBasePath " + localBasePath
                    + " doesn't start with checkoutDirectory " + checkoutDirectory );
        }
        this.id = COUNTER.incrementAndGet();
        this.repoId = repoId;
        this.config = config;
        this.svnUrl = svnUrl;
        this.localBasePath = localBasePath;
        this.checkoutDirectory = checkoutDirectory;
    }

    /**
     * Gets the repo id.
     *
     * @return the repo id
     */
    public String getRepoId()
    {
        return repoId;
    }

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
     * Gets the locale base path.
     *
     * @return the locale base path
     */
    public String getLocaleBasePath()
    {
        return localBasePath;
    }

    /**
     * Adds the file.
     *
     * @param file the file
     */
    public void addFile( ResourceFile file )
    {
        file.setBundle( this );
        files.put( file.getLanguage(), file );
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append( "ResourceBundle: localBasePath=" ).append( localBasePath ).append( "\n" );
        sb.append( "    project: " ).append( getProjectName() ).append( "\n" );
        sb.append( "    checkoutDirectory: " ).append( checkoutDirectory ).append( "\n" );
        sb.append( "    " ).append( files );
        sb.append( "\n" );
        return sb.toString();
    }

    /**
     * Gets the default file.
     *
     * @return the default file
     */
    public ResourceFile getDefaultFile()
    {
        return files.get( "default" );
    }

    /**
     * Gets the languages.
     *
     * @return the languages
     */
    public List<String> getLanguages()
    {
        Set<String> result = new HashSet<String>( files.keySet() );
        result.remove( "default" );
        List<String> resultList = new ArrayList<String>( result );
        Collections.sort( resultList, String.CASE_INSENSITIVE_ORDER );
        return resultList;
    }

    /**
     * Gets the by language.
     *
     * @param language the language
     * @return the by language
     */
    public ResourceFile getByLanguage( String language )
    {
        return files.get( language );
    }

    /**
     * Gets the all properties.
     *
     * @return the all properties
     */
    public Map<String, Properties> getAllProperties()
    {
        Map<String, Properties> result = new HashMap<String, Properties>();
        for ( Map.Entry<String, ResourceFile> entry : files.entrySet() )
        {
            result.put( entry.getKey(), entry.getValue().getProperties() );
        }
        return result;
    }

    /**
     * Gets the project name.
     *
     * @return the project name
     */
    public String getProjectName()
    {
        Model pom = getPOMModel();
        if ( pom == null )
        {
            return "Unknown Project " + id;
        }
        else if ( StringUtils.isEmpty( pom.getName() ) )
        {
            return getMavenCoordinates();
        }
        else
        {
            return pom.getName();
        }
    }

    /**
     * Gets the maven coordinates.
     *
     * @return the maven coordinates
     */
    public String getMavenCoordinates()
    {
        Model model = getPOMModel();
        if ( model == null )
        {
            return "unknown:version";
        }
        
        StringBuilder coordinates = new StringBuilder();
        if ( StringUtils.isEmpty( model.getGroupId() ) )
        {
            coordinates.append( model.getParent().getGroupId() );
        }
        else
        {
            coordinates.append( model.getGroupId() );
        }
        coordinates.append( ":" );
        coordinates.append( model.getArtifactId() );
        coordinates.append( ":" );

        if ( StringUtils.isEmpty( model.getVersion() ) )
        {
            coordinates.append( model.getParent().getVersion() );
        }
        else
        {
            coordinates.append( model.getVersion() );
        }

        return coordinates.toString();
    }

    private Model getPOMModel()
    {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = null;
        try
        {
            File pomFile = findPOM();
            if ( pomFile != null )
            {
                model = reader.read( new InputStreamReader( new FileInputStream( pomFile ), "UTF-8" ) );
            }
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
        return model;
    }

    private File findPOM()
    {
        File result = null;
        String[] directories = localBasePath.split( Pattern.quote( File.separator ) );
        String[] stopDirectory = checkoutDirectory.split( Pattern.quote ( File.separator ) );
        for ( int i = directories.length - 1; i >= stopDirectory.length - 1 && i >= 0; i-- )
        {
            File f = new File( joinPaths( directories, i ), "pom.xml" );
            if ( f.exists() )
            {
                result = f;
                break;
            }
        }
        return result;
    }

    private static String joinPaths( String[] paths, int count )
    {
        StringBuilder sb = new StringBuilder();
        for ( int i = 0; i <= count && i < paths.length; i++ )
        {
            if ( i > 0 )
            {
                sb.append( File.separator );
            }
            sb.append( paths[i] );
        }
        return sb.toString();
    }

    /**
     * Gets the report.
     *
     * @param log the log
     * @return the report
     */
    public ReportModel getReport( Log log )
    {
        List<LanguageModel> allLanguages = new ArrayList<LanguageModel>();
        ResourceFile defaultFile = getDefaultFile();
        CheckConfiguration checks = config.getChecks();
        String baseName = null;
        if ( defaultFile != null )
        {
            allLanguages.add( defaultFile.toLanguageModel( log,
                    checks.getIgnoreKeys(),
                    checks.getIssuesThreshold() ) );
            baseName = defaultFile.getBaseName();
        }
        for ( String lang : getLanguages() )
        {
            allLanguages.add( getByLanguage( lang ).toLanguageModel( log,
                    checks.getIgnoreKeys(),
                    checks.getIssuesThreshold() ) );
        }

        ReportModel model = new ReportModel();
        model.setRelativeReportUrl( getRelativeReportUrl() );
        model.setProjectName( getProjectName() );
        model.setAllLanguages( allLanguages );
        model.setMavenCoordinates( getMavenCoordinates() );
        model.setGenerationDate( new Date().toString() );
        model.setVersion( DashboardMojo.getVersion() );
        model.setBasePath( localBasePath );
        model.setBaseName( baseName );
        model.setSvnCheckoutUrl( allLanguages.get( 0 ).getSvnCheckoutUrl() );
        return model;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId()
    {
        return getProjectName().replaceAll( "[^_a-zA-Z0-9]", "_" ) + "_" + id;
    }

    private String getRelativeReportUrl()
    {
        return getId() + ".html";
    }

    private String getRelativeTmxUrl()
    {
        return getId() + ".tmx";
    }

    /**
     * To bundle model.
     *
     * @param log the log
     * @param allLanguages the languages to include in the report
     * @return the bundle model
     */
    public BundleModel toBundleModel( Log log, List<String> allLanguages )
    {
        BundleModel model = new BundleModel();

        model.setProjectName( getProjectName() );
        if ( !files.isEmpty() )
        {
            ResourceFile firstFile = files.values().iterator().next();
            model.setBundleName( firstFile.getBaseName() );
        }
        model.setRelativeReportUrl( getRelativeReportUrl() );
        model.setRelativeTmxUrl( getRelativeTmxUrl() );

        CheckConfiguration checks = config.getChecks();
        ResourceFile defaultFile = getDefaultFile();
        if ( defaultFile != null )
        {
            model.setBase( defaultFile.toLanguageModel( log,
                    checks.getIgnoreKeys(),
                    checks.getIssuesThreshold() ) );
        }
        for ( String lang : allLanguages )
        {
            ResourceFile file = getByLanguage( lang );
            LanguageModel langModel;
            if ( file == null )
            {
                langModel = new LanguageModel();
                langModel.setName( lang );
                langModel.setExisting( false );
                langModel.setVariant( ResourceFile.isVariant( lang ) );
            }
            else
            {
                langModel = file.toLanguageModel( log,
                        checks.getIgnoreKeys(),
                        checks.getIssuesThreshold() );
            }
            model.addLanguage( langModel );
        }
        return model;
    }
}
