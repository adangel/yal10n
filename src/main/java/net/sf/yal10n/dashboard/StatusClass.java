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
    OK( "success" ),

    /** Major issues, e.g. wrong encoding, many missing translations, etc. */
    MAJOR_ISSUES( "warning" ),

    /** Only minor issues, e.g. additional messages, etc. */
    MINOR_ISSUES( "info" );

    private final String foundationClass;

    private StatusClass( String foundationClass )
    {
        this.foundationClass = foundationClass;
    }

    /**
     * Returns the style (css class) for foundation.
     * @return the css class to be used with foundation.
     */
    public String getFoundationClass()
    {
        return foundationClass;
    }
}
