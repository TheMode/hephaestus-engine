package team.unnamed.hephaestus.mythicmobs;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import team.unnamed.hephaestus.AnimationEnginePlugin;
import team.unnamed.hephaestus.ModelRegistry;
import team.unnamed.hephaestus.ModelViewRegistry;
import team.unnamed.hephaestus.Model;
import team.unnamed.hephaestus.view.BukkitModelView;
import team.unnamed.hephaestus.view.ModelViewRenderer;

public class ModelMechanic
        extends SkillMechanic
        implements ITargetedEntitySkill {

    private static final float ARMORSTAND_HEIGHT = 0.726F;

    private final ModelRegistry registry;
    private final ModelViewRegistry viewRegistry;
    private final PlaceholderString modelName;
    private final ModelViewRenderer renderer;

    public ModelMechanic(
            ModelRegistry registry,
            ModelViewRegistry viewRegistry,
            MythicLineConfig config,
            ModelViewRenderer renderer
    ) {
        super(config.getLine(), config);

        this.registry = registry;
        this.viewRegistry = viewRegistry;
        this.modelName = config.getPlaceholderString("model", null);
        this.renderer = renderer;
    }

    @Override
    public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
        Model model = registry.get(modelName.get(data, target));
        LivingEntity entity = (LivingEntity) target.getBukkitEntity();
        entity.setInvisible(true);

        BukkitModelView view = renderer.render(model, entity.getLocation());
        viewRegistry.register(view);

        new BukkitRunnable() {

            private boolean wasMoving = false;

            @Override
            public void run() {
                Vector velocity = entity.getVelocity();
                boolean moving = velocity.getX() != 0D || velocity.getY() != 0D || velocity.getZ() != 0D;

                if (wasMoving != moving) {
                    view.stopAllAnimations();
                    view.playAnimation(moving ? "walk" : "idle");
                    wasMoving = moving;
                }

                view.setLocation(entity.getLocation().subtract(0, ARMORSTAND_HEIGHT, 0));
                view.tickAnimations();
            }

        }.runTaskTimerAsynchronously(
                AnimationEnginePlugin.getPlugin(AnimationEnginePlugin.class),
                0L,
                1L
        );
        return true;
    }

}
