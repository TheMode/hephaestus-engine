package team.unnamed.hephaestus.commands;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import team.unnamed.hephaestus.Model;
import team.unnamed.hephaestus.animation.ModelAnimation;
import team.unnamed.hephaestus.view.BukkitModelView;
import team.unnamed.hephaestus.view.ModelViewRenderer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class SummonCommand implements CommandClass {

    private final ModelViewRenderer renderer;
    // temporal
    private final Map<String, BukkitModelView> views = new HashMap<>();

    public SummonCommand(ModelViewRenderer renderer) {
        this.renderer = renderer;
    }

    @Command(names = "teleport")
    public void teleport(@Sender Player player, String viewId) {
        BukkitModelView view = views.get(viewId);
        if (view == null) {
            player.sendMessage("§cUnknown view");
            return;
        }

        view.teleport(player.getLocation());
    }

    @Command(names = "colorize")
    public void colorize(
            @Sender Player player,
            String viewId,
            int red,
            int green,
            int blue
    ) {
        BukkitModelView view = views.get(viewId);
        if (view == null) {
            player.sendMessage("§cUnknown view");
            return;
        }

        view.colorize(Color.fromRGB(red, green, blue));
    }

    @Command(names = "animate")
    public void animate(
            @Sender Player player,
            String viewId,
            String animationName
    ) {
        BukkitModelView view = views.get(viewId);
        if (view == null) {
            player.sendMessage("§cUnknown view");
            return;
        }
        ModelAnimation animation = view.getModel().getAnimations().get(animationName);
        if (animation == null) {
            player.sendMessage("§cUnknown animation");
            return;
        }

        view.playAnimation(animation);
    }

    @Command(names = "summon")
    public void onCommand(
            @Sender Player player,
            Model model,
            @OptArg ModelAnimation animation
    ) {
        Location location = player.getLocation();

        String id = Integer.toHexString(ThreadLocalRandom.current().nextInt(0xFFFFFFF));
        BukkitModelView entity = renderer.render(model, location, player);
        views.put(id, entity);
        player.sendMessage("Model '" + model.getName() + "' summoned. Id: " + id);

        if (animation != null) {
            entity.playAnimation(animation);
        }
    }
}