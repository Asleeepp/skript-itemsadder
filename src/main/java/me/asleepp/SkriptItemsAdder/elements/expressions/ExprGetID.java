package me.asleepp.SkriptItemsAdder.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomEntity;
import dev.lone.itemsadder.api.CustomStack;
import jdk.jfr.Description;
import jdk.jfr.Name;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Name("Get ID")
@Description("Gets the Namespaced:ID of your blocks, furnitures, or items.")
@Examples("send id of itemsadder item player's tool")
@Since("1.6")
@RequiredPlugins("ItemsAdder")
public class ExprGetID extends SimpleExpression<String> {

    private Expression<?> expr;

    static {
        Skript.registerExpression(ExprGetID.class, String.class, ExpressionType.SIMPLE, "id of [custom] (ia|itemsadder) [item|block|furniture] %itemstacks/blocks/entities%");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        expr = exprs[0];
        return true;
    }

    @Override
    protected String[] get(Event event) {
        Object[] objects = expr.getArray(event);
        List<String> ids = new ArrayList<>();
        for (Object object : objects) {
            if (object instanceof ItemStack) {
                CustomStack customStack = CustomStack.byItemStack((ItemStack) object);
                if (customStack != null) {
                    ids.add(customStack.getNamespacedID());
                }
            } else if (object instanceof Block) {
                CustomBlock customBlock = CustomBlock.byAlreadyPlaced((Block) object);
                if (customBlock != null) {
                    ids.add(customBlock.getNamespacedID());
                }
            } else if (object instanceof Entity) {
                CustomEntity customEntity = CustomEntity.byAlreadySpawned((Entity) object);
                if (customEntity != null) {
                    ids.add(customEntity.getNamespacedID());
                }
            }
        }
        return ids.toArray(new String[0]);
    }

    @Override
    public boolean isSingle() {
        return expr.isSingle();
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "id of [custom] (ia|itemsadder) " + expr.toString(e, debug);
    }
}
