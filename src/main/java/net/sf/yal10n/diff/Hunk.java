package net.sf.yal10n.diff;

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

import java.util.HashMap;
import java.util.Map;

/**
 * Helper class that represents one single hunk of a unified diff.
 */
public class Hunk
{
    Map<Integer, String> commonLines = new HashMap<Integer, String>();
    Map<Integer, String> origLines = new HashMap<Integer, String>();
    Map<Integer, String> newLines = new HashMap<Integer, String>();
    Map<Integer, Character> indicators = new HashMap<Integer, Character>();

    int firstLineNumber = 0;
    int lastLineNumber = 0;
}
