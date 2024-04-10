package me.asleepp.SkriptItemsAdder.elements.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
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

@Name("Create Custom Inventory")
@Description({
        "Creates a new ItemsAdder inventory with the following properties:",
        "- Title: The title of the inventory. This will be displayed at the top of the inventory GUI.",
        "- Rows: The number of rows in the inventory. Each row corresponds to 9 slots, so an inventory with 3 rows would have 26 slots. You start at slot 0, so thats why its 26, not 27.)",
        "- Texture: The texture of the inventory. This should be the name of a texture defined in your ItemsAdder configuration. You may also have multple strings/textures here.",
        "- Title Offset: The horizontal offset of the title in pixels. This can be used to adjust the position of the title relative to the top of the inventory GUI.",
        "- Texture Offset: The horizontal offset of the texture in pixels. This can be used to adjust the position of the texture relative to the top of the inventory GUI.",
        "The last created inventory can be accessed using the 'Last Created Gui' expression.",
        "To create an Inventory, you need a Texture, everything else is optional. See Examples for a demonstration."
})
@Examples({
    "create a new custom inventory:",
        "\ttitle: \"Shop\"",
        "\trows: 5",
        "\ttexture: \"inventory:shop\" and \"inventory:shop_extras\"",
        "\ttitle offset: 150",
        "\ttexture offset: 0",
    "set {_shop} to last created ia gui",
    "show custom {_gui} to all players"})
@Since("1.5")
@RequiredPlugins("ItemsAdder")
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
        String textures[] = this.texture.getArray(e);
        int titleOffset = this.titleOffset != null ? this.titleOffset.getSingle(e) : 0;
        int textureOffset = this.textureOffset != null ? this.textureOffset.getSingle(e) : 0;

        FontImageWrapper[] fontTextures = new FontImageWrapper[textures.length];
        for (int i = 0; i < textures.length; i++) {
            fontTextures[i] = new FontImageWrapper(textures[i]);
            if (!fontTextures[i].exists()) {
                Skript.error("The specified texture does not exist: " + textures[i]);
                return;
            }
        }

        TexturedInventoryWrapper inventory = new TexturedInventoryWrapper(null, rows * 9, title, titleOffset, textureOffset, fontTextures);
        if (inventory != null) {
            String id = UUID.randomUUID().toString(); // uuid for storing inventory
            Types.inventoryMap.put(id, inventory);
            lastCreatedGui = inventory;
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


