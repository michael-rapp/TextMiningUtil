/*
 * Copyright 2017 Michael Rapp
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
package de.mrapp.textmining.util.metrics;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests the functionality of the class {@link DiceCoefficient}.
 */
public class DiceCoefficientTest {

    @Test
    public final void testDefaultConstructor() {
        DiceCoefficient diceCoefficient = new DiceCoefficient();
        assertEquals(1, diceCoefficient.getMinLength());
        assertEquals(Integer.MAX_VALUE, diceCoefficient.getMaxLength());
    }

    @Test
    public final void testConstructorWithMaxLengthParameter() {
        int maxLength = 3;
        DiceCoefficient diceCoefficient = new DiceCoefficient(maxLength);
        assertEquals(1, diceCoefficient.getMinLength());
        assertEquals(maxLength, diceCoefficient.getMaxLength());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testConstructorWithMaxLengthParameterThrowsExceptionIfMaxLengthIsLessThanOne() {
        new DiceCoefficient(0);
    }

    @Test
    public final void testConstructorWithMinAndMaxLengthParameters() {
        int minLength = 2;
        int maxLength = 3;
        DiceCoefficient diceCoefficient = new DiceCoefficient(minLength, maxLength);
        assertEquals(minLength, diceCoefficient.getMinLength());
        assertEquals(maxLength, diceCoefficient.getMaxLength());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testConstructorWithMinAndMaxLengthParametersThrowsExceptionIfMinLengthIsLessThanOne() {
        new DiceCoefficient(0, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testConstructorWithMinAndMaxLengthParametersThrowsExceptionIfMaxLengthIsLessThanMinLength() {
        new DiceCoefficient(2, 1);
    }

    @Test
    public final void testEvaluate() {
        DiceCoefficient diceCoefficient = new DiceCoefficient();
        assertEquals(1, diceCoefficient.evaluate("wirk", "wirk"), 0);
        assertEquals(0.5, diceCoefficient.evaluate("wirk", "work"), 0);
        assertEquals(0, diceCoefficient.evaluate("wirk", "xxxx"), 0);
    }

    @Test
    public final void testEvaluateWithMinAndMaxLength() {
        DiceCoefficient diceCoefficient = new DiceCoefficient(1, 1);
        assertEquals(1, diceCoefficient.evaluate("abcd", "abcd"), 0);
        assertEquals(0.75, diceCoefficient.evaluate("abcd", "abcx"), 0);
        assertEquals(0.57, diceCoefficient.evaluate("abcd", "abxx"), 0.1);
        assertEquals(0.33, diceCoefficient.evaluate("abcd", "axxx"), 0.1);
        assertEquals(0, diceCoefficient.evaluate("abcd", "xxxx"), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testEvaluateThrowsExceptionIfFistTextIsNull() {
        new DiceCoefficient().evaluate(null, "foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testEvaluateThrowsExceptionIfSecondTextIsNull() {
        new DiceCoefficient().evaluate("foo", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testEvaluateThrowsExceptionIfFistTextIsEmpty() {
        new DiceCoefficient().evaluate("", "foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testEvaluateThrowsExceptionIfSecondTextIsEmpty() {
        new DiceCoefficient().evaluate("foo", "");
    }

    @Test
    public final void testMinValue() {
        assertEquals(0, new DiceCoefficient().minValue(), 0);
    }

    @Test
    public final void testMaxValue() {
        assertEquals(1, new DiceCoefficient().maxValue(), 0);
    }

    @Test
    public final void testIsGainMetric() {
        assertTrue(new DiceCoefficient().isGainMetric());
    }

}