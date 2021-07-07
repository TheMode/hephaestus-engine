package team.unnamed.hephaestus.util;

import team.unnamed.hephaestus.struct.Vector3Float;

/**
 * Utility class for working with
 * vectors
 */
public final class Vectors {

    private Vectors() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    public static Vector3Float lerp(Vector3Float start, Vector3Float end, float percent) {
        return start.add(end.subtract(start).multiply(percent));
    }
}