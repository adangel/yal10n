package net.sf.yal10n.analyzer;

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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.MalformedInputException;
import java.util.Locale;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * Exploratory tests for message bundles which are encoded with UTF-8 and a BOM.
 */
public class ExploratoryEncodingTest
{
    private static final Pattern JAVA_VERSION_PATTERN = Pattern.compile( "(\\d+)[\\.\\d]*" );

    private byte[] data;
    Charset utf8 = Charset.forName( "UTF-8" );

    @BeforeClass
    public static void assumeAtLeastJava9()
    {
        String javaVersion = System.getProperty( "java.version" );
        Matcher matcher = JAVA_VERSION_PATTERN.matcher( javaVersion );
        if ( matcher.find() )
        {
            int major = Integer.parseInt( matcher.group( 1 ) );
            Assume.assumeTrue( "Java Runtime needs to be 9+, but was: " + major , major >= 9 );
        }
    }

    /** 
     * Initialize the unit test - setup the test data.
     * @throws Exception any error
     */
    @Before
    public void init() throws Exception
    {
        data = "This is malformed UTF-8 äöü - because it's not UTF-8 encoded ß".getBytes( "ISO-8859-1" );
    }

    /**
     * Test how malformed input is reported.
     * @throws Exception any error
     */
    @Test( expected = MalformedInputException.class )
    public void testMalformedEncoding() throws Exception
    {
        ByteBuffer buffer = ByteBuffer.wrap( data );

        utf8.newDecoder().onMalformedInput( CodingErrorAction.REPORT ).onUnmappableCharacter( CodingErrorAction.REPORT )
                .decode( buffer );
    }

    /**
     * Test how malformed input is reported in a coder result.
     * @throws Exception any error
     */
    @Test
    public void testMalformedEncodingResult() throws Exception
    {
        ByteBuffer buffer = ByteBuffer.wrap( data );

        CharsetDecoder decoder = utf8.newDecoder().onMalformedInput( CodingErrorAction.REPORT )
                .onUnmappableCharacter( CodingErrorAction.REPORT );
        CharBuffer chars = CharBuffer.allocate( 100 );
        CoderResult result = decoder.decode( buffer, chars, false );
        Assert.assertTrue( result.isMalformed() );
    }

    /**
     * Test to load java properties files with a BOM.
     * @throws Exception any error
     */
    @Test
    public void testPropertiesWithBOM() throws Exception
    {
        Properties properties = new Properties();
        InputStream in = ExploratoryEncodingTest.class.getResourceAsStream( "/UTF8BOM.properties" );

        // ------
        // need to skip 3 bytes, the BOM bytes
        // in.skip(3);
        // ------

        properties.load( new InputStreamReader( in, "UTF-8" ) );
        Assert.assertEquals( 3, properties.size() );

        // Assert.assertEquals("first key", properties.getProperty("testkey1"));
        // note: utf-8 bom read as first character
        Assert.assertEquals( "first key", properties.getProperty( "\ufefftestkey1" ) );

        Assert.assertEquals( "second key", properties.getProperty( "testkey2" ) );
        Assert.assertEquals( "This file is encoded as UTF-8 with BOM ä.", properties.getProperty( "description" ) );
    }

    /**
     * Test to load java properties files with a BOM, when the first line starts with a comment.
     * @throws Exception any error
     */
    @Test
    public void testPropertiesWithBOMandComment() throws Exception
    {
        Properties properties = new Properties();
        InputStream in = ExploratoryEncodingTest.class.getResourceAsStream( "/UTF8BOMwithComment.properties" );

        properties.load( new InputStreamReader( in, "UTF-8" ) );
        Assert.assertEquals( 4, properties.size() );

        Assert.assertEquals( "", properties.get( "\ufeff#abc" ) );
        Assert.assertEquals( "first key", properties.getProperty( "testkey1" ) );
        Assert.assertEquals( "second key", properties.getProperty( "testkey2" ) );
        Assert.assertEquals( "This file is encoded as UTF-8 with BOM ä.", properties.getProperty( "description" ) );
    }

    /**
     * Test to load java properties with a BOM, when the first line is blank.
     * @throws Exception any error
     */
    @Test
    public void testPropertiesWithBOMandBlankLine() throws Exception
    {
        Properties properties = new Properties();
        InputStream in = ExploratoryEncodingTest.class.getResourceAsStream( "/UTF8BOMwithBlankLine.properties" );

        properties.load( new InputStreamReader( in, "UTF-8" ) );
        Assert.assertEquals( 4, properties.size() );

        Assert.assertEquals( "", properties.get( "\ufeff" ) );
        Assert.assertEquals( "first key", properties.getProperty( "testkey1" ) );
        Assert.assertEquals( "second key", properties.getProperty( "testkey2" ) );
        Assert.assertEquals( "This file is encoded as UTF-8 with BOM ä.", properties.getProperty( "description" ) );
    }

