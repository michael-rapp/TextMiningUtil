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

/**
 * Tests the functionality of the class [OptimalStringAlignmentDissimilarity].
 *
 * @author Michael Rapp
 */
class OptimalStringAlignmentDissimilarityTest {

    @Test
    fun testEvaluate() {
        val osaDissimilarity = OptimalStringAlignmentDissimilarity()
        assertEquals(1.0, osaDissimilarity.evaluate("CA", ""), 0.0)
        assertEquals(1.0, osaDissimilarity.evaluate("", "ABC"), 0.0)
        assertEquals(0.0, osaDissimilarity.evaluate("", ""), 0.0)
        assertEquals(0.0, osaDissimilarity.evaluate("CA", "CA"), 0.0)
        assertEquals(0.0, osaDissimilarity.evaluate("ABC", "ABC"), 0.0)
        assertEquals(0.33, osaDissimilarity.evaluate("ABC", "ABD"), 0.01)
        assertEquals(0.33, osaDissimilarity.evaluate("ABD", "ABC"), 0.01)
        assertEquals(0.33, osaDissimilarity.evaluate("ABC", "ACB"), 0.01)
        assertEquals(0.33, osaDissimilarity.evaluate("ACB", "ABC"), 0.01)
        assertEquals(1.0, osaDissimilarity.evaluate("CA", "ABC"), 0.0)
        assertEquals(1.0, osaDissimilarity.evaluate("ABC", "CA"), 0.0)
    }

    @Test
    fun testMinValue() {
        assertEquals(0.0, OptimalStringAlignmentDissimilarity().minValue(), 0.0)
    }

    @Test
    fun testMaxValue() {
        assertEquals(1.0, OptimalStringAlignmentDissimilarity().maxValue(), 0.0)
    }

    @Test
    fun testIsGainMetric() {
        assertFalse(OptimalStringAlignmentDissimilarity().isGainMetric())
    }

}