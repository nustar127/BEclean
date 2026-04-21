ALTER TABLE orders DROP CONSTRAINT IF EXISTS orders_status_check;
ALTER TABLE orders
ADD CONSTRAINT orders_status_check
CHECK (status IN (0, 1, 2, 3, 4, 5));
