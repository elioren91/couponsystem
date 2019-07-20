package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import beans.Company;
import beans.Coupon;
import db.ConnectionPool;
import enums.CouponType;

/**
 * * this class implements CompanyDAO and creates the actual connection with the DB and USES the SQL (DML)/CRUD operations.
 * @author Eli Oren
 *
 */
public class CompanyDBDAO implements CompanyDAO {

	private ConnectionPool pool = ConnectionPool.getInstance();

	@Override
	public void createCompany(Company company) throws SQLException {
		Connection con = pool.getConnection();
		try {
			Statement st = con.createStatement();
			String create = String.format("insert into company values('%s', '%s', '%s')", company.getCompName(),
					company.getPassword(), company.getEmail());

			int result = st.executeUpdate(create);
			if (result == 0) {
				throw new SQLException("Company name already exists", company.getCompName());
			}
			System.out.println("Company created successfully");
		} catch (SQLException e) {
			System.out.println(e.getMessage() + " Could not retrieve data from DB");
		} finally {
			try {
				pool.returnConnection(con);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}

	}

	@Override
	public void removeCompany(Company company) throws SQLException {
		Connection con = pool.getConnection();
		try {
			Statement st = con.createStatement();
			int res = st.executeUpdate("delete from company where id=" + company.getId());
			if (res == 0) {
				// no company found with company.getId()
				System.out.println("no company found with ID: " + company.getId());
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage() + " Could not retrieve data from DB");
		} finally {
			try {
				pool.returnConnection(con);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}

	}

	@Override
	public void updateCompany(Company company) throws SQLException {
		Connection con = pool.getConnection();
		try {
			Statement st = con.createStatement();
			String update = String.format(
					"update company set email=('%s') where id in ('%d')" + " update company set PASSWORD=('%s')"
							+ " where id in('%d')",
					company.getEmail(), company.getId(), company.getPassword(), company.getId());

			st.executeUpdate(update);
			System.out.println("Company updated successfully");
		} catch (SQLException e) {
			System.out.println(e.getMessage() + "Unable to update the Company, Try Again! ");
		} finally {
			try {
				pool.returnConnection(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public Company getCompany(long id) throws Exception {
		Company comp = new Company();

		Connection con = pool.getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select * from company where id = " + id);
			if (rs.next()) {
				String name = rs.getString(2);
				String password = rs.getString(3);
				String email = rs.getString(4);
				comp = new Company(id, name, password, email);
				// System.out.println("[" + id + "] name:" + name + " pass:" + password + "
				// email:" + email);
			} else {
				throw new Exception("Company with an ID : " + id + " was not found.");
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage() + " Could not retrieve data from db.");
		} finally {
			pool.returnConnection(con);
		}
		return comp;
	}

	@Override
	public ArrayList<Company> getAllCompanies() throws SQLException {

		ArrayList<Company> companies = new ArrayList<Company>();

		Connection con = pool.getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select * from company");
			int result = 0;

			while (rs.next()) {
				Company comp = new Company();
				comp.setId(rs.getLong("id"));
				comp.setCompName(rs.getString("comp_name"));
				comp.setPassword(rs.getString("password"));
				comp.setEmail(rs.getString("email"));
				companies.add(comp);
				result++;
			}
			System.out.println(result + " Companies were retrieved.");

		} catch (SQLException e) {
			System.out.println(e.getMessage() + " Could not retrieve data from DB");
		} finally {
			try {
				pool.returnConnection(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return companies;
	}

	@Override
	public ArrayList<Coupon> getCompanyCoupons(long companyid) throws SQLException {
		ArrayList<Coupon> coupons = new ArrayList<>();

		Connection con = pool.getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select * from coupon join company_coupon "
					+ "on coupon.id = company_coupon.coupon_id " + "where company_coupon.comp_id = " + companyid);
			int result = 0;
			while (rs.next()) {
				long coupondId = rs.getLong("id");
				String title = rs.getString("title");
				double price = rs.getDouble("price");
				Date startDate = rs.getDate("start_date");
				Date endDate = rs.getDate("end_date");
				CouponType type = CouponType.valueOf(rs.getString("type"));
				String message = rs.getString("message");
				int amount = rs.getInt("amount");
				String image = rs.getString("image");
				coupons.add(new Coupon(coupondId, title, startDate, endDate, price, type, message, amount, image));
				result++;
			}
			System.out.println(result + " Coupons were retrieved.");

		} catch (SQLException e) {
			System.out.println(e.getMessage() + " Could not retrieve data from DB");
		} finally {
			try {
				pool.returnConnection(con);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}

		return coupons;
	}

	/**
	 * retrieves Coupons up to a (Expiration)date
	 * 
	 * @param startDate
	 * @param EndDate
	 * @param compID
	 * @return
	 * @throws Exception
	 */

	public Collection<Coupon> getAllCouponsByDate(Date EndDate, long compID) throws Exception {
		Collection<Coupon> coupons = getCompanyCoupons(compID);
		Iterator<Coupon> iterator = coupons.iterator();
		while (iterator.hasNext()) {
			Coupon coup = iterator.next();
			if (EndDate.before(coup.getEndDate()) || EndDate.equals(coup.getEndDate())) {
				iterator.remove();
			}
		}

		return coupons;
	}

	@Override
	public long login(String companyName, String password) throws SQLException {
		Connection con = pool.getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(
					"select id, password, comp_name from company " + "where comp_name like '" + companyName + "'");
			if (rs.next()) {
				String pass = rs.getString("password");
				if (password.equals(pass)) {
					System.out.println("Welcome");
					return rs.getLong("id"); // name and password match!

				} else {
					System.out.println("Password incorrect");
					return -1; // password incorrect
				}
			} else { // no such companyName
				System.out.println("NO company found");
				return -1;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

}
