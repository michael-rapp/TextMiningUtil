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
package de.mrapp.textmining.util.parser;

import de.mrapp.textmining.util.tokenizer.Substring;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the functionality of the class {@link ValueToken}.
 *
 * @author Michael Rapp
 */
public class ValueTokenTest {

    @Test
    public final void testConstructor() {
        Substring token = new Substring("foo", 0);
        token.addPosition(1);
        String value = "value";
        ValueToken<String> valueToken = new ValueToken<>(token, value);
        assertEquals(token.getToken(), valueToken.getToken());
        assertEquals(token.getPositions(), valueToken.getPositions());
        assertEquals(value, valueToken.getValue());
        assertEquals(AssociationType.NONE, valueToken.getAssociationType());
    }

    @Test
    public final void testSetValue() {
        Substring token = new Substring("foo", 0);
        ValueToken<String> valueToken = new ValueToken<>(token, "value");
        String value = "newValue";
        valueToken.setValue(value);
        assertEquals(value, valueToken.getValue());
    }

    @Test
    public final void testSetAssociationType() {
        Substring token = new Substring("foo", 0);
        ValueToken<String> valueToken = new ValueToken<>(token, "value");
        AssociationType associationType = AssociationType.RIGHT;
        valueToken.setAssociationType(associationType);
        assertEquals(associationType, valueToken.getAssociationType());
    }

    @Test
    public final void testClone() {
        Substring token = new Substring("foo", 0);
        ValueToken<String> valueToken = new ValueToken<>(token, "value");
        valueToken.setAssociationType(AssociationType.RIGHT);
        ValueToken<String> clone = valueToken.clone();
        assertEquals(clone, valueToken);
    }

    @Test
    public final void testToString() {
        Substring token = new Substring("foo", 0);
        ValueToken<String> valueToken = new ValueToken<>(token, "value");
        assertEquals("ValueToken{token=" + valueToken.getToken() + ", positions=" +
                        valueToken.getPositions() + ", value=" + valueToken.getValue() +
                        ", associationType=" + valueToken.getAssociationType() + "}",
                valueToken.toString());
    }

    @Test
    public final void testHashCode() {
        ValueToken<String> valueToken1 = new ValueToken<>(new Substring("token", 0), "value");
        ValueToken<String> valueToken2 = new ValueToken<>(new Substring("token", 0), "value");
        assertEquals(valueToken1.hashCode(), valueToken1.hashCode());
        assertEquals(valueToken1.hashCode(), valueToken2.hashCode());
        valueToken1 = new ValueToken<>(new Substring("token2", 0), "value");
        assertNotEquals(valueToken1.hashCode(), valueToken2.hashCode());
        valueToken1 = new ValueToken<>(new Substring("token", 1), "value");
        assertNotEquals(valueToken1.hashCode(), valueToken2.hashCode());
        valueToken1 = new ValueToken<>(new Substring("token", 0), "value2");
        assertNotEquals(valueToken1.hashCode(), valueToken2.hashCode());
        valueToken1 = new ValueToken<>(new Substring("token", 0), "value");
        valueToken1.setAssociationType(AssociationType.RIGHT);
        assertNotEquals(valueToken1.hashCode(), valueToken2.hashCode());
    }

    @Test
    public final void testEquals() {
        ValueToken<String> valueToken1 = new ValueToken<>(new Substring("token", 0), "value");
        ValueToken<String> valueToken2 = new ValueToken<>(new Substring("token", 0), "value");
        assertFalse(valueToken1.equals(null));
        assertFalse(valueToken1.equals(new Object()));
        assertTrue(valueToken1.equals(valueToken1));
        assertTrue(valueToken1.equals(valueToken2));
        valueToken1 = new ValueToken<>(new Substring("token2", 0), "value");
        assertFalse(valueToken1.equals(valueToken2));
        valueToken1 = new ValueToken<>(new Substring("token", 1), "value");
        assertFalse(valueToken1.equals(valueToken2));
        valueToken1 = new ValueToken<>(new Substring("token", 0), "value2");
        assertFalse(valueToken1.equals(valueToken2));
        valueToken1 = new ValueToken<>(new Substring("token", 0), "value");
        valueToken1.setAssociationType(AssociationType.RIGHT);
        assertFalse(valueToken1.equals(valueToken2));
    }

}