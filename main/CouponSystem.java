package main;

import db.ConnectionPool;
import facade.AdminFacade;
import facade.Client;
import facade.ClientType;
import facade.CompanyFacade;
import facade.CustomerFacade;
import tasks.DailyCouponExpirationTask;

public class CouponSystem {
	private AdminFacade adminfacade = new AdminFacade();
	private CompanyFacade companyfacade = new CompanyFacade();
	private CustomerFacade customerfacade = new CustomerFacade();
	private DailyCouponExpirationTask dCe = new DailyCouponExpirationTask();
	private static volatile CouponSystem instance = null;
	
	
	private CouponSystem() {
		new Thread (dCe).start();
	}
	
	public DailyCouponExpirationTask getDce() {
		return dCe;
	}

	public void setDce(DailyCouponExpirationTask dCe) {
		this.dCe = dCe;
	}

	public static CouponSystem getInstance() {
		if (instance == null) {
			synchronized (CouponSystem.class) {
				if ( instance == null) {
			instance = new CouponSystem();
			return instance;
	
				}
			}
		}
		return null;
	}
	
	public Client login(String name, String password, ClientType ClientType) throws Exception {
		
			switch (ClientType.toString()) {
			case "ADMIN":
				return adminfacade.login(name, password, ClientType);

			case "COMPANY":
				return companyfacade.login(name, password, ClientType);
				
			case "CUSTOMER":
				return customerfacade.login(name, password, ClientType);
				
			default:
				return null;

			}
		}
		
	public void shutdown() throws Exception {
		dCe.setQuit(false);
		ConnectionPool.getInstance().closeAllConnections();
	}

}
