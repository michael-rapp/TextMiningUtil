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
package de.mrapp.textmining.util.parser

import java.io.Serializable

/**
 * Represents that a specific value matches some criteria.
 *
 * @property value          The matching value
 * @property heuristicValue The heuristic value that specifies the accuracy of the match
 * @property isGainMetric   True, if greater heuristic values represent a greater accuracy, false
 *                          otherwise
 * @author Michael Rapp
 * @since 2.1.0
 */
data class Match<T>(val value: T, val heuristicValue: Double, val isGainMetric: Boolean) :
        Serializable
