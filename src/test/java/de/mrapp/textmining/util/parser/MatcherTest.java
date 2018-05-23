/*
 * Copyright 2017 - 2018 Michael Rapp
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package de.mrapp.textmining.util.parser;

import de.mrapp.textmining.util.metrics.HammingAccuracy;
import de.mrapp.textmining.util.metrics.HammingDistance;
import de.mrapp.textmining.util.metrics.TextMetric;
import de.mrapp.textmining.util.tokenizer.Substring;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the functionality of the class {@link Matcher}.
 *
 * @author Michael Rapp
 */
public class MatcherTest {

    @Test
    public final void testEquals() {
        Matcher<Substring> matcher = Matcher.equals();
        Substring token = new Substring("token", 1);
        assertTrue(matcher.matches(token, "token"));
        assertFalse(matcher.matches(token, "foo"));
    }

    @Test
    public final void testStartsWith() {
        Matcher<Substring> matcher = Matcher.startsWith();
        Substring token = new Substring("token", 1);
        assertTrue(matcher.matches(token, "to"));
        assertFalse(matcher.matches(token, "foo"));
    }

    @Test
    public final void testEndsWith() {
        Matcher<Substring> matcher = Matcher.endsWith();
        Substring token = new Substring("token", 1);
        assertTrue(matcher.matches(token, "en"));
        assertFalse(matcher.matches(token, "foo"));
    }

    @Test
    public final void testUsingLossMetric() {
        TextMetric metric = new HammingDistance();
        Matcher<Substring> matcher = Matcher.usingMetric(metric, 1);
        Substring token = new Substring("token", 1);
        assertTrue(matcher.matches(token, "doken"));
        assertFalse(matcher.matches(token, "ddken"));
    }

    @Test
    public final void testUsingGainMetric() {
        TextMetric metric = new HammingAccuracy();
        Matcher<Substring> matcher = Matcher.usingMetric(metric, 0.75);
        Substring token = new Substring("token", 1);
        assertTrue(matcher.matches(token, "doken"));
        assertFalse(matcher.matches(token, "ddken"));
    }

}
