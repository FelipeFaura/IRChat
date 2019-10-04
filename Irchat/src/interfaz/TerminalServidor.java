package interfaz;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
//Por el momento no tiene utilidad
//la idea de esta clase es controlar a los usuarios directamente desde el servidor
//Como por ejemplo (kick) o (Ban) a usuarios tener una lista de denuncias entre los usuarios etc.
public class TerminalServidor extends JFrame {
	private JTabbedPane tab;
	private JComponent panel1,panel2;
	public TerminalServidor(){
		setSize(300,300);
		setLocation(500, 1);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		tab = new JTabbedPane();
		add(tab);
		setVisible(true);
		
	}
	public static void main (String []args){
		TerminalServidor t = new TerminalServidor();
	}
}
