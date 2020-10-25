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

If a broker that is a controller dies and then gets back again, he will not reassume the role as controller. The ephemeral node in Zookeeper will be already occupied by another broker and when he tries to create that ephemeral node, he will get a exception "node already exists". Someone else is already elected as a controller.

Summarizing: Zookeeper is the database of the Kafka Cluster control information and one of the broker is elected to take the responsibilies as a controller to monitor Zookeeper. The controller maintains his activities of a normal broker.

#### Kafka Work Distribution Architecture - Tie up relationships between Storage Architecture & Cluster Architecture.

Kafka Cluster is a group of Brokers - these brokers may be running on individual machines. In a large production cluster, you might have organized these machines in multiple racks. 

_How are the partitions allocated to brokers? How we decide which broker should be maintaining which partition? Are there any rules to assign worker between brokers?_

Suppose you have 6 brokers running in 2 racks.

Rack | Brokers
---- | -------
Rack 1 | B0, B1, B2
Rack 2 | B3, B4, B5

You have a topic with --partitions 10 and --replication-factor 3 (Number Of Replicas (30) = Partitions (10) * Replication Factor (3))

Kafka now have 30 replicas to allocate to 6 brokers. Kafka tries to achieve **two goals** for this partition allocation:

1. **Event Distribution**: Partitions are distributed evenly as much as possible to achieve workload balance. 

2. **Fault-Tolerance**: Followed Partitions should be placed in different machines to achieve fault-tolerance.

To distribute the 30 replicas, Kafka makes the following steps:

* Make a ordered list of available brokers: Kafka begins with a randomly chosen broker in a rack and places it into a list (Rack 1 - Broker 0, per example). The next broker in the list must be from a different rack (Rack 2 - Broker 3, per example). The next one again comes from the first rack (Rack 1 - Broker 1, per example). This goes on as an alternating process for selecting another broker in a different rack. Example of ordered list achieved by this process:

- [x] R1-BO

- [x] R2-B3

- [x] R1-B1

- [x] R2-B4

- [x] R1-B2

- [x] R2-B5

The second step is assign partitions to this list. We have 30 partitions replicas to create to these six brokers. Ideally, Kafka should place five partitions on each broker to achieve the first goal of evenly distributing the partitions. However, we have another goal to achieve fault-tolerance (if a broker fails for some reason, we need to have a copy on other broker).

Further, we need to make sure that if a entire rack fails, we still have a copy on a different rack. 

* Assign Leaders and Followers to the list in order

Per example, the first partition (--partition 10) has 03 copies (--replication-factor 3): all we need to do is to make sure that those three copies are not allocated to the same broker in the ordered list above.

Once we have the ordered list of available brokers, assigning partitions is as simple as assign one to each broker using a **round robin** method. Kafka starts with the leader partitions and finishes creating all leaders first.

So, we take the leader of Partition 0 (P0) and assign it to Broker 0 (R1-B0).

The leader of Partition 1 (P1) goes to Broker 3 (R2-B3) - second in the ordered list.

The leader of Partition 2 (P2) goes to Broker 1 (R1-B1) - third in the ordered list. 

The leader of Partition 3 (P3) goes to Broker 4 (R2-B4) - fourth in the ordered list.

The leader of Partition 4 (P4) goes to Broker 2 (R1-B2) - fifth in the ordered list.

The leader of Partition 5 (P5) goes to Broker 5 (R2 - B5) - sixth in the ordered list. So it goes assigning the Partitions Leaders first assuming the ordered list ...

The leader of Partition 6 (P6) goes to Broker 0 (R1-B0) - first in the ordered list since we only have 6 brokers; less than the number of partitions leaders;

The leader of Partition 7 (P7) goes to Broker 3 (R2-B3) - second in the ordered list since we only have 6 brokers; less than the number of partitions leaders;

The leader of Partition 8 (P8) goes to Broker 1 (R1-B1) - third in the ordered list since we only have 6 brokers; less than the number of partitions leaders;

