package me.asleepp.SkriptItemsAdder.elements.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.EnumClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.yggdrasil.Fields;
import dev.lone.itemsadder.api.FontImages.TexturedInventoryWrapper;
import me.asleepp.SkriptItemsAdder.SkriptItemsAdder;
import me.asleepp.SkriptItemsAdder.aliases.AliasesGenerator;
import me.asleepp.SkriptItemsAdder.aliases.CustomItemType;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.Action;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.StreamCorruptedException;
import java.util.HashMap;
import java.util.Map;

public class Types {

    public static Map<String, TexturedInventoryWrapper> inventoryMap = new HashMap<>();

    static {
        Classes.registerClass(new ClassInfo<>(CustomItemType.class, "customitemtype")
                .user("customitemtypes?")
                .name("Custom Item Type")
                .description("Represents a custom item type using aliases.")
                .serializer(new Serializer<CustomItemType>() {
                    private static final String FIELD_NAME = "namespacedID";

                    @Override
                    public @NotNull Fields serialize(CustomItemType customItemType) {
                        Fields fields = new Fields();
                        fields.putObject(FIELD_NAME, customItemType.getNamespacedID());
                        return fields;
                    }

                    @Override
                    public void deserialize(CustomItemType customItemType, @NotNull Fields fields) {
                        try {
                            String namespacedID = (String) fields.getObject(FIELD_NAME);
                            if (namespacedID != null) {
                                customItemType.setNamespacedID(namespacedID);
                            }
                        } catch (StreamCorruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public @Nullable CustomItemType deserialize(@NotNull Fields fields) {
                        try {
                            String namespacedID = (String) fields.getObject(FIELD_NAME);
                            return namespacedID != null ? new CustomItemType(namespacedID) : null;
                        } catch (StreamCorruptedException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }

                    @Override
                    public boolean mustSyncDeserialization() {
                        return true;
                    }

                    @Override
                    public boolean canBeInstantiated() {
                        return true;
                    }
                })
                .parser(new Parser<CustomItemType>() {
                    @Override
                    @Nullable
                    public CustomItemType parse(@NotNull String s, @NotNull ParseContext context) {
                        AliasesGenerator aliasesGenerator = SkriptItemsAdder.getInstance().getAliasesGenerator();
                        String namespacedID = aliasesGenerator.getNamespacedId(s.toLowerCase().replace("_", " "));
                        return namespacedID == null ? null : new CustomItemType(namespacedID);
                    }

                    @Override
                    public @NotNull String toString(CustomItemType customItemType, int flags) {
                        return customItemType.getNamespacedID();
                    }

                    @Override
                    public @NotNull String toVariableNameString(CustomItemType customItemType) {
                        return customItemType.getNamespacedID();
                    }

                    public String getVariableNamePattern() {
                        return ".+";
                    }
                })
        );

        Classes.registerClass(new ClassInfo<>(TexturedInventoryWrapper.class, "texturedinventorywrapper")
                .user("texturedinventorywrappers?")
                .name("Textured Inventory Wrapper")
                .description("Represents a TexturedInventoryWrapper.")
                .since("1.0")
                .parser(new Parser<TexturedInventoryWrapper>() {

                    @Override
                    public TexturedInventoryWrapper parse(@NotNull String s, @NotNull ParseContext context) {
                        return inventoryMap.get(s);
                    }

                    @Override
                    public String toString(TexturedInventoryWrapper texturedInventoryWrapper, int flags) {
                        for (Map.Entry<String, TexturedInventoryWrapper> entry : inventoryMap.entrySet()) {
                            if (entry.getValue().equals(texturedInventoryWrapper)) {
                                return entry.getKey();
                            }
                        }
                        return "";
                    }

                    @Override
                    public @NotNull String toVariableNameString(TexturedInventoryWrapper texturedInventoryWrapper) {
                        return toString(texturedInventoryWrapper, 0);
                    }

                    public String getVariableNamePattern() {
                        return ".+";
                    }
                }));
        if (Classes.getExactClassInfo(Action.class) == null) {
            Classes.registerClass(new EnumClassInfo<>(Action.class, "action", "actions")
                    .user("actions?")
                    .name("Action")
                    .description("The action taken in an event.")
                    .since("1.6"));
        }
        if (Classes.getExactClassInfo(BlockFace.class) == null) {
            Classes.registerClass(new EnumClassInfo<>(BlockFace.class, "blockface", "block faces")
                    .user("block ?faces?")
                    .name("Block faces")
                    .description("The block face clicked in an event.")
                    .since("1.6"));
        }
    }
}
