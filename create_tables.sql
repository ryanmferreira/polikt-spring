-- 1. Tabela users
CREATE TABLE users (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Tabela tag
CREATE TABLE tag (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- 3. Tabela agency
CREATE TABLE agency (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    contact VARCHAR(255)
);

-- 4. Tabela news
CREATE TABLE news (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    summary TEXT,
    content TEXT NOT NULL,
    cover_image VARCHAR(512),
    published_at TIMESTAMP,
    upvotes INT DEFAULT 0,
    user_id INT NOT NULL,
    CONSTRAINT fk_news_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 5. Tabela news_tag (N:N)
CREATE TABLE news_tag (
    news_id INT NOT NULL,
    tag_id INT NOT NULL,
    PRIMARY KEY (news_id, tag_id),
    CONSTRAINT fk_news_tag_news FOREIGN KEY (news_id) REFERENCES news(id) ON DELETE CASCADE,
    CONSTRAINT fk_news_tag_tag FOREIGN KEY (tag_id) REFERENCES tag(id) ON DELETE CASCADE
);

-- 6. Tabela guide
CREATE TABLE guide (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    image VARCHAR(512),
    agency_id INT,
    user_id INT NOT NULL,
    CONSTRAINT fk_guide_agency FOREIGN KEY (agency_id) REFERENCES agency(id) ON DELETE SET NULL,
    CONSTRAINT fk_guide_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 7. Tabela guide_tag (N:N)
CREATE TABLE guide_tag (
    guide_id INT NOT NULL,
    tag_id INT NOT NULL,
    PRIMARY KEY (guide_id, tag_id),
    CONSTRAINT fk_guide_tag_guide FOREIGN KEY (guide_id) REFERENCES guide(id) ON DELETE CASCADE,
    CONSTRAINT fk_guide_tag_tag FOREIGN KEY (tag_id) REFERENCES tag(id) ON DELETE CASCADE
);

-- 8. Tabela guide_step
CREATE TABLE guide_step (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    guide_id INT NOT NULL,
    position INT NOT NULL,
    image VARCHAR(512),
    content TEXT NOT NULL,
    CONSTRAINT fk_guide_step_guide FOREIGN KEY (guide_id) REFERENCES guide(id) ON DELETE CASCADE
);

-- 9. Tabela course
CREATE TABLE course (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    cover_image VARCHAR(512),
    user_id INT NOT NULL,
    CONSTRAINT fk_course_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 10. Tabela course_tag (N:N)
CREATE TABLE course_tag (
    course_id INT NOT NULL,
    tag_id INT NOT NULL,
    PRIMARY KEY (course_id, tag_id),
    CONSTRAINT fk_course_tag_course FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE,
    CONSTRAINT fk_course_tag_tag FOREIGN KEY (tag_id) REFERENCES tag(id) ON DELETE CASCADE
);

-- 11. Tabela course_module
CREATE TABLE course_module (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    course_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    position INT NOT NULL,
    CONSTRAINT fk_course_module_course FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE
);

-- 12. Tabela module_content
CREATE TABLE module_content (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    module_id INT NOT NULL,
    position INT NOT NULL,
    content TEXT NOT NULL,
    CONSTRAINT fk_module_content_module FOREIGN KEY (module_id) REFERENCES course_module(id) ON DELETE CASCADE
);