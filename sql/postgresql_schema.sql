-- 心语 TreeTalk 数据库设计 (PostgreSQL版本)
-- 创建日期: 2023-10-20
-- 版本: 1.0

-- -----------------------------------------------------
-- 用户表 - 存储基本用户信息
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
  id BIGSERIAL PRIMARY KEY,
  email VARCHAR(100) UNIQUE,
  phone VARCHAR(20) UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  nickname VARCHAR(50) NOT NULL,
  avatar_url VARCHAR(255),
  user_type SMALLINT NOT NULL DEFAULT 0,
  register_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  last_login_time TIMESTAMP,
  is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
  version INTEGER NOT NULL DEFAULT 1,
  created_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_phone ON users(phone);

-- 添加注释
COMMENT ON TABLE users IS '用户表';
COMMENT ON COLUMN users.id IS '用户ID';
COMMENT ON COLUMN users.email IS '邮箱';
COMMENT ON COLUMN users.phone IS '手机号';
COMMENT ON COLUMN users.password_hash IS '密码哈希';
COMMENT ON COLUMN users.nickname IS '昵称';
COMMENT ON COLUMN users.avatar_url IS '头像URL';
COMMENT ON COLUMN users.user_type IS '用户类型：0-普通用户，1-会员用户，2-管理员';
COMMENT ON COLUMN users.register_time IS '注册时间';
COMMENT ON COLUMN users.last_login_time IS '最后登录时间';
COMMENT ON COLUMN users.is_deleted IS '是否删除：false-否，true-是';
COMMENT ON COLUMN users.version IS '版本号';
COMMENT ON COLUMN users.created_time IS '创建时间';
COMMENT ON COLUMN users.updated_time IS '更新时间';

