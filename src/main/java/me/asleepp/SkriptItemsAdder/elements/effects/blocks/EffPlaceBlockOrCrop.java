package me.asleepp.SkriptItemsAdder.elements.effects.blocks;

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
import me.asleepp.SkriptItemsAdder.SkriptItemsAdder;
import me.asleepp.SkriptItemsAdder.other.aliases.AliasesGenerator;
import me.asleepp.SkriptItemsAdder.other.aliases.CustomItemType;
import org.bukkit.Location;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Name("Place Custom Block/Crop")
@Description({"Places a custom block or crop at a location."})
@Examples({
        "set block at player's location to itemsadder block \"iasurvival:ruby_block\"",
        "set block at player's location to custom crop \"bestcrops:best_seed\""})
@Since("1.0, 1.5 (Placing Crops & Syntax rework)")
@RequiredPlugins("ItemsAdder")
public class EffPlaceBlockOrCrop extends Effect {
    private Expression<Location> locations;
    private Expression<?> blockNames;
    private List<String> aliases;
    private boolean isCrop;
    private AliasesGenerator aliasesGenerator = SkriptItemsAdder.getInstance().getAliasesGenerator();

    static {
        Skript.registerEffect(EffPlaceBlockOrCrop.class,
                "(set|place) [custom] (ia|itemsadder) (block|:crop) %customitemtypes/strings% [%directions% %locations%]",
                "set block at [%directions% %locations%] to [custom] (ia|itemsadder) (block|:crop) %customitemtypes/strings%");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (matchedPattern == 0) {
            blockNames = exprs[0];
            locations = Direction.combine((Expression<? extends Direction>) exprs[1], (Expression<? extends Location>) exprs[2]);
        } else {
            blockNames = exprs[2];
            locations = Direction.combine((Expression<? extends Direction>) exprs[0], (Expression<? extends Location>) exprs[1]);
        }
        if (blockNames != null) {
            aliases = Arrays.stream(blockNames.getArray(null))
                    .map(obj -> {
                        if (obj instanceof CustomItemType) {
                            return ((CustomItemType) obj).getNamespacedID();
                        } else if (obj instanceof String) {
                            return (String) obj;
                        }
                        return null;
                    })
                    .filter(name -> name != null)
                    .collect(Collectors.toList());
        }
        if (parseResult.hasTag("crop"))
            isCrop = true;
        return true;
    }

    @Override
    protected void execute(Event e) {
        if (locations == null || aliases == null || aliases.isEmpty()) {
            return;
        }

        for (Location location : locations.getArray(e)) {
            for (String alias : aliases) {
                String customBlockId = aliasesGenerator.getNamespacedId(alias);

                if (customBlockId == null) {
                    Skript.error("Invalid custom block ID: " + alias);
                    continue;
                }

                Skript.info("Attempting to place custom block/crop with ID: " + customBlockId + " at location: " + location);

                try {
                    if (isCrop) {
                        try {
                            CustomCrop.place(customBlockId, location);
                            Skript.info("Placed custom crop with ID: " + customBlockId + " at location: " + location);
                        } catch (Exception ex) {
                            Skript.error("The ID " + customBlockId + " does not correspond to a valid crop.");
                            continue;
                        }
                    } else {
                        if (CustomBlock.getInstance(customBlockId) == null) {
                            Skript.error("The ID " + customBlockId + " does not correspond to a valid block.");
                            continue;
                        }

                        CustomBlock existingBlock = CustomBlock.byAlreadyPlaced(location.getBlock());
                        if (existingBlock != null) {
                            existingBlock.remove();
                        }
                        CustomBlock block = CustomBlock.getInstance(customBlockId);
                        if (block != null) {
                            block.place(location);
                            Skript.info("Placed custom block with ID: " + customBlockId + " at location: " + location);
                        } else {
                            Skript.error("Failed to get CustomBlock instance for ID: " + customBlockId);
                        }
                    }
                } catch (Exception ex) {
                    Skript.error("An error occurred while placing custom block/crop: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        String itemTypeString = blockNames.toString(e, debug);
        String type = isCrop ? "crop" : "block";
        return "place custom " + type + " " + itemTypeString + " at " + locations.toString(e, debug);
    }
}
