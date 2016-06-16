FROM            docker.io/centos:6
MAINTAINER      Urushida

RUN     /usr/bin/yum -y install passwd
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

RUN     /usr/bin/yum -y install openssh-server
RUN     /sbin/chkconfig sshd on

RUN     /usr/bin/yum -y install vim

EXPOSE  22

CMD     ["/sbin/init"]
