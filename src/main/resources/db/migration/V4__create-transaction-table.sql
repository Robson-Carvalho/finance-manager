CREATE TABLE transaction (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id UUID NOT NULL,
    account_id UUID NOT NULL,
    category_id UUID NOT NULL,
    description VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL, 
    date TIMESTAMP NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES "user" ON DELETE CASCADE,
    FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE
);
