 create database Website;
  use Website;
CREATE TABLE cust_details(
    id INT AUTO_INCREMENT PRIMARY KEY,
    stripe_token VARCHAR(255),
    name VARCHAR(255),
    email VARCHAR(255),
    exp_month INT,
    exp_year INT,
    last4 VARCHAR(4),
    brand VARCHAR(50),
    amount DECIMAL(10, 2),
    client_ip VARCHAR(50),
    payment_status VARCHAR(50),
    shipping_name VARCHAR(255),
    shipping_address VARCHAR(255),
    shipping_city VARCHAR(100),
    shipping_state VARCHAR(100),
    shipping_zip VARCHAR(20),
    shipping_country VARCHAR(100)
);

