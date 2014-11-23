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
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;

import net.sf.yal10n.dashboard.LanguageModel;
import net.sf.yal10n.dashboard.StatusClass;
import net.sf.yal10n.settings.DashboardConfiguration;
import net.sf.yal10n.settings.Repository;
import net.sf.yal10n.svn.SVNUtilMock;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link ResourceFile}.
 */
public class ResourceFileTest
{

    /**
     * Tests the loading of the properties.
     * @throws Exception any error
     */
    @Test
    public void testLoadProperties() throws Exception
    {
        File tempFile = File.createTempFile( "yal10n", null );
        tempFile.deleteOnExit();
        FileOutputStream out = new FileOutputStream( tempFile );
        out.write( "test=test de DE\n".getBytes( "UTF-8" ) );
        out.close();

        ResourceFile file = new ResourceFile( null, null, null, "", tempFile.getCanonicalPath(), null, null );
        Properties properties = file.getProperties();
        Assert.assertEquals( "test de DE", properties.getProperty( "test" ) );
    }

    /**
     * Tests whether the language is correctly determined.
     * @throws Exception any error
     */
    @Test
    public void testResourceFileLanguage() throws Exception
    {
        File resourceFile = new File( "./target/test-classes/unit/subdirectory/messages.properties" );
        ResourceFile file = new ResourceFile( null, null, null, "", resourceFile.getCanonicalPath(), null, null );
        Assert.assertTrue( file.isDefault() );
        Assert.assertFalse( file.isVariant() );
        Assert.assertEquals( "default", file.getLanguage() );
        Assert.assertEquals( Locale.ROOT, file.getLocale() );
        
        resourceFile = new File( "./target/test-classes/unit/subdirectory/messages_de.properties" );
        file = new ResourceFile( null, null, null, "", resourceFile.getCanonicalPath(), null, null );
        Assert.assertFalse( file.isDefault() );
        Assert.assertFalse( file.isVariant() );
        Assert.assertEquals( "de", file.getLanguage() );
        Assert.assertEquals( Locale.GERMAN, file.getLocale() );

        resourceFile = new File( "./target/test-classes/unit/subdirectory/messages_de_DE.properties" );
        file = new ResourceFile( null, null, null, "", resourceFile.getCanonicalPath(), null, null );
        Assert.assertFalse( file.isDefault() );
        Assert.assertTrue( file.isVariant() );
        Assert.assertEquals( "de_DE", file.getLanguage() );
        Assert.assertEquals( "DE", file.getLocale().getCountry() );
        Assert.assertEquals( new Locale( "de", "DE" ), file.getLocale() );
    }

    /**
     * Checks the base name determination.
     * @throws Exception any error
     */
    @Test
    public void testResourceBaseName() throws Exception
    {
        String directory = new File( "./target/test-classes/unit/subdirectory" ).getCanonicalPath();
        String resourceFile = directory + "/" + "messages_de_DE.properties";
        ResourceFile file = new ResourceFile( null, null, null, "", resourceFile, null, "http://full-svn-path" );
        Assert.assertEquals( "messages", file.getBaseName() );
        Assert.assertEquals( "messages de_DE", file.toString() );
        Assert.assertEquals( directory + "/messages", file.getBundleBaseName() );
        Assert.assertEquals( resourceFile, file.getFullLocalPath() );
        Assert.assertEquals( "/target/test-classes/unit/subdirectory/messages_de_DE.properties",
                file.getRelativeCheckoutUrl() );
        Assert.assertEquals( "http://full-svn-path", file.getSVNPath() );
    }

