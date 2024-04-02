package me.asleepp.SkriptItemsAdder.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import dev.lone.itemsadder.api.FontImages.TexturedInventoryWrapper;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EffCreateAndStoreCustomInventory extends Effect {

    private Expression<String> id;
    private Expression<String> title;
    private Expression<Integer> rows;
    private Expression<FontImageWrapper> texture;

    static {
        Skript.registerEffect(EffCreateAndStoreCustomInventory.class, "create [a] [new] (custom|ia|itemsadder) [chest] inventory (named|with name) %string% [with %-integer% row[s]] [and] [with texture %-fontimagewrapper%] (with id) %string%");
    }

    @Override
    protected void execute(Event e) {
        String id = this.id.getSingle(e);
        String title = this.title.getSingle(e);
        int rows = this.rows.getSingle(e);
        FontImageWrapper texture = this.texture.getSingle(e);
        TexturedInventoryWrapper inventory = new TexturedInventoryWrapper(null, rows * 9, title, texture, 0,0);
        Variables.setVariable(id, inventory, e, true);
    }


    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return null;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.title = (Expression<String>) exprs[0];
        this.rows = (Expression<Integer>) exprs[1];
        this.texture = (Expression<FontImageWrapper>) exprs[2];
        this.id = (Expression<String>) exprs[3];
        return true;
    }
}
