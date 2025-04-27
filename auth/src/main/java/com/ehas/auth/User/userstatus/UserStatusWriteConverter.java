package com.ehas.auth.User.userstatus;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class UserStatusWriteConverter implements Converter<UserStatus, Integer> {
    @Override
    public Integer convert(UserStatus source) {
        return source != null ? source.getInteger() : null;
    }
}