
[![Build Status](https://travis-ci.org/Prodevelop/POIProxy.svg?branch=master)](https://travis-ci.org/Prodevelop/POIProxy)

POIProxy
========

A proxy service to retrieve POIs (Points Of Interest) from several public services (Nominatim, Mapquest, Cloudmade, Geonames, Panoramio, Ovi, Flickr, Twitter, LastFM, Wikipedia, Youtube, Minube, Buzz, Foursquare, Gowalla, ...)

##What is POIProxy?

The main purpose is to have a single service that handles requests to any public POI service providing a well defined REST API. POIProxy is able to parse JSON and XML responses and serve standard GeoJSON format. Finally it has the capability to add new services by providing a configuration file.

<img src="https://raw.githubusercontent.com/Prodevelop/POIProxy/dd43d29e063528ec7fd1aefe5126c309b784eb49/doc/img/poiproxy_diagram.png"/>

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


##API

* ***Get available services***

     Returns the available services registered into POIProxy and their description

     `http://localhost:8080/poiproxy/describeServices`

* ***Browse by tile***

     Returns the points inside the given tile (Z/X/Y). The tile notation is the same as [Google Maps uses][tiles_google]. 
     `http://localhost:8080/poiproxy/browse?service=panoramio&z=0&x=0&y=0&callback=whatever`

* ***Browse by extent***

     Returns the points inside the given bounding box. The coordinate reference system is EPSG:4326

     `http://localhost:8080/poiproxy/browseByExtent?service=panoramio&minX=-0.376&minY=39.47&maxX=-0.37&maxY=39.48&callback=whatever`

* ***Browse by lon,lat and distance***

     Returns the points inside a given radius distance of a point. The coordinate reference system is EPSG:4326

     `http://localhost:8080/poiproxy/browseByLonLat?service=panoramio&lon=-0.38&lat=39.46&dist=500&callback=whatever`
     
* ***Search by tile***

     Returns the points inside the given tile (Z/X/Y) for the given query. The tile notation is the same as [Google Maps uses][tiles_google].

     `http://localhost:8080/poiproxy/browse?service=flickr&z=0&x=0&y=0&query=search_term&callback=whatever`

* ***Search by extent***

     Returns the points inside the given bounding box for the given query. The coordinate reference system is EPSG:4326

     `http://localhost:8080/poiproxy/browseByExtent?service=flickr&minX=-0.376&minY=39.47&maxX=-0.37&maxY=39.48&query=search_term&callback=whatever`

* ***Search by lon,lat and distance***

     Returns the points inside a given radius distance of a point for the given query. The coordinate reference system is EPSG:4326

     `http://localhost:8080/poiproxy/browseByLonLat?service=flickr&lon=-0.38&lat=39.46&dist=500&query=search_term&callback=whatever`

POIProxy supports JSONP by adding the parameter `callback`to any request. If not provided then a JSON response is returned.

***IMPORTANT***: Not all services registered in POIProxy support all the API requests. Please refer to the describeServices operation to see the list of available operations per registered service.


