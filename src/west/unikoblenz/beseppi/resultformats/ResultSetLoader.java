package west.unikoblenz.beseppi.resultformats;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

public class ResultSetLoader {

	//Reads json to comparable result set
	public ComparableResultSet readJsonResult(String path)
			throws FileNotFoundException, IOException, ParseException {
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(new FileReader(path));
		JSONObject jsonObject = (JSONObject) obj;
		JSONObject head = (JSONObject) jsonObject.get("head");
		JSONArray vars = (JSONArray) head.get("vars"); // All occurring variable
														// names in result are
														// stored here
		String[] variables = toStringArray(vars);
		ComparableResultSet resultSet = new ComparableResultSet(variables);

		JSONObject results = (JSONObject) jsonObject.get("results");
		JSONArray bindings = (JSONArray) results.get("bindings");
		@SuppressWarnings("unchecked")
		Iterator<JSONObject> itrBindings = bindings.iterator();
		while (itrBindings.hasNext()) {
			JSONObject singleBinding = (JSONObject) itrBindings.next();
			String[] newBindingArray = new String[variables.length];
			for (int i = 0; i < variables.length; i++) {
				JSONObject description = (JSONObject) singleBinding
						.get(variables[i]);
				String value = (String) description.get("value");
				newBindingArray[i] = value;
			}
			resultSet.add(newBindingArray);
		}
		return resultSet;
	}

	//Reads xml to comparable result sets
	public ComparableResultSet readXmlResult(String path)
			throws ParserConfigurationException, SAXException, IOException {
		File xmlFile = new File(path);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(xmlFile);

		// gets all variables names and stores them in array
		NodeList variableNodeList = doc.getElementsByTagName("variable");
		String[] variables = new String[variableNodeList.getLength()];
		for (int i = 0; i < variableNodeList.getLength(); i++) {
			Element currentElement = (Element) variableNodeList.item(i);
			variables[i] = currentElement.getAttribute("name");
		}
		ComparableResultSet resultSet = new ComparableResultSet(variables);

		NodeList results = doc.getElementsByTagName("result");
		for (int i = 0; i < results.getLength(); i++) {
			Node currentResult = results.item(i);
			Element currentResultElement = (Element) currentResult;
			NodeList resultChildren = currentResultElement.getChildNodes();
			String[] newValueArray = new String[variables.length];
			int arrayIndex = 0;
			for (int j = 0; j < resultChildren.getLength(); j++) {
				Node currentNode = resultChildren.item(j);
				if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
					NodeList variableMappings = currentNode.getChildNodes();
					for (int l = 0; l < variableMappings.getLength(); l++) {
						Node currentMapping = variableMappings.item(l);
						if (currentMapping.getNodeType() == Node.ELEMENT_NODE) {
							newValueArray[arrayIndex] = currentMapping
									.getTextContent();
							arrayIndex++;
						}
					}

				}

			}
			resultSet.add(newValueArray);
		}

		return resultSet;
	}

	//Reads tsv to comparable result set
	public ComparableResultSet readTsvResult(String path) throws IOException {
		String line;
		BufferedReader br = new BufferedReader(new FileReader(path));
		String[] variables = br.readLine().split("\t");
		ComparableResultSet resultSet = new ComparableResultSet(variables);
		while ((line = br.readLine()) != null) {
			String[] currentBindings = line.split("\t");
			if (currentBindings.length == variables.length) {
				resultSet.add(currentBindings);
			}
		}
		br.close();
		return resultSet;
	}

	//Read csv to comparable result set
	public ComparableResultSet readCsvResult(String path) throws IOException {
		String line;
		BufferedReader br = new BufferedReader(new FileReader(path));
		String[] variables = br.readLine().split(",");
		ComparableResultSet resultSet = new ComparableResultSet(variables);
		while ((line = br.readLine()) != null) {
			String[] currentBindings = line.split(",");
			if (currentBindings.length == variables.length) {
				resultSet.add(currentBindings);
			}
		}
		br.close();
		return resultSet;
	}

	// turns JSONArray to String[]
	public static String[] toStringArray(JSONArray array) {
		if (array == null)
			return null;

		String[] arr = new String[array.size()];
		@SuppressWarnings("unchecked")
		Iterator<String> itr = array.iterator();
		for (int i = 0; i < arr.length; i++) {
			arr[i] = itr.next();
		}
		return arr;
	}

}
