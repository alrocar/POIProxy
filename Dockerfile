FROM jeanblanchard/busybox-tomcat

RUN curl -ksL http://github.com/tai-lab/POIProxy/releases/download/20150226/poiproxy.war -o /opt/tomcat/webapps/poiproxy.war
