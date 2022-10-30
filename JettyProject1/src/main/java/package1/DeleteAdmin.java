package package1;

import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class DeleteAdmin {
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

	public void deleteAdmin(HttpServletResponse response) {
		try {
			if(validateJsonSyntax(content)&&validateJsonData(content)) {
				JSONObject json=new JSONObject(content);
				HashMap<String, Object> returnMap=new HashMap<>();
				returnMap=EsOperation.getInstance().deleteAdmin(indexName,json);
				JSONObject json1=new JSONObject();
				json1.put("deleted admin", returnMap);
				response.setContentType("text/plain");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write("admin"+json1);

			}

		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	private boolean validateJsonData(String content2) {
		boolean dataValid=false;
		try{
			JSONObject json=new JSONObject(content2);
			
			if(!json.has("adminName")) {
				throw new ParameterMissingException("adminName missing");
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
