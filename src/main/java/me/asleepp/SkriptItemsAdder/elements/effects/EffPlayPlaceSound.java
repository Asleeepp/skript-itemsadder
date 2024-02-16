package me.asleepp.SkriptItemsAdder.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.CustomBlock;
import org.bukkit.block.Block;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

import org.bukkit.entity.Entity;

public class EffPlayPlaceSound extends Effect {

    private Expression<String> customBlockId;
    private Expression<Entity> entities;

    static {
        Skript.registerEffect(EffPlayPlaceSound.class, new String[] {"play [the] place sound from (custom|ia|itemsadder) block %string% to %entities%"});
    }

    @Override
    protected void execute(Event e) {
        String customBlockId = this.customBlockId.getSingle(e);
        Entity[] entities = this.entities.getArray(e);

        if (customBlockId != null) {
            CustomBlock customBlock = CustomBlock.getInstance(customBlockId);
            if (customBlock != null) {
                Block bukkitBlock = customBlock.getBlock();
                if (bukkitBlock != null) {
                    for (Entity entity : entities) {
                        if (entity.getLocation().distance(bukkitBlock.getLocation()) <= 16) {
                            CustomBlock.playPlaceSound(bukkitBlock);
                        }
                    }
                }
            }
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "play the place sound from custom block " + customBlockId.toString(e, debug) + " to " + entities.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.customBlockId = (Expression<String>) exprs[0];
        this.entities = (Expression<Entity>) exprs[1];
        return true;
    }
}

