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
package de.mrapp.textmining.util.parser

import de.mrapp.textmining.util.metrics.LevenshteinDistance
import kotlin.test.*

/**
 * Tests the functionality of the class [Dictionary].
 */
class DictionaryTest {

    @Test
    fun testLookupByKey() {
        val dictionary = Dictionary<String, Int>()
        dictionary.addEntry(Dictionary.Entry("one", 1))
        dictionary.addEntry(Dictionary.Entry("two", 2, AssociationType.BIDIRECTIONAL))
        dictionary.addEntry(Dictionary.Entry("three", 3))
        val entry = dictionary.lookup("two")
        assertNotNull(entry)
        assertEquals("two", entry!!.key)
        assertEquals(2, entry.value)
        assertEquals(AssociationType.BIDIRECTIONAL, entry.associationType)
    }

    @Test
    fun testLookupByKeyReturnsNull() {
        val dictionary = Dictionary<String, Int>()
        dictionary.addEntry(Dictionary.Entry("one", 1))
        dictionary.addEntry(Dictionary.Entry("two", 2))
        dictionary.addEntry(Dictionary.Entry("three", 3))
        val entry = dictionary.lookup("foo")
        assertNull(entry)
    }

    @Test
    fun testLookupUsingMatcher() {
        val dictionary = Dictionary<String, Int>()
        dictionary.addEntry(Dictionary.Entry("one", 1))
        dictionary.addEntry(Dictionary.Entry("two", 2))
        dictionary.addEntry(Dictionary.Entry("twoo", 2))
        dictionary.addEntry(Dictionary.Entry("three", 3))
        val matches = dictionary.lookup("two", Matcher.metric(LevenshteinDistance(), 1.0))
        assertNotNull(matches)
        assertTrue(matches.iterator().asSequence()
                .contains(Match(Dictionary.Entry("two", 2), "two", 0.0)))
        assertTrue(matches.iterator().asSequence()
                .contains(Match(Dictionary.Entry("twoo", 2), "two", 1.0)))
    }

    @Test
    fun testLookupUsingMatcherReturnsNoMatches() {
        val dictionary = Dictionary<String, Int>()
        dictionary.addEntry(Dictionary.Entry("one", 1))
        dictionary.addEntry(Dictionary.Entry("two", 2))
        dictionary.addEntry(Dictionary.Entry("twoo", 2))
        dictionary.addEntry(Dictionary.Entry("three", 3))
        val matches = dictionary.lookup("foo", Matcher.metric(LevenshteinDistance(), 1.0))
        assertNotNull(matches)
        assertFalse(matches.iterator().hasNext())
    }

}
