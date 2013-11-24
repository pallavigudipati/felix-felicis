package visitor;

import java.util.LinkedHashMap;
import java.util.Map;

import visitor.Method;

public class Info {
	public Map<String, Method> methods = new LinkedHashMap<String, Method>();
	public Map<String, Integer> variables = new LinkedHashMap<String, Integer>(); 
}
