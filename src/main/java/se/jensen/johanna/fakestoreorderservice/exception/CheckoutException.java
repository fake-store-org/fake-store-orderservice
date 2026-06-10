package se.jensen.johanna.fakestoreorderservice.exception;

public class CheckoutException extends DomainException {

  public CheckoutException(String message) {
    super(message, ErrorType.CHECKOUT_ERROR);
  }
}
