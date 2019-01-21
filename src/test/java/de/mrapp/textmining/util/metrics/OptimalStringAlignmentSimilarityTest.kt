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
import org.junit.Assert.assertTrue
import kotlin.test.Test

/**
 * Tests the functionality of the class [OptimalStringAlignmentSimilarity].
 *
 * @author Michael Rapp
 */
class OptimalStringAlignmentSimilarityTest {

    @Test
    fun testEvaluate() {
        val osaSimilarity = OptimalStringAlignmentSimilarity()
        assertEquals(0.0, osaSimilarity.evaluate("CA", ""), 0.0)
        assertEquals(0.0, osaSimilarity.evaluate("", "ABC"), 0.0)
        assertEquals(1.0, osaSimilarity.evaluate("", ""), 0.0)
        assertEquals(1.0, osaSimilarity.evaluate("CA", "CA"), 0.0)
        assertEquals(1.0, osaSimilarity.evaluate("ABC", "ABC"), 0.0)
        assertEquals(0.66, osaSimilarity.evaluate("ABC", "ABD"), 0.01)
        assertEquals(0.66, osaSimilarity.evaluate("ABD", "ABC"), 0.01)
        assertEquals(0.66, osaSimilarity.evaluate("ABC", "ACB"), 0.01)
        assertEquals(0.66, osaSimilarity.evaluate("ACB", "ABC"), 0.01)
        assertEquals(0.0, osaSimilarity.evaluate("CA", "ABC"), 0.0)
        assertEquals(0.0, osaSimilarity.evaluate("ABC", "CA"), 0.0)
    }

    @Test
    fun testMinValue() {
        assertEquals(0.0, OptimalStringAlignmentSimilarity().minValue(), 0.0)
    }

    @Test
    fun testMaxValue() {
        assertEquals(1.0, OptimalStringAlignmentSimilarity().maxValue(), 0.0)
    }

    @Test
    fun testIsGainMetric() {
        assertTrue(OptimalStringAlignmentSimilarity().isGainMetric())
    }

}