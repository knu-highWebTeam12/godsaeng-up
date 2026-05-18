CREATE TABLE members (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     login_id VARCHAR(50) NOT NULL UNIQUE,
     password VARCHAR(255) NOT NULL,
     created_at DATETIME NOT NULL,
     updated_at DATETIME NOT NULL
);

CREATE TABLE profiles (
      id BIGINT AUTO_INCREMENT PRIMARY KEY,
      member_id BIGINT NOT NULL UNIQUE,
      nickname VARCHAR(50) NOT NULL UNIQUE,
      level INT NOT NULL DEFAULT 1,
      exp INT NOT NULL DEFAULT 0,
      created_at DATETIME NOT NULL,
      updated_at DATETIME NOT NULL,

      CONSTRAINT fk_profiles_member
          FOREIGN KEY (member_id)
              REFERENCES members(id)
              ON DELETE CASCADE
);

CREATE TABLE missions (
      id BIGINT AUTO_INCREMENT PRIMARY KEY,
      member_id BIGINT NOT NULL,
      content VARCHAR(255) NOT NULL,
      difficulty VARCHAR(20) NOT NULL COMMENT 'EASY, NORMAL, HARD',
      status VARCHAR(20) NOT NULL DEFAULT 'TODO' COMMENT 'TODO, DONE',
      image_url VARCHAR(500) NULL,
      mission_date DATE NOT NULL,
      completed_at DATETIME NULL,
      earned_exp INT NOT NULL DEFAULT 0,
      is_modified BOOLEAN NOT NULL DEFAULT FALSE,
      created_at DATETIME NOT NULL,
      updated_at DATETIME NOT NULL,

      CONSTRAINT fk_missions_member
          FOREIGN KEY (member_id)
              REFERENCES members(id)
              ON DELETE CASCADE,

      CONSTRAINT uk_missions_member_date_difficulty
          UNIQUE (member_id, mission_date, difficulty),

      INDEX idx_missions_member_date (member_id, mission_date),
      INDEX idx_missions_status (status)
);