/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solitariogolf;

import java.util.LinkedList;
import java.util.Stack;


public class SolitarioGolf {

    //Atributos
    /*
     * Se hace uso de las clases Baraja y Carta que se encargan de generar las cartas necesarias
     *
     * Se elige la clase Stack(Pila) para representar las pilas y los mazos destino y origen debido
     * a que en el transcurso del juego solo necesitamos la parte superior de cada pila o mazo
     *
     * Se crea las constantes J,Q y K para representar las letras de la baraja
     *
     * FIN: determina si el juego ha finalizado
     * pilasVacias: determina si todas las pilas estan vacias
     * resultado: determina el tipo de resultado, que puede ser PAR, BAJO_PAR o SOBRE_PAR
     * numPilas: Es el numero de pilas que hay en el juego
     * numCartas: Es el numero de cartas de la baraja con las que trabaja el juego
     * historialDeMovimientos: Guarda el historial de movimientos del juego, se usa esta clase antes que String por su bajo coste en la concatenacion
     * cartasJugadas: Lista de cartas jugadas en un movimiento, se usa LinkedList antes que un array debido a que tiene implementado un Iterador
     * puntuacionFinal: La puntuacion final del juego
     */
    
    Baraja baraja;
    Stack<Carta> mazoDestino;
    Stack<Carta> mazoOrigen;
    Stack<Carta>[] pilas;
    
    final int J = 11;
    final int Q = 12;
    final int K = 13;
    static final int    PAR = 0,
                        BAJO_PAR = -1,
                        SOBRE_PAR = 1;
    
    boolean FIN;
    boolean pilasVacias;
    int resultado;

    int numPilas;
    int numCartas;
    StringBuilder historialDeMovimientos;
    LinkedList<Carta> cartasJugadas;
    int puntuacionFinal;
    
    public SolitarioGolf(){
        FIN = false;
        pilasVacias = false;
        this.cartasJugadas = new LinkedList<Carta>();
        historialDeMovimientos = new StringBuilder("");
    }
    
    public SolitarioGolf(int numPilas, int numCartas){
        
        FIN = false;
        pilasVacias = false;
        this.numPilas = numPilas;
        this.numCartas = numCartas;
        this.cartasJugadas = new LinkedList<Carta>();
        this.historialDeMovimientos = new StringBuilder("");

        //Se crea una nueva baraja de cartas con un numero numCartas determinado
        baraja = new Baraja(numCartas);
        historialDeMovimientos.append("Baraja: "+baraja+"\n");

        //Se baraja el monton de cartas
        baraja.barajar();
        historialDeMovimientos.append("Barajada!: "+baraja+"\n");

        //se cargan las pilas
        inicializarPilas(numPilas);
        
        //Variable auxiliar
        Carta carta;
       
        //Se inicializa la pila destino
        mazoDestino = new Stack<Carta>();
        carta = baraja.dameCarta();
        mazoDestino.push(carta);
        
        
        //Se inicializa la pila origen
        mazoOrigen = new Stack<Carta>();
        while(baraja.hayCartas()){
            carta = baraja.dameCarta();
            mazoOrigen.push(carta);
        }
        
    }
    
    void inicializarPilas(int numPilas){
        
        pilas = new Stack[numPilas];
        for(int i=0; i<numPilas; i++){
            pilas[i] = new Stack<Carta>();
            for(int j=0; j<5; j++){
                Carta carta = baraja.dameCarta();
                pilas[i].push(carta);
            }
        }
        
    }
    

    /*  Devuelve:
     *
     * siguiente_jugada(n)  {   -1, Si no hay cartaCorrelativa
     *                      {   numeroDePila, Si hay alguna pila con una cartaCorrelativa con (n<=numeroDePila<numPilas)
     */
    int siguiente_jugada(int n){
        

        
        historialDeMovimientos.append("\n\n"+this+"\n");
        //hacer una jugada
        //buscar en las pilas algun numero correlativo
        Carta cimaDestino = mazoDestino.lastElement();//la carta de la cima de la pilaOrigen

        int numeroDePila = buscarCorrelativoEnPilas(cimaDestino, n);

        Carta cimaOrigen;

        cartasJugadas.clear();//En cada nueva jugada se limpia la lista de cartas jugadas

        //Si hay alguna cartaCorrelativa
        if(numeroDePila!=-1){
            Carta cartaCorrelativa = pilas[numeroDePila].pop(); //Se coge la carta de la pila correspondiente
            mazoDestino.push(cartaCorrelativa);                 //Esa carta se coloca en el mazoDestino
            cartasJugadas.add(cartaCorrelativa);                //Se actualiza la lista de cartasJugadas

            //Mientras que en la cima del mazoDestino haya una K y el mazoOrigen no esta vacia
            while(mazoDestino.lastElement().getValor()==K && !mazoOrigen.isEmpty()){
                historialDeMovimientos.append("Se ha encontrado una K en el mazoDestino!\n");
                cimaOrigen = mazoOrigen.pop();      //Se coge la carta del mazoOrigen
                mazoDestino.push(cimaOrigen);       //Y se coloca en el mazoDestino
                cartasJugadas.add(cimaOrigen);      //Se actualiza la lista de cartasJugadas

                historialDeMovimientos.append("\n\n"+this+"\n");
            }
            
            //Se comprueba el estado de todas las pilas
            pilasVacias = true;
            for(int i=0; i<numPilas && pilasVacias; i++){
                if(!pilas[i].isEmpty())//Si la pila aun tiene algo
                    pilasVacias = pilasVacias && false;
            }
            
            FIN = pilasVacias;
            return numeroDePila;
        }
        else{//Sino hay correlativa
            historialDeMovimientos.append("No se encontraron cartas correlativas\n");
            if(!mazoOrigen.isEmpty()){//Si el mazoOrigen aun tiene cartas
                cimaOrigen = mazoOrigen.pop();  //Se coge la carta del mazoOrigen
                mazoDestino.push(cimaOrigen);   //Y se coloca en el mazoDestino
                cartasJugadas.add(cimaOrigen);  //Se actualiza la lista de cartasJugadas
            }
            else{//Sino hay cartas correlativas en las pilas y el mazoOrigen esta vacio
                historialDeMovimientos.append("El juego no puede continuar.\n");
                FIN = true;//Se acaba el juego
            }
            return -1;//no hay pila que contenga carta correlativa
        }
            
        
    }
    
