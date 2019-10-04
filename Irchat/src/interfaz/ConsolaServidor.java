package interfaz;

import java.awt.FlowLayout;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
//Consola para que el servidor registre lo que ocurre con la conexión o desconexion de usuarios
public class ConsolaServidor extends JFrame{
	private JTextArea consola;
	private JPanel p1;
	private Vector<String> Log = new Vector<String>();
	public ConsolaServidor(){
		setSize(550, 700);
		setLocation(1, 1);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new FlowLayout());
		
		p1 = new JPanel();
		consola = new JTextArea(40,43);
		consola.setEditable(false);
		p1.add(consola);
		add(p1);
		setVisible(true);
		
		
	}

	public static void main (String[]args){
		ConsolaServidor c = new ConsolaServidor();
		
	}
	public void mostrarLog(){ //Actualiza la consola para mostrar los nuevos mensajes
		String conv = "";
		try {
			for(int c=Log.size();c>0;c--){
				conv = conv + Log.get(c-1) + "\n";
			}
			consola.setText(conv);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void añadirLog(String info){ //Añade un nuevo registro al log
		Log.add(info);
		mostrarLog();
	}
}


