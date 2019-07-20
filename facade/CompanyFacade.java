package facade;

import static facade.ClientType.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import dao.CompanyDBDAO;
import dao.CouponDBDAO;
import facade.ClientType;
import enums.CouponType;
import beans.Company;
import beans.Coupon;
import db.ConnectionPool;

/**
 * This Class imitates the possible operations for a Company on the client side.  
 * @author Eli Oren
 *
 */
public class CompanyFacade implements Client {

	private CompanyDBDAO companyDBDAO = new CompanyDBDAO();
	private CouponDBDAO couponDBDAO = new CouponDBDAO();
	private long loggedCompany = 0;

	public CompanyFacade() {
	}

	/**
	 * Login modifier.
	 * 
	 * @param clientID
	 * @param password
	 * @param Client_Type
	 * @throws Exception
	 */
	public CompanyFacade(long clientID, String password, ClientType Client_Type) throws Exception {
		ArrayList<Company> check = (ArrayList<Company>) companyDBDAO.getAllCompanies();
		int ErrMassage = 0;

		for (Company c : check) {
			ErrMassage++;
			if (Client_Type.equals(COMPANY) && password.equals(c.getPassword())) {
				System.out.println("Welcom " + c.getCompName());
				loggedCompany = c.getId();
				break;
			} else if (check.size() == ErrMassage) {
				System.out.println("Username or Password combination is incorrect, please try again.");
			}
		}
	}

	public long getLoggedCompany() {
		return loggedCompany;
	}

	public void setLoggedCompany(long loggedCompany) {
		this.loggedCompany = loggedCompany;
	}

