package com.convallyria.taleofkingdoms.quest.guild_captain;

import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.GuildCaptainEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.GuildGuardEntity;
import com.convallyria.taleofkingdoms.quest.Quest;

public class MiningVillage extends Quest {

    public MiningVillage() {
        super();
        this.setStartMessage("Guild Captain: My King, one of our mining towns is running wild with bandits! The guild cannot spare any more men to locate and save the mining town. Please, find the mining town and save them!");
    }

    @Override
    public Class<? extends TOKEntity> getEntity() {
        return GuildGuardEntity.class;
    }

    @Override
    public String getName() {
        return "Overrun Mining Village";
    }
}
