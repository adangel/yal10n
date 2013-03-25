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
import java.util.UUID;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Unit test for {@link DetectChangesStatus}.
 */
public class DetectChangesStatusTest
{

    /**
     * Writes and reads the status.
     * @throws Exception any error
     */
    @Test
    public void testWriteAndRead() throws Exception
    {
        File outputDirectory = new File( "./target/test-output/status/" );
        if ( !outputDirectory.exists() )
        {
            Assert.assertTrue( "Couldn't create directories for test: " + outputDirectory, outputDirectory.mkdirs() );
        }
        Assert.assertTrue( "OutputDirectory is not a directory: " + outputDirectory, outputDirectory.isDirectory() );

        String file = new File( outputDirectory, "status-" + UUID.randomUUID().toString() + ".json" )
            .getCanonicalPath();

        RepoStatus repo = new RepoStatus();
        repo.setCompleteRepoUrl( "completeUrl" );
        repo.setId( "myId" );
        final long revision = 4711L;
        repo.setRevision( revision );

        DetectChangesStatus status = new DetectChangesStatus();
        status.setLastDetection( "now" );
        status.getRepos().add( repo );

        status.writeToFile( file );
        Assert.assertTrue( new File( file ).exists() );

        DetectChangesStatus status2 = DetectChangesStatus.readFromFile( file );
        Assert.assertEquals( "now", status2.getLastDetection() );
        Assert.assertEquals( 1, status2.getRepos().size() );
        RepoStatus repo2 = status2.getRepoStatusById( "myId" );
        Assert.assertEquals( "completeUrl", repo2.getCompleteRepoUrl() );
        Assert.assertEquals( "myId", repo2.getId() );
        Assert.assertEquals( revision, repo2.getRevision() );
    }

    /**
     * Test reading of the status json file.
     *
     * @throws Exception the exception
     */
    @Test
    public void testRead() throws Exception
    {
        File f = new File( "target/test-classes/status/yal10n-status-sample.json" );
        DetectChangesStatus status = DetectChangesStatus.readFromFile( f.getCanonicalPath() );
        Assert.assertNotNull( status.getLastDetection() );
        Assert.assertEquals( 3, status.getRepos().size() );
    }
}
