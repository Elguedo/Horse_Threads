
package carrera_caballo;

/**
 *
 * @author Ezequiel Elguedo
 * 
 * Realizar una ventana con 4 progressbar en Java con Swing, se crearan 4 hilos que representaran a caballos 
 * (que tienen un nombre y un camino recorrido o porcentaje recorrido). Al pulsar el botón de iniciar se empezará a avanzar,
 * actualizando los progressbar con un numero aleatorio entre 1 y 15. Gana el primero que llegue a 100.
 */

public class Carerra_Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                CarreraDeCaballos carrera = new CarreraDeCaballos();
                carrera.setVisible(true);
            }
        });
    }
    
}
