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

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Collection;

import net.sf.yal10n.analyzer.ResourceAnalyzer;
import net.sf.yal10n.analyzer.ResourceBundle;
import net.sf.yal10n.dashboard.DashboardModel;
import net.sf.yal10n.dashboard.DashboardRenderer;
import net.sf.yal10n.report.ReportModel;
import net.sf.yal10n.report.ReportRenderer;
import net.sf.yal10n.settings.DashboardConfiguration;
import net.sf.yal10n.settings.Repository;
import net.sf.yal10n.settings.ScmType;
import net.sf.yal10n.svn.SVNUtil;
import net.sf.yal10n.tmx.TranslationMemoryRenderer;

import org.apache.maven.plugin.logging.Log;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link DashboardMojo}.
 */
public class DashboardMojoTest
{

    /**
     * Tests the version.
     */
    @Test
    public void testVersion()
    {
        String version = DashboardMojo.getVersion();
        Assert.assertNotNull( version );
    }

    /**
     * Tests the interaction between {@link DashboardMojo#execute()} and the renderers.
     * @throws Exception any error
     */
    @SuppressWarnings( "unchecked" )
    @Test
    public void testExecute() throws Exception
    {
        SVNUtil svn = mock( SVNUtil.class );
        when( svn.checkout( (Log) anyObject(), (ScmType) anyObject(), anyString(), anyString() ) ).thenReturn( "1" );
        ResourceAnalyzer analyzer = mock( ResourceAnalyzer.class );
        doNothing().when( analyzer ).analyze( (Log) anyObject(), anyString(), anyString(),
                (DashboardConfiguration) anyObject(),
                (Repository) anyObject(), anyString() );
        DashboardRenderer dashboardRenderer = mock( DashboardRenderer.class );
        ReportRenderer reportRenderer = mock( ReportRenderer.class );
        when( reportRenderer.prepareOutputDirectory( anyString() ) ).thenReturn( true );
        TranslationMemoryRenderer tmxRenderer = mock( TranslationMemoryRenderer.class );
        File settings = new File( "target/test-classes/settings/yal10n-settings-sample.json" );
        
        DashboardMojo mojo = new DashboardMojo( svn, analyzer,
                dashboardRenderer,
                reportRenderer,
                tmxRenderer );
        mojo.setYal10nSettings( settings.getAbsolutePath() );
        mojo.setOutputDirectory( System.getProperty( "java.io.tmpdir" ) );
        mojo.execute();

        verify( svn, times( 3 ) ).checkout( (Log) anyObject(), (ScmType) anyObject(), anyString(), anyString() );
        verify( analyzer, times( 3 ) ).analyze( (Log) anyObject(), anyString(), anyString(),
                (DashboardConfiguration) anyObject(), (Repository) anyObject(), anyString() );
        verify( analyzer, times( 1 ) ).getBundles();
        verify( dashboardRenderer, times( 1 ) ).render( (DashboardModel) anyObject(), anyString() );
        verify( reportRenderer, times( 1 ) ).prepareOutputDirectory( anyString() );
        verify( reportRenderer, times( 0 ) ).render( (ReportModel) anyObject(), anyString() );
        verify( tmxRenderer, times( 1 ) ).render( (Collection<ResourceBundle>) anyObject(), anyString() );
        verify( tmxRenderer, times( 0 ) ).render( (Log) anyObject(), (ResourceBundle) anyObject(), anyString(),
                anyListOf( String.class ) );

        verifyNoMoreInteractions( svn, analyzer, dashboardRenderer, reportRenderer, tmxRenderer );
    }
}
