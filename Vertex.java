package lab14;import java.util.Iterator;import java.util.LinkedList;import java.util.List;import java.util.NoSuchElementException;/** * A class that represents a vertex in a graph. * * @author Frank M. Carrano * @version 11/21/2015 * @modyfiedBy Anna Bieszczad */class Vertex<T> implements VertexInterface<T>{    //private Profile<T> profile;    private T label;    private List<Edge> edgeList; // edges to neighbors    private boolean visited;                          // true if visited    private VertexInterface<T> previousVertex;        // on path to this vertex    private double cost;                              // of path to this vertex    public Vertex(T vertexLabel)    {        this.label = vertexLabel;        this.edgeList = new LinkedList<>();        this.visited = false;        this.previousVertex = null;        this.cost = 0;    } // end constructor//    public Vertex(Profile<T> profile) {//        this.profile = profile;//        this.label = this.profile.getName();//        this.edgeList = new LinkedList<>();//        this.visited = false;//        this.previousVertex = null;//        this.cost = 0;//    }    public T getLabel()    {        return this.label;    } // end getLabel    public boolean connect(VertexInterface<T> endVertex,                           double edgeWeight)    {        boolean result = false;        if (!this.equals(endVertex))        { // vertices are distinct            Iterator<VertexInterface<T>> neighbors = this.getNeighborIterator();            boolean duplicateEdge = false;            while (!duplicateEdge && neighbors.hasNext())            {                VertexInterface<T> nextNeighbor = neighbors.next();                if (endVertex.equals(nextNeighbor))                    duplicateEdge = true;            } // end while            if (!duplicateEdge)            {                this.edgeList.add(new Edge(endVertex, edgeWeight));                result = true;            } // end if        } // end if        return result;    } // end connect    public boolean connect(VertexInterface<T> endVertex)    {        return connect(endVertex, 0);    } // end connect    public Iterator<VertexInterface<T>> getNeighborIterator()    {        return new NeighborIterator();    } // end getNeighborIterator    public Iterator<Double> getWeightIterator()    {        return new WeightIterator();    } // end getWeightIterator    public boolean hasNeighbor()    {        return !this.edgeList.isEmpty();    } // end hasNeighbor    public VertexInterface<T> getUnvisitedNeighbor()    {        VertexInterface<T> result = null;        Iterator<VertexInterface<T>> neighbors = getNeighborIterator();        while (neighbors.hasNext() && (result == null))        {            VertexInterface<T> nextNeighbor = neighbors.next();            if (!nextNeighbor.isVisited())                result = nextNeighbor;        } // end while        return result;    } // end getUnvisitedNeighbor    public boolean hasPredecessor()    {        return this.previousVertex != null;    } // end hasPredecessor    public void setPredecessor(VertexInterface<T> predecessor)    {        this.previousVertex = predecessor;    } // end setPredecessor    public VertexInterface<T> getPredecessor()    {        return this.previousVertex;    } // end getPredecessor    public void visit()    {        this.visited = true;    } // end visit    public void unvisit()    {        this.visited = false;    } // end unvisit    public boolean isVisited()    {        return this.visited;    } // end isVisited    public double getCost()    {        return this.cost;    } // end getCost    public void setCost(double newCost)    {        this.cost = newCost;    } // end setCost    public boolean equals(Object other)    {        boolean result;        if ((other == null) || (getClass() != other.getClass()))            result = false;        else        {            // the cast is safe because other has the same type as this object            @SuppressWarnings("unchecked")            Vertex<T> otherVertex = (Vertex<T>) other;            result = this.label.equals(otherVertex.label);        } // end if        return result;    } // end equals    public String toString()    {        return this.label.toString();    } // end toString    public void display() // for testing    {        System.out.print(this.label + " -> ");        Iterator<VertexInterface<T>> vertexIterator = getNeighborIterator();        Iterator<Double> weightIterator = getWeightIterator();        while (vertexIterator.hasNext())        {            Vertex<T> vert = (Vertex<T>) vertexIterator.next();            System.out.print(vert + " " + weightIterator.next() + " ");        } // end while        System.out.println();    } // end display    protected class Edge    {        private VertexInterface<T> vertex; // end vertex        private double weight;        protected Edge(VertexInterface<T> endVertex, double edgeWeight)        {            this.vertex = endVertex;            this.weight = edgeWeight;        } // end constructor        protected VertexInterface<T> getEndVertex()        {            return this.vertex;        } // end getEndVertex        protected double getWeight()        {            return this.weight;        } // end getWeight        public String toString() // for testing only        {            return this.vertex.toString() + " " + this.weight;        } // end toString    } // end Edge    private class NeighborIterator implements Iterator<VertexInterface<T>>    {        private Iterator<Edge> edges;        private NeighborIterator()        {            this.edges = edgeList.iterator();        } // end default constructor        public boolean hasNext()        {            return this.edges.hasNext();        } // end hasNext        public VertexInterface<T> next()        {            VertexInterface<T> nextNeighbor = null;            if (this.edges.hasNext())            {                Edge edgeToNextNeighbor = this.edges.next();                nextNeighbor = edgeToNextNeighbor.getEndVertex();            }            else                throw new NoSuchElementException();            return nextNeighbor;        } // end next        public void remove()        {            throw new UnsupportedOperationException();        } // end remove    } // end NeighborIterator    private class WeightIterator implements Iterator<Double>    {        private Iterator<Edge> edges;        private WeightIterator()        {            this.edges = edgeList.iterator();        } // end default constructor        public boolean hasNext()        {            return this.edges.hasNext();        } // end hasNext        public Double next()        {            Double edgeWeight = new Double(0);            if (this.edges.hasNext())            {                Edge edgeToNextNeighbor = this.edges.next();                edgeWeight = edgeToNextNeighbor.getWeight();            }            else                throw new NoSuchElementException();            return edgeWeight;        } // end next        public void remove()        {            throw new UnsupportedOperationException();        } // end remove    } // end WeightIterator}