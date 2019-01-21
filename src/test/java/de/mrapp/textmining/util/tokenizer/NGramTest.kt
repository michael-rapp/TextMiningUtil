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
package de.mrapp.textmining.util.tokenizer

import org.junit.Assert
import kotlin.test.Test

/**
 * Tests the functionality of the class [NGram].
 */
class NGramTest {

    @Test
    fun testConstructor() {
        val n = 2
        val token = "token"
        val position = 1
        val nGram = NGram(n, token, position)
        Assert.assertEquals(n, nGram.n)
        Assert.assertEquals(token, nGram.token)
        Assert.assertEquals(1, nGram.positions.size)
        Assert.assertTrue(nGram.positions.contains(position))
        Assert.assertEquals(token.length, nGram.length)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testConstructorThrowsExceptionIfTokenIsEmpty() {
        NGram(1, "", 0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testConstructorThrowsExceptionIfPositionIsLessThanZero() {
        NGram(1, "token", -1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testConstructorThrowsExceptionIfDegreeIsLessThanZero() {
        NGram(0, "token", 0)
    }

}
