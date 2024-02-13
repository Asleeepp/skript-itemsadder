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
import dev.lone.itemsadder.api.CustomEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
@Name("Make Entity Play Animation")
@Description({"Makes an ItemsAdder entity play an animation."})
@Examples({
        "on right click:",
        "\tif clicked entity is a custom entity",
        "\t\tmake event-entity play animation \"default_dance\""})
@Since("1.0")
public class EffMakeEntityPlayAnimation extends Effect {

    private Expression<Entity> entityExpr;
    private Expression<String> animationIdExpr;


    static {
        Skript.registerEffect(EffMakeEntityPlayAnimation.class, new String[] {"(make|force) %entities% [to] play (anim|animation) %string%"});
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        entityExpr = (Expression<Entity>) exprs[0];
        animationIdExpr = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        Entity[] entities = entityExpr.getArray(e);
        String animationId = animationIdExpr.getSingle(e);

        if (entities == null || animationId == null) {
            return;
        }

        for (Entity entity : entities) {
            CustomEntity customEntity = CustomEntity.byAlreadySpawned(entity);
            if (customEntity != null) {
                customEntity.playAnimation(animationId);
            }
        }
    }


    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "make " + entityExpr.toString(e, debug) + " play animation " + animationIdExpr.toString(e, debug);
    }


}
