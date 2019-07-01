package me.codalot.billiards.manager;

import me.codalot.billiards.Billiards;
import me.codalot.billiards.player.BilliardsPlayer;
import me.codalot.core.managers.Manager;
import org.bukkit.Bukkit;

public class TickManager implements Manager {

    private Billiards plugin;

    private int ticks;

    public TickManager(Billiards plugin) {
        this.plugin = plugin;
    }

    @Override
    public void load() {
        Bukkit.getScheduler().runTaskTimer(plugin, this::update, 1, 1);
        ticks = 0;
    }

    @Override
    public void save() {

    }

    private void update() {
        plugin.getPlayers().forEach(BilliardsPlayer::update);

        ticks++;
    }
}
