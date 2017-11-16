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
package de.mrapp.textmining.util.ngram;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests the functionality of the class {@link NGramTokenizer}.
 *
 * @since 1.0.0
 */
public class NGramTokenizerTest {

    @Test
    public final void testTokenize() {
        Set<NGram> nGrams = new NGramTokenizer().tokenize("text");
        assertEquals(10, nGrams.size());
        assertTrue(nGrams.contains(new NGram("t", 0)));
        assertTrue(nGrams.contains(new NGram("e", 1)));
        assertTrue(nGrams.contains(new NGram("x", 2)));
        assertTrue(nGrams.contains(new NGram("t", 3)));
        assertTrue(nGrams.contains(new NGram("te", 0)));
        assertTrue(nGrams.contains(new NGram("ex", 1)));
        assertTrue(nGrams.contains(new NGram("xt", 2)));
        assertTrue(nGrams.contains(new NGram("tex", 0)));
        assertTrue(nGrams.contains(new NGram("ext", 1)));
        assertTrue(nGrams.contains(new NGram("text", 0)));
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testTokenizeThrowsExceptionIfTextIsNull() {
        new NGramTokenizer().tokenize(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testTokenizeThrowsExceptionIfTextIsEmpty() {
        new NGramTokenizer().tokenize("");
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testTokenizeWithMinAndMaxLengthParametersThrowsExceptionIfTextIsNull() {
        new NGramTokenizer().tokenize(null, 1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testTokenizeWithMinAndMaxLengthParametersThrowsExceptionIfTextIsEmpty() {
        new NGramTokenizer().tokenize("", 1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testTokenizeWithMinAndMaxLengthParametersThrowsExceptionIfMinLengthIsLessThanOne() {
        new NGramTokenizer().tokenize("text", 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testTokenizeWithMinAndMaxLengthParametersThrowsExceptionIfMaxLengthIsGreaterThanTextLength() {
        new NGramTokenizer().tokenize("text", 1, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testTokenizeWithMinAndMaxLengthParametersThrowsExceptionIfMinLengthIsGreaterThanMaxLength() {
        new NGramTokenizer().tokenize("text", 2, 1);
    }

    @Test
    public final void testTokenizeWithMinAndMaxLengthParameters1() {
        Set<NGram> nGrams = new NGramTokenizer().tokenize("text", 2, 3);
        assertEquals(5, nGrams.size());
        assertTrue(nGrams.contains(new NGram("te", 0)));
        assertTrue(nGrams.contains(new NGram("ex", 1)));
        assertTrue(nGrams.contains(new NGram("xt", 2)));
        assertTrue(nGrams.contains(new NGram("tex", 0)));
        assertTrue(nGrams.contains(new NGram("ext", 1)));
    }

    @Test
    public final void testTokenizeWithMinAndMaxLengthParameters2() {
        Set<NGram> nGrams = new NGramTokenizer().tokenize("text", 2, 2);
        assertEquals(3, nGrams.size());
        assertTrue(nGrams.contains(new NGram("te", 0)));
        assertTrue(nGrams.contains(new NGram("ex", 1)));
        assertTrue(nGrams.contains(new NGram("xt", 2)));
    }

}