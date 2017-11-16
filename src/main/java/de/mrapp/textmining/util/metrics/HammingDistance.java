/*
 * Copyright 2017 Michael Rapp
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
package de.mrapp.textmining.util.metrics;

import org.jetbrains.annotations.NotNull;

import static de.mrapp.util.Condition.ensureEqual;
import static de.mrapp.util.Condition.ensureNotNull;

/**
 * Allows to calculate the distance of texts according to the Hamming distance. The Hamming distance
 * is defined as the number of characters at corresponding positions that are not equal. It can only
 * be calculated for texts with the same length.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class HammingDistance implements TextMetric {

    @Override
    public final long evaluate(@NotNull final String text1, @NotNull final String text2) {
        ensureNotNull(text1, "The first text must not be null");
        ensureNotNull(text2, "The second text must not be null");
        ensureEqual(text1.length(), text2.length(), "The texts must have the same length");
        long distance = 0;

        for (int i = 0; i < text1.length(); i++) {
            if (text1.charAt(i) != text2.charAt(i)) {
                distance++;
            }
        }

        return distance;
    }

    @Override
    public final long minValue() {
        return 0;
    }

    @Override
    public final long maxValue() {
        return Long.MAX_VALUE;
    }

    @Override
    public final boolean isGainMetric() {
        return false;
    }

}