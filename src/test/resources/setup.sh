#!/bin/bash

wget https://dlcdn.apache.org/maven/maven-3/3.8.5/binaries/apache-maven-3.8.5-bin.tar.gz -P /tmp
sudo tar xf /tmp/apache-maven-3.8.5-bin.tar.gz -C /opt
sudo ln -s /opt/apache-maven-3.8.5 /opt/maven
echo 'export PATH=$PATH:/opt/maven/bin' | sudo tee -a /etc/profile