FROM jeanblanchard/busybox-tomcat

COPY dist/poiproxy.war .
RUN cp ./poiproxy.war /opt/tomcat/webapps/poiproxy.war

EXPOSE 8080
