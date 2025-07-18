package com.abhi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.abhi.models.Address;

@Repository
public interface AddressDao extends JpaRepository<Address, Integer>{

}
