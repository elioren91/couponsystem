package beans;

import java.sql.Date;

import enums.CouponType;

public class Coupon {
	
	private long id;
	private String title, message, image;
	private Date startDate, endDate;
	private int amount;
	private double price;
	private CouponType type;
	
	public Coupon(long id, String title, String message, String image, Date startDate, Date endDate, int amount,
			double price, CouponType type) {
		this.id = id;
		this.title = title;
		this.message = message;
		this.image = image;
		this.startDate = startDate;
		this.endDate = endDate;
		this.amount = amount;
		this.price = price;
		this.type = type;
	}
	
	public Coupon(String title, String message, String image, Date startDate, Date endDate, int amount,
			double price, CouponType type) {
		this.title = title;
		this.message = message;
		this.image = image;
		this.startDate = startDate;
		this.endDate = endDate;
		this.amount = amount;
		this.price = price;
		this.type = type;
	}
	
	public Coupon(long id,String title, Date startDate, Date endDate, CouponType type) {
		this.id = id;
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.type = type;
	}
	
	public Coupon(long id, String title, Date startDate, Date endDate, double price,
			CouponType type, String message, int amount, String image) {
		this.id = id;
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.price = price;
		this.type = type;
		this.message = message;
		this.amount = amount;
		this.image = image;
	}
	
	public Coupon(String title, Date startDate, Date endDate, double price,
			CouponType type, String message, int amount, String image) {
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.price = price;
		this.type = type;
		this.message = message;
		this.amount = amount;
		this.image = image;
	}

	public Coupon() {
		// TODO Auto-generated constructor stub
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public CouponType getType() {
		return type;
	}

	public void setType(CouponType type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "Coupon name: " + "\"" + getTitle() + "\"" + " ID: " + getId() + " Amount: " + getAmount() + " Type : "
				+ getType() + " Massage: " + getMessage() + " Price: " + getPrice() + "$" + " Start Date:" + getStartDate() + 
				" Expires: " + getEndDate() + " Image: " + getImage();
	}

}
