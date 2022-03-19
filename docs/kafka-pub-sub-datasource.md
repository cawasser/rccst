<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [Kafka, Pub-Subs and DatSources](#kafka-pub-subs-and-datsources)
- [How Rocky-Road Does It](#how-rocky-road-does-it)
  - [RCCST Approach](#rccst-approach)
    - [Topologies](#topologies)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

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
subscribes to each^1.



> ^1 Currently, we don't track how many times any given client subscribes to the same source, just 
> subscriptions across all clients.


### Topologies

Have the topologies depend on the :pub-sub component, and have it expose `start!` and `stop!` functions
to start and stop the individual topologies we might want to have.

> like listening to multiple topics to provide data to connected clients.



## What about data sources that have too much data for the client to process all at once?

What about sources that just have too much data, say 10,000 entities, or 100,000 measurements, or
20 years of history? There simply isn't a good way to visualize so much data.

In a query-driven approach we could just "page" the data, ask for the data in workable chunks, say 100 entities, at 
at time. Each result should include how much dat is actually available, and what range of that collection is
present in the given result. This way the UI component can figure out what to ask for when teh User needs access.

For example, a table control might only have screen space to show 5 rows at a time. The table could ask for 100
rows worth, and let the use scroll around. Then when they get close to the "edge" the control then asks for more.

But what do we do whn "push" is the default?

Some options:

### Subscriptions with Extra Parameters (:source/remote)

We could build the remote subscriptions such that they could take extra parameters to specify the range of data, and then 
do a "hidden fetch" to make sure the necessary data gets into the client's local cache. The change in the cache is what
actually triggers the UI re-render. This requires some interesting work with the Re-frame subscriptions, since they aren't 
really intended to generate side effects (that's the job of Events).



### Subscriptions that are really just notifications (:source/remote-notify-query)

There might be cases where the "push" is just a notification that some new data is available, maybe including which "entities" have
the novelty (either new ID's or the ID's of entities that have updates). In this case, the IDs in the "push" would be checked
against the client's local cache and a "hidden fetch" to get updates to entities in the cache. Again, it's the change in the cache 
that actually triggers the UI re-render. This also implies building 2 different subscriptions; one to "catch" the "notification push"
and one to actually provide the updated data to the UI for re-render.

Obviously this is a significant amount of work, but it can be built just once (as we do currently) and leveraged by everything.

