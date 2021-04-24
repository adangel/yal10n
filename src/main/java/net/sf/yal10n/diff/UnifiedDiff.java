package net.sf.yal10n.diff;

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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

/**
 * This class represents a diff produced by subversion.
 * It can convert the diff into a side-by-side comparison html format.
 */
public class UnifiedDiff
{
    private static final Pattern HUNK_START_PATTERN = Pattern.compile( "^@@ \\-(\\d+),(\\d+) \\+(\\d+),(\\d+) @@" );
    private String originalName;
    private String newName;
    private List<Hunk> hunks = new ArrayList<Hunk>();

    /**
     * Create a new {@link UnifiedDiff} with the given diff.
     * @param diffString the diff data
     */
    public UnifiedDiff( String diffString )
    {
        this( diffString, false, null );
    }

    /**
     * Creates a new {@link UnifiedDiff} with the given diff.
     * If the flag <code>newFile</code> is true, then not a diff is assumed,
     * simply the content of the new file is expected.
     * @param diffString the diff data or file content
     * @param newFile if <code>false</code>, a real diff is expected.
     * @param filename the file name. Only needed if not a real diff is provided.
     */
    public UnifiedDiff( String diffString, boolean newFile, String filename )
    {
        if ( !newFile )
        {
            parse( diffString );
        }
        else
        {
            parseNewFile( diffString, filename );
        }
    }

    private void parseNewFile( String content, String filename )
    {
        String[] lines = content.split( "\n" );
        int lineNumber = 1;
        Hunk hunk = new Hunk();
        hunk.firstLineNumber = lineNumber;
        for ( String line : lines )
        {
            hunk.newLines.put( lineNumber, line );
            hunk.indicators.put( lineNumber, '+' );
            lineNumber++;
        }
        hunk.lastLineNumber = lineNumber;
        hunks.add( hunk );
        originalName = "--";
        newName = String.valueOf( filename );
    }

    private void parse( String diff )
    {
        String[] lines = diff.split( "\n" );

        if ( lines.length < 5 )
        {
            throw new IllegalArgumentException( "The given diff is too short. " );
        }

        originalName = lines[2].substring( 4 );
        newName = lines[3].substring( 4 );

        int origLineNumber = 0;
        int newLineNumber = 0;
        Hunk currentHunk = null;

        for ( int i = 4; i < lines.length; i++ )
        {
            String line = lines[i];
            String nextLine = "\\\\";
            if ( i + 1 < lines.length )
            {
                nextLine = lines[i + 1];
            }
            if ( StringUtils.isEmpty( nextLine ) )
            {
                nextLine = "\\\\";
            }

            Matcher m = HUNK_START_PATTERN.matcher( line );
            boolean newHunkStarted = m.find();
            if ( newHunkStarted || StringUtils.isEmpty( line ) )
            {

                if ( currentHunk != null )
                {
                    currentHunk.lastLineNumber = newLineNumber;
                    calculateChangedLines( currentHunk );
                    hunks.add( currentHunk );
                    currentHunk = null;
                }

                if ( newHunkStarted )
                {
                    currentHunk = new Hunk();
                    origLineNumber = Integer.parseInt( m.group( 1 ) );
                    newLineNumber = origLineNumber;
                    currentHunk.firstLineNumber = origLineNumber;
                }
            }
            else
            {
                if ( currentHunk == null )
                {
                    // ignore this line outside of a hunk
                    continue;
                }

                char indicator = line.charAt( 0 );
                String diffLine = line.substring( 1 );

                switch ( indicator )
                {
                case ' ':
                    if ( origLineNumber < newLineNumber )
                    {
                        origLineNumber = newLineNumber;
                    }
                    else
                    {
                        newLineNumber = origLineNumber;
                    }
                    currentHunk.commonLines.put( origLineNumber, diffLine );
                    currentHunk.indicators.put( origLineNumber, ' ' );
                    origLineNumber++;
                    newLineNumber++;
                    break;
                case '-':
                    currentHunk.origLines.put( origLineNumber, diffLine );
                    currentHunk.indicators.put( origLineNumber, '-' );
                    origLineNumber++;
                    break;
                case '+':
                    currentHunk.newLines.put( newLineNumber, diffLine );
                    currentHunk.indicators.put( newLineNumber, '+' );
                    newLineNumber++;
                    break;
                default:
                    // ignore
                }
            }
        }
        if ( currentHunk != null )
        {
            currentHunk.lastLineNumber = newLineNumber;
            calculateChangedLines( currentHunk );
            hunks.add( currentHunk );
        }
    }

