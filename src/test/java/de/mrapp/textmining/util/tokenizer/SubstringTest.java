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
package de.mrapp.textmining.util.tokenizer;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the functionality of the class {@link Substring}.
 *
 * @author Michael Rapp
 */
public class SubstringTest {

    @Test
    public final void testConstructor() {
        String token = "token";
        int position = 1;
        Substring substring = new Substring(token, position);
        assertEquals(token, substring.getToken());
        assertEquals(1, substring.getPositions().size());
        assertTrue(substring.getPositions().contains(position));
        assertEquals(token.length(), substring.length());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testConstructorThrowsExceptionIfTokenIsNull() {
        new Substring(null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testConstructorThrowsExceptionIfTokenIsEmpty() {
        new Substring("", 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testConstructorThrowsExceptionIfPositionIsLessThanZero() {
        new Substring("token", -1);
    }

    @Test
    public final void testClone() {
        Substring substring = new Substring("token", 1);
        Substring clone = substring.clone();
        assertEquals(substring.getToken(), clone.getToken());
        assertEquals(substring.getPositions().size(), clone.getPositions().size());
        assertTrue(clone.getPositions().contains(substring.getPositions().iterator().next()));
    }

    @Test
    public final void testToString() {
        String token = "token";
        int position = 1;
        Substring substring = new Substring(token, position);
        assertEquals("Substring [token=" + token + ", positions=[" + position + "]]",
                substring.toString());
    }

    @Test
    public final void testHashCode() {
        Substring substring1 = new Substring("token", 0);
        Substring substring2 = new Substring("token", 0);
        assertEquals(substring1.hashCode(), substring1.hashCode());
        assertEquals(substring1.hashCode(), substring2.hashCode());
        substring1 = new Substring("foo", 0);
        assertNotEquals(substring1.hashCode(), substring2.hashCode());
        substring1 = new Substring("token", 1);
        assertNotEquals(substring1.hashCode(), substring2.hashCode());
    }

    @Test
    public final void testEquals() {
        Substring substring1 = new Substring("token", 0);
        Substring substring2 = new Substring("token", 0);
        assertFalse(substring1.equals(null));
        assertFalse(substring1.equals(new Object()));
        assertTrue(substring1.equals(substring1));
        assertTrue(substring1.equals(substring2));
        substring1 = new Substring("foo", 0);
        assertFalse(substring1.equals(substring2));
        substring1 = new Substring("token", 1);
        assertNotEquals(substring1.hashCode(), substring2.hashCode());
        assertFalse(substring1.equals(substring2));
    }

}
