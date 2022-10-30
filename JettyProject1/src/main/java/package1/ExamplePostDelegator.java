package package1;

import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

public class ExamplePostDelegator {
	HashMap<String, String> headerHashMap;
	HashMap<String, String> bodyHashMap;
	String content;
	
	public static final ExamplePostDelegator EXAMPLE_POST_DELEGATOR=new ExamplePostDelegator();
	
	public static ExamplePostDelegator getInstance() {
		return EXAMPLE_POST_DELEGATOR;
	}
	
	public void setParam(HashMap<String, String> headerHashMap,HashMap<String, String> bodyHashMap,
			String content, HttpServletResponse response) {
		this.headerHashMap=headerHashMap;
		this.bodyHashMap=bodyHashMap;
		this.content=content;
		switchMethod(response);
	}

	private void switchMethod(HttpServletResponse response) {
		switch(getEventName()) {
		case "CREATE_ADMIN":
			CreateAdmin createAdmin=new CreateAdmin();
			try {
				createAdmin.setParam(headerHashMap,bodyHashMap,content,"admin");
				createAdmin.createAdmin(response);
			}catch(Exception e) {
				e.printStackTrace();
			}		
		case "GET_ALL_ADMIN":
			GetAllAdmin getAllAdmin=new GetAllAdmin();
			try {
				getAllAdmin.setParam(headerHashMap,bodyHashMap,content,"admin");
				getAllAdmin.getAllAdminInfo(response);
			}catch(Exception e) {
				e.printStackTrace();
			}
		case "DELETE_ADMIN":
			DeleteAdmin deleteAdmin=new DeleteAdmin();
			try {
				deleteAdmin.setParam(headerHashMap,bodyHashMap,content,"admin");
				deleteAdmin.deleteAdmin(response);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	private String getEventName() {
		return headerHashMap.get("Event-Name");
	}
}
