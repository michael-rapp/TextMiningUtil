# TextMiningUtil - README

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) [![Donate](https://img.shields.io/badge/Donate-PayPal-green.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=X75YSLEJV3DWE)

"TextMiningUtil" is a Kotlin library that provides various utility classes for use in text mining such as text distance and similarity metrics. The library currently provides the following features:

- Various metrics for measuring the similarity or dissimilarity of texts.
- Tokenizers for splitting texts into shorter subtexts.

Note that this library was implemented in Java 8 prior to version 2.0.0.

## License Agreement

This project is distributed under the Apache License version 2.0. For further information about this license agreement's content please refer to its full version, which is available at http://www.apache.org/licenses/LICENSE-2.0.txt.

## Download

The latest release of this library can be downloaded as a zip archive from the download section of the project's Github page, which is available [here](https://github.com/michael-rapp/TextMiningUtil/releases). Furthermore, the library's source code is available as a Git repository, which can be cloned using the URL https://github.com/michael-rapp/TextMiningUtil.git.

Alternatively, the library can be added to your project as a Gradle dependency by adding the following to the `build.gradle` file:

```groovy
dependencies {
    compile 'com.github.michael-rapp:text-mining-util:2.1.3'
}
```

When using Maven, the following dependency can be added to the `pom.xml`:

```xml
<dependency>
    <groupId>com.github.michael-rapp</groupId>
    <artifactId>text-mining-util</artifactId>
    <version>2.1.3</version>
</dependency>
```

## Features

In the following a brief overview of the features, which are provided by the library, is given.

### Metrics

The library comes with various metrics for measuring the similarity or dissimilarity of texts. The following metrics are provided:

- `DiceCoefficient`: Measures the similarity of texts by splitting them into n-grams and calculating the percentage of n-grams that occur in both texts.
- `HammingDistance`: Measures the distance between texts by counting the number of corresponding characters that are not equal (can only be applied to texts with the same length). `HammingLoss` and `HammingAccuracy` measure the dissimilarity, respectively similarity as a percentage.
- `LevenshteinDistance`: Measures the distance between texts by counting the number of single-character edits that are necessary to change one text to another (can be applied to texts with different lengths). `LevenshteinDissimilarity` and `LevenshteinSimilarity` measure the dissimilarity, respectively similarity, as a percentage.
- `OptimalStringAlignmentDistance`: Measures the distance between text by counting the number of single-character edits and transpositions of adjacent characters that are necessary to change one text to another (only one edit is allowed per substring; can be applied to texts with different lengths). `OptimalStringAlignmentDissimilarity` and `OptimalStringAlignmentSimilarity` measure the dissimilarity, respectively similarity, as a percentage.
- `DamerauLevenshteinDistance`: Measures the distance between text by counting the number of single-character edits and transpositions of adjacent characters that are necessary to change one text to another (no restrictions; can be applied to texts with different length). `DamerauLevenshteinDissimilarity` and `DamerauLevenshteinSimilarity` measure the dissimilarity, respectively similarity, as a percentage.

### Tokenizers

Tokenizers allow to split texts into shorter subtexts. The library provides the following implementations:

- `SubstringTokenizer`: Allows to split texts into all possible substrings.
- `FixedLengthTokenizer`: Allows to split texts into substrings with a specific length.
- `RegexTokenizer`: Allows to split texts based on regular expressions (e.g. at whitespace or at certain delimiters).
- `NGramTokenizer`: Allows to split texts into n-grams of specific lengths.

## Contact information

For personal feedback or questions feel free to contact me via the mail address, which is mentioned on my [Github profile](https://github.com/michael-rapp). If you have found any bugs or want to post a feature request, please use the [bugtracker](https://github.com/michael-rapp/TextMiningUtil/issues) to report them.
