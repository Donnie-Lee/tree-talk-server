-- 心语 TreeTalk 数据库设计 (MySQL版本)
-- 创建日期: 2023-10-20
-- 版本: 1.0

-- 设置字符集和排序规则
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- -----------------------------------------------------
-- 用户表 - 存储基本用户信息
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `users` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `email` VARCHAR(100) NULL COMMENT '邮箱',
  `phone` VARCHAR(20) NULL COMMENT '手机号',
  `password_hash` VARCHAR(255) NOT NULL COMMENT '密码哈希',
  `nickname` VARCHAR(50) NOT NULL COMMENT '昵称',
  `avatar_url` VARCHAR(255) NULL COMMENT '头像URL',
  `user_type` TINYINT NOT NULL DEFAULT 0 COMMENT '用户类型：0-普通用户，1-会员用户，2-管理员',
  `register_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `last_login_time` DATETIME NULL COMMENT '最后登录时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `version` INT NOT NULL DEFAULT 1 COMMENT '版本号',
  `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `idx_email` (`email` ASC) VISIBLE,
  UNIQUE INDEX `idx_phone` (`phone` ASC) VISIBLE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表';

-- -----------------------------------------------------
-- 用户资料表 - 存储用户详细信息
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_profiles` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '资料ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `continuous_check_in_days` INT NOT NULL DEFAULT 0 COMMENT '连续打卡天数',
  `total_check_in_days` INT NOT NULL DEFAULT 0 COMMENT '总打卡天数',
  `total_meditation_minutes` INT NOT NULL DEFAULT 0 COMMENT '总冥想时长(分钟)',
  `total_chat_count` INT NOT NULL DEFAULT 0 COMMENT '总聊天次数',
  `privacy_setting` TINYINT NOT NULL DEFAULT 0 COMMENT '隐私设置：0-默认，1-日记加密',
  `diary_password` VARCHAR(255) NULL COMMENT '日记密码(加密存储)',
  `notification_setting` JSON NULL COMMENT '通知设置',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `version` INT NOT NULL DEFAULT 1 COMMENT '版本号',
  `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `idx_user_id` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_profiles_users`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户资料表';

-- -----------------------------------------------------
-- 情绪记录表 - 存储用户每日情绪打卡
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `emotion_records` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `emotion_type` TINYINT NOT NULL COMMENT '情绪类型：1-开心，2-平静，3-焦虑，4-低落，5-愤怒，6-疲惫',
  `emotion_value` TINYINT NOT NULL COMMENT '情绪值：1-5分',
  `record_date` DATE NOT NULL COMMENT '记录日期',
  `record_time` TIME NOT NULL COMMENT '记录时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `version` INT NOT NULL DEFAULT 1 COMMENT '版本号',
  `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_user_date` (`user_id` ASC, `record_date` ASC) VISIBLE,
  CONSTRAINT `fk_emotions_users`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '情绪记录表';

-- -----------------------------------------------------
-- 情绪日记表 - 存储用户日记内容
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `diaries` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '日记ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `emotion_record_id` BIGINT UNSIGNED NULL COMMENT '关联的情绪记录ID',
  `content` TEXT NOT NULL COMMENT '日记内容',
  `is_encrypted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否加密：0-否，1-是',
  `encrypted_content` TEXT NULL COMMENT '加密内容(当is_encrypted=1时使用)',
  `record_date` DATE NOT NULL COMMENT '记录日期',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `version` INT NOT NULL DEFAULT 1 COMMENT '版本号',
  `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_user_date` (`user_id` ASC, `record_date` ASC) VISIBLE,
  INDEX `idx_emotion_record` (`emotion_record_id` ASC) VISIBLE,
  CONSTRAINT `fk_diaries_users`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_diaries_emotions`
    FOREIGN KEY (`emotion_record_id`)
    REFERENCES `emotion_records` (`id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '情绪日记表';

-- -----------------------------------------------------
-- 日记标签表 - 存储日记标签
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `diary_tags` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '标签ID',
  `diary_id` BIGINT UNSIGNED NOT NULL COMMENT '日记ID',
  `tag_name` VARCHAR(20) NOT NULL COMMENT '标签名称',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `version` INT NOT NULL DEFAULT 1 COMMENT '版本号',
  `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_diary_id` (`diary_id` ASC) VISIBLE,
  INDEX `idx_tag_name` (`tag_name` ASC) VISIBLE,
  CONSTRAINT `fk_tags_diaries`
    FOREIGN KEY (`diary_id`)
    REFERENCES `diaries` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '日记标签表';

