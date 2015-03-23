FROM jeanblanchard/busybox-tomcat

RUN curl -ksL https://github.com/Prodevelop/POIProxy/releases/download/v2.0-SNAPSHOT-23032015/poiproxy.war -o /opt/tomcat/webapps/poiproxy.war
