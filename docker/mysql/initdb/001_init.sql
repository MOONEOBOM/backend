CREATE TABLE IF NOT EXISTS users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  firebase_uid VARCHAR(128) NOT NULL UNIQUE,
  email VARCHAR(255) NOT NULL UNIQUE,
  name VARCHAR(100) NOT NULL,
  photo_url VARCHAR(500) NULL,
  is_first_login BOOLEAN NOT NULL DEFAULT TRUE,
  last_login_at TIMESTAMP NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS counsel_summary (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  title VARCHAR(200) NOT NULL,
  content TEXT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT fk_consultation_summaries_user
    FOREIGN KEY (user_id) REFERENCES users(id)
    ON DELETE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_counsel_summary_user_id_id
ON counsel_summary (user_id, id DESC);


CREATE TABLE IF NOT EXISTS counsel_summary_highlight (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  summary_id BIGINT NOT NULL,
  seq INT NOT NULL,
  speaker ENUM('agent','user') NOT NULL,
  text TEXT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT fk_highlight_summary
    FOREIGN KEY (summary_id) REFERENCES counsel_summary(id)
    ON DELETE CASCADE,

  UNIQUE KEY uk_summary_seq (summary_id, seq),
  INDEX idx_summary_id (summary_id)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;
  
 
  CREATE TABLE IF NOT EXISTS call_counsel (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  started_at TIMESTAMP NOT NULL,
  preview_text VARCHAR(500) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  KEY idx_call_counsel_started_at (started_at)
)ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS call_counsel_messages (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,

  call_counsel_id BIGINT NOT NULL,  
  seq INT NOT NULL,
  role VARCHAR(20) NOT NULL,
  message TEXT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  UNIQUE KEY uk_call_seq (call_counsel_id, seq),

  CONSTRAINT fk_call_messages_call
    FOREIGN KEY (call_counsel_id) REFERENCES call_counsel(id)
    ON DELETE CASCADE
)ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;
  
