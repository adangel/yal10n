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

import java.util.Date;

import org.apache.maven.plugin.logging.Log;
import org.junit.Assert;

/**
 * Mock for the {@link SVNUtil}
 */
public class SVNUtilMock extends SVNUtil
{
    /** Timestamp used for svn info. */
    public static final long TIMESTAMP = 1361649083272L;

    /** The timestamp as a Date string. */
    public static final String TIMESTAMP_STRING = new Date( TIMESTAMP ).toString();
    
    private String expectedFile = null;

    /**
     * Instantiates a new SVN util mock.
     *
     * @param expectedFile the expected file
     */
    public SVNUtilMock( String expectedFile )
    {
        this.expectedFile = expectedFile;
    }

    @Override
    public SVNInfo checkFile( Log log, String fullFilePath )
    {
        if ( expectedFile != null )
        {
            Assert.assertEquals( expectedFile, fullFilePath );
        }
        SVNInfo info = new SVNInfo( "1", TIMESTAMP_STRING );
        return info;
    }
}

