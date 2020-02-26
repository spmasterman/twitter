# Reactive Streams with the Twitter API #


Three services
* tracked-hashtag - exposes HTTP interface to start tracking a subject/keyword. Persists this in Redis, and pushes it out on a rabbit queue
* tweet-gathering - subscribes to the queue above, and queries the Twitter API in a non blocking way when it gets a message. As tweets arrive they are piped into a second queue
* tweet-dispatching - subscribes to this second queue and pipes the tweets through to another non blocking endpoint

This was originally an example from a book, that didn't even remotely work, that I wrestled into submission. It's not super useful but 
was a learning experience for me.

There are still some issues 
- I don't think the Dispatcher is anything like an idiomatic Spring Reactive endpoint
- If you run all components in docker, using a docker network, the spring microservices dont attempt to connect to rabbit (and hence dont create the queues and exchanges). This despite the correct profile being selected, and a busybox container attached to the same network resolving them properly. I don't really have any ideas why - but it wasn't the point of this repo to debug docker-compose issues
- The whole thing is very contrived - its just really to show three services interacting in a reactive way, rather than anything that should be considered best practice.

To run - execute ./rabbit and ./redis in the platform folder to start those services in a local docker container. Then start each of the services from IntelliJ, making sure that appropriate credentials are supplied as environment variables for the tweet-gathering service. (You will need to create a developer account at twitter, generate an app and give it at least read permissions to get the keys)

`curl -H "Content-Type: application/json" -X POST -d '{"hashTag":"bitcoin","queue":"bitcoin"}' http://localhost:9090/api/tracked-hash-tag`

to kick things off, watch the logs of the dispatcher service and you will see tweets coming out the other end if you subscribe a consumer (like postman) to the endpoint

`localhost:9099/tweets?q=bitcoin`

I imagine this was part of a much more useful and larger project, that was hacked down for use in the book (Spring 5 End to End Programming) and suffered in purpose and quality during the process. I wouldn't recommend the book - its full of rudimentary errors (substituting a method invocation for a method reference for example i.e. `fun this() = that()` instead of `fun this() = ::that`) and doesn't do a competent job of explaining much at all. 

### There are no plans to update this repository further - it was a learning exercise ###