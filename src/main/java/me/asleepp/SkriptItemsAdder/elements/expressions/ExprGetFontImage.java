package me.asleepp.SkriptItemsAdder.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Name("Get ItemsAdder Font Image")
@Description({"Gets ItemsAdder font image(s) with optional offset"})
@Examples({"set {_t::*} to font images \"emojis:laughing_emoji\" and \"emojis:crying_emoji\" with x offset 2"})
@Since("1.0, 1.5 (Multiple Images), 1.6 (With Offset)")
@RequiredPlugins("ItemsAdder")
public class ExprGetFontImage extends SimpleExpression<String> {
    private Expression<String> fontImageNames;
    private Expression<Integer> offset;

    static {
        Skript.registerExpression(ExprGetFontImage.class, String.class, ExpressionType.SIMPLE, "[font|custom|ia|itemsadder] image[s] %strings% [with [x] offset %-integer%]");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        fontImageNames = (Expression<String>) exprs[0];
        offset = (Expression<Integer>) exprs[1];
        return true;
    }

    @Nullable
    @Override
    protected String[] get(@NotNull Event e) {
        if (fontImageNames == null || fontImageNames.getArray(e) == null) {
            return null;
        }
        String[] strArray = fontImageNames.getArray(e);
        Integer offsetValue = offset != null ? offset.getSingle(e) : 0;
        List<String> fontImages = new ArrayList<>();
        for (String str : strArray) {
            FontImageWrapper fontImageWrapper = new FontImageWrapper(str);
            if (!fontImageWrapper.exists()) {
                continue;
            }
            fontImages.add(fontImageWrapper.applyPixelsOffset(offsetValue));
        }
        return fontImages.toArray(new String[0]);
    }

    @NotNull
    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public boolean isSingle() {
        return fontImageNames.isSingle();
    }

    @NotNull
    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "font images " + fontImageNames.toString(e, debug) + (offset != null ? " with offset " + offset.toString(e, debug) : "");
    }
}
