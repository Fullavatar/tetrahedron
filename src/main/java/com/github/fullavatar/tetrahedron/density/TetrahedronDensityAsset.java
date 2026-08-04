package com.github.fullavatar.tetrahedron.density;

import com.hypixel.hytale.builtin.hytalegenerator.assets.curves.ConstantCurveAsset;
import com.hypixel.hytale.builtin.hytalegenerator.assets.curves.CurveAsset;
import com.hypixel.hytale.builtin.hytalegenerator.assets.density.DensityAsset;
import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.builtin.hytalegenerator.density.nodes.ConstantValueDensity;
import com.hypixel.hytale.builtin.hytalegenerator.density.nodes.RotatorDensity;
import com.hypixel.hytale.builtin.hytalegenerator.density.nodes.ScaleDensity;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.schema.SchemaContext;
import com.hypixel.hytale.codec.schema.config.Schema;
import com.hypixel.hytale.codec.validation.ValidationResults;
import com.hypixel.hytale.codec.validation.Validator;
import com.hypixel.hytale.math.vector.Vector3dUtil;
import org.joml.Vector3d;

import javax.annotation.Nonnull;

public final class TetrahedronDensityAsset extends DensityAsset {

    @Nonnull
    public static final BuilderCodec<TetrahedronDensityAsset> CODEC = BuilderCodec
        .builder(TetrahedronDensityAsset.class, TetrahedronDensityAsset::new, DensityAsset.ABSTRACT_CODEC)
        .append(
            new KeyedCodec<>("Curve", CurveAsset.CODEC, true),
            (asset, value) -> asset.densityCurveAsset = value,
            asset -> asset.densityCurveAsset
        ).add()
        .append(
            new KeyedCodec<>("Scale", Vector3dUtil.CODEC, false),
            (asset, value) -> asset.scaleVector = value,
            asset -> asset.scaleVector
        ).addValidator(new Validator<Vector3d>() {
            @Override
            public void accept(Vector3d value, ValidationResults results) {
                if (value.x == 0 || value.y == 0 || value.z == 0) {
                    results.fail("scale vector contains 0.0");
                }
            }

            @Override
            public void updateSchema(SchemaContext context, Schema target) {
            }
        }).add()
        .append(
            new KeyedCodec<>("NewYAxis", Vector3dUtil.CODEC, false),
            (asset, value) -> {
                if (value.length() != 0) {
                    asset.newYAxis = value;
                }
            },
            asset -> asset.newYAxis
        ).add()
        .append(
            new KeyedCodec<>("Spin", Codec.DOUBLE, false),
            (asset, value) -> asset.spinAngle = value,
            asset -> Double.valueOf(asset.spinAngle)
        ).add()
        .build();

    private CurveAsset densityCurveAsset = new ConstantCurveAsset();
    private Vector3d scaleVector = new Vector3d(1, 1, 1);
    @Nonnull
    private Vector3d newYAxis = new Vector3d(0, 1, 0);
    private double spinAngle;

    @Override
    public @Nonnull Density build(@Nonnull Argument argument) {
        if (isSkipped() || densityCurveAsset == null) {
            return new ConstantValueDensity(0.0);
        }

        var tetrahedron = new TetrahedronDensity(densityCurveAsset.build());
        var scale = new ScaleDensity(scaleVector.x, scaleVector.y, scaleVector.z, tetrahedron);

        return new RotatorDensity(scale, newYAxis, spinAngle);
    }

    @Override
    public void cleanUp() {
        cleanUpInputs();
        if (densityCurveAsset != null) {
            densityCurveAsset.cleanUp();
        }
    }
}
