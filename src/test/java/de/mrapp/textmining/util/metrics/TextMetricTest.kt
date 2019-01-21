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

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests the functionality of the class [TextMetric].
 */
class TextMetricTest {

    @Test
    fun testCaseInsensitive() {
        val metric = LevenshteinDistance()
        assertEquals(3.0, metric.evaluate("foo", "FOO"))
        val caseInsensitiveMetric = TextMetric.caseInsensitive(metric)
        assertEquals(0.0, caseInsensitiveMetric.evaluate("foo", "FOO"))
    }

    @Test
    fun testComparatorIfMetricIsGainMetric() {
        val metric = LevenshteinSimilarity()
        val comparator = TextMetric.Comparator(metric)
        assertEquals(0, comparator.compare(null, null))
        assertEquals(1, comparator.compare(null, 0.5))
        assertEquals(-1, comparator.compare(0.5, null))
        assertEquals(1, comparator.compare(0.1, 0.5))
        assertEquals(0, comparator.compare(0.5, 0.5))
        assertEquals(-1, comparator.compare(0.9, 0.5))
    }

    @Test
    fun testComparatorIfMetricIsLossMetric() {
        val metric = LevenshteinDissimilarity()
        val comparator = TextMetric.Comparator(metric)
        assertEquals(0, comparator.compare(null, null))
        assertEquals(1, comparator.compare(null, 0.5))
        assertEquals(-1, comparator.compare(0.5, null))
        assertEquals(-1, comparator.compare(0.1, 0.5))
        assertEquals(0, comparator.compare(0.5, 0.5))
        assertEquals(1, comparator.compare(0.9, 0.5))
    }

}
