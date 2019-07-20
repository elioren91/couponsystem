package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import beans.Coupon;
import beans.Customer;
import db.ConnectionPool;

/**
 * * this class implements CustomerDAO and creates the actual connection with the DB and USES the SQL (DML)/CRUD operations.
 * @author Eli Oren
 *
 */
public class CustomerDBDAO implements CustomerDAO {

	private ConnectionPool pool = ConnectionPool.getInstance();

	public CustomerDBDAO() {
		// constructor
	}
	/**
	 * Creates a new Customer directly in the DB.
	 */
	@Override
	public void createCustomer(Customer customer) throws Exception {
		Connection con = pool.getConnection();
		try {
			
			Statement st = con.createStatement();
			String create = String.format("insert into customer values('%s', '%s')", customer.getCustName(),
					customer.getPassword());
			st.executeUpdate(create);
			System.out.println("Customer added successfully");
		} catch (SQLException e) {
			System.out.println(e.getMessage() + "Unable to add A customer, Try Again! ");
		} finally {
			pool.returnConnection(con);
		}
	}

	/**
	 * Removes a customer directly from the DB.
	 */
	@Override
	public void removeCustomer(Customer customer) throws Exception {
		Connection con = pool.getConnection();
		
		Customer custCheck = getCustomer(customer.getId());
		if(custCheck.getCustName() == null)
			throw new Exception("No Such customer Exists.");
		
		if(custCheck.getCustName().equalsIgnoreCase(customer.getCustName()) && customer.getId() == custCheck.getId()) {
			try {
					Statement st = con.createStatement();
					String remove = String.format("delete from customer where id in('%d')", 
						customer.getId());
					st.executeUpdate(remove);
					System.out.println("Customer removed successfully");
			} catch (SQLException e) {
				System.out.println(e.getMessage() + " Could not retrieve data from DB");
			}finally {
				try {
					ConnectionPool.getInstance().returnConnection(con);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}

	/**
	 * Updates a customer directly from the DB.
	 */
	@Override
	public void updateCustomer(Customer customer) throws Exception{
		Connection con = pool.getConnection();
		Customer custCheck = getCustomer(customer.getId());
		if(custCheck.getCustName()== null)
			return;
			//throws Exception no customer found in DB

		try {
			Statement st = con.createStatement();
			String update = String.format("update customer set CUST_NAME=('%s') where id in ('%d')" +
			" update customer set PASSWORD=('%s')"
					+ " where id in('%d')",
					customer.getCustName(),
					customer.getId(),
					customer.getPassword(),
					customer.getId());
			
			st.executeUpdate(update);
			System.out.println("Customer updated successfully");
		} catch (SQLException e) {
			System.out.println(e.getMessage() + "Unable to update A customer, Try Again! ");
		} finally {
			pool.returnConnection(con);
		}

	}

	/**
	 * Retrieves a customer directly from the DB.
	 */
	@Override
	public Customer getCustomer(long id) throws Exception {
		Connection con = pool.getConnection();
		Customer customer = new Customer();
		try {
			String getCustomer = String.format("select * from customer where id in('%d')", id);

			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(getCustomer);

			if (rs.next()) {
				customer.setId(id);
				customer.setCustName(rs.getString("Cust_Name"));
				customer.setPassword(rs.getString("Password"));
				System.out
						.println("Customer " + customer.getCustName() + " ID: " + customer.getId() + " was retrived.");
			} else {
				throw new Exception("Customer with ID: " + id + " was not found in the DB.");
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage() + " Could not retrieve data from DB.");
		} finally {
			try {
				ConnectionPool.getInstance().returnConnection(con);
			} catch (Exception e) {
				System.out.println(e.getMessage() + " Could not close connection");
			}
		}
		return customer;
	}

	/**
	 * Retrieves all Customers from the DB.
	 * @throws Exception 
	 */
	@Override
	public Collection<Customer> getAllCustomers() throws Exception {
		ArrayList<Customer> customers = new ArrayList<Customer>();
		Connection con = pool.getConnection();
		
		try {
			String getAllCustomers = "select * from customer";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(getAllCustomers);
			int result = 0;

				while (rs.next()) {
					Customer cust = new Customer();
					cust.setId(rs.getLong("ID"));
					cust.setCustName(rs.getString("Cust_Name"));
					cust.setPassword(rs.getString("Password"));
					customers.add(cust);
					result++;
				}
				System.out.println(result + " Customers were retrieved.");
                
		} catch (SQLException e) {
			System.out.println(e.getMessage() + " Could not retrieve data from DB.");
		} finally {
			try {
				ConnectionPool.getInstance().returnConnection(con);
			} catch (Exception e) {
				System.out.println(e.getMessage() + " Could not close the connection to the DB");
			}
		}
		return customers;
	}

	@Override
	public Collection<Coupon> getCustomerCoupons(long custId) throws Exception {
		ArrayList<Coupon> coupons = new ArrayList<Coupon>();
		Connection con = pool.getConnection();
		
		try {
			con = ConnectionPool.getInstance().getConnection();
			String getCustomerCoupons = String.format("select * from customer_coupon where cust_id in('%d')"
					, custId);
		
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(getCustomerCoupons);
			int result = 0;
			
				while (rs.next()) {
					CouponDBDAO c = new CouponDBDAO();
					Coupon coupon = c.getCoupon(rs.getLong("Coupon_ID"));
					coupons.add(coupon);
					result++;
				}
				System.out.println(result + " Coupons were retrieved.");	

		} catch (SQLException e) {
			System.out.println(e.getMessage() + " Could not retrieve data from DB.");
		} finally {
			try {
				ConnectionPool.getInstance().returnConnection(con);
			} catch (Exception e) {
				System.out.println(e.getMessage() + " Could not close the connection to the DB.");
			}
		}
		return coupons;
	}

	@Override
	public long login(String custName, String password)throws Exception {
		Connection con = pool.getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select id, password, cust_name from customer "
					+ "where cust_name like '" + custName + "'" );
			if(rs.next()) {
				String pass = rs.getString("password");
				if(password.equals(pass)) {
					System.out.println("Welcome " + custName);
					return rs.getLong("id"); // name and password match!
				}else {
					System.out.println("password incorrect");
					return -1; // password incorrect
				}
			}else { // no such companyName
				System.out.println("no customer found");
				return -1;
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage() + " Not authorized");
		} finally {
			try {
				ConnectionPool.getInstance().returnConnection(con);
			} catch (Exception e) {
				System.out.println(e.getMessage() + " Could not close the connection to the DB.");
			}
		}
		return -1;
	}

}
