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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.yal10n.charset.UTF8BOMCharsetProvider;
import net.sf.yal10n.dashboard.LanguageModel;
import net.sf.yal10n.settings.DashboardConfiguration;
import net.sf.yal10n.svn.SVNInfo;
import net.sf.yal10n.svn.SVNUtil;

import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;

/**
 * A {@link ResourceFile} represents a single locale of a {@link ResourceBundle}.
 */
public class ResourceFile
{
    private static final String DEFAULT_LANGUAGE = "default";
    private static final double SEVERITY_MINOR = 0.5;
    private ResourceBundle bundle;
    private SVNUtil svn;
    private String fullLocalPath;
    private String fullSvnPath;
    private DashboardConfiguration config;
    private String svnRepoUrl;
    private String checkedOutPath;
    private String relativeFilePath;
    private Properties properties;
    private String language;

    /**
     * Creates a new resource file that can be analyzed.
     *
     * @param config the config
     * @param svnRepoUrl the url to the repository
     * @param checkedOutPath the path where the repository has been checked out
     * @param relativeFilePath the relative file path of the resource file
     * @param svn the svn
     * @param fullLocalPath the full local path
     * @param fullSvnPath the full svn path
     */
    public ResourceFile( DashboardConfiguration config, String svnRepoUrl, String checkedOutPath,
            String relativeFilePath,
            SVNUtil svn, String fullLocalPath, String fullSvnPath )
    {
        this.config = config;
        this.svnRepoUrl = svnRepoUrl;
        this.checkedOutPath = checkedOutPath;
        this.relativeFilePath = relativeFilePath;
        this.svn = svn;
        this.fullLocalPath = fullLocalPath;
        this.fullSvnPath = fullSvnPath;
        loadProperties();
        determineLanguage();
    }

    /**
     * Sets the bundle.
     *
     * @param bundle the new bundle
     */
    public void setBundle( ResourceBundle bundle )
    {
        this.bundle = bundle;
    }

