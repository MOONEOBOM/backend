package com.ureca.ureca.global.external.clova;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "clova.speech")
public class ClovaSpeechProperties {
    private String secret;
    private String invokeUrl;
}
