package se.jensen.johanna.fakestoreorderservice.dto;

import java.util.List;

public record ProductBatchResponse(
    List<ProductDTO> products
) {

}
