package net.sf.yal10n.report;

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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.sf.yal10n.dashboard.LanguageModel;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link ReportRenderer}.
 */
public class ReportRendererTest
{

    /**
     * Test the basic rendering process.
     */
    @Test
    public void testReportRendering()
    {
        ReportModel model = new ReportModel();
        model.setGenerationDate( "### the generation date ###" );
        model.setVersion( "### the version ###" );
        model.setMavenCoordinates( "### maven coordinates ###" );
        model.setBaseName( "### the base name ###" );
        model.setBasePath( "### the base path ###" );
        model.setProjectName( "### the project name ###" );
        List<LanguageModel> allLanguages = new ArrayList<LanguageModel>();
        LanguageModel lm = new LanguageModel();
        lm.setEncoding( "### the encoding ###" );
        allLanguages.add( lm );
        model.setAllLanguages( allLanguages );

        ReportRenderer renderer = new ReportRenderer();
        StringWriter out = new StringWriter();
        renderer.render( model, out );

        String output = out.toString();
        Assert.assertTrue( output.contains( model.getGenerationDate() ) );
        Assert.assertTrue( output.contains( model.getMavenCoordinates() ) );
        Assert.assertTrue( output.contains( model.getBaseName() ) );
        Assert.assertTrue( output.contains( model.getBasePath() ) );
        Assert.assertTrue( output.contains( model.getProjectName() ) );
        Assert.assertTrue( output.contains( lm.getEncoding() ) );
        Assert.assertTrue( output.contains( model.getVersion() ) );
    }

    /**
     * Verify that the report file is actually created.
     * @throws Exception any error
     */
    @Test
    public void testReportCreation() throws Exception
    {
        String expectedFilename = "myreport-" + UUID.randomUUID().toString() + ".html";
        File expectedOutputPath = new File( "./target/test-output/reportrenderertest/" );
        File completeExpectedOutputPath = new File( expectedOutputPath, "/reports/" );
        if ( !completeExpectedOutputPath.exists() )
        {
            Assert.assertTrue( "couldn't prepare output directory for test - stopping",
                    completeExpectedOutputPath.mkdirs() );
        }
        Assert.assertTrue( "test output path is not a directory: " + completeExpectedOutputPath,
                completeExpectedOutputPath.isDirectory() );
        ReportModel model = new ReportModel();
        model.setRelativeReportUrl( expectedFilename );

        
        ReportRenderer renderer = new ReportRenderer();
        renderer.render( model, expectedOutputPath.getCanonicalPath() );

        Assert.assertTrue( "report was not created",
                new File( completeExpectedOutputPath, expectedFilename ).exists() );
    }
}
