package com.ehas.auth;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.convert.CustomConversions.StoreConversions;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;

import com.ehas.auth.User.userstatus.UserStatusReadConverter;
import com.ehas.auth.User.userstatus.UserStatusWriteConverter;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "spring.r2dbc")
@Data
public class R2dbcConfig extends AbstractR2dbcConfiguration {

    private String url;
    private String username;
    private String password;

    @Override
    public ConnectionFactory connectionFactory() {
        return ConnectionFactories.get(url);
    }
    
    @Override
    public R2dbcCustomConversions r2dbcCustomConversions() {
        return new R2dbcCustomConversions(
            StoreConversions.NONE,
            List.of(
                new UserStatusWriteConverter(),
                new UserStatusReadConverter()
            )
        );
    }
    
    @Bean // @Transactional 사용하기 위함
    public R2dbcTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }
}
