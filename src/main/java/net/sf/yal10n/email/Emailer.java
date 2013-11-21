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

import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

/**
 * Helper class to actually send off a email.
 */
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
     * @param properties the properties. Needs properties for <code>mail.smtp.host</code>
     * and <code>mail.smtp.port</code>
     * @param from the from address
     * @param recipients the recipients
     * @param subject the subject
     * @param content the content
     * @param projectName the project name
     */
    public void sendEmail( Properties properties, InternetAddress from, List<Address> recipients,
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

            Transport.send( msg );

            getLog().info( "Email sent for project " + projectName + " to " + recipients );
        }
        catch ( MessagingException mex )
        {
            throw new RuntimeException( mex );
        }
    }
}
