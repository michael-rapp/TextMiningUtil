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
 * Tests the functionality of the class [DamerauLevenshteinSimilarity].
 *
 * @author Michael Rapp
 */
class DamerauLevenshteinSimilarityTest {

    @Test
    fun testEvaluate() {
        val damerauLevenshteinSimilarity = DamerauLevenshteinSimilarity()
        assertEquals(0.0, damerauLevenshteinSimilarity.evaluate("CA", ""), 0.0)
        assertEquals(0.0, damerauLevenshteinSimilarity.evaluate("", "ABC"), 0.0)
        assertEquals(1.0, damerauLevenshteinSimilarity.evaluate("", ""), 0.0)
        assertEquals(1.0, damerauLevenshteinSimilarity.evaluate("CA", "CA"), 0.0)
        assertEquals(1.0, damerauLevenshteinSimilarity.evaluate("ABC", "ABC"), 0.0)
        assertEquals(0.66, damerauLevenshteinSimilarity.evaluate("ABC", "ABD"), 0.01)
        assertEquals(0.66, damerauLevenshteinSimilarity.evaluate("ABD", "ABC"), 0.01)
        assertEquals(0.66, damerauLevenshteinSimilarity.evaluate("ABC", "ACB"), 0.01)
        assertEquals(0.66, damerauLevenshteinSimilarity.evaluate("ACB", "ABC"), 0.01)
        assertEquals(0.33, damerauLevenshteinSimilarity.evaluate("CA", "ABC"), 0.01)
        assertEquals(0.33, damerauLevenshteinSimilarity.evaluate("ABC", "CA"), 0.01)
    }

    @Test
    fun testMinValue() {
        assertEquals(0.0, DamerauLevenshteinSimilarity().minValue(), 0.0)
    }

    @Test
    fun testMaxValue() {
        assertEquals(1.0, DamerauLevenshteinSimilarity().maxValue(), 0.0)
    }

    @Test
    fun testIsGainMetric() {
        assertTrue(DamerauLevenshteinSimilarity().isGainMetric())
    }

}