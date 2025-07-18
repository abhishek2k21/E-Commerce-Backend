package com.abhi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abhi.models.CartItem;

public interface CartItemDao extends JpaRepository<CartItem, Integer>{

}
