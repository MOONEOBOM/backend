package com.ureca.ureca.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        if (!FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.getInstance();
        }

        // 1. Cloudtype 환경 변수 'FIREBASE_CONFIG_JSON'에서 데이터를 읽어옵니다.
        String firebaseConfig = System.getenv("FIREBASE_CONFIG_JSON");
        InputStream in;

        if (firebaseConfig != null && !firebaseConfig.isEmpty()) {
            // 배포 환경: 환경 변수에 저장된 JSON 문자열을 스트림으로 변환
            in = new ByteArrayInputStream(firebaseConfig.getBytes(StandardCharsets.UTF_8));
        } else {
            // 로컬 환경 혹은 환경변수가 없을 때 에러 방지를 위한 예외 처리
            throw new RuntimeException("Firebase 환경 변수(FIREBASE_CONFIG_JSON)를 찾을 수 없습니다.");
        }

        try (in) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(in))
                    .build();

            return FirebaseApp.initializeApp(options);
        }
    }

    @Bean
    public FirebaseAuth firebaseAuth(FirebaseApp firebaseApp) {
        return FirebaseAuth.getInstance(firebaseApp);
    }
}