package de.chojo.sadu.core.conversion;

public final class Unboxing {
    private Unboxing() {
        throw new UnsupportedOperationException("This is a utility class.");
    }

    public static byte[] unbox(Byte[] array) {
        var unboxed = new byte[array.length];
        for (var i = 0; i < array.length; i++) unboxed[i] = array[i];
        return unboxed;
    }
}
