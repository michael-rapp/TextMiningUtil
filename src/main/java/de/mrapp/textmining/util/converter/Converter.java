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
package de.mrapp.textmining.util.converter;

import org.jetbrains.annotations.NotNull;

/**
 * Defines the interface, a class, which allows to convert text into data of another type, must
 * implement.
 *
 * @param <ResultType> The type of the data, the text should be converted to
 * @author Michael Rapp
 * @since 1.3.0
 */
public interface Converter<ResultType> {

    /**
     * Converts a specific text into data of another type.
     *
     * @param text The text, which should be converted, as a {@link String}. The text may neither be
     *             null, or empty
     * @return The data, the given text has been converted to, as an instance of the generic type
     * {@link ResultType}. The data may not be null
     */
    @NotNull
    ResultType convert(@NotNull String text);

}