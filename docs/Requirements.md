## Requirements

#### Database
1. Create a DB (whatever flavor) containing 10,000 random entries for valid latitude, longitude coordinates. 

#### Web Services

1. getAllDataSets() - GET method, returns all data in the DB.
1. getData(latitude, longitude) - GET method, returns if the coordinates exist in the DB or not
1. addData(latitude, longitude) - POST method, adds the coordinate to the DB if it doesn't exist
1. Given the entry's coordinates, determine if those coordinates are within the United States.
1. If they're not within the United States, determine if the coordinates are within 500 miles of the following cities:
	*. Tokyo, Japan
	*. Sydney, Australia
	*. Riyadh, Saudi Arabia
	*. Zurich, Switzerland
	*. Reykjavik, Iceland
	*. Mexico City, Mexico
	*. Lima, Peru
1. For each of the above, tell us how far away the entry's coordinates are from each city.


## Analysis

*. It is evident from the requirements that this project will be doing a lot of GeoSpatial look ups such as:
	*. Given a point find out if it exists.
	*. Distances from one point to another.
	*. Given a point determine if those co-ordinates are within a certain country.
*. Knowing this let us pick an underlying database that comes up GeoSpatail features built in.
	*. There are many databases that support GeoSpatial look ups, a complete list can be seen (here)[https://en.wikipedia.org/wiki/Spatial_database].
	*. For this project we decided to use (RethinkDB)[http://rethinkdb.com/].
		*. 	


