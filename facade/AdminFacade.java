package facade;

import java.util.ArrayList;
import java.util.Collection;

import beans.Company;
import beans.Coupon;
import beans.Customer;
import dao.CompanyDBDAO;
import dao.CouponDBDAO;
import dao.CustomerDBDAO;
import facade.ClientType;
import static facade.ClientType.ADMIN;

/**
 * This Class imitates the possible operations for a Admin on the client side.  
 * @author Eli Oren
 *
 */
public class AdminFacade implements Client {
	private CompanyDBDAO companyDBDAO = new CompanyDBDAO();
	private CustomerDBDAO customerDBDAO = new CustomerDBDAO();
	private CouponDBDAO couponDBDAO = new CouponDBDAO();

	// Default Constructor
	public AdminFacade() {
	}

	/**
	 * Login Admin modifier
	 */
	public AdminFacade(long clientID, String password, ClientType Client_Type) {
		if (Client_Type.equals(ClientType.ADMIN) && password.equals("1234")) {
			System.out.println("Welcome " + Client_Type);
		} else {
			System.out.println("Please Try Again");
		}
	}

	/**
	 * Create a new Company must not exist in the DB.
	 */
	public void creatCompany(Company comp) throws Exception {
		ArrayList<Company> allCompanies = (ArrayList<Company>) companyDBDAO.getAllCompanies();

		for (Company c : allCompanies) {
			if (c.getCompName().equalsIgnoreCase(comp.getCompName())) {
				throw new Exception(
						"Company with the name " + comp.getCompName() + " is already exist in the Data base. "
								+ "Compay name is a unique value please try adding with a diffarent name.");
			}
		}
		companyDBDAO.createCompany(comp);
		System.out.println("Company " + comp.getCompName() + " was created successfuly.");

	}

	/**
	 * removes a Company directly from the DB.
	 * 
	 * @param comp
	 * @throws Exception
	 */
	public void removeCompany(Company comp) throws Exception {
		comp.setCupons(companyDBDAO.getCompanyCoupons(comp.getId()));

		for (Coupon c : comp.getCupons()) {
			couponDBDAO.removeCoupon(c);
			couponDBDAO.removeCustomerCoupon(c.getId());
			;
			if (comp.getCupons().size() == 0) {
				companyDBDAO.removeCompany(comp);
			} else {
				throw new Exception("Not all Company coupons were deleted - Can't delete company.");
			}
		}
	}

	/**
	 * Updating Company data (Cannot update name for it's Unique.)
	 * 
	 * @param comp
	 * @throws Exception
	 */
	public void updateCompany(Company comp) throws Exception {
		Company compCheck = companyDBDAO.getCompany(comp.getId());

		if (!comp.getCompName().equalsIgnoreCase(compCheck.getCompName())) {
			throw new Exception(" Cannot Update Company NAME for it's Unique.");
		} else {
			companyDBDAO.updateCompany(comp);
		}
	}

	/**
	 * Retrieves a company from the DB.
	 * 
	 * @param id
	 * @return
	 */
	public Company getCompany(long id) {
		try {
			return companyDBDAO.getCompany(id);
		} catch (Exception e) {
			System.out.println(e.getMessage() + "getComp() in adminF Error.");
		}
		return null;
	}

	/**
	 * Retrieves all Existing companies in the DB.
	 * 
	 * @return
	 */
	public Collection<Company> getAllCompanies() {
		try {
			return companyDBDAO.getAllCompanies();
		} catch (Exception e) {
			System.out.println(e.getMessage() + " getAllComps() in AdminF Error.");
		}
		return null;
	}

	/**
	 * Create a New Customer in the DB, Cust cannot have an Existing name.
	 * 
	 * @param customer
	 * @throws Exception
	 */
	public void createCustomer(Customer customer) throws Exception {
		ArrayList<Customer> customers = (ArrayList<Customer>) customerDBDAO.getAllCustomers();

		for (Customer c : customers) {
			if (customer.getCustName().equalsIgnoreCase(c.getCustName())) {
				throw new Exception(customer.getCustName() + " " + customer.getId()
						+ " Exist in the DB please try a diffarent name.");
			}
		}
		customerDBDAO.createCustomer(customer);
	}

	public void removeCustomer(Customer customer) throws Exception {
		customer.setCupons(customerDBDAO.getCustomerCoupons(customer.getId()));
		// ArrayList<Coupon> coupons = (ArrayList<Coupon>)
		// customerDBDAO.getCustomerCoupons(customer.getId());
		// commit
		for (Coupon c : customer.getCupons()) {
			couponDBDAO.removeCoupon(c);
			if (customer.getCupons().size() == 0) {
				customerDBDAO.removeCustomer(customer);
			} else {
				throw new Exception("Not all customer coupons were deleted - Can't delete Customer.");
			}
		}

	}

	/**
	 * Updates an existing customer.
	 * 
	 * @param customer
	 * @throws Exception
	 */
	public void updateCustomer(Customer customer) throws Exception {
		Customer custCheck = customerDBDAO.getCustomer(customer.getId());

		if (!customer.getCustName().equalsIgnoreCase(custCheck.getCustName())) {
			throw new Exception("Cannot update a NAME for it's unique.");
		} else {
			customerDBDAO.updateCustomer(customer);
		}
	}

	/**
	 * Retrieves all existing customers in the DB.
	 * 
	 * @return
	 * @throws Exception
	 */
	public Collection<Customer> getAllCustomers() throws Exception {

		return customerDBDAO.getAllCustomers();
	}

	/**
	 * Retrieves a customer of choice.
	 * 
	 * @param custID
	 * @return
	 * @throws Exception
	 */
	public Customer getCustomer(long custID) throws Exception {

		return customerDBDAO.getCustomer(custID);
	}

	@Override
	public Client login(String name, String password, ClientType Client_Type) {
		if (Client_Type.equals(ADMIN) && password.equals("1234")) {
			System.out.println("Welcome " + Client_Type);
			return new AdminFacade();
		} else {
			System.out.println("Username or password is incorrect please try again.");
		}
		return null;
	}
}
