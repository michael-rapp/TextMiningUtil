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
package de.mrapp.textmining.util.metrics

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import kotlin.test.Test

/**
 * Tests the functionality of the class [DiceCoefficient].
 *
 * @author Michael Rapp
 */
class DiceCoefficientTest {

    @Test
    fun testDefaultConstructor() {
        val diceCoefficient = DiceCoefficient()
        assertEquals(1, diceCoefficient.minLength)
        assertEquals(Integer.MAX_VALUE, diceCoefficient.maxLength)
    }

    @Test
    fun testConstructorMinLengthParameter() {
        val minLength = 3
        val diceCoefficient = DiceCoefficient(minLength)
        assertEquals(minLength, diceCoefficient.minLength)
        assertEquals(Int.MAX_VALUE, diceCoefficient.maxLength)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testConstructorWithMinLengthParameterThrowsExceptionIfMinLengthIsLessThanOne() {
        DiceCoefficient(0)
    }

    @Test
    fun testConstructorWithMinAndMaxLengthParameters() {
        val minLength = 2
        val maxLength = 3
        val diceCoefficient = DiceCoefficient(minLength, maxLength)
        assertEquals(minLength, diceCoefficient.minLength)
        assertEquals(maxLength, diceCoefficient.maxLength)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testConstructorWithMinAndMaxLengthParametersThrowsExceptionIfMinLengthIsLessThanOne() {
        DiceCoefficient(0, 2)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testConstructorWithMinAndMaxLengthParametersThrowsExceptionIfMaxLengthIsLessThanMinLength() {
        DiceCoefficient(2, 1)
    }

    @Test
    fun testEvaluate() {
        val diceCoefficient = DiceCoefficient()
        assertEquals(1.0, diceCoefficient.evaluate("wirk", "wirk"), 0.0)
        assertEquals(0.5, diceCoefficient.evaluate("wirk", "work"), 0.0)
        assertEquals(0.0, diceCoefficient.evaluate("wirk", "xxxx"), 0.0)
    }

    @Test
    fun testEvaluateWithMinAndMaxLength() {
        val diceCoefficient = DiceCoefficient(1, 1)
        assertEquals(1.0, diceCoefficient.evaluate("abcd", "abcd"), 0.0)
        assertEquals(0.75, diceCoefficient.evaluate("abcd", "abcx"), 0.0)
        assertEquals(0.57, diceCoefficient.evaluate("abcd", "abxx"), 0.1)
        assertEquals(0.33, diceCoefficient.evaluate("abcd", "axxx"), 0.1)
        assertEquals(0.0, diceCoefficient.evaluate("abcd", "xxxx"), 0.0)
    }

    @Test
    fun testEvaluateThrowsExceptionIfFistTextIsEmpty() {
        assertEquals(0.0, DiceCoefficient().evaluate("", "foo"), 0.0)
    }

    @Test
    fun testEvaluateThrowsExceptionIfSecondTextIsEmpty() {
        assertEquals(0.0, DiceCoefficient().evaluate("foo", ""), 0.0)
    }

    @Test
    fun testMinValue() {
        assertEquals(0.0, DiceCoefficient().minValue(), 0.0)
    }

    @Test
    fun testMaxValue() {
        assertEquals(1.0, DiceCoefficient().maxValue(), 0.0)
    }

    @Test
    fun testIsGainMetric() {
        assertTrue(DiceCoefficient().isGainMetric())
    }

}