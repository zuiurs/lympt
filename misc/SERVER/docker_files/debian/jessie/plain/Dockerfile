FROM            docker.io/debian:jessie
MAINTAINER      Urushida

# Update apt-get database
RUN     /usr/bin/apt-get update

# Generate autopassword.sh
RUN     /usr/bin/apt-get -y install expect
RUN     echo "#!/usr/bin/expect" >> /usr/bin/autopassword.sh && \
        echo "" >> /usr/bin/autopassword.sh && \
        echo "set tmp_password [lindex \$argv 0]" >> /usr/bin/autopassword.sh && \
        echo "" >> /usr/bin/autopassword.sh && \
        echo "set timeout 5" >> /usr/bin/autopassword.sh && \
        echo "spawn passwd" >> /usr/bin/autopassword.sh && \
        echo "expect \"Enter new UNIX password:\"" >> /usr/bin/autopassword.sh && \
        echo "send \${tmp_password}" >> /usr/bin/autopassword.sh && \
        echo "send \"\\n\"" >> /usr/bin/autopassword.sh && \
        echo "expect \"Retype new UNIX password:\"" >> /usr/bin/autopassword.sh && \
        echo "send \${tmp_password}" >> /usr/bin/autopassword.sh && \
        echo "send \"\\n\"" >> /usr/bin/autopassword.sh && \
        echo "interact" >> /usr/bin/autopassword.sh
RUN     /bin/chmod 500 /usr/bin/autopassword.sh

# for ssh-connection
RUN     /usr/bin/apt-get -y install ssh
RUN     /bin/sed -e 's/^\(PermitRootLogin \).*/\1yes/' /etc/ssh/sshd_config
RUN     /bin/sed -e 's/^\(UsePAM\).*/\1 no/' /etc/ssh/sshd_config

# My hobby
RUN     /usr/bin/apt-get -y install vim

# Port 22 forwarded to host
EXPOSE  22

# Initialize command
CMD     ["/sbin/init"]
