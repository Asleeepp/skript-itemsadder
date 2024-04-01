package me.asleepp.SkriptItemsAdder.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import dev.lone.itemsadder.api.FontImages.TexturedInventoryWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EffSetPlayerInventoryTexture extends Effect {

    private Expression<Player> player;
    private Expression<FontImageWrapper> texture;

    static {
        Skript.registerEffect(EffSetPlayerInventoryTexture.class, "(set|replace) texture of %players%'[s] [custom|ia|itemsadder] inventory to %fontimagewrapper%");
    }

    @Override
    protected void execute(Event e) {
        Player player = this.player.getSingle(e);
        FontImageWrapper texture = this.texture.getSingle(e);
        if (player != null && texture != null) {
            TexturedInventoryWrapper.setPlayerInventoryTexture(player, texture);
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "set texture of " + player.toString(e, debug) + "'s inventory to " + texture.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.player = (Expression<Player>) exprs[0];
        this.texture = (Expression<FontImageWrapper>) exprs[1];
        return true;
    }
}
