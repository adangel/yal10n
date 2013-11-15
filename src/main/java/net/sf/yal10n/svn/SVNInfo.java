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

/**
 * Contains info about one revision.
 */
public class SVNInfo
{
    /** The revision. */
    private String revision;

    /** The committed date. */
    private String committedDate;

    /**
     * Instantiates a new sVN info.
     *
     * @param revision the revision
     * @param committedDate the committed date
     */
    public SVNInfo( String revision, String committedDate )
    {
        this.revision = revision;
        this.committedDate = committedDate;
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
     * Gets the committed date.
     *
     * @return the committed date
     */
    public String getCommittedDate()
    {
        return committedDate;
    }
}
