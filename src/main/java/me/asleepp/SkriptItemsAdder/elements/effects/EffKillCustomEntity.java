package me.asleepp.SkriptItemsAdder.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.CustomEntity;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

import org.bukkit.entity.Entity;

public class EffKillCustomEntity extends Effect {

    private Expression<Entity> customEntityId;

    static {
        Skript.registerEffect(EffKillCustomEntity.class, new String[] {"kill (custom|ia|itemsadder) entity %entity%"});
    }

    @Override
    protected void execute(Event e) {
        Entity bukkitEntity = this.customEntityId.getSingle(e);

        if (bukkitEntity != null) {
            CustomEntity customEntity = CustomEntity.byAlreadySpawned(bukkitEntity);
            if (customEntity != null) {
                customEntity.destroy();
            }
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "kill custom entity " + customEntityId.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.customEntityId = (Expression<Entity>) exprs[0];
        return true;
    }
}

