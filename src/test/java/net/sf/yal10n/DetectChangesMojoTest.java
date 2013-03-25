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

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.File;

import net.sf.yal10n.analyzer.ResourceAnalyzer;
import net.sf.yal10n.settings.DashboardConfiguration;
import net.sf.yal10n.settings.Repository;
import net.sf.yal10n.svn.SVNUtil;

import org.apache.maven.plugin.logging.Log;
import org.junit.Test;

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
        when( svn.checkout( (Log) anyObject(), anyString(), anyString() ) ).thenReturn( 1L );
        ResourceAnalyzer analyzer = mock( ResourceAnalyzer.class );
        doNothing().when( analyzer ).analyze( (Log) anyObject(), anyString(), anyString(),
                (DashboardConfiguration) anyObject(),
                (Repository) anyObject(), anyString() );
        File settings = new File( "target/test-classes/settings/yal10n-settings-sample.json" );
        File status = new File( "target/test-classes/status/yal10n-status-sample.json" );

        DetectChangesMojo mojo = new DetectChangesMojo( svn, analyzer );
        mojo.setYal10nSettings( settings.getAbsolutePath() );
        mojo.setOutputDirectory( System.getProperty( "java.io.tmpdir" ) );
        mojo.setYal10nStatus( status.getAbsolutePath() );
        mojo.execute();

        verify( svn, times( 3 ) ).checkout( (Log) anyObject(), anyString(), anyString() );
        verify( analyzer, times( 3 ) ).analyze( (Log) anyObject(), anyString(), anyString(),
                (DashboardConfiguration) anyObject(), (Repository) anyObject(), anyString() );
        verify( analyzer, times( 1 ) ).getBundles();

        verifyNoMoreInteractions( svn, analyzer );
    }
}