    /**
     * For reporting, the file is converted into a {@link LanguageModel}.
     * @throws Exception any error
     */
    @Test
    public void testLanguageModelConversion() throws Exception
    {
        TimeZone.setDefault( TimeZone.getTimeZone( "UTC" ) );
        ResourceBundle bundle = new ResourceBundle( null, null, null, "test", "test" );
        String checkoutPath = "./target/test-classes/unit";
        final String relativeFile = "subdirectory/messages.properties";
        final String relativeFile2 = "subdirectory/messages_de.properties";
        final String relativeFile3 = "subdirectory/messages_de_DE.properties";
        DashboardConfiguration config = new DashboardConfiguration();
        Repository repo = new Repository();
        String svnRepoUrl = "http://svn.foo.com/svn/test-project";
        List<String> ignoreKeys = Arrays.asList( "ignored.message", "ignored.default.message" );

        ResourceFile file = new ResourceFile( config, repo, svnRepoUrl, checkoutPath, relativeFile,
                new SVNUtilMock( relativeFile ),
                "http://svn.foo.com/svn/test-project/subdirectory/messages.properties" );
        bundle.addFile( file );
        LanguageModel languageModel = file.toLanguageModel( new NullLog(), ignoreKeys );
        Assert.assertEquals( 4, languageModel.getCountOfMessages() );
        Assert.assertEquals( "UTF8", languageModel.getEncoding() );
        Assert.assertEquals( StatusClass.OK, languageModel.getEncodingStatus() );
        Assert.assertEquals( "default", languageModel.getName() );
        Assert.assertEquals( "/target/test-classes/unit/subdirectory/messages.properties",
                languageModel.getRelativeUrl() );
        Assert.assertTrue( languageModel.getSvnInfo().startsWith( "Revision 1 (Sat Feb 23" ) );
        Assert.assertEquals( 0, languageModel.getNotTranslatedMessages().size() );
        Assert.assertEquals( 0, languageModel.getMissingMessages().size() );
        Assert.assertEquals( 0, languageModel.getAdditionalMessages().size() );
        Assert.assertEquals( "no-issues", languageModel.getIssuesSeverityClass() );
        Assert.assertEquals( 0, languageModel.getScoreLog().size() );
        Assert.assertEquals( "http://svn.foo.com/svn/test-project/subdirectory/", languageModel.getSvnCheckoutUrl() );

        ResourceFile file2 = new ResourceFile( config, repo, svnRepoUrl, "./target/test-classes/unit", relativeFile2,
                new SVNUtilMock( relativeFile2 ), null );
        bundle.addFile( file2 );
        LanguageModel languageModel2 = file2.toLanguageModel( new NullLog(), ignoreKeys );
        Assert.assertEquals( 4, languageModel2.getCountOfMessages() );
        Assert.assertEquals( "UTF8", languageModel2.getEncoding() );
        Assert.assertEquals( StatusClass.OK, languageModel2.getEncodingStatus() );
        Assert.assertEquals( "de", languageModel2.getName() );
        Assert.assertEquals( "/target/test-classes/unit/subdirectory/messages_de.properties",
                languageModel2.getRelativeUrl() );
        Assert.assertTrue( languageModel2.getSvnInfo().startsWith( "Revision 1 (Sat Feb 23" ) );
        Assert.assertEquals( 1, languageModel2.getNotTranslatedMessages().size() );
        Assert.assertTrue( languageModel2.getNotTranslatedMessages().containsKey( "not.translated.same" ) );
        Assert.assertEquals( 1, languageModel2.getMissingMessages().size() );
        Assert.assertTrue( languageModel2.getMissingMessages().containsKey( "not.translated.missing" ) );
        Assert.assertEquals( 1, languageModel2.getAdditionalMessages().size() );
        Assert.assertTrue( languageModel2.getAdditionalMessages().containsKey( "additional.message" ) );
        Assert.assertEquals( "severity-major", languageModel2.getIssuesSeverityClass() );
        Assert.assertEquals( 2, languageModel2.getScoreLog().size() );

        ResourceFile file3 = new ResourceFile( config, repo, svnRepoUrl, "./target/test-classes/unit", relativeFile3,
                new SVNUtilMock( relativeFile3 ), null );
        bundle.addFile( file3 );
        LanguageModel languageModel3 = file3.toLanguageModel( new NullLog(), ignoreKeys );
        Assert.assertTrue( languageModel3.isVariant() );
        Assert.assertEquals( "[Wrong Encoding (1.0): MALFORMED[1] at line 5 , column 114]",
                languageModel3.getScoreLog().toString() );
        Assert.assertEquals( "OTHER", languageModel3.getEncoding() );
        Assert.assertEquals( StatusClass.MAJOR_ISSUES, languageModel3.getEncodingStatus() );
    }
    
}
