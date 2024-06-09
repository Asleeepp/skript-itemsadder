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
import ch.njol.skript.util.Direction;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomCrop;
import org.bukkit.Location;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
@Name("Place Custom Block/Crop")
@Description({"Places a custom block or crop at a location."})
@Examples({
        "set block at player's location to itemsadder block \"iasurvival:ruby_block\"",
        "set block at player's location to custom crop \"bestcrops:best_seed\""})
@Since("1.0, 1.5 (Placing Crops & Syntax rework)")
@RequiredPlugins("ItemsAdder")
public class EffPlaceBlockOrCrop extends Effect {
    private Expression<Location> locations;
    private Expression<String> customBlockIdExpr;
    private boolean isCrop;

    static {
        Skript.registerEffect(EffPlaceBlockOrCrop.class, "(set|place) [custom] (ia|itemsadder) (block|:crop) %string% [%directions% %locations%]");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        customBlockIdExpr = (Expression<String>) exprs[0];
        locations = Direction.combine((Expression<? extends Direction>) exprs[1], (Expression<? extends Location>) exprs[2]);
        if (parseResult.hasTag("crop"))
            isCrop = true;
        return true;
    }

    @Override
    protected void execute(Event e) {
        String customBlockId = customBlockIdExpr.getSingle(e);

        if (locations == null || customBlockId == null) {
            return;
        }
        // TODO test this
        for (Location location : this.locations.getArray(e)) {
            if (isCrop) {
                try { // for some reason CustomCrop doesn't have getInstance
                    CustomCrop crop = CustomCrop.place(customBlockId, location);
                }
                catch (Exception exception) {
                   Skript.error("Your namespace:id does not exist.");
                }
            } else {
                CustomBlock block = CustomBlock.getInstance(customBlockId);
                if (block != null) {
                    block.place(location);
                }
            }
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "place custom " + (isCrop ? "crop " : "block ") + this.customBlockIdExpr.toString(e, debug) + " at " + this.locations.toString(e, debug);
    }
}