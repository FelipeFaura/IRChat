package interfaz;

import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import cliente.HiloCliente;

public class PantallaPrivado extends JFrame implements ActionListener{
	private String nombre;
	public JLabel et1,eObjetivo;
	public TextField entrada;
	public TextArea chat;
	private HiloCliente cliente;
	private PantallaChat pantallaChat;
	private Vector<String> conversacion = new Vector<String>();
	public PantallaPrivado(String nombre,HiloCliente cliente,PantallaChat pantallachat){
		setSize(400,400);
		setLocationRelativeTo(this);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		
		this.nombre = nombre;
		this.cliente = cliente;
		this.pantallaChat = pantallachat;
		this.setTitle(nombre);
		//Barra inferior de insercion del texto
		barraInferior();
		entrada.setBounds(1, 337, 380, 23);
		//Pantalla de chat
		chat();
		chat.setBounds(1, 20, 380, 317);
		//etiquetas
		etiquetas();
		et1.setBounds(100, 1, 100, 23);
		eObjetivo.setBounds(220, 1, 100, 23);
		//------
		entrada.addActionListener(this);
		setVisible(true);
	}

	private void chat() {
		chat = new TextArea();
		add(chat);
	}
	private void etiquetas(){
		et1 = new JLabel("Privado con: ");
		eObjetivo = new JLabel(nombre);
		add(et1);
		add(eObjetivo);
	}

	private void barraInferior() {
		entrada = new TextField();
		add(entrada);
		
	}
	public static void main (String [] args){
	}


	private String obtenerHora() {
		String horaFinal ="";
		Calendar fecha = new GregorianCalendar();
		int hora = fecha.get(Calendar.HOUR_OF_DAY);
        int minuto = fecha.get(Calendar.MINUTE);
        int segundo = fecha.get(Calendar.SECOND);
        horaFinal = Integer.toString(hora) +":"+ Integer.toString(minuto) +":"+ Integer.toString(segundo);
        return horaFinal;
	}

	public void añadirMensaje(String mensaje) { //añade el mensaje recibido a la pantalla
		conversacion.add(mensaje);
		mostrarMensaje();
		
	}

	private void mostrarMensaje() { //Actualiza la pantalla del privado
		String conv ="";
		try {
			for(int c=conversacion.size();c>0;c--){
				conv = conv + conversacion.get(c-1) + "\n";
			}
			chat.setText(conv);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) { //Desde aqui se envía el mensaje que se quiere enviar en un privado
		if(e.getSource().equals(entrada)){
			String hora = obtenerHora();
			String amigo = eObjetivo.getText();
			String mensaje = entrada.getText();
			if(!entrada.getText().equals("")){
				conversacion.add(hora+" / "+"Yo"+" --> "+mensaje);
				mostrarMensaje();
				cliente.enviarDatoPrivado(hora,amigo,mensaje);	
				entrada.setText("");
			}
		}
		
	}
	
}
