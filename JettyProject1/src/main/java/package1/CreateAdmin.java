package package1;

import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class CreateAdmin {
	HashMap<String, String> headerHashMap;
	HashMap<String, String> bodyHashMap;
	String content;
	String indexName;
	
	public void setParam(HashMap<String, String> headerHashMap,HashMap<String, String> bodyHashMap,
			String content, String indexName) {
		this.headerHashMap=headerHashMap;
		this.bodyHashMap=bodyHashMap;
		this.content=content;
		this.indexName=indexName;
	}

	public void createAdmin(HttpServletResponse response) {
		JSONObject obj=new JSONObject();
		try {
			if(validateJsonSyntax(content)&&validateJsonData(content)) {
				JSONObject json=new JSONObject(content);
				HashMap<String, Object> map=new HashMap<>();
				
				if(json.has("network")) {
					map.put("network", json.getString("network"));
				}
				
				if(json.has("adminName")) {
					map.put("adminName", json.getString("adminName"));
				}
				
				if(json.has("userId")) {
					map.put("userId", json.getString("userId"));
				}
				
				if(json.has("password")) {
					map.put("password", json.getString("password"));
				}
				
				map.put("adminStatus", "active");
				
				HashMap<String, Object> returnMap=new HashMap<>();
				JSONObject json1=new JSONObject();
				if(EsOperation.getInstance().indexExist(indexName)) {
					returnMap=EsOperation.getInstance().insertData(indexName,map);
					json1.put("admin", returnMap);
					response.setContentType("text/plain");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write("admin created successfuly"+json1);
				}else {
					EsOperation.getInstance().createIndex(indexName);
					returnMap=EsOperation.getInstance().insertData(indexName,map);
					json1.put("admin", returnMap);
					response.setContentType("text/plain");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write("admin created successfuly"+json1);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	private boolean validateJsonData(String content2) {
		boolean dataValid=false;
		try{
			JSONObject json=new JSONObject(content2);
			if(!json.has("network")) {
				throw new ParameterMissingException("adminName missing");
			}
			
			if(!json.has("adminName")) {
				throw new ParameterMissingException("adminName missing");
			}
			
			if(!json.has("userId")) {
				throw new ParameterMissingException("userId missing");
			}
			
			if(!json.has("password")) {
				throw new ParameterMissingException("password missing");
			}
			
			dataValid=true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return dataValid;
	}

	private boolean validateJsonSyntax(String content2) {
		try {
			JSONObject json=new JSONObject(content2);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
