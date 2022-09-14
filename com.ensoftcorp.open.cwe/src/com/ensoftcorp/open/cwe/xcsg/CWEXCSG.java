package com.ensoftcorp.open.cwe.xcsg;

import com.ensoftcorp.atlas.core.xcsg.HierarchyRegistry;
import com.ensoftcorp.atlas.core.xcsg.XCSG;

public interface CWEXCSG {

	public static final HierarchyRegistry<String> HIERARCHY = XCSG.HIERARCHY;
	
	/**
	 * A prefix to be applied to each {@link String} defined in this interface.
	 */
	public static final String CWEXCSG_PREFIX = "XCSG.CWE.";
	
	/**
	 * An attribute to specify associated Id.
	 */
	String Id = CWEXCSG_PREFIX + "Id";
	
	/**
	 * A tag applied to a {@link Node} representing a Weakness {@link XCSG#Node}.
	 */
	String Weakness = HIERARCHY.registerTag(CWEXCSG_PREFIX + "Weakness", XCSG.Node); //$NON-NLS-1$
	
	/**
	 * A tag to represent a relation between two {@link #Weakness}s.
	 */
	String Relation = HIERARCHY.registerTag(CWEXCSG_PREFIX + "Relation", XCSG.Edge); //$NON-NLS-1$
	
	/**
	 * A tag to represent a parent to child relationship.
	 */
	String ParentOfRelation = HIERARCHY.registerTag(CWEXCSG_PREFIX + "ParentOf", Relation); //$NON-NLS-1$	
	
	/**
	 * A tag to represent that the parent can precedes child relationship.
	 */
	String CanPrecedeRelation = HIERARCHY.registerTag(CWEXCSG_PREFIX + "CanPrecede", Relation); //$NON-NLS-1$
	
	/**
	 * A tag to represent that the parent can also be of type child relationship but not the other way around.
	 */
	String CanAlsoBeRelation = HIERARCHY.registerTag(CWEXCSG_PREFIX + "CanAlsoBe", Relation); //$NON-NLS-1$
	
	/**
	 * An {@link XCSG#Edge} tag to denote some similarity with the target weakness that does not 
	 * fit any of the other type of relationships.
	 */
	String PeerOfRelation = HIERARCHY.registerTag(CWEXCSG_PREFIX + "PeerOf", Relation); //$NON-NLS-1$
	
	/**
	 * The RequiredBy and Requires relationships are used to denote a weakness that is part of a composite weakness structure.
	 */
	String RequiresRelation = HIERARCHY.registerTag(CWEXCSG_PREFIX + "Requires", Relation); //$NON-NLS-1$
	
	/**
	 * A tag to represent a view slice of relation between two {@link #Weakness}s.
	 */
	String View = HIERARCHY.registerTag(CWEXCSG_PREFIX + "View", XCSG.Edge); //$NON-NLS-1$
	
	/**
	 * A tag applied to an edge that represent a software development view.
	 */
	String SoftwareDevelopmentView = HIERARCHY.registerTag(CWEXCSG_PREFIX + "SoftwareDevelopment", View); //$NON-NLS-1$
	
	/**
	 * A tag applied to an edge that represent a Hardware Design view.
	 */
	String HardwareDesignView = HIERARCHY.registerTag(CWEXCSG_PREFIX + "HardwareDesign", View); //$NON-NLS-1$
	
	/**
	 * A tag applied to an edge that represent a Research Concepts view.
	 */
	String ResearchConceptsView = HIERARCHY.registerTag(CWEXCSG_PREFIX + "ResearchConcepts", View); //$NON-NLS-1$
	
	/**
	 * A tag applied to an edge that represent a CISQ Data Protection Measures view.
	 */
	String DataProtectionMeasuresView = HIERARCHY.registerTag(CWEXCSG_PREFIX + "DataProtectionMeasures", View); //$NON-NLS-1$
	
	/**
	 * A tag applied to an edge that represent a Simplified Mappings view.
	 */
	String SimplifiedMappingView = HIERARCHY.registerTag(CWEXCSG_PREFIX + "SimplifiedMapping", View); //$NON-NLS-1$
	
	/**
	 * A tag applied to an edge that represent a CISQ Quality Measures view.
	 */
	String QualityMeasuresView = HIERARCHY.registerTag(CWEXCSG_PREFIX + "QualityMeasures", View); //$NON-NLS-1$
	
	/**
	 * A tag applied to an edge that represent a Seven Pernicious Kingdoms view.
	 */
	String SevenPerniciousKingdomsView = HIERARCHY.registerTag(CWEXCSG_PREFIX + "SevenPerniciousKingdoms", View); //$NON-NLS-1$
	
}
