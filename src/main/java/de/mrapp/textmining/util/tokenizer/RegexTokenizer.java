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

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static de.mrapp.util.Condition.*;

/**
 * Allows to split texts into substrings using regular expressions.
 *
 * @author Michael Rapp
 * @since 1.2.0
 */
public class RegexTokenizer extends AbstractTokenizer<Substring> {

    /**
     * The delimiters, which are used to split texts.
     */
    private final Pattern pattern;

    /**
     * Creates a new tokenizer, which allows to split texts into substrings using a specific regular
     * expression.
     *
     * @param pattern The regular expression, which should be used to split texts, as a {@link
     *                String}. The regular expression may neither be null, nor empty
     */
    public RegexTokenizer(@NotNull final String pattern) {
        ensureNotNull(pattern, "The regular expression may not be null");
        ensureNotEmpty(pattern, "The regular expression may not be empty");
        this.pattern = Pattern.compile(pattern);
    }

    /**
     * Creates a new tokenizer, which allows to split texts into substrings using a specific regular
     * expression.
     *
     * @param pattern The regular expression, which should be used to split texts, as an instance of
     *                the class {@link Pattern}. The regular expression may not be null
     */
    public RegexTokenizer(@NotNull final Pattern pattern) {
        ensureNotNull(pattern, "The regular expression may not be null");
        this.pattern = pattern;
    }

    /**
     * Creates a new tokenizer, which allows to split texts at whitespaces.
     *
     * @return The tokenizer, which has been created, as an instance of the class {@link
     * RegexTokenizer}. The tokenizer may not be null
     */
    @NotNull
    public static RegexTokenizer splitByWhitespace() {
        return new RegexTokenizer("\\s+");
    }

    /**
     * Creates a new tokenizer, which allows to split texts at specific delimiters.
     *
     * @param delimiters The delimiters, the texts should be split at, as a {@link String} array.
     *                   The array may neither be null, nor empty
     * @return The tokenizer, which has been created, as an instance of the class {@link
     * RegexTokenizer}. The tokenizer may not be null
     */
    @NotNull
    public static RegexTokenizer splitByDelimiters(@NotNull final String... delimiters) {
        ensureNotNull(delimiters, "The delimiters may not be null");
        return splitByDelimiters(Arrays.asList(delimiters));
    }

    /**
     * Creates a new tokenizer, which allows to split texts at specific delimiters.
     *
     * @param delimiters The delimiters, the texts should be split at, as an instance of the type
     *                   {@link Iterable}. The iterable may neither be null, nor empty
     * @return The tokenizer, which has been created, as an instance of the class {@link
     * RegexTokenizer}. The tokenizer may not be null
     */
    @NotNull
    public static RegexTokenizer splitByDelimiters(
            @NotNull final Iterable<? extends CharSequence> delimiters) {
        ensureTrue(delimiters.iterator().hasNext(), "At least one delimiter must be given");
        StringBuilder stringBuilder = new StringBuilder();

        for (CharSequence delimiter : delimiters) {
            ensureNotNull(delimiter, "Delimiters may not be null");
            ensureNotEmpty(delimiter, "Delimiters may not be empty");

            if (stringBuilder.length() > 0) {
                stringBuilder.append("|");
            }

            stringBuilder.append(delimiter);
        }

        return new RegexTokenizer(stringBuilder.toString());
    }

    /**
     * Returns the regular expression, which is used to split texts.
     *
     * @return The regular expression, which is used to split texts, as an instance of the class
     * {@link Pattern}. The regular expression may not be nul
     */
    @NotNull
    public final Pattern getPattern() {
        return pattern;
    }

    @Override
    protected final void onTokenize(@NotNull final String text,
                                    @NotNull final Map<String, Substring> tokens) {
        int i = 0;
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String token = text.substring(i, matcher.start());
            addToken(tokens, token, i, Substring::new);
            i = matcher.end();
        }

        String token = text.substring(i, text.length());
        addToken(tokens, token, i, Substring::new);
    }

}
