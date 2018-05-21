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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * Defines the interface, a class, which allows to parse text in order to convert it into data of
 * another type, must implement.
 *
 * @param <ResultType> The type of the data, the text should be converted to
 * @author Michael Rapp
 * @since 1.3.0
 */
public interface TextParser<ResultType> {

    /**
     * Sets a pre-processor, which is applied to a text before it is processed by the parser.
     *
     * @param preProcessor The pre-processor, which should be set, as an instance of the type {@link
     *                     Function} or null, if no pre-processor should be set
     */
    void setPreProcessor(@Nullable Function<String, String> preProcessor);

    /**
     * Sets a post-processor, which is applied to the converted data, after it has been processed by
     * the parser.
     *
     * @param postProcessor The post-processor, which should be set, as an instance of the type
     *                      {@link Function} or null, if no post-processor should be set
     */
    void setPostProcessor(@Nullable Function<ResultType, ResultType> postProcessor);

    /**
     * Parses a specific text in order to convert it into data of another type.
     *
     * @param text The text, which should be parsed, as a {@link String}. The text may neither be
     *             null, or empty
     * @return The data, the given text has been converted to, as an instance of the generic type
     * {@link ResultType}. The data may not be null
     * @throws MalformedTextException The exception, which is thrown, if the given text is
     *                                malformed
     */
    @NotNull
    ResultType parse(@NotNull String text) throws MalformedTextException;

}