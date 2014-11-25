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

import java.io.FileOutputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import net.sf.yal10n.DashboardMojo;
import net.sf.yal10n.analyzer.ResourceBundle;
import net.sf.yal10n.dashboard.BundleModel;

import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Creates a translation memory exchange file from one or more {@link ResourceBundle}s.
 */
@Component( role = TranslationMemoryRenderer.class, hint = "TranslationMemoryRenderer" )
public class TranslationMemoryRenderer
{
    /**
     * Creates one file which contains all bundles.
     *
     * @param bundles the bundles to convert.
     * @param outputDirectory the output directory
     */
    public void render( Collection<ResourceBundle> bundles, String outputDirectory )
    {
        FileOutputStream stream = null;
        try
        {
            stream = new FileOutputStream( FileUtils.normalize( outputDirectory + "/all-translations.tmx" ) );
            XMLOutputFactory factory = XMLOutputFactory.newInstance();
            XMLStreamWriter out = new IndentingXMLStreamWriter( factory.createXMLStreamWriter( stream, "UTF-8" ) );

            writeBegin( out );

            Set<String> languages = new HashSet<String>();
            for ( ResourceBundle b : bundles )
            {
                languages.addAll( b.getLanguages() );
            }

            for ( ResourceBundle b : bundles )
            {
                writeBundle( out, languages, b );
            }

            writeEnd( out );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
        finally
        {
            IOUtil.close( stream );
        }
    }

    private void writeEnd( XMLStreamWriter out ) throws XMLStreamException
    {
        out.writeEndElement();
        out.writeEndElement();
        out.writeEndDocument();
        out.close();
    }

    private void writeBundle( XMLStreamWriter out, Set<String> languages, ResourceBundle b ) throws XMLStreamException
    {
        Map<String, Properties> allProperties = b.getAllProperties();
        if ( allProperties.containsKey( "default" ) )
        {
            Properties defaultProps = allProperties.get( "default" );
            for ( Object o : defaultProps.keySet() )
            {
                String s = (String) o;

                out.writeStartElement( "tu" );

                out.writeAttribute( "tuid", b.getSvnUrl() + ":" + s );

                writeProperty( out, "x-svn-base-path", b.getSvnUrl() );
                writeProperty( out, "x-key", s );

                writeMessage( out, "en", defaultProps.getProperty( s ) );
                for ( String l : languages )
                {
                    Properties langfile = allProperties.get( l );
                    if ( langfile != null )
                    {
                        writeMessage( out, l, langfile.getProperty( s ) );
                    }
                }
                out.writeEndElement();
            }
        }
    }

    private void writeProperty( XMLStreamWriter out, String propertyType, String propertyValue )
        throws XMLStreamException
    {
        out.writeStartElement( "prop" );
        out.writeAttribute( "type", propertyType );
        out.writeCharacters( propertyValue );
        out.writeEndElement();
    }

    private void writeBegin( XMLStreamWriter out ) throws XMLStreamException
    {
        out.writeStartDocument( "UTF-8", "1.0" );
        out.writeStartElement( "tmx" );
        out.writeAttribute( "version", "1.4b" );
        out.writeStartElement( "header" );
        out.writeAttribute( "creationtool", "yal10n" );
        out.writeAttribute( "creationtoolversion", DashboardMojo.getVersion() );
        out.writeAttribute( "datatype", "plaintext" );
        out.writeAttribute( "segtype", "sentence" );
        out.writeAttribute( "adminlang", "en-us" );
        out.writeAttribute( "srclang", "en" );
        DateTimeFormatter formatter = DateTimeFormat.forPattern( "yyyyMMdd'T'HHmmss'Z'" );
        out.writeAttribute( "creationdate", DateTime.now( DateTimeZone.UTC ).toString( formatter ) );
        out.writeEndElement();
        out.writeStartElement( "body" );
    }

    private void writeMessage( XMLStreamWriter out, String lang, String message ) throws XMLStreamException
    {
        if ( message != null )
        {
            out.writeStartElement( "tuv" );
            out.writeAttribute( "xml", "http://www.w3.org/XML/1998/namespace", "lang", lang );
            out.writeStartElement( "seg" );
            out.writeCharacters( message );
            out.writeEndElement();
            out.writeEndElement();
        }
    }

    /**
     * Creates one file which contains only the one given bundle.
     *
     * @param log the log
     * @param bundle the bundle
     * @param outputDirectory the output directory
     * @param includeVariants the include variants
     */
    public void render( Log log, ResourceBundle bundle, String outputDirectory, List<String> includeVariants )
    {
        BundleModel bundleModel = bundle.toBundleModel( log, bundle.getLanguages(), includeVariants );
        FileOutputStream stream = null;
        try
        {
            stream = new FileOutputStream( FileUtils.normalize( outputDirectory + "/reports/"
                    + bundleModel.getRelativeTmxUrl() ) );
            XMLOutputFactory factory = XMLOutputFactory.newInstance();
            XMLStreamWriter out = new IndentingXMLStreamWriter( factory.createXMLStreamWriter( stream, "UTF-8" ) );

            writeBegin( out );

            Set<String> languages = new HashSet<String>();
            languages.addAll( bundle.getLanguages() );

            writeBundle( out, languages, bundle );

            writeEnd( out );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
        finally
        {
            IOUtil.close( stream );
        }
    }
}
