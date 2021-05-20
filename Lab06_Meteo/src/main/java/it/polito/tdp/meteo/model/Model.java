package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	
	private List<Citta> leCitta;
	private List<Citta> best;

	public Model() {
		MeteoDAO meteoDao = new MeteoDAO();
		leCitta = meteoDao.getAllCitta();
	}
	

	// of course you can change the String output with what you think works best
	public Double getUmiditaMedia(int mese, String localita) {
		MeteoDAO meteoDao = new MeteoDAO();
		return meteoDao.getAvgRilevamentiLocalitaMese(mese, localita);
	}
	
	// of course you can change the String output with what you think works best
	public List<Citta> trovaSequenza(int mese) {
		List<Citta> parziale = new ArrayList<Citta>();
		this.best = null;
		MeteoDAO dao = new MeteoDAO();
		
		for(Citta c : leCitta) {
			c.setRilevamenti(dao.getAllRilevamentiLocalitaMese(mese, c.getNome()));
		}
		cerca(parziale,0);
		return best;
	}


	private void cerca(List<Citta> parziale, int livello) {
		if(livello == NUMERO_GIORNI_TOTALI) {
			Double costo = (parziale);
			
			if(best==null || costo<calcolaCosto(best)) {
				best = new ArrayList<Citta>(parziale);
			} else {
				for(Citta prova : leCitta) {
					if(aggiuntaValida(parziale,prova)) {
						parziale.add(prova);
						cerca(parziale, livello+1);
						parziale.remove(parziale.size()-1);
					}
				}
			}
		}
		
		
	}


	private Double (List<Citta> parziale) {
		double costo =0.0;
		
		for(int giorno =1;giorno<=NUMERO_GIORNI_TOTALI;giorno++) {
			Citta c = parziale.get(giorno-1);
			double umidita = c.getRilevamenti().get(giorno-1).getUmidita();
			
			costo+=umidita;
		}
		for(int giorno =2; giorno<=NUMERO_GIORNI_TOTALI; giorno++) {
			if(!parziale.get(giorno-1).equals(parziale.get(giorno-2))) {
				costo+=COST;
			}
		}
		return costo;
	}
	

}
