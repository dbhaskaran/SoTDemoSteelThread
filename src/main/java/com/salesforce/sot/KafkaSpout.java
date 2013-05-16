package com.salesforce.sot;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

import java.util.Map;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: sroy
 * Date: 5/16/13
 * Time: 2:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class KafkaSpout extends BaseRichSpout {
    private SpoutOutputCollector spoutOutputCollector = null;

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("event"));
    }

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.spoutOutputCollector = spoutOutputCollector;
    }

    @Override
    public void nextTuple() {
        Utils.sleep(1000);
        final Random rand = new Random();
        String event = "EventId:" + rand.nextInt(5000);
        spoutOutputCollector.emit(new Values(event));
    }
}
