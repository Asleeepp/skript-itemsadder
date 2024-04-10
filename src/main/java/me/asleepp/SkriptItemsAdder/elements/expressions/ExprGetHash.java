package me.asleepp.SkriptItemsAdder.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.Events.ResourcePackSendEvent;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Get Hash of Resource Pack")
@Description({"Gets the hash of a resource pack."})
@Examples({"set {_t} to resource pack hash"})
@Since("1.0")
@RequiredPlugins("ItemsAdder")
public class ExprGetHash extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprGetHash.class, String.class, ExpressionType.SIMPLE, "[the] [ia|itemsadder] (texture|resource) pack hash");
    }

    @Override
    protected String[] get(Event e) {
        if (e instanceof ResourcePackSendEvent) {
            ResourcePackSendEvent rpEvent = (ResourcePackSendEvent) e;
            return new String[]{rpEvent.getHash()};
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
        return "resource pack hash";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }
}
