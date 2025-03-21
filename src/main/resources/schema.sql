USE ganma_shop_db;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS user;
CREATE TABLE user
(
  id                    VARCHAR(255) NOT NULL PRIMARY KEY COMMENT 'User ID',
  email                 VARCHAR(255) NOT NULL COMMENT 'email',
  username              VARCHAR(255) NOT NULL COMMENT 'Username',
  password              VARCHAR(255) NOT NULL COMMENT 'Password',
  name                  VARCHAR(255) COMMENT '收货人名字',
  phone                 VARCHAR(255) COMMENT 'Phone number',
  address               VARCHAR(255) COMMENT '收货地址',
  user_type             VARCHAR(255) NOT NULL DEFAULT 'Customer' COMMENT 'Customer or Admin',
  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO user(id, email, username, password, phone, address, user_type)
VALUES
('AD1','admin@gmail.com', 'admin', '$2a$10$okh6g45A6PlC9EMWanGrMu/90OnVQm5H.mmZBXwAsmXkuBfaTmoua', 012345, '5,jalan tuah', 'admin'),--password: 123
('AD2','admin_2@gmail.com', 'admin2', '$2a$10$okh6g45A6PlC9EMWanGrMu/90OnVQm5H.mmZBXwAsmXkuBfaTmoua', 1331354002, '5,jalan tuah kuala lumpur', 'admin'),
('AD3','admin_3@gmail.com', 'admin3', '$2a$10$okh6g45A6PlC9EMWanGrMu/90OnVQm5H.mmZBXwAsmXkuBfaTmoua', 11335566441, '11-10,jalan klang,kuala lumpur', 'admin'),
('3','customer@gmail.com', 'customer', '$2a$10$okh6g45A6PlC9EMWanGrMu/90OnVQm5H.mmZBXwAsmXkuBfaTmoua',01234567, '10,jalan pelangi,7500,Johor', 'customer'),
('2','jason@gmail.com', 'Jason', '$2a$10$okh6g45A6PlC9EMWanGrMu/90OnVQm5H.mmZBXwAsmXkuBfaTmoua', 0102355466, '2,Jalan Kan,Taman Kan,53211,Kuala Lumpur', 'customer');--password: 123
-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS role;
CREATE TABLE role (
  id                VARCHAR(255) NOT NULL PRIMARY KEY COMMENT 'Role ID',
  name              VARCHAR(255) DEFAULT NULL COMMENT 'Role name',
  status            CHAR(1) DEFAULT '0' COMMENT 'Role status (0 normal, 1 disabled)',
  remark            VARCHAR(255) DEFAULT NULL COMMENT 'Remarks',
  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT='Role table';

INSERT INTO role(id, name)
VALUES
('1','Admin'),
('2','Customer');

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS user_role;
CREATE TABLE user_role
(
  user_id       VARCHAR(255) NOT NULL COMMENT 'User ID',
  role_id       VARCHAR(255) NOT NULL COMMENT 'Role ID',
  PRIMARY KEY (user_id,role_id),
  FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
  FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE
);

INSERT INTO user_role(user_id, role_id)
VALUES
('1','1'),
('2','2');

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS permission;
CREATE TABLE permission (
  id                VARCHAR(255) NOT NULL PRIMARY KEY COMMENT 'Permission ID',
  name              VARCHAR(255) NOT NULL COMMENT 'Permission name',
  status            CHAR(1) DEFAULT '0' COMMENT 'Permission status (0 normal, 1 disabled)',
  code              VARCHAR(255) COMMENT 'Permission code',
  remark            VARCHAR(255) COMMENT 'Remarks',
  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT='Permission table';

INSERT INTO permission(id, name, code)
VALUES
('1','System management permission','system:admin'),
('2','Customer permission','system:customer');

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS role_permission;
CREATE TABLE role_permission (
  role_id               VARCHAR(255) NOT NULL COMMENT 'Role ID',
  permission_id         VARCHAR(255) NOT NULL COMMENT 'Permission ID',
  PRIMARY KEY (role_id,permission_id),
  FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE,
  FOREIGN KEY (permission_id) REFERENCES permission(id) ON DELETE CASCADE
);

INSERT INTO role_permission(role_id, permission_id)
VALUES
('1','1'), ('1','2'), -- Admin has all permissions
('2','2');            -- Customer permission

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS category;
CREATE TABLE category (
  id                    VARCHAR(255) NOT NULL PRIMARY KEY COMMENT 'Category ID',
  type_name             VARCHAR(255) NOT NULL COMMENT 'Category Type',
  create_time           TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation Time',
  update_time           TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update Time'
);

INSERT INTO category(id, type_name)
VALUES
('1', '天然健康产品'),
('2', '保健品');

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS product;
CREATE TABLE product (
  id                    VARCHAR(255) NOT NULL PRIMARY KEY COMMENT 'product ID',
  user_id               VARCHAR(255) NOT NULL COMMENT 'user ID',
  category_id           VARCHAR(255) NOT NULL,
  name                  VARCHAR(255) NOT NULL COMMENT 'product name',
  description           VARCHAR(255) NOT NULL COMMENT 'Additional Information',
  price                 DOUBLE NOT NULL COMMENT 'Items price',
  image_name            VARCHAR(255) NOT NULL,
  create_time           TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation Time',
  update_time           TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update Time',
  FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);
INSERT INTO product(id, user_id, category_id, name, description, price,image_name)
VALUES
('1','AD1', '1', 'V18', '-', 97,'V18.jpg'),
('2','AD1', '1', 'GSure', '-', 30,'GSure.jpg'),
('3','AD1', '1', 'VPlus', '-', 50,'VPlus.jpg'),
('4','AD1', '1', 'VGrains', '-', 99.99,'VGrains.jpg'),
('5','AD1', '1', 'Fruit Drink (Mixed)', '-', 51,'MixedBerries.jpg'),
('6','AD1', '1', 'Fruit Drink (Mango)', '-', 13.10,'Mango.jpg');

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS cart;
CREATE TABLE cart
(
  id                        VARCHAR(255) NOT NULL PRIMARY KEY COMMENT 'cart id',
  product_id                VARCHAR(255) COMMENT 'product id',
  user_id                   VARCHAR(255) COMMENT 'user id',
  quantity                  INTEGER COMMENT 'quantity',
  price                     DOUBLE  COMMENT 'Price',
  create_time               TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation Time',
  update_time               TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update Time',
  FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);
INSERT INTO cart(id, product_id, user_id,quantity,price)
VALUES
('1','2','1',1, 30.00),
('2','4','2',2, 199.98);

SET FOREIGN_KEY_CHECKS = 1;

DROP TABLE IF EXISTS orders;
CREATE TABLE orders
(
  id                         VARCHAR(255) NOT NULL PRIMARY KEY COMMENT 'order id',
  user_id                    VARCHAR(255) COMMENT 'user id',
  product_id                 VARCHAR(255) COMMENT 'product id',
  quantity                   INTEGER COMMENT 'quantity',
  price                      DOUBLE  COMMENT 'Price',
  status                     VARCHAR(255) DEFAULT 'Pending' COMMENT 'Order Status',
  create_time                TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation Time',
  update_time               TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update Time',
  FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);

INSERT INTO orders(id,user_id,product_id,quantity,price)
VALUES
('1','2','2','2',60.00),
('2','3','6','3',39.30),
('3','2','4','1',99.99),
('4','3','3','1',50.00),
('5','2','1','1',97.00);


SET FOREIGN_KEY_CHECKS = 1;

DROP TABLE IF EXISTS favourite;
CREATE TABLE favourite
(
  id                         VARCHAR(255) NOT NULL PRIMARY KEY COMMENT 'favourite id',
  user_id                    VARCHAR(255) COMMENT 'user id',
  product_id                 VARCHAR(255) COMMENT 'product id',
  FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);

INSERT INTO favourite(id,user_id,product_id)
VALUES
('1','2','2'),
('2','2','3'),
('3','2','4');

SET FOREIGN_KEY_CHECKS = 1;