    private void loadProperties()
    {
        properties = new Properties();
        Reader reader = null;
        try
        {
            reader = new InputStreamReader( new FileInputStream( new File( fullLocalPath ) ),
                    new UTF8BOMCharsetProvider().charsetForName( "UTF-8-BOM" ) );
            properties.load( reader );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
        finally
        {
            IOUtil.close( reader );
        }
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return getBaseName() + " " + getLocale();
    }

    /**
     * Gets the SVN path.
     *
     * @return the sVN path
     */
    public String getSVNPath()
    {
        return fullSvnPath;
    }

    /**
     * Gets the full local path.
     *
     * @return the full local path
     */
    public String getFullLocalPath()
    {
        return fullLocalPath;
    }

    /**
     * Gets the properties.
     *
     * @return the properties
     */
    public Properties getProperties()
    {
        return properties;
    }

    /**
     * Gets the language.
     *
     * @return the language
     */
    public String getLanguage()
    {
        return language;
    }
    
    /**
     * Determines the language
     */
    private void determineLanguage()
    {
        String baseName = FileUtils.basename( fullLocalPath );
        if ( baseName.endsWith( "." ) )
        {
            baseName = baseName.substring( 0, baseName.length() - 1 );
        }
        if ( baseName.contains( "_" ) )
        {
            language = baseName.substring( baseName.indexOf( "_" ) + 1 );
        }
        else
        {
            language = DEFAULT_LANGUAGE;
        }
    }

    /**
     * Gets the locale.
     *
     * @return the locale
     */
    public Locale getLocale()
    {
        Locale result = Locale.ROOT;
        if ( !isDefault() )
        {
            if ( !isVariant() )
            {
                result = new Locale( language );
            }
            else
            {
                String[] parts = language.split( "_", 2 );
                result = new Locale( parts[0], parts[1] );
            }
        }
        return result;
    }

    /**
     * Checks if is variant.
     *
     * @return true, if is variant
     */
    public boolean isVariant()
    {
        return isVariant( getLanguage() );
    }

    /**
     * Utility method to check whether a given language code is a variant (e.g. de_DE).
     * @param languageCode the code
     * @return <code>true</code> if the code is a variant.
     */
    public static boolean isVariant( String languageCode )
    {
        return languageCode != null && languageCode.contains( "_" );
    }

    /**
     * Checks if this file contains default language.
     *
     * @return true, if is default
     */
    public boolean isDefault()
    {
        return DEFAULT_LANGUAGE.equals( getLanguage() );
    }

    /**
     * Gets the relative checkout url.
     *
     * @return the relative checkout url
     */
    public String getRelativeCheckoutUrl()
    {
        try
        {
            String currentDir = new File( "." ).getCanonicalPath();
            String localPath = new File( fullLocalPath ).getCanonicalPath();
            return localPath.substring( currentDir.length() );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * To language model.
     *
     * @param log the log
     * @param ignoreKeys the ignore keys
     * @return the language model
     */
    public LanguageModel toLanguageModel( Log log, List<String> ignoreKeys )
    {
        LanguageModel model = new LanguageModel();
        model.setSvnUrl( fullSvnPath );
        model.setSvnCheckoutUrl( fullSvnPath != null ? FileUtils.dirname( fullSvnPath ) + "/" : null );
        model.setRelativeUrl( getRelativeCheckoutUrl() );
        model.setName( getLanguage() );
        SimpleEncodingDetector detector = new SimpleEncodingDetector();
        EncodingResult detectedEncoding = detector.detectEncoding( new File( fullLocalPath ) );
        model.setEncoding( detectedEncoding.getDetected().name() );
        model.setCountOfMessages( getProperties().size() );
        model.setExisting( true );
        model.setVariant( isVariant() );

        Map<String, String> notTranslatedKeys = new HashMap<String, String>();
        Map<String, String> missingKeys = new HashMap<String, String>();
        Map<String, String> additionalKeys = new HashMap<String, String>();

        ResourceFile defaultFile = bundle.getDefaultFile();
        if ( defaultFile != null && defaultFile != this )
        {
            Properties defaultProperties = defaultFile.getProperties();
            for ( Object o : defaultProperties.keySet() )
            {
                String key = o.toString();
                if ( !isIgnoreKey( key, ignoreKeys ) )
                {
                    String defaultProperty = defaultProperties.getProperty( key );
                    String translatedProperty = getProperties().getProperty( key );

                    if ( translatedProperty == null )
                    {
                        missingKeys.put( key, defaultProperty );
                    }
                    else if ( defaultProperty.equals( translatedProperty ) )
                    {
                        notTranslatedKeys.put( key, defaultProperty );
                    }
                }
            }

            for ( Object o : getProperties().keySet() )
            {
                String key = o.toString();
                if ( !isIgnoreKey( key, ignoreKeys ) && defaultProperties.getProperty( key ) == null )
                {
                    additionalKeys.put( key, getProperties().getProperty( key ) );
                }
            }
        }

        model.setNotTranslatedMessages( notTranslatedKeys );
        model.setMissingMessages( missingKeys );
        model.setAdditionalMessages( additionalKeys );

        SVNInfo svnInfo = svn.checkFile( log, svnRepoUrl, checkedOutPath, relativeFilePath );
        String info = "Revision " + svnInfo.getRevision() + " (" + svnInfo.getCommittedDate() + ")";
        model.setSvnInfo( info );

        double score = 0.0;
        List<String> scoreLog = new ArrayList<String>();

        score = executeChecks( detectedEncoding, notTranslatedKeys, missingKeys, additionalKeys, defaultFile, score,
                scoreLog );

        String issuesSeverityClass;
        if ( score >= 1.0 )
        {
            issuesSeverityClass = "severity-major";
        }
        else if ( score >= SEVERITY_MINOR )
        {
            issuesSeverityClass = "severity-minor";
        }
        else
        {
            issuesSeverityClass = "no-issues";
        }
        model.setIssuesSeverityClass( issuesSeverityClass );
        model.setScoreLog( scoreLog );

        return model;
    }

    private double executeChecks( EncodingResult detectedEncoding, Map<String, String> notTranslatedKeys,
            Map<String, String> missingKeys, Map<String, String> additionalKeys, ResourceFile defaultFile,
            double startScore, List<String> scoreLog )
    {
        double score = startScore;
        // wrong encoding
        if ( detectedEncoding.getDetected() == Encoding.OTHER )
        {
            score += 1.0;
            scoreLog.add( "Wrong Encoding (1.0): "
                    + detectedEncoding.getError()
                    + " at line " + detectedEncoding.getErrorLine()
                    + " , column " + detectedEncoding.getErrorColumn() );
        }
        // missing base file
        if ( defaultFile == null )
        {
            score += 1.0;
            scoreLog.add( "Missing default file (1.0)" );
        }
        // more than 10 % not translated or missing
        if ( defaultFile != null && !isVariant() )
        {
            double missingPercentage = 100.0 * ( notTranslatedKeys.size() + missingKeys.size() )
                    / defaultFile.getProperties().size();
            if ( missingPercentage > config.getChecks().getPercentageMissing() )
            {
                score += SEVERITY_MINOR;
                scoreLog.add( String.format( Locale.ENGLISH, "%.2f %% missing or not translated keys (0.5)",
                        missingPercentage ) );
            }
        }
        // at least one additional
        if ( !additionalKeys.isEmpty() )
        {
            score += SEVERITY_MINOR;
            scoreLog.add( "There are additional keys (0.5)" );
        }

        if ( config.getChecks().isCheckFileHeaders() )
        {
            try
            {
                String s = IOUtil.toString( new InputStreamReader( new FileInputStream( new File( fullLocalPath ) ),
                        new UTF8BOMCharsetProvider().charsetForName( "UTF-8-BOM" ) ) );
                Pattern p = Pattern.compile( config.getChecks().getFileHeaderRegexp(), Pattern.MULTILINE );

                Matcher m = p.matcher( s );
                if ( !m.find() || m.start() > 0 )
                {
                    scoreLog.add( "File header missing or not a the beginning of the file (0.5)" );
                    score += SEVERITY_MINOR;
                }

            }
            catch ( Exception e )
            {
                throw new RuntimeException( e );
            }
        }

        if ( detectedEncoding.getDetected() == Encoding.UTF8_BOM )
        {
            BufferedReader reader = null;
            try
            {
                reader = new BufferedReader( new InputStreamReader( new FileInputStream( new File( fullLocalPath ) ),
                        new UTF8BOMCharsetProvider().charsetForName( "UTF-8-BOM" ) ) );

                String firstLine = reader.readLine();
                if ( firstLine != null )
                {
                    firstLine = firstLine.trim();
                    if ( !firstLine.startsWith( "#" ) && firstLine.indexOf( '=' ) > -1 )
                    {
                        scoreLog.add( "File has BOM, but first line contains already a message."
                                + "Please add a blank line. (0.5)" );
                        score += SEVERITY_MINOR;
                    }
                }

            }
            catch ( Exception e )
            {
                throw new RuntimeException( e );
            }
            finally
            {
                IOUtil.close( reader );
            }
        }
        return score;
    }

    private boolean isIgnoreKey( String key, List<String> ignoreKeys )
    {
        if ( ignoreKeys != null )
        {
            return ignoreKeys.contains( key );
        }
        return false;
    }

    /**
     * Returns the base name of this file without the filename suffix and without the locale.
     * @return the base name
     */
    public String getBaseName()
    {
        String basename = FileUtils.basename( fullLocalPath );
        if ( basename.endsWith( "." ) )
        {
            basename = basename.substring( 0, basename.length() - 1 );
        }
        int underscoreIndex = basename.indexOf( '_' );
        if ( underscoreIndex > -1 )
        {
            basename = basename.substring( 0, underscoreIndex );
        }
        return basename;
    }
    
    /**
     * Returns the full bundle path.
     * @return the full bunde base name.
     */
    public String getBundleBaseName()
    {
        String directory = FileUtils.dirname( fullLocalPath );
        String filename = getBaseName();
        return directory + "/" + filename;
    }
}