The leader of Partition 9 (P9) goes to Broker 4 (R2-B4) - fourth in the ordered list since we only have 6 brokers; less than the number of partitions leaders;

Once the leader partitions are allocated with the ordered list, the followers start to be allocated. It starts creating the first follower that will be allocated in the **second** broker in the list following a round robin rule.

The first follower of Partition 0 (P0) will be allocated in Broker 3 (R2-B3) - the second broker in the ordered list;

The first follower of Partition 1 (P1) goes to Broker 1 (R1-B1) - the third broker in the ordered list; So it follows ...

Finnaly it starts allocating the second follower of each partition (--replication-factor 3). It maps them to the same broker list by jumping one more broker from the previous start.

The second follower of Partition 0 (P0) goes to Broker 1 (R1-B1) - the **third** broker in the ordered list.

Brokers In Ordered List | Leaders | Followers | Followers
----------------------- | ------- | --------- | ---------
R1-B0 | P0, P6 | P5 | P4
R2-B3 | P1, P7 | P0, P6 | P5
R1-B1 | P2, P8 | P1, P7 | P0, P6
R2-B4 | P3, P9 | P2, P8 | P1, P7
R1-B2 | P4     | P3, P9 | P2, P8
R2-B5 | P5     | P4     | P3, P9

This is what happens when you create a topic. Leaders and Followers of the topic are created across the cluster. If you look at the allocation, we couldn't achieve a perfect even distribution. The Broker 0 has got 4 partitions, while Broker 4 has six partitions. However, we made an ideal fault-tolerance at the price of little disparity.

* Kafka Work Distribution Architecture Part 2 - Defining Roles for Broker

Partitions can be: Leaders or Followers.

Brokers also can act as: Leaders or Followers.

For a Broker to act as a Leader means one thing: **the leader is responsible for all the request from the producers and consumers**.

If the Producer wants to send a message to a Kafka Topic, it will connect to one of the brokers in the cluster and query for the topic metadata. All Kafka brokers can answer a metadata request and hence the producer can connect to any of the brokers and query for the metadata. The metadata contains a list of all the leader partitions **and their respective host and port information**. _So, every broker in the cluster has a topic metadata with host and port to each leader partition._

After querying the metadata, now the producer has a list of all leaders. It is the producer that decides on which partition does it want to send the data and accordingly send the message to the respective broker. That means, the producer directly transmits the message to a leader. 

On receiving the message, the leader broker persists the message in the leader partition and sends back an ackowledgement. 

Similarly, when a consumer wants to read messages it connects to any of the brokers in the cluster - they all have topic metadata with the list of all leader partitions and their respective hosts and port. Containing the list of all leader partitions, the consumer chooses from which partitions it wants to read a message and sends the request to the leader. 

The producer and the consumer always interact with the leader broker.

What about the follower? Kafka Brokers also act as a follower to the follower partitions allocated to the broker. The Broker B4 acts as a follower broker for the partitions P2, P8, P1 & P7 (you can see the table above).

Followers do not serve producers and consumer requests - their only job is to copy messages from leader and stay up-to-date with all the messages. The aim of the follower is to get elected as Leader if the leader broker fails. They have a single-point agenda: stay in sync with the Leader because they may assume this role at any time.

To stay in sync with the leader, the follower connects with the leader and requests for the data. This goes on forever as an infinite loop to ensure that the followers are in sync with the leader - this is called the **Follower Thread**. 

As followers can fall behind the leader due to network congestion or broker failures, the leader has another important job that is to maintain a list of **In-Sync-Replicas (ISR)**.

This list is known as the ISR list of the partition and persisted in the Zookeeper and this list is maintained by the leader broker. The **ISR List is very critical** because all of the followers in that list are known to be in sync with the leader and are candidate to become leaders if something goes wrong with the leader broker.

The first request from a follower would ask the leader to send messages starting from the offset zero. Let's assume that the leader received request for 10 messages (0-9) and them wired them to the follower. Then, the follower will perform another request starting from offset 10 - in this case, since the follower asked for offset 10, that means a leader can safely assume that the follower has already persisted all the earlier messages.

