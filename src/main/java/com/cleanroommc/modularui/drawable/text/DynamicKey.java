package com.cleanroommc.modularui.drawable.text;

import java.util.function.Supplier;

public class DynamicKey extends BaseKey {

    private final Supplier<String> supplier;

    public DynamicKey(Supplier<String> supplier) {
        this.supplier = supplier;
    }

    @Override
    public String get() {
        return this.supplier.get();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof DynamicKey dynamicKey && dynamicKey.supplier == this.supplier);
    }
}