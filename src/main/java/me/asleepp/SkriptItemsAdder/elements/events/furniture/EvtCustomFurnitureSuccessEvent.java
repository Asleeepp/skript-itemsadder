package me.asleepp.SkriptItemsAdder.elements.events.furniture;

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
import dev.lone.itemsadder.api.CustomFurniture;
import dev.lone.itemsadder.api.Events.FurniturePlaceSuccessEvent;
import me.asleepp.SkriptItemsAdder.SkriptItemsAdder;
import me.asleepp.SkriptItemsAdder.other.aliases.AliasesGenerator;
import me.asleepp.SkriptItemsAdder.other.aliases.CustomItemType;
import org.bukkit.Location;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Name("Furniture Place Success")
@Description("This event is called when a furniture is successfully placed into the world, use this event instead if you would like to know the location, etc.")
@Examples({"on place success of custom itemsadder furniture:", "on successfully placing a custom itemsadder furniture:"})
@Since("1.5")
@RequiredPlugins("ItemsAdder")
public class EvtCustomFurnitureSuccessEvent extends SkriptEvent {

    private Literal<?>[] furnitureIDs;
    private List<String> aliases;
    private AliasesGenerator aliasesGenerator = SkriptItemsAdder.getInstance().getAliasesGenerator();

    static {
        Skript.registerEvent("Custom Furniture Success", EvtCustomFurnitureSuccessEvent.class, FurniturePlaceSuccessEvent.class,
                "success[fully] plac(e|ing) [a] [custom] (ia|itemsadder) furniture [%customitemtypes/strings%]",
                "place success [of] [custom] (ia|itemsadder) furniture [%customitemtypes/strings%]");
        EventValues.registerEventValue(FurniturePlaceSuccessEvent.class, Location.class, new Getter<Location, FurniturePlaceSuccessEvent>() {
            @Override
            public Location get(FurniturePlaceSuccessEvent arg) {
                return arg.getBukkitEntity().getLocation();
            }
        }, 0);
        EventValues.registerEventValue(FurniturePlaceSuccessEvent.class, CustomFurniture.class, new Getter<CustomFurniture, FurniturePlaceSuccessEvent>() {
            @Override
            public @Nullable CustomFurniture get(FurniturePlaceSuccessEvent event) {
                return event.getFurniture();
            }
        }, 0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        furnitureIDs = args;
        if (furnitureIDs != null) {
            aliases = Arrays.stream(furnitureIDs)
                    .map(literal -> {
                        if (literal instanceof Literal) {
                            Object value = ((Literal<?>) literal).getSingle();
                            if (value instanceof CustomItemType) {
                                return ((CustomItemType) value).getNamespacedID();
                            } else if (value instanceof String) {
                                return (String) value;
                            }
                        }
                        return null;
                    })
                    .filter(name -> name != null)
                    .collect(Collectors.toList());
        }
        return true;
    }

    @Override
    public boolean check(Event e) {
        if (e instanceof FurniturePlaceSuccessEvent) {
            FurniturePlaceSuccessEvent event = (FurniturePlaceSuccessEvent) e;

            if (furnitureIDs != null && !aliases.isEmpty()) {
                String actualFurnitureName = event.getNamespacedID();
                return aliases.contains(aliasesGenerator.getNamespacedId(actualFurnitureName));
            }

            return true;
        }
        return false;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "Furniture Place Success event" + (furnitureIDs != null ? " with id " + Arrays.toString(furnitureIDs) : "");
    }
}
