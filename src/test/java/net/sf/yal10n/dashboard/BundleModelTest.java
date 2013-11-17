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


import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link BundleModel}.
 */
public class BundleModelTest
{
    /**
     * Test comparison based on project name.
     */
    @Test
    public void testCompareEqualsHashCode()
    {
        BundleModel model1 = new BundleModel();
        model1.setProjectName( "a" );
        BundleModel model2 = new BundleModel();
        model2.setProjectName( "b" );

        Assert.assertTrue( model1.compareTo( model2 ) < 0 );
        
        BundleModel model3 = new BundleModel();
        model3.setProjectName( "a" );

        Assert.assertEquals( model1.hashCode(), model3.hashCode() );
        Assert.assertEquals( model1, model3 );
        Assert.assertTrue( model1.hashCode() != model2.hashCode() );
        Assert.assertTrue( model1.hashCode() != new BundleModel().hashCode() );
        Assert.assertFalse( model1.equals( model2 ) );
        Assert.assertFalse( model1.equals( null ) );
        Assert.assertFalse( model1.equals( new BundleModel() ) );
        Assert.assertFalse( new BundleModel().equals( model1 ) );
        Assert.assertFalse( model1.equals( "a" ) );
    }
}
