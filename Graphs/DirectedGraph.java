package GeoNet.Graphs;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.util.TreeMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A class that implements the ADT directed graph.
 */
public class DirectedGraph<T> implements GraphInterface<T>
{
    private Map<T, GeoNet.Graphs.VertexInterface<T>> vertices;
    private int edgeCount;

    public DirectedGraph()
    {
        this.vertices = new TreeMap<>();
        this.edgeCount = 0;
    }

    /**
     *
     * @param vertexLabel An object that labels the new vertex and is
     *                    distinct from the labels of current vertices.
     * @return
     */
    public boolean addVertex(T vertexLabel)
    {
        VertexInterface<T> isDuplicate = this.vertices.put(vertexLabel, new Vertex<T>(vertexLabel));
        return isDuplicate == null; // was add to dictionary successful?
    }

    /**
     *
     * @param begin      An object that labels the origin vertex of the edge.
     * @param end        An object, distinct from begin, that labels the end
     *                   vertex of the edge.
     * @param edgeWeight The real value of the edge's weight.
     * @return true if the edge was successfully added
     */
    public boolean addEdge(T begin, T end, double edgeWeight)
    {
        boolean result = false;

        VertexInterface<T> beginVertex = this.vertices.get(begin);
        VertexInterface<T> endVertex = this.vertices.get(end);

        if ((beginVertex != null) && (endVertex != null))
            result = beginVertex.connect(endVertex, edgeWeight);

        if (result)
            this.edgeCount++;

        return result;
    }

    /**
     *
     * @param begin An object that labels the origin vertex of the edge.
     * @param end   An object, distinct from begin, that labels the end
     *              vertex of the edge.
     * @return true if the edge was successfully added
     */
    public boolean addEdge(T begin, T end)
    {
        return addEdge(begin, end, 0);
    }

    /**
     *
     * @param begin An object that labels the origin vertex of the edge.
     * @param end   An object that labels the end vertex of the edge.
     * @return true if an edge exists between the given vertices
     */
    public boolean hasEdge(T begin, T end)
    {
        boolean found = false;

        VertexInterface<T> beginVertex = this.vertices.get(begin);
        VertexInterface<T> endVertex = this.vertices.get(end);

        if ((beginVertex != null) && (endVertex != null)) {
            Iterator<VertexInterface<T>> neighbors =
                    beginVertex.getNeighborIterator();
            while (!found && neighbors.hasNext()) {
                VertexInterface<T> nextNeighbor = neighbors.next();
                if (endVertex.equals(nextNeighbor))
                    found = true;
            }
        }

        return found;
    }

    /**
     *
     * @return true if there are no vertices in the graph
     */
    public boolean isEmpty()
    {
        return this.vertices.isEmpty();
    }

    /**
     * clear all vertices
     */
    public void clear()
    {
        this.vertices.clear();
        this.edgeCount = 0;
    }

    /**
     *
     * @return number of vertices in the graph
     */
    public int getNumberOfVertices()
    {
        return this.vertices.size();
    }

    /**
     *
     * @return number of edges in the graph
     */
    public int getNumberOfEdges()
    {
        return this.edgeCount;
    }

    /**
     * unvisit all vertices, set their costs to 0 and their predecessors to null
     */
    protected void resetVertices()
    {
        Collection<VertexInterface<T>> values = this.vertices.values();
        Iterator<VertexInterface<T>> vertexIterator = values.iterator();
        while (vertexIterator.hasNext()) {
            VertexInterface<T> nextVertex = vertexIterator.next();
            nextVertex.unvisit();
            nextVertex.setCost(0);
            nextVertex.setPredecessor(null);
        }
    }

