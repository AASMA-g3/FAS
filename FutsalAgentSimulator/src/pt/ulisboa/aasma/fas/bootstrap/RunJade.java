package pt.ulisboa.aasma.fas.bootstrap;


import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;


/**
 * This class starts the Jade framework and/or aditional containers on an existing one
 * feel free to extend/expand it if you want more functionality from it
 */

public class RunJade {
	private jade.core.Runtime runtime = jade.core.Runtime.instance();
	private ContainerController home=null;
	private Profile p = new ProfileImpl();
	private String PORT="1099";

	/**
	 * @return Returns the ContainerController of the container created in the constructor.
	 */
	public ContainerController getHome() {
		return home;
	}


	public RunJade(boolean cuInterfata,boolean main, String host, String hostPort, String port){
		if(checkPort(port, "local"))PORT=port;
		if(main) runMainJade(cuInterfata);
		else runAuxJade(host, port);

	}

	/**
	 * Creates a JADE platform and it's MainContainer 
	 */
	public RunJade(boolean cuInterfata, String port){
		if(checkPort(port, "local"))PORT=port;
		runMainJade(cuInterfata);
	}

	/**
	 * Creates an auxiliary container to the platform running on host:hostPort (where the MainContainer can be found)
	 */
	public RunJade( String host, String hostPort, String port){
		if(checkPort(port, "local"))PORT=port;
		runAuxJade(host, hostPort);

	}

	protected void runMainJade(boolean cuInterfata){
		p.setParameter(p.LOCAL_PORT, PORT);
		home= runtime.createMainContainer(p);

		if(cuInterfata){

			try {
				AgentController rma =home.createNewAgent("rma",	"jade.tools.rma.rma", new Object[0]);
				rma.start();
			} catch (StaleProxyException e) {
				e.printStackTrace();
			}

		}
	}

	protected void runAuxJade(String host, String hostPort){
		if(checkIP(host)){
			p.setParameter(p.MAIN, "false"); 
			p.setParameter(p.LOCAL_PORT, PORT); 
			if(checkPort(hostPort,"host"))
				p.setParameter(p.MAIN_PORT, hostPort); 
			p.setParameter(p.MAIN_HOST, host);  
			home = runtime.createAgentContainer(p);
		}
	}



	/**
	 * Checks the validity of a string that should specify a port. Doest not check the availability of the port.
	 * 
	 * @param s the String representing the port
	 * @return false if the String is not a number or is null or void.
	 */
	private boolean checkPort(String s, String portType){		
		if(s==null){
			System.out.println("The specified "+portType+" port is null. Running JADE on default port.");
			return false;
		}
		if(s.equals("")){
			System.out.println("The specified "+portType+" port is a void string. Running JADE on default port.");
			return false;
		}
		try{
			int x=Integer.parseInt(s);
			if(x>65535 || x<0){
				System.out.println("The specified "+portType+" port is not a number in the interval [0,65535]. Running JADE on default port.");
				return false;
			}
		}catch(NumberFormatException e){
			System.out.println("The specified "+portType+" port is not a number. Running JADE on default port.");
			return false;
		}		
		return true;
	}

	/**
	 * Checks the validity of a string that should specify an IP. Does not check the actual IP just the String.
	 * 
	 * @param s the String representing the IP
	 * @return false if the String is not a number or is null or void or represents a wrong number of bytes.
	 */
	private boolean checkIP(String s){		
		if(s==null){
			System.out.println("The specified IP is null.");
			return false;
		}

		if(s.equals("")){
			System.out.println("The specified IP is a void string.");
			return false;
		}

		String[] s1=s.split("[.]");
		if(s1.length!=4 && s1.length!=6) {
			System.out.println("The specified IP has "+s1.length+" bytes. It should have 4 or 6.");
			return false;
		}
		for(int i=0;i<s1.length;i++){
			try{
				int x= Integer.parseInt(s1[i]);
				if(x<0 || x>255){
					System.out.println("One of the bytes of the specified IP is not a number in the interval [0,255].");
					return false;
				}
			}catch(NumberFormatException e){
				System.out.println("One of the bytes of the specified IP is not a number.");
				return false;
			}		
		}
		return true;
	}



}
