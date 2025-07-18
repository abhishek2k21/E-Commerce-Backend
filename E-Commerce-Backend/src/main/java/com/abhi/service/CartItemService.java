package com.abhi.service;

import com.abhi.models.CartDTO;
import com.abhi.models.CartItem;

public interface CartItemService {
	
	public CartItem createItemforCart(CartDTO cartdto);
	
}
