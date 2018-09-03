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
import de.mrapp.textmining.util.tokenizer.Substring
import java.util.regex.Pattern
import kotlin.test.*

/**
 * Tests the functionality of the class [Matcher].
 *
 * @author Michael Rapp
 */
class MatcherTest {

    @Test
    fun testEquals() {
        val matcher = Matcher.equals<Substring, String>()
        assertTrue(matcher.isGainMetric())
        val text = "foo"
        val token = Substring(text)
        assertTrue(matcher.matches(token, text))
        assertFalse(matcher.matches(token, "FOO"))
        val match = matcher.getMatch(token, text)
        assertNotNull(match)
        assertEquals(match!!.first, token)
        assertEquals(match.second, text)
        assertEquals(match.heuristicValue, 1.0)
        assertNull(matcher.getMatch(token, "FOO"))
    }

    @Test
    fun testEqualsIgnoreCase() {
        val matcher = Matcher.equals<Substring, String>(true)
        assertTrue(matcher.isGainMetric())
        val text = "foo"
        val token = Substring(text.toUpperCase())
        assertTrue(matcher.matches(token, text))
        assertFalse(matcher.matches(token, "bar"))
        val match = matcher.getMatch(token, text)
        assertNotNull(match)
        assertEquals(match!!.first, token)
        assertEquals(match.second, text)
        assertEquals(match.heuristicValue, 1.0)
        assertNull(matcher.getMatch(token, "bar"))
    }

    @Test
    fun testStartsWith() {
        val matcher = Matcher.startsWith<Substring, String>()
        assertTrue(matcher.isGainMetric())
        val text = "foo"
        val token = Substring("${text}bar")
        assertTrue(matcher.matches(token, text))
        assertFalse(matcher.matches(token, "FOO"))
        val match = matcher.getMatch(token, text)
        assertNotNull(match)
        assertEquals(match!!.first, token)
        assertEquals(match.second, text)
        assertEquals(match.heuristicValue, 1.0)
        assertNull(matcher.getMatch(token, "FOO"))
    }

    @Test
    fun testStartsWithIgnoreCase() {
        val matcher = Matcher.startsWith<Substring, String>()
        assertTrue(matcher.isGainMetric())
        val text = "foo"
        val token = Substring("${text.toUpperCase()}bar")
        assertTrue(matcher.matches(token, text))
        assertFalse(matcher.matches(token, "bar"))
        val match = matcher.getMatch(token, text)
        assertNotNull(match)
        assertEquals(match!!.first, token)
        assertEquals(match.second, text)
        assertEquals(match.heuristicValue, 1.0)
        assertNull(matcher.getMatch(token, "bar"))
    }

    @Test
    fun testEndsWith() {
        val matcher = Matcher.endsWith<Substring, String>()
        assertTrue(matcher.isGainMetric())
        val text = "foo"
        val token = Substring("bar$text")
        assertTrue(matcher.matches(token, text))
        assertFalse(matcher.matches(token, "FOO"))
        val match = matcher.getMatch(token, text)
        assertNotNull(match)
        assertEquals(match!!.first, token)
        assertEquals(match.second, text)
        assertEquals(match.heuristicValue, 1.0)
        assertNull(matcher.getMatch(token, "FOO"))
    }

    @Test
    fun testEndsWithIgnoreCase() {
        val matcher = Matcher.endsWith<Substring, String>(true)
        assertTrue(matcher.isGainMetric())
        val text = "foo"
        val token = Substring("bar${text.toUpperCase()}")
        assertTrue(matcher.matches(token, text))
        assertFalse(matcher.matches(token, "bar"))
        val match = matcher.getMatch(token, text)
        assertNotNull(match)
        assertEquals(match!!.first, token)
        assertEquals(match.second, text)
        assertEquals(match.heuristicValue, 1.0)
        assertNull(matcher.getMatch(token, "bar"))
    }

    @Test
    fun testContains() {
        val matcher = Matcher.contains<Substring, String>()
        assertTrue(matcher.isGainMetric())
        val text = "foo"
        val token = Substring("bar${text}bar")
        assertTrue(matcher.matches(token, text))
        assertFalse(matcher.matches(token, "FOO"))
        val match = matcher.getMatch(token, text)
        assertNotNull(match)
        assertEquals(match!!.first, token)
        assertEquals(match.second, text)
        assertEquals(match.heuristicValue, 1.0)
        assertNull(matcher.getMatch(token, "FOO"))
    }

    @Test
    fun testContainsCaseInsensitive() {
        val matcher = Matcher.contains<Substring, String>(true)
        assertTrue(matcher.isGainMetric())
        val text = "foo"
        val token = Substring("bar${text.toUpperCase()}bar")
        assertTrue(matcher.matches(token, text))
        assertFalse(matcher.matches(token, "bar"))
        val match = matcher.getMatch(token, text)
        assertNotNull(match)
        assertEquals(match!!.first, token)
        assertEquals(match.second, text)
        assertEquals(match.heuristicValue, 1.0)
        assertNull(matcher.getMatch(token, "bar"))
    }

    @Test
    fun testPattern() {
        val matcher = Matcher.pattern<Substring>()
        assertTrue(matcher.isGainMetric())
        val token = Substring("abc")
        val pattern = Pattern.compile("[a-z]")
        assertTrue(matcher.matches(token, pattern))
        assertFalse(matcher.matches(token, Pattern.compile("[0-9]")))
        val match = matcher.getMatch(token, pattern)
        assertNotNull(match)
        assertEquals(match!!.first, token)
        assertEquals(match.second, pattern)
        assertEquals(match.heuristicValue, 1.0)
        assertNull(matcher.getMatch(token, Pattern.compile("[0-9]")))
    }

    @Test
    fun testMetric() {
        val metric = LevenshteinDistance()
        val matcher = Matcher.metric<Substring, CharSequence>(metric, 1.0)
        assertEquals(metric.isGainMetric(), matcher.isGainMetric())
        val text = "foo"
        val token = Substring("${text}b")
        assertTrue(matcher.matches(token, text))
        assertFalse(matcher.matches(token, "foobar"))
        val match = matcher.getMatch(token, text)
        assertNotNull(match)
        assertEquals(match!!.first, token)
        assertEquals(match.second, text)
        assertEquals(match.heuristicValue, 1.0)
        assertNull(matcher.getMatch(token, "foobar"))
    }

}