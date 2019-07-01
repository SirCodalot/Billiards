package me.codalot.billiards.player;

import me.codalot.billiards.player.components.ChargeBar;
import me.codalot.core.files.YamlFile;
import me.codalot.core.player.CPlayer;

import java.util.UUID;

public class BilliardsPlayer extends CPlayer {

    private ChargeBar bar;

    public BilliardsPlayer(UUID uuid, YamlFile file) {
        super(uuid, file);

        bar = null;
    }

    public void update() {
        if (bar != null)
            bar.progress();
    }

    public void drawCue() {
        cancelCharge();
        bar = new ChargeBar(this);
    }

    public double releaseCue() {
        if (bar == null)
            return 0;

        double strength = bar.releaseAndRemove();
        bar = null;
        return strength;
    }

    public void cancelCharge() {
        if (bar != null)
            bar.releaseAndRemove();
    }
}
