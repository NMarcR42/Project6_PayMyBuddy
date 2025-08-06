DELIMITER $$

CREATE TRIGGER trg_users_hash_password
BEFORE INSERT ON users
FOR EACH ROW
BEGIN
  SET NEW.password = SHA2(NEW.password, 256);
END$$

CREATE TRIGGER trg_users_hash_password_update
BEFORE UPDATE ON users
FOR EACH ROW
BEGIN
  IF OLD.password <> NEW.password THEN
    SET NEW.password = SHA2(NEW.password, 256);
  END IF;
END$$

DELIMITER ;
