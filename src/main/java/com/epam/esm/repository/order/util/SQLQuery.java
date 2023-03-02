package com.epam.esm.repository.order.util;

public final class SQLQuery {

    public static final String PARAM_ID = "id";
    public static final String FIND_ALL_ORDERS = "SELECT order FROM Order order " +
            "JOIN order.user user " +
            "WHERE user.id =: id";
    public static final String MOST_WIDELY_USED_TAG = "SELECT tag " +
            "FROM Order order " +
            "JOIN order.certificates certificate " +
            "JOIN certificate.tags tag " +
            "WHERE order.id = ( " +
                                    "SELECT order.id " +
                                    "FROM Order order " +
                                    "JOIN order.user user " +
                                    "JOIN order.certificates certificate " +
                                    "WHERE user.id = :id " +
                                    "GROUP BY order.purchaseDate " +
                                    "ORDER BY SUM(certificate.price) DESC " +
                                    "LIMIT 1 " +
                                " ) "+
            "GROUP BY tag.tagName " +
            "ORDER BY COUNT(*) DESC " +
            "LIMIT 1";

}
