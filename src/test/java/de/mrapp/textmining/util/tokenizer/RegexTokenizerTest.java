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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Tests the functionality of the class {@link RegexTokenizer}.
 *
 * @since 1.2.0
 */
public class RegexTokenizerTest {

    @Test
    public final void testConstructorWithPatternArgument() {
        Pattern pattern = Pattern.compile("\\s+");
        RegexTokenizer regexTokenizer = new RegexTokenizer(pattern);
        assertEquals(regexTokenizer.getPattern(), pattern);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testConstructorWithPatternArgumentThrowsException() {
        new RegexTokenizer((Pattern) null);
    }

    @Test
    public final void testConstructorWithStringArgument() {
        String regex = "\\s+";
        RegexTokenizer regexTokenizer = new RegexTokenizer(regex);
        assertEquals(regexTokenizer.getPattern().toString(), regex);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testConstructorWithStringArgumentThrowsExceptionIfStringIsNull() {
        new RegexTokenizer((String) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testConstructorWithStringArgumentThrowsExceptionIfStringIsEmpty() {
        new RegexTokenizer("");
    }

    @Test
    public final void testTokenize1() {
        Set<Substring> substrings = new RegexTokenizer(",").tokenize("foo");
        assertEquals(1, substrings.size());

        for (Substring substring : substrings) {
            switch (substring.getToken()) {
                case "foo":
                    assertTrue(substring.getPositions().contains(0));
                    break;
                default:
                    fail();
            }
        }
    }

    @Test
    public final void testTokenize2() {
        Set<Substring> substrings = new RegexTokenizer(",").tokenize("foo,bar");
        assertEquals(2, substrings.size());

        for (Substring substring : substrings) {
            switch (substring.getToken()) {
                case "foo":
                    assertTrue(substring.getPositions().contains(0));
                    break;
                case "bar":
                    assertTrue(substring.getPositions().contains(4));
                    break;
                default:
                    fail();
            }
        }
    }

    @Test
    public final void testTokenize3() {
        Set<Substring> substrings = new RegexTokenizer(",").tokenize("foo,bar,foobar");
        assertEquals(3, substrings.size());

        for (Substring substring : substrings) {
            switch (substring.getToken()) {
                case "foo":
                    assertTrue(substring.getPositions().contains(0));
                    break;
                case "bar":
                    assertTrue(substring.getPositions().contains(4));
                    break;
                case "foobar":
                    assertTrue(substring.getPositions().contains(8));
                    break;
                default:
                    fail();
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testTokenizeThrowsExceptionIfTextIsNull() {
        new RegexTokenizer(",").tokenize(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testTokenizeThrowsExceptionIfTextIsEmpty() {
        new RegexTokenizer(",").tokenize(null);
    }

    @Test
    public final void testSplitByWhitespace() {
        RegexTokenizer regexTokenizer = RegexTokenizer.splitByWhitespace();
        assertEquals(regexTokenizer.getPattern().toString(), "\\s+");
        Set<Substring> substrings = regexTokenizer.tokenize("foo  bar\nfoobar");
        assertEquals(3, substrings.size());

        for (Substring substring : substrings) {
            switch (substring.getToken()) {
                case "foo":
                    assertTrue(substring.getPositions().contains(0));
                    break;
                case "bar":
                    assertTrue(substring.getPositions().contains(5));
                    break;
                case "foobar":
                    assertTrue(substring.getPositions().contains(9));
                    break;
                default:
                    fail();
            }
        }
    }

    @Test
    public final void testSplitByDelimitersWithArrayArgument() {
        RegexTokenizer regexTokenizer = RegexTokenizer.splitByDelimiters(",", ";");
        Set<Substring> substrings = regexTokenizer
                .tokenize("foo,bar;foobar");
        assertEquals(regexTokenizer.getPattern().toString(), ",|;");
        assertEquals(3, substrings.size());

        for (Substring substring : substrings) {
            switch (substring.getToken()) {
                case "foo":
                    assertTrue(substring.getPositions().contains(0));
                    break;
                case "bar":
                    assertTrue(substring.getPositions().contains(4));
                    break;
                case "foobar":
                    assertTrue(substring.getPositions().contains(8));
                    break;
                default:
                    fail();
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testSplitByDelimitersWithArrayArgumentThrowsExceptionIfArrayIsNull() {
        RegexTokenizer.splitByDelimiters((String[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testSplitByDelimitersWithArrayArgumentThrowsExceptionIfArrayIsEmpty() {
        RegexTokenizer.splitByDelimiters(new String[0]);
    }

    @Test
    public final void testSplitByDelimitersWithIterableArgument() {
        List<String> iterable = new LinkedList<>();
        iterable.add(",");
        iterable.add(";");
        RegexTokenizer regexTokenizer = RegexTokenizer.splitByDelimiters(iterable);
        Set<Substring> substrings = regexTokenizer
                .tokenize("foo,bar;foobar");
        assertEquals(regexTokenizer.getPattern().toString(), ",|;");
        assertEquals(3, substrings.size());

        for (Substring substring : substrings) {
            switch (substring.getToken()) {
                case "foo":
                    assertTrue(substring.getPositions().contains(0));
                    break;
                case "bar":
                    assertTrue(substring.getPositions().contains(4));
                    break;
                case "foobar":
                    assertTrue(substring.getPositions().contains(8));
                    break;
                default:
                    fail();
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testSplitByDelimitersWithIterableArgumentThrowsExceptionIfIterableIsNull() {
        RegexTokenizer.splitByDelimiters((Iterable) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testSplitByDelimitersWithIterableArgumentThrowsExceptionIfIterableIsEmpty() {
        RegexTokenizer.splitByDelimiters(Collections.emptyList());
    }

}
