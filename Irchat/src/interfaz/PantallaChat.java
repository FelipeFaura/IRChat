package interfaz;


import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import cliente.HiloCliente;


public class PantallaChat extends JFrame implements ActionListener {
public JLabel eServidor,servidor,eNombre,nombre,usuarios;
private JPanel bI,c,bD;
public TextField entrada;
public TextArea chat;
private HiloCliente cliente;
public Vector<JButton> botonesUsuarios = new Vector<JButton>(); 
private PantallaPrivado privado;


	public PantallaChat(HiloCliente cliente){
		this.cliente = cliente;
		
		setSize(510, 500);
		setLocationRelativeTo(this);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		
		this.setTitle("Chat");
		
		//Creación de los elementos Barra inferior
		barraInferior();
		bI.setBounds(5,435,394,23);
		//Creación de los elementos Chat
		chat();
		c.setBounds(1, 20, 400, 400);
		//Creación de los elementos Barra derecha
		barraUsuarios();
		bD.setBounds(401, 25, 80, 400);
		//Creación de las etiquetas.
		eServidor = new JLabel("Servidor:");
		eServidor.setBounds(1, 1, 100, 23);
		servidor = new JLabel();
		servidor.setForeground(Color.BLUE);
		servidor.setBounds(100, 1, 100, 23);
		eNombre = new JLabel("NickName");
		eNombre.setBounds(200, 1, 100, 23);
		nombre = new JLabel();
		nombre.setForeground(Color.BLUE);
		nombre.setBounds(300, 1, 100, 23);
		usuarios = new JLabel("usuarios");
		usuarios.setBounds(420, 1, 50, 23);
		add(eServidor);
		add(servidor);
		add(eNombre);
		add(nombre);
		add(usuarios);
	}
	public void barraInferior(){
		bI = new JPanel(new GridLayout(1,1));
		entrada = new TextField();
		bI.add(entrada);
		add(bI);
	}
	public void chat(){
		c = new JPanel();
		chat = new TextArea(25,53);
		c.add(chat);
		add(c);		
	}
	public void barraUsuarios(){
		bD = new JPanel(new FlowLayout());
		JScrollPane scroll = new JScrollPane(bD,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(bD);	
		add(scroll);
	}

	public void agregarUsuarios(String usuario){ //Añadir botones con los usuarios conectados en el chat
		bD.removeAll();
		if(botonesUsuarios.size()!=0){
			boolean comprobacion = comprobarExistentes(usuario);
				if(comprobacion==false){
					botonesUsuarios.add(new JButton(usuario));
				}
				for(int c=0;c<botonesUsuarios.size();c++){
					bD.add(botonesUsuarios.get(c));
					botonesUsuarios.get(c).addActionListener(this);
					
				}
			}
		else{
			botonesUsuarios.add(new JButton(usuario));
			bD.add(botonesUsuarios.get(0));
			botonesUsuarios.get(0).addActionListener(this);
		
			}
		setVisible(true);
		}
	
	public boolean comprobarExistentes(String usuario){ //Comprobar si un usuario está conectado(para no repetirlo)
		boolean comprobacion = false;
		for(int c=0;c<botonesUsuarios.size();c++){
			if(usuario.equals(botonesUsuarios.get(c).getText())){
				comprobacion = true;
			}
		}
		
		return comprobacion;
	}
	public void borrarUsuario(String usuario) { //Eliminar el usuario desconectado de la lista y el chat

		for(int c=0;c<botonesUsuarios.size();c++){
			if(usuario.equals(botonesUsuarios.get(c).getText())){
				botonesUsuarios.remove(c);
			}
		}
		mostrarUsuarios();
		
	}
	private void mostrarUsuarios() { //Mostrar los usuarios en el chat
		bD.removeAll();
		for(int c=0;c<botonesUsuarios.size();c++){
			bD.add(botonesUsuarios.get(c));
		}
		repaint();
		setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) { 
		//Aquí es donde se realiza la acción de querer abrir un chat con una persona
		//Este método controla que el usuario no pueda abrir un privado consigo mismo.
			String nombreObjetivo = "";
			String nombreCreador = cliente.obtenerNombre();
			for(int c=0;c<botonesUsuarios.size();c++){
				if(e.getSource().equals(botonesUsuarios.get(c))){
					nombreObjetivo = botonesUsuarios.get(c).getText();
			}
		}
		if(!nombreObjetivo.equals(nombreCreador)){
			cliente.enviarSolicitudPrivado(nombreCreador,nombreObjetivo);
			abrirPrivado(nombreObjetivo);
		}
		
		
	}
	public void abrirPrivado(String usuario) { //Método para que el usuario abra su privado
		privado = new PantallaPrivado(usuario,cliente,this);
		
	}
	public void añadirMensajeAPantallaprivada(String mensaje) { //Método para añadir un mensaje a la pantalla de privado
		privado.añadirMensaje(mensaje);
		
	}
	
	
		
	

	}