    /**
     *
     * @param origin an object that labels the origin vertex of the
     *               traversal
     * @return queue of all vertices in the graph in breadth first order
     */
    public Queue<T> getBreadthFirstTraversal(T origin)
    {
        resetVertices();
        Queue<T> traversalOrder = new LinkedBlockingQueue<>();
        Queue<VertexInterface<T>> vertexQueue =
                new LinkedBlockingQueue<>();
        VertexInterface<T> originVertex = this.vertices.get(origin);
        originVertex.visit();
        traversalOrder.offer(origin);    // enqueue vertex label
        vertexQueue.offer(originVertex); // enqueue vertex

        while (!vertexQueue.isEmpty()) {
            VertexInterface<T> frontVertex = vertexQueue.poll();

            Iterator<VertexInterface<T>> neighbors = frontVertex.getNeighborIterator();
            while (neighbors.hasNext()) {
                VertexInterface<T> nextNeighbor = neighbors.next();
                if (!nextNeighbor.isVisited()) {
                    nextNeighbor.visit();
                    traversalOrder.offer(nextNeighbor.getLabel());
                    vertexQueue.offer(nextNeighbor);
                }
            }
        }
        return traversalOrder;
    }

    /**
     *
     * @param origin an object that labels the origin vertex of the
     *               traversal
     * @return queue of all vertices in the graph in depth first order
     */
    public Queue<T> getDepthFirstTraversal(T origin)
    {
        resetVertices();
        Queue<T> traversalOrder = new LinkedBlockingQueue<>();
        Stack<VertexInterface<T>> vertexStack = new Stack<>();
        VertexInterface<T> originVertex = this.vertices.get(origin); //mark origin as visited
        originVertex.visit();
        traversalOrder.offer(origin);
        vertexStack.push(originVertex);
        VertexInterface<T> top;
        while (!vertexStack.isEmpty()) {
            top = vertexStack.peek();
            if (top.getUnvisitedNeighbor() != null) {
                VertexInterface<T> nextNeighbor = top.getUnvisitedNeighbor();
                nextNeighbor.visit();
                traversalOrder.offer(nextNeighbor.getLabel());
                vertexStack.push(nextNeighbor);
            } else
                vertexStack.pop();
        }
        return traversalOrder;
    }

    /**
     *
     * @return a stack of all the vertices in topological order
     */
    public Stack<T> getTopologicalOrder()
    {
        resetVertices();

        Stack<T> vertexStack = new Stack<>();
        int numberOfVertices = getNumberOfVertices();
        for (int counter = 1; counter <= numberOfVertices; counter++) {
            VertexInterface<T> nextVertex = findTerminal();
            nextVertex.visit();
            vertexStack.push(nextVertex.getLabel());
        }

        return vertexStack;
    }

    /**
     * Precondition: path is an empty stack (NOT null)
     */
    public int getShortestPath(T begin, T end, Stack<T> path)
    {
        resetVertices();
        boolean done = false;
        Queue<VertexInterface<T>> vertexQueue =
                new LinkedBlockingQueue<>();
        VertexInterface<T> originVertex = this.vertices.get(begin);
        VertexInterface<T> endVertex = this.vertices.get(end);

        originVertex.visit();
        // Assertion: resetVertices() has executed setCost(0)
        // and setPredecessor(null) for originVertex

        vertexQueue.offer(originVertex);

        while (!done && !vertexQueue.isEmpty()) {
            VertexInterface<T> frontVertex = vertexQueue.poll();

            Iterator<VertexInterface<T>> neighbors =
                    frontVertex.getNeighborIterator();
            while (!done && neighbors.hasNext()) {
                VertexInterface<T> nextNeighbor = neighbors.next();

                if (!nextNeighbor.isVisited()) {
                    nextNeighbor.visit();
                    nextNeighbor.setCost(1 + frontVertex.getCost());
                    nextNeighbor.setPredecessor(frontVertex);
                    vertexQueue.offer(nextNeighbor);
                }

                if (nextNeighbor.equals(endVertex))
                    done = true;
            }
        }

        // traversal ends; construct shortest path
        int pathLength = (int) endVertex.getCost();
        path.push(endVertex.getLabel());

        VertexInterface<T> vertex = endVertex;
        while (vertex.hasPredecessor()) {
            vertex = vertex.getPredecessor();
            path.push(vertex.getLabel());
        }

        return pathLength;
    }

