package com.abhi.service;

import java.util.List;

import com.abhi.controller.ProductNotFound;
import com.abhi.exception.CartItemNotFound;
import com.abhi.models.Cart;
import com.abhi.models.CartDTO;
import com.abhi.models.CartItem;




public interface CartService {
	
	public Cart addProductToCart(CartDTO cart, String token) throws CartItemNotFound;
	public Cart getCartProduct(String token);
	public Cart removeProductFromCart(CartDTO cartDto,String token) throws ProductNotFound;
//	public Cart changeQuantity(Product product,Customer customer,Integer quantity);
	
	public Cart clearCart(String token);
	
}
