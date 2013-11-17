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

import java.util.Comparator;
import java.util.List;

/**
 * Collects all bundles of one single project.
 */
public class ProjectModel
{
    private List<BundleModel> allBundles;

    /**
     * Instantiates a new project model.
     *
     * @param allBundles all the bundles
     */
    public ProjectModel( List<BundleModel> allBundles )
    {
        this.allBundles = allBundles;
    }

    /**
     * Gets the all bundles.
     *
     * @return the all bundles
     */
    public List<BundleModel> getAllBundles()
    {
        return allBundles;
    }

    /**
     * Comparator for sorting by project name.
     */
    public static final Comparator<ProjectModel> BY_NAME = new Comparator<ProjectModel>()
    {
        /**
         * Sorting by the project name of the first bundle.
         *
         * @param o1 the one project
         * @param 02 the other project
         * @return a negative integer, zero, or a positive integer
         * as this object is less than, equal to, or greater than the specified object.
         */
        @Override
        public int compare( ProjectModel o1, ProjectModel o2 )
        {
            int result = 0;
            if ( !o1.getAllBundles().isEmpty() && !o2.getAllBundles().isEmpty() )
            {
                BundleModel bundle1 = o1.getAllBundles().get( 0 );
                BundleModel bundle2 = o2.getAllBundles().get( 0 );
                result = bundle1.compareTo( bundle2 );
            }
            else if ( o1.getAllBundles().isEmpty() && !o2.getAllBundles().isEmpty() )
            {
                result = 1;
            }
            else if ( !o1.getAllBundles().isEmpty() && o2.getAllBundles().isEmpty() )
            {
                result = -1;
            }
            return result;
        }
    };
}
