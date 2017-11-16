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
 * Tests the functionality of the class {@link HammingLoss}.
 *
 * @author Michael Rapp
 */
public class HammingLossTest {

    @Test
    public final void testEvaluate() {
        HammingLoss hammingLoss = new HammingLoss();
        assertEquals(0, hammingLoss.evaluate("", ""), 0);
        assertEquals(0, hammingLoss.evaluate("text", "text"), 0);
        assertEquals(0.25, hammingLoss.evaluate("text", "texf"), 0);
        assertEquals(0.5, hammingLoss.evaluate("text", "teff"), 0);
        assertEquals(0.75, hammingLoss.evaluate("text", "tfff"), 0);
        assertEquals(1, hammingLoss.evaluate("text", "ffff"), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testEvaluateThrowsExceptionIfFistTextIsNull() {
        new HammingLoss().evaluate(null, "foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testEvaluateThrowsExceptionIfSecondTextIsNull() {
        new HammingLoss().evaluate("foo", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testEvaluateThrowsExceptionIfTextsHaveDifferentLengths() {
        new HammingLoss().evaluate("foo", "foo2");
    }

    @Test
    public final void testMinValue() {
        assertEquals(0, new HammingLoss().minValue(), 0);
    }

    @Test
    public final void testMaxValue() {
        assertEquals(1, new HammingLoss().maxValue(), 0);
    }

    @Test
    public final void testIsGainMetric() {
        assertFalse(new HammingLoss().isGainMetric());
    }

}