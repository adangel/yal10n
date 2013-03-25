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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.EscapeTool;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;

/**
 * The report renderer creates a html report of single file with velocity.
 */
@Component( role = ReportRenderer.class, hint = "ReportRenderer" )
public class ReportRenderer
{
    private String reportDirectory;

    void render( ReportModel model, Writer out )
    {
        VelocityEngine ve = new VelocityEngine();
        Properties vProperties = new Properties();
        vProperties.put( "resource.loader", "class" );
        vProperties.put( "class.resource.loader.description", "Velocity Classpath Resource Loader" );
        vProperties.put( "class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader" );
        ve.init( vProperties );
        Template template = ve.getTemplate( "velocity/report.vm" );
        VelocityContext ctx = new VelocityContext();
        ctx.put( "report", model );
        ctx.put( "esc", new EscapeTool() );
        template.merge( ctx, out );
    }

    /**
     * Renders the given report model.
     *
     * @param model the report to render.
     * @param outputDirectory the output directory
     */
    public void render( ReportModel model, String outputDirectory )
    {
        Writer out = null;
        try
        {
            out = new OutputStreamWriter( new FileOutputStream( FileUtils.normalize( outputDirectory + "/reports/"
                    + model.getRelativeReportUrl() ) ), "UTF-8" );
            render( model, out );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
        finally
        {
            IOUtil.close( out );
        }
    }

    /**
     * Makes sure the report output directory exists.
     *
     * @param outputDirectory the output directory
     * @return <code>true</code> if the output report directory exists and is usable, <code>false</code> otherwise.
     * Problem could be, that the directory couldn't be created or the reportDirectory is not a directory.
     * @see #getReportDirectory()
     */
    public boolean prepareOutputDirectory( String outputDirectory )
    {
        boolean result = true;
        reportDirectory = FileUtils.normalize( outputDirectory + "/reports" );
        File reportDirectoryFile = new File( reportDirectory );
        if ( !reportDirectoryFile.exists() && !reportDirectoryFile.mkdirs() || !reportDirectoryFile.isDirectory() )
        {
            result = false;
        }
        return result;
    }

    /**
     * Gets the directory path the is used for the report output directory.
     *
     * @return the report output directory.
     */
    public String getReportDirectory()
    {
        return reportDirectory;
    }
}
