package me.asleepp.skript_itemsadder.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExprGetFontImage extends SimpleExpression<String> {
    private Expression<String> fontImageName;

    static {
        Skript.registerExpression(ExprGetFontImage.class, String.class, ExpressionType.SIMPLE, "[(font|custom|ia|itemsadder)] image %string%");
    }

    @NotNull
    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        fontImageName = (Expression<String>) exprs[0];
        return true;
    }


    @NotNull
    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "fontimage " + fontImageName.toString(e, debug);
    }

    @Nullable
    @Override
    protected String[] get(@NotNull Event e) {
        if (fontImageName == null || fontImageName.getSingle(e) == null) {
            return null;
        }
        String str = fontImageName.getSingle(e);
        FontImageWrapper fontImageWrapper = new FontImageWrapper(str);
        if (!fontImageWrapper.exists())
            return null;
        return new String[]{fontImageWrapper.getString()};
    }
}

