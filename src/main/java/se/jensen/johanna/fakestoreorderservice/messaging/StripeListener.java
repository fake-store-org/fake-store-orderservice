package se.jensen.johanna.fakestoreorderservice.messaging;

import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import se.jensen.johanna.fakestoreorderservice.dto.StripeEventDTO;
import se.jensen.johanna.fakestoreorderservice.repository.OrderRepository;
import se.jensen.johanna.fakestoreorderservice.service.OrderService;

@Slf4j
@Component
@RequiredArgsConstructor
public class StripeListener {

  private final OrderService orderService;
  private final OrderRepository orderRepository;


  /**
   * Listens to successfully paid stripe-events.
   *
   * @param stripeEvent
   */
  @SqsListener("${app.queues.order-paid-events}")
  public void handlePaymentStatusPaid(StripeEventDTO stripeEvent) {
    log.info("Received stripe event: {}", stripeEvent);
    if (stripeEvent == null) {
      log.warn("Stripe event is null");
      return;
    }
    if (!orderRepository.existsByStripeSessionId(
        stripeEvent.detail().data().stripeObject().sessionId())) {
      log.warn("Order not found for stripe session id: {}",
          stripeEvent.detail().data().stripeObject().sessionId());
      return;
    }
    orderService.handlePaidOrder(stripeEvent);

  }

}
