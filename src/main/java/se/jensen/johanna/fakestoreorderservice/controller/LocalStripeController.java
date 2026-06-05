package se.jensen.johanna.fakestoreorderservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.jensen.johanna.fakestoreorderservice.dto.StripeEventDTO;
import se.jensen.johanna.fakestoreorderservice.dto.StripeEventDTO.Data;
import se.jensen.johanna.fakestoreorderservice.dto.StripeEventDTO.Detail;
import se.jensen.johanna.fakestoreorderservice.dto.StripeEventDTO.Metadata;
import se.jensen.johanna.fakestoreorderservice.dto.StripeEventDTO.StripeObject;
import se.jensen.johanna.fakestoreorderservice.service.OrderService;

@Profile("local")
@RestController
@RequestMapping("/api/local")
@RequiredArgsConstructor
public class LocalStripeController {

  private final OrderService orderService;

  /**
   * Endpoint for local development.
   */
  @PostMapping("/confirm-payment/{sessionId}")
  public ResponseEntity<Void> confirmPayment(@PathVariable String sessionId) {
    StripeEventDTO eventDTO = new StripeEventDTO(
        new Detail(new Data(new StripeObject(sessionId, "paid", null, null, new Metadata(null)))));
    orderService.handlePaidOrder(eventDTO);
    return ResponseEntity.ok().build();
  }

}
