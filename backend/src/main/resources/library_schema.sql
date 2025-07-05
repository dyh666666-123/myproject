-- 用户表
CREATE TABLE user (
  id INT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL,
  email VARCHAR(100),
  status INT DEFAULT 1
);

-- 角色表
CREATE TABLE role (
  id INT PRIMARY KEY AUTO_INCREMENT,
  role_name VARCHAR(50) NOT NULL UNIQUE,
  description VARCHAR(100)
);

-- 用户角色关联表
CREATE TABLE user_role (
  id INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT NOT NULL,
  role_id INT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES user(id),
  FOREIGN KEY (role_id) REFERENCES role(id)
);

-- 图书分类表
CREATE TABLE book_category (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  parent_id INT
);

-- 图书表
CREATE TABLE book (
  id INT PRIMARY KEY AUTO_INCREMENT,
  isbn VARCHAR(20) NOT NULL UNIQUE,
  title VARCHAR(200) NOT NULL,
  author VARCHAR(100),
  category_id INT,
  stock INT DEFAULT 0,
  FOREIGN KEY (category_id) REFERENCES book_category(id)
);

-- 借阅记录表
CREATE TABLE borrow_record (
  id INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT NOT NULL,
  book_id INT NOT NULL,
  borrow_date DATETIME,
  return_date DATETIME,
  status VARCHAR(50) DEFAULT 'BORROWED', -- 状态: BORROWED, RETURNED, OVERDUE
  FOREIGN KEY (user_id) REFERENCES user(id),
  FOREIGN KEY (book_id) REFERENCES book(id)
);

-- 初始化角色
INSERT INTO role (role_name, description) VALUES ('ADMIN', '管理员'), ('USER', '普通用户');

-- 初始化管理员用户（密码为123456）
INSERT INTO user (username, password, email, status) VALUES ('admin', '$2a$10$ijX8TJgb4zCkY2F1HuN2juYp7.AcgGJkeL9lzl2J3ZSxIcx0asWPy', 'admin@example.com', 1);

-- 管理员用户分配admin角色（假设admin用户id=1，admin角色id=1）
INSERT INTO user_role (user_id, role_id) VALUES (1, 1);

-- 初始化图书分类
INSERT INTO book_category (name, parent_id) VALUES ('A类', NULL), ('B类', NULL), ('A1类', 1);

-- 初始化图书
INSERT INTO book (isbn, title, author, category_id, stock) VALUES
('9787302423287', 'Java编程思想', 'Bruce Eckel', 1, 5),
('9787115428028', '深入理解计算机系统', 'Randal E. Bryant', 2, 3),
('9787302291756', '算法导论', 'Thomas H. Cormen', 3, 2); 