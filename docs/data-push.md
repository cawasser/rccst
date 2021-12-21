<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [Pushing Data to Clients](#pushing-data-to-clients)
    - [General Purpose](#general-purpose)
    - [Reactive](#reactive)
  - [Examples](#examples)
    - [Rocky-Road](#rocky-road)
      - [FetchableDataSource](#fetchabledatasource)
        - [Pros](#pros)
        - [Cons](#cons)
      - [subscription-manager](#subscription-manager)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# Pushing Data to Clients


A key feature we want to maintain in our "new approach" is the asynchronous push of new or updated data from the "Server"
to each connected Client.

And we'd like it to be "general purpose" and perhaps even "reactive".


> NOTE: this discussion is _not_ about data queries from the server suing http(s) via GET, POST, etc.

### General Purpose

We want the mechanism to be the same regardless of how it is used. For example, we may have data sources in the Server that 

- are updated periodically (using a timer or schedule), 
- are updated by some third-party over the network (to which the server itself "subscribes")
- can be generated on the Server via the REPL

In each case we want all the subscribed clients to be pushed the new data.

### Reactive

In many systems the publication mechanism is exposed throughout the applications, in Clojure it would just be a function, which puts
the onus on the code that recognized the changing data to explicitly call the publication function. This is as opposed to something
like [Reagent]() or [Re-frame]() which provide a mechanism similar to ["watchers"]() on `atoms` that automatically call any function 
which dereferences the atom (Reagent) or atom-like value returned by a subscription (Re-frame). In this way the only action needed
is ot update the "data source" wiht its new value, and the watcher mechansim would automatically publish the change to all the 
subscribed clients.

## Examples

### Rocky-Road

#### FetchableDataSource

[Rocky-Road](https://github.com/cawasser/rocky-road), like [Vanilla](https://github.com/cawasser/vanilla) 
before it, is built on top of a library called [dashboard-clj](https://github.com/multunus/dashboard-clj), 
and we used its push mechanism without really understanding how it worked. Now we ned to understand it, in order
to decide if we should keep using it.

To provide asynchrony, Rocky-road, specifically [WebsocketServer](https://github.com/cawasser/rocky-road/blob/e32bfd804295f5d23d5f94403472c7cb0d41ef1b/bases/vanilla/src/rocky_road/dashboard_clj/components/websocket.clj#L39),
uses a Clojure [core-async](https://github.com/clojure/core.async) [go-loop](https://github.com/cawasser/rocky-road/blob/e32bfd804295f5d23d5f94403472c7cb0d41ef1b/bases/vanilla/src/rocky_road/dashboard_clj/components/websocket.clj#L51)
to listen to a channel for messages to send, which are then transmitted to connected client's using the socket's
[send-fn](https://github.com/cawasser/rocky-road/blob/e32bfd804295f5d23d5f94403472c7cb0d41ef1b/bases/vanilla/src/rocky_road/dashboard_clj/components/websocket.clj#L59).

The mechanism also mixes together the [out-chan](https://github.com/cawasser/rocky-road/blob/e32bfd804295f5d23d5f94403472c7cb0d41ef1b/bases/vanilla/src/rocky_road/dashboard_clj/components/websocket.clj#L45)
with [channels associated with each data-source](https://github.com/cawasser/rocky-road/blob/e32bfd804295f5d23d5f94403472c7cb0d41ef1b/bases/vanilla/src/rocky_road/dashboard_clj/components/websocket.clj#L48).

Each data-source has a size=1 output channel [associated](https://github.com/cawasser/rocky-road/blob/e32bfd804295f5d23d5f94403472c7cb0d41ef1b/bases/vanilla/src/rocky_road/dashboard_clj/data_source.clj#L50)
with it, which is used so the data-source is totally self-contained: when there is new data available,
a [data->event](https://github.com/cawasser/rocky-road/blob/e32bfd804295f5d23d5f94403472c7cb0d41ef1b/bases/vanilla/src/rocky_road/dashboard_clj/data_source.clj#L53) 
containing the update is [put onto the output-chan](https://github.com/cawasser/rocky-road/blob/e32bfd804295f5d23d5f94403472c7cb0d41ef1b/bases/vanilla/src/rocky_road/dashboard_clj/data_source.clj#L42).

This looks to be a clever solution to a typically thorny problem which is often solved by passing a lot of
context around the system. For example, the same result could be achieved by passing the out-chan of the 
WebsocketServer to every data-source, but using core.async as a "common carrier" is cleaner. Each "end" only 
needs to know of core.async, rather that the actual implementor on either side.

This is the implementation the dashboard-clj developers chose for providing "reactive" updates. 

##### Pros

The mechanism is clean, fairly compact, and not terribly difficult to understand, once you really dig into it. Each
data source has a single channel to deal with, the WebsocketServer automatically takes data from all of the data source-
specific channels using ony 3 extra lines of code.

##### Cons

1. The major Con seems to be that all the FetchableDataSources must be defined _prior_ to constructing the WebsocketServer, 
since the channels are mixed as part of WebsocketServer construction, via [(start)](https://github.com/cawasser/rocky-road/blob/e32bfd804295f5d23d5f94403472c7cb0d41ef1b/bases/vanilla/src/rocky_road/dashboard_clj/components/websocket.clj#L41). 
Adding data-sources at any time _after_ server startup is **NOT** possible. 

>Q: Is the ability to add data sources while the server is running a critical, or even desirable, feature?

2. A second Con is that the out-chan inside WebsocketServer is _not_ exposed, only the send-fn, via
[:chsk-send!](https://github.com/cawasser/rocky-road/blob/e32bfd804295f5d23d5f94403472c7cb0d41ef1b/bases/vanilla/src/rocky_road/dashboard_clj/components/websocket.clj#L66).
This means that we need to pass the entire WebsocketServer, or worse, the entire SystemMap, among the various
calls to get the critical send-fn to the right spot. See [here](https://github.com/cawasser/rocky-road/blob/e32bfd804295f5d23d5f94403472c7cb0d41ef1b/bases/vanilla/src/rocky_road/vanilla/subscription_manager.clj#L67)
and [here](https://github.com/cawasser/rocky-road/blob/e32bfd804295f5d23d5f94403472c7cb0d41ef1b/bases/vanilla/src/rocky_road/vanilla/subscription_manager.clj#L72).
As can be seen in the code, Rocky-road store the SystemMap in a global atom to make this easier on the coding, but
just as complex in the logic.

3. The "reactive" mechanism seems inextricably tied to the scheduled updates ot data-sources baked into the data-source definitions.


#### subscription-manager

Rocky-road also provides a more "on demand" mechanism for pushing data to connected clients, specifically
[subscription-manager](https://github.com/cawasser/rocky-road/blob/master/bases/vanilla/src/rocky_road/vanilla/subscription_manager.clj#L72).

>Note: Let's be frank right up front: subscription-manage is ["complected"](https://medium.com/netdef/complect-ca7e65f6354d); it 
> manages the subscribers (adding and removing) _and_ it provides function to push messages to one or more subscribers.
> 
> We should split these apart if we choose to move forward with this kind of implementation.

Unfortunately, this mechanism cannot be considered "reactive" as the push function must be called by the code implementing the
data update.
