(ns bh.rccst.views.technologies.overview.micro-services
  (:require [bh.rccst.views.technologies.overview.overview :as o]))


(defn overview []
  (o/overview "Microservices"
    "### _Modular Software Architecture_

Microservices - aka the microservice architecture - is an architecture style that structures a system as a collection of
services that are: highly maintainable and testable, loosely coupled, independently deployable, organized around business
capabilities, and owned by a small team.  Microservices enable the rapid, frequent, and reliable delivery of large,
complex applications.  It also allows for technological innovation and trying out new ideas on a much smaller and easier
to work with scale.  Breaking a stovepipe system up into easily manageable microservices allows for the system to have
greater overall flexibility, while making development and maintenance easier and faster.

> For more information on Event Modeling, please see:
>
> [Event Sourcing Microservices with Kafka](https://medium.com/kontenainc/event-sourcing-microservices-with-kafka-2568801527d8)
>
> [Event-Driven Microservices - not (just) about Events!](https://www.youtube.com/watch?v=DzGuDNHsOQ0)
>
> [Distributed Systems, Microservices and Interstellar Travel](https://www.youtube.com/watch?v=IRrh4VUHhNY)
>
> [Microservices Architecture](https://microservices.io)
    "))
