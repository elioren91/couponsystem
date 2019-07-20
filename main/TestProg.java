package main;

import java.sql.Date;
import java.util.ArrayList;

import dao.CompanyDBDAO;
import dao.CouponDBDAO;
import dao.CustomerDBDAO;
import facade.ClientType;
import enums.CouponType;
import facade.AdminFacade;
import facade.CompanyFacade;
import facade.CustomerFacade;
import facade.Client;
import beans.Company;
import beans.Coupon;
import beans.Customer;

public class TestProg {

	public static void main(String[] args) throws Exception {
//================================================CouponSystem================================================================================
		//CouponSystem cs = CouponSystem.getInstance(); //====> Daily Task (Works), ConnectionPool(Works as required).
		//AdminFacade adminF = (AdminFacade)
		//CompanyFacade compF = (CompanyFacade)
		//CustomerFacade custF = (CustomerFacade) 
		//cs.login("admin", "1234", ClientType.ADMIN);//Admin=>(Checked),Company=>(Checked),Customer=>(Checked).
		//System.out.println();
		//cs.shutdown();//(done)
//================================================AdminFacade================================================================================
		//Company comp = new Company(7,"TestFcompany2", "abcdefg", "AdmnidComp@Gmail.com");
		//Customer customer = new Customer(100008,"AdminCustomer", "123123");
		//AdminFacade admin = new AdminFacade();
		//admin.creatCompany(comp);//(done)
		//admin.removeCompany(comp);//(done)====>>>>> need a check when there are coupons linked(CHECKED).
		//admin.updateCompany(comp);//(done)
		//System.out.println(admin.getCompany(100015));//(done)
		/*ArrayList<Company> comps = (ArrayList<Company>) admin.getAllCompanies();
		for(Company c: comps) {
			System.out.println(c);
		}*///(done)
		//admin.createCustomer(customer);//(done)
		//admin.removeCustomer(customer);//(done)
		//admin.updateCustomer(customer);//(done)
		//System.out.println(admin.getCustomer(100005));//(done)
		/*ArrayList<Customer> custs = (ArrayList<Customer>) admin.getAllCustomers();
		for(Customer c:custs) {
			System.out.println(c);
		}*///(done)
		//admin.login("admin", "1234", ClientType.ADMIN);//(done)
//================================================CutomerFacade================================================================================
		//Coupon coupon = new Coupon(17,"Coupon For Fun", "das", "dddd", new Date(System.currentTimeMillis()),
			// Date.valueOf("2019-10-20"),20, 50, CouponType.CAMPING);
		//CustomerFacade custF1 = new CustomerFacade();
		//CustomerFacade loggedCust = (CustomerFacade) custF1.login("eli", "123", ClientType.CUSTOMER);
		//loggedCust.purchaseCoupon(coupon);//(done!!!!!)====>>>>> CustLogin needed.
		/*ArrayList<Coupon> coups = (ArrayList<Coupon>) loggedCust.getAllPurchaedCoupons();
		for(Coupon c:coups) {
			System.out.println(c);
		}*///(done)
		/*ArrayList<Coupon> coups = (ArrayList<Coupon>) loggedCust.getPurchasedCouponsByType(CouponType.CAMPING);
		for(Coupon c:coups) {
			System.out.println(c);
		}*///(done)
		/*ArrayList<Coupon> coups = (ArrayList<Coupon>) loggedCust.getPurchaedCouponsByPrice(45);
		for(Coupon c:coups) {
			System.out.println(c);
		}*///(done)	
		//custF.login("bob", "123456", ClientType.CUSTOMER);//(done)
//================================================CompanyFacade================================================================================
		//Coupon coupon = new Coupon(31,"Test Electricity Coupon", new Date(System.currentTimeMillis()), Date.valueOf("2019-10-20"),
	  //	221.5, CouponType.ELECTRICITY, "Test Message", 150, "Test Image");
		//CompanyFacade CF = new CompanyFacade();
		//CompanyFacade loggedComp = (CompanyFacade) CF.login("Temy", "123e", ClientType.COMPANY);
		//loggedComp.createCoupon(coupon);//(done)//====>>>>> yet don't link the coupon with the company because no company was logged in.
		//loggedComp.removeCoupon(coupon);//(done)//====>>>>> a company has to be logged in in-order for this method to work.
		//loggedComp.updateCoupon(coupon);//(done)
		/* ====>>>>> CompLogin needed + you receive a kind massage only if there is a 
		mismatch of compID and compName although the method only updates Price&EndDate as required.*/
		//System.out.println(loggedComp.getCoupon(31));//(done)
		/*ArrayList<Coupon> coups = (ArrayList<Coupon>) loggedComp.getAllCoupons();
		for(Coupon c:coups) {
			System.out.println(c);
		}*///(done)//====>>>>> CompLogin needed
		/*ArrayList<Coupon> coups = (ArrayList<Coupon>) loggedComp.getCouponsByType(CouponType.ELECTRICITY);
		for(Coupon c : coups) {
			System.out.println(c);
		}*///(done)
		/*ArrayList<Coupon> coups = (ArrayList<Coupon>) loggedComp.getCouponsPrice(45);
		for(Coupon c : coups) {
			System.out.println(c);
		}*///(done)
		/*ArrayList<Coupon> coups = (ArrayList<Coupon>) loggedComp.getCouponByDate(new Date(System.currentTimeMillis()));
		for(Coupon c : coups) {
			System.out.println(c);
		}*///(done)//====>>>>> Retrieves coupons up to an expired date and shows them in case the coupon still active.
		
//================================================CouponDBDAO================================================================================
		//Coupon coupon = new Coupon(16,"Coupon For Fun", "das", "dddd", new Date(System.currentTimeMillis()),
			//	 Date.valueOf("2019-10-20"),20, 50, CouponType.CAMPING);
		
		//Coupon coup = new Coupon(23, "test3", "asd123", "123asddas", new Date(System.currentTimeMillis()),
		//		Date.valueOf("2021-12-22"), 10, 12.32,CouponType.SPORTS);
		//CouponDBDAO couponDAO = new CouponDBDAO();
		//couponDAO.createCoupon(coup);//(done)
		//couponDAO.removeCoupon(coup);//(done)
		//couponDAO.updateCoupon(coup);//(done)//====>>>>> still updates all parameters.
		//System.out.println(couponDAO.getCoupon(coup.getId()));//(done)
		/*ArrayList<Coupon> coups = (ArrayList<Coupon>) couponDAO.getAllCoupons();
		for(Coupon c : coups) {
			System.out.println(c);
		}*///(done)
		/*ArrayList<Coupon> coupsT = (ArrayList<Coupon>) couponDAO.getCouponByType(CouponType.Electricity);
		for(Coupon c : coupsT) {
			System.out.println(c);
		}*///(done)
		//couponDAO.getCouponsByPrice(1, 111);//====>>>>> was created later due to a facade function request.
		//couponDAO.getAllCouponsByDate(start, expiration);//====>>>>> was created later due to a facade function request.
//================================================CustomerDBDAO================================================================================		
		//Customer c = new Customer(14,"eli", "123456");
		//CustomerDBDAO custDAO = new CustomerDBDAO();
		//custDAO.createCustomer(c);//(done)
		//custDAO.removeCustomer(c);//(done)
		//custDAO.updateCustomer(c);//(done)//====>>>>> still updates all parameters in the DB.
		//System.out.println(custDAO.getCustomer(13));//(done)
		/*ArrayList<Customer> custs = (ArrayList<Customer>) custDAO.getAllCustomers();
		for(Customer cust : custs ) {
			System.out.println(cust);
		}*///(done)
		/*ArrayList<Coupon> custCoupons = (ArrayList<Coupon>) custDAO.getCustomerCoupons(c.getId());
		for(Coupon coup: custCoupons) {
			System.out.println(coup);
		}*///(done)
		//custDAO.login("Bob", "123456");//(done)
//================================================CompanyDBDAO================================================================================
		//Company c = new Company(17,"Temy", "123e", "eea@gm.om");
		//CompanyDBDAO compDAO = new CompanyDBDAO();
		//compDAO.createCompany(c);//(done)
		//compDAO.removeCompany(c);//(done)
		//compDAO.updateCompany(c);//(done)//====>>>>> still updates all parameters in the DB.
		//System.out.println(compDAO.getCompany(c.getId()));//(done)
		//compDAO.getAllCouponsByDate(Date.valueOf("2018-05-05"), Date.valueOf("2033-05-05"), c.getId());
		/*ArrayList<Company> comps = (ArrayList<Company>) compDAO.getAllCompanies();
		for(Company comp : comps) {
			System.out.println(comp);
		}*///(done)
		//compDAO.getCompanyCoupons(c.getId());//******************
		//compDAO.login("ca", "123e21312");//(done)
	}

}
