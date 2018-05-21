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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests the functionality of the class {@link MalformedTextException}.
 *
 * @author Michael Rapp
 */
public class MalformedTextExceptionTest {

    @Test
    public final void testConstructorWithMessageArgument() {
        String text = "text";
        String message = "message";
        MalformedTextException malformedTextException = new MalformedTextException(text, message);
        assertEquals(text, malformedTextException.getText());
        assertEquals(message, malformedTextException.getMessage());
        assertNull(malformedTextException.getCause());
    }

    @Test
    public final void testConstructorWithMessageAndThrowableArgument() {
        String text = "text";
        String message = "message";
        Throwable cause = new Exception();
        MalformedTextException malformedTextException = new MalformedTextException(text, message,
                cause);
        assertEquals(text, malformedTextException.getText());
        assertEquals(message, malformedTextException.getMessage());
        assertEquals(cause, malformedTextException.getCause());
    }

}
