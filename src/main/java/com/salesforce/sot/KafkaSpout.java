package com.salesforce.sot;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: sroy
 * Date: 5/16/13
 * Time: 2:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class KafkaSpout extends BaseRichSpout {
    private SpoutOutputCollector spoutOutputCollector = null;
    private ConsumerConnector consumer;
    private KafkaStream<byte[], byte[]> stream;

    private ConsumerConfig createConsumerConfig()
    {
        Properties props = new Properties();
        props.put("zookeeper.connect", "localhost:2181");
        props.put("group.id", "sotdemo");
        props.put("zookeeper.session.timeout.ms", "400");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");

        return new ConsumerConfig(props);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("event"));
    }

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.spoutOutputCollector = spoutOutputCollector;

        String topic = "event_in";
        System.out.println("Initializing connection to Kafka...");
        consumer = Consumer.createJavaConsumerConnector(createConsumerConfig());

        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, new Integer(1));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        stream =  consumerMap.get(topic).get(0);
    }

    @Override
    public void close() {
        consumer.shutdown();
        super.close();
    }

    @Override
    public void nextTuple() {

        // Note: This is an incredibly naive way to do this. In the real world, we should consult this project.
        // https://github.com/nathanmarz/storm-contrib/blob/master/storm-kafka/src/jvm/storm/kafka/KafkaSpout.java
        // ** This is NOT compatible with the latest Kafka 0.8. We need to investigate the proper, scalable, and
        // fault tolerant way to do this for Kafka 0.8.

        String event = null;

        ConsumerIterator<byte[], byte[]> messageStreamIterator = stream.iterator();
        if (messageStreamIterator.hasNext()) {
            event = new String(messageStreamIterator.next().message());
        }

        spoutOutputCollector.emit(new Values(event));
    }
}
