FROM tomcat

ADD es.alrocar.poiproxy.rest/target/poiproxy.war /usr/local/tomcat/webapps/

CMD ["catalina.sh", "run"]
