package com.spm.api.utils;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.stream.events.EndElement;

import org.camunda.bpm.engine.impl.bpmn.diagram.ProcessDiagramLayoutFactory;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.impl.instance.EventBasedGatewayImpl;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.BpmnModelElementInstance;
import org.camunda.bpm.model.bpmn.instance.BusinessRuleTask;
import org.camunda.bpm.model.bpmn.instance.EndEvent;
import org.camunda.bpm.model.bpmn.instance.EventBasedGateway;
import org.camunda.bpm.model.bpmn.instance.ExclusiveGateway;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.InclusiveGateway;
import org.camunda.bpm.model.bpmn.instance.IntermediateCatchEvent;
import org.camunda.bpm.model.bpmn.instance.IntermediateThrowEvent;
import org.camunda.bpm.model.bpmn.instance.ManualTask;
import org.camunda.bpm.model.bpmn.instance.MessageFlow;
import org.camunda.bpm.model.bpmn.instance.ParallelGateway;
import org.camunda.bpm.model.bpmn.instance.ReceiveTask;
import org.camunda.bpm.model.bpmn.instance.ScriptTask;
import org.camunda.bpm.model.bpmn.instance.SendTask;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.bpmn.instance.SubProcess;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.graphstream.graph.Edge;
import org.graphstream.graph.implementations.SingleGraph;

//<<<<<<< HEAD
import com.spm.api.utils.*;
import com.spm.api.utils.LTSNode.NodeType;

import javafx.util.Pair;


import com.spm.api.utils.LTSNode;

/*
=======
import utils.LTSNode;
import utils.LTSNode.NodeType;
import core.utils.*;
>>>>>>> 8a8f222e8099fb7026976e124303d07c8a41bceb*/
import org.camunda.bpm.model.bpmn.impl.instance.FlowNodeImpl;




public class Collaboration {
	Graph<Node> LTS;
	private BpmnModelInstance modelInstance;
	private SingleGraph collaborationGraph;
	private Collection<String> choreographyActions;
	public static  Integer  collaborationCounter;
	public static ArrayList<LTSNode> nodeSet;
	public static String collaborationPath;



	/*public static void main(String[] args) {
		Collaboration collaboration = new Collaboration();
		JFileChooser chooser = new JFileChooser("/home/cippus/Documents/testinput");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("BPMN", "bpmn");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
		}
		File file = new File(chooser.getSelectedFile().getAbsolutePath());
//		File file=new File("/home/cippus/Documents/testinput/collaborationPAR.bpmn");
		System.out.println(file.getAbsolutePath());
		//collaboration.init(file, true);


	}*/

	public void init(InputStream file, boolean showGraph, File outputFile) {

		modelInstance = Bpmn.readModelFromStream(file);
		Node root = new Node();
		// ELEMENTS COUNT
		int elements = 0;
		for (org.camunda.bpm.model.bpmn.instance.Process p : modelInstance
				.getModelElementsByType(org.camunda.bpm.model.bpmn.instance.Process.class)) {
			elements += p.getChildElementsByType(FlowNode.class).size();
		}

		for (StartEvent startEvent : modelInstance.getModelElementsByType(StartEvent.class)) {

			if (!(startEvent.getParentElement() instanceof SubProcess) && getMessageFlow(startEvent) == null) {
				for (SequenceFlow s : startEvent.getOutgoing()) {
					root.addEdge(s);
				}
			}
		}

		long elapsed = System.nanoTime();
		LTS.addVertex(root);
		LTS.unvisited.add(root);
		BFS();

		// System.out.println(LTS.toString());
		SingleGraph graph = showGraph();
		collaborationPath = Utils.generatemAUTFile(graph, outputFile);
		if (showGraph) {
			Utils.showGraph(graph);

		}
		System.out.println("Collaboration State Space dimension: "+LTS.map.size());
		elapsed = System.nanoTime() - elapsed;


	}
	public SingleGraph showGraph() {
		SingleGraph graphStream = Utils.GenerateGraph("Collaboration");
		for (Node n : LTS.map.keySet()) {
			graphStream.addNode(String.valueOf(n.getId())).setAttribute("ui.label", n.getId());
		}
		int i = 0;
		for (Node n : LTS.map.keySet()) {
			for (Node child : LTS.map.get(n).keySet()) {
				if (child != null && n != null) {
					graphStream.addEdge(String.valueOf(i++), String.valueOf(n.getId()), String.valueOf(child.getId()),
							true).setAttribute("ui.label", LTS.map.get(n).get(child));
				}

			}
		}

		return graphStream;

	}

	public Collaboration() {
		LTS = new Graph<Node>();
		collaborationCounter=0;
		nodeSet=new ArrayList<LTSNode>();
	}

