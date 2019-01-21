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
 * Tests the functionality of the class [LevenshteinDissimilarity].
 *
 * @author Michael Rapp
 */
class LevenshteinDissimilarityTest {

    @Test
    fun testEvaluate() {
        val levenshteinDissimilarity = LevenshteinDissimilarity()
        assertEquals(1.0, levenshteinDissimilarity.evaluate("kitten", ""), 0.0)
        assertEquals(1.0, levenshteinDissimilarity.evaluate("", "sitting"), 0.0)
        assertEquals(0.0, levenshteinDissimilarity.evaluate("", ""), 0.0)
        assertEquals(0.0, levenshteinDissimilarity.evaluate("kitten", "kitten"), 0.0)
        assertEquals(0.25, levenshteinDissimilarity.evaluate("kitt", "kitf"), 0.0)
        assertEquals(0.25, levenshteinDissimilarity.evaluate("kitf", "kitt"), 0.0)
        assertEquals(0.5, levenshteinDissimilarity.evaluate("kitt", "kiff"), 0.0)
        assertEquals(0.5, levenshteinDissimilarity.evaluate("kiff", "kitt"), 0.0)
        assertEquals(0.75, levenshteinDissimilarity.evaluate("kitt", "kfff"), 0.0)
        assertEquals(0.75, levenshteinDissimilarity.evaluate("kfff", "kitt"), 0.0)
        assertEquals(1.0, levenshteinDissimilarity.evaluate("kitt", "ffff"), 0.0)
        assertEquals(1.0, levenshteinDissimilarity.evaluate("ffff", "kitt"), 0.0)
    }

    @Test
    fun testMinValue() {
        assertEquals(0.0, LevenshteinDissimilarity().minValue, 0.0)
    }

    @Test
    fun testMaxValue() {
        assertEquals(1.0, LevenshteinDissimilarity().maxValue, 0.0)
    }

    @Test
    fun testIsGainMetric() {
        assertFalse(LevenshteinDissimilarity().isGainMetric)
    }

    @Test
    fun testIsLossMetric() {
        assertTrue(LevenshteinDissimilarity().isLossMetric)
    }

}