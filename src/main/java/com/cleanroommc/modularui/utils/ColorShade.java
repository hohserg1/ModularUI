package com.cleanroommc.modularui.utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ColorShade implements Iterable<Integer> {

    public static Builder builder(int main) {
        return new Builder(main);
    }

    public final int main;
    private final int[] brighter;
    private final int[] darker;
    private final int[] all;

    private ColorShade(int main, int[] brighter, int[] darker) {
        this.main = main;
        this.brighter = brighter;
        this.darker = darker;
        this.all = new int[brighter.length + darker.length + 1];
        int k = 0;
        for (int i = brighter.length - 1; i >= 0; i--) {
            this.all[k++] = brighter[i];
        }
        this.all[k++] = this.main;
        for (int j : darker) {
            this.all[k++] = j;
        }
    }

    public int darker(int index) {
        return this.darker[index];
    }

    public int brighter(int index) {
        return this.brighter[index];
    }

    @NotNull
    @Override
    public Iterator<Integer> iterator() {
        return Arrays.stream(this.all).iterator();
    }

    public static class Builder {

        private final int main;
        private final List<Integer> darker = new ArrayList<>();
        private final List<Integer> brighter = new ArrayList<>();

        public Builder(int main) {
            this.main = main;
        }

        public Builder addDarker(int... darker) {
            Arrays.stream(darker).forEach(this.darker::add);
            return this;
        }

        public Builder addBrighter(int... brighter) {
            Arrays.stream(brighter).forEach(this.brighter::add);
            return this;
        }

        public ColorShade build() {
            return new ColorShade(this.main, this.darker.stream().mapToInt(Integer::intValue).toArray(), this.brighter.stream().mapToInt(Integer::intValue).toArray());
        }
    }
}
