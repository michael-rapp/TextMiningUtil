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
package de.mrapp.textmining.util.parser.number;

import de.mrapp.textmining.util.Token;
import de.mrapp.textmining.util.parser.*;
import de.mrapp.textmining.util.tokenizer.RegexTokenizer;
import de.mrapp.textmining.util.tokenizer.Tokenizer;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

/**
 * Allows to parse texts that contain numbers written in English in order to convert them to {@link
 * Integer} values.
 *
 * @author Michael Rapp
 * @since 1.3.0
 */
public class EnNumberParser extends AbstractTextParser<Integer> implements
        LocalizedTextParser<Integer> {

    private static final class EnNumberDictionary extends Dictionary<Integer> {

        private static final long serialVersionUID = 118756146776316093L;

        public EnNumberDictionary() {
            addEntry(new Entry<>("zero", 0));
            addEntry(new Entry<>("null", 0));
            addEntry(new Entry<>("nil", 0));
            addEntry(new Entry<>("one", 1));
            addEntry(new Entry<>("two", 2));
            addEntry(new Entry<>("three", 3));
            addEntry(new Entry<>("four", 4));
            addEntry(new Entry<>("five", 5));
            addEntry(new Entry<>("six", 6));
            addEntry(new Entry<>("seven", 7));
            addEntry(new Entry<>("eight", 8));
            addEntry(new Entry<>("nine", 9));
            addEntry(new Entry<>("ten", 10));
            addEntry(new Entry<>("teen", 10, AssociationType.LEFT));
            addEntry(new Entry<>("eleven", 11));
            addEntry(new Entry<>("twelve", 12));
            addEntry(new Entry<>("thirteen", 13));
            addEntry(new Entry<>("fifteen", 15));
            addEntry(new Entry<>("eighteen", 18));
            addEntry(new Entry<>("twenty", 20, AssociationType.RIGHT));
            addEntry(new Entry<>("thirty", 30, AssociationType.RIGHT));
            addEntry(new Entry<>("fourty", 40, AssociationType.RIGHT));
            addEntry(new Entry<>("fifty", 50, AssociationType.RIGHT));
            addEntry(new Entry<>("sixty", 60, AssociationType.RIGHT));
            addEntry(new Entry<>("seventy", 70, AssociationType.RIGHT));
            addEntry(new Entry<>("eighty", 80, AssociationType.RIGHT));
            addEntry(new Entry<>("ninety", 90, AssociationType.RIGHT));
        }

    }

    private final Dictionary<Integer> dictonary = new EnNumberDictionary();

    private final Tokenizer<? extends Token> tokenizer = new RegexTokenizer("\\s+|[^a-z]+");

    public EnNumberParser() {
        setPreProcessor(text -> text.toLowerCase(getLocale()));
    }

    @NotNull
    @Override
    protected final Integer onParse(@NotNull final String text) throws MalformedTextException {
        Set<? extends Token> tokens = tokenizer.tokenize(text);
        TokenSequence<ModifiableToken> tokenSequence = TokenSequence.createMappedSequence(tokens,
                ModifiableToken::new);

        for (ModifiableToken token : tokenSequence) {
            Matcher<ModifiableToken> matcher = Matcher.equals();

            for (Dictionary.Entry<Integer> entry : dictonary) {
                if (matcher.matches(token, entry)) {
                    if (tokenSequence.size() == 1) {
                        return entry.getValue();
                    } else {
                        ValueToken<Integer> valueToken = new ValueToken<>(token,
                                entry.getValue());
                        valueToken.setAssociationType(entry.getAssociationType());
                        token.setToken(valueToken);
                    }
                }
            }
        }

        Iterator<ModifiableToken> iterator = tokenSequence.iterator();
        int i = 0;

        while (iterator.hasNext()) {
            ModifiableToken token = iterator.next();

            if (token.getCurrent() instanceof ValueToken) {
                ValueToken<Integer> valueToken = token.getCurrent();
                ModifiableToken associatedToken = null;

                if (valueToken.getAssociationType() == AssociationType.LEFT) {
                    associatedToken = tokenSequence.get(i - 1);
                } else if (valueToken.getAssociationType() == AssociationType.RIGHT) {
                    associatedToken = tokenSequence.get(i + 1);
                }

                if (associatedToken != null && associatedToken.getCurrent() instanceof ValueToken) {
                    ValueToken<Integer> associatedValueToken = associatedToken.getCurrent();
                    return valueToken.getValue() + associatedValueToken.getValue();
                }
            }

            i++;
        }

        throw new MalformedTextException(text, "Unable to convert text to number");
    }

    @NotNull
    @Override
    public final Locale getLocale() {
        return Locale.ENGLISH;
    }

}
