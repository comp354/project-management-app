package graphview_components;


import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;

import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgraph.layout.JGraphLayoutAlgorithm;
import org.jgraph.layout.SugiyamaLayoutAlgorithm;
import org.jgrapht.graph.*;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.jgrapht.ext.JGraphModelAdapter;

import resources.Activities;

public class CriticalPathChart extends JFrame {

	private static final long serialVersionUID = 1L;

	public CriticalPathChart(DirectedAcyclicGraph<Activities, DefaultEdge> graph, String title) {
		super(title);
		
		JGraphModelAdapter<Activities, DefaultEdge> graphAdapter = new JGraphModelAdapter<Activities, DefaultEdge>( graph );
	    JGraph jgraph = new JGraph( graphAdapter );
	    
		List<DefaultGraphCell> roots = new ArrayList<DefaultGraphCell>(); 
		Iterator<Activities> vertexIterator = graph.vertexSet().iterator(); 
		while (vertexIterator.hasNext()) { 
			Activities vertex = (Activities) vertexIterator.next();
			resizeVertex(vertex, graphAdapter);
			if (graph.inDegreeOf(vertex) == 0) { 
				roots.add(graphAdapter.getVertexCell(vertex));
			}
		}
		
		JGraphLayoutAlgorithm layout = new SugiyamaLayoutAlgorithm();
		JGraphLayoutAlgorithm.applyLayout(jgraph, roots.toArray(), layout);
	    setContentPane(jgraph);
	}

	@SuppressWarnings("unchecked")
	private void resizeVertex(Object vertex, JGraphModelAdapter<Activities, DefaultEdge> jgAdapter)
	{
	    DefaultGraphCell cell = jgAdapter.getVertexCell(vertex);
	    AttributeMap attr = ((GraphCell) cell).getAttributes();
	    Rectangle2D bounds = GraphConstants.getBounds(attr);
	    
	    Rectangle2D newBounds =
	        new Rectangle2D.Double(
	            bounds.getX(),
	            bounds.getY(),
	            370,
	            30);
	
	    GraphConstants.setBounds(attr, newBounds);
	
	    AttributeMap cellAttr = new AttributeMap();
	    cellAttr.put(cell, attr);
	    jgAdapter.edit(cellAttr, null, null, null);
	}
}
