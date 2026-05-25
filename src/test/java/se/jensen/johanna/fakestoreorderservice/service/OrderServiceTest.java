package se.jensen.johanna.fakestoreorderservice.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.client.RestTemplate;
import se.jensen.johanna.fakestoreorderservice.dto.AddressRequest;
import se.jensen.johanna.fakestoreorderservice.dto.CartItemRequest;
import se.jensen.johanna.fakestoreorderservice.dto.CheckoutResponse;
import se.jensen.johanna.fakestoreorderservice.dto.OrderRequest;
import se.jensen.johanna.fakestoreorderservice.dto.ProductBatchResponse;
import se.jensen.johanna.fakestoreorderservice.dto.ProductDTO;
import se.jensen.johanna.fakestoreorderservice.mapper.AddressMapper;
import se.jensen.johanna.fakestoreorderservice.mapper.OrderItemMapper;
import se.jensen.johanna.fakestoreorderservice.mapper.OrderMapper;
import se.jensen.johanna.fakestoreorderservice.model.Order;
import se.jensen.johanna.fakestoreorderservice.model.OrderItem;
import se.jensen.johanna.fakestoreorderservice.model.ShippingAddress;
import se.jensen.johanna.fakestoreorderservice.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

  @InjectMocks
  private OrderService orderService;
  @Mock
  private OrderRepository orderRepository;
  @Mock
  private OrderMapper orderMapper;
  @Mock
  private PaymentService paymentService;
  @Mock
  private OrderItemMapper orderItemMapper;
  @Mock
  private AddressMapper addressMapper;
  @Mock
  private RestTemplate restTemplate;

  private Jwt jwt;
  private Set<CartItemRequest> itemRequests;
  private List<OrderItem> orderItems;


  @BeforeEach
  void setUp() {
    UUID userId = UUID.randomUUID();
    jwt = mock(Jwt.class);

    orderItems = new java.util.ArrayList<>();
    OrderItem orderItem = OrderItem.builder().orderItemId(UUID.randomUUID())
        .order(mock(Order.class)).pricePerItem(
            BigDecimal.valueOf(100)).quantity(1).title("title").build();
    OrderItem orderItem2 = OrderItem.builder().orderItemId(UUID.randomUUID())
        .order(mock(Order.class)).pricePerItem(
            BigDecimal.valueOf(100)).quantity(1).title("title2").build();
    orderItems.add(orderItem);
    orderItems.add(orderItem2);
    itemRequests = new HashSet<>();
    itemRequests.add(new CartItemRequest(UUID.randomUUID(), 1));
    itemRequests.add(new CartItemRequest(UUID.randomUUID(), 2));
  }


  @Test
  void putOrder_ShouldSuccessfullyPutOrderAndSave2() {
    UUID sharedProductId = UUID.randomUUID();
    UUID buyerId = UUID.randomUUID();

    when(jwt.getSubject()).thenReturn(buyerId.toString());
    when(jwt.getClaimAsString("email")).thenReturn("test@test.com");

    CartItemRequest cartItem = new CartItemRequest(sharedProductId, 2);
    Set<CartItemRequest> itemRequests = Set.of(cartItem);

    AddressRequest addressRequest = new AddressRequest("firstname", "lastname", null, "streetname1",
        null, "54345", "city", "country");
    OrderRequest request = new OrderRequest(itemRequests, addressRequest);

    ProductDTO productDto = new ProductDTO(sharedProductId, "Titel", 100, "d", "s");
    ProductBatchResponse fakeResponse = new ProductBatchResponse(List.of(productDto));

    OrderItem realItem = OrderItem.builder()
        .pricePerItem(new BigDecimal("100.00"))
        .quantity(2)
        .title("Titel")
        .build();

    when(addressMapper.toShippingAddress(any())).thenReturn(mock(ShippingAddress.class));

    when(restTemplate.postForObject(anyString(), any(HttpEntity.class),
        eq(ProductBatchResponse.class)))
        .thenReturn(fakeResponse);

    when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Void.class)))
        .thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

    when(orderItemMapper.toOrderItem(any(ProductDTO.class), anyInt())).thenReturn(realItem);

    when(paymentService.createCheckoutSession(any(Order.class), anyString()))
        .thenReturn(new CheckoutResponse("http://url", "123"));

    assertDoesNotThrow(() -> orderService.putOrder(jwt, request));
    verify(orderRepository, times(1)).save(any(Order.class));
  }


  @Test
  void reserveOrderItems() {
  }

  @Test
  void getOrderItems() {
  }
}