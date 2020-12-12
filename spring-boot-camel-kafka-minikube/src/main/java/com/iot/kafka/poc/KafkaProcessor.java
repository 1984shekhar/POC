package com.iot.kafka.poc;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.kafka.clients.producer.RecordMetadata;

public class KafkaProcessor implements Processor{
	@Override
	public void process(Exchange exchange) throws Exception {
		
		List<RecordMetadata> recordMetaData = (List<RecordMetadata>) exchange.getIn().getHeader(KafkaConstants.KAFKA_RECORDMETA);
		
		for(RecordMetadata meta: recordMetaData) {
			
			System.out.println("offset is:" +meta.offset());
			System.out.println("partition is:" +meta.partition());
			System.out.println("value size is:" +meta.serializedValueSize());
		}
		
	}

}
