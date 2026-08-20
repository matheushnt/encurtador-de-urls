ALTER TABLE short_links
ADD COLUMN user_id UUID;

ALTER TABLE short_links ADD CONSTRAINT fk_short_links_user
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;