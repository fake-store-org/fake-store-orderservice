package se.jensen.johanna.fakestoreorderservice.dto;

import java.util.UUID;

public record ConfirmReservationEventDTO(
    UUID orderId
) {

}
