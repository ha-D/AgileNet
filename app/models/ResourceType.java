package models;

import com.avaje.ebean.annotation.EnumValue;

public enum ResourceType {
    @EnumValue("B")
    BOOK,
    @EnumValue("V")
    VIDEO,
    @EnumValue("A")
    ARTICLE,
    @EnumValue("W")
    WEBSITE;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

    public static ResourceType fromString(String value) {
        switch (value.toLowerCase()) {
            case "book": return BOOK;
            case "video": return VIDEO;
            case "article": return ARTICLE;
            case "website": return WEBSITE;
            default: return null;
        }
    }
}
