/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package solitariogolf;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Stack;

/**
 *
 * @author Ricardo
 */
public class Main {

    static int n_veces=0;
    static int cartas_por_palo;
    static SolitarioGolf mejorSolucion;
    static int mejorPuntuacion = Integer.MAX_VALUE;
    public static void main(String[] args) {

        int numCartas = 40;
        cartas_por_palo=numCartas/4;
        SolitarioGolf juego = new SolitarioGolf(4,numCartas);

        RBoolean b = new RBoolean();
        boolean[][] marcaje = new boolean[4][numCartas];
     
        //buscarPar(juego, b, marcaje);

        buscarMejorSolucion(juego, marcaje);

        
    }

    static void buscarPar(SolitarioGolf juego, RBoolean b, boolean[][]marcaje){

        //Tiempo inicial
        long ini = System.currentTimeMillis();
        
        vuelta_atras(juego, b, marcaje);

        System.out.println("Numero de veces que se ha ejecutado el algoritmo: "+n_veces);

        //Tiempo final
        long fin = System.currentTimeMillis();
        //Resto de tiempos
        long tiempo = (fin-ini);
        System.out.println("Tiempo en ms: "+ tiempo);
    }

    static void buscarMejorSolucion(SolitarioGolf juego, boolean[][] marcaje){

         //Tiempo inicial
        long ini = System.currentTimeMillis();

        vuelta_atras_mejor_solucion(juego, marcaje);
        System.out.println();
        System.out.println();
        System.out.println("La mejor puntuacion es: "+mejorPuntuacion);
        System.out.println("La mejor solucion es: ");
        System.out.println(mejorSolucion.historialDeMovimientos);

        System.out.println("Numero de veces que se ha ejecutado el algoritmo: "+n_veces);

        //Tiempo final
        long fin = System.currentTimeMillis();
        //Resto de tiempos
        long tiempo = (fin-ini);
        System.out.println("Tiempo en ms: "+ tiempo);
        
    }

    static void vuelta_atras(SolitarioGolf juego, RBoolean encontrado, boolean[][] marcaje){

        n_veces++;//Se cuenta cuantas veces se ha ejecutado el algoritmo
        //Se prepara el recorrido
        int i=0;
        SolitarioGolf copiaJuego = null;
        LinkedList<SolitarioGolf> listaNodos = new LinkedList<SolitarioGolf>();//Lista de jugadas que se hacen a partir del estado del juego
        int numperoDePila = 0;
        while(i<juego.numPilas && numperoDePila!=-1){//Mientras que no se consulten todas las pilas y ademas sepamos que aun queda por explorar alguna pila
            copiaJuego = juego.clone();
            numperoDePila = copiaJuego.siguiente_jugada(i);//Se genera una posible jugada

            if(numperoDePila!=-1){//Si hay alguna pila con cartaCorrelativa
                i = numperoDePila + 1;//con este avance se descarta las pilas que se sabe que no tienen cartas correlativas
                listaNodos.add(copiaJuego);//Se aniade a la lista la posible jugada
            }
            else{//Si no hay cartas correlativas en el resto de las pilas
                if(i==0){//Y si no las en todas las pilas <-- (-1+1=0), es decir, si devuelve -1 la primera vez significa que noy jugada en la que se coja una carta de alguna pila
                    listaNodos.add(copiaJuego);//Se añade la jugada que implica coger una carta del mazoOrigen
                }
            }

        }

        ListIterator<SolitarioGolf> iterador = listaNodos.listIterator();
        SolitarioGolf nodo;
        LinkedList<Carta> cartasCogidas;

        //Se recorren todos los nodosHijo y no se haya encontrado alguna solucion al Par
        while(iterador.hasNext() && !encontrado.booleano){

            nodo = iterador.next();//Se coge el siguiente hijo del nivel
            cartasCogidas = nodo.getCartasCogidas();
            marcar(marcaje, cartasCogidas, true);//Marcar


            if(nodo.haTerminado()){//esSolucion?
                if(nodo.resultado==SolitarioGolf.PAR){
                    System.out.println("Se ha encontrado un resultado PAR!");
                    System.out.println("Se finaliza la busqueda, aunque queden nodos por explorar.");
                    System.out.println(nodo.historialDeMovimientos);
                    encontrado.booleano = true;
                }
                else{
                    System.out.println("Se ha encontrado un resultado que NO ES PAR :( ");
                    //System.out.println(nodo.historialDeMovimientos);
                }
            }
            else{
                //if(funciondePoda())//esCompletable?
               //if(esCompletable(marcaje, cartasCogidas.getFirst()))
                    vuelta_atras(nodo,encontrado, marcaje);
                //sino es completable, no merece la pena seguir explorando
            }

            marcar(marcaje, cartasCogidas, false);//Desmarcar
        }
    } 

