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
package de.mrapp.textmining.util.parser.numbers

import de.mrapp.textmining.util.parser.AbstractTextParser
import de.mrapp.textmining.util.parser.Dictionary
import java.util.*

/**
 * A [NumberParser] that allows to convert textual representation of numbers written in English to
 * integer values.
 *
 * @author Michael Rapp
 * @since 2.1.0
 */
class EnNumberParser : AbstractTextParser<Int>(), NumberParser {

    private class Dictionary : de.mrapp.textmining.util.parser.Dictionary<CharSequence, Int>() {

    }

    override fun onParse(text: CharSequence): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLocale(): Locale = Locale.ENGLISH

}