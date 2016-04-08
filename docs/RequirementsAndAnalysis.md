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
* It is evident from the requirements that this project will be doing a lot of GeoSpatial look ups such as:
	* Given a point find out if it exists.
	* Distances from one point to another.
	* Given a point determine if those co-ordinates are within a certain country.
* Knowing this let us pick an underlying database that comes with builtin GeoSpatail features.
	* There are many databases that support GeoSpatial look ups, a complete list can be seen [here](https://en.wikipedia.org/wiki/Spatial_database).
	* For this project we decided to use [RethinkDB](http://rethinkdb.com/).
		
#### Data
* Our next challenge is to find geo spatial data, our needs our:
	* It should contain latitudes and longitudes for all cities/villages/towns...etc and preferably which country the point belongs too.
	* Be able to easily download the data so that we can import it.
* MaxMind provides a great solution they have a free [download](https://www.maxmind.com/en/free-world-cities-database) containing all cities in the world with their latitude and longitude along with country information.
	* We decided to boot stap our database with the free download from MaxMind and we can later on agument this with other data sources as needed.


