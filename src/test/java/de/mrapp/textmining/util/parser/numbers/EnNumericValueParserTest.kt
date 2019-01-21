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
        assertEquals(14, parser.parse("four teen"))
        assertEquals(15, parser.parse("fifteen"))
        assertEquals(16, parser.parse("six-teen"))
        assertEquals(16, parser.parse("six teen"))
        assertEquals(17, parser.parse("seven-teen"))
        assertEquals(17, parser.parse("seven teen"))
        assertEquals(18, parser.parse("eighteen"))
        assertEquals(19, parser.parse("nine-teen"))
        assertEquals(19, parser.parse("nine teen"))

        assertEquals(20, parser.parse("twenty"))
        assertEquals(21, parser.parse("twenty-one"))
        assertEquals(21, parser.parse("twenty one"))
        assertEquals(22, parser.parse("twenty-two"))
        assertEquals(22, parser.parse("twenty two"))
        assertEquals(23, parser.parse("twenty-three"))
        assertEquals(23, parser.parse("twenty three"))
        assertEquals(24, parser.parse("twenty-four"))
        assertEquals(24, parser.parse("twenty four"))
        assertEquals(25, parser.parse("twenty-five"))
        assertEquals(25, parser.parse("twenty five"))
        assertEquals(26, parser.parse("twenty-six"))
        assertEquals(26, parser.parse("twenty six"))
        assertEquals(27, parser.parse("twenty-seven"))
        assertEquals(27, parser.parse("twenty seven"))
        assertEquals(28, parser.parse("twenty-eight"))
        assertEquals(28, parser.parse("twenty eight"))
        assertEquals(29, parser.parse("twenty-nine"))
        assertEquals(29, parser.parse("twenty nine"))

        assertEquals(30, parser.parse("thirty"))
        assertEquals(31, parser.parse("thirty-one"))
        assertEquals(31, parser.parse("thirty one"))
        assertEquals(32, parser.parse("thirty-two"))
        assertEquals(32, parser.parse("thirty two"))
        assertEquals(33, parser.parse("thirty-three"))
        assertEquals(33, parser.parse("thirty three"))
        assertEquals(34, parser.parse("thirty-four"))
        assertEquals(34, parser.parse("thirty four"))
        assertEquals(35, parser.parse("thirty-five"))
        assertEquals(35, parser.parse("thirty five"))
        assertEquals(36, parser.parse("thirty-six"))
        assertEquals(36, parser.parse("thirty six"))
        assertEquals(37, parser.parse("thirty-seven"))
        assertEquals(37, parser.parse("thirty seven"))
        assertEquals(38, parser.parse("thirty-eight"))
        assertEquals(38, parser.parse("thirty eight"))
        assertEquals(39, parser.parse("thirty-nine"))
        assertEquals(39, parser.parse("thirty nine"))

        assertEquals(40, parser.parse("forty"))
        assertEquals(41, parser.parse("forty-one"))
        assertEquals(41, parser.parse("forty one"))
        assertEquals(42, parser.parse("forty-two"))
        assertEquals(42, parser.parse("forty two"))
        assertEquals(43, parser.parse("forty-three"))
        assertEquals(43, parser.parse("forty three"))
        assertEquals(44, parser.parse("forty-four"))
        assertEquals(44, parser.parse("forty four"))
        assertEquals(45, parser.parse("forty-five"))
        assertEquals(45, parser.parse("forty five"))
        assertEquals(46, parser.parse("forty-six"))
        assertEquals(46, parser.parse("forty six"))
        assertEquals(47, parser.parse("forty-seven"))
        assertEquals(47, parser.parse("forty seven"))
        assertEquals(48, parser.parse("forty-eight"))
        assertEquals(48, parser.parse("forty eight"))
        assertEquals(49, parser.parse("forty-nine"))
        assertEquals(49, parser.parse("forty nine"))

        assertEquals(50, parser.parse("fifty"))
        assertEquals(51, parser.parse("fifty-one"))
        assertEquals(51, parser.parse("fifty one"))
        assertEquals(52, parser.parse("fifty-two"))
        assertEquals(52, parser.parse("fifty two"))
        assertEquals(53, parser.parse("fifty-three"))
        assertEquals(53, parser.parse("fifty three"))
        assertEquals(54, parser.parse("fifty-four"))
        assertEquals(54, parser.parse("fifty four"))
        assertEquals(55, parser.parse("fifty-five"))
        assertEquals(55, parser.parse("fifty five"))
        assertEquals(56, parser.parse("fifty-six"))
        assertEquals(56, parser.parse("fifty six"))
        assertEquals(57, parser.parse("fifty-seven"))
        assertEquals(57, parser.parse("fifty seven"))
        assertEquals(58, parser.parse("fifty-eight"))
        assertEquals(58, parser.parse("fifty eight"))
        assertEquals(59, parser.parse("fifty-nine"))
        assertEquals(59, parser.parse("fifty nine"))

        assertEquals(60, parser.parse("sixty"))
        assertEquals(61, parser.parse("sixty-one"))
        assertEquals(61, parser.parse("sixty one"))
        assertEquals(62, parser.parse("sixty-two"))
        assertEquals(62, parser.parse("sixty two"))
        assertEquals(63, parser.parse("sixty-three"))
        assertEquals(63, parser.parse("sixty three"))
        assertEquals(64, parser.parse("sixty-four"))
        assertEquals(64, parser.parse("sixty four"))
        assertEquals(65, parser.parse("sixty-five"))
        assertEquals(65, parser.parse("sixty five"))
        assertEquals(66, parser.parse("sixty-six"))
        assertEquals(66, parser.parse("sixty six"))
        assertEquals(67, parser.parse("sixty-seven"))
        assertEquals(67, parser.parse("sixty seven"))
        assertEquals(68, parser.parse("sixty-eight"))
        assertEquals(68, parser.parse("sixty eight"))
        assertEquals(69, parser.parse("sixty-nine"))
        assertEquals(69, parser.parse("sixty nine"))

        assertEquals(70, parser.parse("seventy"))
        assertEquals(71, parser.parse("seventy-one"))
        assertEquals(71, parser.parse("seventy one"))
        assertEquals(72, parser.parse("seventy-two"))
        assertEquals(72, parser.parse("seventy two"))
        assertEquals(73, parser.parse("seventy-three"))
        assertEquals(73, parser.parse("seventy three"))
        assertEquals(74, parser.parse("seventy-four"))
        assertEquals(74, parser.parse("seventy four"))
        assertEquals(75, parser.parse("seventy-five"))
        assertEquals(75, parser.parse("seventy five"))
        assertEquals(76, parser.parse("seventy-six"))
        assertEquals(76, parser.parse("seventy six"))
        assertEquals(77, parser.parse("seventy-seven"))
        assertEquals(77, parser.parse("seventy seven"))
        assertEquals(78, parser.parse("seventy-eight"))
        assertEquals(78, parser.parse("seventy eight"))
        assertEquals(79, parser.parse("seventy-nine"))
        assertEquals(79, parser.parse("seventy nine"))

        assertEquals(80, parser.parse("eighty"))
        assertEquals(81, parser.parse("eighty-one"))
        assertEquals(81, parser.parse("eighty one"))
        assertEquals(82, parser.parse("eighty-two"))
        assertEquals(82, parser.parse("eighty two"))
        assertEquals(83, parser.parse("eighty-three"))
        assertEquals(83, parser.parse("eighty three"))
        assertEquals(84, parser.parse("eighty-four"))
        assertEquals(84, parser.parse("eighty four"))
        assertEquals(85, parser.parse("eighty-five"))
        assertEquals(85, parser.parse("eighty five"))
        assertEquals(86, parser.parse("eighty-six"))
        assertEquals(86, parser.parse("eighty six"))
        assertEquals(87, parser.parse("eighty-seven"))
        assertEquals(87, parser.parse("eighty seven"))
        assertEquals(88, parser.parse("eighty-eight"))
        assertEquals(88, parser.parse("eighty eight"))
        assertEquals(89, parser.parse("eighty-nine"))
        assertEquals(89, parser.parse("eighty nine"))

        assertEquals(90, parser.parse("ninety"))
        assertEquals(91, parser.parse("ninety-one"))
        assertEquals(91, parser.parse("ninety one"))
        assertEquals(92, parser.parse("ninety-two"))
        assertEquals(92, parser.parse("ninety two"))
        assertEquals(93, parser.parse("ninety-three"))
        assertEquals(93, parser.parse("ninety three"))
        assertEquals(94, parser.parse("ninety-four"))
        assertEquals(94, parser.parse("ninety four"))
        assertEquals(95, parser.parse("ninety-five"))
        assertEquals(95, parser.parse("ninety five"))
        assertEquals(96, parser.parse("ninety-six"))
        assertEquals(96, parser.parse("ninety six"))
        assertEquals(97, parser.parse("ninety-seven"))
        assertEquals(97, parser.parse("ninety seven"))
        assertEquals(98, parser.parse("ninety-eight"))
        assertEquals(98, parser.parse("ninety eight"))
        assertEquals(99, parser.parse("ninety-nine"))
        assertEquals(99, parser.parse("ninety nine"))

        assertEquals(100, parser.parse("one-hundred"))
    }

    @Test
    fun testLocale() {
        assertEquals(Locale.ENGLISH, parser.getLocale())
    }

}