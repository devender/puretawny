## Requirements

#### Database
1. Create a DB containing 10,000 random entries for valid latitude, longitude coordinates. 

#### Web Services

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
	* There are many databases that support GeoSpatial look ups, a complete list can be seen [here](https://en.wikipedia.org/wiki/Spatial_database).
	* For this project I decided to use [RethinkDB](http://rethinkdb.com/).
		* Reasons : Apart from supporting Gis queries, rethink is fast, has a native java driver, and its most important feature is callbacks. 	
		
#### Data

##### All Cities
* Next challenge is to find geo spatial data that:
	* Contains atleast 10,000 valid latitudes and longitudes.
	* Be able to easily download the data so that we can import it.
* MaxMind provides a great solution they have a free [download](https://www.maxmind.com/en/free-world-cities-database) containing all cities in the world with their latitude and longitude along with country information.
	* Decided to boot stap the database with the free download from MaxMind and we can later on agument this with other data sources as needed.
	* This is the download (link)[http://download.maxmind.com/download/worldcities/worldcitiespop.txt.gz] it is 33 MB compressed.
	* Doing a wc -l on the file tells us that there are *3173959 -1 (for header) records* in the file, this exceeds the requirements of having 10,000 entries.

##### Country Polygons
* Given a point we need to determine if it is within US or not.
* In order to do this we need to be able to define a polygon whose vertices connect all the boder points of a country.
* Then we can check if the give point is contained with in the polygon.
* County Polygons datasets are available in many places, I decided to use [this](https://github.com/datasets/geo-countries).

## Implementation

* The only requirement given was to use Java, apart from that the following are some of libraries used: 
	* Maven.
	* RethinkDB.
	* Spring.
* Please see main [pom](https://github.com/devender/puretawny/blob/master/code/pom.xml) file for a complete list of dependencies.


## Data Loading

* All the code for bootstraping the database is available in the submodule [code/dataloader](https://github.com/devender/puretawny/tree/master/code/dataLoader).
* The main class of interest is [```DbBootStrapImpl.java```](https://github.com/devender/puretawny/blob/master/code/dataLoader/src/main/java/com/gdr/puretawny/dataLoader/impl/DbBootStrapImpl.java), which contains code to read and populate the database.
* 


