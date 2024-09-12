package me.asleepp.SkriptItemsAdder.elements.events.other;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import dev.lone.itemsadder.api.Events.CustomEntityDeathEvent;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
@Name("On Custom Entity Death")
@Description({"Fires when a ItemsAdder Entity dies."})
@Examples({"on custom entity death:"})
@Since("1.0")
@RequiredPlugins("ItemsAdder")
public class EvtCustomEntityDeath extends SkriptEvent {
    private Literal<String> mobID;

    static {
        Skript.registerEvent("Custom Entity Death", EvtCustomEntityDeath.class, CustomEntityDeathEvent.class, "[custom] (ia|itemsadder) entity death [of %string%]")
                .description("Fires when a ItemsAdder Entity dies.")
                .examples("on itemsadder entity death:")
                .since("1.0")
                .requiredPlugins("ItemsAdder");
        EventValues.registerEventValue(CustomEntityDeathEvent.class, Entity.class, new Getter<Entity, CustomEntityDeathEvent>() {
            @Override
            public @Nullable Entity get(CustomEntityDeathEvent customEntityDeathEvent) {
                return customEntityDeathEvent.getEntity();
            }
        }, 0);
        EventValues.registerEventValue(CustomEntityDeathEvent.class, Location.class, new Getter<Location, CustomEntityDeathEvent>() {
            @Override
            public @Nullable Location get(CustomEntityDeathEvent customEntityDeathEvent) {
                return customEntityDeathEvent.getEntity().getLocation();
            }
        },0);
        EventValues.registerEventValue(CustomEntityDeathEvent.class, Entity.class, new Getter<Entity, CustomEntityDeathEvent>() {
            @Override
            public @Nullable Entity get(CustomEntityDeathEvent customEntityDeathEvent) {
                return customEntityDeathEvent.getKiller();
            }
        }, 0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        mobID = (Literal<String>) args[0];
        return true;
    }

    @Override
    public boolean check(Event e) {
        if (e instanceof CustomEntityDeathEvent) {
            CustomEntityDeathEvent event = (CustomEntityDeathEvent) e;
            if (mobID == null) {
                return true;
            } else {
                String id = event.getNamespacedID();
                return id.equals(mobID.getSingle(e));
            }
        }
        return false;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "ItemsAdder custom entity " + (mobID != null ? mobID.toString(e, debug) : "") + " death";
    }
}
