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
package de.mrapp.textmining.util.parser.numbers

import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals


/**
 * Tests the functionality of the class [EnNumberParser].
 *
 * @author Michael Rapp
 */
class EnNumberParserTest {

    private val parser = NumberParser.Builder().build(Locale.ENGLISH)

    @Test
    fun testParse() {
        assertEquals(parser.parse("zero"), 0)
        assertEquals(parser.parse("null"), 0)
        assertEquals(parser.parse("nil"), 0)
        assertEquals(parser.parse("one"), 1)
        assertEquals(parser.parse("two"), 2)
        assertEquals(parser.parse("three"), 3)
        assertEquals(parser.parse("four"), 4)
        assertEquals(parser.parse("five"), 5)
        assertEquals(parser.parse("six"), 6)
        assertEquals(parser.parse("seven"), 7)
        assertEquals(parser.parse("eight"), 8)
        assertEquals(parser.parse("nine"), 9)

        assertEquals(parser.parse("ten"), 10)
        assertEquals(parser.parse("eleven"), 11)
        assertEquals(parser.parse("twelve"), 12)
        assertEquals(parser.parse("thirteen"), 13)
        assertEquals(parser.parse("four-teen"), 14)
        assertEquals(parser.parse("fifteen"), 15)
        assertEquals(parser.parse("six-teen"), 16)
        assertEquals(parser.parse("seven-teen"), 17)
        assertEquals(parser.parse("eighteen"), 18)
        assertEquals(parser.parse("nine-teen"), 19)

        assertEquals(parser.parse("twenty"), 20)
        assertEquals(parser.parse("twenty-one"), 21)
        assertEquals(parser.parse("twenty-two"), 22)
        assertEquals(parser.parse("twenty-three"), 23)
        assertEquals(parser.parse("twenty-four"), 24)
        assertEquals(parser.parse("twenty-five"), 25)
        assertEquals(parser.parse("twenty-six"), 26)
        assertEquals(parser.parse("twenty-seven"), 27)
        assertEquals(parser.parse("twenty-eight"), 28)
        assertEquals(parser.parse("twenty-nine"), 29)

        assertEquals(parser.parse("thirty"), 30)
        assertEquals(parser.parse("thirty-one"), 31)
        assertEquals(parser.parse("thirty-two"), 32)
        assertEquals(parser.parse("thirty-three"), 33)
        assertEquals(parser.parse("thirty-four"), 34)
        assertEquals(parser.parse("thirty-five"), 35)
        assertEquals(parser.parse("thirty-six"), 36)
        assertEquals(parser.parse("thirty-seven"), 37)
        assertEquals(parser.parse("thirty-eight"), 38)
        assertEquals(parser.parse("thirty-nine"), 39)
    }

    @Test
    fun testLocale() {
        assertEquals(Locale.ENGLISH, parser.getLocale())
    }

}