package west.unikoblenz.beseppi.databaseaccessors;

import java.io.IOException;
import java.net.URLEncoder;
import java.io.*;
import java.net.*;

//Accessor for AllegroGraph
//Needs query url pw and userName
public class AgraphAccessor {
	private String queryUrl;
	private String password;
	private String userName;
	
	public AgraphAccessor(String queryUrl, String userName, String password) {
		this.queryUrl = queryUrl;
		this.userName = userName;
		this.password = password;
	}

	public String executeQuery(String query) throws IOException {
		Authenticator.setDefault (new Authenticator() {
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication (userName, password.toCharArray());
		    }
		});
		
		StringBuilder result = new StringBuilder();
		URL toExecute = new URL(queryUrl + URLEncoder.encode(query, "UTF-8"));
		HttpURLConnection conn = (HttpURLConnection) toExecute.openConnection();
	      conn.setRequestMethod("GET");
	      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	      String line;
	      while ((line = rd.readLine()) != null) {
	         result.append(line);
	      }
	      rd.close();
	      return result.toString();
	   }
	
	
	}

