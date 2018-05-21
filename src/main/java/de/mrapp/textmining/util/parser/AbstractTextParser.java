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

import static de.mrapp.util.Condition.ensureNotEmpty;
import static de.mrapp.util.Condition.ensureNotNull;

/**
 * An abstract base class for all parsers, that allow to parse text in order to convert it into data
 * of another type.
 *
 * @param <ResultType> The type of the data, the text should be converted to
 * @author Michael Rapp
 * @since 1.3.0
 */
public abstract class AbstractTextParser<ResultType> implements TextParser<ResultType> {

    private Function<String, String> preProcessor;

    private Function<ResultType, ResultType> postProcessor;

    private String preProcess(@NotNull final String text) {
        return preProcessor != null ? preProcessor.apply(text) : text;
    }

    private ResultType postProcess(@NotNull final ResultType result) {
        return postProcessor != null ? postProcessor.apply(result) : result;
    }

    /**
     * The method, which is invoked on implementing subclasses in order to parse a specific
     * (pre-processed) text.
     *
     * @param text The text, which should be parsed, as a {@link String}. The text may neither be
     *             null, nor empty The data, the given text has been converted to, as an instance of
     *             the generic type {@link ResultType}. The data may not be null
     * @throws MalformedTextException The exception, which is thrown, if the given text is
     *                                malformed
     */
    protected abstract ResultType onParse(@NotNull final String text) throws MalformedTextException;

    @Override
    public final void setPreProcessor(@Nullable final Function<String, String> preProcessor) {
        this.preProcessor = preProcessor;
    }

    @Override
    public final void setPostProcessor(
            @Nullable final Function<ResultType, ResultType> postProcessor) {
        this.postProcessor = postProcessor;
    }

    @NotNull
    @Override
    public final ResultType parse(@NotNull final String text) throws MalformedTextException {
        ensureNotNull(text, "The text may not be null");
        ensureNotEmpty(text, "The text may not be empty");
        String preProcessedText = preProcess(text);
        ResultType result = onParse(preProcessedText);
        return postProcess(result);
    }

}