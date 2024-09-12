package me.asleepp.SkriptItemsAdder.elements.effects.furniture;

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
import dev.lone.itemsadder.api.CustomFurniture;
import me.asleepp.SkriptItemsAdder.util.Util;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Name("Place Custom Furniture")
@Description({
        "Place a custom furniture at a location.",
        "If there is a furniture at the same location, this effect will replace it."
})
@Examples({"place itemsadder furniture \"coolfurniture:comfy_chair\" at player's location"})
@Since("1.3, 1.6 (Replacing)")
@RequiredPlugins("ItemsAdder")
public class EffPlaceCustomFurniture extends Effect {

    private Expression<?> furnitureIdExpr;
    private Expression<Location> locationExpr;
    private JavaPlugin plugin;

    static {
        Skript.registerEffect(EffPlaceCustomFurniture.class, "(set|place) [custom] (ia|itemsadder) furniture %customitemtypes/strings% at %location%");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        furnitureIdExpr = exprs[0];
        locationExpr = (Expression<Location>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        if (furnitureIdExpr == null || locationExpr == null) {
            return;
        }

        List<String> furnitureIds = new ArrayList<>();
        if (furnitureIdExpr.isSingle()) {
            Object itemType = furnitureIdExpr.getSingle(e);
            furnitureIds.add(Util.getCustomBlockId(itemType));
        } else {
            for (Object itemType : furnitureIdExpr.getArray(e)) {
                furnitureIds.add(Util.getCustomBlockId(itemType));
            }
        }

        Location location = locationExpr.getSingle(e);
        if (location == null) {
            return;
        }

        for (String furnitureId : furnitureIds) {
            if (furnitureId == null) {
                Skript.error("Invalid custom furniture ID.");
                continue;
            }

            CustomFurniture customFurniture = CustomFurniture.byAlreadySpawned(location.getBlock());
            if (customFurniture == null) {
                CustomFurniture.spawn(furnitureId, location.getBlock());
            } else {
                Entity armorStand = customFurniture.getArmorstand();
                if (armorStand != null) {
                    Location originalLocation = armorStand.getLocation();
                    customFurniture.remove(false);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            CustomFurniture.spawn(furnitureId, originalLocation.getBlock());
                        }
                    }.runTaskLater(plugin, 3);
                }
            }
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "(set|place) [custom] (ia|itemsadder) furniture " + furnitureIdExpr.toString(e, debug) + " at " + locationExpr.toString(e, debug);
    }

}
