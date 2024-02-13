package me.asleepp.skript_itemsadder.elements.effects;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Description;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.Events.ResourcePackSendEvent;
import org.bukkit.event.Event;

@Name("Get Resource Pack URL")
@Description("Gets the URL of a ResourcePackSendEvent.")
public class EffGetURL extends Effect {

    private Expression<String> url;

    @Override
    protected void execute(Event event) {
        if (event instanceof ResourcePackSendEvent) {
            ResourcePackSendEvent rpEvent = (ResourcePackSendEvent) event;
            url.getSingle(event).set(rpEvent.getUrl());
        }
    }

    @Override
    public String toString(Event event, boolean b) {
        return "get resource pack url";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        if (!ScriptLoader.isCurrentEvent(ResourcePackSendEvent.class)) {
            return false;
        }
        url = (Expression<String>) expressions[0];
        return true;
    }
}
