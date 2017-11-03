package service;

import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import dto.User;
import repository.AdRepostitory;


@Path("/auth")
public class AuthService {
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response login( Map<String,String> payload ) throws Exception {
		
		String username = payload.get("username");
		String password = payload.get("password");
		
		
		User user = new AdRepostitory().doLogin(username, password);
		
		if (user != null) {
			return Response.ok(user).build();
		}
		else {
			return Response.status(Status.UNAUTHORIZED).build();
		}
		
	}

}
