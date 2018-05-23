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
package de.mrapp.textmining.util.parser.number;

import de.mrapp.textmining.util.parser.MalformedTextException;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * Tests the functionality of the class {@link EnNumberParser}.
 *
 * @author Michael Rapp
 */
public class EnNumberParserTest {

    @Test
    public final void testParse() throws MalformedTextException {
        EnNumberParser numberParser = new EnNumberParser();
        assertEquals(numberParser.parse("zero"), new Integer(0));
        assertEquals(numberParser.parse("null"), new Integer(0));
        assertEquals(numberParser.parse("nil"), new Integer(0));
        assertEquals(numberParser.parse("one"), new Integer(1));
        assertEquals(numberParser.parse("two"), new Integer(2));
        assertEquals(numberParser.parse("three"), new Integer(3));
        assertEquals(numberParser.parse("four"), new Integer(4));
        assertEquals(numberParser.parse("five"), new Integer(5));
        assertEquals(numberParser.parse("six"), new Integer(6));
        assertEquals(numberParser.parse("seven"), new Integer(7));
        assertEquals(numberParser.parse("eight"), new Integer(8));
        assertEquals(numberParser.parse("nine"), new Integer(9));

        assertEquals(numberParser.parse("ten"), new Integer(10));
        assertEquals(numberParser.parse("eleven"), new Integer(11));
        assertEquals(numberParser.parse("twelve"), new Integer(12));
        assertEquals(numberParser.parse("thirteen"), new Integer(13));
        assertEquals(numberParser.parse("four-teen"), new Integer(14));
        assertEquals(numberParser.parse("fifteen"), new Integer(15));
        assertEquals(numberParser.parse("six-teen"), new Integer(16));
        assertEquals(numberParser.parse("seven-teen"), new Integer(17));
        assertEquals(numberParser.parse("eighteen"), new Integer(18));
        assertEquals(numberParser.parse("nine-teen"), new Integer(19));

        assertEquals(numberParser.parse("twenty"), new Integer(20));
        assertEquals(numberParser.parse("twenty-one"), new Integer(21));
        assertEquals(numberParser.parse("twenty-two"), new Integer(22));
        assertEquals(numberParser.parse("twenty-three"), new Integer(23));
        assertEquals(numberParser.parse("twenty-four"), new Integer(24));
        assertEquals(numberParser.parse("twenty-five"), new Integer(25));
        assertEquals(numberParser.parse("twenty-six"), new Integer(26));
        assertEquals(numberParser.parse("twenty-seven"), new Integer(27));
        assertEquals(numberParser.parse("twenty-eight"), new Integer(28));
        assertEquals(numberParser.parse("twenty-nine"), new Integer(29));

        assertEquals(numberParser.parse("thirty"), new Integer(30));
        assertEquals(numberParser.parse("thirty-one"), new Integer(31));
        assertEquals(numberParser.parse("thirty-two"), new Integer(32));
        assertEquals(numberParser.parse("thirty-three"), new Integer(33));
        assertEquals(numberParser.parse("thirty-four"), new Integer(34));
        assertEquals(numberParser.parse("thirty-five"), new Integer(35));
        assertEquals(numberParser.parse("thirty-six"), new Integer(36));
        assertEquals(numberParser.parse("thirty-seven"), new Integer(37));
        assertEquals(numberParser.parse("thirty-eight"), new Integer(38));
        assertEquals(numberParser.parse("thirty-nine"), new Integer(39));
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testParseThrowsExceptionIfTextIsNull() throws MalformedTextException {
        new EnNumberParser().parse(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testParseThrowsExceptionIfTextIsEmpty() throws MalformedTextException {
        new EnNumberParser().parse("");
    }

    @Test
    public final void testLocale() {
        assertEquals(new EnNumberParser().getLocale(), Locale.ENGLISH);
    }

}
