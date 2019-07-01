package me.codalot.billiards.listeners;

import me.codalot.billiards.entities.EntityBall;
import me.codalot.core.listeners.CodalotListener;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

@SuppressWarnings("unused")
public class BallListener extends CodalotListener {

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        EntityBall ball = getBall(event.getEntity());
        if (ball == null)
            return;

        event.setCancelled(true);

        Vector velocity;
        if (event.getDamager() instanceof Projectile)
            velocity = event.getDamager().getVelocity();
        else
            velocity = event.getDamager().getLocation().getDirection();

        ball.collide(velocity);
    }

    private static EntityBall getBall(Entity entity) {
        if (((CraftEntity) entity).getHandle() instanceof EntityBall)
            return (EntityBall) ((CraftEntity) entity).getHandle();
        else
            return null;
    }
}
