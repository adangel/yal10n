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

/**
 * Summarizes the overall status of a language translation into 3 possible options: OK, Major problem,
 * or minor problems.
 */
public enum StatusClass
{
    /** Everything is ok. E.g. no missing translations, etc. */
    OK( "success", "no-issues", "ok" ),

    /** Major issues, e.g. wrong encoding, many missing translations, etc. */
    MAJOR_ISSUES( "warning", "severity-major", "major issues" ),

    /** Only minor issues, e.g. additional messages, etc. */
    MINOR_ISSUES( "info", "severity-minor", "minor issues" ),

    /** No status possible, e.g. the file doesn't exist. */
    NO_STATUS( "", "highlight", "" );

    private final String foundationClass;
    private final String dashboardClass;
    private final String message;

    private StatusClass( String foundationClass, String dashboardClass, String message )
    {
        this.foundationClass = foundationClass;
        this.dashboardClass = dashboardClass;
        this.message = message;
    }

    /**
     * Returns the style (css class) for foundation.
     * @return the css class to be used with foundation.
     */
    public String getFoundationClass()
    {
        return foundationClass;
    }

    /**
     * Gets the style for the dashboard table.
     * @return the css class to be used in the dashboard table.
     */
    public String getDashboardClass()
    {
        return dashboardClass;
    }

    /**
     * Gets the textual representation of the status.
     * @return the message
     */
    public String getMessage()
    {
        return message;
    }
}