    boolean sonCorrelativos(Carta a, Carta b){
        
        return Math.abs(a.getValor()-b.getValor()) == 1;
        
    }
    
    boolean haTerminado(){
        
        if(FIN){
            int puntos;
            if(pilasVacias && mazoOrigen.isEmpty()){
                historialDeMovimientos.append("Se termina en al PAR\n");
                historialDeMovimientos.append("Puntuacion: "+0+"\n");
                resultado = PAR;
                puntuacionFinal = 0;
            }
            else if(pilasVacias && !mazoOrigen.isEmpty()){
                puntos = mazoOrigen.size()*(-1);
                historialDeMovimientos.append("Se termina en BAJO PAR\n");
                historialDeMovimientos.append("Puntuacion: "+puntos+"\n");
                resultado = BAJO_PAR;
                puntuacionFinal = puntos;
            }
            else{//Si el mazo origen esta vacio y no hay cartas correlativas en las pilas
                puntos = 0;
                for(int i=0; i<numPilas; i++)
                    puntos += pilas[i].size();
                
                historialDeMovimientos.append("Se termina en SOBRE PAR\n");
                historialDeMovimientos.append("Puntuacion: "+puntos+"\n");
                resultado = SOBRE_PAR;
                puntuacionFinal = puntos;
            }
        }
        
        return FIN;
        
    }

    int getPuntuacionFinal(){
        return puntuacionFinal;
    }


    //Devuelve la pila en la que hay una cartaCorrelativa
    //Sino la hay, devuelve -1
    int buscarCorrelativoEnPilas(Carta carta, int desde) {

        for(int i=desde; i<numPilas; i++){
            if(!pilas[i].isEmpty() && sonCorrelativos(pilas[i].lastElement(), carta)){
                //System.out.println("La carta de la pila "+i+": "+pilas[i].lastElement()+"es correlativa.");
                historialDeMovimientos.append("La carta de la pila "+i+": "+pilas[i].lastElement()+"es correlativa.\n");
                return i;
            }
        }

        return -1;
    }

    @Override
    public SolitarioGolf clone() {
        SolitarioGolf clon = new SolitarioGolf();
        clon.numPilas = this.numPilas;
        clon.baraja = this.baraja.clone();
        clon.mazoDestino = (Stack<Carta>)this.mazoDestino.clone();
        clon.mazoOrigen = (Stack<Carta>)this.mazoOrigen.clone();
        
        clon.pilas = new Stack[numPilas];
        for(int i=0; i<numPilas; i++)
            clon.pilas[i] = (Stack<Carta>)this.pilas[i].clone();


        clon.FIN = this.FIN;
        clon.pilasVacias = this.pilasVacias;
        clon.resultado = this.resultado;
        clon.numPilas = this.numPilas;
        clon.numCartas = this.numCartas;
        clon.historialDeMovimientos = new StringBuilder(this.historialDeMovimientos);
        clon.cartasJugadas = (LinkedList<Carta>)this.cartasJugadas.clone();
        clon.puntuacionFinal = this.puntuacionFinal;

        return clon;
    }

    @Override
    public String toString() {
        
        String aux = "Estado del Juego:\n";
        for(int i=0; i<numPilas; i++){
            if(!pilas[i].isEmpty())
                aux+= "Pila"+i+"{"+pilas[i].lastElement()+"}";
            else
                aux+= "Pila"+i+"{vacia}";
            
            aux+=" ";
        }
        
        aux+="\n";
        
        if(!mazoOrigen.isEmpty())
            aux+= "PilaOrigen{"+mazoOrigen.lastElement()+"}";
        else
            aux+= "PilaOrigen{vacia}";
        
        aux+="\n";
        
        if(!mazoDestino.isEmpty())
            aux+= "PilaDestino{"+mazoDestino.lastElement()+"}";
        else
            aux+= "PilaDestino{vacia}";
        
        
        return aux;
    }
    

    LinkedList<Carta> getCartasCogidas(){
        return cartasJugadas;
    }

}
