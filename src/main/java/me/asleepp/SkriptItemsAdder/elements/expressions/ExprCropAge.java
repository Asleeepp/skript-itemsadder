package me.asleepp.SkriptItemsAdder.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.CustomCrop;
import jdk.jfr.Description;
import jdk.jfr.Name;
import org.bukkit.block.Block;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Crop Age")
@Description("Get or Set the age of a crop, or get the maximum age of one.")
@Examples("send maximum age of custom itemsadder block {_crop}")
@Since("1.6")
@RequiredPlugins("ItemsAdder")

public class ExprCropAge extends SimpleExpression<Number> {

    private Expression<Block> block;
    private boolean maxAge;

    static {
        Skript.registerExpression(ExprCropAge.class, Number.class, ExpressionType.SIMPLE,
                "[the] [:max[imum]] age of [custom] (ia|itemsadder) %blocks%",
                "[custom] (ia|itemsadder) %blocks%'[s] [:max[imum]] age");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        block = (Expression<Block>) exprs[0];
        maxAge = parseResult.hasTag("max");
        return true;
    }

    @Nullable
    @Override
    protected Number[] get(Event e) {
        Block[] bs = block.getArray(e);
        Number[] ages = new Number[bs.length];
        for (int i = 0; i < bs.length; i++) {
            CustomCrop crop = CustomCrop.byAlreadyPlaced(bs[i]);
            if (crop != null) {
                ages[i] = maxAge ? crop.getMaxAge() : crop.getAge();
            }
        }
        return ages;
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "age of " + block.toString(e, debug);
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.ADD || mode == Changer.ChangeMode.REMOVE)
            return new Class[]{Number.class};
        return null;
    }

    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
        if (delta != null && delta.length != 0) {
            int age = ((Number) delta[0]).intValue();
            Block[] bs = block.getArray(e);
            for (Block b : bs) {
                CustomCrop crop = CustomCrop.byAlreadyPlaced(b);
                if (crop != null) {
                    switch (mode) {
                        case SET:
                            crop.setAge(age);
                            break;
                        case ADD:
                            crop.setAge(crop.getAge() + age);
                            break;
                        case REMOVE:
                            crop.setAge(crop.getAge() - age);
                            break;
                    }
                }
            }
        }
    }
}
