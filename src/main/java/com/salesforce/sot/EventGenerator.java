package com.salesforce.sot;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;
import java.util.Random;

public class EventGenerator
{
    private final kafka.javaapi.producer.Producer<Integer, String> producer;
    private final String topic;
    private final Properties props = new Properties();

    public EventGenerator(String topic)
    {
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("metadata.broker.list", "localhost:9092");
        // Use random partitioner. Don't need the key type. Just set it to Integer.
        // The message is of type String.
        producer = new kafka.javaapi.producer.Producer<Integer, String>(new ProducerConfig(props));
        this.topic = topic;
    }

    public static void main(String[] args) {
        EventGenerator pct = new EventGenerator("event_in");

        for (int i=0; i < 100; i++) {
            Random random = new Random();
            String event = "Event:" + random.nextInt(5000);
            System.out.println("[EVENT GENERATOR] Sending event ==> " + event);
            pct.producer.send(new KeyedMessage<Integer, String>(pct.topic, event));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

}