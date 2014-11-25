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

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link LanguageModel}
 */
public class LanguageModelTest
{

    /**
     * Test percentage calculations.
     */
    @Test
    public void testPercentageCalculations()
    {
        LanguageModel model = new LanguageModel();
        model.setCountOfDefaultMessages( 7 );
        model.setMissingMessages( createMessages( 1 ) );
        model.setNotTranslatedMessages( createMessages( 2 ) );
        model.setAdditionalMessages( createMessages( 3 ) );

        Assert.assertEquals( "14.29 %", model.getMissingMessagesPercentage() );
        Assert.assertEquals( "28.57 %", model.getNotTranslatedMessagesPercentage() );
        Assert.assertEquals( "42.86 %", model.getAdditionalMessagesPercentage() );
    }

    private Map<String, String> createMessages( int count )
    {
        Map<String, String> messages = new HashMap<String, String>();
        for ( int i = 0; i < count; i++ )
        {
            messages.put( "foo" + i, "bar" );
        }
        return messages;
    }
}
