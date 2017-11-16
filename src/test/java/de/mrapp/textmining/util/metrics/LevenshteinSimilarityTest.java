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
import static org.junit.Assert.assertTrue;

/**
 * Tests the functionality of the class {@link LevenshteinSimilarity}.
 *
 * @author Michael Rapp
 */
public class LevenshteinSimilarityTest {

    @Test
    public final void testEvaluate() {
        LevenshteinSimilarity levenshteinSimilarity = new LevenshteinSimilarity();
        assertEquals(0, levenshteinSimilarity.evaluate("kitten", ""), 0);
        assertEquals(0, levenshteinSimilarity.evaluate("", "sitting"), 0);
        assertEquals(1, levenshteinSimilarity.evaluate("", ""), 0);
        assertEquals(1, levenshteinSimilarity.evaluate("kitten", "kitten"), 0);
        assertEquals(0.75, levenshteinSimilarity.evaluate("kitt", "kitf"), 0);
        assertEquals(0.75, levenshteinSimilarity.evaluate("kitf", "kitt"), 0);
        assertEquals(0.5, levenshteinSimilarity.evaluate("kitt", "kiff"), 0);
        assertEquals(0.5, levenshteinSimilarity.evaluate("kiff", "kitt"), 0);
        assertEquals(0.25, levenshteinSimilarity.evaluate("kitt", "kfff"), 0);
        assertEquals(0.25, levenshteinSimilarity.evaluate("kfff", "kitt"), 0);
        assertEquals(0, levenshteinSimilarity.evaluate("kitt", "ffff"), 0);
        assertEquals(0, levenshteinSimilarity.evaluate("ffff", "kitt"), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testEvaluateThrowsExceptionIfFistTextIsNull() {
        new LevenshteinSimilarity().evaluate(null, "foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testEvaluateThrowsExceptionIfSecondTextIsNull() {
        new LevenshteinSimilarity().evaluate("foo", null);
    }

    @Test
    public final void testMinValue() {
        assertEquals(0, new LevenshteinSimilarity().minValue(), 0);
    }

    @Test
    public final void testMaxValue() {
        assertEquals(1, new LevenshteinSimilarity().maxValue(), 0);
    }

    @Test
    public final void testIsGainMetric() {
        assertTrue(new LevenshteinSimilarity().isGainMetric());
    }

}