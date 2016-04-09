## Requirements

#### Database
1. Create a DB (whatever flavor) containing 10,000 random entries for valid latitude, longitude coordinates. 

#### Web Services

1. getAllDataSets() - GET method, returns all data in the DB.
1. getData(latitude, longitude) - GET method, returns if the coordinates exist in the DB or not
1. addData(latitude, longitude) - POST method, adds the coordinate to the DB if it doesn't exist
1. Given the entry's coordinates, determine if those coordinates are within the United States.
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
* Next challenge is to find geo spatial data that matchs the needs of this project:
	* It should contain latitudes and longitudes for all cities/villages/towns...etc and preferably which country the point belongs too.
	* Be able to easily download the data so that we can import it.
* MaxMind provides a great solution they have a free [download](https://www.maxmind.com/en/free-world-cities-database) containing all cities in the world with their latitude and longitude along with country information.
	* Decided to boot stap the database with the free download from MaxMind and we can later on agument this with other data sources as needed.
	* This is the download (link)[http://download.maxmind.com/download/worldcities/worldcitiespop.txt.gz] it is 33 MB compressed.
	* Doing a wc -l on the file tells us that there are *3173959 -1 (for header) records* in the file, this exceeds the requirements of having 10,000 entries.

Sample from the downloaded file :
```
➜  Downloads head worldcitiespop.txt 
Country,City,AccentCity,Region,Population,Latitude,Longitude
ad,aixas,Aix?s,06,,42.4833333,1.4666667
ad,aixirivali,Aixirivali,06,,42.4666667,1.5
ad,aixirivall,Aixirivall,06,,42.4666667,1.5
ad,aixirvall,Aixirvall,06,,42.4666667,1.5
ad,aixovall,Aixovall,06,,42.4666667,1.4833333
ad,andorra,Andorra,07,,42.5,1.5166667
ad,andorra la vella,Andorra la Vella,07,20430,42.5,1.5166667
ad,andorra-vieille,Andorra-Vieille,07,,42.5,1.5166667
ad,andorre,Andorre,07,,42.5,1.5166667
```

## Implementation

* The only requirement given was to use Java, apart from that the following are some of libraries used: 
	* Maven.
	* RethinkDB.
	* Spring.
* Please see main [pom](https://github.com/devender/puretawny/blob/master/code/pom.xml) file for a complete list of dependencies.


## Data Loading

* All the code for bootstraping the database is available in the submodule [code/dataloader](https://github.com/devender/puretawny/tree/master/code/dataLoader).
* The main class of interest is [```DbBootStrapImpl.java```](https://github.com/devender/puretawny/blob/master/code/dataLoader/src/main/java/com/gdr/puretawny/dataLoader/impl/DbBootStrapImpl.java)
* 


