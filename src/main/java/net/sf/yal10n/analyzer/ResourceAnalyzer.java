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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.yal10n.settings.DashboardConfiguration;
import net.sf.yal10n.settings.Repository;
import net.sf.yal10n.svn.SVNUtil;

import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.FileUtils;

/**
 * The {@link ResourceAnalyzer} scans a directory for resource bundles.
 */
public class ResourceAnalyzer
{
    private Log log;
    private SVNUtil svn;
    private Map<String, ResourceBundle> bundles = new HashMap<String, ResourceBundle>();

    /**
     * Creates a new resource analyzer.
     * @param svn the svn utility
     * @param log the logger
     */
    public ResourceAnalyzer( SVNUtil svn, Log log )
    {
        this.svn = svn;
        this.log = log;
    }

    /**
     * Gets the found resource bundles. The key of the Map is the base directory of the bundle.
     * @return a map of the found resource bundles.
     */
    public Map<String, ResourceBundle> getBundles()
    {
        return bundles;
    }

    /**
     * Analyzes the given dstPath and adds any resource bundles found.
     *
     * @param svnUrl the svn url
     * @param dstPath the dst path
     * @param config the config
     * @param repo the repo
     * @param repoId the repo id
     */
    public void analyze( String svnUrl, String dstPath, DashboardConfiguration config, Repository repo, String repoId )
    {
        DirectoryScanner scanner = new DirectoryScanner();

        Set<String> allIncludes = new HashSet<String>();
        if ( config.getIncludes() != null )
        {
            allIncludes.addAll( config.getIncludes() );
        }
        if ( repo.getIncludes() != null )
        {
            allIncludes.addAll( repo.getIncludes() );
        }
        Set<String> allExcludes = new HashSet<String>();
        if ( config.getExcludes() != null )
        {
            allExcludes.addAll( config.getExcludes() );
        }
        if ( repo.getExcludes() != null )
        {
            allExcludes.addAll( repo.getExcludes() );
        }

        scanner.setBasedir( dstPath );
        scanner.setIncludes( allIncludes.toArray( new String[allIncludes.size()] ) );
        scanner.setExcludes( allExcludes.toArray( new String[allExcludes.size()] ) );
        scanner.scan();
        for ( String s : scanner.getIncludedFiles() )
        {
            try
            {
                log.debug( "found: " + s );

                String fullPath = new File( dstPath, s ).getCanonicalPath();
                String fullDstPath = new File( dstPath ).getCanonicalPath();
                String svnPath = svnUrl + "/" + s;
                ResourceFile resourceFile = new ResourceFile( config, svn, fullPath, svnPath );
                String baseBundleName = resourceFile.getBundleBaseName();

                ResourceBundle bundle = bundles.get( baseBundleName );
                if ( bundle == null )
                {
                    String svnBaseUrl = svnUrl + "/" + FileUtils.basename( s );
                    if ( svnBaseUrl.endsWith( "." ) )
                    {
                        svnBaseUrl = svnBaseUrl.substring( 0, svnBaseUrl.length() - 1 );
                    }

                    bundle = new ResourceBundle( config, svnBaseUrl, repoId, baseBundleName, fullDstPath );
                    bundles.put( baseBundleName, bundle );
                }
                bundle.addFile( resourceFile );
            }
            catch ( Exception e )
            {
                throw new RuntimeException( e );
            }
        }
    }
}
