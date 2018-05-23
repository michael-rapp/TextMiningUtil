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
package de.mrapp.textmining.util;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Set;

/**
 * Defines the interface, a token must implement.
 *
 * @author Michael Rapp
 * @since 1.2.0
 */
public interface Token extends Serializable, Cloneable {

    /**
     * Returns the token.
     *
     * @return The token as a {@link String}. The token may neither be null, nor empty
     */
    @NotNull
    String getToken();

    /**
     * Returns the length of the token.
     *
     * @return The length of the token as an {@link Integer} value
     */
    default int length() {
        return getToken().length();
    }

    /**
     * Returns the position(s) of the token in the original text.
     *
     * @return A set, which contains the position(s) of the token, as an instance of the type {@link
     * Set}
     */
    @NotNull
    Set<Integer> getPositions();

}