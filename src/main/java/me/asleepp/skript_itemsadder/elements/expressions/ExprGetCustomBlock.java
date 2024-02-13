package me.asleepp.skript_itemsadder.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.CustomBlock;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class ExprGetCustomBlock extends SimpleExpression<CustomBlock> {

    private Expression<String> blockName;

    static {
        Skript.registerExpression(ExprGetCustomBlock.class, CustomBlock.class, ExpressionType.SIMPLE, "(custom|ia|itemsadder) block %string%");
    }

    @Override
    protected CustomBlock[] get(Event e) {
        String name = blockName.getSingle(e);
        if (name != null) {
            CustomBlock customBlock = CustomBlock.getInstance(name);
            if (customBlock != null) {
                return new CustomBlock[]{customBlock};
            }
        }
        return new CustomBlock[0];
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends CustomBlock> getReturnType() {
        return CustomBlock.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "ItemsAdder block " + blockName.toString(e, debug);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        blockName = (Expression<String>) exprs[0];
        return true;
    }
}
