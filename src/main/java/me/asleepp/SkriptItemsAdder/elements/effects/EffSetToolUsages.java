package me.asleepp.SkriptItemsAdder.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

@Name("Set Usages Of Tool")
@Description({"Set the usages of your tool"})
@Examples({"set usages of player's tool to 2"})
@Since("1.3")
public class EffSetToolUsages extends Effect {
    private Expression<ItemStack> tool;
    private Expression<Integer> usages;


    static {
        Skript.registerEffect(EffSetToolUsages.class, "set usages of %itemstack% to %integer%");
    }

    @Override
    protected void execute(Event e) {
        ItemStack tool = this.tool.getSingle(e);
        Integer usages = this.usages.getSingle(e);

        if (tool != null && usages != null) {
            CustomStack customStack = CustomStack.byItemStack(tool);
            if (customStack != null) {
                customStack.setUsages(usages);
            }
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "set usages of " + tool.toString(e, debug) + " to " + usages.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        tool = (Expression<ItemStack>) exprs[0];
        usages = (Expression<Integer>) exprs[1];
        return true;
    }
}
