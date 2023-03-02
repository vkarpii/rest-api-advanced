package com.epam.esm.repository.certificate.util;

public final class SQLQuery {

    public static final String PARAM_CERTIFICATE_NAME  = "certificateName";
    public static final String GET_ALL = "SELECT DISTINCT certificate " +
            "FROM GiftCertificate certificate " +
            "JOIN certificate.tags tag ";
    public static final String GET_CERTIFICATE_BY_NAME = "SELECT certificate " +
            "FROM GiftCertificate certificate " +
            "WHERE certificate.certificateName =: certificateName";
}
