package dao;

import java.util.Collection;

import beans.Coupon;
import beans.Customer;

public interface CustomerDAO {
	
	public void createCustomer(Customer customer) throws Exception;

	public void removeCustomer(Customer customer) throws Exception;

	public void updateCustomer(Customer customer) throws Exception;

	public Customer getCustomer(long id) throws Exception;

	public Collection<Customer> getAllCustomers() throws  Exception;

	public Collection<Coupon> getCustomerCoupons(long custId) throws Exception;

	public long login(String custName, String password) throws Exception;

}
