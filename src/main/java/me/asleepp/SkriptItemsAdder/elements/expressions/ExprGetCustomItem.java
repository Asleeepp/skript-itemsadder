package me.asleepp.SkriptItemsAdder.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
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
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Name("ItemsAdder Item")
@Description({"Gets an ItemsAdder item, or items."})
@Examples({
        "give player itemsadder item \"_iainternal:icon_arrow_chest\"",
        "give player itemsadder items \"_iainternal:icon_back_orange\" and \"_iainternal:icon_cancel\""})
@Since("1.0, 1.5 (multiple items)")
@RequiredPlugins("ItemsAdder")
public class ExprGetCustomItem extends SimpleExpression<ItemType> {

    private Expression<String> itemNames;

    static {
        Skript.registerExpression(ExprGetCustomItem.class, ItemType.class, ExpressionType.SIMPLE, "(custom|ia|itemsadder) item[s] %strings%");
    }

    @Override
    protected ItemType[] get(Event e) {
        String[] names = itemNames.getAll(e);
        List<ItemType> items = new ArrayList<>();
        for (String name : names) {
            if (name != null) {
                CustomStack customStack = CustomStack.getInstance(name);
                if (customStack != null) {
                    ItemStack item = customStack.getItemStack();
                    if (item != null) {
                        items.add(new ItemType(item));
                    }
                }
            }
        }
        return items.toArray(new ItemType[0]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends ItemType> getReturnType() {
        return ItemType.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "ItemsAdder items " + itemNames.toString(e, debug);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        itemNames = (Expression<String>) exprs[0];
        return true;
    }
}
