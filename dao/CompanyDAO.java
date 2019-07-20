package dao;

import java.sql.SQLException;
import java.util.Collection;
import beans.Company;
import beans.Coupon;

public interface CompanyDAO {
	
	
	public void createCompany(Company company) throws SQLException, Exception;

	public void removeCompany(Company company) throws SQLException, Exception;

	public void updateCompany(Company company) throws SQLException, Exception;

	public Company getCompany(long id) throws Exception;

	public Collection<Company> getAllCompanies() throws SQLException, Exception;

	public Collection<Coupon> getCompanyCoupons(long id) throws SQLException, Exception;
	
	public long login(String compName, String password) throws SQLException, Exception;
	
}
