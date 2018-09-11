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

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests the functionality of the class [UnsupportedLocaleException].
 *
 * @author Michael Rapp
 */
class UnsupportedLocaleExceptionTest {

    @Test
    fun testConstructor() {
        val text = "text"
        val message = "message"
        val cause = Exception()
        val exception = UnsupportedLocaleException(text, message, cause)
        assertEquals(text, exception.text)
        assertEquals(message, exception.message)
        assertEquals(cause, exception.cause)
    }

}