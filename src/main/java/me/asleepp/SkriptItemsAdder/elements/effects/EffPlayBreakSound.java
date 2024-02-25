package me.asleepp.SkriptItemsAdder.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.CustomBlock;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;


@Name("Play Break Sound")
@Description({"Play the breaking sound from a custom block to a player(s)"})
@Examples({"play the break sound from custom block \"ruby_block\" to all players"})
@Since("1.2")
public class EffPlayBreakSound extends Effect {

    private Expression<String> customBlockId;
    private Expression<Player> players;

    static {
        Skript.registerEffect(EffPlayBreakSound.class, new String[] {"play [the] break[ing] sound from (custom|ia|itemsadder) block %string% to %players%"});
    }

    @Override
    protected void execute(Event e) {
        String customBlockId = this.customBlockId.getSingle(e);
        Player[] players = this.players.getAll(e);

        if (customBlockId != null) {
            CustomBlock customBlock = CustomBlock.getInstance(customBlockId);
            if (customBlock != null) {
                Block bukkitBlock = customBlock.getBlock();
                if (bukkitBlock != null) {
                    for (Player player : players) {
                        CustomBlock.playBreakSound(bukkitBlock);
                        }
                    }
                }
            }
        }


    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "play the break sound from custom block " + customBlockId.toString(e, debug) + " to " + players.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.customBlockId = (Expression<String>) exprs[0];
        this.players = (Expression<Player>) exprs[1];
        return true;
    }
}


