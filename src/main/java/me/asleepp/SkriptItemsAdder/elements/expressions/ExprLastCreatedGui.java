package me.asleepp.SkriptItemsAdder.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.FontImages.TexturedInventoryWrapper;
import me.asleepp.SkriptItemsAdder.elements.sections.SecCreateCustomInventory;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class ExprLastCreatedGui extends SimpleExpression<TexturedInventoryWrapper> {

    static {
        Skript.registerExpression(ExprLastCreatedGui.class, TexturedInventoryWrapper.class, ExpressionType.SIMPLE, "last created [custom] gui");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    @Nullable
    protected TexturedInventoryWrapper[] get(Event event) {
        if (SecCreateCustomInventory.lastCreatedGui != null) {
            return new TexturedInventoryWrapper[]{SecCreateCustomInventory.lastCreatedGui};
        } else {
            return null;
        }
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
        return "last created gui";
    }
}

