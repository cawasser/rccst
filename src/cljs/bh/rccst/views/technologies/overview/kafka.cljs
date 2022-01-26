(ns bh.rccst.views.technologies.overview.kafka
  (:require [bh.rccst.views.technologies.overview.overview :as o]))

(defn overview []
  (o/overview "Kafka"

    "### _A Platform for Event Streaming_

#### What is event streaming?
Events are the basis of our system; they drive the design and architecture of the system as a whole,
as the flow of events defines what the system needs to do.  An \"event\" is a record that \"something happened\"
in the system, whether that be a DB update, a new sensor reading, a button click, anything that \"happens\"
can be captured as an event, and sent to a stream.  \n\nAn \"event stream\" is the practice of capturing
this data in real-time, storing these event streams durably for later retreival, and reacting to the event
streams in real-time or retrospectively; routing the event streams to different destinations and technologies
as needed.
**Simply put, an event stream is a flow of data, that can be read in real-time by listeners,
and reacted to accordingly.**\n\n

#### Kafka is one of the most widely used event streaming platforms\n
Kafka combines three key capabilities so you can implement your use cases for event streaming end-to-end
with a single battle-tested solution:\n\n
- To **publish** (write) and **subscribe** to (read) streams of events, including continuous import/export of your data from other systems.\n
- To **store** streams of events durably and reliably for as long as you want.\n
- To **process** streams of events as they occur or retrospectively.\n\n
And all this functionality is provided in a distributed, highly scalable, elastic, fault-tolerant, and secure manner.
Kafka can be deployed on bare-metal hardware, virtual machines, and containers, and on-premises as well as in the cloud.
You can choose between self-managing your Kafka environments and using fully managed services offered by a variety of vendors.\n\n

#### How does it work?\nKafka runs in a distributed system of servers and clients.  \n
**Kafka Servers**:  run as a cluster of one or more servers, that handle the various import/export/storage needs
 of the event stream data.  Its highly scalable and fault-tolerant, and seamlessly rebalances if any servers come on or offline.\n
 **Clients**: are the microservices/applications that read, write, and process streams of events.  These are the
 pieces of your system that capture and create events, as well as read in events from streams and process the data as needed."

    "/imgs/kafka-logo-png-transparent.png"))
