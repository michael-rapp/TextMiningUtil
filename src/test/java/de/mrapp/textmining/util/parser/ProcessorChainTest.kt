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

/**
 * Tests the functionality of the class [ProcessorChain].
 *
 * @author Michael Rapp
 */
class ProcessorChainTest {

    private inner class Processor1 : Processor<Int, String> {

        override fun process(input: Int) = input.toString()

    }

    private inner class Processor2 : Processor<String, Double> {

        override fun process(input: String) = input.toDouble()

    }

    @Test
    fun testCreate() {
        val processorChain = ProcessorChain.create(Processor1())
        assertEquals("42", processorChain.process(42))
    }

    @Test
    fun testAppend() {
        val processorChain = ProcessorChain.create(Processor1()).append(Processor2())
        assertEquals(42.0, processorChain.process(42))
    }

}
