package pt.ulisboa.aasma.fas.j2d;

import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pt.ulisboa.aasma.fas.bootstrap.RunJade;
import pt.ulisboa.aasma.fas.jade.agents.BallAgent;
import pt.ulisboa.aasma.fas.jade.agents.ReporterAgent;
import pt.ulisboa.aasma.fas.jade.agents.bdi.DefenderAgentBDI;
import pt.ulisboa.aasma.fas.jade.agents.bdi.KeeperAgentBDI;
import pt.ulisboa.aasma.fas.jade.agents.bdi.StrikerAgentBDI;
import pt.ulisboa.aasma.fas.jade.agents.reactive.DefenderAgentReactive;
import pt.ulisboa.aasma.fas.jade.agents.reactive.KeeperAgentReactive;
import pt.ulisboa.aasma.fas.jade.agents.reactive.StrikerAgentReactive;
import pt.ulisboa.aasma.fas.jade.game.Game;
import pt.ulisboa.aasma.fas.jade.game.Player;


public class MenuFrame extends JFrame {
	
	private static RunJade r;
	private static ContainerController home;
	
	private static GameRunner gr;
	private static MenuFrame mf;
	private Game currentGame;
	
	private static final int MIN_ATTR = 0;
	private static final int MED_ATTR = 5;
	private static final int MAX_ATTR = 10;
	
	private static final int TEAM_A = 0;
	private static final int TEAM_B = 1;
	
	private static final int REACTIVE = 2;
	private static final int HYBRID = 1;
	private static final int DELIBERATIVE = 0;
	
	//JPanel
	private JPanel pnlControl = new JPanel();
	//Buttons
	private JButton btnRestart = new JButton("Reset");
	private JButton btnRun = new JButton("Run!");

	public final AtomicBoolean runPressed = new AtomicBoolean(false);
	public final AtomicBoolean restartPressed = new AtomicBoolean(false);
	
	private ArrayList<Integer> sliders = new ArrayList<Integer>();
	
	/*ARGUMENTS REPRESENTING EACH TEAM MODE
	  sliders_status[0] - get team A status
	  sliders_status[1] - get team B status
	  if attribute = 1 -> the team has a reactive behaviour
	  if attribute = 2 -> the team has a hybrid behaviour
	  if attribute = 3 -> the team has a deliberative behaviour*/
	private ArrayList<Integer> sliders_status = new ArrayList<Integer>();
	