_So, by looking at the last offset request by the follower, the leader can tell how far behind is the replica._

Now the ISR List is easy to maintain if the replica is "Not too Far" from the Leader. The ISR list is dynamic.

How do we define "not too far""? The follower will always be a little far from the leader because followers needs to ask for the message from the leader, receive the message from the network, persist in the replica and then ask for more message with another offset. This takes time. The leader gives them some minimum time as a margin to accomplish this. **The default value of "not too far" is 10 seconds**. You can increase or decrease using Kafka configurations.

If the replica has requested the most recent message in the last 10 seconds, it deserves to be in the ISR List persisted in Zookeeper.

The logical of maintaning a ISR List in Zookeeper leads to other two concepts in case the ISR List is logically empty and we don't have any broker to assume the role as leader with the leader crashes - all followers are lagging behind the leader by 11 seconds, per example.

This concepts that arise from ISR List persisted in Zookeeper are:

* Commited _versus_ Uncommited Messages

You can configure your leader to not consider a message **commited** until it is copied to all of the followers in the ISR List. If you do that, the leader may have some commited and some uncommited messages. If the message is commited, we cannot lose it until we loose all the replicas.

However, if we lose the Leader, we still miss the uncommited messages. The uncommited messages shouldn't be a worry because those can be resent by the producer. Producers can choose to receive acknowledgments of sent messages **only after the message is fully commited**. In that case, the producer waits for the acknowledgement for a timeout period and resend the messages in the absence of commit acknowledgment. So, the uncommited messages are lost at the failing leader, but the newly elected leader will receiver these again from the producer.

That's how all the messages can be protected from loss.

* Minimum Number of In-Sync Replicas: protects Kafka from a scenario where the ISR list is logically empty because the brokers failed. In this case, the messages are going to be considered commited if we only have the leader by itself - the messages were copied to all copies of the ISR List (0, for the moment). This scenario is very risky for data consistency, since we are going to lose data if the leader broker fails. That's why Kafka came out with the concept of **Minimun Number of In-Sync Replicas**.

If you would like to be sure that commited data is written to at least two replicas, you need to set the minimum number of in-sync replicas as two. There is side effect to this approach. Consider that a topic has 3 replicas and you set a minimum of In-Sync replicas as two, then you can only write to a partition in the topic if at least two of the three replicas are in sync. The leader will practically become a "read-only" partition and will throw a "Not Enough Replicas" exception. 

#### Three Node Kafka on a Local Machine - Setting Up an Environment

KAFKA_HOME environment variable should be ponting to the directory you have your uncompressed Kafka files;

Include KAFKA_HOME/bin/windows in the PATH Environment Variable;

How to configure the folder which your KAFKA will create the partitions? This /tmp folder should reside in your project directory. log.dir in server.properties is the place where the Kafka broker will store the commit logs containing your data.

You should **delete the /tmp** folder defined between sequence runs.

Important command-lines

~~~bat

##### Command Line to Start Brokers

# Broker 0

kafka-server-start.bat %KAFKA_HOME%\config\server-0.properties

# Broker 1

kafka-server-start.bat %KAFKA_HOME%\config\server-1.properties

# Broker 2

kafka-server-start.bat %KAFKA_HOME%\config\server-2.properties

# Create a topic

kafka-topics.bat --create --zookeeper localhost:2181 --topic hello-producer-topic --partitions 5 --replication-factor 3


# Start a Topic Consumer

kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic hello-producer-topic --from-beginning

# Start zookeeper

zookeeper-server-start.bat %KAFKA_HOME%\config\zookeeper.properties

~~~

#### Creating Real-Time Streams

At high-level, there are two methods of bringing data into Apache Kafka

1. Kafka Producer APIs: send the event records to the Kafka Broker also changing the old application that used to send the records only to a Database; THe event is persisted in a local database and it also goes to real-time consumption connecting to Kafka Producer API.

2. Data Integration Tools: Data Integration Tools such HVR, Talend, etc. Retrieves data from existing databases with Kafka Connect Framework. Mechanismo to bring data to Apache Kafka from a variery of sources.

