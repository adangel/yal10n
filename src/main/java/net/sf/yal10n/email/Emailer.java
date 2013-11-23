package net.sf.yal10n.email;

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

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.codehaus.plexus.component.annotations.Component;

/**
 * Helper class to actually send off a email.
 */
@Component( role = Emailer.class, hint = "Emailer" )
public class Emailer
{
    private Log log;

    /**
     * Gets the log.
     *
     * @return the log
     */
    public Log getLog()
    {
        if ( log == null )
        {
            log = new SystemStreamLog();
        }
        return log;
    }


    /**
     * Sets the log.
     *
     * @param log the new log
     */
    public void setLog( Log log )
    {
        this.log = log;
    }

    /**
     * Sends the email with the given content.
     *
     * @param skip don't send the email, only log it.
     * @param properties the properties. Needs properties for <code>mail.smtp.host</code>
     * and <code>mail.smtp.port</code>
     * @param from the from address
     * @param recipients the recipients
     * @param subject the subject
     * @param content the content
     * @param projectName the project name
     */
    public void sendEmail( boolean skip, Properties properties, InternetAddress from, List<Address> recipients,
            String subject, String content, String projectName )
    {
        Session session = Session.getInstance( properties );
        try
        {
            MimeMessage msg = new MimeMessage( session );
            msg.setFrom( from );
            msg.setRecipients( Message.RecipientType.TO, recipients.toArray( new Address[recipients.size()] ) );
            msg.setSubject( subject );
            msg.setSentDate( new Date() );
            msg.setContent( content, "text/html" );

            if ( !skip )
            {
                Transport.send( msg );
                getLog().info( "Email sent for project " + projectName + " to " + recipients );
            }
            else
            {
                getLog().info( "SkipEmail is true. Do not send the following message:" );
                getLog().info( "-----------------------------------------------------" );
                getLog().info( asString( msg ) );
                getLog().info( "-----------------------------------------------------" );
            }
        }
        catch ( MessagingException mex )
        {
            throw new RuntimeException( mex );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
    }

    private String asString( MimeMessage msg ) throws MessagingException, IOException
    {
        StringBuilder sb = new StringBuilder( "\n" );
        sb.append( "From: " );
        if ( msg.getFrom() != null )
        {
            for ( Address a : msg.getFrom() )
            {
                sb.append( a ).append( "; " );
            }
        }
        sb.append( "\n" );
        sb.append( "To: " );
        if ( msg.getRecipients( RecipientType.TO ) != null )
        {
            for ( Address a : msg.getRecipients( RecipientType.TO ) )
            {
                sb.append( a ).append( "; " );
            }
        }
        sb.append( "\n" );
        sb.append( "Subject: " ).append( msg.getSubject() ).append( "\n" );
        sb.append( "Date: " ).append( msg.getSentDate() ).append( "\n" );
        sb.append( "Content-Type: " ).append( msg.getContentType() ).append( "\n" );
        sb.append( "\n" );
        sb.append( msg.getContent() );
        sb.append( "\n.\n" );
        return sb.toString();
    }
}
