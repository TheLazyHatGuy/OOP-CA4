#OOP CA4
[![CircleCI](https://circleci.com/bb/TheLazyHatGuy/oop-ca4.svg?style=svg&circle-token=1c06da666e5409f99be3d4a9cced46b8785e696c)](https://circleci.com/bb/TheLazyHatGuy/oop-ca4)

[Bitbucket](https://bitbucket.org/TheLazyHatGuy/oop-ca4/src/master/)

## Authors 
* Andrew Carolan
* Cameron Scholes

## Project Description
This *java* application is a client-server application which will grant users access to a database of movies that they can interact with.
Some of the functionality that the users can avail of is:
* Register to the server
    * The client will be given a persistent ID for them to use with the database.
    
* Add 
    * Allows clients to add new movies to the database.
    
* Remove
    * Allows clients to remove data from the database.
    
* Update 
    * Users can change certain info about the movies in the database.
    
* Watch
    * Users will be able to track what movies in the database they have watched.
    
* Recommend
    * Will recommend users movies that they should watch.
    
## Fixes we were given
* Aaron gave us the fix for the error `Connection failed The server time zone value 'GMT Summer Time' is unrecognized or represents more than one time zone. You must configure either the server or JDBC driver (via the serverTimezone configuration property) to use a more specifc time zone value if you want to utilize time zone support.`

```sql
SET @@global.time_zone = '+00:00';
SET @@session.time_zone = '+00:00';
```

* James gave me the solution for ensuring a successful database insert and update