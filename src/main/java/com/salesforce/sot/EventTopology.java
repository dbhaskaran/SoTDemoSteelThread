package com.salesforce.sot;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

/**
 * Created with IntelliJ IDEA.
 * User: sroy
 * Date: 5/16/13
 * Time: 2:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventTopology {

    public static void main(String[] args) {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("kafka", new KafkaSpout());
        builder.setBolt("filter", new FilterBolt(), 2)
                .shuffleGrouping("kafka");
        builder.setBolt("printer", new PrinterBolt(), 2)
                .shuffleGrouping("filter");

        Config conf = new Config();
        conf.setDebug(true);

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("test", conf, builder.createTopology());
        Utils.sleep(30000);
        cluster.killTopology("test");
        cluster.shutdown();
    }
}
