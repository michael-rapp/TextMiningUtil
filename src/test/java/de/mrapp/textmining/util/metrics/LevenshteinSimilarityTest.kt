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
import org.junit.Assert.assertTrue
import kotlin.test.Test

/**
 * Tests the functionality of the class [LevenshteinSimilarity].
 *
 * @author Michael Rapp
 */
class LevenshteinSimilarityTest {

    @Test
    fun testEvaluate() {
        val levenshteinSimilarity = LevenshteinSimilarity()
        assertEquals(0.0, levenshteinSimilarity.evaluate("kitten", ""), 0.0)
        assertEquals(0.0, levenshteinSimilarity.evaluate("", "sitting"), 0.0)
        assertEquals(1.0, levenshteinSimilarity.evaluate("", ""), 0.0)
        assertEquals(1.0, levenshteinSimilarity.evaluate("kitten", "kitten"), 0.0)
        assertEquals(0.75, levenshteinSimilarity.evaluate("kitt", "kitf"), 0.0)
        assertEquals(0.75, levenshteinSimilarity.evaluate("kitf", "kitt"), 0.0)
        assertEquals(0.5, levenshteinSimilarity.evaluate("kitt", "kiff"), 0.0)
        assertEquals(0.5, levenshteinSimilarity.evaluate("kiff", "kitt"), 0.0)
        assertEquals(0.25, levenshteinSimilarity.evaluate("kitt", "kfff"), 0.0)
        assertEquals(0.25, levenshteinSimilarity.evaluate("kfff", "kitt"), 0.0)
        assertEquals(0.0, levenshteinSimilarity.evaluate("kitt", "ffff"), 0.0)
        assertEquals(0.0, levenshteinSimilarity.evaluate("ffff", "kitt"), 0.0)
    }

    @Test
    fun testMinValue() {
        assertEquals(0.0, LevenshteinSimilarity().minValue(), 0.0)
    }

    @Test
    fun testMaxValue() {
        assertEquals(1.0, LevenshteinSimilarity().maxValue(), 0.0)
    }

    @Test
    fun testIsGainMetric() {
        assertTrue(LevenshteinSimilarity().isGainMetric())
    }

}