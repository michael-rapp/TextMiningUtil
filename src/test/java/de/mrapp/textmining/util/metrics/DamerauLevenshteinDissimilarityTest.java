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
package de.mrapp.textmining.util.metrics;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Tests the functionality of the class {@link DamerauLevenshteinDissimilarity}.
 *
 * @author Michael Rapp
 */
public class DamerauLevenshteinDissimilarityTest {

    @Test
    public final void testEvaluate() {
        DamerauLevenshteinDissimilarity damerauLevenshteinDissimilarity = new DamerauLevenshteinDissimilarity();
        assertEquals(1, damerauLevenshteinDissimilarity.evaluate("CA", ""), 0);
        assertEquals(1, damerauLevenshteinDissimilarity.evaluate("", "ABC"), 0);
        assertEquals(0, damerauLevenshteinDissimilarity.evaluate("", ""), 0);
        assertEquals(0, damerauLevenshteinDissimilarity.evaluate("CA", "CA"), 0);
        assertEquals(0, damerauLevenshteinDissimilarity.evaluate("ABC", "ABC"), 0);
        assertEquals(0.33, damerauLevenshteinDissimilarity.evaluate("ABC", "ABD"), 0.01);
        assertEquals(0.33, damerauLevenshteinDissimilarity.evaluate("ABD", "ABC"), 0.01);
        assertEquals(0.33, damerauLevenshteinDissimilarity.evaluate("ABC", "ACB"), 0.01);
        assertEquals(0.33, damerauLevenshteinDissimilarity.evaluate("ACB", "ABC"), 0.01);
        assertEquals(0.66, damerauLevenshteinDissimilarity.evaluate("CA", "ABC"), 0.01);
        assertEquals(0.66, damerauLevenshteinDissimilarity.evaluate("ABC", "CA"), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testEvaluateThrowsExceptionIfFirstTextIsNull() {
        new DamerauLevenshteinDissimilarity().evaluate(null, "foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testEvaluateThrowsExceptionIfSecondTextIsNull() {
        new DamerauLevenshteinDissimilarity().evaluate("foo", null);
    }

    @Test
    public final void testMinValue() {
        assertEquals(0, new DamerauLevenshteinDissimilarity().minValue(), 0);
    }

    @Test
    public final void testMaxValue() {
        assertEquals(1, new DamerauLevenshteinDissimilarity().maxValue(), 0);
    }

    @Test
    public final void testIsGainMetric() {
        assertFalse(new DamerauLevenshteinDissimilarity().isGainMetric());
    }

}