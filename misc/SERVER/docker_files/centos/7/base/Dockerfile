FROM            centos:7
MAINTAINER      Urushida

# Official container have no "passwd" command
RUN     /usr/bin/yum -y install passwd

# yum groupinstall
RUN     /usr/bin/yum -y groupinstall base

# Generate autopassword.sh
RUN     /usr/bin/yum -y install expect
RUN     echo "#!/usr/bin/expect" >> /usr/bin/autopassword.sh && \
        echo "" >> /usr/bin/autopassword.sh && \
        echo "set tmp_password [lindex \$argv 0]" >> /usr/bin/autopassword.sh && \
        echo "" >> /usr/bin/autopassword.sh && \
        echo "set timeout 5" >> /usr/bin/autopassword.sh && \
        echo "spawn passwd" >> /usr/bin/autopassword.sh && \
        echo "expect \"New password:\"" >> /usr/bin/autopassword.sh && \
        echo "send \${tmp_password}" >> /usr/bin/autopassword.sh && \
        echo "send \"\\n\"" >> /usr/bin/autopassword.sh && \
        echo "expect \"Retype new password:\"" >> /usr/bin/autopassword.sh && \
        echo "send \${tmp_password}" >> /usr/bin/autopassword.sh && \
        echo "send \"\\n\"" >> /usr/bin/autopassword.sh && \
        echo "interact" >> /usr/bin/autopassword.sh
RUN     chmod 500 /usr/bin/autopassword.sh

# for ssh-connection
RUN     /usr/bin/yum -y install openssh-server
RUN     /usr/bin/systemctl enable sshd.service

# My hobby
RUN     /usr/bin/yum -y install vim

# Port 22 forwarded to host
EXPOSE  22

# Initialize command (systemd requiring)
CMD     ["/sbin/init"]
