package net.sf.yal10n.dashboard;

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
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import net.sf.yal10n.DashboardMojo;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link DashboardRenderer}.
 */
public class DashboardRendererTest
{

    /**
     * Checks the rendering content.
     */
    @Test
    public void testDashboardRenderer()
    {
        StringWriter out = new StringWriter();
        DashboardRenderer renderer = new DashboardRenderer( "." );

        DashboardModel model = createModel();

        renderer.render( model, out );
        String dashboard = out.toString();
        Assert.assertTrue( dashboard.contains( "<td>Bundle 1</td>" ) );
        Assert.assertTrue( dashboard.contains( "<a href=\"bundle1/base\">default</a>" ) );
        Assert.assertTrue( dashboard.contains( "<a href=\"bundle1/de\">de</a>" ) );
        Assert.assertTrue( dashboard.contains( "<a href=\"bundle1/fr\">fr</a>" ) );
        Assert.assertTrue( dashboard.contains( "<a href=\"reports/bundle1\">report</a>" ) );
        Assert.assertTrue( dashboard.contains( "<td>Bundle 2</td>" ) );
        Assert.assertTrue( dashboard.contains( "<a href=\"bundle2/base\">default</a>" ) );
        Assert.assertTrue( dashboard.contains( DashboardMojo.getVersion() ) );
    }

    /**
     * Checks that a file is actually created by the rendering.
     * @throws Exception any error
     */
    @Test
    public void testDashboardCreation() throws Exception
    {
        File expectedOutputDirectory = new File( "./target/test-output/dashboardrenderertest/test-run-"
                + UUID.randomUUID().toString() );
        File expectedFile = new File( expectedOutputDirectory, "dashboard.html" );
        if ( !expectedOutputDirectory.exists() )
        {
            Assert.assertTrue( "Couldn't create output directory: " + expectedOutputDirectory,
                expectedOutputDirectory.mkdirs() );
        }
        Assert.assertTrue( "Output directory is not a directory: " + expectedOutputDirectory,
                expectedOutputDirectory.isDirectory() );

        DashboardModel model = new DashboardModel();
        DashboardRenderer renderer = new DashboardRenderer( expectedOutputDirectory.getCanonicalPath() );
        renderer.render( model );

        Assert.assertTrue( expectedFile.exists() );
    }

    private DashboardModel createModel()
    {
        BundleModel bundle1 = createBundleModel( "Bundle 1", "bundle1" );
        BundleModel bundle2 = createBundleModel( "Bundle 2", "bundle2" );

        DashboardModel model = new DashboardModel();
        model.setAllLanguages( Arrays.asList( "de", "fr" ) );
        model.setAllBundles( Arrays.asList( bundle1, bundle2 ) );
        model.setGenerationDate( new Date().toString() );
        model.setVersion( DashboardMojo.getVersion() );

        return model;
    }

    private BundleModel createBundleModel( String projectName, String reportUrl )
    {
        BundleModel bundle1 = new BundleModel();
        bundle1.setProjectName( projectName );
        bundle1.setRelativeReportUrl( reportUrl );
        bundle1.setBase( createLanguageModel( reportUrl + "/base" ) );
        bundle1.getLanguages().put( "de", createLanguageModel( reportUrl + "/de" ) );
        bundle1.getLanguages().put( "fr", createLanguageModel( reportUrl + "/fr" ) );
        return bundle1;
    }

    private LanguageModel createLanguageModel( String svnUrl )
    {
        LanguageModel base = new LanguageModel();
        base.setSvnUrl( svnUrl );
        return base;
    }
}
