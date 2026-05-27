-- 1. members -> user_account
ALTER TABLE members RENAME TO user_account;

-- 2. profiles -> profile
ALTER TABLE profiles RENAME TO profile;

-- 3. profile.member_id -> profile.user_id
ALTER TABLE profile RENAME COLUMN member_id TO user_id;

-- 4. profile FK constraint rename
ALTER TABLE profile DROP FOREIGN KEY fk_profiles_member;
ALTER TABLE profile
    ADD CONSTRAINT fk_profile_user
        FOREIGN KEY (user_id)
            REFERENCES user_account(id)
            ON DELETE CASCADE;

-- 5. mission.member_id -> mission.user_id
ALTER TABLE missions RENAME COLUMN member_id TO user_id;

-- 6. mission FK/UK/INDEX rename
ALTER TABLE missions DROP FOREIGN KEY fk_missions_member;
ALTER TABLE missions DROP INDEX uk_missions_member_date_difficulty;
ALTER TABLE missions DROP INDEX idx_missions_member_date;

ALTER TABLE missions
    ADD CONSTRAINT fk_missions_user
        FOREIGN KEY (user_id)
            REFERENCES user_account(id)
            ON DELETE CASCADE;

ALTER TABLE missions
    ADD CONSTRAINT uk_missions_user_date_difficulty
        UNIQUE (user_id, mission_date, difficulty);

CREATE INDEX idx_missions_user_date ON missions (user_id, mission_date);
