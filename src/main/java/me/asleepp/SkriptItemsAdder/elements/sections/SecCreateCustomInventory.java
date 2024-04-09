package me.asleepp.SkriptItemsAdder.elements.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Section;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.lang.util.SimpleLiteral;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import dev.lone.itemsadder.api.FontImages.TexturedInventoryWrapper;
import me.asleepp.SkriptItemsAdder.elements.types.Types;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

import java.util.List;
import java.util.UUID;

public class SecCreateCustomInventory extends Section {

    @Nullable
    public static TexturedInventoryWrapper lastCreatedGui;
    private Expression<String> title;
    private Expression<Integer> rows;
    private Expression<String> texture;
    private Expression<Integer> titleOffset;
    private Expression<Integer> textureOffset;
    private static final EntryValidator validator = EntryValidator.builder()
            .addEntryData(new ExpressionEntryData<>("title", null, true, String.class))
            .addEntryData(new ExpressionEntryData<>("rows", new SimpleLiteral<>(3, false), true, Integer.class))
            .addEntryData(new ExpressionEntryData<>("texture", null, false, String.class)) // texture is required
            .addEntryData(new ExpressionEntryData<>("title offset", null, true, Integer.class))
            .addEntryData(new ExpressionEntryData<>("texture offset", null, true, Integer.class))
            .build();

    static {
        Skript.registerSection(SecCreateCustomInventory.class, "create [a] [new] (custom|ia|itemsadder) [chest] inventory");
    }

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult, SectionNode sectionNode, List<TriggerItem> triggerItems) {
        @Nullable EntryContainer entryContainer = validator.validate(sectionNode);
        if (entryContainer == null)
            return false;

        // Use getOptional to get the expressions
        this.title = (Expression<String>) entryContainer.getOptional("title", true);
        if (this.title == null) return false;
        this.rows = (Expression<Integer>) entryContainer.getOptional("rows", true);
        if (this.rows == null) return false;
        this.texture = (Expression<String>) entryContainer.getOptional("texture", false);
        if (this.texture == null) return false;
        this.titleOffset = (Expression<Integer>) entryContainer.getOptional("title offset", true);
        if (this.titleOffset == null) return false;
        this.textureOffset = (Expression<Integer>) entryContainer.getOptional("texture offset", true);
        if (this.textureOffset == null) return false;

        if (this.texture == null) {
            Skript.error("A texture is required for creating an ItemsAdder inventory. If you'd like to create a normal inventory, please use the Skript inventory syntax, or another addon's syntax.");
            return false;
        }

        return true;
    }


    protected void execute(Event e) {
        String title = this.title.getSingle(e);
        int rows = this.rows.getSingle(e);
        String texture = this.texture.getSingle(e);
        int titleOffset = this.titleOffset != null ? this.titleOffset.getSingle(e) : 0;
        int textureOffset = this.textureOffset != null ? this.textureOffset.getSingle(e) : 0;

        FontImageWrapper fontTexture = new FontImageWrapper(texture);
        if (fontTexture.exists()) {
            TexturedInventoryWrapper inventory = new TexturedInventoryWrapper(null, rows * 9, title, fontTexture, titleOffset, textureOffset);
            if (inventory != null) {
                String id = UUID.randomUUID().toString(); // uuid for storing inventory
                Types.inventoryMap.put(id, inventory);
                lastCreatedGui = inventory;
            }
        }
    }


    @Override
    protected @Nullable TriggerItem walk(Event event) {
        debug(event, true);
        execute(event);
        return getNext();
    }


    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "create a new itemsadder inventory";
    }
}


