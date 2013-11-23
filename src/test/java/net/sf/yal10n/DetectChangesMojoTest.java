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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;

import net.sf.yal10n.analyzer.ResourceAnalyzer;
import net.sf.yal10n.diff.UnifiedDiff;
import net.sf.yal10n.email.Emailer;
import net.sf.yal10n.settings.DashboardConfiguration;
import net.sf.yal10n.settings.Notification;
import net.sf.yal10n.settings.Repository;
import net.sf.yal10n.settings.ScmType;
import net.sf.yal10n.svn.SVNUtil;

import org.apache.maven.plugin.logging.Log;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * @author Andreas Dangel
 *
 */
public class DetectChangesMojoTest
{

    /**
     * Test execution of the detect changes mojo.
     *
     * @throws Exception the exception
     */
    @Test
    public void testExecute() throws Exception
    {
        SVNUtil svn = mock( SVNUtil.class );
        when( svn.checkout( (Log) anyObject(), (ScmType) anyObject(), anyString(), anyString() ) ).thenReturn( "1" );
        ResourceAnalyzer analyzer = mock( ResourceAnalyzer.class );
        doNothing().when( analyzer ).analyze( (Log) anyObject(), anyString(), anyString(),
                (DashboardConfiguration) anyObject(),
                (Repository) anyObject(), anyString() );
        Emailer emailer = mock( Emailer.class );

        File settings = new File( "target/test-classes/settings/yal10n-settings-sample.json" );
        File status = new File( "target/test-classes/status/yal10n-status-sample.json" );

        DetectChangesMojo mojo = new DetectChangesMojo( svn, analyzer, emailer );
        mojo.setYal10nSettings( settings.getAbsolutePath() );
        mojo.setOutputDirectory( System.getProperty( "java.io.tmpdir" ) );
        mojo.setYal10nStatus( status.getAbsolutePath() );
        mojo.execute();

        verify( svn, times( 3 ) ).checkout( any( Log.class ), any( ScmType.class ), anyString(), anyString() );
        verify( analyzer, times( 3 ) ).analyze( any( Log.class ), anyString(), anyString(),
                any( DashboardConfiguration.class ), any( Repository.class ), anyString() );
        verify( analyzer, times( 1 ) ).getBundles();
        verify( emailer, times( 1 ) ).setLog( any( Log.class ) );

        verifyNoMoreInteractions( svn, analyzer, emailer );
    }

    /**
     * Verify the email creation.
     * @throws Exception any error
     */
    @Test
    public void testCreateEmail() throws Exception
    {
        DashboardConfiguration config = new DashboardConfiguration();
        config.setNotification( new Notification() );
        config.getNotification().setMailFrom( "sender" );
        config.getNotification().setRecipients( "recipient1, recipient2" );
        config.getNotification().setSmtpServer( "server" );
        config.getNotification().setSmtpPort( "587" );
        config.getNotification().setSubject( "localization {{projectName}}" );
        Repository repo = new Repository();
        String projectName = "Foo";
        String viewvcDiff = "http://viewvc/r=1";
        UnifiedDiff unifiedDiff = new UnifiedDiff( "bar", true, "bar.file" );
        List<Address> recipients = new ArrayList<Address>();
        recipients.add( new InternetAddress( "recipient1" ) );
        recipients.add( new InternetAddress( "recipient2" ) );

        Emailer emailer = mock( Emailer.class );
        DetectChangesMojo mojo = new DetectChangesMojo( null, null, emailer );
        ArgumentCaptor<String> content = ArgumentCaptor.forClass( String.class );

        mojo.createAndSendEmail( config, repo, projectName, viewvcDiff, unifiedDiff );
        verify( emailer, times( 1 ) ).sendEmail( anyBoolean(), any( Properties.class ),
                eq( new InternetAddress( "sender" ) ),
                eq( recipients ), eq( "localization Foo" ), content.capture(), eq( projectName ) );
        verifyNoMoreInteractions( emailer );

        Assert.assertTrue( content.getValue().contains( "Changes detected in <strong>Foo</strong>" ) );
        Assert.assertTrue( content.getValue().contains( viewvcDiff ) );
        Assert.assertTrue( content.getValue().contains( unifiedDiff.asHtmlSnippet() ) );
    }
}
