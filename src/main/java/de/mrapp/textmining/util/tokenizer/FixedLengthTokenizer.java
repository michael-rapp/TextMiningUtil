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

import java.util.Map;

import static de.mrapp.util.Condition.ensureAtLeast;
import static de.mrapp.util.Condition.ensureTrue;

/**
 * Allows to split texts into substrings with a fixed length. E.g. given the length 2, the text
 * "text" is split into the substrings "te", "xt".
 *
 * @author Michael Rapp
 * @since 1.2.0
 */
public class FixedLengthTokenizer extends AbstractTokenizer<Substring> {

    /**
     * The length of the substrings, which are created by the tokenizer.
     */
    private final int length;


    /**
     * Creates a new tokenizer, which splits texts into substrings with a fixed length.
     *
     * @param length The length of the substrings, which should be created by the tokenizer, as an
     *               {@link Integer} value. The length must be at least 1
     */
    public FixedLengthTokenizer(final int length) {
        ensureAtLeast(length, 1, "The length must be at least 1");
        this.length = length;
    }

    /**
     * Returns the length of the substrings, which are created by the tokenizer.
     *
     * @return The length of the substrings, which are created by the tokenizer, as an {@link
     * Integer} value
     */
    public final int getLength() {
        return length;
    }

    @Override
    protected final void onTokenize(@NotNull final String text,
                                    @NotNull final Map<String, Substring> tokens) {
        int length = text.length();
        ensureTrue(length % this.length == 0,
                "The length of the text must be dividable by " + this.length);

        for (int i = 0; i <= length - this.length; i += this.length) {
            String token = text.substring(i, i + this.length);
            addToken(tokens, token, i, Substring::new);
        }
    }

}
