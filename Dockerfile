FROM	centos

ENV	UPDATE_VERSION=8u212
ENV	JAVA_VERSION=1.8.0_212
ENV	BUILD=b04

ENV     JAVA_HOME=/root/jdk${UPDATE_VERSION}-${BUILD}/

RUN	yum -y update && \
        yum -y install wget && \
        cd /root && \
        wget https://github.com/AdoptOpenJDK/openjdk8-binaries/releases/download/jdk${UPDATE_VERSION}-${BUILD}/OpenJDK8U-jdk_x64_linux_hotspot_${UPDATE_VERSION}${BUILD}.tar.gz && \
        gunzip OpenJDK8U-jdk_x64_linux_hotspot_${UPDATE_VERSION}${BUILD}.tar.gz && \
        tar -xvpf OpenJDK8U-jdk_x64_linux_hotspot_${UPDATE_VERSION}${BUILD}.tar && \
	alternatives --install /usr/bin/java java /root/jdk${UPDATE_VERSION}-${BUILD}/bin/java 1 && \
	alternatives --set java /root/jdk${UPDATE_VERSION}-${BUILD}/bin/java && \
	export JAVA_HOME=/root/jdk${UPDATE_VERSION}-${BUILD}/ && \
	echo "export JAVA_HOME=/root/jdk${UPDATE_VERSION}-${BUILD}/" | tee /etc/environment && \
	source /etc/environment

ADD ./target/regdbamqpservice-0.0.1-SNAPSHOT.jar /opt

CMD ["java","-jar","/opt/regdbamqpservice-0.0.1-SNAPSHOT.jar"]





	

