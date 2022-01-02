# Kafka

We've been using Kafka as an inter-service communications mechanism for a while now, and
we want to keep doing it.

## Component

We are pretty mush all-in on [Component](https://github.com/stuartsierra/component) currently, 
and we'd like to keep using it, but also take advantage of it for managing (state-ful Java-object) 
connections to Kafka. To this end, we found [rp-jackdaw-clj](https://github.com/rentpath/rp-jackdaw-clj). 

### rp-jackdaw-clj

This library (archived by the developers) includes:

- stream processor
- subscriber (a simple example of a stream processor that can be used like a consumer, but built 
on top of the Streams API instead of the lower-level Consumer API)
- producer
- topic registry (a dependency of the other components; a topic registry wraps a map of topic metadata 
and optional serde resolver config (schema registry url and type registry))
- mock processor, producer and registry components for use in tests

These mock implementations make it possible to test _without_ connecting to a running
Kafka broker.

> See [here](https://github.com/rentpath/rp-jackdaw-clj#testing-recommendations) for
> additional information)
