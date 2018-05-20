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
 * Tests the functionality of the class {@link HammingDistance}.
 *
 * @author Michael Rapp
 */
public class HammingDistanceTest {

    @Test
    public final void testEvaluate() {
        HammingDistance hammingDistance = new HammingDistance();
        assertEquals(0, hammingDistance.evaluate("", ""), 0);
        assertEquals(0, hammingDistance.evaluate("foo", "foo"), 0);
        assertEquals(1, hammingDistance.evaluate("foo", "for"), 0);
        assertEquals(3, hammingDistance.evaluate("foo", "bar"), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testEvaluateThrowsExceptionIfFirstTextIsNull() {
        new HammingDistance().evaluate(null, "foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testEvaluateThrowsExceptionIfSecondTextIsNull() {
        new HammingDistance().evaluate("foo", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testEvaluateThrowsExceptionIfTextsHaveDifferentLengths() {
        new HammingDistance().evaluate("foo", "foo2");
    }

    @Test
    public final void testMinValue() {
        assertEquals(0, new HammingDistance().minValue(), 0);
    }

    @Test
    public final void testMaxValue() {
        assertEquals(Double.MAX_VALUE, new HammingDistance().maxValue(), 0);
    }

    @Test
    public final void testIsGainMetric() {
        assertFalse(new HammingDistance().isGainMetric());
    }

}