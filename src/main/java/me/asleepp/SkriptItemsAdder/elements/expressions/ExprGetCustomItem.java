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
import dev.lone.itemsadder.api.ItemsAdder;
import me.asleepp.SkriptItemsAdder.other.CustomItemType;
import org.bukkit.Material;
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

    private Expression<Object> itemTypes;
    private Expression<ItemStack> itemStacks;
    private boolean usesItemMaterial;

    static {
        Skript.registerExpression(ExprGetCustomItem.class, ItemType.class, ExpressionType.SIMPLE,
                "[custom] (ia|itemsadder) item[s] %customitemtypes/strings%",
            "(all [[of] the]) [custom] (ia|itemsadder) item[s] [1:(that uses|using) [item[s]|material[s]] %-itemstacks%]");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (matchedPattern == 1) {
            usesItemMaterial = parseResult.mark == 1;
            if (usesItemMaterial) {
                itemStacks = (Expression<ItemStack>) exprs[0];
            } else {
                itemTypes = (Expression<Object>) exprs[0];
            }
        } else {
            itemTypes = (Expression<Object>) exprs[0];
        }
        return true;
    }

    @Override
    protected ItemType[] get(Event e) {
        List<ItemType> items = new ArrayList<>();
        if (itemTypes != null) {
            if (itemTypes.isSingle()) {
                Object itemType = itemTypes.getSingle(e);
                if (itemType != null) {
                    CustomStack customStack = getCustomStack(itemType);
                    if (customStack != null) {
                        items.add(new ItemType(customStack.getItemStack()));
                    }
                }
            } else {
                for (Object itemType : itemTypes.getArray(e)) {
                    CustomStack customStack = getCustomStack(itemType);
                    if (customStack != null) {
                        items.add(new ItemType(customStack.getItemStack()));
                    }
                }
            }
        } else if (itemStacks != null) {
            for (ItemStack itemStack : itemStacks.getArray(e)) {
                Material material = itemStack.getType();
                List<CustomStack> customStacks = ItemsAdder.getAllItems(material);
                for (CustomStack customStack : customStacks) {
                    items.add(new ItemType(customStack.getItemStack()));
                }
            }
        } else {
            List<CustomStack> customStacks = ItemsAdder.getAllItems();
            for (CustomStack customStack : customStacks) {
                items.add(new ItemType(customStack.getItemStack()));
            }
        }
        return items.toArray(new ItemType[0]);
    }

    private CustomStack getCustomStack(Object itemType) {
        if (itemType instanceof CustomItemType) {
            return CustomStack.getInstance(((CustomItemType) itemType).getNamespacedID());
        } else if (itemType instanceof String) {
            return CustomStack.getInstance((String) itemType);
        }
        return null;
    }

    @Override
    public boolean isSingle() {
        return itemTypes != null && itemTypes.isSingle();
    }

    @Override
    public Class<? extends ItemType> getReturnType() {
        return ItemType.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        if (usesItemMaterial) {
            return "ItemsAdder items that use item material " + itemStacks.toString(e, debug);
        } else {
            return "ItemsAdder items " + itemTypes.toString(e, debug);
        }
    }
}
