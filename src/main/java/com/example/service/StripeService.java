package com.example.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

@Service
public class StripeService {

	@Value("${stripe.api.key}")
	private String apiKey;
	
	@Value("${app.base.url}")
	private String baseUrl;
	
	public void init()
	{
		Stripe.apiKey = apiKey;
	}
	
	public Session createCheckoutSession(BigDecimal price, String productName, Long orderId) throws Exception
	{
		Long priceInCents = price.multiply(new BigDecimal("100")).longValue();
		SessionCreateParams params = SessionCreateParams.builder()
			.setMode(SessionCreateParams.Mode.PAYMENT)
			.setSuccessUrl(baseUrl +"/checkout/success?orderId=" + orderId + "&session_id = {CHECKOUT_SESSION_ID}")
			.setCancelUrl(baseUrl + "/checkout/cancel?orderId=" + orderId )
			.addLineItem(
					SessionCreateParams.LineItem.builder()
					.setQuantity(1L)
					.setPriceData(
							SessionCreateParams.LineItem.PriceData.builder()
							.setCurrency("ron")
							.setUnitAmount(priceInCents)
							.setProductData(
									SessionCreateParams.LineItem.PriceData.ProductData.builder()
									.setName(productName)
									.build())
							.build())
					.build())
			.putMetadata("orderId",orderId.toString())
			.build();
		
		return Session.create(params);			
	}
}
