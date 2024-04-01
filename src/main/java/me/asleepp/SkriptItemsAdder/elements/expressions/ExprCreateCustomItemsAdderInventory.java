package me.asleepp.SkriptItemsAdder.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import dev.lone.itemsadder.api.FontImages.TexturedInventoryWrapper;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class ExprCreateCustomItemsAdderInventory extends SimpleExpression<TexturedInventoryWrapper> {

    private Expression<String> title;
    private Expression<Integer> rows;
    private Expression<FontImageWrapper> texture;


    static {
        Skript.registerExpression(ExprCreateCustomItemsAdderInventory.class, TexturedInventoryWrapper.class, ExpressionType.SIMPLE,
                "[a] [new] (custom|ia|itemsadder) [chest] inventory (named|with name) %string% [with %-integer% row[s]] [and] [with texture %-fontimagewrapper%]",
                        "[a] [new] (custom|ia|itemsadder) [chest] inventory with %integer% row[s] [(named|with name) %-string%] [with texture %-fontimagewrapper%]" );
        // make font image wrapper type later
    }


    @Override
    protected @Nullable TexturedInventoryWrapper[] get(Event e) {
        String title = this.title.getSingle(e);
        int rows = this.rows.getSingle(e);
        FontImageWrapper texture = this.texture.getSingle(e);
        TexturedInventoryWrapper inventory = new TexturedInventoryWrapper(null, rows * 9, title, texture, 0,0);
        return new TexturedInventoryWrapper[]{inventory};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends TexturedInventoryWrapper> getReturnType() {
        return TexturedInventoryWrapper.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "create a new custom inventory named " + title.toString(e, debug) + " with " + rows.toString(e, debug) + " rows and texture " + texture.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.title = (Expression<String>) exprs[matchedPattern];
        this.rows = (Expression<Integer>) exprs[matchedPattern ^ 1];
        this.texture = (Expression<FontImageWrapper>) exprs[matchedPattern ^ 2];
        return true;
    }
}
