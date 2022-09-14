package com.ensoftcorp.open.cwe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.ensoftcorp.open.cwe.util.Utils;

/**
 * The driver class for building a CWE Atlas graph, manipulation and query.
 */
public class CWEGraph {
	
	private static final String CWE_XML_FILE_PATH = "resources\\cwec_v4.8.xml";

	/**
	 * The main entry point for building the CWE Atlas graph.
	 * 
	 * @throws FileNotFoundException If the {@link #CWE_XML_FILE_PATH} is not found.
	 */
	public static void build() throws FileNotFoundException {
		File cweXMLFile = Utils.getResourceFile(CWE_XML_FILE_PATH);
		CWEXMLToGraphConverter.toGraph(new FileInputStream(cweXMLFile));
	}
	
}
