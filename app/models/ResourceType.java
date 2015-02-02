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
    WEBSITE
}
