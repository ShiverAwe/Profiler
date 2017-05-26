package org.jetbrains.test.Tracker.Argument;

/**
 * Vladimir Shefer
 * 22.05.2017.
 */
@FunctionalInterface
public interface Serializer<T> {
    String getSerializedValue(T value);
}
