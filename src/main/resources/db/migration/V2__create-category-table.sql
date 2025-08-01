CREATE TABLE category (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    user_id UUID,
    FOREIGN KEY (user_id) REFERENCES "user" ON DELETE CASCADE
);
