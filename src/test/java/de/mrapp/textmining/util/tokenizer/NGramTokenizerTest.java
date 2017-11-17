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
        assertEquals(position, nGram.getPosition());
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
        assertEquals(nGram.getPosition(), clone.getPosition());
    }

    @Test
    public final void testNGramToString() {
        String token = "token";
        int position = 1;
        NGram nGram = new NGram(token, position);
        assertEquals("NGram [token=" + token + ", position=" + position + "]",
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
        nGram1 = new NGram("token", 1);
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
        nGram1 = new NGram("token", 1);
        assertFalse(nGram1.equals(nGram2));
    }

    @Test
    public final void testDefaultConstructor() {
        NGramTokenizer nGramTokenizer = new NGramTokenizer();
        assertEquals(1, nGramTokenizer.getMinLength());
        assertEquals(Integer.MAX_VALUE, nGramTokenizer.getMaxLength());
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
        assertTrue(nGrams.contains(new NGram("w", 0)));
        assertTrue(nGrams.contains(new NGram("wi", 0)));
        assertTrue(nGrams.contains(new NGram("wir", 0)));
        assertTrue(nGrams.contains(new NGram("irk", 1)));
        assertTrue(nGrams.contains(new NGram("rk", 2)));
        assertTrue(nGrams.contains(new NGram("k", 3)));
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
        assertTrue(nGrams.contains(new NGram("wi", 0)));
        assertTrue(nGrams.contains(new NGram("wir", 0)));
        assertTrue(nGrams.contains(new NGram("irk", 1)));
        assertTrue(nGrams.contains(new NGram("rk", 2)));
    }

    @Test
    public final void testTokenizeWithMinAndMaxLength2() {
        Set<NGram> nGrams = new NGramTokenizer(1, 2).tokenize("wirk");
        assertEquals(5, nGrams.size());
        assertTrue(nGrams.contains(new NGram("w", 0)));
        assertTrue(nGrams.contains(new NGram("wi", 0)));
        assertTrue(nGrams.contains(new NGram("ir", 1)));
        assertTrue(nGrams.contains(new NGram("rk", 2)));
        assertTrue(nGrams.contains(new NGram("k", 3)));
    }

    @Test
    public final void testTokenizeWithMinAndMaxLength3() {
        Set<NGram> nGrams = new NGramTokenizer(2, 2).tokenize("wirk");
        assertEquals(3, nGrams.size());
        assertTrue(nGrams.contains(new NGram("wi", 0)));
        assertTrue(nGrams.contains(new NGram("ir", 1)));
        assertTrue(nGrams.contains(new NGram("rk", 2)));
    }

}