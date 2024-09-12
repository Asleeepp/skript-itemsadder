package me.asleepp.SkriptItemsAdder.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import dev.lone.itemsadder.api.CustomStack;
import me.asleepp.SkriptItemsAdder.aliases.CustomItemType;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Name("Is ItemsAdder Item")
@Description({"Checks if the item is an ItemsAdder item."})
@Examples({
        "if player's tool is a custom item",
        "if player's tool is a custom item \"icon_arrow_chest\""
})
@Since("1.0, 1.5 (Negative comparison)")
@RequiredPlugins("ItemsAdder")
public class CondIsCustomItem extends Condition {

    private Expression<ItemStack> items;
    private Expression<Object> ids;
    private boolean negated;

    static {
        Skript.registerCondition(CondIsCustomItem.class,
                "%itemstacks% (is [a[n]]|are) [custom] (ia|itemsadder) item[s] [[with id] %-customitemtypes/strings%]",
                "%itemstacks% (is[n't| not]) [a] [custom] (ia|itemsadder) item[s] [[with id] %-customitemtypes/strings%]");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        items = (Expression<ItemStack>) expressions[0];
        ids = (Expression<Object>) expressions[1];
        negated = matchedPattern == 1;
        return true;
    }

    @Override
    public boolean check(Event event) {
        List<String> expectedIds = Arrays.stream(ids.getArray(event))
                .map(id -> {
                    if (id instanceof CustomItemType) {
                        return ((CustomItemType) id).getNamespacedID();
                    } else if (id instanceof String) {
                        return (String) id;
                    }
                    return null;
                })
                .filter(id -> id != null)
                .collect(Collectors.toList());

        boolean allMatch = items.getArray(event).length > 0;
        for (ItemStack itemStack : items.getArray(event)) {
            CustomStack customStack = CustomStack.byItemStack(itemStack);
            boolean matches = customStack != null && expectedIds.contains(customStack.getId());
            if (negated && matches || !negated && !matches) {
                allMatch = false;
                break;
            }
        }
        return allMatch;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        String itemString = items.toString(event, debug);
        String idString = ids.toString(event, debug);
        return itemString + (negated ? " isn't" : " is") + " a custom item" + (idString != null ? " with id " + idString : "");
    }
}