#### Creating a Kafka Producer with Java

We need to create a java Properties object and add some necessary configurations in it;

Kafka producer API is highly configurable and we set the behaviour by setting this configurations - in Python we would add a config.py;

~~~java

public class HelloProducer {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {

        logger.info("Creating a Kafka Producer....");

        Properties props = new Properties();

        // Setting up 4 basic configurations for the producer to work.

        // Simple string passed to the Kafka server.
        props.put(ProducerConfig.CLIENT_ID_CONFIG, AppConfigs.applicationID);

        /*

        BOOTSTRAP SERVER CONFIG - comma separated list of HOST & PORT
        The producer will use this information for establishing the initial connecton
        to the Kafka cluster

        If you are running on a single node Kafka, you can supply an individual host/port
        information. The bootstrap configuration is used only for the initial connection -
        Once connected, the Kafka producer will automatically query for the metadata
        and discover the full list of Kafka brokers in the cluster.

        That means you do not need to supply a complete list of Kafka brokers as a bootstrap
        configuration. However, is recommended to provide 2-3 broker addresses of a multinode
        cluster.

        */

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfigs.bootstrapServers);

        /*

        A Kafka message must have a key/value structure. That means each message that we want to send
        to the Kafka server should have a key and a value.
        You can have a null key, but the message is still structured as a key/value pair.
        The second concept is about serializer. Kafka messages are sent over the network,
        So, the key and the value should be serialized into bytes before they are streamed over
        the network.

        Kafka Producer API comes with a bunch of ready to use serializer classes. 
                

        */
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // Create an instance of the KafkaProducer
        // We need to pass the properties that we created earlier to the constructor.

        KafkaProducer<Integer, String> producer = new KafkaProducer<Integer, String>(props);

        // The third step is to start sending messages to the Kafka.
        // So, we will create a loop that executes a million times 

        for (int i = 0; i < AppConfigs.numEvents; i++) {

            // The send method takes a ProducerRecord object and sends it to the Kafka cluster.
            // ProducerRecord constructor takes three arguments 
            // 1. The first argument is the topic name;
            // 2. The second argument is the message key;
            // 3. The third argument is the message value;
            producer.send(new ProducerRecord<>(AppConfigs.topicName, i, "Simple String Message -" + i));

    }
    