-- -----------------------------------------------------
-- AI聊天会话表 - 存储用户与AI的聊天会话
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chat_sessions` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '会话ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `ai_name` VARCHAR(50) NOT NULL DEFAULT '小树' COMMENT 'AI名称',
  `session_title` VARCHAR(100) NULL COMMENT '会话标题',
  `start_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
  `end_time` DATETIME NULL COMMENT '结束时间',
  `message_count` INT NOT NULL DEFAULT 0 COMMENT '消息数量',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `version` INT NOT NULL DEFAULT 1 COMMENT '版本号',
  `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_user_id` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_sessions_users`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'AI聊天会话表';

-- -----------------------------------------------------
-- 聊天消息表 - 存储聊天消息内容
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chat_messages` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `session_id` BIGINT UNSIGNED NOT NULL COMMENT '会话ID',
  `sender_type` TINYINT NOT NULL COMMENT '发送者类型：0-用户，1-AI',
  `content` TEXT NOT NULL COMMENT '消息内容',
  `send_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  `is_voice` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否语音：0-否，1-是',
  `voice_url` VARCHAR(255) NULL COMMENT '语音URL',
  `voice_duration` INT NULL COMMENT '语音时长(秒)',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `version` INT NOT NULL DEFAULT 1 COMMENT '版本号',
  `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_session_id` (`session_id` ASC) VISIBLE,
  CONSTRAINT `fk_messages_sessions`
    FOREIGN KEY (`session_id`)
    REFERENCES `chat_sessions` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '聊天消息表';

-- -----------------------------------------------------
-- 冥想音频表 - 存储冥想音频信息
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `meditations` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '冥想ID',
  `title` VARCHAR(100) NOT NULL COMMENT '标题',
  `description` VARCHAR(500) NULL COMMENT '描述',
  `category` VARCHAR(50) NOT NULL COMMENT '分类',
  `duration` INT NOT NULL COMMENT '时长(秒)',
  `audio_url` VARCHAR(255) NOT NULL COMMENT '音频URL',
  `cover_image_url` VARCHAR(255) NULL COMMENT '封面图URL',
  `background_sound` VARCHAR(50) NULL COMMENT '背景音(雨声、海浪等)',
  `is_premium` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否会员专属：0-否，1-是',
  `play_count` INT NOT NULL DEFAULT 0 COMMENT '播放次数',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `version` INT NOT NULL DEFAULT 1 COMMENT '版本号',
  `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_category` (`category` ASC) VISIBLE,
  INDEX `idx_is_premium` (`is_premium` ASC) VISIBLE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '冥想音频表';

-- -----------------------------------------------------
-- 冥想记录表 - 存储用户冥想记录
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `meditation_records` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `meditation_id` BIGINT UNSIGNED NOT NULL COMMENT '冥想ID',
  `start_time` DATETIME NOT NULL COMMENT '开始时间',
  `duration` INT NOT NULL COMMENT '实际时长(秒)',
  `is_completed` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否完成：0-否，1-是',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `version` INT NOT NULL DEFAULT 1 COMMENT '版本号',
  `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_user_id` (`user_id` ASC) VISIBLE,
  INDEX `idx_meditation_id` (`meditation_id` ASC) VISIBLE,
  CONSTRAINT `fk_med_records_users`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_med_records_meditations`
    FOREIGN KEY (`meditation_id`)
    REFERENCES `meditations` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '冥想记录表';

-- -----------------------------------------------------
-- 呼吸练习表 - 存储呼吸练习模式
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `breathing_exercises` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '练习ID',
  `name` VARCHAR(100) NOT NULL COMMENT '名称',
  `description` VARCHAR(500) NULL COMMENT '描述',
  `pattern_json` JSON NOT NULL COMMENT '呼吸模式JSON',
  `default_duration` INT NOT NULL COMMENT '默认时长(秒)',
  `is_premium` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否会员专属：0-否，1-是',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `version` INT NOT NULL DEFAULT 1 COMMENT '版本号',
  `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '呼吸练习表';

-- -----------------------------------------------------
-- 呼吸练习记录表 - 存储用户呼吸练习记录
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `breathing_records` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `exercise_id` BIGINT UNSIGNED NOT NULL COMMENT '练习ID',
  `start_time` DATETIME NOT NULL COMMENT '开始时间',
  `duration` INT NOT NULL COMMENT '实际时长(秒)',
  `is_completed` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否完成：0-否，1-是',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `version` INT NOT NULL DEFAULT 1 COMMENT '版本号',
  `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_user_id` (`user_id` ASC) VISIBLE,
  INDEX `idx_exercise_id` (`exercise_id` ASC) VISIBLE,
  CONSTRAINT `fk_breath_records_users`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_breath_records_exercises`
    FOREIGN KEY (`exercise_id`)
    REFERENCES `breathing_exercises` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '呼吸练习记录表';

