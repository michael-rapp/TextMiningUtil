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
package de.mrapp.textmining.util.tokenizer;

import de.mrapp.textmining.util.tokenizer.SubstringTokenizer.Substring;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * Tests the functionality of the class {@link SubstringTokenizer}.
 *
 * @since 1.0.0
 */
public class SubstringTokenizerTest {

    @Test
    public final void testSubstringConstructor() {
        String token = "token";
        int position = 1;
        Substring substring = new Substring(token, position);
        assertEquals(token, substring.getToken());
        assertEquals(1, substring.getPositions().size());
        assertTrue(substring.getPositions().contains(position));
        assertEquals(token.length(), substring.length());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testSubstringConstructorThrowsExceptionIfTokenIsNull() {
        new Substring(null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testSubstringConstructorThrowsExceptionIfTokenIsEmpty() {
        new Substring("", 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testConstructorThrowsExceptionIfPositionIsLessThanZero() {
        new Substring("token", -1);
    }

    @Test
    public final void testSubstringClone() {
        Substring substring = new Substring("token", 1);
        Substring clone = substring.clone();
        assertEquals(substring.getToken(), clone.getToken());
        assertEquals(substring.getPositions().size(), clone.getPositions().size());
        assertTrue(clone.getPositions().contains(substring.getPositions().iterator().next()));
    }

    @Test
    public final void testSubstringToString() {
        String token = "token";
        int position = 1;
        Substring substring = new Substring(token, position);
        assertEquals("Substring [token=" + token + ", positions=[" + position + "]]",
                substring.toString());
    }

    @Test
    public final void testSubstringHashCode() {
        Substring substring1 = new Substring("token", 0);
        Substring substring2 = new Substring("token", 0);
        assertEquals(substring1.hashCode(), substring1.hashCode());
        assertEquals(substring1.hashCode(), substring2.hashCode());
        substring1 = new Substring("foo", 0);
        assertNotEquals(substring1.hashCode(), substring2.hashCode());
    }

    @Test
    public final void testSubstringEquals() {
        Substring substring1 = new Substring("token", 0);
        Substring substring2 = new Substring("token", 0);
        assertFalse(substring1.equals(null));
        assertFalse(substring1.equals(new Object()));
        assertTrue(substring1.equals(substring1));
        assertTrue(substring1.equals(substring2));
        substring1 = new Substring("foo", 0);
        assertFalse(substring1.equals(substring2));
    }

    @Test
    public final void testDefaultConstructor() {
        SubstringTokenizer substringTokenizer = new SubstringTokenizer();
        assertEquals(1, substringTokenizer.getMinLength());
        assertEquals(Integer.MAX_VALUE, substringTokenizer.getMaxLength());
    }

    @Test
    public final void testConstructorWithMinAndMaxLengthParameters() {
        int minLength = 2;
        int maxLength = 3;
        SubstringTokenizer substringTokenizer = new SubstringTokenizer(minLength, maxLength);
        assertEquals(minLength, substringTokenizer.getMinLength());
        assertEquals(maxLength, substringTokenizer.getMaxLength());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testConstructorWithMinAndMaxLengthParametersThrowsExceptionIfMinLengthIsLessThanOne() {
        new SubstringTokenizer(0, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testTokenizeWithMinAndMaxLengthParametersThrowsExceptionIfMaxLengthIsLessThanMinLength() {
        new SubstringTokenizer(2, 1);
    }

    @Test
    public final void testTokenize() {
        Set<Substring> substrings = new SubstringTokenizer().tokenize("text");
        assertEquals(8, substrings.size());

        for (Substring substring : substrings) {
            switch (substring.getToken()) {
                case "t":
                    assertTrue(substring.getPositions().contains(0));
                    assertTrue(substring.getPositions().contains(3));
                    break;
                case "e":
                    assertTrue(substring.getPositions().contains(1));
                    break;
                case "x":
                    assertTrue(substring.getPositions().contains(2));
                    break;
                case "te":
                    assertTrue(substring.getPositions().contains(0));
                    break;
                case "ex":
                    assertTrue(substring.getPositions().contains(1));
                    break;
                case "xt":
                    assertTrue(substring.getPositions().contains(2));
                    break;
                case "tex":
                    assertTrue(substring.getPositions().contains(0));
                    break;
                case "ext":
                    assertTrue(substring.getPositions().contains(1));
                    break;
                default:
                    fail();
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testTokenizeThrowsExceptionIfTextIsNull() {
        new SubstringTokenizer().tokenize(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testTokenizeThrowsExceptionIfTextIsEmpty() {
        new SubstringTokenizer().tokenize("");
    }

    @Test
    public final void testTokenizeWithMinAndMaxLength1() {
        Set<Substring> substrings = new SubstringTokenizer(2, 3).tokenize("text");
        assertEquals(5, substrings.size());

        for (Substring substring : substrings) {
            switch (substring.getToken()) {
                case "te":
                    assertTrue(substring.getPositions().contains(0));
                    break;
                case "ex":
                    assertTrue(substring.getPositions().contains(1));
                    break;
                case "xt":
                    assertTrue(substring.getPositions().contains(2));
                    break;
                case "tex":
                    assertTrue(substring.getPositions().contains(0));
                    break;
                case "ext":
                    assertTrue(substring.getPositions().contains(1));
                    break;
                default:
                    fail();
            }
        }
    }

    @Test
    public final void testTokenizeWithMinAndMaxLength2() {
        Set<Substring> substrings = new SubstringTokenizer(2, 2).tokenize("text");
        assertEquals(3, substrings.size());

        for (Substring substring : substrings) {
            switch (substring.getToken()) {
                case "te":
                    assertTrue(substring.getPositions().contains(0));
                    break;
                case "ex":
                    assertTrue(substring.getPositions().contains(1));
                    break;
                case "xt":
                    assertTrue(substring.getPositions().contains(2));
                    break;
                default:
                    fail();
            }
        }
    }

}