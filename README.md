# gregorsamsa

[A Guide to Apache Kafka](https://kafka.apache.org/)

[Quick Start for Apache Kafka](https://docs.confluent.io/current/quickstart/ce-docker-quickstart.html)

[Docker Image Installation](https://docs.confluent.io/current/installation/docker/image-reference.html)

#### Dependencies

[Java 8](https://www.oracle.com/br/java/technologies/javase/javase-jdk8-downloads.html)

[Maven Project](https://maven.apache.org/guides/getting-started/)

[IntelliJ Idea Community Edition](https://www.jetbrains.com/pt-br/idea/download/)

[JUnit Framework](https://junit.org/junit4/)

[Apache Log4j](https://logging.apache.org/log4j/2.x/)

### Ports

Zookeeper port: 2181

Apache Kafka Brokers: 9092

Confluent Control Center: 9021

Kafka Connect REST API: 8083

KSQL Server REST API: 8088

REST Proxy: 8082

Schema Registry REST API: 8081


* O Kafka was originnaly developed by LinkedIn
* Open-source distributed Streaming platform;
* You can **sign** & **publish** registrys/events
  * Real-time Processing, or;
  * Storage (Fault-Tolerant);
* Kafka can't be called only as a *Mensageria* due to its capacity of storage - distributed streaming platform;
* Kafka is developled in Scala & Java
* Kafka is executed as a cluster in one or more servvers that can include various datacenters;
* Kafka stores events flows in categories called **topics**
* Each event consists of an **key, value and timestamp**
* Untill Kafka 2.6.x -> Java 8;

#### Producer API

Allows that an application publishes a flow of events in on or more Kafka topics


#### Consumer API

Allows that an application assigns to one or more Kafka topics

#### Streams API

Allows that an application make transformations into incoming flow events to become output flow of events

#### Connector API

Allows to create and execute connections between topics of producers and consumers that are re-usable

#### Streaming Fundamentals

* When DataLake came up, the idea behind was to save it in its raw format and then process it;
* More and more, companies are taking everything that happens in their environment (Business Events) and throwing into a Event Bus. Then applications can consume this events; We are talking about Real-time Stream Processing Applications;
* Data Lake are stationary, but Stream is data in motion; When we process data that is already sitting on a Data Lake, we called it simply Data Processing;
 * How to Identitfy and Model Events?
 * How to Stream?
 * How to Transport Events?
 * How to Process Events?
* An event is a business action that we capture as a **Data Object**;
* Real-time Stream Processing Challenges:
 * Many-to-Many relationships becomes a **Stream Mash**. One Producer is sending events to a lot of consumers - they send to a Event Bus. The consumers also consuming events  from a lot of Producers
 * With Real-time Streaming, we need to deliver:
 * 1. Time sensitivity
 * 2. Decoupling
 * 3. Data format evolution
 * 4. Reliability (Fault-tolerant, checkpoints)
 * 5. Scalability
* A good design to achieve this is using **Publish/Subscriber** Design:

#### Publish/Subscriber Design With Broker and Topic (Log File) - Kafka Design

Pub/Sub semantix provides us the 5 criteria for a good streaming platform: Time-sensitivity, Decoupling, Data Format Evolution, Reliability, Scalability;

Time-Sensitivity: producers can send messages to the Kafka broker as quickly as the events occurs; Data consumers can fetch data from the broker as soon as they arrive at the broker;

Decoupling: producers and consumers are completely decoupled; No direct connections. They always interact with the Kafka Broker using a consistent interface; The Producers don't need to be concerned about who is consuming the data - it can send the data once don't worrying about how many consumers will fetch the data. Producers and consumers can be added, removed and updated as the business case evolves;

Data Format Evolution: Coupled with a Schema Registry and a Connection Service, the Producers and Consumers would have the flexibility to handle Schema Evolution. 

Reliability: Offered by Kafka Architecture and is designed; Kafka is distributed, fault-tolerant and highly scalable platform. 

Scalability: Offered by Kafka Architecture and is designed; Kafka is distributed, fault-tolerant and highly scalable platform.

 * **Publisher** - application that sends events
 * **Subscriber** - appplications that consume events sent by the Publisher
 * **Broker** - Main point of a Publisher/Subscriber system. The heart of the Publisher/Subscriber system; Sits in the middle of Publishers and Subscribers; Broker is responsible for receiving the messages from a Publisher (Producer), storing in a LogFile and sending it to the Subscribers (Consumers); Any application that wants to send a message should send it to the broker. Broker receives the message, sends its aknowledge and persists the data into a **log file**. When consumer application wants to read the message, it consume it from the broker; 
 * **Topic** - Is **message namespace**. Offer mechanismo to categorize the messages. You can think of the topic is a TABLE_NAME if you think the broker as a database. Producer always writes the message to a topic and consumer reads it from the topic. Broker creates a **log file for each topic**. The broker maintains multiple topics like a database that maintains many table. When producer sends a message, it defines the topic name for the message and the broker persists the message in the corresponding log file. 

#### Enter the World of Apache Kafka

Apache Kafka _Broker Responsibilities_:

* _Receive_ messages from the producers and acknowledge the successful receipt;

* _Store_ the messages in a log file to safeguard it from potential loss;

* _Deliver_ the messages to the consumers when they request it;

Apache Kafka Architecture:

Architecture | Importance
------------ | -----------
Kafka **Storage** Architecture | This topic will help understand what is replication factor, offset, offset index, logs and partitions.
Kafka **Cluster** Architecture | THis topic will help understand concepts associated with Cluster formation, ZooKeeper e Controller.
Kafka **Distribution** Architecture | Tie up Storage and Cluster Architecture and understand how the work is distributed. Concepts as leaders, followers, commited and uncommited messages


#### Kafka Storage Architecture

Topic is a logical name to group your messages. Broker defines a log file to each Topic to store the messages. However, this log files are **partitioned, replicated and segmented**. You must define a HOME_DIRECTORY (tmp) for the Kafka Brokers. That means that everything that my Kafka Brokers are going to create will reside in this HOME_DIRECTORY. This makes things easier to understand what is happening under the hood.

In the HOME_DIRECTORY for the Apache Kafka Brokers (in our case, we are working with 03 brokers), we will have:

* KAFKA_HOME_DIRECTORY
  * kafka-log-0
      * .lock
      * cleaner-offset-checkpoint
      * log-start-offset-checkpoint
      * meta.properties
      * recover-point-offset-checkpoint
      * replication-offset-checkpoint
  * kafka-log-1
      * .lock
      * cleaner-offset-checkpoint
      * log-start-offset-checkpoint
      * meta.properties
      * recover-point-offset-checkpoint
      * replication-offset-checkpoint
  * kafka-log-2
      * .lock
      * cleaner-offset-checkpoint
      * log-start-offset-checkpoint
      * meta.properties
      * recover-point-offset-checkpoint
      * replication-offset-checkpoint
  * zookeeper-data

When a Broker starts, it will create some initial files. Most of the files will be empty at the beggining. 

Command Line to create a topic:

~~~bat

kafka-topics.bat --create --zookeeper localhost:2101 --topic invoice --partitions 5 --replication-factor 3

~~~

When you create a topic in Apache, you must specify **partitions** & **replication-factor**:

* Partitions: In Kafka, a single topic may store millions of messages. Is not practical to keep all this messages in a single file. Topic partitions are a mechanism to break the file into smaller parts (partitions). For Apache Kafka is nothing but a **physical directory**. Creates a separate director for each Topic Partition. If Partitions==5, Kafka will create 5 folders for the Topic Invoice. 

* Replication Factor: Specifies **Number of Copies for each Partition**. Number of Replicas (15) = Partitions (5) * Replication-Factor (3). Kafka will create 15 directories for the Topic Invoice created. 

After the creation of the Invoice Topic with --partitions 5 --replication-factor 3, we will have this distributions in the Apache Kafka HOME_DIRECTORY:

* KAFKA_HOME_DIRECTORY
  * kafka-log-0
     * invoice-0
     * invoice-1
     * invoice-2
     * invoice-3
     * invoice-4
        * .lock
        * cleaner-offset-checkpoint
        * log-start-offset-checkpoint
        * meta.properties
        * recover-point-offset-checkpoint
        * replication-offset-checkpoint
  * kafka-log-1
     * invoice-0
     * invoice-1
     * invoice-2
     * invoice-3
        * .lock
        * cleaner-offset-checkpoint
        * log-start-offset-checkpoint
        * meta.properties
        * recover-point-offset-checkpoint
        * replication-offset-checkpoint
  * kafka-log-2
     * invoice-0
     * invoice-1
     * invoice-2
     * invoice-3
        * .lock
        * cleaner-offset-checkpoint
        * log-start-offset-checkpoint
        * meta.properties
        * recover-point-offset-checkpoint
        * replication-offset-checkpoint
  * zookeeper-data

All the 15 Partitions created for Invoice Topic are part of the same topic but are **distributed** along the available brokers. This directories are there to give home to log files where the messages are stored. We can classify Topic Partition Replicas into two categories:

* Leader Partitions: The first five directories created for Invoice Topic. The Leaders are created first.

* Follower Partitions: Two more Follower Partitions for each Leader Partition with --replication-factor 3. The follower is a duplicate copy of the leader. They are directories. 

Command to descibre a Kafka topic and discover where the leaders/followers directories reside:

~~~bat

kafka-topics.bat --describe -zookeeper localhost:2101 --topic invoice

~~~

* Log Segments and Maximum Segment Limit (1GB or 1 Week Of Data by default)

Instead of creating a huge file in the Leader/Follower Replica Directory, Kafka created **Log Segments**. When the partition receives it first messages, it stores the message in the first segment. The next message also goes in the same segment. The segment will grow untill the **maximum segment limit** is reached. If the limit is reached, it closed the file and starts a new segment. The default maximum segment size is **either 1GB of data or week of data**.

* Message Offsets -64-bit integer that uniquely identify each message in a Partition. They are not unique across the topic.

Each message in a partition is **uniquely indentified by a 64-bit integer OFFSET**. Every Kafka message within a partition is uniquely identified by the offset. The offset of the first message would be 0000, the second message 0001, and so on. _The numbering also keeps ACROSS THE SEGMENTS to keep the offset unique within the partition_. If the offset of the last message of the first segment is 30652 and the maximum segment limit is reached. Kafka will close the segment file and open a new log segment - _the first message in the new segment should be 30653 to keep the offset unique within the partition_. **To make identification easier, the segment file name is also suffixed by the first offset in that segment**.

Offsets are not unique across the topic. So if you want to look for a specific message, you'll need three things:

1. Topic Name
2. Partition Number
3. Offset Number

Only the offset number and topic name will not be enough because the offset is not unique across the topic. Message Offset is unique across the partition. The consumer application requests messages based on the message offset. Kafka allow consumers to start fetching messages from a giving offset number - this means that if the consumer demands for messages beggining at offset 100, the broker must be able to locate the message for offset 100.

* .index (offset)

To help brokers rapidly find the message for a given offset, Kafka maintains an **index of offsets**. The index files are also segmented for easy management, and they are also stored in the partition directory along with the log file segment. 

* .timeindex:

Kafka allows consumers to start fetching messages based on the offset number. However, in many use cases, you might want to seek messages based on timestamp. These requirements are as straightforward as you want to read all the events that are created after a specific timestamp. To support such needs, Kafka also maintains a timestamp for each message, builds a time index to quickly seek the first message that arrived after the given timestamp. The time index is like the offset index and is also segmented and stored in the partition directory along with the offset index.

#### Kafka Cluster Architecture

Scalability side of Apache Kafka and how Kafka Cluster is formed. Kafka Brokers are configured to form a **cluster of various Kafka Brokers: group of Brokers that work together to share the workload and that's how Apache Kafka becomes a distributed and scalable system**.

As the workload grows, you can increase the number of BROKERS in the cluster. 

* _Who Manages Cluster Membership_? **Zookeeper**

In a typical distributed system, there is a master node that maintains a list of active cluster members. The master always know the state of other members. 

If a broker dies, we need someone to reassign that work to an active broker to ensure that the cluster continues to function.

**Kafka Broker is a master-less cluster**. It does not follow a master-slave architecture. However, it uses **Apache Zookeeper** to maintain the list of active brokers. Every Kafka Broker has a unique id that you define in the broker configuration file. We also specify the Zookeeper connection details in the broker configuration file. 

When the broker starts, it connects to the Zookeeper and creates an ephemeral node using broker_id to represent an active Broker session. The ephemeral node remains intact as longs as the broker session with the Zookeeper is active. When a broker looses connectivity to Zookeeper for some reason, the Zookeeper automatically removes that ephemeral node.

So, the list of active brokers in the cluster is maintaned as the list of ephemeral nodes under the **broker/ids path in the Zookeeper**. 

You can start the Zookeeper shell with this command:

~~~sh

zookeeper-shell.bat localhost:2181

~~~

With this, we are connected to the **Zookeeper in my Kafka cluster**. We can easily look what we have in the Zookeeper database inside the Zookeeper shell with the ls command.

~~~sh

# Check brokers inside Zookeeper shell

ls /brokers

# IDS of the brokers that are active in this Kafka Cluster (The list of active nodes in the cluster that in common clusterized architectures are managed by the master, but Kafka is master-less and has a Zookeeper)

ls /brokers/ids

~~~

* _Who perform the routine Administrative Tasks in the cluster - Who's gonna clean Gregor Samsa's room_? **Controller - a normal Broker elected to it**

This activities are also typically performed by a master in a clustered environment. 

The activities of monitoring the list of active brokers and reassingn tasks when a broker enters/leaves the cluster is done by a Cluster **Controller**.

The controller is not a master - it is simply a **broker that is elected as a controller to take up some extra responsibilities**.

That means that controller **also acts as a regular broker**. So, if you have a single node cluster, it serves as a controller as well as a broker.  However, the is only one controller in the Kafka Cluster at any point in time. The controller is responsible for monitoring the list of active brokers in the Zookeeper. When the controller notices that a broker left the cluster, it knows it is time to reassign some work to other brokers. 

The controller election is straightforward. The first broker that starts the cluster becomes the controller creating a ephemeral node in the Zookeeper. When other brokers start, they also try to create an ephemeral controller node in Zookeeper, but they receive an exception as "node already exists" - the controller is already elected. When the controller dies, the ephemeral node in zookeeper disappears. Now every broker tries to create an ephenmeral controller node in Zookeeper, but only one suceeds - there is always a controller in the cluster and **only one**.

~~~sh

# In Zookeeper shell:

get /controller

~~~
















