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
 * Tests the functionality of the class {@link OptimalStringAlignmentDistance}.
 *
 * @author Michael Rapp
 */
public class OptimalStringAlignmentDistanceTest {

    @Test
    public final void testEvaluate() {
        OptimalStringAlignmentDistance osaDistance = new OptimalStringAlignmentDistance();
        assertEquals("CA".length(), osaDistance.evaluate("CA", ""), 0);
        assertEquals("ABC".length(), osaDistance.evaluate("", "ABC"), 0);
        assertEquals(0, osaDistance.evaluate("", ""), 0);
        assertEquals(0, osaDistance.evaluate("CA", "CA"), 0);
        assertEquals(0, osaDistance.evaluate("ABC", "ABC"), 0);
        assertEquals(1, osaDistance.evaluate("ABC", "ABD"), 0);
        assertEquals(1, osaDistance.evaluate("ABD", "ABC"), 0);
        assertEquals(1, osaDistance.evaluate("ABC", "ACB"), 0);
        assertEquals(1, osaDistance.evaluate("ACB", "ABC"), 0);
        assertEquals(3, osaDistance.evaluate("CA", "ABC"), 0);
        assertEquals(3, osaDistance.evaluate("ABC", "CA"), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testEvaluateThrowsExceptionIfFistTextIsNull() {
        new OptimalStringAlignmentDistance().evaluate(null, "foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testEvaluateThrowsExceptionIfSecondTextIsNull() {
        new OptimalStringAlignmentDistance().evaluate("foo", null);
    }

    @Test
    public final void testMinValue() {
        assertEquals(0, new OptimalStringAlignmentDistance().minValue(), 0);
    }

    @Test
    public final void testMaxValue() {
        assertEquals(Double.MAX_VALUE, new OptimalStringAlignmentDistance().maxValue(), 0);
    }

    @Test
    public final void testIsGainMetric() {
        assertFalse(new OptimalStringAlignmentDistance().isGainMetric());
    }

}