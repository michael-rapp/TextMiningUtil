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
 * Tests the functionality of the class [DamerauLevenshteinDistance].
 *
 * @author Michael Rapp
 */
class DamerauLevenshteinDistanceTest {

    @Test
    fun testEvaluate() {
        val damerauLevenshteinDistance = DamerauLevenshteinDistance()
        assertEquals("CA".length.toDouble(), damerauLevenshteinDistance.evaluate("CA", ""), 0.0)
        assertEquals("ABC".length.toDouble(), damerauLevenshteinDistance.evaluate("", "ABC"), 0.0)
        assertEquals(0.0, damerauLevenshteinDistance.evaluate("", ""), 0.0)
        assertEquals(0.0, damerauLevenshteinDistance.evaluate("CA", "CA"), 0.0)
        assertEquals(0.0, damerauLevenshteinDistance.evaluate("ABC", "ABC"), 0.0)
        assertEquals(1.0, damerauLevenshteinDistance.evaluate("ABC", "ABD"), 0.0)
        assertEquals(1.0, damerauLevenshteinDistance.evaluate("ABD", "ABC"), 0.0)
        assertEquals(1.0, damerauLevenshteinDistance.evaluate("ABC", "ACB"), 0.0)
        assertEquals(1.0, damerauLevenshteinDistance.evaluate("ACB", "ABC"), 0.0)
        assertEquals(2.0, damerauLevenshteinDistance.evaluate("CA", "ABC"), 0.0)
        assertEquals(2.0, damerauLevenshteinDistance.evaluate("ABC", "CA"), 0.0)
    }

    @Test
    fun testMinValue() {
        assertEquals(0.0, DamerauLevenshteinDistance().minValue, 0.0)
    }

    @Test
    fun testMaxValue() {
        assertEquals(java.lang.Double.MAX_VALUE, DamerauLevenshteinDistance().maxValue, 0.0)
    }

    @Test
    fun testIsGainMetric() {
        assertFalse(DamerauLevenshteinDistance().isGainMetric)
    }

    @Test
    fun testIsLossMetric() {
        assertTrue(DamerauLevenshteinDistance().isLossMetric)
    }

}