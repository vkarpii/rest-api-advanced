package com.epam.esm.service.util;

import com.epam.esm.exception.ApplicationException;
import com.epam.esm.exception.ExceptionMessage;
import com.epam.esm.util.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class QueryBuilder {
    private static final String FIND_BY_PART_OF_NAME_QUERY = " certificate.certificateName LIKE '%?%' ";
    private static final String FIND_BY_TAG_NAME_QUERY = " tag.tagName IN ";
    private static final String FIND_BY_PART_OF_DESCRIPTION_QUERY = " certificate.certificateDescription LIKE '%?%' ";
    private static final String FIND_BY_TAG_NAME_FILTERING = "GROUP BY certificate.id HAVING COUNT(certificate.id) = ";
    private static final String BY_NAME_ASCENDING = "certificate.certificateName ASC ";
    private static final String BY_NAME_DESCENDING = "certificate.certificateName DESC ";
    private static final String BY_DATE_ASCENDING = "certificate.createDate ASC ";
    private static final String BY_DATE_DESCENDING = "certificate.createDate DESC ";
    private static final String PARAM_GIFT_CERTIFICATE_NAME = "giftCertificateName";
    private static final String PARAM_GIFT_CERTIFICATE_DESCRIPTION = "giftCertificateDescription";
    private static final String PARAM_ORDER_BY = "order";
    private static final String PARAM_TAG_NAME = "tagName";
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_SIZE = "size";

    private static final String PARAM_LOCALE = "locale";
    private static final String ORDER_BY_NAME_ASC_PARAM = "name:asc";
    private static final String ORDER_BY_NAME_DESC_PARAM = "name:desc";
    private static final String ORDER_BY_CREATE_DATE_ASC_PARAM = "createDate:asc";
    private static final String ORDER_BY_CREATE_DATE_DESC_PARAM = "createDate:desc";
    private static final String ORDER_GIFT_CERTIFICATES_BY = " ORDER BY ";
    private static final String AND_CLAUSE = " AND ";
    private static final String WHERE_CLAUSE = " WHERE ";
    private static final String EMPTY_STRING = "";

    private static final Map<String, String> searchQueries = new HashMap<>();
    private static final Map<String, String> orderQueries = new HashMap<>();

    static {
        searchQueries.put(PARAM_TAG_NAME, FIND_BY_TAG_NAME_QUERY);
        searchQueries.put(PARAM_ORDER_BY, ORDER_GIFT_CERTIFICATES_BY);
        searchQueries.put(PARAM_GIFT_CERTIFICATE_NAME, FIND_BY_PART_OF_NAME_QUERY);
        searchQueries.put(PARAM_GIFT_CERTIFICATE_DESCRIPTION, FIND_BY_PART_OF_DESCRIPTION_QUERY);

        orderQueries.put(ORDER_BY_NAME_ASC_PARAM, BY_NAME_ASCENDING);
        orderQueries.put(ORDER_BY_NAME_DESC_PARAM, BY_NAME_DESCENDING);
        orderQueries.put(ORDER_BY_CREATE_DATE_ASC_PARAM, BY_DATE_ASCENDING);
        orderQueries.put(ORDER_BY_CREATE_DATE_DESC_PARAM, BY_DATE_DESCENDING);
    }

    public String buildQuery(Map<String, String> params) {
        StringBuilder queryBuilder = new StringBuilder(EMPTY_STRING);
        checkParams(params);
        if (!params.isEmpty()) {
            appendQuery(params, queryBuilder);
            appendSortConditionIfExists(params, queryBuilder);
        }
        return queryBuilder.toString();
    }

    private void checkParams(Map<String, String> params) {
        Set<String> parameters = params.keySet();
        Map<String, String> fullMap = new HashMap<>();
        fullMap.putAll(searchQueries);
        fullMap.putAll(orderQueries);
        fullMap.put(PARAM_PAGE,EMPTY_STRING);
        fullMap.put(PARAM_SIZE,EMPTY_STRING);
        fullMap.put(PARAM_LOCALE,EMPTY_STRING);
        Set<String> allCommands = fullMap.keySet();
        parameters.forEach(param -> {
            if (!checkParam(param, allCommands)) {
                log.error(ExceptionMessage.BAD_ATTRIBUTES);
                throw new ApplicationException(ExceptionMessage.BAD_ATTRIBUTES);
            }
        });
    }

    private boolean checkParam(String param, Set<String> allCommands) {
        AtomicBoolean result = new AtomicBoolean(false);
        allCommands.forEach(element -> {
            if (element.equals(param)) {
                result.set(true);
            }
        });
        return result.get();
    }


    private void appendQuery(Map<String, String> params, StringBuilder queryBuilder) {
        appendSelectorByTagName(params, queryBuilder);
        params.keySet().forEach(key -> {
            String queryCondition = searchQueries.get(key);
            if (!PARAM_ORDER_BY.equals(key) && queryCondition!=null) {
                if (!whereClauseIsRequired(queryBuilder)) {
                    queryBuilder.append(AND_CLAUSE);
                } else {
                    queryBuilder.append(WHERE_CLAUSE);
                }
                queryCondition = queryCondition.replaceAll("\\?",  params.get(key) );
                queryBuilder.append(queryCondition);
            }
        });
    }

    private boolean appendSelectorByTagName(Map<String, String> params, StringBuilder queryBuilder) {
        if (params.containsKey(PARAM_TAG_NAME)) {

            List<String> tags = Arrays.stream(params.get(PARAM_TAG_NAME).split(",")).toList();
            StringBuilder query = new StringBuilder(WHERE_CLAUSE + FIND_BY_TAG_NAME_QUERY + "(");
            tags.forEach(tag -> query.append("'").append(tag).append("',"));
            query.deleteCharAt(query.length() - 1);
            query.append(") ").append(FIND_BY_TAG_NAME_FILTERING).append(tags.size());

            queryBuilder.append(query);
            params.remove(PARAM_TAG_NAME);
            return true;
        }
        return false;
    }

    private boolean whereClauseIsRequired(StringBuilder queryBuilder) {
        return !queryBuilder.toString().contains(WHERE_CLAUSE);
    }

    private void appendSortConditionIfExists(Map<String, String> params, StringBuilder queryBuilder) {
        if (params.containsKey(PARAM_ORDER_BY)) {
            queryBuilder.append(ORDER_GIFT_CERTIFICATES_BY);
            queryBuilder.append(orderQueries.get(params.get(PARAM_ORDER_BY)));
        }
    }
}
