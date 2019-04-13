# OOP CA4

[![CircleCI](https://circleci.com/gh/TheLazyHatGuy/OOP-CA4.svg?style=svg&circle-token=c0211011755c3a5cb073b8f5ad4f2bdffebcdc10)](https://circleci.com/gh/TheLazyHatGuy/OOP-CA4)
[![Build Status](https://travis-ci.com/TheLazyHatGuy/OOP-CA4.svg?token=BQERceMxGCg14WSa1ns6&branch=master)](https://travis-ci.com/TheLazyHatGuy/OOP-CA4)

Old Repo - [Bitbucket](https://bitbucket.org/TheLazyHatGuy/oop-ca4/src/master/)

New Repo - [GitHub](https://github.com/TheLazyHatGuy/OOP-CA4/)

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
