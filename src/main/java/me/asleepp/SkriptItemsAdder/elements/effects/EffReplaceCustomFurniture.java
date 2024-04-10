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
import org.bukkit.event.Event;
import dev.lone.itemsadder.api.CustomFurniture;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import javax.annotation.Nullable;

@Name("Replace Custom Furniture")
@Description({"If there is furniture at a location, this effect will remove it and place the one you specify."})
@Examples({"replace custom furniture \"furniture:comfy_chair\" at player's location"})
@Since("1.4")
public class EffReplaceCustomFurniture extends Effect {

    private Expression<String> furnitureId;
    private Expression<Location> location;

    static {
        Skript.registerEffect(EffReplaceCustomFurniture.class, "replace (custom|ia|itemsadder) furniture %string% at %location%");
    }

    @Override
    protected void execute(Event e) {
        String id = furnitureId.getSingle(e);
        Location loc = location.getSingle(e);
        if (id != null && loc != null) {
            CustomFurniture existingFurniture = CustomFurniture.byAlreadySpawned(loc.getBlock());
            if (existingFurniture != null) {
                Entity armorStand = existingFurniture.getArmorstand();
                Location originalLocation = armorStand.getLocation();
                existingFurniture.replaceFurniture(id);
                existingFurniture.teleport(originalLocation);
            }
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "replace (custom|ia|itemsadder) furniture " + furnitureId.toString(e, debug) + " at " + location.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        furnitureId = (Expression<String>) exprs[0];
        location = (Expression<Location>) exprs[1];
        return true;
    }
}
