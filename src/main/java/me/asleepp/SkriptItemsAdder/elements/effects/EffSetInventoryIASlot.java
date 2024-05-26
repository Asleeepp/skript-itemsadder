package me.asleepp.SkriptItemsAdder.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.FontImages.TexturedInventoryWrapper;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

@Name("Set Custom Inventory Slot")
@Description("Set a slot of an ItemsAdder custom inventory, works like the regular Skript syntax.")
@Examples("set slot 2 of custom {_g} to cooked beef # {_g} is the gui, stored in a variable, like Vanilla GUIs")
@Since("1.5")
@RequiredPlugins("ItemsAdder")
public class EffSetInventoryIASlot extends Effect {

    private Expression<TexturedInventoryWrapper> inventory;
    private Expression<Integer> slots;
    private Expression<ItemStack> items;

    static {
        Skript.registerEffect(EffSetInventoryIASlot.class, "set slot[s] %integers% of [custom] (ia|itemsadder) %texturedinventorywrapper% to %itemstacks%");
    }

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        slots = (Expression<Integer>) expressions[0];
        inventory = (Expression<TexturedInventoryWrapper>) expressions[1];
        items = (Expression<ItemStack>) expressions[2];
        return true;
    }
    @Override
    protected void execute(Event e) {
        Integer[] slotArray = this.slots.getArray(e);
        TexturedInventoryWrapper inventory = this.inventory.getSingle(e);
        ItemStack[] itemArray = this.items.getArray(e);
        for (int i = 0; i < slotArray.length && i < itemArray.length; i++) {
            inventory.getInternal().setItem(slotArray[i], itemArray[i]);
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "set slot " + slots.toString(e, debug) + " of " + inventory.toString(e, debug) + " to " + items.toString(e, debug);
    }

}
