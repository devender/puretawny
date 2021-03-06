## Requirements

1. Create a DB containing 10,000 random entries for valid latitude, longitude coordinates. 
1. getAllDataSets() - GET method, returns all data in the DB.
1. getData(latitude, longitude) - GET method, returns if the coordinates exist in the DB or not
1. addData(latitude, longitude) - POST method, adds the coordinate to the DB if it doesn't exist
1. Given the entry's coordinates, determine if those coordinates are within the United States. (__*Need to use Country Polygons*__).
1. If they're not within the United States, determine if the coordinates are within 500 miles of the following cities:
	* Tokyo, Japan
	* Sydney, Australia
	* Riyadh, Saudi Arabia
	* Zurich, Switzerland
	* Reykjavik, Iceland
	* Mexico City, Mexico
	* Lima, Peru
1. For each of the above, tell us how far away the entry's coordinates are from each city.

## Analysis

#### Database
* It is evident from the requirements that this project will be doing a lot of GeoSpatial look-ups such as:
	* Given a point find out if it exists.
	* Distances from one point to another.
	* Given a point determine if those co-ordinates are within a certain country.
* With the above requirement in mind it is best to pick an underlying database that comes with builtin GeoSpatail features.
	* There are many databases that support GeoSpatial, a complete list can be seen [here](https://en.wikipedia.org/wiki/Spatial_database).
	* For this project I decided to use [RethinkDB](http://rethinkdb.com/).
		
#### Data

* Next challenge is to find geo spatial data that contains atleast 10,000 valid latitudes and longitudes.
* MaxMind provides a good solution they have a free [download](https://www.maxmind.com/en/free-world-cities-database) containing all cities in the world with their latitude and longitude along with country information.
* After downloading the data I limited it to 10,000.
* This seed data is available [here](https://github.com/devender/puretawny/blob/master/code/dataLoader/src/main/resources/cities.txt).
* This will be bundeled with the war so no need to download this seperately.

##### Country Polygons
* Given a point we need to determine if it is within US or not.
* In order to do this we need to be able to define a polygon whose vertices connect all the boder points of a country.
* Then we can check if the give point is contained with in the polygon.
* Country Polygons datasets are available in many places, I decided to use [this](https://github.com/datasets/geo-countries).
* I extracted polygons just of the US and it is available [here](https://github.com/devender/puretawny/blob/master/data/us.geojson).
* The above file defines multiple polygons for "Minor Outlying Islands","United States of America","Virgin Islands"
* This file is bundeled with the war so no need to download this seperately.

## Implementation

* The only requirement given was to use Java, apart from that the following are some of libraries used: 
	* Maven.
	* RethinkDB.
	* SpringBoot.
* Please see main [pom](https://github.com/devender/puretawny/blob/master/code/pom.xml) file for a complete list of dependencies.


## Data Loading

* All the code for bootstraping the database is available in the submodule [code/dataloader](https://github.com/devender/puretawny/tree/master/code/dataLoader).
* The main class of interest is [```DbBootStrapImpl.java```](https://github.com/devender/puretawny/blob/master/code/dataLoader/src/main/java/com/gdr/puretawny/dataLoader/impl/DbBootStrapImpl.java), which contains code to read and populate the database.
* This file not only loads the 10,000 set of points but also creates polygons for the US so that later on we can test if a point is contained with in them.


## Check if a point is within US

* I already uploaded the entire set of polygons that define the US.(Described above.)
* Now to check if a particular point is contained within one of the above polygons I use the following code.

```JAVA
@Override
    public boolean isPointInUs(final Point point) {
        boolean intersects = false;
        try (Connection connection = r.connection().hostname(host).port(port).connect()) {
            Cursor<Map> o = r.db(DB_NAME).table(US_POLYGONS_TABLE_NAME).
                    g("poly"). //for each of the polygon check if the give point intersects
                    intersects(r.point(point.getLongitude(), point.getLatitude())).run(connection);
            intersects = o.hasNext();
        }
        return intersects;
    }
```

The complete source code of this class is available [here](https://github.com/devender/puretawny/blob/master/code/db/src/main/java/com/gdr/puretawny/db/impl/DbServiceImpl.java).

## Results


*  **getAllDataSets() - GET method, returns all data in the DB**

```
curl http://localhost:8080/point/all	
or
curl http://52.90.172.60:8080/point/all
```

Sample output
```JSON
[{
	"country": "us",
	"city": "bellamy",
	"latitude": 32.4488889,
	"longitude": -88.1336111,
	"id": 0
}, {
	"country": "us",
	"city": "gepp",
	"latitude": 36.3877778,
	"longitude": -92.1041667,
	"id": 0
}
...
...]
```

*  **getData(latitude, longitude) - GET method, returns if the coordinates exist in the DB or not**

###### When point exists
```
curl http://localhost:8080/point/longitude/-88.1336111/latitude/32.4488889
or
curl http://52.90.172.60:8080/point/longitude/-88.1336111/latitude/32.4488889
```
Response
```JSON
{"country":"us","city":"bellamy","latitude":32.4488889,"longitude":-88.1336111,"id":0}
```
###### When point does not exists
```
curl -v http://localhost:8080/point/longitude/-88.1336111/latitude/22.2
or
curl -v http://52.90.172.60:8080/point/longitude/-88.1336111/latitude/22.2

< HTTP/1.1 404 Not Found
< Server: Apache-Coyote/1.1
< Content-Length: 0
```

*  **addData(latitude, longitude) - POST method, adds the coordinate to the DB if it doesn't exist**

###### When point does not exists
```
curl -v -X POST  http://localhost:8080/point/longitude/107.237894/latitude/34.109327
or
curl -v -X POST  http://52.90.172.60:8080/point/longitude/107.237894/latitude/34.109327
*   Trying ::1...
* Connected to localhost (::1) port 8080 (#0)
> POST /point/longitude/107.237894/latitude/34.109327 HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.43.0
> Accept: */*
> 
< HTTP/1.1 201 Created
< Server: Apache-Coyote/1.1
< Content-Length: 0
< Date: Mon, 11 Apr 2016 01:23:35 GMT
```
###### When point already exists
```
curl -v -X POST  http://localhost:8080/point/longitude/107.237894/latitude/34.109327
or
curl -v -X POST  http://52.90.172.60:8080/point/longitude/107.237894/latitude/34.109327
*   Trying ::1...
* Connected to localhost (::1) port 8080 (#0)
> POST /point/longitude/107.237894/latitude/34.109327 HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.43.0
> Accept: */*
> 
< HTTP/1.1 412 Precondition Failed
< Server: Apache-Coyote/1.1
< Content-Length: 0
< Date: Mon, 11 Apr 2016 01:23:37 GMT
< 
```
*  **Given the entry's coordinates, determine if those coordinates are within the United States.**

###### Check if Moscow is inside US (Moscow lat 55.752222, long 37.615556)
```
curl http://localhost:8080/point/longitude/37.615556/latitude/55.752222/insideUS
or
curl http://52.90.172.60:8080/point/longitude/37.615556/latitude/55.752222/insideUS
false
```

###### Check if Los Angeles is inside US (Moscow lat 34.0522222, long -118.2427778)
```
curl http://localhost:8080/point/longitude/-118.2427778/latitude/34.0522222/insideUS
or
curl http://52.90.172.60:8080/point/longitude/-118.2427778/latitude/34.0522222/insideUS
true
```

*  **If they're not within the United States, determine if the coordinates are within 500 miles of the following cities:**
*  **For each of the above, tell us how far away the entry's coordinates are from each city.**

For the above 2 we can use the info end point, for any give point it will return.
	* If it is inside US.
	* For each of the 7 points of interest 
		* How far it is from the point of interest
		* Is it within 500 miles of the point of interest.
```
curl http://localhost:8080/point/longitude/37.615556/latitude/55.752222/info
or
curl http://localhost:52.90.172.60:8080/point/longitude/37.615556/latitude/55.752222/info
```		

```JSON
[{
	"distanceInMiles": 2062.722566480321,
	"city": {
		"country": "is",
		"city": "reykjavik",
		"latitude": 64.15,
		"longitude": -21.95,
		"id": 0
	},
	"fromPoint": {
		"country": null,
		"city": null,
		"latitude": 55.752222,
		"longitude": 37.615556,
		"id": 0
	},
	"inUS": false,
	"within500MilesOfCity": false
}, {
	"distanceInMiles": 8999.550337831148,
	"city": {
		"country": "au",
		"city": "sydney",
		"latitude": -33.861481,
		"longitude": 151.205475,
		"id": 0
	},
	"fromPoint": {
		"country": null,
		"city": null,
		"latitude": 55.752222,
		"longitude": 37.615556,
		"id": 0
	},
	"inUS": false,
	"within500MilesOfCity": false
}, {
	"distanceInMiles": 7855.166040433584,
	"city": {
		"country": "pe",
		"city": "lima",
		"latitude": -12.046374,
		"longitude": -77.042793,
		"id": 0
	},
	"fromPoint": {
		"country": null,
		"city": null,
		"latitude": 55.752222,
		"longitude": 37.615556,
		"id": 0
	},
	"inUS": false,
	"within500MilesOfCity": false
}, {
	"distanceInMiles": 1366.7494919536348,
	"city": {
		"country": "ch",
		"city": "zurich",
		"latitude": 47.366667,
		"longitude": 8.55,
		"id": 0
	},
	"fromPoint": {
		"country": null,
		"city": null,
		"latitude": 55.752222,
		"longitude": 37.615556,
		"id": 0
	},
	"inUS": false,
	"within500MilesOfCity": false
}, {
	"distanceInMiles": 2194.9134760126453,
	"city": {
		"country": "sa",
		"city": "riyadh",
		"latitude": 24.653664,
		"longitude": 46.71522,
		"id": 0
	},
	"fromPoint": {
		"country": null,
		"city": null,
		"latitude": 55.752222,
		"longitude": 37.615556,
		"id": 0
	},
	"inUS": false,
	"within500MilesOfCity": false
}, {
	"distanceInMiles": 6671.45687521232,
	"city": {
		"country": "mx",
		"city": "mexico city",
		"latitude": 19.432608,
		"longitude": -99.133209,
		"id": 0
	},
	"fromPoint": {
		"country": null,
		"city": null,
		"latitude": 55.752222,
		"longitude": 37.615556,
		"id": 0
	},
	"inUS": false,
	"within500MilesOfCity": false
}, {
	"distanceInMiles": 4660.735348176216,
	"city": {
		"country": "jp",
		"city": "tokyo",
		"latitude": 35.685,
		"longitude": 139.751389,
		"id": 0
	},
	"fromPoint": {
		"country": null,
		"city": null,
		"latitude": 55.752222,
		"longitude": 37.615556,
		"id": 0
	},
	"inUS": false,
	"within500MilesOfCity": false
}]
```		
*  **A spreadsheet with the above answers for the original 10,000 entries.**

	* The CSV file has the following format:
		* Latitude, Longitude, Within US, POI City, Distance From POI City, Is Within 500 Miles of POI City.
	* File is available [here](https://github.com/devender/puretawny/blob/master/export/output.csv.gz).
	* Source code to produce the file is [here](https://github.com/devender/puretawny/blob/master/code/dataLoader/src/main/java/com/gdr/puretawny/dataexport/Export.java).

*  **Source code for the solution.**

	* Entire source code is [here](https://github.com/devender/puretawny/tree/master/code)

*  **The mechanism by which you populated the 10,000 entries.**

This has already been described.


*  **WAR file that we can deploy to Tomcat to test functionality.**
	* Clone this repo.
	* cd into the code folder and run ```mvn clean install```
	* cd into the rest-service folder and run ```java -jar target/rest-service-1.0-SNAPSHOT.war```
	* This is an executable war file so you need not deploy it into a container.

*NOTE: You need to have rethinkDB running locally only if you plan to run the war locally.*
* To install RethinkDB follow these [instructions](http://rethinkdb.com/docs/install/).
* You will also need to run the DBBootStrapIntegration.java one time to load the data.
* ```mvn -Dtest=DBBootStrapIntegration test```

*  **DB schema.**

RethinkDB is a document database, for this project I created 1 database with 3 tables
```Java
    @Override
    public void bootStrapDb() {
        try {
            // drops database if it exists and recreates it.
            dbService.initDB();

            // load 10,000 points
            Path path = Paths.get(ClassLoader.getSystemResource(sampleCitiesFile).toURI());
            LOGGER.info("Reading data from file {}", path.toString());
            List<Point> points = fileLoader.loadFromFile(path);
            LOGGER.info("Read {} points ", points.size());

            dbService.insertPoints(points);

            LOGGER.info("Reading US polygon data");
            Path usPolygonsPath = Paths.get(ClassLoader.getSystemResource(usPolygonsFile).toURI());
            List<Polygon> usPolygons = fileLoader.loadPolygons(usPolygonsPath);
            LOGGER.info("Read {} polygons for US...inserting", usPolygons.size());
            dbService.insertPolygonsForUs(usPolygons);

            // finally create a seperate table for the 7 points of interest
            Path path2 = Paths.get(ClassLoader.getSystemResource(pointsOfInterestFile).toURI());
            LOGGER.info("Reading data from file {}", path.toString());
            List<Point> points2 = fileLoader.loadFromFile(path2);
            LOGGER.info("Read {} points ", points.size());
            dbService.insertPointsOfInterest(points2);

        } catch (IOException | ParseException | URISyntaxException e) {
            LOGGER.error("Unable to read file", e);
        }
    }

```

*  **Seed data for the DB.**

	* [Seed Data](https://github.com/devender/puretawny/blob/master/code/dataLoader/src/main/resources/cities.txt)
	* [US Polygons](https://github.com/devender/puretawny/blob/master/code/dataLoader/src/main/resources/us.geojson)
	* [Preferred points of interest](https://github.com/devender/puretawny/blob/master/code/dataLoader/src/main/resources/pointsOfInterest.txt)



