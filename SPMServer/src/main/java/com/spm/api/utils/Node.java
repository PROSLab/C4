package com.spm.api.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import org.camunda.bpm.model.bpmn.instance.MessageFlow;
import org.camunda.bpm.model.bpmn.instance.Participant;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;


public class Node {
	public HashMap<SequenceFlow,Integer> edges;
	public HashMap<MessageFlow,Integer> messages;
	public int id;
	private ArrayList<SequenceFlow> reachabilityPath;


	public Node() {
		this.edges = new HashMap<SequenceFlow, Integer>();
		this.messages = new HashMap<MessageFlow, Integer>();
		this.reachabilityPath=new ArrayList<SequenceFlow>();
	}

	public void addEdge(SequenceFlow key){
		if (edges.containsKey(key)) {
			Integer value=edges.get(key);
			if (value<3) {
				edges.replace(key, value+1);
			}

		}else{
			edges.put(key, 1);
		}
	}
	public void removeEdge(SequenceFlow key){
		if (edges.get(key)!=null) {
			if(edges.get(key)==1){
				edges.remove(key);
			}else{
				Integer value=edges.get(key);
				edges.replace(key, value-1);
			}
		}

	}

	public void addMessages(MessageFlow key){
		if (messages.containsKey(key)) {
			Integer value=messages.get(key);
			if (value<3) {
				messages.replace(key, value+1);
			}
		}else{
			messages.put(key, 1);
		}

	}

	public boolean decreaseMessage(MessageFlow key){
		if (messages.containsKey(key)){
			if (messages.get(key) == 1) {
				messages.remove(key);
				return true;
			} else if (messages.get(key) > 1) {
				Integer value = messages.get(key);
				messages.replace(key, value - 1);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}


	public List<SequenceFlow> getReachabilityPath() {
		return reachabilityPath;
	}

	public void addReachabilityPath(SequenceFlow reachabilityPath) {
		this.reachabilityPath.add(reachabilityPath);
	}


	@Override
	public String toString() {
		String edge = "",   message = "";
		for (SequenceFlow key : edges.keySet()){
			edge+= key.getId()+"_"+edges.get(key)+", ";
		}

		for (MessageFlow key : messages.keySet()) {
			message+= key.getId()+"_"+messages.get(key)+", ";
		}



		return "[ID="+ id+" edges=" + edge + "; messages=" + message  + "]";
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public Node clone(){
		Node returnNode=new Node();
		returnNode.edges=(HashMap<SequenceFlow, Integer>) this.edges.clone();
		returnNode.messages=(HashMap<MessageFlow, Integer>) this.messages.clone();
		returnNode.reachabilityPath=(ArrayList<SequenceFlow>) this.reachabilityPath.clone();
		return returnNode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((edges == null) ? 0 : edges.hashCode());
		result = prime * result + ((messages == null) ? 0 : messages.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (edges == null) {
			if (other.edges != null)
				return false;
		} else if (!edges.equals(other.edges))
			return false;
		if (messages == null) {
			if (other.messages != null)
				return false;
		} else if (!messages.equals(other.messages))
			return false;
		return true;
	}

}
