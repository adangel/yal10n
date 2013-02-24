package net.sf.yal10n.settings;

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

/**
 * The configuration about the notification for changes in the messages files.
 */
public class Notification
{
    private String smtpServer;
    private String smtpPort = "25";
    private String subject = "[yal10n] Changes detect in {{projectName}}";
    private String recipients;
    private String mailFrom;

    /**
     * Gets the configured smtp server.
     * @return the smtp server
     */
    public String getSmtpServer()
    {
        return smtpServer;
    }

    /**
     * Sets the configured smtp server.
     * @param smtpServer the smtp server
     */
    public void setSmtpServer( String smtpServer )
    {
        this.smtpServer = smtpServer;
    }

    /**
     * Gets the smtp port.
     * @return the port
     */
    public String getSmtpPort()
    {
        return smtpPort;
    }

    /**
     * Sets the smtp port.
     * @param smtpPort the port
     */
    public void setSmtpPort( String smtpPort )
    {
        this.smtpPort = smtpPort;
    }

    /**
     * Gets the subject.
     *
     * @return the subject
     */
    public String getSubject()
    {
        return subject;
    }

    /**
     * Sets the subject.
     *
     * @param subject the new subject
     */
    public void setSubject( String subject )
    {
        this.subject = subject;
    }

    /**
     * Gets the recipients. A comma-separated list of email addresses.
     *
     * @return the recipients
     */
    public String getRecipients()
    {
        return recipients;
    }

    /**
     * Sets the recipients. A comma-separated list of email addresses.
     *
     * @param recipients the new recipients
     */
    public void setRecipients( String recipients )
    {
        this.recipients = recipients;
    }

    /**
     * Gets the mail from.
     *
     * @return the mail from
     */
    public String getMailFrom()
    {
        return mailFrom;
    }

    /**
     * Sets the mail from.
     *
     * @param mailFrom the new mail from
     */
    public void setMailFrom( String mailFrom )
    {
        this.mailFrom = mailFrom;
    }
}
