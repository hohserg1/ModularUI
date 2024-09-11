package com.cleanroommc.modularui.drawable.text;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class StringKey extends BaseKey {

    private final String string;
    private final Object[] args;

    public StringKey(String string) {
        this(string, null);
    }

    public StringKey(String string, @Nullable Object[] args) {
        this.string = Objects.requireNonNull(string);
        this.args = args == null || args.length == 0 ? null : args;
    }

    @Override
    public String get() {
        return this.args == null ? this.string : String.format(this.string, this.args);
    }
}