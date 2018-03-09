package com.dotto.cli.ui;

/**
 * /// Class Description ///
 * 
 * @author lite20 (Ephraim Bilson)
 */
public class Generator {

    public static Element buildButton(
        String text, Graphic parts[], int x, int y, int w
    ) {
        // TODO create functions to stretch and combine graphics
        Graphic graph = null;

        return new Element(graph, x, y, w, graph.getHeight());
    }

    public static float[] CalculateBezierPoint(
        float t, int[] p0, int[] p1, int[] p2, int[] p3
    ) {
        float u = 1.0f - t;
        float tt = t * t;
        float uu = u * u;
        float uuu = uu * u;
        float ttt = tt * t;

        // compute the first term of the equation
        float[] pt = { uuu * p0[0], uuu * p0[1] };

        // add second term
        float[] pt2 = { pt[0] + (3 * uu * t * p1[0]),
            pt[1] + (3 * uu * t * p1[1]) };

        // add third term
        float[] pt3 = { pt2[0] + (3 * u * tt * p2[0]),
            pt2[1] + (3 * u * tt * p2[1]) };

        // return addition with fourth terms
        float[] pt4 = { pt3[0] + (ttt * p3[0]), pt3[1] + (ttt * p3[1]) };
        return pt4;
    }
}
