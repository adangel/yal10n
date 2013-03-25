package net.sf.yal10n.svn;

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

import org.apache.maven.plugin.logging.Log;
import org.tmatesoft.svn.core.SVNCancelException;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.ISVNEventHandler;
import org.tmatesoft.svn.core.wc.SVNEvent;
import org.tmatesoft.svn.core.wc.SVNEventAction;

/**
 * Logging bridge between SVN and maven.
 */
public class EventHandler implements ISVNEventHandler
{

    private Log log;
    private long lastUpdateCompletedRevision;

    /**
     * Instantiates a new event handler.
     */
    public EventHandler()
    {
        super();
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

    private void logDebug( String message )
    {
        if ( log != null )
        {
            log.debug( message );
        }
    }

    private void logInfo( String message )
    {
        if ( log != null )
        {
            log.info( message );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkCancelled() throws SVNCancelException
    {
        // not used
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleEvent( SVNEvent event, double progress ) throws SVNException
    {
        logDebug( "SVN Event: " + event );

        if ( event.getAction() == SVNEventAction.UPDATE_COMPLETED )
        {
            logInfo( "At revision " + event.getRevision() );
            lastUpdateCompletedRevision = event.getRevision();
        }
    }

    /**
     * Gets the revision of the last completed update.
     * @return the last update completed revision.
     */
    public long getLastUpdateCompletedRevision()
    {
        return lastUpdateCompletedRevision;
    }
}
