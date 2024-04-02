package me.asleepp.SkriptItemsAdder.elements.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import dev.lone.itemsadder.api.FontImages.TexturedInventoryWrapper;

public class Types {

    static {
        Classes.registerClass(new ClassInfo<>(CustomBlock.class, "customblock")
                .user("customblock")
                .name("Custom Block")
                .description("Represents a Custom Block from ItemsAdder")
                .defaultExpression(new EventValueExpression<>(CustomBlock.class))
                .since("1.4.2")
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
        Classes.registerClass(new ClassInfo<>(FontImageWrapper.class, "fontimagewrapper")
                .user("font ?image ?wrappers?")
                .name("Font Image Wrapper")
                .defaultExpression(new EventValueExpression<>(FontImageWrapper.class))
                .description("Represents a font image wrapper from the ItemsAdder API.")
                .since("1.0")
                .parser(new Parser<FontImageWrapper>() {

                    @Override
                    public String toString(FontImageWrapper fontImageWrapper, int flags) {
                        return fontImageWrapper.getNamespacedID();
                    }

                    @Override
                    public FontImageWrapper parse(String s, ParseContext context) {
                        return new FontImageWrapper(s);
                    }

                    @Override
                    public boolean canParse(ParseContext context) {
                        return true;
                    }

                    @Override
                    public String toVariableNameString(FontImageWrapper fontImageWrapper) {
                        return toString(fontImageWrapper, 0);
                    }


                    public String getVariableNamePattern() {
                        return ".+";
                    }
                }));
    }
}

