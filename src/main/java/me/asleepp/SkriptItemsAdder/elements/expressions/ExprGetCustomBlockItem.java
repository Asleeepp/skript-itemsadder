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
import dev.lone.itemsadder.api.Events.CustomBlockInteractEvent;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
@Name("Get Custom Block Item")
@Description({"Gets the item associated with the custom block."})
@Examples({
    "on interact with custom itemsadder block:",
        "\tset {_t} to the item linked with the itemsadder block"})
@Since("1.4")
@RequiredPlugins("ItemsAdder")
public class ExprGetCustomBlockItem extends SimpleExpression<ItemStack> {

    static {
        Skript.registerExpression(ExprGetCustomBlockItem.class, ItemStack.class, ExpressionType.SIMPLE, "[the] item (linked|associated) with [the] [custom] (ia|itemsadder) block");
    }

    @Override
    protected ItemStack[] get(Event e) {
        if (e instanceof CustomBlockInteractEvent) {
            CustomBlockInteractEvent event = (CustomBlockInteractEvent) e;
            return new ItemStack[]{event.getCustomBlockItem().clone()};
        }
        return null;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends ItemStack> getReturnType() {
        return ItemStack.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "item linked with the block";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (!getParser().isCurrentEvent(CustomBlockInteractEvent.class)) {
            Skript.error("You can't use 'the item linked/associated with the block' outside of a custom block interact event!");
            return false;
        }
        return true;
    }

}
