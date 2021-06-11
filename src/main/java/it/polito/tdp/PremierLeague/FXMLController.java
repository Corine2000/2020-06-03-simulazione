/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Model;
import it.polito.tdp.PremierLeague.model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnTopPlayer"
    private Button btnTopPlayer; // Value injected by FXMLLoader

    @FXML // fx:id="btnDreamTeam"
    private Button btnDreamTeam; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="txtGoals"
    private TextField txtGoals; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	
    	if(txtGoals.getText().isEmpty()) {
    		txtResult.setText("per creare il grafo , devi scegliere un Goals");
    		return;
    	}
    	
    	double goals =-1.0;
    	try {
    		goals = Double.parseDouble(txtGoals.getText());
    		
    	}catch(NumberFormatException n) {
    		txtResult.setText("il goals deve essere un numero");
    		return;
    	}
    	
    	if(goals<0 || goals>4) {
    		txtResult.setText("il goals deve essere compreso tra 0 e 4");
    		return;
    	}
    	
    	model.creaGrafo(goals);
    	txtResult.appendText("GRAFO CREATO: \n #VERTICI: "+model.getNumVertici()+ "\n#Archi: "+model.getNumArchi());
    }

    @FXML
    void doDreamTeam(ActionEvent event) {
    	
    	
    	if(txtK.getText().isEmpty()) {
    		txtResult.setText("inserisci un numero di giocatori");
    		return;
    	}
    	
    	int k=0;
    	try {
    		k = Integer.parseInt(txtK.getText());
    	}catch(NumberFormatException n) {
    		txtResult.setText("il numero di giocatori deve essere un intero");
    		return;
    	}
    	
    	model.doRicorsione(k);
    	
    	
    }

    @FXML
    void doTopPlayer(ActionEvent event) {
       txtResult.clear();
    	
    	if(!model.esisteGrafo()) {
    		txtResult.setText("DEVI PRIMA CREARE IL GRAFO");
    		return;
    	}
    	
    	List<Adiacenza> battuti = model.getMiglioreGiocatore();
    	Player best = battuti.get(0).getP1();
    	
    	txtResult.appendText("TOP PLAYER: "+best.toString()+"\n");
    	txtResult.appendText("AVVERSARI BATTUTI: \n");
    	
    	  for(Adiacenza a: battuti) {
    		  txtResult.appendText(a.getP2()+" |"+a.getDiffTempo()+"\n");
    	  }
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnTopPlayer != null : "fx:id=\"btnTopPlayer\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDreamTeam != null : "fx:id=\"btnDreamTeam\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGoals != null : "fx:id=\"txtGoals\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
