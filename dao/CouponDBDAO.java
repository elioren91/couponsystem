package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import beans.Coupon;
import db.ConnectionPool;
import enums.CouponType;

/**
 * * this class implements CouponDAO and creates the actual connection with the DB and USES the SQL (DML)/CRUD operations.
 * @author Eli Oren
 *
 */
public class CouponDBDAO implements CouponDAO {

	private ConnectionPool pool = ConnectionPool.getInstance();
	
	public CouponDBDAO() {
	}
	
	/**
	 * Creates a coupon in the DB/ fills in the columns in Coupon table
	 * 
	 * @throws Exception
	 */
	@Override
	public void createCoupon(Coupon coupon) throws Exception {
		Connection con = ConnectionPool.getInstance().getConnection();
		try {
			
			String creatSQL = "INSERT INTO Coupon"
					+ "(Title,Start_Date,End_Date,Amount,Type,Message,Price,Image)"
					+ "VALUES (?,?,?,?,?,?,?,?)";

			PreparedStatement ps = con.prepareStatement(creatSQL);
			ps.setString(1, coupon.getTitle());
			ps.setDate(2, coupon.getStartDate());
			ps.setDate(3, coupon.getEndDate());
			ps.setInt(4, coupon.getAmount());
			ps.setString(5, coupon.getType().toString());
			ps.setString(6, coupon.getMessage());
			ps.setDouble(7, coupon.getPrice());
			ps.setString(8, coupon.getImage());
			
			int result = ps.executeUpdate();
			
			try {
			if (result == 0) {
				throw new SQLException("the coupon was not created!");
			} else {
				String check = String.format("SELECT * FROM Coupon WHERE Title = ? ");
				PreparedStatement ps2 = con.prepareStatement(check);
				ps2.setString(1, coupon.getTitle());
				ResultSet rs = ps2.executeQuery();
				if(rs.next()) {
				System.out.println("Coupon " + coupon.getTitle() + " ID: " + rs.getLong("ID") +  " was created succesfully");
				}
			}
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				ConnectionPool.getInstance().returnConnection(con);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}

	}

	/**
	 * Removes a Coupon directly from the DB.
	 * 
	 * @throws Exception
	 */
	@Override
	public void removeCoupon(Coupon coupon) throws SQLException {
		Connection con = pool.getConnection();
		try {
			Statement st = con.createStatement();
			int res = st.executeUpdate("delete from coupon where id=" + coupon.getId());
			if (res == 0) {
				// no coupon found with company.getId()
				System.out.println("no coupon found with ID: " + coupon.getId());

			} else
				System.out.println("Coupon deleted!");
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
	public void updateCoupon(Coupon c) throws Exception {
		Connection connection = ConnectionPool.getInstance().getConnection();
		try {
			String UpdateCoupon = "UPDATE Coupon SET Title=?, Start_Date=?,"
					+ "End_Date=?, Amount=?,Type=?, Message=?, Price=?, Image=? WHERE ID=?";

			PreparedStatement ps = connection.prepareStatement(UpdateCoupon);
			ps.setString(1, c.getTitle());
			ps.setDate(2, c.getStartDate());
			ps.setDate(3, c.getEndDate());
			ps.setInt(4, c.getAmount());
			ps.setString(5, c.getType().toString());
			ps.setString(6, c.getMessage());
			ps.setDouble(7, c.getPrice());
			ps.setString(8, c.getImage());
			ps.setLong(9, c.getId());

			int affectedRaws = ps.executeUpdate();

			System.out.println("Coupon " + c.getTitle() + " ID: " + c.getId() + " was updated successfully");
			System.out.println(affectedRaws + " raw was affected");

		} catch (SQLException e) {
			System.out.println(e.getMessage() + " Could not retrieve data from DB");
		} finally {
			try {
				ConnectionPool.getInstance().returnConnection(connection);
			} catch (Exception e) {
				System.out.println(e.getMessage() + " Could not close connection");
			}
		}

	}

	@Override
	public Coupon getCoupon(long id) throws SQLException {
		Coupon coupon = new Coupon();
		Connection connection = ConnectionPool.getInstance().getConnection();

		try {
			String getCouponFromDB = String.format("select * from coupon where id = %d", id);

			Statement st = connection.createStatement();
			ResultSet result = st.executeQuery(getCouponFromDB);
			if (result.next()) {
				coupon.setId(result.getLong(1));
				coupon.setTitle(result.getString(2));
				coupon.setStartDate(result.getDate(3));
				coupon.setEndDate(result.getDate(4));
				coupon.setAmount(result.getInt(5));
				coupon.setType(CouponType.valueOf(result.getString(6)));
				coupon.setMessage(result.getString(7));
				coupon.setPrice(result.getDouble(8));
				coupon.setImage(result.getString(9));
			} else {
				throw new SQLException("Coupon was not found.");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage() + " Could not retrieve data from DB");
		} finally {
			try {
				ConnectionPool.getInstance().returnConnection(connection);
			} catch (Exception e) {
				System.out.println(e.getMessage() + " Final in getCoup() in CoupDBDAO.");
			}
		}

		return coupon;
	}

	/**
	 * Retrieves all Existing Coupons in the DB.
	 */
	@Override
	public Collection<Coupon> getAllCoupons() throws SQLException {
		ArrayList<Coupon> coupons = new ArrayList<Coupon>();
		Connection con = ConnectionPool.getInstance().getConnection();
		try {
			String getAllCoupons = "SELECT * FROM Coupon";

			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(getAllCoupons);
			int result = 0;

			while (rs.next()) {
				Coupon coupon = getCoupon(rs.getLong("ID"));
				coupons.add(coupon);
				result++;
			}
			System.out.println(result + " Coupons retrieved from the database!");
		} catch (SQLException e) {
			System.out.println(e.getMessage() + " Could not retrieve data from DB");
		} finally {
			try {
				ConnectionPool.getInstance().returnConnection(con);
			} catch (Exception e) {
				System.out.println(e.getMessage() + " Final in getAllCoups() CoupDBDAO.");
			}
		}

		return coupons;
	}

	/**
	 * Retrieves Coupons by type of choice
	 * 
	 * @throws Exception
	 */
	public Collection<Coupon> getCouponByType(CouponType type) throws Exception {
		ArrayList<Coupon> coupons = new ArrayList<Coupon>();
		Connection connection = ConnectionPool.getInstance().getConnection();
		try {
			String getCouponsByType = "SELECT ID FROM Coupon WHERE Type=('%s')";

			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(getCouponsByType);
			int resultCount = 0;

			while (rs.next()) {
				Coupon coupon = getCoupon(rs.getLong("ID"));
				if (coupon.getId() != 0) {
					coupons.add(this.getCoupon(coupon.getId()));
					resultCount++;
				}
			}
			System.out.println(resultCount + " number of " + type.toString() + " Type coupons were retrived.");

			if (resultCount == 0) {
				System.out.println(type.toString() + " coupons were not found.");
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage() + " Could not retrieve data from DB");
		} finally {
			try {
				ConnectionPool.getInstance().returnConnection(connection);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}

		return coupons;
	}

	/**
	 * @param fromPrice
	 * @param maxPrice
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Coupon> getCouponsByPrice(double fromPrice,double maxPrice) throws Exception{
		ArrayList<Coupon> coupons = new ArrayList<Coupon>();
		Connection connection = ConnectionPool.getInstance().getConnection();
		
		try {
			connection = ConnectionPool.getInstance().getConnection();
			String getCouponsByPrice = "SELECT * FROM Coupon WHERE Price > ? and Price < ?";
			PreparedStatement ps = connection.prepareStatement(getCouponsByPrice);
			ps.setDouble(1, fromPrice);
			ps.setDouble(2, maxPrice);
			ResultSet rs = ps.executeQuery();
			
			int resultCount = 0;

			if (rs != null) {
				while (rs.next()) {
					Coupon coupon = getCoupon(rs.getLong("ID"));
					if (coupon.getId() != 0) {
						coupons.add(this.getCoupon(coupon.getId()));
						resultCount++;
						System.out.println("Coupon name: " + "\"" + coupon.getTitle() + "\" " + "ID: " + coupon.getId());
					}
				}
				System.out.println(resultCount + " coupons were retrived.");
			} else {
				throw new SQLException("Error occured");
			}
			if (resultCount == 0) {
				System.out.println("Coupons within the price range of : " + fromPrice + " - " + maxPrice + " were not found.");
			}
			
		}catch(SQLException e) {
			System.out.println(e.getMessage() + " Retriving by price Failed");
		}finally {
			ConnectionPool.getInstance().returnConnection(connection);
		}
		
		return coupons;
	}

	/**
	 * not in use ====>>>>> instead there is a cascade command in the DB.
	 * 
	 * @param couponID
	 * @throws Exception
	 */
	public void removeCustomerCoupon(long couponID) throws Exception {
		Connection connection = ConnectionPool.getInstance().getConnection();
		try {
			String removeCustomerCoupon = "DELETE FROM Customer_Coupon WHERE Coupon_ID=?";
			Statement st = connection.createStatement();
			st.execute(removeCustomerCoupon);
		} catch (SQLException e) {
			System.out.println(e.getMessage() + " Could not retrieve data from DB");
		} finally {
			ConnectionPool.getInstance().returnConnection(connection);
		}

	}

	/**
	 * Helps with the daily task creating this method here makes life easier.
	 * @throws Exception
	 */
	public void removeExpiredCoupon() throws Exception {
		CouponDBDAO couponTask = new CouponDBDAO();
		ArrayList<Coupon> taskList = (ArrayList<Coupon>) couponTask.getAllCoupons();
		
		for (Coupon c : taskList) {
			if(c.getEndDate().before(new Date(System.currentTimeMillis()))) {
				couponTask.removeCustomerCoupon(c.getId());
				couponTask.removeCoupon(c);
			}
		}
	}

}
