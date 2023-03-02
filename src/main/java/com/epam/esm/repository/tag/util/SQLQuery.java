package com.epam.esm.repository.tag.util;

public final class SQLQuery {

    public static final String PARAM_TAG_NAME = "tagName";
    public static final String TAG_IS_EXISTS = "SELECT tag " +
            "FROM Tag tag " +
            "WHERE tag.tagName =: tagName";
    public static final String FIND_ALL_TAGS = "SELECT tag " +
            "FROM Tag tag";
}
