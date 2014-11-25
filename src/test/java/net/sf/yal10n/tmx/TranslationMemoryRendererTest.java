package net.sf.yal10n.tmx;

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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import net.sf.yal10n.analyzer.NullLog;
import net.sf.yal10n.analyzer.ResourceAnalyzer;
import net.sf.yal10n.analyzer.ResourceBundle;
import net.sf.yal10n.settings.DashboardConfiguration;
import net.sf.yal10n.settings.Repository;
import net.sf.yal10n.svn.SVNUtil;
import net.sf.yal10n.svn.SVNUtilMock;

import org.codehaus.plexus.util.IOUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link TranslationMemoryRenderer}.
 */
public class TranslationMemoryRendererTest
{
    /**
     * Test to convert a single bundle.
     * @throws Exception any error
     */
    @Test
    public void testSingleBundle() throws Exception
    {
        File outputDirectory = new File( "./target/test-output/translationmemory/" );
        File completeDirectory = new File( outputDirectory, "/reports" );
        if ( !completeDirectory.exists() )
        {
            Assert.assertTrue( "Could create output directory for test: " + completeDirectory,
                    completeDirectory.mkdirs() );
        }
        Assert.assertTrue( "Not a directory: " + completeDirectory, completeDirectory.isDirectory() );

        TranslationMemoryRenderer renderer = new TranslationMemoryRenderer();
        ResourceBundle bundle = createBundle().iterator().next();
        renderer.render( new NullLog(), bundle, outputDirectory.getCanonicalPath(), Collections.<String> emptyList() );

        String relativeTmxUrl = bundle.toBundleModel( new NullLog(), bundle.getLanguages(),
                Collections.<String> emptyList() ).getRelativeTmxUrl();
        Assert.assertTrue( new File( completeDirectory, relativeTmxUrl ).exists() );
        String fileData = IOUtil.toString( new FileInputStream( new File( completeDirectory, relativeTmxUrl ) ),
                "UTF-8" );
        assertContains( "tuid=\"http://svn/repo/messages:not.translated.missing\"", fileData );
        assertContains( "<prop type=\"x-key\">not.translated.missing</prop>", fileData );
        assertContains( "xml:lang=\"de\"", fileData );
        assertContains( "this is a sample for for unit testing in locale German", fileData );
    }

    private static void assertContains( String expected, String data )
    {
        if ( !data.contains( expected ) )
        {
            Assert.fail( "Expected to find \"" + expected + "\", but didn't. Full data:\n" + data );
        }
    }

    /**
     * Test to convert a collection of bundles.
     * @throws Exception any error
     */
    @Test
    public void testCollection() throws Exception
    {
        File outputDirectory = new File( "./target/test-output/translationmemory/" );
        File completeDirectory = new File( outputDirectory, "/reports" );
        if ( !completeDirectory.exists() )
        {
            Assert.assertTrue( "Could create output directory for test: " + completeDirectory,
                    completeDirectory.mkdirs() );
        }
        Assert.assertTrue( "Not a directory: " + completeDirectory, completeDirectory.isDirectory() );

        TranslationMemoryRenderer renderer = new TranslationMemoryRenderer();
        Collection<ResourceBundle> bundle = createBundle();
        renderer.render( bundle, outputDirectory.getCanonicalPath() );

        String relativeTmxUrl = "all-translations.tmx";
        Assert.assertTrue( new File( outputDirectory, relativeTmxUrl ).exists() );
        String fileData = IOUtil.toString( new FileInputStream( new File( outputDirectory, relativeTmxUrl ) ),
                "UTF-8" );
        assertContains( "http://svn/repo/messages:not.translated.same", fileData );
    }

    private Collection<ResourceBundle> createBundle() throws Exception
    {
        SVNUtil svnUtil = new SVNUtilMock( null );
        ResourceAnalyzer analyzer = new ResourceAnalyzer( svnUtil );
        DashboardConfiguration config = new DashboardConfiguration();
        Repository repo = new Repository();
        repo.setUrl( "http://svn/repo" );
        repo.setIncludes( Arrays.asList( "**/messages*.properties" ) );
        String repoId = SVNUtil.toRepoId( "", repo.getUrl() );
        config.getRepositories().add( repo );

        analyzer.analyze( new NullLog(), repo.getUrl(),
                new File( "./target/test-classes/unit/subdirectory/" ).getCanonicalPath(),
                config, repo, repoId );

        return analyzer.getBundles();
    }
}
