-- Tạo database
-- DROP DATABASE face_book;

CREATE DATABASE face_book;
USE face_book;


-- ====================================
-- 1. Bảng ROLES: Lưu thông tin vai trò người dùng
-- ====================================
CREATE TABLE roles (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
	role_name ENUM('ADMIN','USER'),
    description TEXT,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ====================================
-- 2. Bảng PERMISSIONS: Danh sách các quyền hạn
-- ====================================
CREATE TABLE permissions (
    permission_id INT AUTO_INCREMENT PRIMARY KEY,
    permission_name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    created DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ====================================
-- 3. Bảng ROLE_PERMISSIONS: Liên kết quyền với từng vai trò
-- ====================================
CREATE TABLE role_permissions (
    role_id INT NOT NULL,
    permission_id INT NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions(permission_id) ON DELETE CASCADE
);

-- ====================================
-- 4. Bảng USERS: Lưu thông tin người dùng
-- ====================================
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20),
    is_verified BOOLEAN DEFAULT FALSE,
    registration_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    last_login_date DATETIME,
    account_status ENUM('ACTIVE', 'INACTIVE', 'BLOCKED','DELETED') DEFAULT 'ACTIVE'
);

-- ====================================
-- 5. Bảng USER_ROLES: Liên kết users ↔ roles (1 người có thể có nhiều vai trò)
-- ====================================
CREATE TABLE user_roles (
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE
);

