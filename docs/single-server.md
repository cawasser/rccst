# Combine the project back into a Single Server

As originally built, there are actually 2 separate "servers"

1. the "UI Server" which provide the Reagent/Re-frame-based UI, hosted at port 8280, and
2. the "Service Server" which provides the data updates, hosted at port 5000.

We drew inspiration and code examples from [here](https://github.com/DavidVujic/sente-with-reagent-and-re-frame).

## Solution

Once we had the overall Sente approach working, this was fairly trivial:

1. Turn off the :dev-http in [shadow-cljs.edn](https://github.com/cawasser/rccst/blob/ffcb5a3a5cb8fbb390a1455032a478889a6ea90e/shadow-cljs.edn#L5)
2. Change the hard-coded http-ports in both [bh.rccst.core](https://github.com/cawasser/rccst/blob/ffcb5a3a5cb8fbb390a1455032a478889a6ea90e/src/clj/bh/rccst/core.clj#L12) and [bh.rccst.subscriptions](https://github.com/cawasser/rccst/blob/ffcb5a3a5cb8fbb390a1455032a478889a6ea90e/src/cljs/bh/rccst/subscriptions.cljs#L32)
3. Add the "basic" routes for `(GET "/" ...)` to [bh.rccst.routes/routes](https://github.com/cawasser/rccst/blob/ffcb5a3a5cb8fbb390a1455032a478889a6ea90e/src/clj/bh/rccst/routes.clj#L15)
   1. The _trick_ here is that we need to wrap [(content-type ... "text/html")](https://github.com/cawasser/rccst/blob/ffcb5a3a5cb8fbb390a1455032a478889a6ea90e/src/clj/bh/rccst/routes.clj#L15) around a (resource-response ...) for the html file
   2. Also be sure to include [`(resources "/")`](https://github.com/cawasser/rccst/blob/ffcb5a3a5cb8fbb390a1455032a478889a6ea90e/src/clj/bh/rccst/routes.clj#L18) to ensure the client can load _app.js_ and any _css_ files.


