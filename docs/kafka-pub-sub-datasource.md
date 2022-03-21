<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [Kafka, Pub-Subs and Data Sources](#kafka-pub-subs-and-data-sources)
- [How Rocky-Road Does It](#how-rocky-road-does-it)
  - [RCCST Approach](#rccst-approach)
    - [Topologies](#topologies)
  - [Composable UI](#composable-ui)
    - [Definition Data Structure](#definition-data-structure)
    - [Visualizing the Digraph](#visualizing-the-digraph)
    - [What about data sources that have too much data for the client to process all at once?](#what-about-data-sources-that-have-too-much-data-for-the-client-to-process-all-at-once)
      - [Subscriptions with Extra Parameters (:source/remote)](#subscriptions-with-extra-parameters-sourceremote)
      - [Subscriptions that are really just notifications (:source/remote-notify-query)](#subscriptions-that-are-really-just-notifications-sourceremote-notify-query)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# Kafka, Pub-Sub, and Data Sources


There are three "components" in the system that all need to work together:

- Kafka
- the Pub-Sub component (which uses the Websocket Component)
- DataSource

Rocky-Road works in a related way, so let's start there.

## How Rocky-Road Does It

In the Rocky-Road implementation of Vanilla, the Websocket uses [core.async](https://github.com/clojure/core.async) to 
listen to a collection of channels as an input. Separately, each DataSource create a single
channel to use as output.

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
updates (especially asynchronous updates, say from Kafka), the DataSource can just put the
update message Pub-Sub, which is a dependency for the DataSources component. 

This _should_ make it possible to create individual DataSources only when some user first 
subscribes to each^1.



> ^1 Currently, we don't track how many times any given client subscribes to the same source, just 
> subscriptions across all clients.


## Topologies

Have the topologies depend on the :pub-sub component, and have it expose `start!` and `stop!` functions
to start and stop the individual topologies we might want to have.

> like listening to multiple topics to provide data to connected clients.


## Composable UI

We have a goal of building UI's by means of a data-structure that describes the [Event Model](https://eventmodeling.org) (also
known as a [Directed graph](https://en.wikipedia.org/wiki/Directed_graph)) of data flowing through the UI, implemented by a 
collection of UI components and local and remote data sources.

This is much like how [Willa](https://github.com/DaveWM/willa) defines Kafka topologies, which also can be modeled with an 
Event Model/Digraph.


### Definition Data Structure

Like Willa, we break the directed graph description into part, in our case three:

1. `:components` - the specific elements (ui, sources, and stream processing functions) that make up the overall ui "widget"
2. `:link` - how the different components hook into each other, where data comes from, where it goes, how it gets displayed
3. `:layout` - a stylized representation of how the UI components are organized on the display, roughly by rows and columns

### Visualizing the Digraph

Since the UI is defined by a data structure that describes a digraph (directed graph), we can easily visualize it, using a 
combination of [Loom](https://github.com/aysylu/loom) and some relevant React component, such as [react-flow](https://reactflow.dev). 
This also opens the door to _constructing_ the UI using visual tools.


## What about data sources that have too much data for the client to process all at once?

What about sources that just have too much data, say 10,000 entities, or 100,000 measurements, or
20 years of history? There simply isn't a good way to visualize so much data.

In a query-driven approach we could just "page" the data, i.e., ask for the data in workable chunks, say 100 entities, 
at time. Each result should include how much data is actually available, and what range of that collection is
present in the given result. This way the UI component can figure out what to ask for when the User needs access.

For example, a table control might only have screen space to show 5 rows at a time. The table could ask for 100
rows worth and let the user scroll around. Then, when they get close to the "edge" the control can ask for more rows.

But what do we do when "push" is the default?

### Subscriptions with Extra Parameters (`:source/remote`)

We could build the remote subscriptions such that they could take extra parameters to specify the range of data, and then 
do a "hidden fetch" to make sure the necessary data gets into the client's local cache. The change in the cache is what
actually triggers the UI re-render. This requires some interesting work with the Re-frame subscriptions, since they aren't 
really intended to generate side effects (that's the job of Events).


### Subscriptions that are really just notifications (`:source/remote-notify-query`)

There might be cases where the "push" is just a notification that some new data is available, maybe including which "entities" have
the novelty (either new ID's or the ID's of entities that have updates). In this case, the IDs in the "push" would be checked
against the client's local cache and a "hidden fetch" to get updates to entities in the cache. Again, it's the change in the cache 
that actually triggers the UI re-render. This also implies building 2 different subscriptions; one to "catch" the "notification push"
and one to actually provide the updated data to the UI for re-render.

> Actually, after some more time in the hammock, we would have to have an Event for processing the "notification" push from the 
> server anyway and this could be processes as a reg-event-fx which in turn calls `:dispatch` to fetch the actual data and the 
> `:on-success` handler of that call would put the data into app-db's `:sources`, triggering the "normal" `:source/remote` 
> subscription.

Obviously this is some amount of work, but it can be built just once (as we do currently) and leveraged 
by everything. The key is having the build for `:source/remote-notify-query` (or whatever we call it) build ***BOTH*** the "trigger
catcher" event and the event to put the query result into `:sources`. The subscription for the `:source` data when the
query makes the update is already built, so there is no extra work there.

> The next question is "Do we expose the notify/query" nature of this kind of `:source/xxx` to the "user"? In other words, do we use this
> new type of `:source/xxx` directly in the composite definition?
> 
> The alternative is to add another level of indirection; we still call it `:source/remote` in the composite-def, but that entry 
> (like with the ui components) is a lookup into another registry that tells how the source _actually works_. This is 
> reasonable, since we should really be sending the `:source` registry over from the server at login anyway.

This concept could also be extended by adding a third Event which allows the UI to run the query on-demand,
such as when a Table needs another page-worth of data. This event calls the server and puts the result into the same
`:source` element as the others, so the standard subscription still causes the re-render when the query returns.

Another alternative might be to add a contorl to all UI-components that indicate when the data it "stale"
and the user can click to get the updated data.
