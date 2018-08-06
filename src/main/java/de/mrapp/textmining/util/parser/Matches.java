package de.mrapp.textmining.util.parser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.stream.StreamSupport;

import static de.mrapp.util.Condition.ensureNotNull;
import static de.mrapp.util.Condition.ensureTrue;

public class Matches<T> implements Serializable, Iterable<Matches.Match<T>> {

    public static class Match<T> implements Serializable {

        private static final long serialVersionUID = 2113132463577885833L;

        private final double heuristicValue;

        private final boolean gainMetric;

        private T value;

        public Match(final double heuristicValue, final boolean gainMetric,
                     @Nullable final T value) {
            this.heuristicValue = heuristicValue;
            this.gainMetric = gainMetric;
            this.value = value;
        }

        public final double getHeuristicValue() {
            return heuristicValue;
        }

        public final boolean isGainMetric() {
            return gainMetric;
        }

        public final T getValue() {
            return value;
        }

        public final void setValue(@Nullable final T value) {
            this.value = value;
        }

        // TODO: clone, hashcode, equals

    }

    private static final long serialVersionUID = 8123934978904938901L;

    private final Iterable<Match<T>> matches;

    public Matches(@NotNull final Iterable<Match<T>> matches) {
        ensureNotNull(matches, "The iterable may not be null");
        ensureTrue(matches.iterator().hasNext(), "The iterable must not be empty");
        this.matches = matches;
    }

    @Nullable
    public final Match<T> getBestMatch() {
        return getBestMatch(null);
    }

    @Nullable
    public final Match<T> getBestMatch(
            @Nullable final BiFunction<Match<T>, Match<T>, Match<T>> tieBreaker) {
        return StreamSupport.stream(matches.spliterator(), false).reduce(null,
                (firstMatch, secondMatch) -> {
                    double heuristicValue1 = firstMatch.getHeuristicValue();
                    double heuristicValue2 = secondMatch.getHeuristicValue();
                    boolean firstBetter =
                            firstMatch.isGainMetric() ? heuristicValue1 > heuristicValue2 :
                                    heuristicValue1 < heuristicValue2;
                    return firstBetter ? firstMatch :
                            (tieBreaker != null ? tieBreaker.apply(firstMatch, secondMatch) :
                                    firstMatch);
                });
    }

    @NotNull
    @Override
    public final Iterator<Match<T>> iterator() {
        return matches.iterator();
    }

    // TODO: clone, hashcode, equals

}