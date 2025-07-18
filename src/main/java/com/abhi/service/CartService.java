package com.abhi.service;



import com.abhi.exception.CartItemNotFound;
import com.abhi.exception.ProductNotFoundException;
import com.abhi.models.Cart;
import com.abhi.dto.CartDTO;





public interface CartService {
	
	public Cart addProductToCart(CartDTO cart, String token) throws CartItemNotFound;
	public Cart getCartProduct(String token);
	public Cart removeProductFromCart(CartDTO cartDto,String token) throws ProductNotFoundException;
//	public Cart changeQuantity(Product product,Customer customer,Integer quantity);
	
	public Cart clearCart(String token);
	
}




