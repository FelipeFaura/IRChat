package interfaz;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class PantallaConexion extends JFrame {
	private JLabel lb2,lb3;
	public JTextField direccion,nickName;
	public JButton conectar;
	
	public PantallaConexion(){
		//pantalla lineas generales
		setSize(200, 200);
		setLocationRelativeTo(this);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new GridLayout(7,1));
		//creación de los elementos.
		lb2 = new JLabel("Dirección IP");
		lb3 = new JLabel("NickName");
		direccion = new JTextField();
		nickName = new JTextField();
		conectar = new JButton("Conectar");
		lb2.setHorizontalAlignment((int) CENTER_ALIGNMENT);
		lb3.setHorizontalAlignment((int) CENTER_ALIGNMENT);
		add(lb3);
		add(nickName);
		add(lb2);
		add(direccion);
		add(conectar);
		
	}
}
