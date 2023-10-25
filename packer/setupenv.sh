#!/bin/bash
sudo apt update
sudo apt upgrade -y

echo "Starting install jdk"
sudo apt install openjdk-17-jdk -y
java -version
echo "JDK installed"

echo "Starting install sed"
sudo apt install sed -y
java -version
echo "sed installed"

#echo "Starting install mariaDB"
#sudo apt install mariadb-server -y
#echo "mariaDB installed"
#
#echo "Strating mariaDB"
#sudo systemctl start mariadb
#sudo systemctl status mariadb
#
#MYSQL_USER="root"
#MYSQL_PASSWORD="root"
#
#NEW_DB_NAME="webappdb"
#NEW_USER="csye6225"
#NEW_USER_PASSWORD="PASSword123!"
#
#sudo mysql -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" <<MYSQL_SCRIPT
#CREATE DATABASE $NEW_DB_NAME;
#MYSQL_SCRIPT
#
#sudo mysql -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" <<MYSQL_SCRIPT
#CREATE USER '$NEW_USER'@'localhost' IDENTIFIED BY '$NEW_USER_PASSWORD';
#GRANT ALL PRIVILEGES ON $NEW_DB_NAME.* TO '$NEW_USER'@'localhost';
#FLUSH PRIVILEGES;
#MYSQL_SCRIPT
#
#sudo mysql -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" -e "exit"
#
#echo "Create database '$NEW_DB_NAME' and user '$NEW_USER'，and grant privileges。"

echo "Create user group and user"
sudo groupadd csye6225
sudo useradd -s /bin/false -g csye6225 -d /opt/csye6225 -m csye6225
echo "Create user group and user done"

echo "Copy files and grant privileges"
sudo cp ~/users.csv /opt/users.csv
sudo chown csye6225:csye6225 /opt/users.csv
sudo chmod +r /opt/users.csv
sudo cp ~/application.properties /opt/csye6225/application.properties
sudo cp ~/webapp-0.0.1-SNAPSHOT.jar /opt/csye6225/webapp-0.0.1-SNAPSHOT.jar
sudo chown csye6225:csye6225 /opt/csye6225/application.properties
sudo chown csye6225:csye6225 /opt/csye6225/webapp-0.0.1-SNAPSHOT.jar
sudo chmod +x /opt/csye6225/webapp-0.0.1-SNAPSHOT.jar
sudo chmod +r /opt/csye6225/application.properties
sudo cp ~/webapp.service /etc/systemd/system/webapp.service
sudo ls /opt
sudo ls /etc/systemd/system
echo "Copy files and grant privileges done"

echo "Systemd setup"
sudo systemctl daemon-reload
sudo systemctl enable webapp.service
sudo systemctl start webapp.service
echo "Systemd setup done"
