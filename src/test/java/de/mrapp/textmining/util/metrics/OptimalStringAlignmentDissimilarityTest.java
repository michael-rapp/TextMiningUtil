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
 * Tests the functionality of the class {@link OptimalStringAlignmentDissimilarity}.
 *
 * @author Michael Rapp
 */
public class OptimalStringAlignmentDissimilarityTest {

    @Test
    public final void testEvaluate() {
        OptimalStringAlignmentDissimilarity osaDissimilarity = new OptimalStringAlignmentDissimilarity();
        assertEquals(1, osaDissimilarity.evaluate("CA", ""), 0);
        assertEquals(1, osaDissimilarity.evaluate("", "ABC"), 0);
        assertEquals(0, osaDissimilarity.evaluate("", ""), 0);
        assertEquals(0, osaDissimilarity.evaluate("CA", "CA"), 0);
        assertEquals(0, osaDissimilarity.evaluate("ABC", "ABC"), 0);
        assertEquals(0.33, osaDissimilarity.evaluate("ABC", "ABD"), 0.01);
        assertEquals(0.33, osaDissimilarity.evaluate("ABD", "ABC"), 0.01);
        assertEquals(0.33, osaDissimilarity.evaluate("ABC", "ACB"), 0.01);
        assertEquals(0.33, osaDissimilarity.evaluate("ACB", "ABC"), 0.01);
        assertEquals(1, osaDissimilarity.evaluate("CA", "ABC"), 0);
        assertEquals(1, osaDissimilarity.evaluate("ABC", "CA"), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testEvaluateThrowsExceptionIfFirstTextIsNull() {
        new OptimalStringAlignmentDissimilarity().evaluate(null, "foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testEvaluateThrowsExceptionIfSecondTextIsNull() {
        new OptimalStringAlignmentDissimilarity().evaluate("foo", null);
    }

    @Test
    public final void testMinValue() {
        assertEquals(0, new OptimalStringAlignmentDissimilarity().minValue(), 0);
    }

    @Test
    public final void testMaxValue() {
        assertEquals(1, new OptimalStringAlignmentDissimilarity().maxValue(), 0);
    }

    @Test
    public final void testIsGainMetric() {
        assertFalse(new OptimalStringAlignmentDissimilarity().isGainMetric());
    }

}