package me.asleepp.SkriptItemsAdder.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomCrop;

import javax.annotation.Nullable;

public class ExprGetLoot extends SimpleExpression<ItemStack> {

    private Expression<Block> blocks;

    static {
        Skript.registerExpression(ExprGetLoot.class, ItemStack.class, ExpressionType.SIMPLE, "[the] loot [of] [custom] (ia|itemsadder) %blocks%");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        blocks = (Expression<Block>) exprs[0];
        return true;
    }
    @Nullable
    @Override
    protected ItemStack[] get(Event event) {
        Block[] bs = blocks.getArray(event);
        List<ItemStack> loot = null;
        for (Block b : bs) {
            CustomBlock customBlock = CustomBlock.byAlreadyPlaced(b);
            if (customBlock != null) {
                loot = customBlock.getLoot();
            } else {
                CustomCrop customCrop = CustomCrop.byAlreadyPlaced(b);
                if (customCrop != null) {
                    loot = customCrop.getLoot();
                }
            }
        }
        return loot != null ? loot.toArray(new ItemStack[0]) : null;
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends ItemStack> getReturnType() {
        return ItemStack.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "loot of " + blocks.toString(event, debug);
    }
}
