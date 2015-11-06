/**
 * 
 */
package simplebeans;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sumit
 *
 */
public class QueryBean {

	Map<String, String> queryNameValuePairs;

	/**
	 * 
	 */
	public QueryBean() {
		queryNameValuePairs = new HashMap<String, String>();
	}

	public void addQueryParam(String name, String value) {
		this.queryNameValuePairs.put(name, value);
	}

	public Map<String, String> getQuery() {
		return this.queryNameValuePairs;
	}
}
