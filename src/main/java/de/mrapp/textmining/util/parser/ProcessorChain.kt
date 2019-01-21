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

/**
 * A meta-processor that executes several processors in a predefined order. The result of the first
 * processor is passed to the second processor and so on.
 *
 * @param I The type of the data to be processed by the first processor
 * @param O The type of the resulting data of the last processor
 * @author Michael Rapp
 * @since 2.1.0
 */
class ProcessorChain<I, O> private constructor(private val processor: Processor<I, O>) :
        Processor<I, O> {

    companion object {

        /**
         * Creates a new processor chain consisting of a single [processor].
         */
        fun <I, O> create(processor: Processor<I, O>) = ProcessorChain(processor)

    }

    /**
     * Appends a new [processor] to the processor chain.
     */
    fun <T> append(processor: Processor<O, T>) = ProcessorChain(object : Processor<I, T> {

        override fun process(input: I) =
                processor.process(this@ProcessorChain.processor.process(input))

    })

    override fun process(input: I) = processor.process(input)

}
