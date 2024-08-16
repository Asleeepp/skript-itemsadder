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
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.CustomBlock;
import me.asleepp.SkriptItemsAdder.SkriptItemsAdder;
import me.asleepp.SkriptItemsAdder.other.aliases.AliasesGenerator;
import me.asleepp.SkriptItemsAdder.other.util.Util;
import org.bukkit.Location;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Name("Set Custom Blocks Within")
@Description({"Sets the blocks within 2 locations to ItemsAdder custom blocks."})
@Examples({"set all blocks within location(0, 100, 0) and player's location to itemsadder block \"iasurvival:ruby_block\""})
@Since("1.0")
@RequiredPlugins("ItemsAdder")
public class EffSetBlocksBetween extends Effect {
    private Expression<Location> location1Expr;
    private Expression<Location> location2Expr;
    private Expression<?> customBlockIdExpr;
    private List<String> aliases;
    private AliasesGenerator aliasesGenerator = SkriptItemsAdder.getInstance().getAliasesGenerator();

    static {
        Skript.registerEffect(EffSetBlocksBetween.class, "(set|place) [all] blocks within %location% and %location% to [custom] (ia|itemsadder) block %customitemtypes/strings%");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        location1Expr = (Expression<Location>) exprs[0];
        location2Expr = (Expression<Location>) exprs[1];
        customBlockIdExpr = exprs[2];
        aliases = Arrays.stream(exprs)
                .filter(expr -> expr instanceof Expression)
                .map(expr -> {
                    Object value = ((Expression<?>) expr).getSingle(null);
                    return Util.getCustomBlockId(value);
                })
                .filter(name -> name != null)
                .collect(Collectors.toList());
        return true;
    }

    @Override
    protected void execute(Event e) {
        Location location1 = location1Expr.getSingle(e);
        Location location2 = location2Expr.getSingle(e);
        Object customBlockIdObj = customBlockIdExpr.getSingle(e);
        String customBlockId = Util.getCustomBlockId(customBlockIdObj);

        if (location1 == null || location2 == null || customBlockId == null) {
            return;
        }

        int minX = Math.min(location1.getBlockX(), location2.getBlockX());
        int minY = Math.min(location1.getBlockY(), location2.getBlockY());
        int minZ = Math.min(location1.getBlockZ(), location2.getBlockZ());
        int maxX = Math.max(location1.getBlockX(), location2.getBlockX());
        int maxY = Math.max(location1.getBlockY(), location2.getBlockY());
        int maxZ = Math.max(location1.getBlockZ(), location2.getBlockZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Location location = new Location(location1.getWorld(), x, y, z);
                    CustomBlock customBlock = CustomBlock.getInstance(customBlockId);
                    if (customBlock != null) {
                        customBlock.place(location);
                    }
                }
            }
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "place custom block " + customBlockIdExpr.toString(e, debug) + " between locations " + location1Expr.toString(e, debug) + " and " + location2Expr.toString(e, debug);
    }
}