    /**
     * Test to load a reource bundle from a BOM encoded file.
     * @throws Exception any error
     */
    @Test
    public void testResourceBundleWithBOM() throws Exception
    {
        ResourceBundle bundle = PropertyResourceBundle.getBundle( "UTF8BOM", Locale.ROOT );
        Assert.assertEquals( 3, bundle.keySet().size() );

        // utf-8 bom is part of the first key
        Assert.assertEquals( "first key", bundle.getString( "\ufefftestkey1" ) );
        // before java 9: utf-8 bom read as iso-8859-1
        //Assert.assertEquals( "first key", bundle.getString( "\u00ef\u00bb\u00bftestkey1" ) );

        Assert.assertEquals( "second key", bundle.getString( "testkey2" ) );
        Assert.assertEquals( "This file is encoded as UTF-8 with BOM ä.",
                bundle.getString( "description" ) );
    }

    /**
     * Test to load a resource bundle from a BOM encoded file that starts with a blank line.
     * @throws Exception any error
     */
    @Test
    public void testResourceBundleWithBOMandBlankLine() throws Exception
    {
        ResourceBundle bundle = PropertyResourceBundle.getBundle( "UTF8BOMwithBlankLine", Locale.ROOT );
        Assert.assertEquals( 4, bundle.keySet().size() );

        Assert.assertEquals( "first key", bundle.getString( "testkey1" ) );
        Assert.assertEquals( "second key", bundle.getString( "testkey2" ) );
        Assert.assertEquals( "This file is encoded as UTF-8 with BOM ä.",
                bundle.getString( "description" ) );
    }

    /**
     * Test to load a resource bundle from a BOM encoded file that starts with a comment.
     * @throws Exception any error
     */
    @Test
    public void testResourceBundleWithBOMandComment() throws Exception
    {
        ResourceBundle bundle = PropertyResourceBundle.getBundle( "UTF8BOMwithComment", Locale.ROOT );
        Assert.assertEquals( 4, bundle.keySet().size() );

        // utf-8 bom makes the first comment line a property
        Assert.assertEquals( "", bundle.getString( "\ufeff#abc" ) );
        Assert.assertEquals( "first key", bundle.getString( "testkey1" ) );
        Assert.assertEquals( "second key", bundle.getString( "testkey2" ) );
        Assert.assertEquals( "This file is encoded as UTF-8 with BOM ä.",
                bundle.getString( "description" ) );
    }

    /**
     * Test to load a resource bundle via Spring's message source when the file is
     * encoded with a BOM.
     * @throws Exception any error
     */
    @Test
    public void testSpringMessageSourceBOMDefault() throws Exception
    {
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setBasename( "UTF8BOM" );
        source.setDefaultEncoding( "UTF-8" );
        source.setFallbackToSystemLocale( false );

        // Assert.assertEquals("first key", source.getMessage("testkey1", null, Locale.ROOT));
        // note: utf-8 bom read as first character
        Assert.assertEquals( "first key", source.getMessage( "\ufefftestkey1", null, Locale.ROOT ) );
        Assert.assertEquals( "second key", source.getMessage( "testkey2", null, Locale.ROOT ) );
        Assert.assertEquals( "This file is encoded as UTF-8 with BOM ä.",
                source.getMessage( "description", null, Locale.ROOT ) );
    }

    /**
     * Test to load a resource bundle via Spring's message source when the file is
     * encoded with a BOM and starts with a blank line.
     * @throws Exception any error
     */
    @Test
    public void testSpringMessageSourceBOMandBlankLine() throws Exception
    {
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setBasename( "UTF8BOMwithBlankLine" );
        source.setDefaultEncoding( "UTF-8" );
        source.setFallbackToSystemLocale( false );

        Assert.assertEquals( "first key", source.getMessage( "testkey1", null, Locale.ROOT ) );
        Assert.assertEquals( "second key", source.getMessage( "testkey2", null, Locale.ROOT ) );
        Assert.assertEquals( "This file is encoded as UTF-8 with BOM ä.",
                source.getMessage( "description", null, Locale.ROOT ) );
    }

    /**
     * Test to load a resource bundle via Spring's message source when the file
     * is encoded with a BOM and starts with a comment. 
     * @throws Exception any error
     */
    @Test
    public void testSpringMessageSourceBOMandComment() throws Exception
    {
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setBasename( "UTF8BOMwithComment" );
        source.setDefaultEncoding( "UTF-8" );
        source.setFallbackToSystemLocale( false );

        Assert.assertEquals( "", source.getMessage( "\ufeff#abc", null, Locale.ROOT ) );
        Assert.assertEquals( "first key", source.getMessage( "testkey1", null, Locale.ROOT ) );
        Assert.assertEquals( "second key", source.getMessage( "testkey2", null, Locale.ROOT ) );
        Assert.assertEquals( "This file is encoded as UTF-8 with BOM ä.",
                source.getMessage( "description", null, Locale.ROOT ) );
    }

    /**
     * Test to load a resource bundle via Spring's message source when the file
     * is encoded with a BOM and the correct encoding is used.
     * @throws Exception any error
     */
    @Test
    public void testSpringMessageSourceBOM() throws Exception
    {
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setBasename( "UTF8BOM" );
        source.setDefaultEncoding( "UTF-8-BOM" );
        source.setFallbackToSystemLocale( false );

        Assert.assertEquals( "first key", source.getMessage( "testkey1", null, Locale.ROOT ) );
        Assert.assertEquals( "second key", source.getMessage( "testkey2", null, Locale.ROOT ) );
        Assert.assertEquals( "This file is encoded as UTF-8 with BOM ä.",
                source.getMessage( "description", null, Locale.ROOT ) );
    }
}
