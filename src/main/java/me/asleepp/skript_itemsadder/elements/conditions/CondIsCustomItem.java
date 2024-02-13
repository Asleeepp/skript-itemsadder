package me.asleepp.skript_itemsadder.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import dev.lone.itemsadder.api.CustomStack;

import javax.annotation.Nullable;


public class CondIsCustomItem extends Condition {

    private Expression<ItemType> item;

    static{
        Skript.registerCondition(CondIsCustomItem.class, new String[] {"%itemtype% (is|are) [(a|an)] (custom|ia|itemsadder) item"});
    }

    @Override
    public boolean check(Event e) {
        ItemType itemType = item.getSingle(e);
        if(itemType == null)
            return false;
        ItemStack itemStack = itemType.getRandom();
        if(itemStack == null)
            return false;
        CustomStack customStack = CustomStack.byItemStack(itemStack);
        return customStack != null;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return item.toString(e, debug) + " is a custom item";
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        item = (Expression<ItemType>) exprs[0];
        return true;
    }
}
