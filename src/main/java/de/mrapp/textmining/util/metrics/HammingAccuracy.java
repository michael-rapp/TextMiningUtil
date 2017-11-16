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

/**
 * Hamming accuracy is the gain metric pendant to the {@link HammingDistance}. It measures the
 * percentage of characters at corresponding positions that are equal. Hamming accuracy always
 * evaluates to heuristic values within the interval [0,1]. Texts that evaluate to greater heuristic
 * values are considered to be more similar than texts that evaluate to smaller heuristic values.
 */
public class HammingAccuracy implements TextMetric {

    @Override
    public final double evaluate(@NotNull final String text1, @NotNull final String text2) {
        double hammingLoss = new HammingLoss().evaluate(text1, text2);
        return 1 - hammingLoss;
    }

    @Override
    public final double minValue() {
        return 0;
    }

    @Override
    public final double maxValue() {
        return 1;
    }

    @Override
    public final boolean isGainMetric() {
        return true;
    }

}