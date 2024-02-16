package me.asleepp.SkriptItemsAdder.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.CustomEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EffAddPassengerToCustomEntity extends Effect {

    private Expression<Entity> entities;
    private Expression<Entity> customEntityId;

    static {
        Skript.registerEffect(EffAddPassengerToCustomEntity.class, new String[] {"add %entities% to passenger[s] of (custom|ia|itemsadder) entity %entity%"});
    }

    @Override
    protected void execute(Event e) {
        Entity[] entities = this.entities.getArray(e);
        Entity bukkitEntity = this.customEntityId.getSingle(e);

        if (bukkitEntity != null) {
            CustomEntity customEntity = CustomEntity.byAlreadySpawned(bukkitEntity);
            if (customEntity != null) {
                Entity customBukkitEntity = customEntity.getEntity();
                if (customBukkitEntity instanceof LivingEntity) {
                    LivingEntity customLivingEntity = (LivingEntity) customBukkitEntity;
                    for (Entity entity : entities) {
                        if (entity instanceof LivingEntity) {
                            customLivingEntity.addPassenger((LivingEntity) entity);
                        }
                    }
                }
            }
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "add " + entities.toString(e, debug) + " to passengers of custom entity " + customEntityId.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.entities = (Expression<Entity>) exprs[0];
        this.customEntityId = (Expression<Entity>) exprs[1];
        return true;
    }
}
