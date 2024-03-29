## Purpose

A very simple starter project intended for learning / demo purposes.

Demonstrates the following flow:

- Send events to a Kafka queue for persistent buffering
- Drain events from Kafka into Storm
- Execute a Storm topology with 2 bolts:
   - First one filters out every event with a number greater than 2500.
   - Second bolt takes the filtered results and prints them to the screen.

## Prerequisites

- Java 7
- Pre-built Kafka jars, installed in local maven repository

### Building and installing Kafka

For the purposes of this simple project, in the interest of avoiding any external dependencies, I did the following:

- Build Kafka according to the instructions [here](https://cwiki.apache.org/KAFKA/kafka-08-quick-start.html)
- Then execute the following commands:

<pre>
$ cd core/target/scala-2.8.0
$ mvn install:install-file -Dfile=./kafka_2.8.0-0.8.0-SNAPSHOT.jar -DgroupId=org.apache -DartifactId=kafka -Dversion=0.8 -Dpackaging=jar
$ mvn install:install-file -Dfile=./kafka-assembly-0.8.0-SNAPSHOT-deps.jar -DgroupId=org.apache -DartifactId=kafka-static-deps -Dversion=0.8 -Dpackaging=jar
</pre>

In the real world, this type of dependency will of course be available via a proper repository.

## Build

<pre>
$ mvn clean install
</pre>

**Note** : Yes, install. The way the project is currently configured, this is required in order to run it easily.

## Run Demo

### Start messaging infrastructure externally

Follow the directions in the bottom half of the [Kafka quick start](https://cwiki.apache.org/KAFKA/kafka-08-quick-start.html). This will start up Kafka with 3 brokers and 1 topic. 

Name the topic **event_in** instead of **mytopic**

### Run the storm system
<pre>
$ bash target/bin/runstorm
</pre>

### Start simulated event generator
<pre>
$ bash target/bin/eventsim
</pre>
