package me.codalot.billiards;

import me.codalot.billiards.entities.EntityBall;
import me.codalot.billiards.listeners.BallListener;
import me.codalot.billiards.setup.KeyItem;
import me.codalot.core.CodalotPlugin;
import me.codalot.core.commands.CmdNode;
import me.codalot.core.commands.Command;
import me.codalot.core.commands.Executor;
import me.codalot.core.managers.types.ListenerManager;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.util.Vector;

public class Billiards extends CodalotPlugin {

    @Override
    public void onEnable() {
        super.onEnable();

        EntityBall.register();

        managers.add(new ListenerManager(this,
                new BallListener()));

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
        }, "test").register(this);
    }
}
