package com.ning.utils;

import org.springframework.util.StringUtils;

public class EntityExistException extends RuntimeException {

    public EntityExistException(String field, String val) {
        super(EntityExistException.generateMessage(field, val));
    }

    private static String generateMessage(String field, String val) {
//        return StringUtils.capitalize(entity)
//                + " with " + field + " "+ val + " existed";
        return field + " 为 " + val + " 的账号已存在";
    }
}
