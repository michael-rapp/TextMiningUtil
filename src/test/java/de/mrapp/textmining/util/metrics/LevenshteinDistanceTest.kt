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
 * Tests the functionality of the class [LevenshteinDistance].
 *
 * @author Michael Rapp
 */
class LevenshteinDistanceTest {

    @Test
    fun testEvaluate() {
        val levenshteinDistance = LevenshteinDistance()
        assertEquals("kitten".length.toDouble(), levenshteinDistance.evaluate("kitten", ""), 0.0)
        assertEquals("sitting".length.toDouble(), levenshteinDistance.evaluate("", "sitting"), 0.0)
        assertEquals(0.0, levenshteinDistance.evaluate("", ""), 0.0)
        assertEquals(0.0, levenshteinDistance.evaluate("kitten", "kitten"), 0.0)
        assertEquals(3.0, levenshteinDistance.evaluate("kitten", "sitting"), 0.0)
        assertEquals(3.0, levenshteinDistance.evaluate("sitting", "kitten"), 0.0)
    }

    @Test
    fun testMinValue() {
        assertEquals(0.0, LevenshteinDistance().minValue(), 0.0)
    }

    @Test
    fun testMaxValue() {
        assertEquals(java.lang.Double.MAX_VALUE, LevenshteinDistance().maxValue(), 0.0)
    }

    @Test
    fun testIsGainMetric() {
        assertFalse(LevenshteinDistance().isGainMetric())
    }

}