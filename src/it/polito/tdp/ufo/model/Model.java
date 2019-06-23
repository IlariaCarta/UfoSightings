package it.polito.tdp.ufo.model;

import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.ufo.db.SightingsDAO;

public class Model {
	
	private SightingsDAO dao;
	private List<AnnoCount> anni;
	private List<String> stati;
	private Graph <String, DefaultEdge> grafo ;
	private boolean esiste = false;
	
	public Model() {
		this.dao = new SightingsDAO();
		this.anni = new ArrayList<>();
		this.stati = new ArrayList<String>();
		
	}

	
	
	
	public List<AnnoCount>getAnni() {
		return this.anni = dao.loadAnni();
	}
	
	public List<String> getStati(Year anno){
		this.stati=dao.loadStati(anno);
		return this.stati;
	}
	
	
	public void creaGrafo(Year anno) {
		this.grafo = new SimpleDirectedGraph<String, DefaultEdge >(DefaultEdge.class);
		
		this.stati=dao.loadStati(anno);
		//Vertici
		Graphs.addAllVertices(this.grafo, this.stati);
		
		for(String s1 : this.grafo.vertexSet()) {
			for(String s2 : this.grafo.vertexSet()) {
				if(!s1.equals(s2) && dao.esisteArco(anno, s1, s2) )
					this.grafo.addEdge(s1, s2);
			}
		}
		
		
		System.out.println("Grafo creato!");
		System.out.println("# vertici: " + this.grafo.vertexSet().size());
		System.out.println("# archi: " + this.grafo.edgeSet().size());
	}
	
	public int getVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<String> getPrecedenti(String s){
		List<String> precedenti = new ArrayList<String>();
		precedenti = Graphs.predecessorListOf(this.grafo, s);
		return precedenti;
	}
	
	
	public List<String> getSuccessivi(String s){
		List<String> successivi = new ArrayList<String>();
		successivi = Graphs.successorListOf(this.grafo, s);
		return successivi;
	}
	
	
	public List<String> getConnessi(String s){
		List<String> visitati = new ArrayList<String>();
		DepthFirstIterator<String, DefaultEdge> dv = new DepthFirstIterator<>(this.grafo, s);
	
		while(dv.hasNext()) {
			visitati.add(dv.next());
		}
	
		
		return visitati;
		
	}
	

	
}
