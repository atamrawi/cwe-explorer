package com.ensoftcorp.open.cwe.util;

import com.ensoftcorp.atlas.core.db.graph.Edge;
import com.ensoftcorp.atlas.core.db.graph.Graph;
import com.ensoftcorp.atlas.core.db.graph.Node;
import com.ensoftcorp.atlas.core.db.set.AtlasSet;
import com.ensoftcorp.atlas.core.xcsg.XCSG;

/**
 * A set of utilities for query/manipulate an Atlas graph.
 */
public class GraphUtils {

	/**
	 * Creates a new instance of {@link Node}.
	 * 
	 * @return A new instance of {@link Node}.
	 */
	public static Node createNode() {
		Node node = Graph.U.createNode();
		node.tag(XCSG.Node);
		return node;
	}
	
	/**
	 * Creates a new instance of {@link Edge} that connects <code>from</code> to <code>to</code>.
	 * 
	 * @param from An instance of {@link Node}.
	 * @param to An instance of {@link Node}.
	 * @return A new instance of {@link Edge}.
	 */
	public static Edge createEdge(Node from, Node to) {
		Edge edge = Graph.U.createEdge(from, to);
		edge.tag(XCSG.Edge);
		return edge;
	}
	
	/**
	 * Creates a unidirectional {@link Edge} between the given <code>parent</code> and <code>child</code> with the given 
	 * tags: <code>viewTag</code> and <code>natureTag</code>.
	 * <p>
	 * To create unidirectional edge, we instead create two directional edges between <code>parent</code> and <code>child</code>.
	 * <p>
	 * The creation of edges is dependent on whether the edge does not exist already.
	 * 
	 * @param parent An instance of {@link Node}.
	 * @param child An instance of {@link Node}.
	 * @param viewTag A {@link String}.
	 * @param natureTag A {@link String}.
	 */
	public static void createViewUndirectionalRelationEdge(Node parent, Node child, String viewTag, String natureTag) {
		createViewRelationEdge(parent, child, viewTag, natureTag);
		createViewRelationEdge(child, parent, viewTag, natureTag);
	}
	
	/**
	 * Creates an {@link Edge} between the given <code>parent</code> and <code>child</code> with the given 
	 * tags: <code>viewTag</code> and <code>natureTag</code>.
	 * <p>
	 * The creation of edges is dependent on whether the edge does not exist already.
	 * 
	 * @param parent An instance of {@link Node}.
	 * @param child An instance of {@link Node}.
	 * @param viewTag A {@link String}.
	 * @param natureTag A {@link String}.
	 * @return An instance of {@link Edge}.
	 */
	public static Edge createViewRelationEdge(Node parent, Node child, String viewTag, String natureTag) {
		Edge existingEdge = edgeExists(parent, child, viewTag, natureTag);
		if(existingEdge == null) {
			existingEdge = createEdge(parent, child);
			existingEdge.tag(viewTag);
			existingEdge.tag(natureTag);
		}
		return existingEdge;
	}
	
	/**
	 * Retrieves an {@link Edge} that already existing (if any) between the given <code>parent</code> and <code>child</code> with the given 
	 * tags: <code>viewTag</code> and <code>natureTag</code>.
	 * 
	 * @param parent An instance of {@link Node}.
	 * @param child An instance of {@link Node}.
	 * @param viewTag A {@link String}.
	 * @param natureTag A {@link String}.
	 * @return An instance of {@link Edge} or <code>null</code> if not {@link Edge} is found.
	 */
	public static Edge edgeExists(Node parent, Node child, String viewTag, String natureTag) {
		AtlasSet<Edge> outEdges = parent.out(viewTag);
		for(Edge outEdge: outEdges) {
			if(outEdge.taggedWith(natureTag)) {
				Node candidateChild = outEdge.to();
				if(child.equals(candidateChild)) {
					return outEdge;
				}
			}
		}
		return null;
	}
	
}
