/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package mpp.venusfr.utils.animations.impl;

import mpp.venusfr.utils.animations.Animation;
import mpp.venusfr.utils.animations.Direction;

public class SmoothStepAnimation
extends Animation {
    public SmoothStepAnimation(int n, double d) {
        super(n, d);
    }

    public SmoothStepAnimation(int n, double d, Direction direction) {
        super(n, d, direction);
    }

    @Override
    protected double getEquation(double d) {
        double d2 = d / (double)this.duration;
        return -2.0 * Math.pow(d2, 3.0) + 3.0 * Math.pow(d2, 2.0);
    }
}

