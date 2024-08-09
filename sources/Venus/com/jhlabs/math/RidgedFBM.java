/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.jhlabs.math;

import com.jhlabs.math.Function2D;
import com.jhlabs.math.Noise;

public class RidgedFBM
implements Function2D {
    @Override
    public float evaluate(float f, float f2) {
        return 1.0f - Math.abs(Noise.noise2(f, f2));
    }
}

