package me.asleepp.SkriptItemsAdder.elements.types;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.lang.DefaultExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.Classes;
import ch.njol.util.Checker;
import ch.njol.util.Kleenean;
import ch.njol.yggdrasil.Fields;
import dev.lone.itemsadder.api.FontImages.TexturedInventoryWrapper;
import me.asleepp.SkriptItemsAdder.SkriptItemsAdder;
import me.asleepp.SkriptItemsAdder.other.AliasesGenerator;
import me.asleepp.SkriptItemsAdder.other.CustomItemType;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Types {

    public static Map<String, TexturedInventoryWrapper> inventoryMap = new HashMap<>();

    static {
        Classes.registerClass(new ClassInfo<>(CustomItemType.class, "customitemtype")
                .user("customitemtypes?")
                .name("Custom Item Type")
                .description("Represents a custom item type using aliases.")
                .serializer(new Serializer<CustomItemType>() {
                    @Override
                    public Fields serialize(CustomItemType customItemType) {
                        Fields fields = new Fields();
                        fields.putObject("namespacedID", customItemType.getNamespacedID());
                        return fields;
                    }

                    @Override
                    public void deserialize(CustomItemType customItemType, Fields fields) {
                        try {
                            customItemType.setNamespacedID((String) fields.getObject("namespacedID"));
                        } catch (java.io.StreamCorruptedException e) {
                            e.printStackTrace();
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
                    public CustomItemType parse(String s, ParseContext context) {
                        AliasesGenerator aliasesGenerator = SkriptItemsAdder.getInstance().getAliasesGenerator();
                        String namespacedID = aliasesGenerator.getNamespacedId(s.toLowerCase().replace("_", " "));
                        return namespacedID == null ? null : new CustomItemType(namespacedID);
                    }

                    @Override
                    public String toString(CustomItemType customItemType, int flags) {
                        return customItemType.getNamespacedID();
                    }

                    @Override
                    public String toVariableNameString(CustomItemType customItemType) {
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
                    public TexturedInventoryWrapper parse(String s, ParseContext context) {
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
                    public String toVariableNameString(TexturedInventoryWrapper texturedInventoryWrapper) {
                        return toString(texturedInventoryWrapper, 0);
                    }

                    public String getVariableNamePattern() {
                        return ".+";
                    }
                }));
    }
}

