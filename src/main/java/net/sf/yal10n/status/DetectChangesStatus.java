package net.sf.yal10n.status;

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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

/**
 * This class contains the status of the last run of the detect-changes mojo.
 */
public class DetectChangesStatus
{
    private String lastDetection = null;
    private List<RepoStatus> repos = new ArrayList<RepoStatus>();

    /**
     * Gets the repos.
     *
     * @return the repos
     */
    public List<RepoStatus> getRepos()
    {
        return repos;
    }

    /**
     * Sets the repos.
     *
     * @param repos the new repos
     */
    public void setRepos( List<RepoStatus> repos )
    {
        this.repos = repos;
    }

    /**
     * Gets the last detection.
     *
     * @return the last detection
     */
    public String getLastDetection()
    {
        return lastDetection;
    }

    /**
     * Sets the last detection.
     *
     * @param lastDetection the new last detection
     */
    public void setLastDetection( String lastDetection )
    {
        this.lastDetection = lastDetection;
    }

    /**
     * Reads the status file.
     * @param file the file to read
     * @return the status
     */
    public static DetectChangesStatus readFromFile( String file )
    {
        try
        {
            DetectChangesStatus status;
            File f = new File( file ).getCanonicalFile();
            if ( f.exists() )
            {
                JsonMapper mapper = JsonMapper.builder()
                        .configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false )
                        .build();
                status = mapper.readerFor( DetectChangesStatus.class ).readValue( f );
            }
            else
            {
                status = new DetectChangesStatus();
            }
            return status;
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * Writes this status into the given file.
     * @param file the file
     */
    public void writeToFile( String file )
    {
        try
        {
            File f = new File( file ).getCanonicalFile();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue( f, this );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * Get a specific repository status by id.
     * @param id the repo id.
     * @return the repo status or <code>null</code>.
     */
    public RepoStatus getRepoStatusById( String id )
    {
        RepoStatus result = null;
        for ( RepoStatus r : repos )
        {
            if ( r.getId().equals( id ) )
            {
                result = r;
                break;
            }
        }
        return result;
    }
}
