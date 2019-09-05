package com.razdolbai.server;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

class Parser {
    private final String FIELD_DELIMITER;
    private final String HEAD_BODY_DELIMITER;

    Parser(String field_delimiter, String head_body_delimiter) {
        FIELD_DELIMITER = field_delimiter;
        HEAD_BODY_DELIMITER = head_body_delimiter;
    }

    public Map<String, String> parse(String message) {
        Map<String, String> resultMap = Arrays.stream(message.split(FIELD_DELIMITER))
                .map(s -> s.split(HEAD_BODY_DELIMITER))
                .collect(Collectors.toMap(s -> s[0], s -> s[1]));
        return resultMap;
    }
}
