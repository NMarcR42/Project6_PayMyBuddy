CREATE TABLE users (
  user_id     INT AUTO_INCREMENT PRIMARY KEY,
  username    VARCHAR(50) NOT NULL UNIQUE,
  email       VARCHAR(100) NOT NULL UNIQUE,
  password    VARCHAR(255) NOT NULL,
  created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE connections (
  user_id        INT NOT NULL,
  connection_id  INT NOT NULL,
  created_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (user_id, connection_id),
  FOREIGN KEY (user_id)       REFERENCES users(user_id),
  FOREIGN KEY (connection_id) REFERENCES users(user_id)
);

CREATE TABLE transactions (
  transaction_id INT AUTO_INCREMENT PRIMARY KEY,
  sender_id      INT NOT NULL,
  receiver_id    INT NOT NULL,
  description    TEXT,
  amount         DOUBLE NOT NULL CHECK (amount > 0),
  fee            DOUBLE NOT NULL DEFAULT 0.005 * amount,
  status         VARCHAR(20) NOT NULL DEFAULT 'PENDING',
  created_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (sender_id)   REFERENCES users(user_id),
  FOREIGN KEY (receiver_id) REFERENCES users(user_id),
  INDEX (sender_id),
  INDEX (receiver_id)
);
