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

import junit.framework.Assert;

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
}
