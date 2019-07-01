package me.codalot.billiards.entities;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import lombok.Getter;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.entity.Zombie;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.*;

@SuppressWarnings("all")
@Getter
public class EntityBall extends EntityZombie {

    private static EntityTypes typesLoc;

    private boolean xCollision;
    private boolean zCollision;

    public EntityBall(EntityTypes types, World world) {
        super(types.ZOMBIE, world);

        xCollision = false;
        zCollision = false;

        clearPathfinders();
    }

    public void initialize() {
        setBaby(true);
        setSilent(true);
        setInvulnerable(true);
        setNoGravity(true);
        ((Zombie) getBukkitEntity()).setRemoveWhenFarAway(false);
        ((Zombie) getBukkitEntity()).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 9999, 1, true, false));
    }

    @Override
    public void entityBaseTick() {
        fireTicks = 0;

        getBukkitEntity().setVelocity(getBukkitEntity().getVelocity().setY(0));
        detectCollision();

        super.entityBaseTick();
    }

    public void setModel(org.bukkit.inventory.ItemStack model) {
        ((Zombie) getBukkitEntity()).getEquipment().setHelmet(model);
    }

    public void hit(Vector velocity) {
        getBukkitEntity().setVelocity(getBukkitEntity().getVelocity().add(velocity));
    }

    public void collide(boolean horizontal) {
        Vector multiply = horizontal ? new Vector(-1, 1, 1) : new Vector(1, 1, -1);
        getBukkitEntity().setVelocity(getBukkitEntity().getVelocity().multiply(multiply));
    }

    public void collide(Vector other) {
        getBukkitEntity().setVelocity(other);
        velocity = other;
    }

    private Vector velocity;

    private void detectCollision() {

        if (velocity != null) {
            if (velocity.getX() != 0 && getBukkitEntity().getVelocity().getX() == 0 &&
                    !getBukkitEntity().getLocation().add(new Vector(velocity.getX(), 0, 0)).getBlock().isPassable()) {
                getBukkitEntity().setVelocity(getBukkitEntity().getVelocity().setX(velocity.getX()));
            }

        if (velocity.getZ() != 0 && getBukkitEntity().getVelocity().getZ() == 0 &&
                    !getBukkitEntity().getLocation().add(new Vector(0, 0, velocity.getZ())).getBlock().isPassable()) {
                getBukkitEntity().setVelocity(getBukkitEntity().getVelocity().setZ(velocity.getZ()));
            }
        }

        velocity = getBukkitEntity().getVelocity();

        if (!getBukkitEntity().getLocation().add(new Vector(getBukkitEntity().getVelocity().getX(), 0, 0)).getBlock().isPassable()) {
            if (!xCollision)
                collide(true);
            xCollision = true;
        } else
            xCollision = false;

        if (!getBukkitEntity().getLocation().add(new Vector(0, 0, getBukkitEntity().getVelocity().getZ())).getBlock().isPassable()) {
            if (!zCollision)
                collide(false);
            zCollision = true;
        } else
            zCollision = false;
    }

    @SuppressWarnings("unchecked")
    public static void register() {
        Map<String, Type<?>> types = (Map<String, Type<?>>) DataConverterRegistry.a().getSchema(DataFixUtils.makeKey(SharedConstants.a().getWorldVersion())).findChoiceType(DataConverterTypes.ENTITY).types();
        types.put("minecraft:pool_ball", types.get("minecraft:zombie"));
        EntityTypes.a<Entity> a = EntityTypes.a.a(EntityBall::new, EnumCreatureType.MISC);
        typesLoc = IRegistry.a(IRegistry.ENTITY_TYPE, "pool_ball", a.a("pool_ball"));
    }

    public static EntityBall spawn(Location location) {
        World world = ((CraftWorld) location.getWorld()).getHandle();
        EntityBall entity = (EntityBall) typesLoc.b(
                world,
                null,
                null,
                null,
                new BlockPosition(location.getX(), location.getY(), location.getZ()),
                null,
                false,
                false
        );
        world.addEntity(entity);

        return entity;
    }

    private static Object getPrivateField(String fieldName, Class clazz, Object object)
    {
        Field field;
        Object o = null;
        try {
            field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            o = field.get(object);
        }
        catch(NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return o;
    }

    private void clearPathfinders() {
        try {
            Field brField = EntityLiving.class.getDeclaredField("br");
            brField.setAccessible(true);
            BehaviorController<?> controller = (BehaviorController<?>) brField.get(this);

            /*
            Replace memoriesField with this in 1.14
            Field aField = BehaviorController.class.getDeclaredField("a");
            aField.setAccessible(true);
            aField.set(controller, new HashMap<>());
            */

            Field memoriesField = BehaviorController.class.getDeclaredField("memories");
            memoriesField.setAccessible(true);
            memoriesField.set(controller, new HashMap<>());

            /*
            Replace sensors field with this in 1.14.1
            Field bField = BehaviorController.class.getDeclaredField("b");
            bField.setAccessible(true);
            bField.set(controller, new LinkedHashMap<>());
            */

            Field sensorsField = BehaviorController.class.getDeclaredField("sensors");
            sensorsField.setAccessible(true);
            sensorsField.set(controller, new LinkedHashMap<>());

            Field cField = BehaviorController.class.getDeclaredField("c");
            cField.setAccessible(true);
            cField.set(controller, new TreeMap<>());
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }


        try {
            Field dField;
            dField = PathfinderGoalSelector.class.getDeclaredField("d");
            dField.setAccessible(true);
            dField.set(goalSelector, new LinkedHashSet<>());
            dField.set(targetSelector, new LinkedHashSet<>());

            Field cField;
            cField = PathfinderGoalSelector.class.getDeclaredField("c");
            cField.setAccessible(true);
            dField.set(goalSelector, new LinkedHashSet<>());
            cField.set(targetSelector, new EnumMap<>(PathfinderGoal.Type.class));

            Field fField;
            fField = PathfinderGoalSelector.class.getDeclaredField("f");
            fField.setAccessible(true);
            dField.set(goalSelector, new LinkedHashSet<>());
            fField.set(targetSelector, EnumSet.noneOf(PathfinderGoal.Type.class));
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
