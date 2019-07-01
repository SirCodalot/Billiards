package me.codalot.billiards.listeners;

import me.codalot.billiards.Billiards;
import me.codalot.billiards.entities.EntityBall;
import me.codalot.billiards.player.BilliardsPlayer;
import me.codalot.billiards.setup.KeyItem;
import me.codalot.core.listeners.CodalotListener;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.*;
import org.bukkit.util.Vector;

@SuppressWarnings("unused")
public class BallListener extends CodalotListener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem() == null || !event.getItem().isSimilar(KeyItem.CUE.getItem()))
            return;

        if (!event.getAction().toString().contains("RIGHT"))
            return;

        getPlayer(event.getPlayer()).drawCue();
    }

    @EventHandler
    public void onBow(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        if (event.getBow() == null || !event.getBow().isSimilar(KeyItem.CUE.getItem()))
            return;

        event.setCancelled(true);

        Player player = (Player) event.getEntity();
        double strength = getPlayer(player).releaseCue();

        EntityBall ball = getTargetBall(player);
        if (ball == null)
            return;

        ball.getBukkitEntity().getWorld().playSound(ball.getBukkitEntity().getLocation(), "billiards.cue_collision", (int) (5 * strength), 0);
        ball.collide(player.getEyeLocation().getDirection().normalize().multiply(2 * Math.sqrt(strength)), true);
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        getPlayer(event.getPlayer()).cancelCharge();
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        getPlayer(event.getPlayer()).cancelCharge();
    }

    @EventHandler
    public void onPlayerChangeMainHand(PlayerChangedMainHandEvent event) {
        getPlayer(event.getPlayer()).cancelCharge();
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        getPlayer(event.getPlayer()).cancelCharge();
    }

    private static BilliardsPlayer getPlayer(Player player) {
        return Billiards.getInstance().getPlayer(player);
    }

    private static EntityBall getTargetBall(Player player) {
        Vector direction = player.getEyeLocation().getDirection().multiply(0.1);
        Location originalLocation = player.getEyeLocation().clone();
        Location location = originalLocation.clone();

        while (location.distance(originalLocation) <= 2) {
            for (Entity entity : location.getWorld().getNearbyEntities(location, 0.1, 0.1, 0.1)) {
                EntityBall ball = getBall(entity);
                if (ball != null)
                    return ball;
            }

            if (!location.getBlock().isPassable())
                break;

            location = location.add(direction);
        }

        return null;
    }

    private static EntityBall getBall(Entity entity) {
        if (((CraftEntity) entity).getHandle() instanceof EntityBall)
            return (EntityBall) ((CraftEntity) entity).getHandle();
        else
            return null;
    }
}
