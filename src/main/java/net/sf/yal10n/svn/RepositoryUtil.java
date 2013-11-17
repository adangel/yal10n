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

import org.apache.commons.lang.StringUtils;

import net.sf.yal10n.settings.DashboardConfiguration;
import net.sf.yal10n.settings.Repository;

/**
 * Utility to deal with repository settings.
 */
public class RepositoryUtil
{

    /**
     * Gets the complete URL, either using the mirror if specified or not.
     *
     * @param config the config
     * @param repo the repo
     * @return the svn url
     */
    public static String getSvnUrl( DashboardConfiguration config, Repository repo )
    {
        String svnCheckoutUrl = getCheckoutUrl( config, repo );
        String mirrorUrl = SVNUtil.toCompleteUrl( config.getMirrorPrefix(), repo.getMirrorUrl() );
        String svnUrl = StringUtils.isEmpty( mirrorUrl ) ? svnCheckoutUrl : mirrorUrl;
        return svnUrl;
    }

    /**
     * Gets the complete URL without the mirror.
     *
     * @param config the config
     * @param repo the repo
     * @return the checkout url
     */
    public static String getCheckoutUrl( DashboardConfiguration config, Repository repo )
    {
        String svnCheckoutUrl = SVNUtil.toCompleteUrl( config.getRepoPrefix(), repo.getUrl() );
        return svnCheckoutUrl;
    }
}
