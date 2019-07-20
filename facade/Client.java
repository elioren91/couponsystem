package facade;

import facade.ClientType;

public interface Client {
	Client login(String name, String password, ClientType clientType) throws Exception;
}
