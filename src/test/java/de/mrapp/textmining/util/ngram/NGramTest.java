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

import static org.junit.Assert.*;

/**
 * Tests the functionality of the class {@link NGram}.
 *
 * @author Michael Rapp
 */
public class NGramTest {

    @Test
    public final void testConstructor() {
        String token = "token";
        int position = 1;
        NGram nGram = new NGram(token, position);
        assertEquals(token, nGram.getToken());
        assertEquals(position, nGram.getPosition());
        assertEquals(token.length(), nGram.length());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testConstructorThrowsExceptionIfTokenIsNull() {
        new NGram(null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testConstructorThrowsExceptionIfTokenIsEmpty() {
        new NGram("", 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testConstructorThrowsExceptionIfPositionIsLessThanZero() {
        new NGram("token", -1);
    }

    @Test
    public final void testClone() {
        NGram nGram = new NGram("token", 1);
        NGram clone = nGram.clone();
        assertEquals(nGram.getToken(), clone.getToken());
        assertEquals(nGram.getPosition(), clone.getPosition());
    }

    @Test
    public final void testToString() {
        String token = "token";
        int position = 1;
        NGram nGram = new NGram(token, position);
        assertEquals("NGram [token=" + token + ", position=" + position + "]", nGram.toString());
    }

    @Test
    public final void testHashCode() {
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
    public final void testEquals() {
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

}