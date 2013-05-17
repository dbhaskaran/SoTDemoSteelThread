package com.salesforce.sot;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sroy
 * Date: 5/16/13
 * Time: 2:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class FilterBolt extends BaseRichBolt {

    private OutputCollector collector;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        String event = tuple.getString(0);

        // Filter out all events < 2500
        System.out.println("[FILTER BOLT] : Processing event => " + event);
        int eventId = 0;
        try {
            eventId = Integer.parseInt(event.split(":")[1]);
        } catch (Exception e) {
            System.out.println("[FILTER BOLT] : Malformed event => " + event);
        }
        if (eventId > 2500) {
            collector.emit(tuple, new Values(event));
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("event"));
    }
}
