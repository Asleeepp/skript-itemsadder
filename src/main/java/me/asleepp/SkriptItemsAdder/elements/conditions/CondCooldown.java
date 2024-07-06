package me.asleepp.SkriptItemsAdder.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

public class CondCooldown extends Condition {

    private Expression<ItemStack> itemStackExpression;

    static {
        Skript.registerCondition(CondCooldown.class, "[custom] [ia|itemsadder] %itemstacks% has [a] permission [associated [with it]]");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        itemStackExpression = (Expression<ItemStack>) exprs[0];
        return true;
    }

    @Override
    public boolean check(Event event) {
        ItemStack[] itemStacks = itemStackExpression.getArray(event);

        if (itemStacks == null) {
            return false;
        }

        for (ItemStack itemStack : itemStacks) {
            CustomStack customStack = CustomStack.byItemStack(itemStack);
            if (!(customStack == null)) {
                return customStack.hasPermission();
            }
        }

        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return itemStackExpression.toString(event, debug) + "has a permission associated with it";
    }

}
