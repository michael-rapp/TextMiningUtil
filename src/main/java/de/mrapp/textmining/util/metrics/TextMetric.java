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
 * Defines the interface, a metric, which allows to calculate heuristic values, which specify the
 * similarity or dissimilarity of texts, must implement.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public interface TextMetric {

    /**
     * Calculates the heuristic value of two texts according to the metric.
     *
     * @param text1 The first text as a {@link String}
     * @param text2 The second text as a {@link String}
     * @return The heuristic value, which has been calculated, as a {@link Long} value. The
     * heuristic value is at least {@link #minValue()} and at maximum {@link #maxValue()}
     */
    long evaluate(@NotNull String text1, @NotNull String text2);

    /**
     * Returns the minimum value of the metric.
     *
     * @return The minimum value of the metric as a {@link Long} value
     */
    long minValue();

    /**
     * Returns the maximum value of the metric.
     *
     * @return The maximum value of the metric as a {@link Long} value
     */
    long maxValue();

    /**
     * Returns, whether the metric is a gain metric, i.e. that two strings that evaluate to a
     * greater heuristic value are considered to be more similar than two strings that evaluate to a
     * smaller heuristic value.
     *
     * @return True, if the gain metric is a gain metric, false, if the metric is a loss metric
     */
    boolean isGainMetric();

}