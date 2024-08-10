package me.asleepp.SkriptItemsAdder.other.aliases;

import org.bukkit.event.Event;

import java.util.Objects;
import java.util.function.Function;

public class CustomItemType {

    private String namespacedID;

    public CustomItemType(String namespacedID) {
        this.namespacedID = namespacedID;
    }

    public String getNamespacedID() {
        return namespacedID;
    }

    public void setNamespacedID(String namespacedID) {
        this.namespacedID = namespacedID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomItemType that = (CustomItemType) o;
        return namespacedID.equals(that.namespacedID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespacedID);
    }

    @Override
    public String toString() {
        return namespacedID;
    }


    public static CustomItemType fromEvent(Event event, AliasesGenerator aliasesGenerator, Function<Event, String> idExtractor) {
        String namespacedID = idExtractor.apply(event);
        String alias = aliasesGenerator.getNamespacedId(namespacedID);
        return alias != null ? new CustomItemType(alias) : null;
    }

}