    static void vuelta_atras_mejor_solucion(SolitarioGolf juego, boolean[][] marcaje){

        n_veces++;//Se cuenta cuantas veces se ha ejecutado el algoritmo
        //Se prepara el recorrido
        int i=0;
        SolitarioGolf copiaJuego = null;
        LinkedList<SolitarioGolf> listaNodos = new LinkedList<SolitarioGolf>();
        int numperoDePila = 0;
        while(i<juego.numPilas && numperoDePila!=-1){
            copiaJuego = juego.clone();
            numperoDePila = copiaJuego.siguiente_jugada(i);

            if(numperoDePila!=-1){
                i = numperoDePila + 1;//con este avance se descarta las pilas que se sabe que no tienen cartas correlativas
                listaNodos.add(copiaJuego);
            }
            else{//Si no hay cartas correlativas en el resto de las pilas
                if(i==0){//Y si no las en todas las pilas
                    listaNodos.add(copiaJuego);
                }
            }

        }

        ListIterator<SolitarioGolf> iterador = listaNodos.listIterator();

        SolitarioGolf nodo;
        LinkedList<Carta> cartasCogidas;
        while(iterador.hasNext()){

            nodo = iterador.next();//Se coge el siguiente hijo del nivel
            cartasCogidas = nodo.getCartasCogidas();
            marcar(marcaje, cartasCogidas, true);//Marcar


            if(nodo.haTerminado()){//esSolucion?
                int nuevaPuntuacionFinal = nodo.getPuntuacionFinal();
                if(nodo.resultado==SolitarioGolf.PAR){
                    System.out.println("Se ha encontrado un resultado PAR!");
                    System.out.println("Puntuacion: "+nuevaPuntuacionFinal);
                }
                else if(nodo.resultado==SolitarioGolf.BAJO_PAR){
                    System.out.println("Se ha encontrado un resultado que BAJO PAR!");
                    System.out.println("Puntuacion: "+nuevaPuntuacionFinal);
                }
                else{
                    System.out.println("Se ha encontrado un resultado que SOBRE PAR!");
                    System.out.println("Puntuacion: "+nuevaPuntuacionFinal);
                }
                
                if(nuevaPuntuacionFinal<mejorPuntuacion){
                    mejorPuntuacion = nuevaPuntuacionFinal;
                    mejorSolucion = nodo.clone();
                }
            }
            else{
                //if(funciondePoda())//esCompletable?
               //if(esCompletable(marcaje, cartasCogidas.getFirst()))
                    vuelta_atras_mejor_solucion(nodo, marcaje);
                //sino es completable, no merece la pena seguir explorando
            }

            marcar(marcaje, cartasCogidas, false);//Desmarcar
        }
    }

   //Marca o Desmarca las cartas que ya han sido usadas
    static void marcar(boolean[][] marcados, LinkedList<Carta> listaCartas, boolean marcar){

        Carta carta;
        ListIterator<Carta> lista = listaCartas.listIterator();
        while(lista.hasNext()){
            carta = lista.next();
            marcados[carta.getPalo()][posCorrecta(carta)] = marcar;
        }

    }

    static boolean esCompletable(boolean[][] marcados, Carta carta){

        //Si se juega una carta a el mazoDestino, y sabemos de antemano que
        //todos los correlativos de la carta jugada ya estan en mazoDestino

        boolean OK=false;

        int valorLimite = carta.getValor();
        if(valorLimite==1){
            if(marcados[Carta.ESPADAS][posCorrecta(carta)+1] &&
               marcados[Carta.CORAZONES][posCorrecta(carta)+1] &&
               marcados[Carta.DIAMANTES][posCorrecta(carta)+1] &&
               marcados[Carta.TREBOLES][posCorrecta(carta)+1]){
                OK = false;
            }
            else{
                OK = true;
            }

        }
        else if(valorLimite==13){

            if(marcados[Carta.ESPADAS][posCorrecta(carta)-1] &&
               marcados[Carta.CORAZONES][posCorrecta(carta)-1] &&
               marcados[Carta.DIAMANTES][posCorrecta(carta)-1] &&
               marcados[Carta.TREBOLES][posCorrecta(carta)-1]){
                OK = false;
            }
            else{
                OK = true;
            }

        }
        else{

            if(marcados[Carta.ESPADAS][posCorrecta(carta)-1] && marcados[Carta.ESPADAS][posCorrecta(carta)+1] &&
               marcados[Carta.CORAZONES][posCorrecta(carta)-1] && marcados[Carta.CORAZONES][posCorrecta(carta)+1] &&
               marcados[Carta.DIAMANTES][posCorrecta(carta)-1] && marcados[Carta.DIAMANTES][posCorrecta(carta)+1] &&
               marcados[Carta.TREBOLES][posCorrecta(carta)-1] && marcados[Carta.TREBOLES][posCorrecta(carta)+1]){
                OK = false;
            }
            else{
                OK = true;
            }

        }
        
        return OK;
    }

    //calcula la posicion correcta de cada carta en el array de marcaje
    static int posCorrecta(Carta c){

        int valor = c.getValor();
        if(valor==11){
            return cartas_por_palo-3;
        }
        else if(valor==12){
            return cartas_por_palo-2;
        }
        else if(valor==13){
            return cartas_por_palo-1;
        }else{
            return c.getValor()-1;
        }
    }
    
}
