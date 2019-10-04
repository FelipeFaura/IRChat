package cliente;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

import servidor.HiloServidor;
import interfaz.*;

public class Cliente implements ActionListener {
	PantallaConexion pantallaEntrada;
	int puerto=5000;
	String host = "";
	String nombreUsuario ="";
	
	
	public Cliente(){
		//Cliente presenta una primera pantalla para introducir los datos de la conexión, tras esto aparecerá el chat
		pantallaEntrada = new PantallaConexion();
		pantallaEntrada.setVisible(true);
		//listener
		pantallaEntrada.conectar.addActionListener(this);
		pantallaEntrada.direccion.addActionListener(this);
	}
	
	public void iniciarCliente(String host) throws UnknownHostException, IOException{
			//Generador de hilos para conexión con sus respectivos hilosServidor
			HiloCliente hilo;
			hilo = new HiloCliente(nombreUsuario,host);
			hilo.start();
		}
		
		
	public static void main (String [] args){
		Cliente c = new Cliente();
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) { // ordenes de la pantalla de conexión
		if (e.getSource().equals(pantallaEntrada.conectar)||e.getSource().equals(pantallaEntrada.direccion)){
			host = pantallaEntrada.direccion.getText();
			nombreUsuario = pantallaEntrada.nickName.getText();
			pantallaEntrada.setVisible(false);
			try {
				iniciarCliente(host);
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
}
