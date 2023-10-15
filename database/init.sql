-- Create a new database
CREATE DATABASE IF NOT EXISTS chat_app;

-- Create a user and grant privileges on the new database
CREATE USER 'chat_app_user'@'%' IDENTIFIED BY 'chat_app_password';
GRANT ALL PRIVILEGES ON chat_app.* TO 'chat_app_user'@'%';
