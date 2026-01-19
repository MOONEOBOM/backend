package com.ureca.ureca.domain.storage;

import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {

  @Value("${ncp.object-storage.credentials.access-key}")
  private String accessKey;

  @Value("${ncp.object-storage.credentials.secret-key}")
  private String secretKey;

  @Value("${ncp.object-storage.signing-region}")
  private String region;

  @Value("${ncp.object-storage.endpoint}")
  private String endpoint;

  @Bean
  public S3Presigner s3Presigner() {
    return S3Presigner.builder().endpointOverride(URI.create(endpoint)).region(Region.of(region))
        .credentialsProvider(
            StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
        .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build())
        .build();
  }
}