    logger.info("Finished sending messages.");
    producer.close();
        
~~~


#### Some Internals of Kafka Producer API

We pack the message into the ProducerRecord object that has to have at least two fiels: "Topic" & "Message Value".

Kafka Topic Name is the destination address of the message;

The message key is one of the most critical argument: it is used for many purposes such as partitioning, grouping and joining.

The ProducerRecord wraps your information with all necessary information such as topic name, key & timestamp.

Every record goes through **serialization, partitioning and then goes to a buffer** before sending it to the Kafka Broker. The serialization is necessary to send the message over the network. Without serialization of data you cannot transmit it to a remote location. That's why is necessary to configure KEY SERIALIZAITON AND VALUE SERIALIZATION in Kafka Producer configuration - Kafka does not know how to serialize your data to transmit through the network. 

You have a choice to create custom serializer, but the Kafka Producer API already provides you with some serializers like Avro, JSON, etc.

Every Kafka Producer object needs to have a Topic Name configured. Also, topics are partitioned and the Kafka Producer needs to decide to which partition it will send the message. 

There are two approaches to specify the **target partition number for the message**

1. Set partition number on the argument in the ProducerRecord object in Java - this approach is rarely used;

2. Supply a Partitioner.class to determine the partition number at Runtime;

You can specifier a **custom Partitioner** using the object properties. This is oftenly not needed because Kafka Producer API comes with a **default partitioner** which is the most commonly used paritioner.

The default partitioner takes on the two approaches:

1. Hash Key Partitioning - based on the message key; Hash the key to convert to a numeric value; Hashing algorithm on the key to the determine the partitions number for the message; This hashing ensures that all messages with the same key goes to the same partition; This hashing algorithm needs the number of partitions as an input - so we can have a problem if the number of partitions in your production environment increases and you don't update your application; You can easily over provision the number of partitions in your topic; You can easily provide 25% partitions than you need for a topic; There is no much harm in taking this approach;

2. Round Robin Partitioning - default when the message key is null. Rou robin algorithm to achieve an equal distribution among the available partitions. It means the first message goes to one partition, the second message goes to another partitions and the process goes on & repeats in a loop. This is the most used partitioning strategy in Kafka.

Every message in Kafka is automatically timestamped, even you dont explicitly specify it. For the timestamp creation in Kafka, it used two strategies

1. CreateTime: the time when the message was produced. 

2. LogAppendTime: the time when the message was received by the Kafka Broker. 

You cannot use both "CreateTime" & "LogAppendTime" strategies to create a timestamp for the messages in Kafka. Your application must decide between these two timestamping methods while **creating the topic**. 

- message.timestamp.type = 0 ----------------> CreateTime timestamp strategy defined for the topic

- message.timestamp.type = 1 -----------------> LogAppendTime timestamp stragety defined for the topic

The default value is 0 (zero) - CreateTime of the message.

The message will always have a timestamp - even the ProducerTime or a broker time that overrides the producer time when the developer targets the message.timestamp.type =1;

When we are using another tool to bring data into Kafka, it is import to set the LogAppendTime because we need to make sure that the broker is setting a timestamp to the message even if the tool used to brig data into Kafka fails;

Once serialized and assigned a partition number to it, the message goes to sit in a buffer inside the Kafka Producer API waiting to be transmitted. The ProducerObject consists of a partition-wise buffer space that holds the record that haven't yet been sent to the server. 

**Producer also runs a background I/O thread that is responsible for TURNING THESE RECORDS INTO REQUESTS and transfering them to the cluster. **

**I/O Thread in Kafka Producer API**: turn messages (that already have a timestamp and partition number) that are sitting in a buffer space into requests. How you can see, Kafka Producer API has a buffer space, thats why it is important to close the producer with producer.close();

**Why Buffering messages in Kafka Producer API?**: Designed to offer two advantages.

1. Asynchronous: That means the send method will always add the message to the buffer and return without blocking. Those records are then transmitted by the background thread. The arrangement is quite convincing as your send() method is not delayed for the network operation.

2. Network Optimization:  Buffering method also allows to provide combining messages from the same buffer and transmit them together as a single packet to achieve better thoughput.

There is a critical consideration here: if the records are posted faster that they can be transmitted to the server, then this buffer space is exhausted and your next send() method will block for few milliseconds until the buffer is freed by the I/O thread.

If the I/O takes too long to release the buffer, then your send method will throw a TimeOutException. When you are seeing this TimeOutExceptions, you may want to increase your PRODUCERMEMORY. The default producer memory is 32 MB. You can expand the total memory allocated to the buffer by setting buffer.memory producer configuration.

How to set this configurations values that we have seen so far - memory.buffer & message.timestamp.type??? The buffer.memory is a producer configuration so you can set it the properties object. 

message.timestamp.type is a topic configuration and you can set it while creating a topic in the command line tool. 

The producer I/O background thread is responsible for sending the serialized messages that are waiting in the topic partition buffer. Broker returns a successful message to acknowledge that the message was received. You can set the number of retries of the Producer API in the producer configuration - it is important to have in mind that the send() method only sends the message to the Kafka Producer API.

Flux: send() sends to the Kafka Producer API -----> Serialization ---------> Partitions Number decision based on Hash Key ALgorithm or Round Robin Algorithm ----------> background I/O thread to send messages that are sitting in the buffer to the Broker. We have some network optimization sending messages in a packet when they come from the same buffer ---------> Check if receives an acknowledgement message ---------> Retries based on the number of retries configuration -----> If successful, Broker saves the message into a log file.

#### Scaling Kafka Producers


Scaling a Kafka application is straightforward. You have hundreds of producers sending events in parallel, you may want to increase the number of brokers in your Kafka cluster.

You can have a linear scalability where you simply can add more producers and samples. You can also scale a single producer making use of **Threads in Parallel**.

A single producer thread is good enough to support the use cases where data is being produced at a reasonable pace. However, some scenarios may require parallelism at the individual level as well. You can handle such requirements using **multithreaded Kafka Producers***.

Basically, the main thread receives hundreds of thousands of messages per second and reads the data packet as they arrive. The main thread immediately handovers the data packet to a different thread for sending the data to the Kafka broker. The main thread again starts reading the next packet of data. 

The other threads of the application are responsible for uncompressing the data packet, reading individual message from the packet, serialize the data, define the topic partition number based on Hash Key algorithm or Round Robin algorithm, sit the messages on a buffer (messages with same key using Hash Key algorithm will be put in the same partition). The background I/O thread is responsible for sending the message though the network with Network Throughput Optimization. 

**Kafka Multithreaded Process is Thread-Safe**: it means your application can share the same producer object across multiple threads and send messages in parallel using the same producer instance.

_It is not recommended to create various Producer Instances in the same application to handle with large number of messages per seconds. We need to leverage the Kafka Thread-Safe nature to create multiple threads in the same Producer Instance in a application._

You can use kafka.properties to keep producer level configuration ouside of your code.

The Runnable Interface in Java allow us to execute an instance of this class as a separate Thread. 

Multi-threaded Kafka Producer:


~~~java

package guru.learningjournal.kafka.examples;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Scanner;

public class Dispatcher implements Runnable {

