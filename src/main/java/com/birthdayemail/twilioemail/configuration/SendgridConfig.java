package com.birthdayemail.twilioemail.configuration;

import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SendgridConfig {

    @Value("${sendgrid.key}")
    private String key;


    @Bean
    public SendGrid getSendgrid()
    {
        return new SendGrid(key);
    }
}