	private void BFS() {
		Iterator it = LTS.unvisited.iterator();
		int i = 0;
		while (it.hasNext()) {
			Node currentNode = (Node) it.next();
			// System.out.print("\n CurrentNode:"+currentNode+"\n");
			// for (SequenceFlow s : currentNode.getReachabilityPath()) {
			// System.out.print("Reachability Path "+s.getId()+", ");
			// }
			for (SequenceFlow sequenceFlow : currentNode.edges.keySet()) {
				FlowNode nextBPMNElement = sequenceFlow.getTarget();
				String sequenceFlowLabel="tau";
				//				String sequenceFlowLabel=sequenceFlow.getId();

				Pair<MessageFlow, Boolean> messageFlowInvolved = getMessageFlow(nextBPMNElement);

				// System.out.print("\n CurrentNode:"+currentNode+"
				// "+sequenceFlow.getSource().getId()+" "+sequenceFlowLabel+"
				// "+nextBPMNElement.getId());

				// ENDEVEND
				if (nextBPMNElement instanceof EndEvent && messageFlowInvolved == null) {
					Node nextNode;
					if (nextBPMNElement.getParentElement() instanceof SubProcess) {
						nextNode = generateNode(currentNode, sequenceFlow, null, sequenceFlowLabel);
						boolean isFinisced = true;
						for (SequenceFlow sequencesNextNode : nextNode.edges.keySet()) {
							if (sequencesNextNode.getParentElement() instanceof SubProcess) {
								isFinisced = false;
								break;
							}
						}
						if (isFinisced) {
							SubProcess s = (SubProcess) nextBPMNElement.getParentElement();
							for (SequenceFlow sequenceOutSubprocess : s.getOutgoing()) {
								nextNode.addEdge(sequenceOutSubprocess);
							}
						}

					} else {
						nextNode = generateNode(currentNode, sequenceFlow, null, sequenceFlowLabel);
					}
					if (LTS.addEdge(currentNode, nextNode, sequenceFlowLabel)) {
						LTS.unvisited.add(nextNode);
					}

				}

				// SEND MESSAGE ELEMENT
				else if (messageFlowInvolved != null && messageFlowInvolved.getValue()) {
					for (SequenceFlow outgoingSequence : nextBPMNElement.getOutgoing()) {
						Node nextNode = generateNode(currentNode, sequenceFlow, outgoingSequence, sequenceFlowLabel);
						if (messageFlowInvolved.getKey().getTarget() instanceof StartEvent) {
							nextNode.addEdge(Utils.createSequenceFlow(modelInstance, nextBPMNElement,
									(FlowNode) messageFlowInvolved.getKey().getTarget()));
						}

						nextNode.addMessages(messageFlowInvolved.getKey());

						if (LTS.addEdge(currentNode, nextNode, sequenceFlowLabel)) {
							LTS.unvisited.add(nextNode);
						}

					}
					if (nextBPMNElement instanceof EndEvent) {
						Node nextNode = generateNode(currentNode, sequenceFlow, null, sequenceFlowLabel);
						if (messageFlowInvolved.getKey().getTarget() instanceof StartEvent) {
							nextNode.addEdge(Utils.createSequenceFlow(modelInstance, nextBPMNElement,
									(FlowNode) messageFlowInvolved.getKey().getTarget()));
						}

						nextNode.addMessages(messageFlowInvolved.getKey());

						if (LTS.addEdge(currentNode, nextNode, sequenceFlowLabel)) {
							LTS.unvisited.add(nextNode);
						}
					}
				}
				// RECEIVE MESSAGE ELEMENT
				else if (messageFlowInvolved != null && !messageFlowInvolved.getValue()) {

					if (choreographyActions.contains(messageFlowInvolved.getKey().getName())) {
						sequenceFlowLabel = messageFlowInvolved.getKey().getSource().getParentElement()
								.getAttributeValue("id") + "->"
								+ messageFlowInvolved.getKey().getTarget().getParentElement().getAttributeValue("id")
								+ ":" + messageFlowInvolved.getKey().getName();
					}

					if (currentNode.messages.containsKey(messageFlowInvolved.getKey())) {
						for (SequenceFlow outgoingSequence : nextBPMNElement.getOutgoing()) {

							Node nextNode;
							// Remove all other sequence flow outgoing from the eventbase
							if (sequenceFlow.getSource() instanceof EventBasedGateway) {
								nextNode = generateNode(currentNode, null, outgoingSequence, sequenceFlowLabel);
								for (SequenceFlow otherSequence : ((EventBasedGateway) sequenceFlow.getSource())
										.getOutgoing()) {
									nextNode.removeEdge(otherSequence);
								}
							} else {
								nextNode = generateNode(currentNode, sequenceFlow, outgoingSequence, sequenceFlowLabel);

							}
							nextNode.decreaseMessage(messageFlowInvolved.getKey());
							if (LTS.addEdge(currentNode, nextNode, sequenceFlowLabel)) {
								LTS.unvisited.add(nextNode);

							}

							// manca il caso dell'event base
						}
					}

				}

				// INTERMEDIATE CATCH EVENT
				else if (nextBPMNElement instanceof IntermediateCatchEvent) {
					for (SequenceFlow outgoingSequence : nextBPMNElement.getOutgoing()) {

						Node nextNode;
						// Remove all other sequence flow outgoing from the eventbase
						if (sequenceFlow.getSource() instanceof EventBasedGateway) {
							nextNode = generateNode(currentNode, null, outgoingSequence, sequenceFlowLabel);
							for (SequenceFlow otherSequence : ((EventBasedGateway) sequenceFlow.getSource())
									.getOutgoing()) {
								nextNode.removeEdge(otherSequence);
							}
						} else {
							nextNode = generateNode(currentNode, sequenceFlow, outgoingSequence, sequenceFlowLabel);

						}

						if (LTS.addEdge(currentNode, nextNode, sequenceFlowLabel)) {
							LTS.unvisited.add(nextNode);

						}
					}
				}
				// PARALLEL GATEWAY AND EVENT BASED GATEWAY
				else if (nextBPMNElement instanceof ParallelGateway || nextBPMNElement instanceof EventBasedGateway) {
					// SPLIT
					if (nextBPMNElement.getIncoming().size() == 1) {
						Node nextNode = generateNode(currentNode, sequenceFlow, null, sequenceFlowLabel);
						for (SequenceFlow outgoingSequence : nextBPMNElement.getOutgoing()) {
							nextNode.addEdge(outgoingSequence);
						}

						if (LTS.addEdge(currentNode, nextNode, sequenceFlowLabel)) {
							LTS.unvisited.add(nextNode);
						}
					}

					// CONVERGING
					if (nextBPMNElement.getOutgoing().size() == 1) {
						int executed = 0;
						for (SequenceFlow incoming : nextBPMNElement.getIncoming()) {
							if (currentNode.getReachabilityPath().contains(incoming)) {
								executed++;
							}
						}
						if (executed == nextBPMNElement.getIncoming().size() - 1) {
							Node nextNode = generateNode(currentNode, sequenceFlow, null, sequenceFlowLabel);
							for (SequenceFlow outgoingEdge : nextBPMNElement.getOutgoing()) {
								nextNode.addEdge(outgoingEdge);
							}
							if (LTS.addEdge(currentNode, nextNode, sequenceFlowLabel)) {
								LTS.unvisited.add(nextNode);
							}
						} else {
							Node nextNode = generateNode(currentNode, sequenceFlow, null, sequenceFlowLabel);
							if (LTS.addEdge(currentNode, nextNode, sequenceFlowLabel)) {
								LTS.unvisited.add(nextNode);
							}
						}
					}
				}

				else if (nextBPMNElement instanceof SubProcess) {
					for (StartEvent start : nextBPMNElement.getChildElementsByType(StartEvent.class)) {
						for (SequenceFlow outgoing : start.getOutgoing()) {
							Node nextNode = generateNode(currentNode, sequenceFlow, outgoing, sequenceFlowLabel);
							if (LTS.addEdge(currentNode, nextNode, sequenceFlowLabel)) {
								LTS.unvisited.add(nextNode);

							}
						}
					}
				}

				else {
					for (SequenceFlow outgoingSequence : nextBPMNElement.getOutgoing()) {
						Node nextNode = generateNode(currentNode, sequenceFlow, outgoingSequence, sequenceFlowLabel);
						if (LTS.addEdge(currentNode, nextNode, sequenceFlowLabel)) {
							LTS.unvisited.add(nextNode);

						}

					}
				}
			}
			LTS.unvisited.remove(currentNode);
			it = LTS.unvisited.iterator();
			i++;
		}

	}






	public void setChoreographyActions(ArrayList<String> choreographyActions2) {
		this.choreographyActions=choreographyActions2;
	}

	// the boolean return indicate if the element isSource TRUE for source
	public Pair<MessageFlow, Boolean> getMessageFlow(FlowNode currentElement) {
		for (MessageFlow message : modelInstance.getModelElementsByType(MessageFlow.class)) {
			if (message.getSource().getId().equals(currentElement.getId())) {
				return new Pair<MessageFlow, Boolean>(message, true);
			} else if (message.getTarget().getId().equals(currentElement.getId())) {
				return new Pair<MessageFlow, Boolean>(message, false);
			}
		}
		return null;

	}

	public Node generateNode(Node currentNode, SequenceFlow remove, SequenceFlow add, String sequenceFlowLabel) {
		Node nextNode = currentNode.clone();
		nextNode.addReachabilityPath(remove);
		if (remove != null) {
			nextNode.removeEdge(remove);
		}

		if (add != null) {
			nextNode.addEdge(add);
		}
		nextNode.setId(LTS.getNodeId());
		return nextNode;
	}

}
