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
package de.mrapp.textmining.util.parser

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Tests the functionality of the class [ValueToken].
 */
class ValueTokenTest {

    @Test
    fun testConstructor() {
        val token = "token"
        val value = "foo"
        val position = 1
        val valueToken = ValueToken(token, value, position)
        assertEquals(token, valueToken.token)
        assertEquals(1, valueToken.positions.size)
        assertTrue(valueToken.positions.contains(position))
        assertEquals(token.length, valueToken.length)
        assertEquals(value, valueToken.value)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testConstructorThrowsExceptionIfTokenIsEmpty() {
        ValueToken("", "foo", 0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testConstructorThrowsExceptionIfPositionIsLessThanZero() {
        ValueToken("token", "foo", -1)
    }

}
