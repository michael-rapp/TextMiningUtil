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

import de.mrapp.textmining.util.parser.MalformedTextException
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


/**
 * Tests the functionality of the class [EnNumberParser].
 *
 * @author Michael Rapp
 */
class EnNumericValueParserTest {

    private val parser = NumberParser.Builder().build(Locale.ENGLISH)

    @Test
    fun testParse() {
        assertFailsWith(MalformedTextException::class) { parser.parse("") }
        assertFailsWith(MalformedTextException::class) { parser.parse("invalid") }
        assertEquals(0, parser.parse("zero"))
        assertEquals(0, parser.parse("null"))
        assertEquals(0, parser.parse("nil"))
        assertEquals(1, parser.parse("one"))
        assertEquals(2, parser.parse("two"))
        assertEquals(3, parser.parse("three"))
        assertEquals(4, parser.parse("four"))
        assertEquals(5, parser.parse("five"))
        assertEquals(6, parser.parse("six"))
        assertEquals(7, parser.parse("seven"))
        assertEquals(8, parser.parse("eight"))
        assertEquals(9, parser.parse("nine"))

        assertEquals(10, parser.parse("ten"))
        assertFailsWith(MalformedTextException::class) { parser.parse("teen") }
        assertEquals(11, parser.parse("eleven"))
        assertEquals(12, parser.parse("twelve"))
        assertEquals(13, parser.parse("thirteen"))
        assertEquals(14, parser.parse("four-teen"))
        assertEquals(15, parser.parse("fifteen"))
        assertEquals(16, parser.parse("six-teen"))
        assertEquals(17, parser.parse("seven-teen"))
        assertEquals(18, parser.parse("eighteen"))
        assertEquals(19, parser.parse("nine-teen"))

        assertEquals(20, parser.parse("twenty"))
        assertEquals(21, parser.parse("twenty-one"))
        assertEquals(22, parser.parse("twenty-two"))
        assertEquals(23, parser.parse("twenty-three"))
        assertEquals(24, parser.parse("twenty-four"))
        assertEquals(25, parser.parse("twenty-five"))
        assertEquals(26, parser.parse("twenty-six"))
        assertEquals(27, parser.parse("twenty-seven"))
        assertEquals(28, parser.parse("twenty-eight"))
        assertEquals(29, parser.parse("twenty-nine"))

        assertEquals(30, parser.parse("thirty"))
        assertEquals(31, parser.parse("thirty-one"))
        assertEquals(32, parser.parse("thirty-two"))
        assertEquals(33, parser.parse("thirty-three"))
        assertEquals(34, parser.parse("thirty-four"))
        assertEquals(35, parser.parse("thirty-five"))
        assertEquals(36, parser.parse("thirty-six"))
        assertEquals(37, parser.parse("thirty-seven"))
        assertEquals(38, parser.parse("thirty-eight"))
        assertEquals(39, parser.parse("thirty-nine"))
    }

    @Test
    fun testLocale() {
        assertEquals(Locale.ENGLISH, parser.getLocale())
    }

}