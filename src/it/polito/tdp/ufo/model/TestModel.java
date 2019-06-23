package it.polito.tdp.ufo.model;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class TestModel {
	private List<String> visite = new ArrayList<String>();
	
	public void run() {
		Model model = new Model();
		model.creaGrafo(Year.of(1976));
		visite = model.getConnessi("wa");
		for(String s : visite)
			System.out.println(s+"\n");
		
	}
	
	public static void main(String[] args) {
		TestModel main = new TestModel();
		main.run();

	}

}
