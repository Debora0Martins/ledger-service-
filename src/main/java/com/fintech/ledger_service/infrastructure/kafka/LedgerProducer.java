 package com.fintech.ledger_service.infrastructure.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LedgerProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "ledger-events";

    public void send(String key, String message) {
        long start = System.currentTimeMillis();

        log.info("Enviando evento para Kafka | topic={} | key={} | msg={}",
                TOPIC, key, message);

        kafkaTemplate.send(TOPIC, key, message)
                .whenComplete((result, ex) -> {

                    long duration = System.currentTimeMillis() - start;

                    if (ex != null) {
                        log.error(" Erro ao enviar evento para o Kafka | key={} | time={}ms | error={}",
                                key, duration, ex.getMessage(), ex);
                        return;
                    }

                    log.info(" Evento enviado com sucesso | topic={} | partition={} | offset={} | time={}ms",
                            result.getRecordMetadata().topic(),
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset(),
                            duration
                    );
                });
    }
}


