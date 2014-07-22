POIProxy
========

A proxy service to retrieve POIs (Points Of Interest) from several public services (Nominatim, Mapquest, Cloudmade, Geonames, Panoramio, Ovi, Flickr, Twitter, LastFM, Wikipedia, Youtube, Minube, Buzz, Foursquare, Gowalla, ...)

##How to build a development workspace of POIProxy (Work in progress)

***Pre-requisites***

* <a href="http://maven.apache.org/download.cgi" target="_blank">Maven</a>
* <a href="https://www.eclipse.org/downloads/" target="_blank">Eclipse IDE for Java EE developers</a>
* <a href="http://tomcat.apache.org/download-70.cgi" target="_blank">Apache Tomcat 7</a>
* <a href="http://git-scm.com/book/en/Getting-Started-Installing-Git" target="_blank">Git</a>

***Clone this repo***

`git clone https://github.com/Prodevelop/POIProxy.git`

Alternatively you can download the zipped version of the repo

***Import projects into Eclipse***

<img src="https://raw.githubusercontent.com/Prodevelop/POIProxy/master/doc/img/step01.png"/>

Make sure you click on "Search for nested projects" in the Import Projects dialog of Eclipse. Check every project except **es.alrocar.poiproxy.gae**

<img src="https://raw.githubusercontent.com/Prodevelop/POIProxy/master/doc/img/step02.png"/>

If necessary, right click on every project with a red marker and select Maven>Update Project...

<img src="https://raw.githubusercontent.com/Prodevelop/POIProxy/master/doc/img/step03.png"/>

***Add a new Server in Eclipse***

Select Tomcat v7.0 Server

<img src="https://raw.githubusercontent.com/Prodevelop/POIProxy/master/doc/img/step05.png"/>

Browse to your Tomcat installation directory

<img src="https://raw.githubusercontent.com/Prodevelop/POIProxy/master/doc/img/step06.png"/>

Add *es.alrocar.poiproxy.rest* to the configured resources

<img src="https://raw.githubusercontent.com/Prodevelop/POIProxy/master/doc/img/step07.png"/>

***Start your POIProxy instance***

<img src="https://raw.githubusercontent.com/Prodevelop/POIProxy/master/doc/img/step08.png"/>

***Check that everything works***

Open this URL in your preferred browser:

`http://localhost:8080/poiproxy/describeServices`





