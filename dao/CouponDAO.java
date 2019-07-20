package dao;

import java.sql.SQLException;
import java.util.Collection;

import beans.Coupon;
import enums.CouponType;

public interface CouponDAO {

	public void createCoupon(Coupon coupon) throws Exception;

	public void removeCoupon(Coupon coupon) throws Exception;

	public void updateCoupon(Coupon coupon) throws Exception;

	public Coupon getCoupon(long id) throws SQLException;

	public Collection<Coupon> getAllCoupons() throws Exception;

	public Collection<Coupon> getCouponByType(CouponType couponType) throws Exception;
	
}
