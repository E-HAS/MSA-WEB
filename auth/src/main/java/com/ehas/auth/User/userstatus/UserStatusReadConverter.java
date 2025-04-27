package com.ehas.auth.User.userstatus;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class UserStatusReadConverter implements Converter<Integer, UserStatus> {
    @Override
    public UserStatus convert(Integer source) {
        return source != null ? UserStatus.getString(source) : null;
    }
}