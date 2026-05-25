package se.jensen.johanna.fakestoreorderservice.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

public record ReservationRequest(
    @NotNull(message = "Please add items to cart.")
    Set<CartItemRequest> cartItemRequests,
    @NotNull
    UUID orderId
) {

}
