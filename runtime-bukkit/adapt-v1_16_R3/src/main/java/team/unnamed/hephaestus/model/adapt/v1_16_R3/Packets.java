package team.unnamed.hephaestus.model.adapt.v1_16_R3;

import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PlayerConnection;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * Utility class for packets, specific
 * for v1_16_R3 minecraft server version
 */
public final class Packets {

    private Packets() {
    }

    /**
     * Sends the given {@code packets} to the
     * specified {@code player}
     */
    public static void send(Player player, Packet<?>... packets) {
        PlayerConnection connection = ((CraftPlayer) player)
                .getHandle().playerConnection;
        for (Packet<?> packet : packets) {
            connection.sendPacket(packet);
        }
    }

    /**
     * Sends the given {@code packets} to all the
     * specified {@code players}
     */
    public static void send(Iterable<? extends Player> players, Packet<?>... packets) {
        for (Player player : players) {
            PlayerConnection connection = ((CraftPlayer) player)
                    .getHandle().playerConnection;
            for (Packet<?> packet : packets) {
                connection.sendPacket(packet);
            }
        }
    }

}