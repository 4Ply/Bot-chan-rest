# Bot-chan-rest
This is the rest server used to handle requests from http://www.bot-chan.com


[![Twitter](https://img.shields.io/badge/twitter-@4Ply_ZA-blue.svg)](https://twitter.com/4Ply_ZA)
[![Build Status](https://travis-ci.org/4Ply/Bot-chan-rest.svg?branch=master)](https://travis-ci.org/4Ply/Bot-chan-rest)


### Definitions:
* Entity - A user or bot consuming the app that may have hard or soft links to other entities.
* Hard-link - A direct link between two entities, effectively allowing them to interact as one. All resources added to an entity are available to all of it's hard-linked entities, and vice-versa. 
* Soft-link - A one-sided link between two entities, used for one entity to subscribe to another entity's public data


### Current functionality
(All requests produce application/json)

| HTTP Method | URI path | Description |Parameters|
|-------------|----------|-------------|----------|
|POST         |/login     |Authenticate with the application to recieve a session token (which is valid for 5 minutes)| username, password
|POST         |/loginCheck|Check if a sessionKey is valid (and has not timed out), and refresh the token's validity timeout (if valid)| sessionKey
