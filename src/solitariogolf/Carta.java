/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package solitariogolf;

/**
 *
 * @author Ricardo
 */
public class Carta {

    public static final int   ESPADAS     =   0,
                        CORAZONES   =   1,
                        DIAMANTES   =   2,
                        TREBOLES    =   3;

    private int palo;
    private int valor;

    public Carta(int palo, int valor){

        this.palo = palo;
        this.valor = valor;

    }

    public int getPalo() {
        return palo;
    }

    public int getValor() {
        return valor;
    }

    @Override
    public String toString() {
        String aux;
        switch (palo){
            case 0:
                aux = "Carta{"+ valor+"ESPADAS}";
                break;
            case 1:
                aux = "Carta{"+ valor+"CORAZONES}";
                break;
            case 2:
                aux = "Carta{"+ valor+"DIAMANTES}";
                break;
            default:
                aux = "Carta{"+ valor+"TREBOLES}";
                break;
        }
        
        return aux;
        
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Carta other = (Carta) obj;
        if (this.palo != other.palo) {
            return false;
        }
        if (this.valor != other.valor) {
            return false;
        }

        /*if(this.palo==other.palo && this.valor == other.valor)
            return true;*/
        return true;
    }

    @Override
    public Carta clone(){
        Carta clon = new Carta(palo, valor);
        return clon;
    }


}
