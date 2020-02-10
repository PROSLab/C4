package com.spm.api.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Graph<T> { 

	// We use Hashmap to store the edges in the graph 
	public Map<T, HashMap<T,String> > map = new HashMap<>(); 
	public HashSet<Node> unvisited = new HashSet<Node>(); 


	public int getNodeId() {
		return map.size()+1;
	}

	// This function adds a new vertex to the graph 
	public void addVertex(T s) 
	{ 
		map.put(s, new HashMap<T,String>());

	} 

	// This function adds the edge 
	// between source to destination 
	public boolean addEdge(T source, 
			T destination,
			String label) 
	{ 
		boolean addedNewVertex =false;
		if (!map.containsKey(source)) {
			addVertex(source); 
			addedNewVertex=true;
		}


		if (!map.containsKey(destination)) {
			addVertex(destination);
			addedNewVertex=true;
		}
		if (!addedNewVertex) {
			for (T node : map.keySet()) {
				if (node.equals(destination)) {
					map.get(source).put(node, label);
//					System.out.println(node+"____"+destination);
					break;
				}
			}

		}else {
			map.get(source).put(destination, label);
		}
		return addedNewVertex;
	} 

	// This function gives the count of vertices 
	public void getVertexCount() 
	{ 
		System.out.println("The graph has "
				+ map.keySet().size() 
				+ " vertex"); 
	} 

	// This function gives the count of edges 
	public void getEdgesCount(boolean bidirection) 
	{ 
		int count = 0; 
		for (T v : map.keySet()) { 
			count += map.get(v).size(); 
		} 
		if (bidirection == true) { 
			count = count / 2; 
		} 
		System.out.println("The graph has "
				+ count 
				+ " edges."); 
	} 

	// This function gives whether 
	// a vertex is present or not. 
	public void hasVertex(T s) 
	{ 
		if (map.containsKey(s)) { 
			System.out.println("The graph contains "
					+ s + " as a vertex."); 
		} 
		else { 
			System.out.println("The graph does not contain "
					+ s + " as a vertex."); 
		} 
	} 

	// This function gives whether an edge is present or not. 
	public void hasEdge(T s, T d) 
	{ 
		if (map.get(s).containsKey(d)) { 
			System.out.println("The graph has an edge between "
					+ s + " and " + d + "."); 
		} 
		else { 
			System.out.println("The graph has no edge between "
					+ s + " and " + d + "."); 
		} 
	} 

	// Prints the adjancency list of each vertex. 
	@Override
	public String toString() 
	{ 
		StringBuilder builder = new StringBuilder(); 

		for (T v : map.keySet()) { 
			builder.append(v.toString() + ": "); 
			for (T w : map.get(v).keySet()) {
				builder.append(map.get(v).get(w)+ w.toString() ); 
			}

			builder.append("\n"); 
		}
		return (builder.toString()); 
	} 
} 