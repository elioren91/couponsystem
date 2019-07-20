package beans;

import java.util.Collection;

public class Company {
	private long id;
	public String compName, password, email;
	private Collection<Coupon> cupons;
	
	public Company(String compName, String password, String email) {
		this.compName = compName;
		this.password = password;
		this.email = email;
	}
	
	public Company(long id, String compName, String password, String email) {
		this.id = id;
		this.compName = compName;
		this.password = password;
		this.email = email;
	}

	public Company() {
	}

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
		// TODO Auto-generated method stub
		return "[Company:" + "\"" + compName + "\"" + " ID: " + id + " Password:" + password + " Email:" + email + "]";
	}

}
