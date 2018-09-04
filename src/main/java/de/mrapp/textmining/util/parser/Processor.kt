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

/**
 * Defines the interface, a processor that is applied to certain data and returns data of another
 * type, must implement.
 *
 * @param I The type of the data to process
 * @param O The type of the resulting data
 * @author Michael Rapp
 * @since 2.1.0
 */
interface Processor<I, O> {

    /**
     * Processes a certain [input] and returns the result.
     */
    fun process(input: I): O

}