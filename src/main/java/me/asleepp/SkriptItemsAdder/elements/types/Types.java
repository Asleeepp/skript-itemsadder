package me.asleepp.SkriptItemsAdder.elements.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomCrop;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

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
        }

}
