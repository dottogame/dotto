package com.dotto.client.util;

public class Casteljau {
    // simple linear interpolation between two points
    public static float[] lerp(float[] a, float[] b, float t) {
        float[] res = { a[0] + (b[0] - a[0]) * t, a[1] + (b[1] - a[1]) * t };

        return res;
    }

    // evaluate a point on a bezier-curve. t goes from 0 to 1.0
    public static float[] bezier(
        float[] a, float[] b, float[] c, float[] d, float t
    ) {
        float[] ab, bc, cd, abbc, bccd;
        ab = lerp(a, b, t); // point between a and b (green)
        bc = lerp(b, c, t); // point between b and c (green)
        cd = lerp(c, d, t); // point between c and d (green)
        abbc = lerp(ab, bc, t); // point between ab and bc (blue)
        bccd = lerp(bc, cd, t); // point between bc and cd (blue)
        return lerp(abbc, bccd, t); // point on the bezier-curve (black)
    }
}