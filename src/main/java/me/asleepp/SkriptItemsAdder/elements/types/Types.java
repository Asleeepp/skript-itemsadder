package me.asleepp.SkriptItemsAdder.elements.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import dev.lone.itemsadder.api.FontImages.TexturedInventoryWrapper;

import java.util.HashMap;
import java.util.Map;

public class Types {

    public static Map<String, TexturedInventoryWrapper> inventoryMap = new HashMap<>();

    static {
        Classes.registerClass(new ClassInfo<>(CustomBlock.class, "customblock")
                .user("customblock")
                .name("Custom Block")
                .description("Represents a Custom/ItemsAdder Block")
                .defaultExpression(new EventValueExpression<>(CustomBlock.class))
                .since("1.5")
                .parser(new Parser<CustomBlock>() {

                    @Override
                    public String toString(CustomBlock customBlock, int flags) {
                        return customBlock.getId();
                    }

                    @Override
                    public String toVariableNameString(CustomBlock customBlock) {
                        return "customblock:" + customBlock.getId();
                    }

                    @Override
                    public CustomBlock parse(String s, ParseContext context) {
                        return CustomBlock.getInstance(s);
                    }

                    @Override
                    public boolean canParse(ParseContext context) {
                        return true;
                    }
                }));

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

