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
 * Tests the functionality of the class {@link LevenshteinDissimilarity}.
 *
 * @author Michael Rapp
 */
public class LevenshteinDissimilarityTest {

    @Test
    public final void testEvaluate() {
        LevenshteinDissimilarity levenshteinDissimilarity = new LevenshteinDissimilarity();
        assertEquals(1, levenshteinDissimilarity.evaluate("kitten", ""), 0);
        assertEquals(1, levenshteinDissimilarity.evaluate("", "sitting"), 0);
        assertEquals(0, levenshteinDissimilarity.evaluate("", ""), 0);
        assertEquals(0, levenshteinDissimilarity.evaluate("kitten", "kitten"), 0);
        assertEquals(0.25, levenshteinDissimilarity.evaluate("kitt", "kitf"), 0);
        assertEquals(0.25, levenshteinDissimilarity.evaluate("kitf", "kitt"), 0);
        assertEquals(0.5, levenshteinDissimilarity.evaluate("kitt", "kiff"), 0);
        assertEquals(0.5, levenshteinDissimilarity.evaluate("kiff", "kitt"), 0);
        assertEquals(0.75, levenshteinDissimilarity.evaluate("kitt", "kfff"), 0);
        assertEquals(0.75, levenshteinDissimilarity.evaluate("kfff", "kitt"), 0);
        assertEquals(1, levenshteinDissimilarity.evaluate("kitt", "ffff"), 0);
        assertEquals(1, levenshteinDissimilarity.evaluate("ffff", "kitt"), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testEvaluateThrowsExceptionIfFirstTextIsNull() {
        new LevenshteinDissimilarity().evaluate(null, "foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testEvaluateThrowsExceptionIfSecondTextIsNull() {
        new LevenshteinDissimilarity().evaluate("foo", null);
    }

    @Test
    public final void testMinValue() {
        assertEquals(0, new LevenshteinDissimilarity().minValue(), 0);
    }

    @Test
    public final void testMaxValue() {
        assertEquals(1, new LevenshteinDissimilarity().maxValue(), 0);
    }

    @Test
    public final void testIsGainMetric() {
        assertFalse(new LevenshteinDissimilarity().isGainMetric());
    }

}