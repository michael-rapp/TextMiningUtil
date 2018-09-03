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

import kotlin.test.*

/**
 * Tests the functionality of the class [Matches].
 *
 * @author Michael Rapp
 */
class MatchesTest {

    @Test
    fun testConstructor() {
        val match1 = Match("foo", 0.25)
        val match2 = Match("bar", 0.5)
        val isGainMetric = true
        val matches = Matches(listOf(match1, match2), isGainMetric)
        assertEquals(isGainMetric, matches.isGainMetric)
        val iterator = matches.iterator()
        assertTrue(iterator.hasNext())
        assertEquals(match1, iterator.next())
        assertTrue(iterator.hasNext())
        assertEquals(match2, iterator.next())
        assertFalse(iterator.hasNext())
    }

    @Test
    fun testGetMatchBestIfNoMatchesAvailable() {
        val matches = Matches<String>(emptyList(), true)
        val bestMatch = matches.getBestMatch()
        assertNull(bestMatch)
    }

    @Test
    fun testGetBestMatchIfOneMatchAvailable() {
        val match = Match("foo", 1.0)
        val matches = Matches(listOf(match), true)
        val bestMatch = matches.getBestMatch()
        assertEquals(match, bestMatch)
    }

    @Test
    fun testGetBestMatchIfGainMetric() {
        val match1 = Match("foo", 0.25)
        val match2 = Match("bar", 0.75)
        val match3 = Match("foobar", 0.5)
        val matches = Matches(listOf(match1, match2, match3), true)
        val bestMatch = matches.getBestMatch()
        assertEquals(match2, bestMatch)
    }

    @Test
    fun testGetBestMatchIfLossMetric() {
        val match1 = Match("foo", 0.5)
        val match2 = Match("bar", 0.25)
        val match3 = Match("foobar", 0.75)
        val matches = Matches(listOf(match1, match2, match3), false)
        val bestMatch = matches.getBestMatch()
        assertEquals(match2, bestMatch)
    }

    @Test
    fun testGetBestMatchUsingTieBreaker() {
        val match1 = Match("foo", 0.5)
        val match2 = Match("bar", 0.5)
        val matches = Matches(listOf(match1, match2), true)
        val bestMatch = matches.getBestMatch { _, match -> match }
        assertEquals(match2, bestMatch)
    }

}