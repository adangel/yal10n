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

import java.io.StringWriter;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Unit test for {@link IndentingXMLStreamWriter}.
 */
public class IndentingXMLStreamWriterTest
{

    /**
     * Basic test.
     * @throws Exception any error
     */
    @Test
    public void testIndenting() throws Exception
    {
        StringWriter stream = new StringWriter();
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        XMLStreamWriter delegate = factory.createXMLStreamWriter( stream );
        XMLStreamWriter writer = new IndentingXMLStreamWriter( delegate );

        writer.writeStartDocument( "UTF-8", "1.0" );
        writer.writeStartElement( "foo" );
        writer.writeStartElement( "bar" );
        writer.writeAttribute( "myAttribute", "someValue" );
        writer.writeCharacters( "buz" );
        writer.writeEndElement();
        writer.writeEndElement();
        writer.writeEndDocument();

        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<foo>\n"
                + "    <bar myAttribute=\"someValue\">buz</bar>\n"
                + "</foo>";
        Assert.assertEquals( expected, stream.toString() );
    }
}
