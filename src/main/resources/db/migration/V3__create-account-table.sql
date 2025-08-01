CREATE TABLE account (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    balance DECIMAL(15, 2),  -- Usando DECIMAL para representar BigDecimal no banco de dados
    user_id UUID,
    FOREIGN KEY (user_id) REFERENCES "user" ON DELETE CASCADE
);