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
    private Expression<Number> offset;
    private boolean isTitleOffset;

    static {
        Skript.registerEffect(EffSetPlayerInventoryTexture.class, "(set|replace) texture of %players%'[s] [custom|ia|itemsadder] inventory to %fontimagewrapper% [with (:title|:texture) offset %number%]");
    }

    @Override
    protected void execute(Event e) {
        Player player = this.player.getSingle(e);
        FontImageWrapper texture = this.texture.getSingle(e);
        int offset = this.offset != null ? this.offset.getSingle(e).intValue() : 0;
        if (player != null && texture != null) {
            if (isTitleOffset) {
                TexturedInventoryWrapper.setPlayerInventoryTexture(player, texture, "", offset, 0);
            } else {
                TexturedInventoryWrapper.setPlayerInventoryTexture(player, texture, "", 0, offset);
            }
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "set texture of " + player.toString(e, debug) + "'s inventory to " + texture.toString(e, debug) + " with " + (isTitleOffset ? "title" : "texture") + " offset " + (offset != null ? offset.toString(e, debug) : "0");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        player = (Expression<Player>) exprs[0];
        texture = (Expression<FontImageWrapper>) exprs[1];
        offset = (Expression<Number>) exprs[2];
        isTitleOffset = parseResult.hasTag("title");
        return true;
    }
}
