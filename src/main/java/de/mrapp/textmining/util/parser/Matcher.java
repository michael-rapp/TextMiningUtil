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

import de.mrapp.textmining.util.Token;
import de.mrapp.textmining.util.metrics.TextMetric;
import org.jetbrains.annotations.NotNull;

import static de.mrapp.util.Condition.*;

/**
 * Defines the interface, a class, which allows to check, whether is applies to certain tokens, or
 * not, must implement.
 *
 * @param <TokenType> The type of the tokens, the matcher may apply to
 * @author Michael Rapp
 * @since 1.3.0
 */
@FunctionalInterface
public interface Matcher<TokenType extends Token> {

    /**
     * Returns, whether the matcher applies to a specific token, or not.
     *
     * @param token The token, which should be checked, as an instance of the generic type {@link
     *              TokenType}. The token may not be null
     * @param text  The text, the token should be compared to, as an instance of the type {@link
     *              CharSequence}. The text may not be null
     * @return True, if the matcher applies to the given token, false otherwise
     */
    boolean matches(@NotNull TokenType token, @NotNull CharSequence text);

    /**
     * Creates and returns a matcher, which allows to check whether tokens are equal to certain
     * texts.
     *
     * @param <T> The type of the tokens, which should be checked
     * @return The matcher, which has been created, as an instance of the generic type {@link T}.
     * The matcher may not be null
     */
    @NotNull
    static <T extends Token> Matcher<T> equals() {
        return (token, text) -> token.getToken().equals(text.toString());
    }

    /**
     * Creates and returns a matcher, which allows to check, whether tokens start with certain
     * texts.
     *
     * @param <T> The type of the tokens, which should be checked
     * @return The matcher, which has been created, as an instance of the generic type {@link T}.
     * The matcher may not be null
     */
    @NotNull
    static <T extends Token> Matcher<T> startsWith() {
        return (token, text) -> token.getToken().startsWith(text.toString());
    }

    /**
     * Creates and returns a matcher, which allows to check, whether tokens start with certain
     * texts.
     *
     * @param <T> The type of the tokens, which should be checked
     * @return The matcher, which has been created, as an instance of the generic type {@link T}.
     * The matcher may not be null
     */
    @NotNull
    static <T extends Token> Matcher<T> endsWith() {
        return (token, text) -> token.getToken().endsWith(text.toString());
    }

    /**
     * Creates and returns a matcher, which allows to check, whether tokens contain certain texts.
     *
     * @param <T> The type of the tokens, which should be checked
     * @return The matcher, which has been created, as an instance of the generic type {@link T}.
     * The matcher may not be null
     */
    @NotNull
    static <T extends Token> Matcher<T> contains() {
        return (token, text) -> token.getToken().contains(text.toString());
    }

    /**
     * Creates and returns a matcher, which allows to check tokens using a text metric.
     *
     * @param <T>       The type fo the tokens, which should be checked
     * @param metric    The text metric, which should be used, as an instance of the type {@link
     *                  TextMetric}. The metric may not be null
     * @param threshold The threshold, which specifies the difference, which is acceptable for two
     *                  texts to be considered equal, as a {@link Double} value
     * @return The matcher, which has been created, as an instance of the generic type {@link T}.
     * The matcher may not be null
     */
    @NotNull
    static <T extends Token> Matcher<T> usingMetric(@NotNull TextMetric metric, double threshold) {
        ensureNotNull(metric, "The metric may not be null");
        ensureAtLeast(threshold, metric.minValue(),
                "The threshold must be at least " + metric.minValue());
        ensureAtMaximum(threshold, metric.maxValue(),
                "The threshold must be at maximum " + metric.maxValue());
        return (token, text) -> {
            double heuristicValue = metric.evaluate(token.getToken(), text.toString());
            return metric.isGainMetric() ? heuristicValue >= threshold :
                    heuristicValue <= threshold;
        };
    }

}