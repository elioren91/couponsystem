package beans;

import java.util.ArrayList;
import java.util.Collection;

public class Customer {
	
	private long id;
	private String custName, password;
	private Collection<Coupon> cupons;
	
	public Customer(long id, String custName, String password) {
		this.id = id;
		this.custName = custName;
		this.password = password;
		this.cupons = new ArrayList<Coupon>();
	}
	
	public Customer(String custName, String password) {
		this.custName = custName;
		this.password = password;
		this.cupons = new ArrayList<Coupon>();
	}
	
	public Customer(long id) {
		this.id = id;
	}
	public Customer() {
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public Collection<Coupon> getCupons() {
		return cupons;
	}

	public void setCupons(Collection<Coupon> cupons) {
		this.cupons = cupons;
	}
	
	@Override
	public String toString() {
		return "Customer: " + "\"" + custName + "\"" + " ID: " + id + " Password:" + password;
	}

}
