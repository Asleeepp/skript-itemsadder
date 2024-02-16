package me.asleepp.SkriptItemsAdder.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.CustomEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EffPlayDamageEffect extends Effect {

    private Expression<Entity> customEntityId;
    private Expression<Boolean> withFire;

    static {
        Skript.registerEffect(EffPlayDamageEffect.class, new String[] {"(play|make) [the] damage effect on (custom|ia|itemsadder) entity %entity% [with fire]"});
    }

    @Override
    protected void execute(Event e) {
        Entity bukkitEntity = this.customEntityId.getSingle(e);
        Boolean withFire = this.withFire != null ? this.withFire.getSingle(e) : false;

        if (bukkitEntity != null) {
            CustomEntity customEntity = CustomEntity.byAlreadySpawned(bukkitEntity);
            if (customEntity != null) {
                customEntity.playDamageEffect(withFire);
            }
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "(play|make) [the] damage effect on custom entity " + customEntityId.toString(e, debug) + (withFire != null ? " with fire" : "");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.customEntityId = (Expression<Entity>) exprs[0];
        this.withFire = (Expression<Boolean>) exprs[1];
        return true;
    }
}