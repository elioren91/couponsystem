package tasks;

import dao.CouponDBDAO;

public class DailyCouponExpirationTask implements Runnable {
	private boolean quit = true;
	private CouponDBDAO couponDBDAO = new CouponDBDAO();

	public DailyCouponExpirationTask() {
	}

	public boolean getQuit() {
		return quit;
	}

	public void setQuit(boolean quit) {
		this.quit = quit;
	}

	/**
	 * ability for a new thread
	 */
	@Override
	public void run() {
		while (quit) {
			try {
				couponDBDAO.removeExpiredCoupon();
				Thread.sleep(60000 * 60 * 24);// 24 hrs in milliseconds
			} catch (Exception e) {
				System.out.println(e.getMessage() + "Daily Operation task failed.");
			}
		}
	}

}
