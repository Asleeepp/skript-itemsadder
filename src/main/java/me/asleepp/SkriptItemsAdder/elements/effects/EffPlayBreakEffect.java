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

public class EffPlayBreakEffect extends Effect {

    private Expression<String> customBlockId;

    static {
        Skript.registerEffect(EffPlayBreakEffect.class, new String[] {"(play|make) [the] break effect on (custom|ia|itemsadder) block %string%"});
    }

    @Override
    protected void execute(Event e) {
        String customBlockId = this.customBlockId.getSingle(e);

        if (customBlockId != null) {
            CustomBlock customBlock = CustomBlock.getInstance(customBlockId);
            if (customBlock != null) {
                Block bukkitBlock = customBlock.getBlock();
                if (bukkitBlock != null) {
                    CustomBlock.playBreakEffect(bukkitBlock);
                }
            }
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "play the break effect on custom block " + customBlockId.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.customBlockId = (Expression<String>) exprs[0];
        return true;
    }
}
