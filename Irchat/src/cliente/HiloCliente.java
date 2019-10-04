package cliente;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import servidor.HiloServidor;
import interfaz.PantallaChat;
import interfaz.PantallaPrivado;

public class HiloCliente extends Thread implements ActionListener {
private String nombreUsuario="";
private PantallaChat pantallaChat;
private PantallaPrivado pantallaPrivado;
private DataInputStream DIS;
private DataOutputStream DOS;
private DataInputStream DIS2;
private Socket socket;
private Socket socket2;
private String host = "";
private int puerto = 5000;
private int puerto2 = 5001;

//Las conversaciones se guardan en un vector (Sincronizado) al que se accede para actualizar la pantalla de chat
public static Vector<String> conversacion = new Vector<String>();
		public HiloCliente(String nombreUsuario,String host){
			this.nombreUsuario = nombreUsuario;
			this.host = host;

				try {
					//Se crean los sockets creando la conexión con el HiloServidor
					socket = new Socket(host,puerto);
					socket2 = new Socket(host,puerto2);
					DOS = new DataOutputStream(socket.getOutputStream());
					DIS = new DataInputStream(socket.getInputStream());
					DOS.writeUTF(nombreUsuario);
					
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//Se inicia la pantalla del chat y se añaden los listeners de los botones
			pantallaChat = new PantallaChat(this);
			pantallaChat.servidor.setText(host);
			pantallaChat.nombre.setText(nombreUsuario);
			pantallaChat.setVisible(true);
			pantallaChat.entrada.addActionListener(this);
			for(int c=0;c<pantallaChat.botonesUsuarios.size();c++){
				pantallaChat.botonesUsuarios.get(c).addActionListener(this);
			}
			
		}
		public void run(){
			//Parte principal del hilo
			try {
				
				DIS2 = new DataInputStream(socket2.getInputStream());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			int opcion =0;
			String mensaje ="";
			String usuario ="";
			String amigoEnvio="";
			while(true){
				try {
					opcion = DIS2.readInt();
					switch(opcion){
					case 1://Recibir mensaje de chat principal
						mensaje = DIS2.readUTF();
						conversacion.add(mensaje);
						mostrarConversacion();
						
						break;
					case 2://agregar un nuevo usuario que se conecta.
						usuario = DIS2.readUTF();
						System.out.println("Se ha enviado a Hilo Cliente: "+usuario);
						pantallaChat.agregarUsuarios(usuario);
						break;
					case 3://Eliminar un usuario que se ha desconectado
						usuario = DIS2.readUTF();
						pantallaChat.borrarUsuario(usuario);
						break;
					case 4://Recibir mensaje privado de un amigo
						System.out.println("entra al caso 4");
						amigoEnvio = DIS2.readUTF();
						mensaje = DIS2.readUTF();
						System.out.println(mensaje);
						pantallaChat.añadirMensajeAPantallaprivada(mensaje);
						break;
					case 5://Recibir solicitud de Crear un privado de un amigo
						System.out.println("Ha entrado al caso 5 del hilo de cliente");
						usuario = DIS2.readUTF();
						System.out.println(usuario);
						JOptionPane.showMessageDialog(null, "Petición de privado por: "+usuario);
						pantallaChat.abrirPrivado(usuario);
						break;
				}
					
					
					
					
					
					
				} catch (IOException e) {
					e.printStackTrace();
					//Este error da a entender que se ha perdido la conexión con el servidor siendo la culpa del servidor
					JOptionPane.showMessageDialog(null, "Lo siento el servidor se ha desconectado");
					break;
				}
			}
		}
		
		

		private void mostrarConversacion() { //Actualiza la pantalla del chat con la conversacion
			String conv ="";
			try {
				for(int c=conversacion.size();c>0;c--){
					conv = conv + conversacion.get(c-1) + "\n";
				}
				pantallaChat.chat.setText(conv);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
		}
		@Override
		public void actionPerformed(ActionEvent e) {
				String hora = obtenerHora();
				System.out.println("aASDASDASDASDS");
			if(e.getSource().equals(pantallaChat.entrada)){ //Envio de lo escrito en la entrada de texto del chat
				
				if(!pantallaChat.entrada.getText().equals("")){
					enviarDato(hora);	
				}
			}
		}
		public void enviarDatoPrivado(String hora,String amigo,String mensaje) { //Método para enviar un mensaje por privado
			try {
				
				DOS.writeInt(4);
				DOS.writeUTF(amigo);
				DOS.writeUTF(hora+" / "+nombreUsuario+" --> "+mensaje);
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
			
		}
		private void enviarDato(String hora) { //Método para enviar un dato al chat principal
			try {
				
				DOS.writeInt(1);
				DOS.writeUTF(hora+" / "+nombreUsuario+" --> "+pantallaChat.entrada.getText());
				pantallaChat.entrada.setText("");
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
			
		}
		public String obtenerHora() { //Obtener hora actual
			String horaFinal ="";
			Calendar fecha = new GregorianCalendar();
			int hora = fecha.get(Calendar.HOUR_OF_DAY);
	        int minuto = fecha.get(Calendar.MINUTE);
	        int segundo = fecha.get(Calendar.SECOND);
	        horaFinal = Integer.toString(hora) +":"+ Integer.toString(minuto) +":"+ Integer.toString(segundo);
	        return horaFinal;
			
		}

		public String obtenerNombre(){
			return nombreUsuario;
		}
		public void enviarSolicitudPrivado(String nombreCreador,String nombreObjetivo) { //Envía a una persona una peticion para crear un privado entre los dos
			try {
				System.out.println("Entra a enviarSolicitudPrivado");
				DOS.writeInt(5);
				DOS.writeUTF(nombreObjetivo);
				DOS.writeUTF(nombreCreador);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
}
