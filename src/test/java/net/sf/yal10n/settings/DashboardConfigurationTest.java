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

import junit.framework.Assert;

import net.sf.yal10n.svn.SVNUtil;

import org.junit.Test;

/**
 * Unit test for {@link DashboardConfiguration}.
 */
public class DashboardConfigurationTest
{

    /**
     * Reads a config file.
     * @throws Exception any error
     */
    @Test
    public void testRead() throws Exception
    {
        File file = new File( "./target/test-classes/settings/yal10n-settings-sample.json" );
        DashboardConfiguration config = DashboardConfiguration.readFromFile( file.getCanonicalPath() );
        
        Assert.assertEquals( "file:///svnrepos/", config.getRepoPrefix() );
        Assert.assertEquals( "http://svn/viewvc/", config.getViewvcPrefix() );
        Assert.assertEquals( 3, config.getRepositories().size() );
        Repository repo1 = config.getRepositories().get( 0 );
        Assert.assertEquals( "repo1/trunk", repo1.getUrl() );
        Assert.assertEquals( "repo1/trunk", repo1.getViewvcUrl() );
        Assert.assertEquals( ScmType.SVN, repo1.getType() );
        Assert.assertTrue( config.isCreateTMX() );
        Assert.assertNull( config.getRepositoryById( "foo" ) );
        Assert.assertNotNull( config.getRepositoryById( SVNUtil.toRepoId( "file:///svnrepos/", "repo1/trunk" ) ) );
        Assert.assertEquals( "localhost", config.getNotification().getSmtpServer() );
        Assert.assertEquals( "25", config.getNotification().getSmtpPort() );
        Assert.assertEquals( "[yal10n] test subject", config.getNotification().getSubject() );
        Assert.assertEquals( "foo@bar.com, bar@foo.com", config.getNotification().getRecipients() );
        Assert.assertEquals( "foo@bar.com", config.getNotification().getMailFrom() );
        Assert.assertEquals( "[de, fr, ja, nl]", config.getLanguages().toString() );
        Assert.assertEquals( 1, config.getIncludes().size() );
        Assert.assertEquals( "**/*essages*.properties", config.getIncludes().get( 0 ) );
        Assert.assertEquals( 1, config.getExcludes().size() );
        Assert.assertEquals( "**/src/test/**", config.getExcludes().get( 0 ) );
    }
}
