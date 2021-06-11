package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	PremierLeagueDAO dao;
	Map<Integer, Player> idMap;
	Graph<Player,DefaultWeightedEdge > grafo;
	List<Adiacenza> archi;
	
	//parametri di output della ricorsione
	private double gradoTeam;
	private List<Player> DreamTeam;
	
	public Model() {
		dao = new PremierLeagueDAO();
		idMap = new HashMap<>();
		
	}

	//public void creaGrafo(Integer goals) {
		public void creaGrafo(double goals) {
		 this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		 
		 //aggiungo i vertici
		 Graphs.addAllVertices(grafo, dao.getAllVertex(idMap,goals)); //cui riempio la mappa
			
		 //aggiungo gli archi
		  archi = dao.getAllArchi(idMap);
		 
		 for(Adiacenza a: archi) {
			 
			 if(grafo.containsVertex(a.getP1()) && grafo.containsVertex(a.getP2())) {
				 //faccio questo controllo perche il metodo addedgewithvertices aggiungerebbe il vertice al grafo se non esistesse 
			    
				 if(a.getDiffTempo()>0) { 
				//allora il giocatore 1 ha giocato piu minuti del secondo
				 
				 Graphs.addEdgeWithVertices(this.grafo, a.getP1(), a.getP2(), a.getDiffTempo());
			 }else {
				 Graphs.addEdgeWithVertices(this.grafo, a.getP2(), a.getP1(), (-1)*a.getDiffTempo());
				// a.setDiffTempo((-1)*a.getDiffTempo());
			 }
		 }
	 }
		 
	}
		
  public int getNumVertici() {
	  return grafo.vertexSet().size();
  }

  public int getNumArchi() {
	  return grafo.edgeSet().size();
  }
  
  public List<Adiacenza> getMiglioreGiocatore(){
	  int TempoGiocato=0;
	  int NumBattuti=0;
	  Player best = null;
	  Set<DefaultWeightedEdge> SetAvversari=null ;
	  
	  List<Adiacenza> result = new ArrayList<>();
	  
	  for(Player p: this.grafo.vertexSet()) {
		  
		   int minuti=0;
		   int avversari=0;
		   Set<DefaultWeightedEdge> listaAvversari = grafo.outgoingEdgesOf(p);
		    avversari = listaAvversari.size();
		   
		    //calcola la somma dei pesi uscenti
		  for(DefaultWeightedEdge edge: listaAvversari) {
			  minuti += grafo.getEdgeWeight(edge);
		  }
		  
		  if(minuti > TempoGiocato &&  avversari>=NumBattuti) {
			  TempoGiocato = minuti;
			   NumBattuti =avversari ;
			  best = p;
			  SetAvversari = new HashSet<>(listaAvversari);
		  }
		  
		 
	  }
	  
	  //devo mettere il set nella lista e poi ordinare l'insieme
	  if(best!=null && SetAvversari!=null) {
		  for(DefaultWeightedEdge e: SetAvversari) {
			  Adiacenza a = new Adiacenza(best , grafo.getEdgeTarget(e), (int)grafo.getEdgeWeight(e));
			  result.add(a);
		  }
	  }
	  
	  Collections.sort(result);
	  return result; 
  }
  
  public Set<DefaultWeightedEdge> alledges(){
	  return this.grafo.edgeSet();
  }
  
  public List<Adiacenza> ArchiConPeso(){
	  return this.archi;
  }
  
  public boolean esisteGrafo() {
	  if(this.grafo==null)
		  return false;
	  return true;
  }

	public List<Player> doRicorsione(int k) {
		//questa ricorsione cerca di costruire una dream team con k giocatori 
		//che massimizzi il grado di titolarità del team
		/*
		 * caso terminale: abbiamo il trovato l'ottimo : il num di giocatori del team è pari a k 
		 * e Grado titolarita del team( = Somma Grado titolarita del team dei singoli giocatori) è max
		 */
		
		gradoTeam=0.0; 
		
		
		double gradoAttuale=0.0; //contiene il grado del team degli elementi dentro parziale
		List<Player> parziale = new ArrayList<>();
		List<Player> daScartare = new ArrayList<>();
		
		cerca(parziale,  k, daScartare);
		
		return this.DreamTeam;
	}

	private void cerca(List<Player> parziale, int k, List<Player> daScartare) {
		
		//caso terminale
		if(parziale.size()==k ) {
			DreamTeam = new ArrayList<>(parziale);
		//	gradoTeam = gradoAttuale;
			
		}
		
		for(Player p: this.grafo.vertexSet()) {
			//calcolo il grado
			double usenti = 0;
			double entranti =0;
			double grado;
			
			for(DefaultWeightedEdge edge: this.grafo.outgoingEdgesOf(p)) {
				usenti += grafo.getEdgeWeight(edge);
			}
			
			for(DefaultWeightedEdge edge: this.grafo.incomingEdgesOf(p)){
				entranti += grafo.getEdgeWeight(edge);
			}
			grado = usenti - entranti;
			
			if( (this.gradoTeam+grado) > this.gradoTeam) {
				// se è vero allora questo giocatore potrebbe migliorare la team
				
				 this.gradoTeam = (this.gradoTeam+grado);
				if(!daScartare.contains(p)) {
				      parziale.add(p);
				
				daScartare.addAll(Graphs.successorListOf(this.grafo, p));
				cerca(parziale, k, daScartare);
				parziale.remove(p);
				}
				
			}
			
		}
		
	}
  
}
