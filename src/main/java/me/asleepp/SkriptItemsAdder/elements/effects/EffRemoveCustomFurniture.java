package me.asleepp.SkriptItemsAdder.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomFurniture;
import org.bukkit.Location;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
@Name("Remove Custom Furniture")
@Description({"If there is furniture at a location, this effect will remove it."})
@Examples({"remove custom furniture at player's location"})
@Since("1.4")
@RequiredPlugins("ItemsAdder")
public class EffRemoveCustomFurniture extends Effect {

    static {
        Skript.registerEffect(EffRemoveCustomFurniture.class, "(remove|delete) [custom] (ia|itemsadder) furniture [%string%] at %locations%");
    }

    private Expression<String> furnitureId;
    private Expression<Location> locations;

    @Override
    protected void execute(Event e) {
        Location[] locs = locations.getAll(e);
        String id = furnitureId != null ? furnitureId.getSingle(e) : null;

        for (Location loc : locs) {
            if (id != null && loc != null) {
                CustomFurniture existingFurniture = CustomFurniture.byAlreadySpawned(loc.getBlock());

                if (existingFurniture != null) {
                    existingFurniture.remove(false);
                }
            }
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "remove custom furniture " + (furnitureId != null ? furnitureId.toString(e, debug) : "") + " at " + locations.toString(e, debug);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        furnitureId = (Expression<String>) exprs[0];
        locations = (Expression<Location>) exprs[1];
        return true;
    }
}
