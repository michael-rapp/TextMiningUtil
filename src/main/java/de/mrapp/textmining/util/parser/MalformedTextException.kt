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

/**
 * An exception that may be thrown by a [TextParser], if a given text could not be parsed.
 *
 * @property text    The text that could not be parsed
 * @property message The message of the exception
 * @property cause   The cause of the exception
 * @author Michael Rapp
 * @since 2.1.0
 */
class MalformedTextException @JvmOverloads constructor(val text: String, message: String? = null,
                                                       cause: Throwable? = null) :
        Exception(message, cause)
