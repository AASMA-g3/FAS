package pt.ulisboa.aasma.fas.bootstrap;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;


/**
 * This class has the purpose of simplifying the sending of a message
 */
public class MessageManager {
	private Agent myAgent ;
	
	/**
	 * To send a message we will need a reference to the sender agent
	 * @param a the reference to the sender agent
	 */
	public MessageManager(Agent a){
		myAgent=a;		
	}

	/**
	 * this is how to reply to a message
	 * */
	public void SendReply(ACLMessage msg,String content){
		ACLMessage reply = msg.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setContent(content);
		myAgent.send(reply);

	}
	/**
	 * this is how to send a message
	 * */
	public void SendMessage(String receiver, String content, String convId){
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setContent(content);
		msg.setConversationId(convId);
		msg.addReceiver(new AID(receiver, AID.ISLOCALNAME));

		myAgent.send(msg);

	}
	

}
