package GeoNet.Graphs;

import java.util.Queue;
import java.util.Stack;

/**
 * A class that represents an undirected graph
 */
public class UndirectedGraph<E> extends DirectedGraph<E>
        implements GraphInterface<E>, ConnectedGraphInterface<E>, java.io.Serializable
{
    public UndirectedGraph()
    {
        super();
    }

    /**
     *
     * @return number of edges in the graph
     */
    public int getNumberOfEdges()
    {
        return super.getNumberOfEdges() / 2;
    }

    /**
     *
     * @param begin      An object that labels the origin vertex of the edge.
     * @param end        An object, distinct from begin, that labels the end
     *                   vertex of the edge.
     * @param edgeWeight The real value of the edge's weight.
     * @return true if the edge was successfully added
     */
    public boolean addEdge(E begin, E end, double edgeWeight)
    {
        super.addEdge(begin, end, edgeWeight);
        return super.addEdge(end, begin, edgeWeight);
    }

    /**
     *
     * @param begin An object that labels the origin vertex of the edge.
     * @param end   An object, distinct from begin, that labels the end
     *              vertex of the edge.
     * @return true if the edge was successfully added
     */
    public boolean addEdge(E begin, E end)
    {
        return addEdge(begin, end, 0);
    }

    /**
     *
     * @return the topological order of the vertices (cities)
     * @throws UnsupportedOperationException
     */
    public Stack<E> getTopologicalOrder() throws UnsupportedOperationException
    {
        return super.getTopologicalOrder();
    }

    /**
     *
     * @param origin any vertex
     * @return true if the graph is connected
     */
    public boolean isConnected(E origin)
    {
        Queue<E> breadth = getBreadthFirstTraversal(origin);
        int vertices = breadth.size(); //number of vertices in traversal
        return vertices == super.getNumberOfVertices();
    }
}