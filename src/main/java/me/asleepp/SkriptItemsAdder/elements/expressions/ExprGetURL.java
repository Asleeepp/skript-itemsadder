package me.asleepp.SkriptItemsAdder.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.Events.ResourcePackSendEvent;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
@Name("Get URL of Resource Pack")
@Description({"Gets the URL of a resource pack."})
@Examples({"set {_t} to resource pack url"})
@Since("1.0")
public class ExprGetURL extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprGetURL.class, String.class, ExpressionType.SIMPLE, "[the] [(ia|itemsadder)] (texture|resource) pack (url|URl)");
    }

    @Override
    protected String[] get(Event e) {
        if (e instanceof ResourcePackSendEvent) {
            ResourcePackSendEvent rpEvent = (ResourcePackSendEvent) e;
            return new String[]{rpEvent.getUrl()};
        }
        return null;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "resource pack url";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }
}
