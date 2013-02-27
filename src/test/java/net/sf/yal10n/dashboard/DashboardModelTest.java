package net.sf.yal10n.dashboard;

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
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;
import net.sf.yal10n.DashboardMojo;
import net.sf.yal10n.analyzer.ResourceBundle;
import net.sf.yal10n.analyzer.ResourceFile;
import net.sf.yal10n.settings.DashboardConfiguration;
import net.sf.yal10n.svn.SVNUtilMock;

import org.junit.Test;

/**
 * Unit test for {@link DashboardModel}.
 */
public class DashboardModelTest
{

    /**
     * Test create model.
     * @throws Exception any error
     */
    @Test
    public void testCreateModel() throws Exception
    {
        File propertiesFile = new File( "./target/test-classes/unit/subdirectory/messages_de.properties" );
        List<String> languages = Arrays.asList( "fr", "de_DE", "de" );
        List<ResourceBundle> bundles = new ArrayList<ResourceBundle>();
        DashboardConfiguration config = new DashboardConfiguration();
        ResourceBundle bundle = new ResourceBundle( config, null, null, ".", "." );
        bundles.add( bundle );
        ResourceFile file = new ResourceFile( config, new SVNUtilMock( propertiesFile.getCanonicalPath() ),
                propertiesFile.getCanonicalPath(), null );
        bundle.addFile( file );

        DashboardModel model = DashboardModel.create( languages, bundles, true );
        Assert.assertEquals( DashboardMojo.getVersion(), model.getVersion() );
        Assert.assertEquals( "[de, de_DE, fr]", model.getAllLanguages().toString() );
        Assert.assertEquals( 1, model.getAllBundles().size() );
        Assert.assertTrue( model.isCreateTmx() );
        BundleModel bundleModel = model.getAllBundles().get( 0 );
        Assert.assertNotNull( bundleModel.getLanguages().get( 0 ) );
    }
}
