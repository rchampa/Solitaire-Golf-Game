/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package solitariogolf;

import java.util.ArrayList;

/**
 *
 * @author Ricardo
 */
public class Baraja {

    private ArrayList<Carta> baraja;
    private int numCartas; //SOLO se permitira un numero de cartas que sea multiplo de 4

    public Baraja(int numCartas){

        if(numCartas%4==0)
            this.numCartas = numCartas;
        else
            this.numCartas = 52;
        
        baraja = new ArrayList<Carta>(numCartas);

        int cartasPorPalo = numCartas/4;
        //Espadas
        for(int i=0; i<cartasPorPalo-3; i++)
            baraja.add(new Carta(Carta.ESPADAS, i+1));
        
        baraja.add(new Carta(Carta.ESPADAS, 11));
        baraja.add(new Carta(Carta.ESPADAS, 12));
        baraja.add(new Carta(Carta.ESPADAS, 13));


        //Corazones
        for(int i=0; i<cartasPorPalo-3; i++)
            baraja.add(new Carta(Carta.CORAZONES, i+1));

        baraja.add(new Carta(Carta.CORAZONES, 11));
        baraja.add(new Carta(Carta.CORAZONES, 12));
        baraja.add(new Carta(Carta.CORAZONES, 13));

        //Diamantes
        for(int i=0; i<cartasPorPalo-3; i++)
            baraja.add(new Carta(Carta.DIAMANTES, i+1));

        baraja.add(new Carta(Carta.DIAMANTES, 11));
        baraja.add(new Carta(Carta.DIAMANTES, 12));
        baraja.add(new Carta(Carta.DIAMANTES, 13));

        //Treboles
        for(int i=0; i<cartasPorPalo-3; i++)
            baraja.add(new Carta(Carta.TREBOLES, i+1));

        baraja.add(new Carta(Carta.TREBOLES, 11));
        baraja.add(new Carta(Carta.TREBOLES, 12));
        baraja.add(new Carta(Carta.TREBOLES, 13));
    }

    public void barajar(){

        int aleatorio;
        for(int i=0; i<100; i++){
            aleatorio = (int) (Math.random() * (baraja.size()-1));
            Carta c = baraja.remove(aleatorio);
            baraja.add(c);
        }
    }

    
    public Carta dameCarta(){
        Carta c = baraja.remove(baraja.size()-1);
        return c;
    }

    public boolean hayCartas(){
        return !baraja.isEmpty();//si la baraja no esta vacia, es que aun quedan cartas
    }
    
    @Override
    public Baraja clone(){
        Baraja clon = new Baraja(this.numCartas);
        clon.numCartas = this.numCartas;
        clon.baraja = (ArrayList)this.baraja.clone();
        
        return clon;
    }

     public String toString() {
         
         return baraja.toString();
     }

}