	private boolean gameInProgress = false;
	
	
	public MenuFrame(){
		gr = new GameRunner();
		
		  r = new RunJade(true, "30000");
		  home = r.getHome();
			 
		
		 //ControlPanel setbounds
	    btnRestart.setBounds(60, 400, 220, 30);
	    btnRun.setBounds(60, 400, 220, 30);

	    //JPanel bounds
	    pnlControl.setBounds(1000, 1000, 200, 100);

	    for (int i=0; i<10; i++){
	    	sliders.add(new Integer(MED_ATTR));
	    }
	    
	    sliders_status.add(new Integer(2));
	    sliders_status.add(new Integer(2));
	   
	    Box boxGK = Box.createVerticalBox();
	    Box boxDef = Box.createVerticalBox();
	    Box boxAtk = Box.createVerticalBox();
	    Box boxDri = Box.createVerticalBox();
	    Box boxPas = Box.createVerticalBox();
	    Box boxStat = Box.createVerticalBox();

	    JSlider s1a = sliderCreate(MIN_ATTR, MAX_ATTR, MED_ATTR, "GK TeamA", boxGK);
	    boxGK.add(new JLabel(" "));
	    boxGK.add(new JLabel(" "));
	    boxGK.add(new JLabel(" "));
	    JSlider s1b = sliderCreate(MIN_ATTR, MAX_ATTR, MED_ATTR, "GK TeamB", boxGK);
	    JSlider s2a = sliderCreate(MIN_ATTR, MAX_ATTR, MED_ATTR, "Def TeamA", boxDef);
	    boxDef.add(new JLabel(" "));
	    boxDef.add(new JLabel(" "));
	    boxDef.add(new JLabel(" "));
	    JSlider s2b = sliderCreate(MIN_ATTR, MAX_ATTR, MED_ATTR, "Def TeamB", boxDef);
	    JSlider s3a = sliderCreate(MIN_ATTR, MAX_ATTR, MED_ATTR, "Atk TeamA", boxAtk);
	    boxAtk.add(new JLabel(" "));
	    boxAtk.add(new JLabel(" "));
	    boxAtk.add(new JLabel(" "));
	    JSlider s3b = sliderCreate(MIN_ATTR, MAX_ATTR, MED_ATTR, "Atk TeamB", boxAtk);
	    JSlider s4a = sliderCreate(MIN_ATTR, MAX_ATTR, MED_ATTR, "Dri TeamA", boxDri);
	    boxDri.add(new JLabel(" "));
	    boxDri.add(new JLabel(" "));
	    boxDri.add(new JLabel(" "));
	    JSlider s4b = sliderCreate(MIN_ATTR, MAX_ATTR, MED_ATTR, "Dri TeamB", boxDri);
	    JSlider s5a = sliderCreate(MIN_ATTR, MAX_ATTR, MED_ATTR, "Pas TeamA", boxPas);
	    boxPas.add(new JLabel(" "));
	    boxPas.add(new JLabel(" "));
	    boxPas.add(new JLabel(" "));
	    JSlider s5b = sliderCreate(MIN_ATTR, MAX_ATTR, MED_ATTR, "Pas TeamB", boxPas);
	    
	    
	    JSlider s6a = sliderCreate(0, 2, REACTIVE, "Agents TeamA", boxStat);

		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put( new Integer( DELIBERATIVE ), new JLabel("DELIBERATIVE"));
		labelTable.put( new Integer( HYBRID ), new JLabel("HYBRID") );
		labelTable.put( new Integer( REACTIVE ), new JLabel("REACTIVE") );
		s6a.setLabelTable(labelTable);
		
	    boxStat.add(new JLabel(" "));
	    boxStat.add(new JLabel(" "));
	    boxStat.add(new JLabel(" "));
	    
	    JSlider s6b = sliderCreate(0, 2, REACTIVE, "Agents TeamB", boxStat);
		s6b.setLabelTable(labelTable);



	    //Adding to JFrame
	    Box btnBox = Box.createVerticalBox();
	    btnBox.add(btnRun, BorderLayout.CENTER);
	    btnBox.add(btnRestart, BorderLayout.CENTER);
	    pnlControl.add(boxGK);
	    pnlControl.add(boxDef);
	    pnlControl.add(boxAtk);
	    pnlControl.add(boxDri);
	    pnlControl.add(boxPas);
	    pnlControl.add(boxStat);
	    pnlControl.add(btnBox);
	    add(pnlControl, BorderLayout.CENTER);
	    
		btnRestart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (gameInProgress) {
					currentGame.isEnded.set(true);
					gr.setGameInProgress(false);
					
					
					AgentController a;
					try {
						a = home.getAgent("Reporter");
						a.kill();
						a = home.getAgent("Ball");
						a.kill();
					
						for (Player player : currentGame.getTeamA()) {
							a = home.getAgent(player.getName());
							a.kill();
						}

						for (Player player : currentGame.getTeamB()) {
							a = home.getAgent(player.getName());
							a.kill();
						}
						
					
					} catch (ControllerException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					gameInProgress = false;
				} else {
					System.out.println("No game in progress.");
				}
			}} );
		this.btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if((sliders_status.get(TEAM_A).intValue() == HYBRID) ||
						(sliders_status.get(TEAM_B).intValue() == HYBRID)){
					System.out.println("Hybrid not implemented. Please change agent type and reset.");
					return;
				}
				
				if(!gameInProgress){
				currentGame = new Game(getSliders());

				gr.startGame(currentGame);

				Object[] agentParams = { currentGame };
				
				
				
				try {
					AgentController a;

					a = home.createNewAgent("Reporter",
							ReporterAgent.class.getName(),
							agentParams);
					a.start();
					
					
					if(sliders_status.get(TEAM_A).intValue() == REACTIVE){
						for (Player player : currentGame.getTeamA()) {
							if(player.getPosition().equals(Player.KEEPER)){
								KeeperAgentReactive pa = new KeeperAgentReactive(currentGame, player);
								a = home.acceptNewAgent(player.getName(), (Agent)pa);
							} else if (player.getPosition().equals(Player.DEFENDER)){
								DefenderAgentReactive pa = new DefenderAgentReactive(currentGame, player);
								a = home.acceptNewAgent(player.getName(), (Agent)pa);
							} else if (player.getPosition().equals(Player.STRIKER)){
								StrikerAgentReactive pa = new StrikerAgentReactive(currentGame, player);
								a = home.acceptNewAgent(player.getName(), (Agent)pa);
							}
							a.start();
						}
					} else if (sliders_status.get(TEAM_A).intValue() == DELIBERATIVE){
						for (Player player : currentGame.getTeamA()) {
							if(player.getPosition().equals(Player.KEEPER)){
								KeeperAgentBDI pa = new KeeperAgentBDI(currentGame, player);
								a = home.acceptNewAgent(player.getName(), (Agent)pa);
							} else if (player.getPosition().equals(Player.DEFENDER)){
								DefenderAgentBDI pa = new DefenderAgentBDI(currentGame, player);
								a = home.acceptNewAgent(player.getName(), (Agent)pa);
							} else if (player.getPosition().equals(Player.STRIKER)){
								StrikerAgentBDI pa = new StrikerAgentBDI(currentGame, player);
								a = home.acceptNewAgent(player.getName(), (Agent)pa);
							}
							a.start();
						}
					} else {
						System.out.println("Hybrid not implemented. Please change teamA agent type and reset.");
					}

					
					if(sliders_status.get(TEAM_B).intValue() == REACTIVE){
						for (Player player : currentGame.getTeamB()) {
							if(player.getPosition().equals(Player.KEEPER)){
								KeeperAgentReactive pa = new KeeperAgentReactive(currentGame, player);
								a = home.acceptNewAgent(player.getName(), (Agent)pa);
							} else if (player.getPosition().equals(Player.DEFENDER)){
								DefenderAgentReactive pa = new DefenderAgentReactive(currentGame, player);
								a = home.acceptNewAgent(player.getName(), (Agent)pa);
							} else if (player.getPosition().equals(Player.STRIKER)){
								StrikerAgentReactive pa = new StrikerAgentReactive(currentGame, player);
								a = home.acceptNewAgent(player.getName(), (Agent)pa);
							}
							a.start();
						}
					} else if (sliders_status.get(TEAM_B).intValue() == DELIBERATIVE){
						for (Player player : currentGame.getTeamB()) {
							if(player.getPosition().equals(Player.KEEPER)){
								KeeperAgentBDI pa = new KeeperAgentBDI(currentGame, player);
								a = home.acceptNewAgent(player.getName(), (Agent)pa);
							} else if (player.getPosition().equals(Player.DEFENDER)){
								DefenderAgentBDI pa = new DefenderAgentBDI(currentGame, player);
								a = home.acceptNewAgent(player.getName(), (Agent)pa);
							} else if (player.getPosition().equals(Player.STRIKER)){
								StrikerAgentBDI pa = new StrikerAgentBDI(currentGame, player);
								a = home.acceptNewAgent(player.getName(), (Agent)pa);
							}
							a.start();
						}
					} else {
						System.out.println("Hybrid not implemented. Please change teamB agent type and reset.");
					}

					
					
					
					a = home.createNewAgent("Ball",
							BallAgent.class.getName(),
							agentParams);
					a.start();

				} catch (StaleProxyException spe) {
					spe.printStackTrace();
				}
				gameInProgress = true;
				} else{
					System.out.println("Game in progress. Wait for end or reset.");
				}
			}
		});
	    
	    s1a.addChangeListener(new ChangeListener(){
	    	public void stateChanged(ChangeEvent e) {
	    		JSlider source = (JSlider)e.getSource();
	    		sliders.set(0, source.getValue());
	    	}
	    });
	    
	    s1b.addChangeListener(new ChangeListener(){
	    	public void stateChanged(ChangeEvent e) {
	    		JSlider source = (JSlider)e.getSource();
	    		sliders.set(1, source.getValue());
	    	}
	    });
	    
	    s2a.addChangeListener(new ChangeListener(){
	    	public void stateChanged(ChangeEvent e) {
	    		JSlider source = (JSlider)e.getSource();
	    		sliders.set(2, source.getValue());
	    	}
	    });
	    
	    s2b.addChangeListener(new ChangeListener(){
	    	public void stateChanged(ChangeEvent e) {
	    		JSlider source = (JSlider)e.getSource();
	    		sliders.set(3, source.getValue());
	    	}
	    });
	    
	    s3a.addChangeListener(new ChangeListener(){
	    	public void stateChanged(ChangeEvent e) {
	    		JSlider source = (JSlider)e.getSource();
	    		sliders.set(4, source.getValue());
	    	}
	    });
	    
	    s3b.addChangeListener(new ChangeListener(){
	    	public void stateChanged(ChangeEvent e) {
	    		JSlider source = (JSlider)e.getSource();
	    		sliders.set(5, source.getValue());
	    	}
	    });
	    
	    s4a.addChangeListener(new ChangeListener(){
	    	public void stateChanged(ChangeEvent e) {
	    		JSlider source = (JSlider)e.getSource();
	    		sliders.set(6, source.getValue());
	    	}
	    });
	    
	    s4b.addChangeListener(new ChangeListener(){
	    	public void stateChanged(ChangeEvent e) {
	    		JSlider source = (JSlider)e.getSource();
	    		sliders.set(7, source.getValue());
	    	}
	    });
	    
	    s5a.addChangeListener(new ChangeListener(){
	    	public void stateChanged(ChangeEvent e) {
	    		JSlider source = (JSlider)e.getSource();
	    		sliders.set(8, source.getValue());
	    	}
	    });
	    
	    s5b.addChangeListener(new ChangeListener(){
	    	public void stateChanged(ChangeEvent e) {
	    		JSlider source = (JSlider)e.getSource();
	    		sliders.set(9, source.getValue());
	    	}
	    });
	    
	    s6a.addChangeListener(new ChangeListener(){
	    	public void stateChanged(ChangeEvent e) {
	    		JSlider source = (JSlider)e.getSource();
	    		sliders_status.set(0, source.getValue());
	    	}
	    });
	    
	    
	    s6b.addChangeListener(new ChangeListener(){
	    	public void stateChanged(ChangeEvent e) {
	    		JSlider source = (JSlider)e.getSource();
	    		sliders_status.set(1, source.getValue());
	    	}
	    });

	    // JFrame properties
	    setSize(600, 550);
	    setBackground(Color.BLACK);
	    setTitle("FutsalAgentSimulator Controller");
	    setLocationRelativeTo(null);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setVisible(true);
	}

public JSlider sliderCreate(int min, int max, int med, String name, Box boxTo){
	Box box = Box.createVerticalBox();
	JSlider j = new JSlider(JSlider.VERTICAL, min, max, med);
	box.add(j, BorderLayout.CENTER);
	box.add(new JLabel(name), BorderLayout.CENTER);
	boxTo.add(box);
	j.setMajorTickSpacing(1);
	j.setMinorTickSpacing(1);
	j.setPaintTicks(true);
	Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
	labelTable.put( new Integer( MIN_ATTR ), new JLabel(Integer.toString(MIN_ATTR)) );
	labelTable.put( new Integer( MAX_ATTR ), new JLabel(Integer.toString(MAX_ATTR)) );
	j.setLabelTable(labelTable);
	
	j.setPaintLabels(true);


	return j;

}

public ArrayList<Integer> getSliders() {
	return sliders;
}

	
}

