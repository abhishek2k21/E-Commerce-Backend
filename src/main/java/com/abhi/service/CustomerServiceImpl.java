package com.abhi.service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.abhi.exception.CustomerException;
import com.abhi.exception.CustomerNotFoundException;
import com.abhi.exception.LoginException;
import com.abhi.models.Address;
import com.abhi.models.Cart;
import com.abhi.models.CreditCard;
import com.abhi.models.Customer;
import com.abhi.dto.CustomerDTO;
import com.abhi.dto.CustomerUpdateDTO;
import com.abhi.models.Order;
import com.abhi.dto.SessionDTO;
import com.abhi.models.UserSession;
import com.abhi.repository.CustomerDao;
import com.abhi.repository.SessionDao;

@Service
public class CustomerServiceImpl implements CustomerService{

	@Autowired
	private CustomerDao customerDao;

	@Autowired
	private LoginLogoutService loginService;

	@Autowired
	private SessionDao sessionDao;

    @Autowired
    private PasswordEncoder passwordEncoder;


	// Method to add a new customer

	@Override
	public Customer addCustomer(Customer customer) {

		customer.setCreatedOn(LocalDateTime.now());

		// Use findByMobileNoOrEmailId to check for existing customers
		Optional<Customer> existing = customerDao.findByMobileNoOrEmailId(customer.getMobileNo(), customer.getEmailId());

		if(existing.isPresent())
			throw new CustomerException("Customer already exists with this mobile number or email ID. Please try to login.");

		customer.setPassword(passwordEncoder.encode(customer.getPassword()));

		// Create and associate the cart with the customer before saving
		Cart c = new Cart();
		c.setCustomer(customer);
		customer.setCustomerCart(c);
		customer.setOrders(new ArrayList<Order>());

		// Save the customer, which will cascade persist the cart and orders
		return customerDao.save(customer);
	}



	// Method to get a customer by mobile number

	@Override
	public Customer getLoggedInCustomerDetails(String token){

		if(token.contains("customer") == false) {
			throw new LoginException("Invalid session token for customer");
		}

		loginService.checkTokenStatus(token);

		UserSession user = sessionDao.findByToken(token).get();

		Optional<Customer> opt = customerDao.findById(user.getUserId());

		if(opt.isEmpty())
			throw new CustomerNotFoundException("Customer does not exist");

		Customer existingCustomer = opt.get();

		return existingCustomer;
	}




	// Method to get all customers - only seller or admin can get all customers - check validity of seller token

	@Override
	public List<Customer> getAllCustomers(String token) throws CustomerNotFoundException {

		// update to seller

		if(token.contains("seller") == false) {
			throw new LoginException("Invalid session token.");
		}

		loginService.checkTokenStatus(token);

		List<Customer> customers = customerDao.findAll();

		if(customers.size() == 0)
			throw new CustomerNotFoundException("No record exists");

		return customers;
	}


	// Method to update entire customer details - either mobile number or email id should be correct

	@Override
	public Customer updateCustomer(CustomerUpdateDTO customer, String token) throws CustomerNotFoundException {


		if(token.contains("customer") == false) {
			throw new LoginException("Invalid session token for customer");
		}

		loginService.checkTokenStatus(token);

		Optional<Customer> opt = customerDao.findByMobileNo(customer.getMobileNo());

		Optional<Customer> res = customerDao.findByEmailId(customer.getEmailId());

		if(opt.isEmpty() && res.isEmpty())
			throw new CustomerNotFoundException("Customer does not exist with given mobile no or email-id");

		Customer existingCustomer = null;

		if(opt.isPresent())
			existingCustomer = opt.get();
		else
			existingCustomer = res.get();

		UserSession user = sessionDao.findByToken(token).get();

		if(existingCustomer.getCustomerId() == user.getUserId()) {

			if(customer.getFirstName() != null) {
				existingCustomer.setFirstName(customer.getFirstName());
			}

			if(customer.getLastName() != null) {
				existingCustomer.setLastName(customer.getLastName());
			}

			if(customer.getEmailId() != null) {
				existingCustomer.setEmailId(customer.getEmailId());
			}

			if(customer.getMobileNo() != null) {
				existingCustomer.setMobileNo(customer.getMobileNo());
			}

			if(customer.getPassword() != null) {
				existingCustomer.setPassword(passwordEncoder.encode(customer.getPassword()));
			}

			if(customer.getAddresses() != null) {
				for(Address updatedAddress : customer.getAddresses()) {
					boolean found = false;
					for(int i = 0; i < existingCustomer.getAddresses().size(); i++) {
						Address existingAddress = existingCustomer.getAddresses().get(i);
						if(existingAddress.getAddressId() != null && existingAddress.getAddressId().equals(updatedAddress.getAddressId())) {
							existingCustomer.getAddresses().set(i, updatedAddress);
							found = true;
							break;
						}
					}
					if(!found) {
						existingCustomer.getAddresses().add(updatedAddress);
					}
				}
			}

			customerDao.save(existingCustomer);
			return existingCustomer;

		}
		else {
			throw new CustomerException("Error in updating. Verification failed.");
		}


	}


	// Method to update customer mobile number - details updated for current logged in user

	@Override
	public Customer updateCustomerMobileNoOrEmailId(CustomerUpdateDTO customerUpdateDTO, String token) throws CustomerNotFoundException {

		if(token.contains("customer") == false) {
			throw new LoginException("Invalid session token for customer");
		}

		loginService.checkTokenStatus(token);

		UserSession user = sessionDao.findByToken(token).get();

		Optional<Customer> opt = customerDao.findById(user.getUserId());

		if(opt.isEmpty())
			throw new CustomerNotFoundException("Customer does not exist");

		Customer existingCustomer = opt.get();

		if(customerUpdateDTO.getEmailId() != null) {
			existingCustomer.setEmailId(customerUpdateDTO.getEmailId());
		}


		existingCustomer.setMobileNo(customerUpdateDTO.getMobileNo());

		customerDao.save(existingCustomer);

		return existingCustomer;

	}

