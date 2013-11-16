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
import java.util.Arrays;
import java.util.List;

import net.sf.yal10n.dashboard.BundleModel;
import net.sf.yal10n.dashboard.LanguageModel;
import net.sf.yal10n.report.ReportModel;
import net.sf.yal10n.settings.DashboardConfiguration;
import net.sf.yal10n.settings.Repository;
import net.sf.yal10n.svn.SVNUtilMock;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit test for {@link ResourceBundle}.
 */
public class ResourceBundleTest
{
    private static ResourceBundle bundle;
    private static String resourceFile;
    private static ResourceFile file;
    private static ResourceFile fileDe;
    private static String svnUrl = "http://svn-url";
    private static List<String> allLanguages = Arrays.asList( "de", "fr", "de_DE" );

    /**
     * Setup a resource bundle for testing.
     * @throws Exception any error
     */
    @BeforeClass
    public static void setup() throws Exception
    {
        DashboardConfiguration config = new DashboardConfiguration();
        Repository repo = new Repository();
        String repoId = null;
        String checkoutDir = new File( "./target/test-classes/unit" ).getCanonicalPath();
        String localBasePath = checkoutDir + "/subdirectory";
        ResourceBundle.COUNTER.set( -1 );
        bundle = new ResourceBundle( config, svnUrl, repoId, localBasePath, checkoutDir );
        resourceFile = new File( "./target/test-classes/unit/subdirectory/messages.properties" )
            .getCanonicalPath();
        String relativeFilePath = "subdirectory/messages.properties";
        file = new ResourceFile( config, repo, null, checkoutDir, relativeFilePath,
                new SVNUtilMock( relativeFilePath ), null );
        bundle.addFile( file );
        String relativeFilePathDe = "subdirectory/messages_de.properties";
        fileDe = new ResourceFile( config, repo, null, checkoutDir, relativeFilePathDe,
                new SVNUtilMock( relativeFilePathDe ), null );
        bundle.addFile( fileDe );
    }

    /**
     * Tests the language list.
     * @throws Exception any error
     */
    @Test
    public void testLanguageAndOtherGetters() throws Exception
    {
        Assert.assertEquals( "[de]", bundle.getLanguages().toString() );
        Assert.assertSame( fileDe, bundle.getByLanguage( "de" ) );
    }

    /**
     * Tests the svn url getter.
     */
    @Test
    public void testSvnUrl()
    {
        Assert.assertEquals( svnUrl, bundle.getSvnUrl() );
    }

    /**
     * Tests the toString method.
     */
    @Test
    public void testToString()
    {
        Assert.assertTrue( bundle.toString().contains( "de=messages de" ) );
    }

    /**
     * Tests the defaultFile resolution.
     */
    @Test
    public void testDefaultFile()
    {
        Assert.assertEquals( resourceFile, bundle.getDefaultFile().getFullLocalPath() );
    }

    /**
     * Tests getAllProperties.
     */
    @Test
    public void testProperties()
    {
        Assert.assertEquals( 2, bundle.getAllProperties().size() );
    }

    /**
     * Test the project name generation.
     * @throws Exception any error
     */
    @Test
    public void testProjectName() throws Exception
    {
        Assert.assertEquals( "Unknown Project 0", bundle.getProjectName() );
        String pomDir = new File( "./target/test-classes/unit/testpom" ).getCanonicalPath();
        ResourceBundle bundlePOM = new ResourceBundle( null, null, null, pomDir, pomDir );
        Assert.assertEquals( "Test Sample Project", bundlePOM.getProjectName() );
        Assert.assertEquals( "net.sf.yal10n:test-project:1.0.0-SNAPSHOT", bundlePOM.getMavenCoordinates() );
    }

    /**
     * Tests the bundle model.
     */
    @Test
    public void testBundleModel()
    {
        BundleModel bundleModel = bundle.toBundleModel( new NullLog(), allLanguages );
        Assert.assertNotNull( bundleModel.getBase() );
        Assert.assertEquals( "default", bundleModel.getBase().getName() );
        Assert.assertEquals( "messages", bundleModel.getBundleName() );
        Assert.assertEquals( "Unknown_Project_0_0.html", bundleModel.getRelativeReportUrl() );
        Assert.assertEquals( "Unknown_Project_0_0.tmx", bundleModel.getRelativeTmxUrl() );
        Assert.assertEquals( 3, bundleModel.getLanguages().size() );
        assertLanguageModel( bundleModel.getLanguages().get( 0 ), "de", true, false );
        assertLanguageModel( bundleModel.getLanguages().get( 1 ), "fr", false, false );
        assertLanguageModel( bundleModel.getLanguages().get( 2 ), "de_DE", false, true );
    }

    private static void assertLanguageModel( LanguageModel model, String name, boolean existing, boolean variant )
    {
        Assert.assertNotNull( model );
        Assert.assertEquals( name, model.getName() );
        Assert.assertEquals( existing, model.isExisting() );
        Assert.assertEquals( variant, model.isVariant() );
    }

    /**
     * Tests the report model.
     */
    @Test
    public void testReportModel()
    {
        ReportModel report = bundle.getReport( new NullLog() );
        Assert.assertEquals( "messages", report.getBaseName() );
        Assert.assertEquals( 2, report.getAllLanguages().size() );
    }

    /**
     * Test whether the id can be used as a filename, e.g. the slash "/" is removed.
     */
    @Test
    public void testGetId()
    {
        String localBasePath = "./target/test-classes/unit/testpom2";
        ResourceBundle testBundle = new ResourceBundle( null, null, null, localBasePath, localBasePath );
        String id = testBundle.getId();
        Assert.assertFalse( id.contains( "/" ) );
        Assert.assertTrue( id.startsWith( "Test_Sample_Project___With_a_Slash_" ) );
    }
}
