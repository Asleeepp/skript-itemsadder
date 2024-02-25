package me.asleepp.SkriptItemsAdder.elements.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.Skript;
import dev.lone.itemsadder.api.Events.CustomBlockInteractEvent;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
@Name("Get Block Clicked")
@Description({"Gets the block clicked."})
@Examples({"on interact with custom block: /tset {_t} to clicked custom block "})
@Since("1.4")
public class ExprGetCustomBlockClicked extends SimplePropertyExpression<CustomBlockInteractEvent, Block> {

    static {
        Skript.registerExpression(ExprGetCustomBlockClicked.class, Block.class, ExpressionType.PROPERTY, "[the] clicked (custom|ia|itemsadder) block");
    }

    @Override
    public Block convert(CustomBlockInteractEvent event) {
        return event.getBlockClicked();
    }

    @Override
    protected String getPropertyName() {
        return "clicked block";
    }

    @Override
    public Class<? extends Block> getReturnType() {
        return Block.class;
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return null; // expression can't be changed
    }

    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
        // expression can't be changed
    }
}
