#!/bin/bash
sudo apt update
sudo apt upgrade -y

echo "Starting install jdk"
sudo apt install openjdk-17-jdk -y
java -version
echo "JDK installed"

echo "Starting install mariaDB"
sudo apt install mariadb-server -y
echo "mariaDB installed"

echo "Strating mariaDB"
sudo systemctl start mariadb
sudo systemctl status mariadb

MYSQL_USER="root"
MYSQL_PASSWORD="root"

NEW_DB_NAME="webappdb"       
NEW_USER="csye6225"             
NEW_USER_PASSWORD="PASSword123!" 

sudo mysql -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" <<MYSQL_SCRIPT
CREATE DATABASE $NEW_DB_NAME;
MYSQL_SCRIPT

sudo mysql -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" <<MYSQL_SCRIPT
CREATE USER '$NEW_USER'@'localhost' IDENTIFIED BY '$NEW_USER_PASSWORD';
GRANT ALL PRIVILEGES ON $NEW_DB_NAME.* TO '$NEW_USER'@'localhost';
FLUSH PRIVILEGES;
MYSQL_SCRIPT

sudo mysql -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" -e "exit"

echo "Create database '$NEW_DB_NAME' and user '$NEW_USER'，and grant privileges。"

sudo cp ~/users.csv /opt/users.csv
echo "Copy file users.csv"

# run
# java -jar /root/webapp-0.0.1-SNAPSHOT.jar --spring.config.location=/root/application.properties