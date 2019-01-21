/*
 * Copyright 2017 - 2019 Michael Rapp
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
package de.mrapp.textmining.util.metrics

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * Tests the functionality of the class [HammingLoss].
 *
 * @author Michael Rapp
 */
class HammingLossTest {

    @Test
    fun testEvaluate() {
        val hammingLoss = HammingLoss()
        assertEquals(0.0, hammingLoss.evaluate("", ""), 0.0)
        assertEquals(0.0, hammingLoss.evaluate("text", "text"), 0.0)
        assertEquals(0.25, hammingLoss.evaluate("text", "texf"), 0.0)
        assertEquals(0.5, hammingLoss.evaluate("text", "teff"), 0.0)
        assertEquals(0.75, hammingLoss.evaluate("text", "tfff"), 0.0)
        assertEquals(1.0, hammingLoss.evaluate("text", "ffff"), 0.0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testEvaluateThrowsExceptionIfTextsHaveDifferentLengths() {
        HammingLoss().evaluate("foo", "foo2")
    }

    @Test
    fun testMinValue() {
        assertEquals(0.0, HammingLoss().minValue, 0.0)
    }

    @Test
    fun testMaxValue() {
        assertEquals(1.0, HammingLoss().maxValue, 0.0)
    }

    @Test
    fun testIsGainMetric() {
        assertFalse(HammingLoss().isGainMetric)
    }

    @Test
    fun testIsLossMetric() {
        assertTrue(HammingLoss().isLossMetric)
    }

}