-- 创建更新时间触发器
CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_time = CURRENT_TIMESTAMP;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_users_timestamp
BEFORE UPDATE ON users
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- -----------------------------------------------------
-- 用户资料表 - 存储用户详细信息
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS user_profiles (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  continuous_check_in_days INTEGER NOT NULL DEFAULT 0,
  total_check_in_days INTEGER NOT NULL DEFAULT 0,
  total_meditation_minutes INTEGER NOT NULL DEFAULT 0,
  total_chat_count INTEGER NOT NULL DEFAULT 0,
  privacy_setting SMALLINT NOT NULL DEFAULT 0,
  diary_password VARCHAR(255),
  notification_setting JSONB,
  is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
  version INTEGER NOT NULL DEFAULT 1,
  created_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE UNIQUE INDEX idx_user_profiles_user_id ON user_profiles(user_id);

-- 添加注释
COMMENT ON TABLE user_profiles IS '用户资料表';
COMMENT ON COLUMN user_profiles.id IS '资料ID';
COMMENT ON COLUMN user_profiles.user_id IS '用户ID';
COMMENT ON COLUMN user_profiles.continuous_check_in_days IS '连续打卡天数';
COMMENT ON COLUMN user_profiles.total_check_in_days IS '总打卡天数';
COMMENT ON COLUMN user_profiles.total_meditation_minutes IS '总冥想时长(分钟)';
COMMENT ON COLUMN user_profiles.total_chat_count IS '总聊天次数';
COMMENT ON COLUMN user_profiles.privacy_setting IS '隐私设置：0-默认，1-日记加密';
COMMENT ON COLUMN user_profiles.diary_password IS '日记密码(加密存储)';
COMMENT ON COLUMN user_profiles.notification_setting IS '通知设置';
COMMENT ON COLUMN user_profiles.is_deleted IS '是否删除：false-否，true-是';
COMMENT ON COLUMN user_profiles.version IS '版本号';
COMMENT ON COLUMN user_profiles.created_time IS '创建时间';
COMMENT ON COLUMN user_profiles.updated_time IS '更新时间';

-- 创建更新时间触发器
CREATE TRIGGER update_user_profiles_timestamp
BEFORE UPDATE ON user_profiles
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- -----------------------------------------------------
-- 情绪记录表 - 存储用户每日情绪打卡
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS emotion_records (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  emotion_type SMALLINT NOT NULL,
  emotion_value SMALLINT NOT NULL CHECK (emotion_value BETWEEN 1 AND 5),
  record_date DATE NOT NULL,
  record_time TIME NOT NULL,
  is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
  version INTEGER NOT NULL DEFAULT 1,
  created_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX idx_emotion_records_user_date ON emotion_records(user_id, record_date);

-- 添加注释
COMMENT ON TABLE emotion_records IS '情绪记录表';
COMMENT ON COLUMN emotion_records.id IS '记录ID';
COMMENT ON COLUMN emotion_records.user_id IS '用户ID';
COMMENT ON COLUMN emotion_records.emotion_type IS '情绪类型：1-开心，2-平静，3-焦虑，4-低落，5-愤怒，6-疲惫';
COMMENT ON COLUMN emotion_records.emotion_value IS '情绪值：1-5分';
COMMENT ON COLUMN emotion_records.record_date IS '记录日期';
COMMENT ON COLUMN emotion_records.record_time IS '记录时间';
COMMENT ON COLUMN emotion_records.is_deleted IS '是否删除：false-否，true-是';
COMMENT ON COLUMN emotion_records.version IS '版本号';
COMMENT ON COLUMN emotion_records.created_time IS '创建时间';
COMMENT ON COLUMN emotion_records.updated_time IS '更新时间';

-- 创建更新时间触发器
CREATE TRIGGER update_emotion_records_timestamp
BEFORE UPDATE ON emotion_records
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- -----------------------------------------------------
-- 情绪日记表 - 存储用户日记内容
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS diaries (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  emotion_record_id BIGINT REFERENCES emotion_records(id) ON DELETE SET NULL,
  content TEXT NOT NULL,
  is_encrypted BOOLEAN NOT NULL DEFAULT FALSE,
  encrypted_content TEXT,
  record_date DATE NOT NULL,
  is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
  version INTEGER NOT NULL DEFAULT 1,
  created_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX idx_diaries_user_date ON diaries(user_id, record_date);
CREATE INDEX idx_diaries_emotion_record ON diaries(emotion_record_id);

-- 添加注释
COMMENT ON TABLE diaries IS '情绪日记表';
COMMENT ON COLUMN diaries.id IS '日记ID';
COMMENT ON COLUMN diaries.user_id IS '用户ID';
COMMENT ON COLUMN diaries.emotion_record_id IS '关联的情绪记录ID';
COMMENT ON COLUMN diaries.content IS '日记内容';
COMMENT ON COLUMN diaries.is_encrypted IS '是否加密：false-否，true-是';
COMMENT ON COLUMN diaries.encrypted_content IS '加密内容(当is_encrypted=true时使用)';
COMMENT ON COLUMN diaries.record_date IS '记录日期';
COMMENT ON COLUMN diaries.is_deleted IS '是否删除：false-否，true-是';
COMMENT ON COLUMN diaries.version IS '版本号';
COMMENT ON COLUMN diaries.created_time IS '创建时间';
COMMENT ON COLUMN diaries.updated_time IS '更新时间';

-- 创建更新时间触发器
CREATE TRIGGER update_diaries_timestamp
BEFORE UPDATE ON diaries
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- -----------------------------------------------------
-- 日记标签表 - 存储日记标签
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS diary_tags (
  id BIGSERIAL PRIMARY KEY,
  diary_id BIGINT NOT NULL REFERENCES diaries(id) ON DELETE CASCADE,
  tag_name VARCHAR(20) NOT NULL,
  is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
  version INTEGER NOT NULL DEFAULT 1,
  created_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX idx_diary_tags_diary_id ON diary_tags(diary_id);
CREATE INDEX idx_diary_tags_tag_name ON diary_tags(tag_name);

-- 添加注释
COMMENT ON TABLE diary_tags IS '日记标签表';
COMMENT ON COLUMN diary_tags.id IS '标签ID';
COMMENT ON COLUMN diary_tags.diary_id IS '日记ID';
COMMENT ON COLUMN diary_tags.tag_name IS '标签名称';
COMMENT ON COLUMN diary_tags.is_deleted IS '是否删除：false-否，true-是';
COMMENT ON COLUMN diary_tags.version IS '版本号';
COMMENT ON COLUMN diary_tags.created_time IS '创建时间';
COMMENT ON COLUMN diary_tags.updated_time IS '更新时间';

-- 创建更新时间触发器
CREATE TRIGGER update_diary_tags_timestamp
BEFORE UPDATE ON diary_tags
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- -----------------------------------------------------
-- AI聊天会话表 - 存储用户与AI的聊天会话
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS chat_sessions (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  ai_name VARCHAR(50) NOT NULL DEFAULT '小树',
  session_title VARCHAR(100),
  start_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  end_time TIMESTAMP,
  message_count INTEGER NOT NULL DEFAULT 0,
  is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
  version INTEGER NOT NULL DEFAULT 1,
  created_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX idx_chat_sessions_user_id ON chat_sessions(user_id);

-- 添加注释
COMMENT ON TABLE chat_sessions IS 'AI聊天会话表';
COMMENT ON COLUMN chat_sessions.id IS '会话ID';
COMMENT ON COLUMN chat_sessions.user_id IS '用户ID';
COMMENT ON COLUMN chat_sessions.ai_name IS 'AI名称';
COMMENT ON COLUMN chat_sessions.session_title IS '会话标题';
COMMENT ON COLUMN chat_sessions.start_time IS '开始时间';
COMMENT ON COLUMN chat_sessions.end_time IS '结束时间';
COMMENT ON COLUMN chat_sessions.message_count IS '消息数量';
COMMENT ON COLUMN chat_sessions.is_deleted IS '是否删除：false-否，true-是';
COMMENT ON COLUMN chat_sessions.version IS '版本号';
COMMENT ON COLUMN chat_sessions.created_time IS '创建时间';
COMMENT ON COLUMN chat_sessions.updated_time IS '更新时间';

-- 创建更新时间触发器
CREATE TRIGGER update_chat_sessions_timestamp
BEFORE UPDATE ON chat_sessions
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- -----------------------------------------------------
-- 聊天消息表 - 存储聊天消息内容
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS chat_messages (
  id BIGSERIAL PRIMARY KEY,
  session_id BIGINT NOT NULL REFERENCES chat_sessions(id) ON DELETE CASCADE,
  sender_type SMALLINT NOT NULL,
  content TEXT NOT NULL,
  send_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_voice BOOLEAN NOT NULL DEFAULT FALSE,
  voice_url VARCHAR(255),
  voice_duration INTEGER,
  is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
  version INTEGER NOT NULL DEFAULT 1,
  created_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX idx_chat_messages_session_id ON chat_messages(session_id);

-- 添加注释
COMMENT ON TABLE chat_messages IS '聊天消息表';
COMMENT ON COLUMN chat_messages.id IS '消息ID';
COMMENT ON COLUMN chat_messages.session_id IS '会话ID';
COMMENT ON COLUMN chat_messages.sender_type IS '发送者类型：0-用户，1-AI';
COMMENT ON COLUMN chat_messages.content IS '消息内容';
COMMENT ON COLUMN chat_messages.send_time IS '发送时间';
COMMENT ON COLUMN chat_messages.is_voice IS '是否语音：false-否，true-是';
COMMENT ON COLUMN chat_messages.voice_url IS '语音URL';
COMMENT ON COLUMN chat_messages.voice_duration IS '语音时长(秒)';
COMMENT ON COLUMN chat_messages.is_deleted IS '是否删除：false-否，true-是';
COMMENT ON COLUMN chat_messages.version IS '版本号';
COMMENT ON COLUMN chat_messages.created_time IS '创建时间';
COMMENT ON COLUMN chat_messages.updated_time IS '更新时间';

-- 创建更新时间触发器
CREATE TRIGGER update_chat_messages_timestamp
BEFORE UPDATE ON chat_messages
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- -----------------------------------------------------
-- 冥想音频表 - 存储冥想音频信息
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS meditations (
  id BIGSERIAL PRIMARY KEY,
  title VARCHAR(100) NOT NULL,
  description VARCHAR(500),
  category VARCHAR(50) NOT NULL,
  duration INTEGER NOT NULL,
  audio_url VARCHAR(255) NOT NULL,
  cover_image_url VARCHAR(255),
  background_sound VARCHAR(50),
  is_premium BOOLEAN NOT NULL DEFAULT FALSE,
  play_count INTEGER NOT NULL DEFAULT 0,
  is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
  version INTEGER NOT NULL DEFAULT 1,
  created_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX idx_meditations_category ON meditations(category);
CREATE INDEX idx_meditations_is_premium ON meditations(is_premium);

-- 添加注释
COMMENT ON TABLE meditations IS '冥想音频表';
COMMENT ON COLUMN meditations.id IS '冥想ID';
COMMENT ON COLUMN meditations.title IS '标题';
COMMENT ON COLUMN meditations.description IS '描述';
COMMENT ON COLUMN meditations.category IS '分类';
COMMENT ON COLUMN meditations.duration IS '时长(秒)';
COMMENT ON COLUMN meditations.audio_url IS '音频URL';
COMMENT ON COLUMN meditations.cover_image_url IS '封面图URL';
COMMENT ON COLUMN meditations.background_sound IS '背景音(雨声、海浪等)';
COMMENT ON COLUMN meditations.is_premium IS '是否会员专属：false-否，true-是';
COMMENT ON COLUMN meditations.play_count IS '播放次数';
COMMENT ON COLUMN meditations.is_deleted IS '是否删除：false-否，true-是';
COMMENT ON COLUMN meditations.version IS '版本号';
COMMENT ON COLUMN meditations.created_time IS '创建时间';
COMMENT ON COLUMN meditations.updated_time IS '更新时间';

-- 创建更新时间触发器
CREATE TRIGGER update_meditations_timestamp
BEFORE UPDATE ON meditations
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- -----------------------------------------------------
-- 冥想记录表 - 存储用户冥想记录
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS meditation_records (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  meditation_id BIGINT NOT NULL REFERENCES meditations(id) ON DELETE CASCADE,
  start_time TIMESTAMP NOT NULL,
  duration INTEGER NOT NULL,
  is_completed BOOLEAN NOT NULL DEFAULT FALSE,
  is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
  version INTEGER NOT NULL DEFAULT 1,
  created_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX idx_meditation_records_user_id ON meditation_records(user_id);
CREATE INDEX idx_meditation_records_meditation_id ON meditation_records(meditation_id);

-- 添加注释
COMMENT ON TABLE meditation_records IS '冥想记录表';
COMMENT ON COLUMN meditation_records.id IS '记录ID';
COMMENT ON COLUMN meditation_records.user_id IS '用户ID';
COMMENT ON COLUMN meditation_records.meditation_id IS '冥想ID';
COMMENT ON COLUMN meditation_records.start_time IS '开始时间';
COMMENT ON COLUMN meditation_records.duration IS '实际时长(秒)';
COMMENT ON COLUMN meditation_records.is_completed IS '是否完成：false-否，true-是';
COMMENT ON COLUMN meditation_records.is_deleted IS '是否删除：false-否，true-是';
COMMENT ON COLUMN meditation_records.version IS '版本号';
COMMENT ON COLUMN meditation_records.created_time IS '创建时间';
COMMENT ON COLUMN meditation_records.updated_time IS '更新时间';

-- 创建更新时间触发器
CREATE TRIGGER update_meditation_records_timestamp
BEFORE UPDATE ON meditation_records
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- -----------------------------------------------------
-- 呼吸练习表 - 存储呼吸练习模式
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS breathing_exercises (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(500),
  pattern_json JSONB NOT NULL,
  default_duration INTEGER NOT NULL,
  is_premium BOOLEAN NOT NULL DEFAULT FALSE,
  is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
  version INTEGER NOT NULL DEFAULT 1,
  created_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 添加注释
COMMENT ON TABLE breathing_exercises IS '呼吸练习表';
COMMENT ON COLUMN breathing_exercises.id IS '练习ID';
COMMENT ON COLUMN breathing_exercises.name IS '名称';
COMMENT ON COLUMN breathing_exercises.description IS '描述';
COMMENT ON COLUMN breathing_exercises.pattern_json IS '呼吸模式JSON';
COMMENT ON COLUMN breathing_exercises.default_duration IS '默认时长(秒)';
COMMENT ON COLUMN breathing_exercises.is_premium IS '是否会员专属：false-否，true-是';
COMMENT ON COLUMN breathing_exercises.is_deleted IS '是否删除：false-否，true-是';
COMMENT ON COLUMN breathing_exercises.version IS '版本号';
COMMENT ON COLUMN breathing_exercises.created_time IS '创建时间';
COMMENT ON COLUMN breathing_exercises.updated_time IS '更新时间';

-- 创建更新时间触发器
CREATE TRIGGER update_breathing_exercises_timestamp
BEFORE UPDATE ON breathing_exercises
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- -----------------------------------------------------
-- 呼吸练习记录表 - 存储用户呼吸练习记录
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS breathing_records (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  exercise_id BIGINT NOT NULL REFERENCES breathing_exercises(id) ON DELETE CASCADE,
  start_time TIMESTAMP NOT NULL,
  duration INTEGER NOT NULL,
  is_completed BOOLEAN NOT NULL DEFAULT FALSE,
  is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
  version INTEGER NOT NULL DEFAULT 1,
  created_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX idx_breathing_records_user_id ON breathing_records(user_id);
CREATE INDEX idx_breathing_records_exercise_id ON breathing_records(exercise_id);

-- 添加注释
COMMENT ON TABLE breathing_records IS '呼吸练习记录表';
COMMENT ON COLUMN breathing_records.id IS '记录ID';
COMMENT ON COLUMN breathing_records.user_id IS '用户ID';
COMMENT ON COLUMN breathing_records.exercise_id IS '练习ID';
COMMENT ON COLUMN breathing_records.start_time IS '开始时间';
COMMENT ON COLUMN breathing_records.duration IS '实际时长(秒)';
COMMENT ON COLUMN breathing_records.is_completed IS '是否完成：false-否，true-是';
COMMENT ON COLUMN breathing_records.is_deleted IS '是否删除：false-否，true-是';
COMMENT ON COLUMN breathing_records.version IS '版本号';
COMMENT ON COLUMN breathing_records.created_time IS '创建时间';
COMMENT ON COLUMN breathing_records.updated_time IS '更新时间';

-- 创建更新时间触发器
CREATE TRIGGER update_breathing_records_timestamp
BEFORE UPDATE ON breathing_records
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- -----------------------------------------------------
-- 心灵树洞表 - 存储匿名心事
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS tree_holes (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  anonymous_name VARCHAR(50) NOT NULL,
  content VARCHAR(500) NOT NULL,
  star_count INTEGER NOT NULL DEFAULT 0,
  comment_count INTEGER NOT NULL DEFAULT 0,
  status SMALLINT NOT NULL DEFAULT 1,
  is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
  version INTEGER NOT NULL DEFAULT 1,
  created_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX idx_tree_holes_user_id ON tree_holes(user_id);
CREATE INDEX idx_tree_holes_star_count ON tree_holes(star_count DESC);

-- 添加注释
COMMENT ON TABLE tree_holes IS '心灵树洞表';
COMMENT ON COLUMN tree_holes.id IS '树洞ID';
COMMENT ON COLUMN tree_holes.user_id IS '用户ID(加密存储)';
COMMENT ON COLUMN tree_holes.anonymous_name IS '匿名昵称';
COMMENT ON COLUMN tree_holes.content IS '内容';
COMMENT ON COLUMN tree_holes.star_count IS '星光数';
COMMENT ON COLUMN tree_holes.comment_count IS '评论数';
COMMENT ON COLUMN tree_holes.status IS '状态：0-待审核，1-已发布，2-已屏蔽';
COMMENT ON COLUMN tree_holes.is_deleted IS '是否删除：false-否，true-是';
COMMENT ON COLUMN tree_holes.version IS '版本号';
COMMENT ON COLUMN tree_holes.created_time IS '创建时间';
COMMENT ON COLUMN tree_holes.updated_time IS '更新时间';

-- 创建更新时间触发器
CREATE TRIGGER update_tree_holes_timestamp
BEFORE UPDATE ON tree_holes
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- -----------------------------------------------------
-- 树洞星光表 - 存储用户点亮的星光
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS tree_hole_stars (
  id BIGSERIAL PRIMARY KEY,
  tree_hole_id BIGINT NOT NULL REFERENCES tree_holes(id) ON DELETE CASCADE,
  user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  message VARCHAR(100),
  is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
  version INTEGER NOT NULL DEFAULT 1,
  created_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE UNIQUE INDEX idx_tree_hole_stars_hole_user ON tree_hole_stars(tree_hole_id, user_id);
CREATE INDEX idx_tree_hole_stars_user_id ON tree_hole_stars(user_id);

-- 添加注释
COMMENT ON TABLE tree_hole_stars IS '树洞星光表';
COMMENT ON COLUMN tree_hole_stars.id IS '星光ID';
COMMENT ON COLUMN tree_hole_stars.tree_hole_id IS '树洞ID';
COMMENT ON COLUMN tree_hole_stars.user_id IS '用户ID';
COMMENT ON COLUMN tree_hole_stars.message IS '留言';
COMMENT ON COLUMN tree_hole_stars.is_deleted IS '是否删除：false-否，true-是';
COMMENT ON COLUMN tree_hole_stars.version IS '版本号';
COMMENT ON COLUMN tree_hole_stars.created_time IS '创建时间';
COMMENT ON COLUMN tree_hole_stars.updated_time IS '更新时间';

-- 创建更新时间触发器
CREATE TRIGGER update_tree_hole_stars_timestamp
BEFORE UPDATE ON tree_hole_stars
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- -----------------------------------------------------
-- 会员订阅表 - 存储用户会员信息
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS subscriptions (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  plan_type SMALLINT NOT NULL,
  start_date DATE NOT NULL,
  end_date DATE,
  auto_renew BOOLEAN NOT NULL DEFAULT FALSE,
  status SMALLINT NOT NULL,
  is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
  version INTEGER NOT NULL DEFAULT 1,
  created_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX idx_subscriptions_user_id ON subscriptions(user_id);
CREATE INDEX idx_subscriptions_end_date ON subscriptions(end_date);

-- 添加注释
COMMENT ON TABLE subscriptions IS '会员订阅表';
COMMENT ON COLUMN subscriptions.id IS '订阅ID';
COMMENT ON COLUMN subscriptions.user_id IS '用户ID';
COMMENT ON COLUMN subscriptions.plan_type IS '套餐类型：1-月度，2-年度，3-终身';
COMMENT ON COLUMN subscriptions.start_date IS '开始日期';
COMMENT ON COLUMN subscriptions.end_date IS '结束日期';
COMMENT ON COLUMN subscriptions.auto_renew IS '是否自动续费：false-否，true-是';
COMMENT ON COLUMN subscriptions.status IS '状态：1-有效，2-已过期，3-已取消';
COMMENT ON COLUMN subscriptions.is_deleted IS '是否删除：false-否，true-是';
COMMENT ON COLUMN subscriptions.version IS '版本号';
COMMENT ON COLUMN subscriptions.created_time IS '创建时间';
COMMENT ON COLUMN subscriptions.updated_time IS '更新时间';

-- 创建更新时间触发器
CREATE TRIGGER update_subscriptions_timestamp
BEFORE UPDATE ON subscriptions
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- -----------------------------------------------------
-- 支付记录表 - 存储支付信息
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS payment_records (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  subscription_id BIGINT REFERENCES subscriptions(id) ON DELETE SET NULL,
  amount DECIMAL(10, 2) NOT NULL,
  currency VARCHAR(10) NOT NULL DEFAULT 'CNY',
  payment_method VARCHAR(50) NOT NULL,
  transaction_id VARCHAR(100),
  status SMALLINT NOT NULL,
  payment_time TIMESTAMP,
  is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
  version INTEGER NOT NULL DEFAULT 1,
  created_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX idx_payment_records_user_id ON payment_records(user_id);
CREATE INDEX idx_payment_records_subscription_id ON payment_records(subscription_id);

-- 添加注释
COMMENT ON TABLE payment_records IS '支付记录表';
COMMENT ON COLUMN payment_records.id IS '支付ID';
COMMENT ON COLUMN payment_records.user_id IS '用户ID';
COMMENT ON COLUMN payment_records.subscription_id IS '订阅ID';
COMMENT ON COLUMN payment_records.amount IS '金额';
COMMENT ON COLUMN payment_records.currency IS '货币';
COMMENT ON COLUMN payment_records.payment_method IS '支付方式';
COMMENT ON COLUMN payment_records.transaction_id IS '交易ID';
COMMENT ON COLUMN payment_records.status IS '状态：1-待支付，2-已支付，3-已退款，4-已取消';
COMMENT ON COLUMN payment_records.payment_time IS '支付时间';
COMMENT ON COLUMN payment_records.is_deleted IS '是否删除：false-否，true-是';
COMMENT ON COLUMN payment_records.version IS '版本号';
COMMENT ON COLUMN payment_records.created_time IS '创建时间';
COMMENT ON COLUMN payment_records.updated_time IS '更新时间';

-- 创建更新时间触发器
CREATE TRIGGER update_payment_records_timestamp
BEFORE UPDATE ON payment_records
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- -----------------------------------------------------
-- 系统配置表 - 存储系统配置信息
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS system_configs (
  id BIGSERIAL PRIMARY KEY,
  config_key VARCHAR(50) NOT NULL UNIQUE,
  config_value TEXT NOT NULL,
  description VARCHAR(255),
  is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
  version INTEGER NOT NULL DEFAULT 1,
  created_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE UNIQUE INDEX idx_system_configs_config_key ON system_configs(config_key);

-- 添加注释
COMMENT ON TABLE system_configs IS '系统配置表';
COMMENT ON COLUMN system_configs.id IS '配置ID';
COMMENT ON COLUMN system_configs.config_key IS '配置键';
COMMENT ON COLUMN system_configs.config_value IS '配置值';
COMMENT ON COLUMN system_configs.description IS '描述';
COMMENT ON COLUMN system_configs.is_deleted IS '是否删除：false-否，true-是';
COMMENT ON COLUMN system_configs.version IS '版本号';
COMMENT ON COLUMN system_configs.created_time IS '创建时间';
COMMENT ON COLUMN system_configs.updated_time IS '更新时间';