package com.github.fullavatar.tetrahedron;

import com.github.fullavatar.tetrahedron.density.TetrahedronDensityAsset;
import com.hypixel.hytale.builtin.hytalegenerator.assets.density.DensityAsset;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import javax.annotation.Nonnull;

public final class TetrahedronPlugin extends JavaPlugin {

    public TetrahedronPlugin(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        getCodecRegistry(DensityAsset.CODEC)
            .register("Tetrahedron", TetrahedronDensityAsset.class, TetrahedronDensityAsset.CODEC);
    }
}
