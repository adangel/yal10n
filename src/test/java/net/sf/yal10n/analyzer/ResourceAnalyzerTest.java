package net.sf.yal10n.analyzer;

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
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;
import net.sf.yal10n.settings.DashboardConfiguration;
import net.sf.yal10n.settings.Repository;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link ResourceAnalyzer}.
 */
public class ResourceAnalyzerTest
{
    /** location of the test files. */
    private static final String TARGET_TEST_CLASSES_UNIT = "./target/test-classes/unit/";
    private ResourceAnalyzer analyzer;
    private String dstPath;

    /**
     * Prepare test data.
     * @throws Exception any error
     */
    @Before
    public void setup() throws Exception
    {
        analyzer = new ResourceAnalyzer( null );
        dstPath = new File( TARGET_TEST_CLASSES_UNIT ).getCanonicalPath();
    }
    
    /**
     * Tests the analyze - whether the resource bundles are found.
     * @throws Exception any error
     */
    @Test
    public void testAnalyze() throws Exception
    {
        String svnUrl = null;
        DashboardConfiguration config = new DashboardConfiguration();
        config.setIncludes( Arrays.asList( "**/*.properties" ) );
        config.setExcludes( Arrays.asList( "**/someother.properties" ) );
        Repository repo = new Repository();
        String repoId = "repoId";
        analyzer.analyze( new NullLog(), svnUrl, dstPath, config , repo, repoId  );

        List<ResourceBundle> bundles = analyzer.getBundles();
        Assert.assertEquals( 3, bundles.size() );

        ResourceBundle bundle = bundles.get( 0 );
        Assert.assertNotNull( bundle );
        Assert.assertEquals( repoId, bundle.getRepoId() );
        Assert.assertEquals( "[de, de_DE, ja]", bundle.getLanguages().toString() );

        bundle = bundles.get( 1 );
        Assert.assertNotNull( bundle.getLocaleBasePath() );

        bundle = bundles.get( 2 );
        Assert.assertNotNull( bundle.getLocaleBasePath() );
    }

    /**
     * Tests the whether include patterns are correctly used.
     * @throws Exception any error
     */
    @Test
    public void testAnalyzeNoIncludes() throws Exception
    {
        String svnUrl = null;
        DashboardConfiguration config = new DashboardConfiguration();
        config.setIncludes( null );
        Repository repo = new Repository();
        String repoId = "repoId";
        analyzer.analyze( new NullLog(), svnUrl, dstPath, config , repo, repoId  );

        List<ResourceBundle> bundles = analyzer.getBundles();
        Assert.assertEquals( 0, bundles.size() );
    }

    /**
     * Tests the whether include patterns from repo are correctly used.
     * @throws Exception any error
     */
    @Test
    public void testAnalyzeRepoIncludes() throws Exception
    {
        String svnUrl = null;
        DashboardConfiguration config = new DashboardConfiguration();
        config.setIncludes( null );
        Repository repo = new Repository();
        repo.setIncludes( Arrays.asList( "**/*.properties" ) );
        repo.setExcludes( Arrays.asList( "**/someother.prop*" ) );
        String repoId = "repoId";
        analyzer.analyze( new NullLog(), svnUrl, TARGET_TEST_CLASSES_UNIT, config , repo, repoId  );

        List<ResourceBundle> bundles = analyzer.getBundles();
        Assert.assertEquals( 3, bundles.size() );
    }

    /**
     * Tests the whether bundles without file suffix "properties" work.
     * @throws Exception any error
     */
    @Test
    public void testAnalyzeWithoutSuffix() throws Exception
    {
        String svnUrl = null;
        DashboardConfiguration config = new DashboardConfiguration();
        config.setIncludes( Arrays.asList( "**/*" ) );
        config.setExcludes( Arrays.asList( "**/*.properties" ) );
        Repository repo = new Repository();
        String repoId = "repoId";
        analyzer.analyze( new NullLog(), svnUrl, TARGET_TEST_CLASSES_UNIT, config , repo, repoId  );

        List<ResourceBundle> bundles = analyzer.getBundles();
        Assert.assertEquals( "Found too many bundles", 3, bundles.size() );

        ResourceBundle bundle = bundles.get( 0 );
        Assert.assertNotNull( bundle );
        Assert.assertEquals( "[de]", bundle.getLanguages().toString() );

        bundle = bundles.get( 1 );
        Assert.assertNotNull( bundle );

        bundle = bundles.get( 2 );
        Assert.assertNotNull( bundle );
    }

    /**
     * Test if the dst path to scan doesn't exist.
     * @throws Exception any error
     */
    @Test( expected = RuntimeException.class )
    public void testAnalyzeError() throws Exception
    {
        String svnUrl = null;
        String notExistingDstPath = new File( "." ).getCanonicalPath() + "/this does not exist/";
        DashboardConfiguration config = new DashboardConfiguration();
        config.setIncludes( Arrays.asList( "**/*.properties" ) );
        Repository repo = new Repository();
        String repoId = "repoId";
        analyzer.analyze( new NullLog(), svnUrl, notExistingDstPath, config , repo, repoId  );
    }
}
