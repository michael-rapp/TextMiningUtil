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

/**
 * Represents an integer number. It can be aggregated with the number left or right of it using
 * [Operand]s.
 *
 * @property value        The integer number
 * @property leftOperand  The left operand
 * @property rightOperand The right operand
 * @author Michael Rapp
 * @since 2.1.0
 */
class Number(value: Int, leftOperand: Operand<Int>? = null, rightOperand: Operand<Int>? = null) :
        Factor(value, leftOperand, rightOperand)
