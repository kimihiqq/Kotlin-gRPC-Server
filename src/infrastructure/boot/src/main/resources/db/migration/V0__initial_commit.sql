CREATE TABLE IF NOT EXISTS key_value (
  key_column VARCHAR(50) NOT NULL,
  value_column TEXT,
  PRIMARY KEY (key_column)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