	// Method to update password - based on current token

	@Override
	public SessionDTO updateCustomerPassword(CustomerDTO customerDTO, String token) {


		if(token.contains("customer") == false) {
			throw new LoginException("Invalid session token for customer");
		}


		loginService.checkTokenStatus(token);

		UserSession user = sessionDao.findByToken(token).get();

		Optional<Customer> opt = customerDao.findById(user.getUserId());

		if(opt.isEmpty())
			throw new CustomerNotFoundException("Customer does not exist");

		Customer existingCustomer = opt.get();


		if(customerDTO.getMobileId().equals(existingCustomer.getMobileNo()) == false) {
			throw new CustomerException("Verification error. Mobile number does not match");
		}

		existingCustomer.setPassword(passwordEncoder.encode(customerDTO.getPassword()));

		customerDao.save(existingCustomer);

		SessionDTO session = new SessionDTO();

		session.setToken(token);

		loginService.logoutCustomer(session);

		session.setMessage("Updated password and logged out. Login again with new password");

		return session;

	}


	// Method to add/update Address


	@Override
	public Customer updateAddress(Address address, String type, String token) throws CustomerException {
		if(token.contains("customer") == false) {
			throw new LoginException("Invalid session token for customer");
		}

		loginService.checkTokenStatus(token);

		UserSession user = sessionDao.findByToken(token).get();

		Optional<Customer> opt = customerDao.findById(user.getUserId());

		if(opt.isEmpty())
			throw new CustomerNotFoundException("Customer does not exist");

		Customer existingCustomer = opt.get();

		boolean found = false;
		for(int i = 0; i < existingCustomer.getAddresses().size(); i++) {
			if(existingCustomer.getAddresses().get(i).getAddressId().equals(address.getAddressId())) {
				existingCustomer.getAddresses().set(i, address);
				found = true;
				break;
			}
		}
		if(!found) {
			existingCustomer.getAddresses().add(address);
		}

		return customerDao.save(existingCustomer);

	}


	// Method to update Credit card

	@Override
	public Customer updateCreditCardDetails(String token, CreditCard card) throws CustomerException{

		if(token.contains("customer") == false) {
			throw new LoginException("Invalid session token for customer");
		}

		loginService.checkTokenStatus(token);

		UserSession user = sessionDao.findByToken(token).get();

		Optional<Customer> opt = customerDao.findById(user.getUserId());

		if(opt.isEmpty())
			throw new CustomerNotFoundException("Customer does not exist");

		Customer existingCustomer = opt.get();

		existingCustomer.setCreditCard(card);

		return customerDao.save(existingCustomer);
	}



	// Method to delete a customer by mobile id

	@Override
	public SessionDTO deleteCustomer(CustomerDTO customerDTO, String token) throws CustomerNotFoundException {

		if(token.contains("customer") == false) {
			throw new LoginException("Invalid session token for customer");
		}

		loginService.checkTokenStatus(token);

		UserSession user = sessionDao.findByToken(token).get();

		Optional<Customer> opt = customerDao.findById(user.getUserId());

		if(opt.isEmpty())
			throw new CustomerNotFoundException("Customer does not exist");

		Customer existingCustomer = opt.get();

		SessionDTO session = new SessionDTO();

		session.setMessage("");

		session.setToken(token);

		if(existingCustomer.getMobileNo().equals(customerDTO.getMobileId())
				&& passwordEncoder.matches(customerDTO.getPassword(), existingCustomer.getPassword())) {
			customerDao.delete(existingCustomer);

			loginService.logoutCustomer(session);

			session.setMessage("Deleted account and logged out successfully");

			return session;
		}
		else {
			throw new CustomerException("Verification error in deleting account. Please re-check details");
		}

	}



	@Override
	public Customer deleteAddress(String type, String token) throws CustomerException, CustomerNotFoundException {

		if(token.contains("customer") == false) {
			throw new LoginException("Invalid session token for customer");
		}

		loginService.checkTokenStatus(token);

		UserSession user = sessionDao.findByToken(token).get();

		Optional<Customer> opt = customerDao.findById(user.getUserId());

		if(opt.isEmpty())
			throw new CustomerNotFoundException("Customer does not exist");

		Customer existingCustomer = opt.get();

		Address addressToRemove = null;
		for(Address addr : existingCustomer.getAddresses()) {
			if(addr.getAddressType() != null && addr.getAddressType().equalsIgnoreCase(type)) {
				addressToRemove = addr;
				break;
			}
		}

		if(addressToRemove != null) {
			existingCustomer.getAddresses().remove(addressToRemove);
		} else {
			throw new CustomerException("Address type does not exist");
		}

		return customerDao.save(existingCustomer);
	}



	@Override
	public List<Order> getCustomerOrders(String token) throws CustomerException {

		if(token.contains("customer") == false) {
			throw new LoginException("Invalid session token for customer");
		}

		loginService.checkTokenStatus(token);

		UserSession user = sessionDao.findByToken(token).get();

		Optional<Customer> opt = customerDao.findById(user.getUserId());

		if(opt.isEmpty())
			throw new CustomerNotFoundException("Customer does not exist");

		Customer existingCustomer = opt.get();

		List<Order> myOrders = existingCustomer.getOrders();

		if(myOrders.size() == 0)
			throw new CustomerException("No orders found");

		return myOrders;
	}

}


