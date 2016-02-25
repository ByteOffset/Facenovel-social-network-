package socialnet;

import java.util.Queue;
import java.util.Stack;

/**
 * Created by preston.peterson651 on 12/2/2015.
 */
public class UndirectedGraph<T> extends DirectedGraph<T>
        implements GraphInterface<T>, ConnectedGraphInterface<T>, java.io.Serializable {
    UndirectedGraph() {
        super();
    }

    public int getNumberOfEdges() {
        return super.getNumberOfEdges() / 2;
    }

    public boolean addEdge(T begin, T end, double edgeWeight) {
        super.addEdge(begin, end, edgeWeight);
        return super.addEdge(end, begin, edgeWeight);
    }
    public boolean addEdge(T begin, T end) {
        return addEdge(begin, end, 0);
    }
    public Stack<T> getTopologicalOrder() throws UnsupportedOperationException {
        return super.getTopologicalOrder();
    }

    public boolean isConnected(T origin) {
        Queue<T> breadth = getBreadthFirstTraversal(origin);
        int vertices = breadth.size(); //number of vertices in traversal
        return vertices == super.getNumberOfVertices();
    }
}