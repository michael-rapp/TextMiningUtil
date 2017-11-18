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

import de.mrapp.textmining.util.tokenizer.NGramTokenizer.NGram;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * Tests the functionality of the class {@link NGramTokenizer}.
 *
 * @since 1.0.0
 */
public class NGramTokenizerTest {

    @Test
    public final void testNGramConstructor() {
        String token = "token";
        int position = 1;
        NGram nGram = new NGram(token, position);
        assertEquals(token, nGram.getToken());
        assertEquals(1, nGram.getPositions().size());
        assertTrue(nGram.getPositions().contains(position));
        assertEquals(token.length(), nGram.length());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testNGramConstructorThrowsExceptionIfTokenIsNull() {
        new NGram(null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testNGramConstructorThrowsExceptionIfTokenIsEmpty() {
        new NGram("", 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testConstructorThrowsExceptionIfPositionIsLessThanZero() {
        new NGram("token", -1);
    }

    @Test
    public final void testNGramClone() {
        NGram nGram = new NGram("token", 1);
        NGram clone = nGram.clone();
        assertEquals(nGram.getToken(), clone.getToken());
        assertEquals(nGram.getPositions().size(), clone.getPositions().size());
        assertTrue(clone.getPositions().contains(nGram.getPositions().iterator().next()));
    }

    @Test
    public final void testNGramToString() {
        String token = "token";
        int position = 1;
        NGram nGram = new NGram(token, position);
        assertEquals("NGram [token=" + token + ", positions=[" + position + "]]",
                nGram.toString());
    }

    @Test
    public final void testNGramHashCode() {
        NGram nGram1 = new NGram("token", 0);
        NGram nGram2 = new NGram("token", 0);
        assertEquals(nGram1.hashCode(), nGram1.hashCode());
        assertEquals(nGram1.hashCode(), nGram2.hashCode());
        nGram1 = new NGram("foo", 0);
        assertNotEquals(nGram1.hashCode(), nGram2.hashCode());
    }

    @Test
    public final void testNGramEquals() {
        NGram nGram1 = new NGram("token", 0);
        NGram nGram2 = new NGram("token", 0);
        assertFalse(nGram1.equals(null));
        assertFalse(nGram1.equals(new Object()));
        assertTrue(nGram1.equals(nGram1));
        assertTrue(nGram1.equals(nGram2));
        nGram1 = new NGram("foo", 0);
        assertFalse(nGram1.equals(nGram2));
    }

    @Test
    public final void testDefaultConstructor() {
        NGramTokenizer nGramTokenizer = new NGramTokenizer();
        assertEquals(1, nGramTokenizer.getMinLength());
        assertEquals(Integer.MAX_VALUE, nGramTokenizer.getMaxLength());
    }

    @Test
    public final void testConstructorWithMaxLengthParameter() {
        int maxLength = 3;
        NGramTokenizer nGramTokenizer = new NGramTokenizer(maxLength);
        assertEquals(1, nGramTokenizer.getMinLength());
        assertEquals(maxLength, nGramTokenizer.getMaxLength());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testTokenizeWithMaxLengthParameterThrowsExceptionIfMaxLengthIsLessThanOne() {
        new NGramTokenizer(0);
    }

    @Test
    public final void testConstructorWithMinAndMaxLengthParameters() {
        int minLength = 2;
        int maxLength = 3;
        NGramTokenizer nGramTokenizer = new NGramTokenizer(minLength, maxLength);
        assertEquals(minLength, nGramTokenizer.getMinLength());
        assertEquals(maxLength, nGramTokenizer.getMaxLength());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testConstructorWithMinAndMaxLengthParametersThrowsExceptionIfMinLengthIsLessThanOne() {
        new NGramTokenizer(0, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testTokenizeWithMinAndMaxLengthParametersThrowsExceptionIfMaxLengthIsLessThanMinLength() {
        new NGramTokenizer(2, 1);
    }

    @Test
    public final void testTokenize() {
        Set<NGram> nGrams = new NGramTokenizer().tokenize("wirk");
        assertEquals(6, nGrams.size());

        for (NGram nGram : nGrams) {
            switch (nGram.getToken()) {
                case "w":
                    assertTrue(nGram.getPositions().contains(0));
                    break;
                case "wi":
                    assertTrue(nGram.getPositions().contains(0));
                    break;
                case "wir":
                    assertTrue(nGram.getPositions().contains(0));
                    break;
                case "irk":
                    assertTrue(nGram.getPositions().contains(1));
                    break;
                case "rk":
                    assertTrue(nGram.getPositions().contains(2));
                    break;
                case "k":
                    assertTrue(nGram.getPositions().contains(3));
                    break;
                default:
                    break;
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testTokenizeThrowsExceptionIfTextIsNull() {
        new NGramTokenizer().tokenize(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testTokenizeThrowsExceptionIfTextIsEmpty() {
        new NGramTokenizer().tokenize("");
    }

    @Test
    public final void testTokenizeWithMinAndMaxLength1() {
        Set<NGram> nGrams = new NGramTokenizer(2, 3).tokenize("wirk");
        assertEquals(4, nGrams.size());

        for (NGram nGram : nGrams) {
            switch (nGram.getToken()) {
                case "wi":
                    assertTrue(nGram.getPositions().contains(0));
                    break;
                case "wir":
                    assertTrue(nGram.getPositions().contains(0));
                    break;
                case "irk":
                    assertTrue(nGram.getPositions().contains(1));
                    break;
                case "rk":
                    assertTrue(nGram.getPositions().contains(2));
                    break;
                default:
                    break;
            }
        }
    }

    @Test
    public final void testTokenizeWithMinAndMaxLength2() {
        Set<NGram> nGrams = new NGramTokenizer(1, 2).tokenize("wirk");
        assertEquals(5, nGrams.size());

        for (NGram nGram : nGrams) {
            switch (nGram.getToken()) {
                case "w":
                    assertTrue(nGram.getPositions().contains(0));
                    break;
                case "wi":
                    assertTrue(nGram.getPositions().contains(0));
                    break;
                case "ir":
                    assertTrue(nGram.getPositions().contains(1));
                    break;
                case "rk":
                    assertTrue(nGram.getPositions().contains(2));
                    break;
                case "k":
                    assertTrue(nGram.getPositions().contains(3));
                    break;
                default:
                    break;
            }
        }
    }

    @Test
    public final void testTokenizeWithMinAndMaxLength3() {
        Set<NGram> nGrams = new NGramTokenizer(2, 2).tokenize("wirk");
        assertEquals(3, nGrams.size());

        for (NGram nGram : nGrams) {
            switch (nGram.getToken()) {
                case "wi":
                    assertTrue(nGram.getPositions().contains(0));
                    break;
                case "ir":
                    assertTrue(nGram.getPositions().contains(1));
                    break;
                case "rk":
                    assertTrue(nGram.getPositions().contains(2));
                    break;
                default:
                    break;
            }
        }
    }

    @Test
    public final void testTokenizeWithMinAndMaxLength4() {
        Set<NGram> nGrams = new NGramTokenizer(1, 1).tokenize("text");
        assertEquals(3, nGrams.size());

        for (NGram nGram : nGrams) {
            switch (nGram.getToken()) {
                case "t":
                    assertTrue(nGram.getPositions().contains(0));
                    assertTrue(nGram.getPositions().contains(3));
                    break;
                case "e":
                    assertTrue(nGram.getPositions().contains(1));
                    break;
                case "x":
                    assertTrue(nGram.getPositions().contains(2));
                    break;
                default:
                    break;
            }
        }
    }

}