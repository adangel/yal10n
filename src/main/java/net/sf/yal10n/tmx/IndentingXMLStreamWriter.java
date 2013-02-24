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

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * A XML Stream writer that produces pretty formatted XML.
 */
public class IndentingXMLStreamWriter implements XMLStreamWriter
{
    private static final String SINGLE_INDENT = "    ";
    private static final String NEWLINE = "\n";
    private XMLStreamWriter delegate;
    private String indent = "";
    private boolean needNewline = false;

    /**
     * Instantiates a new indenting xml stream writer.
     *
     * @param delegate the delegate
     */
    public IndentingXMLStreamWriter( XMLStreamWriter delegate )
    {
        this.delegate = delegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeStartElement( String localName ) throws XMLStreamException
    {
        delegate.writeCharacters( NEWLINE );
        delegate.writeCharacters( indent );
        delegate.writeStartElement( localName );
        indent += SINGLE_INDENT;
        needNewline = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeStartElement( String namespaceURI, String localName ) throws XMLStreamException
    {
        delegate.writeStartElement( namespaceURI, localName );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeStartElement( String prefix, String localName, String namespaceURI ) throws XMLStreamException
    {
        delegate.writeStartElement( prefix, localName, namespaceURI );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeEmptyElement( String namespaceURI, String localName ) throws XMLStreamException
    {
        delegate.writeEmptyElement( namespaceURI, localName );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeEmptyElement( String prefix, String localName, String namespaceURI ) throws XMLStreamException
    {
        delegate.writeEmptyElement( prefix, localName, namespaceURI );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeEmptyElement( String localName ) throws XMLStreamException
    {
        delegate.writeEmptyElement( localName );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeEndElement() throws XMLStreamException
    {
        indent = indent.substring( SINGLE_INDENT.length() );
        if ( needNewline )
        {
            delegate.writeCharacters( NEWLINE );
            delegate.writeCharacters( indent );
        }
        delegate.writeEndElement();
        needNewline = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeEndDocument() throws XMLStreamException
    {
        delegate.writeEndDocument();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws XMLStreamException
    {
        delegate.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flush() throws XMLStreamException
    {
        delegate.flush();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeAttribute( String localName, String value ) throws XMLStreamException
    {
        delegate.writeAttribute( localName, value );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeAttribute( String prefix, String namespaceURI, String localName, String value )
        throws XMLStreamException
    {
        delegate.writeAttribute( prefix, namespaceURI, localName, value );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeAttribute( String namespaceURI, String localName, String value ) throws XMLStreamException
    {
        delegate.writeAttribute( namespaceURI, localName, value );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeNamespace( String prefix, String namespaceURI ) throws XMLStreamException
    {
        delegate.writeNamespace( prefix, namespaceURI );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeDefaultNamespace( String namespaceURI ) throws XMLStreamException
    {
        delegate.writeDefaultNamespace( namespaceURI );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeComment( String data ) throws XMLStreamException
    {
        delegate.writeComment( data );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeProcessingInstruction( String target ) throws XMLStreamException
    {
        delegate.writeProcessingInstruction( target );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeProcessingInstruction( String target, String data ) throws XMLStreamException
    {
        delegate.writeProcessingInstruction( target, data );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeCData( String data ) throws XMLStreamException
    {
        delegate.writeCData( data );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeDTD( String dtd ) throws XMLStreamException
    {
        delegate.writeDTD( dtd );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeEntityRef( String name ) throws XMLStreamException
    {
        delegate.writeEntityRef( name );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeStartDocument() throws XMLStreamException
    {
        delegate.writeStartDocument();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeStartDocument( String version ) throws XMLStreamException
    {
        delegate.writeStartDocument( version );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeStartDocument( String encoding, String version ) throws XMLStreamException
    {
        delegate.writeStartDocument( encoding, version );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeCharacters( String text ) throws XMLStreamException
    {
        delegate.writeCharacters( text );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeCharacters( char[] text, int start, int len ) throws XMLStreamException
    {
        delegate.writeCharacters( text, start, len );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPrefix( String uri ) throws XMLStreamException
    {
        return delegate.getPrefix( uri );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPrefix( String prefix, String uri ) throws XMLStreamException
    {
        delegate.setPrefix( prefix, uri );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaultNamespace( String uri ) throws XMLStreamException
    {
        delegate.setDefaultNamespace( uri );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNamespaceContext( NamespaceContext context ) throws XMLStreamException
    {
        delegate.setNamespaceContext( context );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NamespaceContext getNamespaceContext()
    {
        return delegate.getNamespaceContext();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getProperty( String name )
    {
        return delegate.getProperty( name );
    }
}
