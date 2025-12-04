package com.fintech.ledger_service.infrastructure.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LedgerConsumer {

    private static final String MAIN_TOPIC = "ledger-events";
    private static final String DLQ_TOPIC  = "ledger-events.DLQ";

    /**
     * Consumer principal com ACK manual
     */
    @KafkaListener(topics = MAIN_TOPIC, groupId = "ledger-group")
    public void consume(ConsumerRecord<String, String> record, Acknowledgment ack) {

        String message = record.value();

        log.info("📥 [KAFKA] Mensagem recebida | topic={} | partition={} | offset={} | key={} | value={}",
                record.topic(), record.partition(), record.offset(), record.key(), message);

        try {
            // Simulação de processamento
            log.info("⚙️  Processando mensagem...");

            // SUCESSO → confirma manualmente
            ack.acknowledge();
            log.info("✅ ACK enviado para o Kafka");

        } catch (Exception e) {
            log.error("❌ Erro ao processar mensagem. Enviando para DLQ...", e);
            throw e; // Spring encaminha para a DLQ (por retry ou por error handler)
        }
    }

    /**
     * Dead Letter Queue (DLQ)
     */
    @KafkaListener(topics = DLQ_TOPIC, groupId = "ledger-group")
    public void consumeDLQ(ConsumerRecord<String, String> record) {
        log.error("🟥 [DLQ] Mensagem caiu na DLQ | key={} | value={} | offset={}",
                record.key(), record.value(), record.offset());
    }
}
