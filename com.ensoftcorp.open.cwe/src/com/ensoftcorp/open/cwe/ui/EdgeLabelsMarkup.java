package com.ensoftcorp.open.cwe.ui;

import com.ensoftcorp.atlas.core.db.graph.Edge;
import com.ensoftcorp.atlas.core.db.graph.GraphElement;
import com.ensoftcorp.atlas.core.markup.IMarkup;
import com.ensoftcorp.atlas.core.markup.MarkupProperty;
import com.ensoftcorp.atlas.core.markup.PropertySet;
import com.ensoftcorp.open.cwe.xcsg.CWEXCSG;

/**
 * An implementation of {@link IMarkup} to put proper labels on {@link Edge}s.
 */
public class EdgeLabelsMarkup implements IMarkup {

	@Override
	public PropertySet get(GraphElement element) {
		PropertySet propertySet = new PropertySet();
		if(element instanceof Edge) {
			Edge edge = (Edge) element;
			if(edge.taggedWith(CWEXCSG.ParentOfRelation)) {
				this.setLabelTextProperty(propertySet, CWEXCSG.ParentOfRelation);
			} else if(edge.taggedWith(CWEXCSG.CanPrecedeRelation)) {
				this.setLabelTextProperty(propertySet, CWEXCSG.CanPrecedeRelation);
			} else if(edge.taggedWith(CWEXCSG.CanAlsoBeRelation)) {
				this.setLabelTextProperty(propertySet, CWEXCSG.CanAlsoBeRelation);
			} else if(edge.taggedWith(CWEXCSG.PeerOfRelation)) {
				this.setLabelTextProperty(propertySet, CWEXCSG.PeerOfRelation);
			} else if(edge.taggedWith(CWEXCSG.RequiresRelation)) {
				this.setLabelTextProperty(propertySet, CWEXCSG.RequiresRelation);
			} 
		}
		return propertySet;
	}
	
	/**
	 * Sets the {@link MarkupProperty#LABEL_TEXT} to the given <code>tag</code> after trimming.
	 * 
	 * @param propertySet An instance of {@link PropertySet}.
	 * @param tag A {@link String}.
	 */
	private void setLabelTextProperty(PropertySet propertySet, String tag) {
		String trimmedTag = tag.substring(tag.lastIndexOf('.') + 1);
		propertySet.set(MarkupProperty.LABEL_TEXT, trimmedTag);
	}

}