    /**
     * Precondition: path is an empty stack (NOT null)
     */
    public double getCheapestPath(T begin, T end, Stack<T> path)
    {
        resetVertices();
        boolean done = false;
        // use EntryPQ instead of GeoNet.Graphs.Vertex because multiple entries contain
        // the same vertex but different costs - cost of path to vertex is EntryPQ's priority value
        PriorityQueue<EntryPQ> priorityQueue = new PriorityQueue<>();

        VertexInterface<T> originVertex = this.vertices.get(begin);
        VertexInterface<T> endVertex = this.vertices.get(end);
        priorityQueue.add(new EntryPQ(originVertex, 0, null));

        while (!done && !priorityQueue.isEmpty()) {
            EntryPQ frontEntry = priorityQueue.remove();
            VertexInterface<T> frontVertex = frontEntry.getVertex();
            if (!frontVertex.isVisited()) {
                frontVertex.visit();
                frontVertex.setCost(frontEntry.getCost());
                frontVertex.setPredecessor(frontEntry.getPredecessor());
                if (frontVertex == endVertex)
                    done = true;
                else {
                    Iterator<VertexInterface<T>> neighborIter = frontVertex.getNeighborIterator();
                    Iterator<Double> edgeWeights = frontVertex.getWeightIterator();
                    while (neighborIter.hasNext()) {
                        VertexInterface<T> nextNeighbor = neighborIter.next();
                        double weightOfEdgeToNeighbor = edgeWeights.next();
                        if (!nextNeighbor.isVisited()) {
                            double nextCost = weightOfEdgeToNeighbor + frontVertex.getCost();
                            priorityQueue.add(new EntryPQ(nextNeighbor, nextCost, frontVertex));
                        }
                    }
                }
            }
        }
        double pathCost = endVertex.getCost();
        path.push(endVertex.getLabel());
        VertexInterface<T> vertex = endVertex;
        while (vertex.hasPredecessor()) {
            vertex = vertex.getPredecessor();
            path.push(vertex.getLabel());
        }
        return pathCost;
    }

    /**
     *
     * @return the terminal vertex of the graph
     */
    protected VertexInterface<T> findTerminal()
    {
        boolean found = false;
        VertexInterface<T> result = null;
        Collection<VertexInterface<T>> values = this.vertices.values();
        Iterator<VertexInterface<T>> vertexIterator = values.iterator();

        while (!found && vertexIterator.hasNext()) {
            VertexInterface<T> nextVertex = vertexIterator.next();

            // if nextVertex is unvisited AND has only visited neighbors)
            if (!nextVertex.isVisited()) {
                if (nextVertex.getUnvisitedNeighbor() == null) {
                    found = true;
                    result = nextVertex;
                }
            }
        }

        return result;
    }

    /**
     * Used for testing
      */
    public void display()
    {
        System.out.println("Graph has " + getNumberOfVertices() + " vertices and " +
                getNumberOfEdges() + " edges.");

        System.out.println("\nEdges exist from the first vertex in each line to the other vertices in the line.");
        System.out.println("(Edge weights are given; weights are zero for unweighted graphs):\n");
        Collection<VertexInterface<T>> values = this.vertices.values();
        Iterator<VertexInterface<T>> vertexIterator = values.iterator();
        while (vertexIterator.hasNext()) {
            ((Vertex<T>) (vertexIterator.next())).display();
        }
    }

    private class EntryPQ implements Comparable<EntryPQ>
    {
        private VertexInterface<T> vertex;
        private VertexInterface<T> previousVertex;
        private double cost; // cost to nextVertex

        private EntryPQ(VertexInterface<T> vertex, double cost, VertexInterface<T> previousVertex)
        {
            this.vertex = vertex;
            this.previousVertex = previousVertex;
            this.cost = cost;
        }

        public VertexInterface<T> getVertex()
        {
            return this.vertex;
        }

        public VertexInterface<T> getPredecessor()
        {
            return this.previousVertex;
        }

        public double getCost()
        {
            return this.cost;
        }

        public int compareTo(EntryPQ otherEntry)
        {
            return (int) Math.signum(this.cost - otherEntry.cost);
        }

        public String toString()
        {
            return this.vertex.toString() + " " + this.cost;
        }
    }
}