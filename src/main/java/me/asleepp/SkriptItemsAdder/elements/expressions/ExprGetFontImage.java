package me.asleepp.SkriptItemsAdder.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Get ItemsAdder Font Image")
@Description({"Gets an ItemsAdder font image"})
@Examples({"set {_t} to font image \"laughing_emoji\""})
@Since("1.0")
public class ExprGetFontImage extends SimpleExpression<FontImageWrapper> {

    private Expression<String> namespaceAndId;

    static {
        Skript.registerExpression(ExprGetFontImage.class, FontImageWrapper.class, ExpressionType.SIMPLE,
                "[font|custom|ia|itemsadder] image [[with] id] %string%");
    }

    @Override
    protected @Nullable FontImageWrapper[] get(Event e) {
        String namespaceAndId = this.namespaceAndId.getSingle(e);
        FontImageWrapper fontImage = new FontImageWrapper(namespaceAndId);
        return new FontImageWrapper[]{fontImage};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends FontImageWrapper> getReturnType() {
        return FontImageWrapper.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "font image with id " + namespaceAndId.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.namespaceAndId = (Expression<String>) exprs[0];
        return true;
    }
}


