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

/**
 * Contains information about a specific repository. Used by detect-changes mojo.
 */
public class RepoStatus
{
    private String id;
    private String completeRepoUrl;
    private String revision;

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId()
    {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId( String id )
    {
        this.id = id;
    }

    /**
     * Gets the revision.
     *
     * @return the revision
     */
    public String getRevision()
    {
        return revision;
    }

    /**
     * Sets the revision.
     *
     * @param revision the new revision
     */
    public void setRevision( String revision )
    {
        this.revision = revision;
    }

    /**
     * Gets the complete repo url.
     *
     * @return the complete repo url
     */
    public String getCompleteRepoUrl()
    {
        return completeRepoUrl;
    }

    /**
     * Sets the complete repo url.
     *
     * @param completeRepoUrl the new complete repo url
     */
    public void setCompleteRepoUrl( String completeRepoUrl )
    {
        this.completeRepoUrl = completeRepoUrl;
    }
}
