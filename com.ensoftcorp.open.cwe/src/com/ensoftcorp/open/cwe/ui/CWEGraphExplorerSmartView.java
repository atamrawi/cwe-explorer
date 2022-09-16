package com.ensoftcorp.open.cwe.ui;

import com.ensoftcorp.atlas.core.markup.IMarkup;
import com.ensoftcorp.atlas.core.query.Q;
import com.ensoftcorp.atlas.core.query.Query;
import com.ensoftcorp.atlas.core.script.FrontierStyledResult;
import com.ensoftcorp.atlas.core.script.StyledResult;
import com.ensoftcorp.atlas.ui.scripts.selections.FilteringAtlasSmartViewScript;
import com.ensoftcorp.atlas.ui.scripts.selections.IExplorableScript;
import com.ensoftcorp.atlas.ui.scripts.selections.IResizableScript;
import com.ensoftcorp.atlas.ui.scripts.util.SimpleScriptUtil;
import com.ensoftcorp.atlas.ui.selection.event.FrontierEdgeExploreEvent;
import com.ensoftcorp.atlas.ui.selection.event.IAtlasSelectionEvent;

public class CWEGraphExplorerSmartView extends FilteringAtlasSmartViewScript implements IResizableScript, IExplorableScript {
	
	@Override
	public String getTitle() {
		return "CWE Graph Explorer";
	}
	
	@Override
	protected String[] getSupportedNodeTags() {
		return EVERYTHING;
	}
	
	@Override
	protected String[] getSupportedEdgeTags() {
		return NOTHING;
	}

	@Override
	public int getDefaultStepTop() {
		return 1;
	}

	@Override
	public int getDefaultStepBottom() {
		return 1;
	}
	
	@Override
	public FrontierStyledResult explore(FrontierEdgeExploreEvent event, FrontierStyledResult oldResult) {
		return SimpleScriptUtil.explore(this, event, oldResult);
	}

	@Override
	public FrontierStyledResult evaluate(IAtlasSelectionEvent event, int reverse, int forward) {
		Q filteredSelections = filter(event.getSelection());
		if(filteredSelections.eval().nodes().isEmpty()){
			return null;
		}
		
		// graph is the entire universe
		Q graph = Query.universe();
		
		// get the selected origin
		Q origin = filteredSelections;
		
		// compute what to show for current steps
		Q f = origin.forwardStepOn(graph, forward);
		Q r = origin.reverseStepOn(graph, reverse);
		Q result = f.union(r);
		
		// compute what is on the frontier
		Q frontierReverse = origin.reverseStepOn(graph, reverse+1);
		frontierReverse = frontierReverse.differenceEdges(result).retainEdges();
		Q frontierForward = origin.forwardStepOn(graph, forward+1);
		frontierForward = frontierForward.differenceEdges(result).retainEdges();

		IMarkup edgeLabelsMarkup = new EdgeLabelsMarkup();
		
		// show the result
		FrontierStyledResult frontier = new FrontierStyledResult(result, frontierReverse, frontierForward, edgeLabelsMarkup);
		
		// highlight the selection
		frontier.setInput(origin);
		
		return frontier;
	}

	@Override
	protected StyledResult selectionChanged(IAtlasSelectionEvent event, Q filteredSelection) {
		return null;
	}
	
}
