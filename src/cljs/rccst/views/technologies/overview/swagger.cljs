(ns rccst.views.technologies.overview.swagger
  (:require [rccst.views.technologies.overview.overview :as o]))

(defn overview []
      (o/overview "Swagger"
        "### _API Development & Documentation_

Swagger helps developers build more robust and expressive API's, quickly and easily using their
set of open-source tools.  **Swagger UI** is just one of the many tools Swagger offers.
It provides an interactive web page for your applications API, that allows you to visualize, manipulate,
and test your API from a single, well organized web page.  Developers are able to execute and monitor
the API requests they send, and results they receive, in an easy-to-use auto generated page for each API endpoint.

> To see a live example of a Swagger UI page for a sample application, [click here](https://petstore.swagger.io/?_ga=2.239271107.1151330550.1642458404-998528997.1642458404)"

                "/imgs/swagger-ui-logo.png"))
