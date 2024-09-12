package me.asleepp.SkriptItemsAdder.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.CustomEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
@Name("Is ItemsAdder Entity")
@Description({"Checks if the entity is an ItemsAdder entity."})
@Examples({
    "on damage:",
        "\tif event-entity is an itemsadder entity:",
            "\t\tsend \"how could you?\" to player",
        "\telse if event-entity is not an itemsadder entity:",
            "\t\t send \"Incredible!\" to player "})
@Since("1.0, 1.5 (Negative Comparison)")
@RequiredPlugins("ItemsAdder")
public class CondIsCustomEntity extends Condition {

    private Expression<Entity> entities;
    private Expression<String> entityId;

    static {
        Skript.registerCondition(CondIsCustomEntity.class, "%entities% (is [a[n]]|are) [custom] (ia|itemsadder) entit(y|ies) [[with id] %-string%]", "%entities% (is[n't| not]) [a] [custom] (ia|itemsadder) entit(y|ies) [[with id] %-string%]");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        entities = (Expression<Entity>) exprs[0];
        entityId = (Expression<String>) exprs[1];
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public boolean check(Event e) {
        for (Entity entity : entities.getArray(e)) {
            CustomEntity customEntity = CustomEntity.byAlreadySpawned(entity);
            if (customEntity == null) {
                return isNegated();
            }
            if (entityId != null) {
                String id = entityId.getSingle(e);
                if (id == null || !customEntity.getId().equals(id)) {
                    return isNegated();
                }
            }
        }
        return !isNegated();
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return entities.toString(e, debug) + (isNegated() ? " isn't" : " is") + " a custom entity" + (entityId != null ? " with id " + entityId.toString(e, debug) : "");
    }
}