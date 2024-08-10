package me.asleepp.SkriptItemsAdder.elements.events.other;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.coll.CollectionUtils;
import dev.lone.itemsadder.api.Events.PlayerEmoteEndEvent;
import dev.lone.itemsadder.api.Events.PlayerEmotePlayEvent;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EvtEmote extends SkriptEvent {

    private Literal<String> id;
    private PlayerEmoteEndEvent.Cause cause;
    private boolean checkCause;
    private int integer;

    static {
        Skript.registerEvent("Emote Play/Stop", EvtEmote.class, CollectionUtils.array(PlayerEmotePlayEvent.class, PlayerEmoteEndEvent.class),
                "(perform|play[ing]) [of] [custom] (ia|itemsadder) emote [%strings%]",
                "(stop|cancel[ing]) [of] [custom] (ia|itemsadder) emote [%strings%] [due to (:stop[ped]|:finish[ed]) [emote]]");
    }

    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        this.integer = matchedPattern;
        this.id = (Literal<String>) args[0];
        if (parseResult.hasTag("stop")) {
            cause = PlayerEmoteEndEvent.Cause.STOP;
            checkCause = true;
        } else if (parseResult.hasTag("finish")) {
            cause = PlayerEmoteEndEvent.Cause.FINISHED;
            checkCause = true;
        } else {
            checkCause = false;
        }
        return true;
    }

    @Override
    public boolean check(Event event) {
        String emoteName = null;
        if (integer == 0 && event instanceof PlayerEmotePlayEvent) {
            emoteName = ((PlayerEmotePlayEvent) event).getEmoteName();
        } else if (integer == 1 && event instanceof PlayerEmoteEndEvent) {
            PlayerEmoteEndEvent endEvent = (PlayerEmoteEndEvent) event;
            emoteName = endEvent.getEmoteName();
            if (checkCause && endEvent.getCause() != cause) {
                return false;
            }
        }
        if (emoteName != null) {
            if (id != null) {
                for (String id : this.id.getArray(event)) {
                    if (emoteName.equals(id)) {
                        return true;
                    }
                }
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return this.integer == 0 ? "perform emote" : "stop emote" + (id != null ? id.toString(event, debug) : "");
    }
}