    private void calculateChangedLines( Hunk currentHunk )
    {
        for ( Integer line : currentHunk.origLines.keySet() )
        {
            if ( currentHunk.newLines.containsKey( line ) )
            {
                currentHunk.indicators.put( line, 'C' );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        StringBuilder out = new StringBuilder();
        out.append( "orig: " ).append( originalName ).append( "\n" );
        out.append( " new: " ).append( newName ).append( "\n" );
        out.append( "found " + hunks.size() + " hunks\n" );
        for ( Hunk hunk : hunks )
        {
            for ( int i = hunk.firstLineNumber; i < hunk.lastLineNumber; i++ )
            {
                Character indicator = hunk.indicators.get( i );

                out.append( indicator ).append( i ).append( "  " );

                switch ( indicator )
                {
                case '-':
                    out.append( hunk.origLines.get( i ) );
                    break;
                case ' ':
                    out.append( hunk.commonLines.get( i ) );
                    break;
                case '+':
                    out.append( hunk.newLines.get( i ) );
                    break;
                case 'C':
                default:
                    out.append( hunk.origLines.get( i ) ).append( " || " ).append( hunk.newLines.get( i ) );
                    break;
                }
                out.append( "\n" );
            }
        }
        return out.toString();
    }

    /**
     * Converts this diff into a side-by-side html format.
     * @return the html code
     */
    public String asHtmlSnippet()
    {
        final String lightYellow = "#ffff80";
        final String lightRed = "#f08080";
        final String lightGreen = "#90ee90";
        final String lightGrey = "#d3d3d3";
        final String darkCyan = "#008b8b";
        
        final String firstColumnStyle = " style=\"padding: 0 0.5em 0 0.5em; text-align: right;\"";

        StringBuilder out = new StringBuilder();
        out.append( "<div style=\"margin: 10px; border: 1px #aaaaaa solid; border-radius: 10px; padding: 5px;\">\n" );
        out.append( "<table border=\"0\" width=\"100%\" cellspacing=\"0\">\n" );
        out.append( "<tr><td" ).append( firstColumnStyle ).append( ">&nbsp;</td>" );
        out.append( "<td><strong>" ).append( StringEscapeUtils.escapeHtml4( originalName ) ).append( "</strong></td>" );
        out.append( "<td><strong>" ).append( StringEscapeUtils.escapeHtml4( newName ) ).append( "</strong></td>" );
        out.append( "</tr>\n" );

        for ( Hunk hunk : hunks )
        {
            out.append( "<tr style=\"background: " ).append( darkCyan ).append( ";\")>" );
            out.append( "<td" ).append( firstColumnStyle ).append( ">#</td>" );
            out.append( "<td><strong>Line " ).append( hunk.firstLineNumber ).append( "</td>" );
            out.append( "<td><strong>Line " ).append( hunk.firstLineNumber ).append( "</td>" );
            out.append( "</tr>" );

            for ( int i = hunk.firstLineNumber; i < hunk.lastLineNumber; i++ )
            {
                Character indicator = hunk.indicators.get( i );
                String currentLine = StringEscapeUtils.escapeHtml4( hunk.commonLines.get( i ) );
                String originalLine = StringEscapeUtils.escapeHtml4( hunk.origLines.get( i ) );
                String newLine = StringEscapeUtils.escapeHtml4( hunk.newLines.get( i ) );

                if ( originalLine == null || originalLine.isEmpty() )
                {
                    originalLine = "&nbsp;";
                }
                if ( currentLine == null || currentLine.isEmpty() )
                {
                    currentLine = "&nbsp;";
                }
                if ( newLine == null || newLine.isEmpty() )
                {
                    newLine = "&nbsp;";
                }

                out.append( "<tr>" );
                out.append( "<td" ).append( firstColumnStyle ).append( ">" ).append( i ).append( "</td>" );
                switch ( indicator )
                {
                case '-':
                    out.append( "<td style=\"background: " ).append( lightRed ).append( ";\">" )
                        .append( originalLine ).append( "</td>" );
                    out.append( "<td style=\"background: " ).append( lightGrey ).append( ";\">&nbsp;</td>" );
                    break;
                case ' ':
                    out.append( "<td>" ).append( currentLine ).append( "</td>" );
                    out.append( "<td>" ).append( currentLine ).append( "</td>" );
                    break;
                case '+':
                    out.append( "<td style=\"background: " ).append( lightGrey ).append( ";\">&nbsp;</td>" );
                    out.append( "<td style=\"background: " ).append( lightGreen ).append( ";\">" )
                        .append( newLine ).append( "</td>" );
                    break;
                case 'C':
                default:
                    out.append( "<td style=\"background: " ).append( lightYellow ).append( ";\">" )
                        .append( originalLine ).append( "</td>" );
                    out.append( "<td style=\"background: " ).append( lightYellow ).append( ";\">" )
                        .append( newLine ).append( "</td>" );
                    break;
                }
                out.append( "</tr>\n" );
            }
        }
        out.append( "</table>\n" );

        out.append( "<hr style=\"border: 1px #aaaaaa solid;\"/>\n" );
        out.append( "<table border=\"0\" cellspacing=\"0\" style=\"border: 1px #aaaaaa solid\">\n" );
        out.append( "<colgroup>\n" );
        out.append( "    <col width=\"50%\" />\n" );
        out.append( "    <col width=\"50%\" />\n" );
        out.append( "</colgroup>\n" );
        out.append( "<tr><td colspan=\"2\">Legend:</td></tr>\n" );
        out.append( "<tr><td style=\"background: " ).append( lightRed ).append( ";\">removed</td>" )
            .append( "<td style=\"background: " ).append( lightGrey ).append( ";\">&nbsp;</td></tr>\n" );
        out.append( "<tr><td colspan=\"2\" style=\"background: " ).append( lightYellow )
            .append( "; text-align: center;\">changed lines</td></tr>\n" );
        out.append( "<tr><td style=\"background: " ).append( lightGrey ).append( ";\">&nbsp;</td>" )
            .append( "<td style=\"background: " ).append( lightGreen ).append( ";\">added</td></tr>\n" );
        out.append( "</table>\n" );
        out.append( "</div>\n" );

        return out.toString();
    }

    /**
     * Gets the original name.
     *
     * @return the original name
     */
    public String getOriginalName()
    {
        return originalName;
    }

    /**
     * Gets the new name.
     *
     * @return the new name
     */
    public String getNewName()
    {
        return newName;
    }

    /**
     * Gets the hunks.
     *
     * @return the hunks
     */
    public List<Hunk> getHunks()
    {
        return hunks;
    }
}
