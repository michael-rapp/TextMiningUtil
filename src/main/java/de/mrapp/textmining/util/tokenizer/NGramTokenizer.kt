package de.mrapp.textmining.util.tokenizer

import de.mrapp.util.Condition.ensureAtLeast

/**
 * Allows to create n-grams from texts. E.g. the substrings "t", "te", "tex", "ext", "xt" and "t"
 * can be created from the text "text". In general, the [NGramTokenizer] less tokens than the class
 * [SubstringTokenizer].
 *
 * @property minLength The minimum length of the n-grams that are created by the tokenizer
 * @property maxLength The maximum length of the n-grams that are created by the tokenizer
 * @author Michael Rapp
 * @since 1.0.0
 */
class NGramTokenizer(val minLength: Int = 1, val maxLength: Int = Integer.MAX_VALUE) :
        AbstractTokenizer<NGram>() {

    init {
        ensureAtLeast(minLength, 1, "The minimum length must be at least 1")
        ensureAtLeast(maxLength, minLength,
                "The maximum length must be at least the minimum length")
    }

    override fun onTokenize(text: String, map: MutableMap<String, NGram>) {
        val length = text.length
        val nGramFactory: (String, Int) -> NGram = { t, p -> NGram(maxLength, t, p) }

        for (n in minLength..Math.min(maxLength, length - 1)) {
            val token = text.substring(0, n)
            addToken(map, token, 0, nGramFactory)
        }

        for (i in 1..length - minLength) {
            val token = text.substring(i, i + Math.min(maxLength, length - i))
            addToken(map, token, i, nGramFactory)
        }
    }

}
