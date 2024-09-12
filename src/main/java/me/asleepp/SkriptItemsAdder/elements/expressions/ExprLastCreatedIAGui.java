package me.asleepp.SkriptItemsAdder.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.FontImages.TexturedInventoryWrapper;
import me.asleepp.SkriptItemsAdder.elements.sections.SecCreateCustomInventory;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
@Name("Last Created ItemsAdder Gui")
@Description("Gets the last created ItemsAdder inventory by the Create ItemsAdder Inventory section.")
@Examples({
    "create a new custom itemsadder inventory:",
        "\ttitle: \"Auction House\"",
        "\trows: 6",
        "\ttexture: \"inventory:auction_house\"",
    "set {_g} to last created ia gui"})
@Since("1.5")
@RequiredPlugins("ItemsAdder")
public class ExprLastCreatedIAGui extends SimpleExpression<TexturedInventoryWrapper> {

    static {
        Skript.registerExpression(ExprLastCreatedIAGui.class, TexturedInventoryWrapper.class, ExpressionType.SIMPLE, "last created [custom] (ia|itemsadder) gui");
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

