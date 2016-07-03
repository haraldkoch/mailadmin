FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/mailadmin.jar /mailadmin/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/mailadmin/app.jar"]