	/**
	 * Create a coupon for a specific company.
	 * 
	 * @param coupon
	 * @throws Exception
	 */
	public void createCoupon(Coupon coupon) throws Exception {
		ArrayList<Coupon> coupons = (ArrayList<Coupon>) companyDBDAO.getCompanyCoupons(loggedCompany);
		Connection connection = ConnectionPool.getInstance().getConnection();
		long coupID = 0;

		for (Coupon c : coupons) {
			if (coupon.getTitle().equalsIgnoreCase(c.getTitle())) {
				throw new Exception("A coupon with title " + coupon.getTitle() + " Exists please try another title.");
			}
		}
		connection.setAutoCommit(false);
		couponDBDAO.createCoupon(coupon);
		// add a method to coordinate with the coupon & company table (Done).
		try {
			String getNewCoupID = "SELECT ID FROM Coupon WHERE Title = ?";
			PreparedStatement ps = connection.prepareStatement(getNewCoupID);
			ps.setString(1, coupon.getTitle());
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				coupID = rs.getLong("ID");
			}
			if (coupID != 0) {
				String linkCompanyCoupon = "INSERT INTO Company_Coupon VALUES(?,?)";
				PreparedStatement ps2 = connection.prepareStatement(linkCompanyCoupon);
				ps2.setLong(1, loggedCompany);
				ps2.setLong(2, coupID);
				int result = ps2.executeUpdate();
				System.out.println(result + " raw was affected");
				connection.commit();
			} else {
				throw new Exception("Coupon was not created due to miss coordination between DB tables. ");
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage() + " Failed to link Company with coupon");
		} finally {
			ConnectionPool.getInstance().returnConnection(connection);
		}

	}

	/**
	 * Removes a coupon from the DB only if the company logged has created it.
	 * 
	 * @param coupon
	 * @throws Exception
	 */
	public void removeCoupon(Coupon coupon) throws Exception {
		ArrayList<Coupon> coupons = (ArrayList<Coupon>) companyDBDAO.getCompanyCoupons(loggedCompany);
		int ErrMassage = 0;
		for (Coupon c : coupons) {
			ErrMassage++;
			if (coupon.getId() == c.getId()) {
				couponDBDAO.removeCoupon(coupon);
				break;
			} else if (ErrMassage == coupons.size()) {
				System.out.println("Sorry you don't have the coupon ID " + coupon.getId());
			}
		}

	}

	public void updateCoupon(Coupon coupon) throws Exception {
		ArrayList<Coupon> coupons = (ArrayList<Coupon>) companyDBDAO.getCompanyCoupons(loggedCompany);
		int ErrMassage = 0;
		for (Coupon c : coupons) {
			ErrMassage++;
			if (coupon.getId() == c.getId() && coupon.getTitle().equalsIgnoreCase(c.getTitle())) {
				Coupon coup = coupon;
				coup.setEndDate(coupon.getEndDate());
				coup.setPrice(coupon.getPrice());
				couponDBDAO.updateCoupon(coup);
				break;
			} else if (ErrMassage == coupons.size()) {
				System.out.println("You don't have a coupon with an ID: " + coupon.getId());
			} else if (coupon.getId() == c.getId() && !coupon.getTitle().equalsIgnoreCase(c.getTitle())) {
				throw new Exception("You can't update the Title for it's unique.");
			}
		}
	}

	/**
	 * Retrieves a chosen Coupon
	 * 
	 * @param couponID
	 * @return
	 * @throws Exception
	 */
	public Coupon getCoupon(long couponID) throws Exception {
		ArrayList<Coupon> coupons = (ArrayList<Coupon>) companyDBDAO.getCompanyCoupons(loggedCompany);
		int ErrMassage = 0;
		for (Coupon c : coupons) {
			ErrMassage++;
			if (couponID == c.getId()) {
				return couponDBDAO.getCoupon(couponID);
			} else if (ErrMassage == coupons.size()) {
				System.out.println("Can't find coupon with the ID: " + couponID);
			}
		}
		return null;
	}

	/**
	 * Retrieves All coupons of the logged company.
	 * 
	 * @return
	 * @throws Exception
	 */
	public Collection<Coupon> getAllCoupons() throws Exception {
		return companyDBDAO.getCompanyCoupons(loggedCompany);
	}

	/**
	 * Retrieve company coupons sorted by type.
	 * 
	 * @param couponType
	 * @return
	 * @throws Exception
	 */
	public Collection<Coupon> getCouponsByType(CouponType couponType) throws Exception {
		ArrayList<Coupon> coupons = (ArrayList<Coupon>) companyDBDAO.getCompanyCoupons(loggedCompany);
		Iterator<Coupon> iterator = coupons.iterator();
		while (iterator.hasNext()) {
			Coupon coupon = iterator.next();
			if (!coupon.getType().equals(couponType)) {
				iterator.remove();
			}
		}
		return coupons;

	}

	/**
	 * Retrieves Company coupons by price.
	 * 
	 * @param price
	 * @return
	 * @throws Exception
	 */
	public Collection<Coupon> getCouponsPrice(double price) throws Exception {
		ArrayList<Coupon> coupons = (ArrayList<Coupon>) companyDBDAO.getCompanyCoupons(loggedCompany);
		// it is possible to do with for loop ... for practice sake Iterator.
		Iterator<Coupon> iterator = coupons.iterator();
		while (iterator.hasNext()) {
			Coupon coupon = iterator.next();
			if ((coupon.getPrice() > price)) {
				iterator.remove();
			}
		}
		if (coupons.size() == 0) {
			System.out.println("No coupons were found up to the price of: " + price);
		}
		return coupons;

	}

	/**
	 * Retrieves Company coupons by type.
	 * 
	 * @param StartDate
	 * @param EndDate
	 * @return
	 * @throws Exception
	 */
	public Collection<Coupon> getCouponByDate(Date EndDate) throws Exception {
		return companyDBDAO.getAllCouponsByDate(EndDate, loggedCompany);
	}

	/**
	 * Login modifier
	 * @throws Exception 
	 */
	@Override
	public Client login(String name, String password, ClientType Client_Type) throws Exception {
		if (companyDBDAO.login(name, password) > 0) {
			if (Client_Type.equals(COMPANY)) {
				// All the code below is due to task and methods requirements.
				Collection<Company> comps = companyDBDAO.getAllCompanies();
				Company comp = null;
				CompanyFacade compF = new CompanyFacade();
				for (Company c : comps) {
					if (c.getCompName().equalsIgnoreCase(name)) {
						comp = c;
					}
				}
				compF.setLoggedCompany(comp.getId());
				return compF;
			}
		} else {
			System.err.println("UserName or Password combination is incorrect.");
		}
		return null;
	}
}