# Kafka, Pub-Subs and DatSources


There are three "components" in the system that all need ot work together:

- Kafka
- the Pub-Sub component (which uses the Websocket Component)
- DataSource

Rocky-Road works in a related way, so let's start there.

# How Rocky-Road Does It

In the Rocky-Road implementation of Vanilla, the Websocket uses [core.async](https://github.com/clojure/core.async) to 
listen to a collection of channels as an input. Separately, each DataSource create a single
channel ot use as output.

By default, the DataSource only publishes data via the Scheduler (i.e., _not_ triggered by
some asynchronous event such as a Kafka message). It does so by writing the message for 
publication onto that channel. This causes the "take" (`<!`) inside the `go-loop` in Websocket
to unblock (it waits for a message on any one of the channels) and publish the message to 
subscribed clients using the Websocket's `send-fn`.

That's actually a pretty slick mechanism, but it has a few drawbacks

1. Websocket depends on all the DataSources.
   1. This means that all DataSources have to be started at server initialization, regardless of
   whether _any_ of them even see a subscription.
2. Websocket also has to map over all the DataSources, so it can merge together all the channels.
3. Websocket complects managing the subscriptions with managing the socket with publishing messages. This
is less flexible that separating these concerns.

## RCCST Approach

It might be better to flip the DAG and have DataSources depend on Pub-Sub, which in turn depends on Websocket. 
In this way, Pub-Sub can create just one channel, and any DataSources can grab a reference to it. Then, when there are
updates (especially asynchronous updates, say from Kafka), the DataSource can still just put the
update message onto the channel (`>!`). Pub-Sub will be blocking on `>!` from the channel, so it 
will unblock when the message(s) arrives, and it can publish using its dependency on the Websocket.

This _should_ make it possible to create individual DataSources only when some user first 
subscribes to each^(1)^.



> ^(1)^ Currently, we don't track how many times any given client subscribes to the same source, just 
> subscriptions across all clients.




