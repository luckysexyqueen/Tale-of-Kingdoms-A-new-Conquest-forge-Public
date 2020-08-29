package net.islandearth.taleofkingdoms.common.listener;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.common.event.tok.KingdomStartCallback;
import net.islandearth.taleofkingdoms.managers.SoundManager;
import net.minecraft.sound.SoundCategory;

public class KingdomListener extends Listener {

    public KingdomListener() {
        KingdomStartCallback.EVENT.register((player, instance) -> {
            SoundManager soundManager = (SoundManager) TaleOfKingdoms.getAPI().get().getManager("Sound Manager");
            player.playSound(soundManager.getSound(SoundManager.TOKSound.TOKTHEME), SoundCategory.MASTER,1, 1);
        });
    }
}
