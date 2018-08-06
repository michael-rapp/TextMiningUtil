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
package de.mrapp.textmining.util.metrics

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import kotlin.test.Test

/**
 * Tests the functionality of the class [HammingDistance].
 *
 * @author Michael Rapp
 */
class HammingDistanceTest {

    @Test
    fun testEvaluate() {
        val hammingDistance = HammingDistance()
        assertEquals(0.0, hammingDistance.evaluate("", ""), 0.0)
        assertEquals(0.0, hammingDistance.evaluate("foo", "foo"), 0.0)
        assertEquals(1.0, hammingDistance.evaluate("foo", "for"), 0.0)
        assertEquals(3.0, hammingDistance.evaluate("foo", "bar"), 0.0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testEvaluateThrowsExceptionIfTextsHaveDifferentLengths() {
        HammingDistance().evaluate("foo", "foo2")
    }

    @Test
    fun testMinValue() {
        assertEquals(0.0, HammingDistance().minValue(), 0.0)
    }

    @Test
    fun testMaxValue() {
        assertEquals(java.lang.Double.MAX_VALUE, HammingDistance().maxValue(), 0.0)
    }

    @Test
    fun testIsGainMetric() {
        assertFalse(HammingDistance().isGainMetric())
    }

}