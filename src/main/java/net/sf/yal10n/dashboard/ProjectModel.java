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
     * Sets the all bundles.
     *
     * @param allBundles the new all bundles
     */
    public void setAllBundles( List<BundleModel> allBundles )
    {
        this.allBundles = allBundles;
    }

}
