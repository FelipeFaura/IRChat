package servidor;

import interfaz.ConsolaServidor;
import interfaz.PantallaChat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JOptionPane;

public class HiloServidor extends Thread {
private Socket socket;
private Socket socket2;
private DataOutputStream DOS;
private DataInputStream DIS;
private DataOutputStream DOS2;
private int idSesion;
private String nombreUsuario ="";
private Servidor servidor;
private ConsolaServidor consola;
//public static Vector<HiloServidor> usuariosActivos = new Vector<HiloServidor>();
		public HiloServidor(Socket socket,Socket socket2,int idSesion,ConsolaServidor consola){
			this.socket = socket;
			this.idSesion = idSesion;
			this.socket2 = socket2;
			this.consola = consola;
			//this.servidor = servidor;
			
			
			
			try {
				DOS2 = new DataOutputStream(socket2.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
		}

		public void run(){
			
			try {
				DIS = new DataInputStream(socket.getInputStream());
				DOS = new DataOutputStream(socket.getOutputStream());
				nombreUsuario=DIS.readUTF();
				boolean comprobante = comprobarSiExiste(nombreUsuario);
				if(servidor.usuariosActivos.size()!=0){
					if(comprobante==false){
						servidor.usuariosActivos.add(this);
					}
					else{
						socket.close();
						socket2.close();
					}
				}
				else{
					servidor.usuariosActivos.add(this);
				}
				enviarActivos();
				//refescarActivos();
			} catch (IOException e) {
				e.printStackTrace();
			}
			int opcion =0;
			String mensaje="";
			String amigo = "";
			String usuario ="";
			//Esta es la parte mas importante del hilo, atendiendo permanentemente a los posibles eventos que le lleguen de los DataInputStream
			//Cada DataInputStream y DataOutputStream envian o reciben un numero para poder identificar que tipo de envio o recibo se está realizando
			while(true){
				try {
					opcion = DIS.readInt();
					switch(opcion){
						case 1://Mensaje se envia a todos los usuarios
							mensaje = DIS.readUTF();
							enviarMensaje(mensaje);
							break;
						case 2:
							break;
						case 3:
							break;
						case 4://El mensaje se envia a un usuario a través de un privado.
							amigo = DIS.readUTF();
							mensaje = DIS.readUTF();
							enviarMensajeAmigo(amigo,mensaje);
							break;
						case 5://Este fragmento de código envia a un cliente, objetivo de una conversación privada, la orden 
								//para que active su ventana de privado con esa persona.
							amigo = DIS.readUTF();
							usuario = DIS.readUTF();
							System.out.println("entra al caso 5 de su hilo servidor" + amigo+"  "+usuario);
							enviarCrearPrivado(amigo,usuario);
							break;
					}
	
				}
				catch (IOException e) {
					consola.añadirLog("Se ha desconectado un usuario");
					break;//Gracias a este break podemos salir del bucle infinito en caso de que ocurra un error
					//De hecho este error se activa cuando un cliente pierde la conexión con el servidor
					//lo que podemos aprovechar como información relevante y eliminarle así de la lista de Usuarios Activos
				}	
			}
			String usuarioDesconectado = this.nombreUsuario;// Se obtiene que usuario se ha desconectado
			servidor.usuariosActivos.removeElement(this);// se elimina de la lista de usuarios activos
			enviarUsuarioDesconectado(usuarioDesconectado);//Se envia a los demas clientes la informacion
			try {
				//Se cierran definitivamente los sockets dando por terminada esa conexión
				socket.close();
				socket2.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		
		private boolean comprobarSiExiste(String nombre) { //Este método averigua si ya existe un cliente con el mismo nombre, si existe no se podrá conectar
			boolean comprobante = false;
			for(int c=0;c<servidor.usuariosActivos.size();c++){
				if(nombre.equals(servidor.usuariosActivos.get(c).nombreUsuario)){
					comprobante = true;
				}
			}
			return comprobante;
		}
private void enviarCrearPrivado(String amigo,String enviador) { //Método que envia a un usuario(hiloCliente) objetivo de privado las ordenes para que su chat abra el privado
	HiloServidor usuario;
	try {
		System.out.println("Entra a enviarCrearPrivado");
		System.out.println(amigo+" "+enviador);
		for(int c=0;c<servidor.usuariosActivos.size();c++){
			usuario= servidor.usuariosActivos.get(c);
			System.out.println(usuario.nombreUsuario);
			System.out.println(usuario);
			if(usuario.nombreUsuario.equals(amigo)){
				System.out.println("Ha encontrado al amigo");
			usuario.DOS2.writeInt(5);
			usuario.DOS2.writeUTF(enviador);
			}
			}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
			
		}
private void enviarMensajeAmigo(String amigo, String mensaje) { //Método que envía a un usuario (hiloCliente) un mensaje por privado
	HiloServidor usuario;
	try {

		for(int c=0;c<servidor.usuariosActivos.size();c++){
		usuario= servidor.usuariosActivos.get(c);
		if(usuario.nombreUsuario.equals(amigo)){
		usuario.DOS2.writeInt(4);
		usuario.DOS2.writeUTF(this.nombreUsuario);
		usuario.DOS2.writeUTF(this.nombreUsuario + mensaje);
		}
		}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
			
		}
private void enviarUsuarioDesconectado(String usuarioDesconectado) {//Método que envía a todos los usuarios el usuario que se ha desconectado
			HiloServidor usuario;
			for(int c=0;c<servidor.usuariosActivos.size();c++){
				usuario = servidor.usuariosActivos.get(c);
				try {
					usuario.DOS2.writeInt(3);
					usuario.DOS2.writeUTF(usuarioDesconectado);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		//		
		private void enviarActivos() { //Método que actualiza la lista de usuarios conectados
			HiloServidor usuario;
			for(int c=0;c<servidor.usuariosActivos.size();c++){
				usuario = servidor.usuariosActivos.get(c);
				for(int i=0;i<servidor.usuariosActivos.size();i++){
				try {
						
						usuario.DOS2.writeInt(2);
						usuario.DOS2.writeUTF(servidor.usuariosActivos.get(i).nombreUsuario);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}	
		}
		public String cogerNombre(){
			return nombreUsuario;
		}
		private void enviarMensaje(String mensaje) { //Método que envia un mensaje en el chat principal para todos los usuarios
			HiloServidor usuario;
			try {

				for(int c=0;c<servidor.usuariosActivos.size();c++){
				usuario= servidor.usuariosActivos.get(c);
				usuario.DOS2.writeInt(1);
				usuario.DOS2.writeUTF(mensaje);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

}
