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

import de.mrapp.textmining.util.Token
import de.mrapp.textmining.util.tokenizer.Substring
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Tests the functionality of the class [MutableToken].
 *
 * @author Michael Rapp
 */
class MutableTokenTest {

    @Test
    fun testConstructor() {
        val token = Substring("foo")
        val position = 2
        token.addPosition(position)
        val mutableToken = MutableToken(token)
        assertEquals(token, mutableToken.getCurrent())
        assertEquals(1, token.getPositions().size)
        assertTrue(token.getPositions().contains(position))
    }

    @Test
    fun testAddPosition() {
        val token = Substring("foo")
        val mutableToken = MutableToken(token)
        val position = 2
        mutableToken.addPosition(position)
        assertEquals(1, token.getPositions().size)
        assertTrue(token.getPositions().contains(position))
    }

    @Test
    fun testMutate() {
        val token1 = Substring("foo")
        val token2 = Substring("foo")
        val mutableToken = MutableToken(token1)
        assertEquals(token1, mutableToken.getCurrent())
        mutableToken.mutate(token2)
        assertEquals(token2, mutableToken.getCurrent())
    }

    @Test
    fun testMutateWithRevisionParameter() {
        val token1 = Substring("foo")
        val token2 = Substring("foo")
        val revision = 1
        val mutableToken = MutableToken(token1)
        assertEquals(token1, mutableToken.getCurrent())
        mutableToken.mutate(token2, revision)
        assertEquals(token2, mutableToken.getCurrent())
        assertEquals(token1, mutableToken.getRevision<Token>(revision))
    }

}
