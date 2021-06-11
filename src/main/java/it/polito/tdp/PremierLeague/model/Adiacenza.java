package it.polito.tdp.PremierLeague.model;

public class Adiacenza implements Comparable<Adiacenza>{
	private Player p1;
	private Player p2;
	private int diffTempo;
	
	public Adiacenza(Player p1, Player p2, int diffTempo) {
		
		this.p1 = p1;
		this.p2 = p2;
		this.diffTempo = diffTempo;
	}

	public Player getP1() {
		return p1;
	}

	public void setP1(Player p1) {
		this.p1 = p1;
	}

	public Player getP2() {
		return p2;
	}

	public void setP2(Player p2) {
		this.p2 = p2;
	}

	public int getDiffTempo() {
		return diffTempo;
	}

	public void setDiffTempo(int diffTempo) {
		this.diffTempo = diffTempo;
	}

	@Override
	public String toString() {
		return this.p1+" "+this.p2+": "+this.diffTempo;
	}

	@Override
	public int compareTo(Adiacenza o) {
		// TODO Auto-generated method stub
		return -(this.diffTempo - o.getDiffTempo());
	}
	
	
	
}
