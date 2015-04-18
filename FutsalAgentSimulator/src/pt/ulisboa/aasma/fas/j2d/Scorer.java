package pt.ulisboa.aasma.fas.j2d;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

public class Scorer implements Sprite {
	
	private int scoreTeamA = 0;
	private int scoreTeamB = 0;
	//private long initialTime = System.currentTimeMillis();
	private long time = 0;	//System.currentTimeMillis(); TODO: A RECEBER DO REPORTER;

	public int getScoreTeamA() {
		return scoreTeamA;
	}

	public void setScoreTeamA(int scoreTeamA) {
		this.scoreTeamA = scoreTeamA;
	}

	public int getScoreTeamB() {
		return scoreTeamB;
	}

	public void setScoreTeamB(int scoreTeamB) {
		this.scoreTeamB = scoreTeamB;
	}

	@Override
	public void update(long time) {
	//	this.time = System.currentTimeMillis() - initialTime;
	}
	
	public String timeToString(long time){
		String minutes = new String();
		String seconds = new String();
		
		long second = (time / 1000) % 60;
		long minute = (time / (1000 * 60)) % 60;
		
		if(minute < 10)
			minutes = "0" + minute;
		else minutes = Long.toString(minute);
		
		if(second < 10)
			seconds = "0" + second;
		else seconds = Long.toString(second);
		
		return minutes + " : " + seconds;
	}

	@Override
	public void draw(Graphics2D g2d) {
		Graphics2D g = (Graphics2D) g2d.create();
		
		//Team A Background
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.RED);
		g.fill(new Rectangle2D.Float(0, 0, (float)300, 50));
		
		//Team B Background
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.BLUE);
		g.fill(new Rectangle2D.Float(300, 0, (float)300, 50));
		
		//Team A Scorer
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setColor(Color.WHITE);
		g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 20));
		g.drawString("TEAM A", 5, 33);
		g.drawString(Integer.toString(getScoreTeamA()), 230, 33);
		
		//Team B Scorer
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setColor(Color.WHITE);
		g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 20));
		g.drawString("TEAM B", 517, 33);
		g.drawString(Integer.toString(getScoreTeamB()), 359, 33);
		
		//Time displayer Background
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.BLACK);
		g.fill(new Rectangle2D.Float(255, 0, (float)90, 50));
		
		//Time displayer
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setColor(Color.WHITE);
		g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 10));
		g.drawString("TIME", 287, 10);
		g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 20));
		g.drawString(timeToString(time), 267, 33);
		
		
		g.dispose();
	}

}
