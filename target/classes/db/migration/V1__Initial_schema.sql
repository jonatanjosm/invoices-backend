-- Create invoices table
CREATE TABLE invoices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    invoice_number VARCHAR(255) NOT NULL UNIQUE,
    customer_name VARCHAR(255) NOT NULL,
    invoice_date DATE NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    description VARCHAR(255),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING'
);

-- Create line_items table
CREATE TABLE line_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    price DECIMAL(19, 2) NOT NULL,
    quantity INT DEFAULT 1,
    total_amount DECIMAL(19, 2) NOT NULL,
    invoice_id BIGINT NOT NULL,
    FOREIGN KEY (invoice_id) REFERENCES invoices(id)
);

-- Create payments table
CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    invoice_id BIGINT NOT NULL,
    payment_date DATE NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    payment_method VARCHAR(255) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (invoice_id) REFERENCES invoices(id)
);