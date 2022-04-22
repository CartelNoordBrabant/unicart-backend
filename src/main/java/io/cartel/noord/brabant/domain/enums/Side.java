package com.ffdev.diff.domain.enums;

/**
 * {@link Side} represents a side of a diff.
 */
public enum Side {
    LEFT("left"),
    RIGHT("right");

    // Id is defined so we don't need to lower case it everywhere
    private final String id;

    Side(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
