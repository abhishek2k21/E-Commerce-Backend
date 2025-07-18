package com.abhi.service;

import com.abhi.dto.CartDTO;
import com.abhi.models.CartItem;

public interface CartItemService {
	
	public CartItem createItemforCart(CartDTO cartdto);
	
}
