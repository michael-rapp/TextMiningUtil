# TextMiningUtil - RELEASE NOTES

## Version 2.1.0 (Sep. 4th 2018)

A feature release, which introduces the following changes:

- Added helper classes for parsing texts.
- Added a helper method to create case-insensitive metrics to the interface `TextMetric`.
- Added class `TextMetric.Comparator`.
- `Tokenizer`s and `TextMetric`s can now be applied to `CharSequence`s.
- Replaced getter and setter methods with properties to be in accordance with the Kotlin paradigm
- Updated dependency "JavaUtil" to version 2.0.2.
- Updated Kotlin to version 1.3.11.

## Version 2.0.0 (Aug. 7th 2018)

A major release, which introduces the following changes:

- Migrated the project to use the Kotlin programming language instead of Java.
- Converted the inner interface `Tokenizer.Token` into a separate interface `Token`.
- Converted the inner class `NGramTokenizer.NGram` into a separate class `NGram`.
- Changed the return type of the method `Tokenizer#tokenize` from `Set` to `Collection`. 

## Version 1.2.0 (May. 21th 2018)

A feature release, which introduces the following changes:

- Added the metrics `OptimalStringAlignmentDistance`, `OptimalStringAlignmentDissimilarity` and `OptimalStringAlignmentSimilarity`.
- Added the metrics `DamerauLevenshteinDistance`, `DamerauLevenshteinDissimilarity` and `DamerauLevenshteinSimilarity`.
- Added the tokenizers `FixedLengthTokenizer` and `RegexTokenizer`.

## Version 1.1.1 (Apr. 29th 2018)

A minor release, which provides the following changes:

- Updated dependency "JavaUtil" to version 1.2.0.

## Version 1.1.0 (Nov. 19th 2017)

A feature release, which provides the following changes:

- Tokenizers do not return multiple n-grams or substrings with the same token anymore. Instead, the positions off all duplicates are aggregated in one `NGram` or `Substring`.
- Added constructors, which only allows to specify a maximum length, but no minimum length, to the classes `NGramTokenizer` and `DiceCoefficient`.

## Version 1.0.0 (Nov. 17th 2017)

The first stable release of the library, which provides the following utility classes:

- The classes `SubstringTokenizer` and `NGramTokenizer` for splitting texts into shorter subtexts.
- The metrics `DiceCoefficient`, `HammingDistance`, `HammingLoss`, `HammingAccuracy`, `LevenshteinDistance`, `LevenshteinDissimilarity` and `LevenshteinSimilarity`.