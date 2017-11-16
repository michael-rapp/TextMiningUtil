/*
 * Copyright 2017 Michael Rapp
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
package de.mrapp.textmining.util.metrics;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Tests the functionality of the class {@link LevenshteinDistance}.
 *
 * @author Michael Rapp
 */
public class LevenshteinDistanceTest {

    @Test
    public final void testEvaluate() {
        LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
        assertEquals("kitten".length(), levenshteinDistance.evaluate("kitten", ""), 0);
        assertEquals("sitting".length(), levenshteinDistance.evaluate("", "sitting"), 0);
        assertEquals(0, levenshteinDistance.evaluate("", ""), 0);
        assertEquals(0, levenshteinDistance.evaluate("kitten", "kitten"), 0);
        assertEquals(3, levenshteinDistance.evaluate("kitten", "sitting"), 0);
        assertEquals(3, levenshteinDistance.evaluate("sitting", "kitten"), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testEvaluateThrowsExceptionIfFistTextIsNull() {
        new LevenshteinDistance().evaluate(null, "foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testEvaluateThrowsExceptionIfSecondTextIsNull() {
        new LevenshteinDistance().evaluate("foo", null);
    }

    @Test
    public final void testMinValue() {
        assertEquals(0, new LevenshteinDistance().minValue(), 0);
    }

    @Test
    public final void testMaxValue() {
        assertEquals(Double.MAX_VALUE, new LevenshteinDistance().maxValue(), 0);
    }

    @Test
    public final void testIsGainMetric() {
        assertFalse(new LevenshteinDistance().isGainMetric());
    }

}