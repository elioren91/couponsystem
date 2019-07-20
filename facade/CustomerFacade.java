package facade;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import beans.Coupon;
import beans.Customer;
import dao.CouponDBDAO;
import dao.CustomerDBDAO;
import db.ConnectionPool;
import enums.CouponType;
import facade.ClientType;
import static facade.ClientType.CUSTOMER;

/**
 * This Class imitates the possible operations for a Customer on the client side.  
 * @author Eli Oren
 *
 */
public class CustomerFacade implements Client {

	private CustomerDBDAO customerDBDAO = new CustomerDBDAO();
	private CouponDBDAO couponDBDAO = new CouponDBDAO();
	private long loggedCutomer = 0;

	public CustomerFacade() {
	}

	/**
	 * Login modifier for a customer.
	 * 
	 * @param clientID
	 * @param password
	 * @param Client_Type
	 * @throws Exception
	 */
	public CustomerFacade(long clientID, String password, ClientType Client_Type) throws Exception {
		ArrayList<Customer> check = (ArrayList<Customer>) customerDBDAO.getAllCustomers();
		int kindMassage = 0;

		for (Customer c : check) {
			kindMassage++;
			if (Client_Type.equals(ClientType.CUSTOMER) && password.equals(c.getPassword())) {
				System.out.println("Welcom " + c.getCustName());
				loggedCutomer = c.getId();
				break;
			} else if (check.size() == kindMassage) {
				System.out.println("Username or Password combination is incorrect, please try again.");
			}
		}
	}

	public long getLoggedCutomer() {
		return this.loggedCutomer;
	}

	public void setLoggedCustomer(long custID) {
		this.loggedCutomer = custID;
	}

	/**
	 * Adds a coupon of choice to the customer
	 * 
	 * @param coupon
	 * @throws Exception
	 */
	public void purchaseCoupon(Coupon coupon) throws Exception {
		Connection connection = ConnectionPool.getInstance().getConnection();
		Coupon chosenCoupon = couponDBDAO.getCoupon(coupon.getId());
		ArrayList<Coupon> customerCoupons = (ArrayList<Coupon>) customerDBDAO.getCustomerCoupons(loggedCutomer);
		int check = 0;

		ArrayList<Coupon> couponCheck = (ArrayList<Coupon>) couponDBDAO.getAllCoupons();
		int check2 = 0;
		for (Coupon c : couponCheck) {
			check2++;
			if (c.getId() == coupon.getId()) {
				break;
			} else if (check2 == couponCheck.size()) {
				System.out.println("The Coupon ID: " + coupon.getId() + " is unavailable");
				return;
			}
		}

		connection.setAutoCommit(false);
		for (Coupon c : customerCoupons) {
			// contains is not working.
			if (c.getId() != coupon.getId())
				check++;
		}
		if (check == customerCoupons.size()) {
			if (chosenCoupon.getEndDate().after(new Date(System.currentTimeMillis()))) {
				if (chosenCoupon.getAmount() > 0) {
					try {
						String updateCustomerCoupon = String.format("insert into Customer_Coupon values('%d', '%d')", 
							getLoggedCutomer(), chosenCoupon.getId());
						Statement st = connection.createStatement();
						st.executeUpdate(updateCustomerCoupon);
						chosenCoupon.setAmount(chosenCoupon.getAmount() - 1);
						couponDBDAO.updateCoupon(chosenCoupon);
						connection.commit();
					} catch (SQLException e) {
						System.out.println(e.getMessage() + " Purchase() link Coupon to Customer Failed.");
						connection.rollback();
					} finally {
						try {
							ConnectionPool.getInstance().returnConnection(connection);
						} catch (Exception e) {
							System.out.println(e.getMessage());
						}
					}
				} else {
					System.out.println("Coupon " + coupon.getTitle() + " Sold out. sorry.");
				}
			} else {
				System.out.println("The coupon you wish to purchase is already expierd");
			}
		} else {
			System.out.println("You own this Coupon already");
		}
	}

	public Collection<Coupon> getAllPurchaedCoupons() throws Exception {
		ArrayList<Coupon> coupons = (ArrayList<Coupon>) customerDBDAO.getCustomerCoupons(loggedCutomer);
		System.out.println("Purchased coupons for customer ID:" + loggedCutomer + " were retrived.");

		return coupons;
	}

	/**
	 * Retrieves customer coupon by Type.
	 * 
	 * @param CouponType
	 * @return
	 * @throws Exception
	 */
	public Collection<Coupon> getPurchasedCouponsByType(CouponType CouponType) throws Exception {
		ArrayList<Coupon> couponsByType = (ArrayList<Coupon>) customerDBDAO.getCustomerCoupons(loggedCutomer);
		Iterator<Coupon> iterator = couponsByType.iterator();
		int check = couponsByType.size();

		while (iterator.hasNext()) {
			Coupon coupon = iterator.next();
			if (!CouponType.equals(coupon.getType())) {
				iterator.remove();
				check--;
			}
		}
		System.out.println("You Purchased " + check + " Coupons type of " + CouponType);
		return couponsByType;
	}

	/**
	 * Retrieve Customer coupons by price.
	 * 
	 * @param price
	 * @return
	 * @throws Exception
	 */
	public Collection<Coupon> getPurchaedCouponsByPrice(double price) throws Exception {
		ArrayList<Coupon> coupons = (ArrayList<Coupon>) customerDBDAO.getCustomerCoupons(loggedCutomer);
		Iterator<Coupon> iterator = coupons.iterator();
		int check = coupons.size();
		while (iterator.hasNext()) {
			Coupon coupon = iterator.next();
			if (coupon.getPrice() > price) {
				iterator.remove();
				check--;
			}
		}
		System.out.println("You have " + check + " amount of coupons up to " + price + "$.");
		return coupons;
	}

	@Override
	public Client login(String name, String password, ClientType client_Type) throws Exception {
		if (customerDBDAO.login(name, password) > 0) {
			if (client_Type.equals(CUSTOMER)) {
				// All the code below is due to task and methods requirements.
				Collection<Customer> custs = customerDBDAO.getAllCustomers();
				Customer cust = null;
				CustomerFacade custF = new CustomerFacade();
				for (Customer c : custs) {
					if (c.getCustName().equalsIgnoreCase(name)) {
						cust = c;
					}
				}
				custF.setLoggedCustomer(cust.getId());
				return custF;
				// }
			} else {
				System.err.println("UserName or Password combination is incorrect.");
			}
			return null;
		}
		return null;
	}
}
