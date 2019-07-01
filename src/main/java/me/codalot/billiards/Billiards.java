package me.codalot.billiards;

import me.codalot.billiards.entities.EntityBall;
import me.codalot.billiards.listeners.BallListener;
import me.codalot.billiards.manager.TickManager;
import me.codalot.billiards.player.BilliardsPlayer;
import me.codalot.billiards.setup.KeyItem;
import me.codalot.core.CodalotPlugin;
import me.codalot.core.commands.CmdNode;
import me.codalot.core.commands.Command;
import me.codalot.core.commands.Executor;
import me.codalot.core.managers.types.ListenerManager;
import me.codalot.core.managers.types.PlayerManager;
import org.bukkit.Color;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public class Billiards extends CodalotPlugin {

    private static Billiards instance;

    @Override
    public void onEnable() {
        instance = this;

        EntityBall.register();

        managers = new ArrayList<>();
        managers.add(new PlayerManager<>(this, BilliardsPlayer.class));
        managers.add(new TickManager(this));
        managers.add(new ListenerManager(this,
                new BallListener()));

        new Command(new CmdNode() {
            @Override
            public void execute(Executor executor, String[] args) {
                executor.getPlayer().getInventory().addItem(KeyItem.CUE.getItem());
            }

            @Override
            public void failure(Executor executor) {

            }
        }, "cue").register(this);

        new Command(new CmdNode() {
            @Override
            public void execute(Executor executor, String[] args) {
                EntityBall ball = EntityBall.spawn(executor.getPlayer().getLocation());
                ball.initialize();
                ball.setModel(KeyItem.POOL_BALL_STRIPED.getItem(Color.RED));
            }

            @Override
            public void failure(Executor executor) {

            }
        }, "ball").register(this);

        super.onEnable();
    }

    public static Billiards getInstance() {
        return instance;
    }

    @SuppressWarnings("unchecked")
    public Collection<BilliardsPlayer> getPlayers() {
        return (Collection<BilliardsPlayer>) getManager(PlayerManager.class).getPlayers().values();
    }

    public BilliardsPlayer getPlayer(UUID uuid) {
        return (BilliardsPlayer) getManager(PlayerManager.class).getOrLoad(uuid);
    }

    public BilliardsPlayer getPlayer(Player player) {
        return getPlayer(player.getUniqueId());
    }
}