-- -----------------------------------------------------
-- 心灵树洞表 - 存储匿名心事
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tree_holes` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '树洞ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID(加密存储)',
  `anonymous_name` VARCHAR(50) NOT NULL COMMENT '匿名昵称',
  `content` VARCHAR(500) NOT NULL COMMENT '内容',
  `star_count` INT NOT NULL DEFAULT 0 COMMENT '星光数',
  `comment_count` INT NOT NULL DEFAULT 0 COMMENT '评论数',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-待审核，1-已发布，2-已屏蔽',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `version` INT NOT NULL DEFAULT 1 COMMENT '版本号',
  `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_user_id` (`user_id` ASC) VISIBLE,
  INDEX `idx_star_count` (`star_count` DESC) VISIBLE,
  CONSTRAINT `fk_holes_users`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '心灵树洞表';

-- -----------------------------------------------------
-- 树洞星光表 - 存储用户点亮的星光
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tree_hole_stars` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '星光ID',
  `tree_hole_id` BIGINT UNSIGNED NOT NULL COMMENT '树洞ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `message` VARCHAR(100) NULL COMMENT '留言',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `version` INT NOT NULL DEFAULT 1 COMMENT '版本号',
  `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `idx_hole_user` (`tree_hole_id` ASC, `user_id` ASC) VISIBLE,
  INDEX `idx_user_id` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_stars_holes`
    FOREIGN KEY (`tree_hole_id`)
    REFERENCES `tree_holes` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_stars_users`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '树洞星光表';

-- -----------------------------------------------------
-- 会员订阅表 - 存储用户会员信息
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `subscriptions` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '订阅ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `plan_type` TINYINT NOT NULL COMMENT '套餐类型：1-月度，2-年度，3-终身',
  `start_date` DATE NOT NULL COMMENT '开始日期',
  `end_date` DATE NULL COMMENT '结束日期',
  `auto_renew` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否自动续费：0-否，1-是',
  `status` TINYINT NOT NULL COMMENT '状态：1-有效，2-已过期，3-已取消',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `version` INT NOT NULL DEFAULT 1 COMMENT '版本号',
  `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_user_id` (`user_id` ASC) VISIBLE,
  INDEX `idx_end_date` (`end_date` ASC) VISIBLE,
  CONSTRAINT `fk_subs_users`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '会员订阅表';

-- -----------------------------------------------------
-- 支付记录表 - 存储支付信息
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `payment_records` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '支付ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `subscription_id` BIGINT UNSIGNED NULL COMMENT '订阅ID',
  `amount` DECIMAL(10, 2) NOT NULL COMMENT '金额',
  `currency` VARCHAR(10) NOT NULL DEFAULT 'CNY' COMMENT '货币',
  `payment_method` VARCHAR(50) NOT NULL COMMENT '支付方式',
  `transaction_id` VARCHAR(100) NULL COMMENT '交易ID',
  `status` TINYINT NOT NULL COMMENT '状态：1-待支付，2-已支付，3-已退款，4-已取消',
  `payment_time` DATETIME NULL COMMENT '支付时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `version` INT NOT NULL DEFAULT 1 COMMENT '版本号',
  `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_user_id` (`user_id` ASC) VISIBLE,
  INDEX `idx_subscription_id` (`subscription_id` ASC) VISIBLE,
  CONSTRAINT `fk_payments_users`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_payments_subs`
    FOREIGN KEY (`subscription_id`)
    REFERENCES `subscriptions` (`id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '支付记录表';

-- -----------------------------------------------------
-- 系统配置表 - 存储系统配置信息
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `system_configs` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_key` VARCHAR(50) NOT NULL COMMENT '配置键',
  `config_value` TEXT NOT NULL COMMENT '配置值',
  `description` VARCHAR(255) NULL COMMENT '描述',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `version` INT NOT NULL DEFAULT 1 COMMENT '版本号',
  `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `idx_config_key` (`config_key` ASC) VISIBLE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统配置表';

SET FOREIGN_KEY_CHECKS = 1;