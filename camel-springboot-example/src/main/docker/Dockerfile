FROM cpandey/minimal-java-8
VOLUME /tmp
ADD camel-springboot-1.0.jar csb.jar
RUN sh -c 'touch /csb.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /csb.jar" ]

