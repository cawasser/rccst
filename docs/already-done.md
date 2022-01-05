# Things We've Already Done

1. [X] [Combine back into a single server](/docs/single-server.md)
2. [X] [Push data from the server](/docs/data-push.md)
3. [X] [Use transit (or maybe just EDN?) to exchange ALL data between the Client and the Sever](/docs/transit.md)
4. [X] [Add Swaggerui using Compojure-api](/docs/swagger-ui.md)
5. [X] Get logging to work correctly (added ch.qos.logback/logback-classic "1.2.5", see [here](https://spin.atomicobject.com/2015/05/11/clojure-logging/))
6. [X] [Anti-forgery support](/docs/anti-forgery.md)
    1. [X] Toggle anti-forgery OFF when doing development
        1. [X] Server-side
        2. [X] Do some testing in "production mode"
7. [X] Add :edn format to the swagger-UI to make it easier to work with
8. [X] [Add a "real" database](/docs/database.md)
    1. [X] Postgres
    2. [X] SQLite
9. [X] Add [reveal](https://vlaaad.github.io/reveal/) tooling
10. [X] Docstrings
11. [X] Defaults for system configuration parameters
    1. [X] Webserver port
    2. [X] nRepl port
    3. [X] Broadcast timeout
12. [X] [Codox](https://github.com/weavejester/codox) (weavejester comes through again!)
13. [X] Get "repl reload" working again
    1. [X] start/stop/reset Component/System
    2. [X] modify URL handlers
    3. [X] (**FAILED**) add/remove URLs and handlers (requires a server bounce because Compojure is MACRO-based)
14. [X] version support using [metav](https://github.com/jgrodziski/metav)
15. [X] what does [cloverage](https://github.com/cloverage/cloverage) actually do, and how do we interpret the results?
    1. see [here](https://blog.jeaye.com/2016/12/29/clojure-test-coverage/)
    2. 1. [X] [De-complect (or at least simplify) things](/docs/decompleting.md)
16. [X] get rid of `websocket.publish`, since we already have `publish!` in `component.subscribers` - *** YES!!! ***
    1. [X] need to hook up Subscribers correctly
        1. [X] subscribe to multiple things
        2. [X] and FIX THE NAME!!!
        3. [X] client must subscribe to  (replacing :dummy, :something-else, and :a-third-thing)
            1. [X] :number
            2. [X] :string
17. [X] refactor `routes/routes`
18. [X] Remove "Broadcast" (Concept22 doesn't really support this notion of the server publishing on a timer)
19. [X] split into api // data-source
    1. [X] version
    2. [X] lookup
20. [X] Destructure hash-map keys out of Components at function boundaries
    1. [X] api
    2. [X] login
    3. [X] lookup - n/a
    4. [X] subscribe
    5. [X] version - n/a
21. [X] do we (can we?) need to stop the database connection using next.jdbc?

