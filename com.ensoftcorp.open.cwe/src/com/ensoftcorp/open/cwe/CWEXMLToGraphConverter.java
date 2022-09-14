package com.ensoftcorp.open.cwe;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.ensoftcorp.atlas.core.db.graph.Node;
import com.ensoftcorp.atlas.core.log.Log;
import com.ensoftcorp.atlas.core.xcsg.XCSG;
import com.ensoftcorp.open.cwe.util.GraphUtils;
import com.ensoftcorp.open.cwe.util.Utils;
import com.ensoftcorp.open.cwe.xcsg.CWEXCSG;

public class CWEXMLToGraphConverter {

	/**
	 * Converts the given <code>cweXMLInputStream</code> into an Atlas graph.
	 * 
	 * @param cweXMLInputStream An implementation instance of {@link InputStream}.
	 */
	public static void toGraph(InputStream cweXMLInputStream) {
		Element documentElement = getDocumentElement(cweXMLInputStream);
		List<Element> childElements = Utils.getChildElements(documentElement);
		for(Element childElement: childElements) {
			String childElementTagName = childElement.getTagName();
			switch(childElementTagName) {
			case "Weaknesses":
				weaknessesToGraph(childElement);
				break;
			case "Categories":
			case "Views":
			case "External_References":
			default:
				String elementPath = Utils.getPath(childElement);
				Log.info("Unsupported conversion of tag name " + elementPath);
				break;
			}
		}
	}

	/**
	 * Parses the given <code>cweXMLInputStream</code> and retrieves the root {@link Element}.
	 * 
	 * @param cweXMLInputStream An implementation instance of {@link InputStream}.
	 * @return An instance of {@link Element}.
	 */
	private static Element getDocumentElement(InputStream cweXMLInputStream) {
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(cweXMLInputStream);
			document.getDocumentElement().normalize();
			Element documentElement = document.getDocumentElement();
			return documentElement;
		} catch (ParserConfigurationException | IOException | SAXException e) {
			Log.error("Error while Parsing XML file", e);
			return null;
		}
	}
	
	/**
	 * Converts the given <code>weaknessesElement</code> into an Atlas graph.
	 * 
	 * @param weaknessesElement An instance of {@link Element} with tag name <code>Weaknesses</code>.
	 */
	private static void weaknessesToGraph(Element weaknessesElement) {
		List<Element> weaknessElements = Utils.getChildElements(weaknessesElement);
		Map<Integer, Node> cweIdToNodeMap = new HashMap<Integer, Node>();
		
		// Map nodes.
		for(Element weaknessElement: weaknessElements) {
			Node node = GraphUtils.createNode();
			node.tag(CWEXCSG.Weakness);
			
			int weaknessId = Integer.parseInt(weaknessElement.getAttribute("ID"));
			node.putAttr(CWEXCSG.Id, weaknessId);
			
			String weaknessName = weaknessElement.getAttribute("Name");
			String nodeName = String.format("%d: %s", weaknessId, weaknessName);
			node.putAttr(XCSG.name, nodeName);
			
			cweIdToNodeMap.put(weaknessId, node);
		}
		
		// Map relations.
		for(Element weaknessElement: weaknessElements) {
			int currentCWEId = Integer.parseInt(weaknessElement.getAttribute("ID"));
			Node currentCWENode = cweIdToNodeMap.get(currentCWEId);
			Element relatedWeaknessesElement = Utils.getChildElementWithTagName(weaknessElement, "Related_Weaknesses");
			if(relatedWeaknessesElement == null) {
				continue;
			} else {
				List<Element> relatedWeaknesses = Utils.getChildElements(relatedWeaknessesElement);
				for(Element relatedWeakness: relatedWeaknesses) {
					int relatedCWEId = Integer.parseInt(relatedWeakness.getAttribute("CWE_ID"));
					Node relatedCWENode = cweIdToNodeMap.get(relatedCWEId);
					
					int viewId = Integer.parseInt(relatedWeakness.getAttribute("View_ID"));
					String viewTag = Utils.getViewTag(viewId);
					if(viewTag == null) {
						String debugMessage = String.format("Unsupported view id [%d] for the relation from CWE [%d] to CWE [%d]", viewId, currentCWEId, relatedCWEId);
						Log.debug(debugMessage);
						continue;
					}
					
					String natureAttribute = relatedWeakness.getAttribute("Nature");
					switch(natureAttribute) {
					case "ChildOf":
						GraphUtils.createViewRelationEdge(relatedCWENode, currentCWENode, viewTag, CWEXCSG.ParentOfRelation);
						break;
					case "ParentOf":	
						GraphUtils.createViewRelationEdge(currentCWENode, relatedCWENode, viewTag, CWEXCSG.ParentOfRelation);
						break;
					case "CanFollow":
					case "StartsWith":
						GraphUtils.createViewRelationEdge(relatedCWENode, currentCWENode, viewTag, CWEXCSG.CanPrecedeRelation);
						break;	
					case "CanPrecede":
						GraphUtils.createViewRelationEdge(currentCWENode, relatedCWENode, viewTag, CWEXCSG.CanPrecedeRelation);
						break;	
					case "CanAlsoBe":	
						GraphUtils.createViewRelationEdge(currentCWENode, relatedCWENode, viewTag, CWEXCSG.CanAlsoBeRelation);
						break;
					case "PeerOf":
						GraphUtils.createViewUndirectionalRelationEdge(currentCWENode, relatedCWENode, viewTag, CWEXCSG.PeerOfRelation);
						break;	
					case "RequiredBy":
						GraphUtils.createViewRelationEdge(relatedCWENode, currentCWENode, viewTag, CWEXCSG.RequiresRelation);
						break;
					case "Requires":
						GraphUtils.createViewRelationEdge(currentCWENode, relatedCWENode, viewTag, CWEXCSG.RequiresRelation);
						break;
					default:
						String debugMessage = String.format("Unsupported nature [%s] for view [%d] for CWE [%d]", natureAttribute, viewId, currentCWEId);
						Log.debug(debugMessage);
					}
				}
			}
		}
	}
	
}
