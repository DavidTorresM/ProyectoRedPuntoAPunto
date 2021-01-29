//Esta interface se realizo para definir como se van a enviar datos entre 
//los diferentes nodos de la red.
// Se puede enviar un mensaje usando un ipc distinto cuando se implementa la 
// clase.
package connectorsnet;

/**
* Sender es una interfaz que especifica los metodos para el envio de mensajes
* entre un proceso y otro.
*
* @author  Yo
* @version 1.0
* @since   2020-07-11 
*/
public interface Sender {
    public static final int TIME_REFRESH_CONN = 10;
    public void sendMenssage(Object msn);
    public Object[] sendMenssages(Object []msn);
    public Object sendRecvMenssage(Object msn);
    public Object[] sendRecvMenssages(Object []msn);
    
}
