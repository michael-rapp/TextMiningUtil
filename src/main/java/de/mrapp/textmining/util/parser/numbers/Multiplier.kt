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
import de.mrapp.util.Condition

/**
 * An operator that multiplies two [NumericValue]s.
 *
 * @property minValue The minimum value of the second value
 * @property maxValue The maximum value of the second value
 * @author Michael Rapp
 * @since 2.1.0
 */
class Multiplier(val minValue: Int = Int.MIN_VALUE, val maxValue: Int = Int.MAX_VALUE) :
        Operand<Int> {

    override fun apply(first: NumericValue<Int>, second: NumericValue<Int>): NumericValue<Int> {
        Condition.ensureAtLeast(second.value, minValue, "The value must be at least $minValue",
                MalformedTextException::class.java)
        Condition.ensureAtMaximum(second.value, maxValue, "The value must be at maximum $maxValue",
                MalformedTextException::class.java)
        return Number(first.value * second.value)
    }

}
