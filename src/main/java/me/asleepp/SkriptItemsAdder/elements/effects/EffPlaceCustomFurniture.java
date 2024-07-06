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
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import dev.lone.itemsadder.api.CustomFurniture;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;

@Name("Place Custom Furniture")
@Description({"Place a custom furniture at a location."})
@Examples({"place custom furniture \"coolfurniture:comfy_chair\" at player's location"})
@Since("1.3")
@RequiredPlugins("ItemsAdder")
public class EffPlaceCustomFurniture extends Effect {

    private Expression<String> furnitureId;
    private Expression<Location> location;
    private JavaPlugin plugin;

    static {
        Skript.registerEffect(EffPlaceCustomFurniture.class, "(set|place) [custom] (ia|itemsadder) furniture %string% at %location%");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        furnitureId = (Expression<String>) exprs[0];
        location = (Expression<Location>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        String id = furnitureId.getSingle(e);
        Location loc = location.getSingle(e);

        CustomFurniture customFurniture = CustomFurniture.byAlreadySpawned(loc.getBlock());
        if (id != null && loc != null) {
            if (customFurniture == null) {
                CustomFurniture.spawn(id, loc.getBlock());
            } else {
                Entity armorStand = customFurniture.getArmorstand();
                if (armorStand != null) {
                    Location originalLocation = armorStand.getLocation();
                    customFurniture.remove(false);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            customFurniture.teleport(originalLocation);
                        }
                    }.runTaskLater(plugin, 3);
                }
            }
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "(set|place) [custom] (ia|itemsadder) furniture " + furnitureId.toString(e, debug) + " at " + location.toString(e, debug);
    }

}
