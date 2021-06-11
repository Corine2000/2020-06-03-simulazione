package it.polito.tdp.PremierLeague.model;

import java.util.*;

import org.jgrapht.graph.DefaultWeightedEdge;

public class TestModel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Model m = new  Model();
		m.creaGrafo(0.3); //creami il grafo
		
  System.out.println("#vertex "+m.getNumVertici()+" #Archi "+m.getNumArchi());
  List<Adiacenza> best = m.getMiglioreGiocatore();
  
  
  
  for(Adiacenza a: best)
	  System.out.println(a.toString());
  
  Set<DefaultWeightedEdge> archi = m.alledges();
  
 /* for(DefaultWeightedEdge e: archi) {
	  System.out.println(e.toString());
  }*/
  
  System.out.println("ARCHI DEL GRAFO");
  List<Adiacenza> Archi = m.ArchiConPeso();
  /*  for(Adiacenza a: Archi) {
    	System.out.println(a.toString());
      }*/
  
	List<Player> dreamTeam = m.doRicorsione(3);
	System.out.println("risultato ricorsione");
	
	 for(Player p:dreamTeam) {
		 System.out.println(p.toString());
	 }
	
	}
	

}
