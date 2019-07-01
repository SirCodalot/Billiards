package me.codalot.billiards.player.components;

import me.codalot.billiards.player.BilliardsPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

public class ChargeBar {

    private BilliardsPlayer player;

    private BossBar bar;

    public ChargeBar(BilliardsPlayer player) {
        this.player = player;
        bar = Bukkit.createBossBar(ChatColor.GOLD + "Draw Your Cue", BarColor.YELLOW, BarStyle.SOLID);

        bar.setProgress(0);
        bar.addPlayer(player.getPlayer());
    }

    public void remove() {
        bar.removePlayer(player.getPlayer());
    }

    public void progress() {
        try {
            bar.setProgress(bar.getProgress() + 0.025);
        } catch (Exception e) {
            bar.setProgress(1);
        }
    }

    public double releaseAndRemove() {
        remove();
        return bar.getProgress();
    }
}
