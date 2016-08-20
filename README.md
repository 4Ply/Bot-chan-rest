# Bot-chan-rest
[![Build Status](https://travis-ci.org/4Ply/Bot-chan-rest.svg?branch=master)](https://travis-ci.org/4Ply/Bot-chan-rest)


This is the rest server used to handle requests from http://www.bot-chan.com


### Definitions:
* Entity - A user or bot consuming the app that may have hard or soft links to other entities.
* Hard-link - A direct link between two entities, effectively allowing them to interact as one. All resources added to an entity are available to all of it's hard-linked entities, and vice-versa. 
* Soft-link - A one-sided link between two entities, used for one entity to subscribe to another entity's public data
