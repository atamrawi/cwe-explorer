package com.ensoftcorp.open.cwe.util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import org.eclipse.core.runtime.FileLocator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ensoftcorp.atlas.core.log.Log;
import com.ensoftcorp.open.cwe.Activator;
import com.ensoftcorp.open.cwe.xcsg.CWEXCSG;

public class Utils {

	/**
	 * Retrieves an instance of {@link File} from the given <code>filePath</code> located within the plugin.
	 * 
	 * @param filePath A {@link String}.
	 * @return A new instance of {@link File}.
	 */
	public static File getResourceFile(String filePath) {	
		URL fileURL = Activator.getDefault().getBundle().getEntry(filePath);
		try {
			fileURL = FileLocator.resolve(fileURL);
		} catch (IOException e) {
			Log.error("Could not resolve the URL for file at the given path: " + filePath, e);
			return null;
		}
		
		URI resolvedURI = null;
		try {
			resolvedURI = new URI(fileURL.getProtocol(), fileURL.getPath(), null);
		} catch (URISyntaxException e) {
			Log.error("Could not resolve the URI for the located file at the given path: " + filePath, e);
			return null;
		}
		
		File file = new File(resolvedURI);
		return file;
	}
	
	/**
	 * Retrieves the list of child {@link Element}s of the given <code>element</code> that are only {@link Node#ELEMENT_NODE}.
	 * 
	 * @param element An instance of {@link Element}.
	 * @return A list of {@link Element}s.
	 */
	public static List<Element> getChildElements(Element element) {
		List<Element> childElements = new ArrayList<Element>();
		NodeList nodeList = element.getChildNodes();
		for (int count = 0; count < nodeList.getLength(); count++) {
	          Node childNode = nodeList.item(count);
	          if (childNode.getNodeType() == Node.ELEMENT_NODE) {
	        	  childElements.add((Element) childNode);
	          }
		}
		return childElements;
	}
	
	public static Element getChildElementWithTagName(Element element, String tagName) {
		NodeList nodeList = element.getChildNodes();
		for (int count = 0; count < nodeList.getLength(); count++) {
	          Node childNode = nodeList.item(count);
	          if (childNode.getNodeType() == Node.ELEMENT_NODE) {
	        	  Element nodeElement = (Element) childNode;
	        	  if(tagName.equals(nodeElement.getTagName())) {
	        		  return nodeElement;
	        	  }
	          }
		}
		return null;
	}
	
	public static String getPath(Node elementNode) {
		List<String> pathParts = new ArrayList<String>();
		Node currentNode = elementNode;
		while(!(currentNode instanceof Document)) {
			String nodeName = currentNode.getNodeName();
			pathParts.add(nodeName);
			currentNode = currentNode.getParentNode();
		}
		
		StringJoiner pathJoiner = new StringJoiner("/", "//", "");
		for(int i = pathParts.size() - 1; i >= 0; i--) {
			pathJoiner.add(pathParts.get(i));
		}
		return pathJoiner.toString();
	}
	
	/**
	 * Gets the {@link CWEXCSG} {@link String} corresponding to the given <code>viewId</code>.
	 * 
	 * @param viewId An {@link Integer}.
	 * @return A {@link String} or <code>null</code> if the <code>viewId</code> is not handled.
	 */
	public static String getViewTag(int viewId) {
		switch(viewId) {
		case 699:
			return CWEXCSG.SoftwareDevelopmentView;
		case 1194:
			return CWEXCSG.HardwareDesignView;
		case 1000:
			return CWEXCSG.ResearchConceptsView;
		case 1340:
			return CWEXCSG.DataProtectionMeasuresView;
		case 1003:
			return CWEXCSG.SimplifiedMappingView;
		case 1305:
			return CWEXCSG.QualityMeasuresView;
		case 700:
			return CWEXCSG.SevenPerniciousKingdomsView;
		}
		return null;
	}
	
}
