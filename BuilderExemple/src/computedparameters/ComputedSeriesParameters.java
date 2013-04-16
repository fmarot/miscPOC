package computedparameters;
import java.util.HashMap;
import java.util.Map;

/** Container for the pairs key/values. Only buildLong and buildShort are public methods */
public class ComputedSeriesParameters {

	private Map<String, String>	longParams;
	private Map<String, String>	shortParams;

	protected ComputedSeriesParameters() {
		longParams = new HashMap<String, String>();
		shortParams = new HashMap<String, String>();
	}

	public String buildLong() {
		return buildFromMap(longParams);
	}


	public String buildShort() {
		return buildFromMap(shortParams);
	}

	protected void addLongOnlyParam(String key, String value) {
		longParams.put(key, value);
	}

	protected void addLongAndShortParam(String key, String value) {
		longParams.put(key, value);
		shortParams.put(key, value);
	}

	private String buildFromMap(Map<String, String> map) {
		String result = "";
		for (String key : map.keySet()) {
			String value = map.get(key);
			result += key + "=" + value + "|";
		}
		result = result.substring(0, result.length() - 1);
		return result;
	}
}
