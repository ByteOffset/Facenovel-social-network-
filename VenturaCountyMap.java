package GeoNet;

import java.util.Stack;

/**
 * A class that represents a physical map of most of the cities in Ventura County, California
 */
public class VenturaCountyMap
{
    UndirectedGraph<String> countyMap;
    public static final String[] CITIES_OF_VENTURA_COUNTY = new String[]{"Camarillo",
            "Fillmore", "Moorpark", "Ojai", "Oxnard", "Port Hueneme", "Santa Paula",
            "Simi Valley", "Thousand Oaks", "Ventura"};

    public VenturaCountyMap()
    {
        this.countyMap = new UndirectedGraph<String>();
        drawMap();
    }

    /**
     *
     * @param destination name of the destination vertex
     * @param start name of the starting vertex
     * @param stack returned with all the vertices that exist along the cheapest (shortest) path between the given vertices
     * @return the cost (total distance in miles) of the cheapest (shortest) path between two given users
     */
    public double getCheapestPath(String destination, String start, Stack stack)
    {
        return this.countyMap.getCheapestPath(start, destination, stack);
    }

    /**
     *
     * @param begin vertex
     * @param end vertex
     * @return true if there exists an edge between the given vertices
     */
    public boolean hasEdge(String begin, String end)
    {
        return this.countyMap.hasEdge(begin, end);
    }

    /**
     * build the vertices and edges of the Ventura County map
     */
    private void drawMap()
    {
        //vertices of countyMap are in all lower case
        for (int i = 0; i < CITIES_OF_VENTURA_COUNTY.length; i++) {
            countyMap.addVertex(CITIES_OF_VENTURA_COUNTY[i].toLowerCase());
        }

        countyMap.addEdge("camarillo", "oxnard", 9);
        countyMap.addEdge("camarillo", "santa paula", 18);
        countyMap.addEdge("camarillo", "moorpark", 11);
        countyMap.addEdge("camarillo", "thousand oaks", 13);

        countyMap.addEdge("fillmore", "santa paula", 9);
        countyMap.addEdge("fillmore", "moorpark", 11);

        countyMap.addEdge("moorpark", "thousand oaks", 12);
        countyMap.addEdge("moorpark", "camarillo", 11);
        countyMap.addEdge("moorpark", "santa paula", 18);
        countyMap.addEdge("moorpark", "fillmore", 11);
        countyMap.addEdge("moorpark", "simi valley", 8);

        countyMap.addEdge("ojai", "ventura", 22);
        countyMap.addEdge("ojai", "santa paula", 16);

        countyMap.addEdge("oxnard", "camarillo", 9);
        countyMap.addEdge("oxnard", "ventura", 9);
        countyMap.addEdge("oxnard", "port hueneme", 4);

        countyMap.addEdge("port hueneme", "oxnard", 4);
        countyMap.addEdge("port hueneme", "thousand Oaks", 23);

        countyMap.addEdge("santa paula", "ojai", 16);
        countyMap.addEdge("santa paula", "ventura", 13);
        countyMap.addEdge("santa paula", "camarillo", 18);
        countyMap.addEdge("santa paula", "fillmore", 9);
        countyMap.addEdge("santa paula", "moorpark", 18);

        countyMap.addEdge("simi valley", "moorpark", 8);
        countyMap.addEdge("simi valley", "thousand oaks", 12);

        countyMap.addEdge("thousand oaks", "port hueneme", 23);
        countyMap.addEdge("thousand oaks", "camarillo", 13);
        countyMap.addEdge("thousand oaks", "moorpark", 12);
        countyMap.addEdge("thousand oaks", "simi valley", 12);

        countyMap.addEdge("ventura", "ojai", 22);
        countyMap.addEdge("ventura", "santa paula", 13);
        countyMap.addEdge("ventura", "oxnard", 9);
    }
}
