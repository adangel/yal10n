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

import java.io.File;

import net.sf.yal10n.analyzer.NullLog;

import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.FileUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link SVNUtil}.
 */
public class SVNUtilTest
{

    /**
     * Check the repo id.
     */
    @Test
    public void testRepoId()
    {
        String id = SVNUtil.toRepoId( "prefix", "url" );
        Assert.assertEquals( "c1a3dcfee6262f557e84cabffdbe32f7", id );
    }

    /**
     * Check the complete url generation.
     */
    @Test
    public void testCompleteUrl()
    {
        Assert.assertEquals( "http://svn/repo/trunk", SVNUtil.toCompleteUrl( "http://svn", "repo/trunk" ) );
        Assert.assertEquals( "http://svn/repo/trunk", SVNUtil.toCompleteUrl( "http://svn/", "repo/trunk" ) );
        Assert.assertEquals( "http://svn/repo/trunk", SVNUtil.toCompleteUrl( "http://svn", "/repo/trunk" ) );
        Assert.assertEquals( "http://svn/repo/trunk", SVNUtil.toCompleteUrl( "http://svn/", "/repo/trunk" ) );
        Assert.assertEquals( "http://svn/repo/trunk", SVNUtil.toCompleteUrl( "http://svn", "/repo/trunk/" ) );
    }

    /**
     * Checks whether the change is correctly detected as modification
     * instead of adding.
     * @throws Exception any error
     */
    @Test
    public void testIssue24DetectChanges() throws Exception
    {
        SVNUtil svnUtil = new SVNUtil();
        Log log = new NullLog();

        String svnUrl = "file://" + new File( "./src/test/resources/svnrepos/issue24-detectchanges" ).getAbsolutePath();
        String destination = new File( "./target/svnrepos/issue24-detectchanges" ).getAbsolutePath();
        if ( new File( destination ).exists() )
        {
            FileUtils.deleteDirectory( destination );
        }

        String revision = svnUtil.checkout( log, svnUrl + "/trunk", destination );
        Assert.assertEquals( "3", revision );
        SVNLogChange result = svnUtil.log( log, svnUrl, destination, "messages.properties",
                "3", "3" );
        Assert.assertEquals( SVNLogChange.MODIFICATION, result );
    }

    /**
     * Test various diffs with and without property changes.
     * @throws Exception any error
     */
    @Test
    public void testChangedPropertiesOnly() throws Exception
    {
        SVNUtil svnUtil = new SVNUtil();
        Log log = new NullLog();

        String svnUrl = "file://" + new File( "./src/test/resources/svnrepos/detectchanges-props-only" )
            .getCanonicalPath() + "/trunk";
        String destination = new File( "./target/svnrepos/detectchanges-props-only" ).getCanonicalPath();
        if ( new File( destination ).exists() )
        {
            FileUtils.deleteDirectory( destination );
        }

        String revision = svnUtil.checkout( log, svnUrl, destination );
        Assert.assertEquals( "4", revision );
        SVNInfo info = svnUtil.checkFile( log, svnUrl, destination, "testfile.txt" );
        Assert.assertEquals( "4", info.getRevision() );
        Assert.assertEquals( "2013-08-04 18:47:40 +0200 (Sun, 04 Aug 2013)", info.getCommittedDate() );
//        Assert.assertEquals( "Sun Aug 04 18:47:40 CEST 2013", info.getCommittedDate() ); // svnkit

        // revision 2: only prop change
        SVNLogChange result = svnUtil.log( log, svnUrl, destination, "testfile.txt", "2", "2" );
        Assert.assertEquals( SVNLogChange.MODIFICATION, result );
        String diff = svnUtil.diff( log, destination, destination + "/testfile.txt", "1", "2" );
        Assert.assertTrue( diff.contains( "Property changes on: " ) );
        Assert.assertFalse( diff.contains( "Index: " ) );

        // revision 3: only file change (real diff)
        result = svnUtil.log( log, svnUrl, destination, "testfile.txt", "3", "3" );
        Assert.assertEquals( SVNLogChange.MODIFICATION, result );
        diff = svnUtil.diff( log, destination, destination + "/testfile.txt", "2", "3" );
        Assert.assertFalse( diff.contains( "Property changes on: " ) );
        Assert.assertTrue( diff.contains( "Index: " ) );

        // revision 4: combined change of file and property
        result = svnUtil.log( log, svnUrl, destination, "testfile.txt", "4", "4" );
        Assert.assertEquals( SVNLogChange.MODIFICATION, result );
        diff = svnUtil.diff( log, destination, destination + "/testfile.txt", "3", "4" );
        Assert.assertTrue( diff.contains( "Property changes on: " ) );
        Assert.assertTrue( diff.contains( "Index: " ) );
    }
    
//    @Test
//    public void testCheckout()
//    {
//        SVNUtil svnUtil = new SVNUtil();
//        Log log = new NullLog();
//
//        String svnUrl = "file://"
//    + new File( "./src/test/resources/svnrepos/issue24-detectchanges" ).getAbsolutePath();
//        String destination = new File( "./target/svnrepos/issue24-detectchanges2" ).getAbsolutePath();
//
//        long checkout = svnUtil.checkout( log, svnUrl + "/trunk", destination );
//        System.out.println(" result: " + checkout);
////        SVNLogChange result = svnUtil.log( log, destination + "/messages.properties",
////                3, 3 );
////        Assert.assertEquals( SVNLogChange.MODIFICATION, result );
//    }

}
