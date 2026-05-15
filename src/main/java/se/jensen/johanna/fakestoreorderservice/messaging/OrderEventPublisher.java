package se.jensen.johanna.fakestoreorderservice.messaging;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import se.jensen.johanna.fakestoreorderservice.dto.ConfirmReservationEventDTO;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventPublisher {

  private final SqsTemplate sqsTemplate;
  @Value("${app.queues.confirm-reservation}")
  private String confirmReservationQueueUrl;


  public void publishConfirmReservationEvent(UUID reservationId) {
    log.info("Publishing confirm reservation event for reservation id: {}...", reservationId);
    ConfirmReservationEventDTO eventDTO = new ConfirmReservationEventDTO(reservationId);

    sqsTemplate.send(to -> to.queue(confirmReservationQueueUrl).payload(eventDTO));
    log.info("Confirm reservation event published for reservation id: {}", reservationId);
  }

}
