sudo systemctl stop elasticsearch
confluent stop
confluent start
./home/sysadmin/Downloads/kibana-5.6.8-linux-x86_64/bin/kibana &
connect-standalone /etc/kafka/connect-source-twitter.properties /etc/kafka/twitter-source.properties &
sudo systemctl start elasticsearch

