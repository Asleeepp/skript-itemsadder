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
import me.asleepp.SkriptItemsAdder.other.CustomItemType;
import org.bukkit.Location;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

@Name("Place Custom Block/Crop")
@Description({"Places a custom block or crop at a location."})
@Examples({
        "set block at player's location to itemsadder block \"iasurvival:ruby_block\"",
        "set block at player's location to custom crop \"bestcrops:best_seed\""})
@Since("1.0, 1.5 (Placing Crops & Syntax rework)")
@RequiredPlugins("ItemsAdder")
public class EffPlaceBlockOrCrop extends Effect {
    private Expression<Location> locations;
    private Expression<Object> customBlockIdExpr;
    private boolean isCrop;

    static {
        Skript.registerEffect(EffPlaceBlockOrCrop.class, "(set|place) [custom] (ia|itemsadder) (block|:crop) %customitemtypes/strings% [%directions% %locations%]");
    }

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        customBlockIdExpr = (Expression<Object>) expressions[0];
        locations = Direction.combine((Expression<? extends Direction>) expressions[1], (Expression<? extends Location>) expressions[2]);
        if (parseResult.regexes.size() > 0 && parseResult.regexes.get(0).group().equals("crop"))
            isCrop = true;
        return true;
    }

    @Override
    protected void execute(Event e) {
        List<Object> customBlockIds = Arrays.asList(customBlockIdExpr.getArray(e));

        if (locations == null || customBlockIds.isEmpty()) {
            return;
        }

        for (Location location : locations.getArray(e)) {
            for (Object customBlockIdObj : customBlockIds) {
                String customBlockId;
                if (customBlockIdObj instanceof CustomItemType) {
                    customBlockId = ((CustomItemType) customBlockIdObj).getNamespacedID();
                } else if (customBlockIdObj instanceof String) {
                    customBlockId = (String) customBlockIdObj;
                } else {
                    continue;
                }

                try {
                    if (isCrop) {
                        CustomCrop existingCrop = CustomCrop.byAlreadyPlaced(location.getBlock());
                        if (existingCrop == null) {
                            CustomCrop crop = CustomCrop.place(customBlockId, location);
                        } else {
                            Skript.error("There is already a crop here!");
                        }
                    } else {
                        CustomBlock existingBlock = CustomBlock.byAlreadyPlaced(location.getBlock());
                        CustomBlock block = CustomBlock.getInstance(customBlockId);
                        if (block != null) {
                            if (existingBlock == null) {
                                existingBlock.remove();
                            }
                            block.place(location);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        String itemTypeString = customBlockIdExpr.toString(e, debug);
        String type = isCrop ? "crop" : "block";
        return "place custom " + type + " " + itemTypeString + " at " + locations.toString(e, debug);
    }
}
