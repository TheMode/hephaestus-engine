package team.unnamed.hephaestus.model;

import org.jetbrains.annotations.Nullable;
import team.unnamed.hephaestus.model.animation.ModelAnimation;

import java.util.List;
import java.util.Map;

public class Model {

    private final String name;
    private final List<ModelBone> bones;
    private ModelAsset asset;
    private final Map<String, ModelAnimation> animations;

    public Model(
            String name,
            List<ModelBone> bones,
            ModelAsset asset
    ) {
        this.name = name;
        this.bones = bones;
        this.asset = asset;
        // data from 'asset' that will persist after calling
        // discardResourcePackData()
        this.animations = asset.getAnimations();
    }

    public String getName() {
        return name;
    }

    public List<ModelBone> getBones() {
        return bones;
    }

    public Map<String, ModelAnimation> getAnimations() {
        return animations;
    }

    @Nullable
    public ModelAsset getAsset() {
        return asset;
    }

    /**
     * Discards the information used only in
     * the resource pack generation in this
     * model instance
     */
    public void discardResourcePackData() {
        this.asset = null;
        for (ModelBone bone : bones) {
            bone.discardResourcePackData();
        }
    }

}