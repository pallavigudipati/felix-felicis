package visitor;

import java.util.LinkedHashMap;
import java.util.Map;

public class Method {
	public Map<String, Integer> args = new LinkedHashMap<String, Integer>();
	public Map<String, Integer> vars = new LinkedHashMap<String, Integer>();
	public boolean overridden = false;
}
