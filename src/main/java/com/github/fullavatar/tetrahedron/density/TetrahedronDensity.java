package com.github.fullavatar.tetrahedron.density;

import com.hypixel.hytale.builtin.hytalegenerator.density.Density;

import it.unimi.dsi.fastutil.doubles.Double2DoubleFunction;

import javax.annotation.Nonnull;

public final class TetrahedronDensity extends Density {

    private static final double SQRT_2 = Math.sqrt(2.0);
    private static final double SQRT_6 = Math.sqrt(6.0);
    @Nonnull
    private final Double2DoubleFunction falloffFunction;

    public TetrahedronDensity(@Nonnull Double2DoubleFunction falloffFunction) {
        this.falloffFunction = falloffFunction;
    }

    private double tetrahedronRadius(@Nonnull Context context) {
        final double scaledZ = SQRT_2 * context.position.z;
        final double scaledX = SQRT_6 * context.position.x;

        final double sidePairBase = context.position.y + scaledZ;

        final double baseFace = -3.0 * context.position.y;
        final double rearFace = context.position.y - 2.0 * scaledZ;
        final double rightFace = sidePairBase + scaledX;
        final double leftFace = sidePairBase - scaledX;
        return Math.max(
            baseFace, Math.max(
                rearFace, Math.max(
                    rightFace,
                    leftFace
                )
            )
        );
    }

    @Override
    public double process(@Nonnull Context context) {
        final double radius = tetrahedronRadius(context);
        return falloffFunction.get(radius);
    }
}
