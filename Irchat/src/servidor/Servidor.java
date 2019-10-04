package servidor;

import interfaz.ConsolaServidor;
import interfaz.PantallaChat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;


public class Servidor {
final int puerto = 5000;
final int puerto2 = 5001;
ServerSocket server;
ServerSocket server2;
HiloServidor hilo;
int idSesion = 0;
public static ConsolaServidor consola;
public static Vector<HiloServidor> usuariosActivos = new Vector<HiloServidor>();

	
	public void inicioServer() throws IOException{
		//Creando el serverSocket
		server = new ServerSocket(puerto);
		server2 = new ServerSocket(puerto2);
		consola.añadirLog("[OK]");
		//Bucle infinito que espera la conexión de x clientes.
		while(true){
		//creando el socket
		Socket socket;
		Socket socket2;
		//Esperando Conexión
		consola.añadirLog("Esperando nueva conexión");
		socket = server.accept();
		socket2 = server2.accept();
		consola.añadirLog("Nueva conexión entrante; Número entrante: "+socket);
		HiloServidor hilo;
		//Cada vez que entra un cliente se crea un nuevo hilo y se inicia.
		hilo = new HiloServidor(socket,socket2,idSesion,consola);
		hilo.start();
		idSesion++;
		}
		
	}
//	private void enviarDatos() throws IOException {
//		
//		
//	}
//	public void cerrarServer() throws IOException{
//		System.out.println("Cerrando Servidor");
//		server.close();
//	}
	
	public static void main (String [] args){
		consola = new ConsolaServidor();
		consola.añadirLog("Inicializando Servidor... ");
		Servidor s = new Servidor();
		try {
			s.inicioServer();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
}

