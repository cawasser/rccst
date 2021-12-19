# Pushing Data to Clients


A key feature we want to maintain in our "new approach" is the asynchronous push of new or updated data from the "Server"
to each connected Client.

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


#### subscription-manager

Rocky-road also provides a more "on demand" mechanism for pushing data to connected clients, specifically
[subscription-manager](https://github.com/cawasser/rocky-road/blob/master/bases/vanilla/src/rocky_road/vanilla/subscription_manager.clj#L72).

>Note: Let's be frank right up front: subscription-manage is ["complected"](); it manages the subscribers (adding and removing) _and_
> it provides function to push messages to one or more subscribers.
> 
> We should split these apart if we choose to move forward with this kind of implementation.


