package pt.ulisboa.aasma.fas.j2d;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class MenuFrame extends JFrame {

	private static final int MIN_ATTR = 0;
	private static final int MED_ATTR = 2;
	private static final int MAX_ATTR = 5;
	
	//JPanel
	JPanel pnlControl = new JPanel();
	//Buttons
	JButton btnRestart = new JButton("Restart Game");
	JButton btnRun = new JButton("Run!");


	
	public MenuFrame(){
		 //ControlPanel setbounds
	    btnRestart.setBounds(60, 400, 220, 30);
	    btnRun.setBounds(60, 400, 220, 30);

	    //JPanel bounds
	    pnlControl.setBounds(800, 800, 200, 100);



	   
	    Box boxGK = Box.createVerticalBox();
	    Box boxDef = Box.createVerticalBox();
	    Box boxAtk = Box.createVerticalBox();
	    Box boxDri = Box.createVerticalBox();
	    Box boxPas = Box.createVerticalBox();

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



	    //Adding to JFrame
	    Box btnBox = Box.createVerticalBox();
	    btnBox.add(btnRun, BorderLayout.CENTER);
	    btnBox.add(btnRestart, BorderLayout.CENTER);
	    pnlControl.add(boxGK);
	    pnlControl.add(boxDef);
	    pnlControl.add(boxAtk);
	    pnlControl.add(boxDri);
	    pnlControl.add(boxPas);
	    pnlControl.add(btnBox);
	    add(pnlControl, BorderLayout.CENTER);
	    
	    btnRestart.addActionListener(new ActionListener() { 
	    	  public void actionPerformed(ActionEvent e) { 
	    		    System.out.println("Game Restarted");
	    		    //TODO: CALL RESTART GAME FUNCTION
	    		  } 
	    		} );
	    btnRun.addActionListener(new ActionListener() { 
	    	  public void actionPerformed(ActionEvent e) { 
	    		    System.out.println("Game Running!");
	    		    //TODO: CALL RUN GAME STAT
	    		  } 
	    		} );
	    
	    
	    s1a.addChangeListener(new ChangeListener(){
	    	public void stateChanged(ChangeEvent e) {
	    		JSlider source = (JSlider)e.getSource();
	    		//TODO: mudaCenasAtributos(source.getValue());
	    	}
	    });
	    
	    s1b.addChangeListener(new ChangeListener(){
	    	public void stateChanged(ChangeEvent e) {
	    		JSlider source = (JSlider)e.getSource();
	    		//TODO: mudaCenasAtributos(source.getValue());
	    	}
	    });
	    
	    s2a.addChangeListener(new ChangeListener(){
	    	public void stateChanged(ChangeEvent e) {
	    		JSlider source = (JSlider)e.getSource();
	    		//TODO: mudaCenasAtributos(source.getValue());
	    	}
	    });
	    
	    s2b.addChangeListener(new ChangeListener(){
	    	public void stateChanged(ChangeEvent e) {
	    		JSlider source = (JSlider)e.getSource();
	    		//TODO: mudaCenasAtributos(source.getValue());
	    	}
	    });
	    
	    s3a.addChangeListener(new ChangeListener(){
	    	public void stateChanged(ChangeEvent e) {
	    		JSlider source = (JSlider)e.getSource();
	    		//TODO: mudaCenasAtributos(source.getValue());
	    	}
	    });
	    
	    s3b.addChangeListener(new ChangeListener(){
	    	public void stateChanged(ChangeEvent e) {
	    		JSlider source = (JSlider)e.getSource();
	    		//TODO: mudaCenasAtributos(source.getValue());
	    	}
	    });
	    
	    s4a.addChangeListener(new ChangeListener(){
	    	public void stateChanged(ChangeEvent e) {
	    		JSlider source = (JSlider)e.getSource();
	    		//TODO: mudaCenasAtributos(source.getValue());
	    	}
	    });
	    
	    s4b.addChangeListener(new ChangeListener(){
	    	public void stateChanged(ChangeEvent e) {
	    		JSlider source = (JSlider)e.getSource();
	    		//TODO: mudaCenasAtributos(source.getValue());
	    	}
	    });
	    
	    s5a.addChangeListener(new ChangeListener(){
	    	public void stateChanged(ChangeEvent e) {
	    		JSlider source = (JSlider)e.getSource();
	    		//TODO: mudaCenasAtributos(source.getValue());
	    	}
	    });
	    
	    s5b.addChangeListener(new ChangeListener(){
	    	public void stateChanged(ChangeEvent e) {
	    		JSlider source = (JSlider)e.getSource();
	    		//TODO: mudaCenasAtributos(source.getValue());
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
	
}