    private static final Logger logger = LogManager.getLogger();
    private String fileLocation;
    private String topicName;

    // In order to send the data to a KafkaBroker we need a KafkaProducer.

    private KafkaProducer<Integer, String> producer;

    // All theses things will be given to the Dispatcher by the main application thread
    // So, let's take a constructor to take these values:

    Dispatcher(KafkaProducer<> producer, String topicName, String fileLocation) {

        this.producer = producer;
        this.topicName = topicName;
        this.fileLocation = fileLocation;


    }


    // The Runnable Interface allow us to execute an instance of this class as a separate Thread.
    @Override
    public void run() {

        logger.info("Starting multi-thread Kafka Producer.");
        logger.info("Started processing " + fileLocation);
        File file = new File(fileLocation);
        int counter = 0;
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // producer.send() always receive a ProducerRecord with topicName, key, value
                producer.send(new ProducerRecord<>(topicName, null, line));
                counter++;
            }
            logger.info("Finished processing " + counter + "message from " + fileLocation);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}

~~~

Class with main method that creates the configuration of the KafkaProducer that will run in multi-threaded mode:


~~~java

package guru.learningjournal.kafka.examples;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DispatcherDemo {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {

        Properties props = new Properties();
        try {

            InputStream inputStream = new FileInputStream(AppConfigs.kafkaConfigFileLocation);
            props.load(inputStream);
            props.put(ProducerConfig.CLIENT_ID_CONFIG, AppConfigs.applicationID);
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create an instance of the producer providing the Producer Configuration with the
        // object props from the class Properties.class

        KafkaProducer<Integer, String> producer = new KafkaProducer<Integer, String>(props);
        // Ready to create dispatcher threads. Each element of the Thread[] array will hold
        // the handle for the Thread so I can join them to wait for their completion.
        // The number of threads here must be equal to the number of files;

        Thread[] dispatchers = new Thread[AppConfigs.eventFiles.length];
        logger.info("Starting dispatcher threads...");
        for (int i = 0; i < AppConfigs.eventFiles.length; i++) {

            // Inside the loop: creating a new Thread using the Dispatcher constructor.
            // All the Threads using the same producer object;

            dispatchers[i] = new Thread(new Dispatcher(producer,
                    AppConfigs.topicName,
                    AppConfigs.eventFiles[i]));
            // Start the thread.
            dispatchers[i].start();
        }

        try {

            // Loop through the Thread handles and join them together.
            // This join will allow the MAIN THREAD to wait for all the threads to complete
            for (Thread t : dispatchers) t.join();
        } catch (InterruptedException e) {
            logger.error("Main Thread interrupted");

        } finally {
            producer.close();
            logger.info("Finished dispatcher demo");
        }

    }

}


~~~

We can set the number of Producer Threads via Command-line Tool. 

#### Advanced Kafka Producer

**Idempotent Producer**

Setting enable.idempotence = true will help you that messages are not going to be lost neither you will have duplicate messages. However, you must always remember one thing: if you are sending duplicate messages from the Application Level, this configuration will not save you from have duplicates. It only saves you from duplicates due to the retries performed by I/O thread in case of failure to receive acknowledgment from the Broker. (Idempotence uses unique ID per producer and message sequency number).

**Transactional Producer**

Goes one ste further then the _idempotent Kafka Producer_ and provides the transactional guarantee, i.e, the **ability to write to several partitions atomically**. 

Transactions are dependent on idempotence.

**Retrieving messages from 2 topics with the same command-line using --whitelist**

~~~sh

kafka-console-consumer.bat --bootstrap-server localhost:9092 --from-beginning --whitelist "hello-producer-1|hello-producer-2"

~~~

#### Other Kafka Producer Concepts

1. Synchronous Send: Default Kafka Producer API is asynchronous and non-blocking - you can lose some messages. In real-use cases, the rate of lost messages is around 1-2%. 

What to do with the failure records? The send() method is asynchronous and returns immediaely withou waiting for the acknowledgment of the broker. 

First approacho: Use a get() method with the producer.send() method. The get() method is synchronous and **blocking**. So it will make the send() method wait for the acknowledgement. If it does not receive, it will throw an Exception. In case of sucess, will return the metadata.

~~~java

# This approach makes your send() method become a synchronous call.
# Allows you to take an action that you want to take in case of failure.
# This is achievale for scenarios where your message are produced in a slower pace.

metadata = producer.send(new ProducerRecord<> (AppConfigs.topicName, i, "Simple Message " + i)).get()

~~~

Second Approach: **Producer Callback**

Important approach when a lot of messages, while the first approach is suitable for messages produced in a slower pace. The asynchronous send() method is way more efficient since it allows grouping messages and optimize network roundtrips to achieve better throughput.

To handle high throughput cases, the Kafka Producer API provides you the **Producer Callback**. 


2. **Producer Callback**: high throughput and also knowing which messages failed to deliver. 

~~~java

// Callback Method -> LoggingCallBack

package guru.learningjournal.kafka.examples;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class CallbackHelloProducer {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) throws InterruptedException {

        logger.trace("Creating Kafka Producer...");
        Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG, AppConfigs.applicationID);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfigs.bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        //We want to raise an exception - So, do not retry.
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        //We want to raise an exception - So, take acknowledgement only when message is persisted to all brokers in ISR
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        /*
            Follow below steps to generate exception.
            1. Start three node cluster
            2. Create topic with --config min.insync.replicas=3
            3. Start producer application
            4. Shutdown one broker while producer is running - It will cause NotEnoughReplicasException
        */

        KafkaProducer<Integer, String> producer = new KafkaProducer<>(props);
        for (int i = 1; i <= AppConfigs.numEvents; i++) {
            Thread.sleep(1000);
            String message = "Simple Message-" + i;
            producer.send(new ProducerRecord<>(AppConfigs.topicName, i, message),
                new LoggingCallback(message));
        }
        logger.info("Finished Application - Closing Producer.");
        producer.close();
    }
}


// Here is the LoggingCallBack.class that will generate the object

package guru.learningjournal.kafka.examples;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;

public class LoggingCallback implements Callback {
    private static final Logger logger = LogManager.getLogger();
    private String message;

    public LoggingCallback(String message){
        this.message=message;
    }

    @Override
    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
        if(e !=null){
            logger.error("Error sending message string = " + message);
        }else {
            logger.info(message + " persisted with offset " + recordMetadata.offset()
                + " and timestamp on " + new Timestamp(recordMetadata.timestamp()));
        }
    }
}

~~~
       

3. Custom Partitioner

4. Avro Serializer and Schema Registry