-- ====================================
-- 6. Bảng USER_PROFILES: Thông tin cá nhân người dùng
-- ====================================
CREATE TABLE user_profiles (
    profile_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    birthdate DATE NOT NULL,
    gender ENUM('MALE', 'FEMALE', 'OTHER') NOT NULL,
    looking_for ENUM('MALE', 'FEMALE', 'OTHER') NOT NULL,
    bio TEXT,
    height INT,
    weight INT,
    location VARCHAR(255),
    job_title VARCHAR(255),
    company VARCHAR(100),
    education VARCHAR(100),
    description TEXT,
    profile_complete BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- ====================================
-- 7. Bảng PHOTOS: Lưu ảnh người dùng
-- ====================================
CREATE TABLE photos (
    photo_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    photo_url VARCHAR(255) NOT NULL,
    is_profile_picture BOOLEAN DEFAULT FALSE,
    upload_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    is_approved BOOLEAN DEFAULT True,
    is_story boolean default false,
    reviewed_by INT,
    review_date DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (reviewed_by) REFERENCES users(user_id) ON DELETE SET NULL
);

-- ====================================
-- 8. Bảng INTERESTS: Danh sách sở thích
-- ====================================
CREATE TABLE interests (
    interest_id INT AUTO_INCREMENT PRIMARY KEY,
    interest_name VARCHAR(100) NOT NULL UNIQUE,
    category VARCHAR(100)
);

-- ====================================
-- 9. Bảng USER_INTERESTS: Liên kết sở thích với người dùng
-- ====================================
CREATE TABLE user_interests (
    user_interests_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    interest_id INT NOT NULL,
    UNIQUE (user_id, interest_id), -- Đảm bảo không trùng lặp user_id & interest_id
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (interest_id) REFERENCES interests(interest_id) ON DELETE CASCADE
);

-- ====================================
-- 10. Bảng MATCHES: Lưu trạng thái ghép đôi
-- ====================================

-- drop table messages;
-- drop table matches;
CREATE TABLE matches (
    match_id INT AUTO_INCREMENT PRIMARY KEY,
    sender_id INT NOT NULL, -- người gửi request match
    receiver_id INT NOT NULL, -- người nhận request,có thể xác nhận match hoặc reject
    match_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    status ENUM('PENDING', 'MATCHED', 'REJECTED') DEFAULT 'PENDING',
    FOREIGN KEY (sender_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- ====================================
-- 11. Bảng MESSAGES: Lưu tin nhắn giữa người dùng
-- ====================================
CREATE TABLE messages (
    message_id INT AUTO_INCREMENT PRIMARY KEY,
    match_id INT NOT NULL,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    message_text TEXT NOT NULL,
    sent_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    is_read BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (match_id) REFERENCES matches(match_id) ON DELETE CASCADE,
    FOREIGN KEY (sender_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- drop table notifications
-- ====================================
-- 12. Bảng NOTIFICATIONS: Lưu thông báo
-- ====================================
CREATE TABLE notifications (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    notification_type ENUM('MATCH', 'MESSAGE', 'LIKE','POST', 'SYSTEM') NOT NULL,
    content TEXT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ====================================
-- 13. Bảng USER_NOTIFICATIONS: Liên kết users ↔ notifications
-- ====================================
CREATE TABLE user_notifications (
    user_id INT NOT NULL,
    notification_id INT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (user_id, notification_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (notification_id) REFERENCES notifications(notification_id) ON DELETE CASCADE
);

-- ====================================
-- 14. Bảng SUBSCRIPTION_PLANS: Lưu các gói đăng ký
-- ====================================
CREATE TABLE subscription_plans (
    plan_id INT AUTO_INCREMENT PRIMARY KEY,
    plan_name VARCHAR(50) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    duration_days INT NOT NULL,
    features TEXT,
    is_active BOOLEAN DEFAULT TRUE
);

-- ====================================
-- 15. Bảng USER_SUBSCRIPTIONS: Lưu thông tin đăng ký của người dùng
-- ====================================
CREATE TABLE user_subscriptions (
    subscription_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    plan_id INT NOT NULL,
    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,
    payment_status ENUM('PENDING', 'COMPLETED', 'FAILED', 'REFUNED') NOT NULL,
    subscription_status ENUM('ACTIVE', 'EXPIRED', 'CANCELLED') DEFAULT 'ACTIVE',
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (plan_id) REFERENCES subscription_plans(plan_id)
);


CREATE TABLE report_types (
    report_type_id INT AUTO_INCREMENT PRIMARY KEY,
    type_name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
);

-- ====================================
-- 16. Bảng REPORTS: Lưu báo cáo người dùng
-- ====================================

CREATE TABLE reports (
     report_id INT AUTO_INCREMENT PRIMARY KEY,
     reporter_id INT NOT NULL,  -- Người tố cáo
     reported_id INT NOT NULL,  -- Người bị tố cáo
     report_type_id INT NOT NULL, -- Loại vi phạm
     additional_details TEXT, -- Chi tiết thêm (nếu có)
     report_date DATETIME DEFAULT CURRENT_TIMESTAMP,
     status_report ENUM('PENDING', 'CHECKING', 'RESOLVED') DEFAULT 'PENDING',
     reviewed_by INT,
     review_date DATETIME,
     FOREIGN KEY (reporter_id) REFERENCES users(user_id) ON DELETE CASCADE,
     FOREIGN KEY (reported_id) REFERENCES users(user_id) ON DELETE CASCADE,
     FOREIGN KEY (report_type_id) REFERENCES report_types(report_type_id) ON DELETE CASCADE,
     FOREIGN KEY (reviewed_by) REFERENCES users(user_id) ON DELETE SET NULL
 )	;
 
 -- Bảng user_preferences lưu tùy chọn tìm kiếm của người dùng
 CREATE TABLE user_preferences (
    preference_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    min_age INT DEFAULT 18,
    max_age INT DEFAULT 100,
    distance_km INT DEFAULT 50,
    show_me_to_others BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);


-- Chèn dữ liệu vào bảng users
INSERT INTO users (email, password, phone_number, is_verified, registration_date, last_login_date, account_status)
VALUES
('alice@example.com', 'hashed_password_1', '0987654321', TRUE, NOW(), NOW(), 'ACTIVE'),
('bob@example.com', 'hashed_password_2', '0976543210', FALSE, NOW(), NULL, 'ACTIVE'),
('charlie@example.com', 'hashed_password_3', '0965432109', TRUE, NOW(), NOW(), 'ACTIVE'),
('david@example.com', 'hashed_password_4', '0954321098', TRUE, NOW(), NOW(), 'ACTIVE'),
('eve@example.com', 'hashed_password_5', '0943210987', FALSE, NOW(), NULL, 'ACTIVE'),
('frank@example.com', 'hashed_password_6', '0932109876', TRUE, NOW(), NOW(), 'ACTIVE'),
('grace@example.com', 'hashed_password_7', '0921098765', FALSE, NOW(), NULL, 'ACTIVE'),
('henry@example.com', 'hashed_password_8', '0910987654', TRUE, NOW(), NOW(), 'ACTIVE'),
('irene@example.com', 'hashed_password_9', '0909876543', TRUE, NOW(), NOW(), 'DELETED'),
('jack@example.com', 'hashed_password_10', '0898765432', TRUE, NOW(), NOW(), 'ACTIVE'),
('kate@example.com', 'hashed_password_11', '0887654321', FALSE, NOW(), NULL, 'DELETED'),
('leo@example.com', 'hashed_password_12', '0876543210', TRUE, NOW(), NOW(), 'ACTIVE'),
('mia@example.com', 'hashed_password_13', '0865432109', FALSE, NOW(), NULL, 'ACTIVE'),
('nathan@example.com', 'hashed_password_14', '0854321098', TRUE, NOW(), NOW(), 'ACTIVE'),
('olivia@example.com', 'hashed_password_15', '0843210987', TRUE, NOW(), NOW(), 'ACTIVE');

-- Chèn dữ liệu vào bảng user_profiles
INSERT INTO user_profiles (user_id, full_name, birthdate, gender, looking_for, bio, height, weight, location, job_title, company, education, profile_complete)
VALUES
(1, 'Alice Johnson', '1995-03-12', 'MALE', 'FEMALE', 'Love hiking and coffee!', 165, 55, 'New York', 'Software Engineer', 'Google', 'MIT', TRUE),
(2, 'Bob Smith', '1992-07-24', 'MALE', 'FEMALE', 'Gamer and foodie.', 180, 75, 'Los Angeles', 'Graphic Designer', 'Freelance', 'UCLA', FALSE),
(3, 'Charlie Brown', '1998-10-05', 'MALE', 'MALE', 'Music producer.', 175, 70, 'Chicago', 'Musician', 'Independent', 'Berklee', TRUE),
(4, 'David Williams', '1990-05-18', 'MALE', 'FEMALE', 'Fitness trainer.', 182, 80, 'San Francisco', 'Personal Trainer', 'GymX', 'Stanford', TRUE),
(5, 'Eve Adams', '1996-12-01', 'MALE', 'FEMALE', 'Passionate about tech.', 160, 50, 'Seattle', 'Data Scientist', 'Amazon', 'Harvard', FALSE),
(6, 'Frank Miller', '1993-06-30', 'MALE', 'MALE', 'Adventurer and writer.', 178, 72, 'Denver', 'Journalist', 'CNN', 'NYU', TRUE),
(7, 'Grace Lee', '1997-09-21', 'MALE', 'FEMALE', 'Dog lover.', 162, 53, 'Austin', 'Veterinarian', 'PetCare', 'Texas A&M', TRUE),
(8, 'Henry Clark', '1994-02-14', 'MALE', 'MALE', 'Coffee addict.', 185, 78, 'Boston', 'Barista', 'Starbucks', 'BU', FALSE),
(9, 'Irene Foster', '1991-11-29', 'MALE', 'FEMALE', 'Entrepreneur.', 167, 58, 'Miami', 'Startup Founder', 'Self-employed', 'Wharton', TRUE),
(10, 'Jack Martinez', '1995-08-10', 'MALE', 'FEMALE', 'Photographer.', 172, 68, 'Portland', 'Photographer', 'Freelance', 'RISD', TRUE),
(11, 'Kate Lewis', '1999-04-15', 'MALE', 'MALE', 'Passionate about cooking.', 159, 49, 'Houston', 'Chef', 'RestaurantX', 'Culinary Institute', FALSE),
(12, 'Leo White', '1990-01-25', 'MALE', 'FEMALE', 'Love traveling.', 177, 74, 'Phoenix', 'Travel Blogger', 'Freelance', 'UCLA', TRUE),
(13, 'Mia Robinson', '1993-07-08', 'MALE', 'MALE', 'Painter and artist.', 163, 52, 'San Diego', 'Artist', 'GalleryX', 'Yale', FALSE),
(14, 'Nathan Green', '1996-05-20', 'MALE', 'FEMALE', 'Nature lover.', 180, 76, 'Nashville', 'Environmental Scientist', 'NGO', 'Berkeley', TRUE),
(15, 'Olivia Carter', '1992-03-17', 'MALE', 'FEMALE', 'Yoga instructor.', 166, 56, 'Atlanta', 'Yoga Teacher', 'Wellness Studio', 'Duke', TRUE);


create table invalidated_token 
(
	token_id INT AUTO_INCREMENT PRIMARY KEY,
    token text,
    expiry_time date
);

-- drop table likes;
CREATE TABLE likes (
    like_id INT AUTO_INCREMENT PRIMARY KEY,
    sender_id INT NOT NULL, -- Người gửi like
    receiver_id INT NOT NULL, -- Người nhận like
    like_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE', -- Trạng thái like (active: đang like, inactive: đã dislike)
    FOREIGN KEY (sender_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES users(user_id) ON DELETE CASCADE,
    UNIQUE KEY unique_like (sender_id, receiver_id) -- Đảm bảo mỗi cặp sender-receiver chỉ có một bản ghi
);

CREATE TABLE video (
    video_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    video_url VARCHAR(255) NOT NULL,
    upload_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    is_approved BOOLEAN DEFAULT FALSE,
	is_story boolean default false,
    reviewed_by INT,
    review_date DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (reviewed_by) REFERENCES users(user_id) ON DELETE SET NULL
);

create table posts(
	post_id int auto_increment primary key,
    content text,
    upload_date datetime default current_timestamp,
    is_approved boolean default true,
    reviewed_by int,
    reviewed_date datetime,
    is_public boolean default true,
    status ENUM('ACTIVE', 'BLOCKED','DELETED') DEFAULT 'ACTIVE',
    FOREIGN KEY (reviewed_by) REFERENCES users(user_id) ON DELETE SET NULL
);

create table user_posts(
	user_id int not null,
    post_id int not null,
    primary key (user_id,post_id),
    upload boolean,
    share boolean,
    save boolean,
    foreign key (user_id) references users(user_id) on delete cascade,
    foreign key (post_id) references posts(post_id) on delete cascade
);

-- ========================
-- Bảng liên kết post với nhiều ảnh (MỚI)
-- ========================
CREATE TABLE post_photos (
    post_id INT NOT NULL,
    photo_id INT NOT NULL,
    PRIMARY KEY (post_id, photo_id),
    FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE CASCADE,
    FOREIGN KEY (photo_id) REFERENCES photos(photo_id) ON DELETE CASCADE
);

-- ========================
-- Bảng liên kết post với nhiều video (MỚI)
-- ========================
CREATE TABLE post_videos (
    post_id INT NOT NULL,
    video_id INT NOT NULL,
    PRIMARY KEY (post_id, video_id),
    FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE CASCADE,
    FOREIGN KEY (video_id) REFERENCES video(video_id) ON DELETE CASCADE
);

-- drop table comments;

CREATE TABLE comments (
    comment_id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT NOT NULL,
    user_id INT NOT NULL,
    content TEXT NOT NULL,
    comment_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    is_edited BOOLEAN DEFAULT FALSE,
    is_deleted BOOLEAN DEFAULT FALSE,
    level int not null,
    parent_comment_id INT DEFAULT NULL, -- để hỗ trợ comment lồng nhau (reply)
    FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (parent_comment_id) REFERENCES comments(comment_id) ON DELETE CASCADE
);

CREATE TABLE emotions (
    emotion_id INT AUTO_INCREMENT PRIMARY KEY,
    emotion_name enum('LIKE','LOVE','HAHA','WOW','SAD','ANGRY')
);

CREATE TABLE reactions (
    reaction_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,           -- Người thả cảm xúc
    emotion_id INT NOT NULL,        -- Loại cảm xúc được thả
    
    content_id INT NOT NULL,        -- ID của nội dung (post_id, photo_id, video_id, comment_id)
    content_type ENUM('POST', 'PHOTO', 'VIDEO', 'COMMENT') NOT NULL, -- Loại nội dung

    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (emotion_id) REFERENCES emotions(emotion_id) ON DELETE CASCADE,
    
    -- Đây là khóa UNIQUE quan trọng nhất để đảm bảo mỗi user chỉ có 1 reaction trên 1 content
    UNIQUE KEY unique_reaction (user_id, content_id, content_type)
);
INSERT INTO emotions (emotion_name) VALUES
('LIKE'),
('LOVE'),
('HAHA'),
('WOW'),
('SAD'),
('ANGRY');
