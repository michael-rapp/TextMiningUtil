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

/**
 * Represents a [NumericValue] that can be used to modify another [NumericValue].
 *
 * @param T The type of the numeric value
 * @author Michael Rapp
 * @since 2.1.0
 */
interface Modifier<T : kotlin.Number> : NumericValue<T> {

    /**
     * Applies the modifier to a specific [value] and returns the result.
     */
    fun apply(value: NumericValue<T>): NumericValue<T>

}