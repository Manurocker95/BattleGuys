import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.Timer;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/*======================================================================================================================//
 * 																													    *
 *  		Clase de Pantalla de GameOver: Es la escena que sale cuando pierdes la partida.							    *
 *  																													*
 * 		 Hecho para Wee por: Manuel Rodríguez Matesanz, Marcos López Tabernero y Diego del Castillo Torguet				*																	*
 *======================================================================================================================*/

public class Scene_Ending extends Scene implements ActionListener
{

	//===================================================================================================================//
	// 											**Variables**															 //
	//===================================================================================================================//
	
	private Image m_imBackground;		//Imagen de fondo 
	private Font m_fFont;				//Fuente que se usa en los textos de esta escena
	private TAdapter t;					//Adapter para las teclas
    private Timer m_timer;				//Timer para las acciones del JFrame
    
 // NOTA: Creamos un Timer, que cada DELAY milisegundos, lanzará una acción, y llamará al método actionPerformed para calcular movimientos y repintar.
    
	//===================================================================================================================//
	// 											**Constructores**														 //
	//===================================================================================================================//
    
	public Scene_Ending()
	{
		ImageIcon ii = new ImageIcon(Util_Const.endBGImage);	//Asignamos la imagen a un icono
		m_imBackground = ii.getImage();					//Asignamos la imagen a nuestra variable
		m_fFont =new Font("Impact",Font.BOLD,25);		//Asignamos la fuente deseada
		t = new TAdapter();								//Inicializamos el TAdapter
		setFoc(true);									//Ponemos el foco de pintado en esta escena
		m_timer = new Timer(Util_Const.DELAY, this); 				//Inicializamos el timer según el delay
		m_timer.start();		
	}
	
	//===================================================================================================================//
	// 							**Método que añade un KeyListener y el foco a la escena**								 //
	//===================================================================================================================//
	
	public void setFoc(boolean focusable)
	{
		setFocusable(focusable);
		if (!focusable)
		{
			removeKeyListener(t);
		}
		else
		{
			addKeyListener(t);
		}
	}
	
	//===================================================================================================================//
	// 							**Método que se llama cada frame para pintar en escena**								 //
	//===================================================================================================================//
	
    @Override
    public void paintComponent(Graphics g) 
    {	// Método que será llamado cuando Java determina que hay que pintar el escenario.
        super.paintComponent(g);				// Llamamos al método de su padre...

        doDrawing(g);							// ... y añadimos nuestra función de pintado de elementos del escenario.

        Toolkit.getDefaultToolkit().sync();		// Forzamos el dibujado de todos los elementos de forma adecuada. Necesario para algunos sistemas.
    }
    
	//===================================================================================================================//
	// 							**Property de TAdapter**																 //
	//===================================================================================================================//
    
    public TAdapter getTAdapter()
    {
    	return t;
    }

	//===================================================================================================================//
	// 							**Método que pinta. Se llama desde paintComponent**										 //
	//===================================================================================================================//
    
    private void doDrawing(Graphics g)
    {
    	
    	Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(m_imBackground, 0, 0, this); 
        g2d.setColor(new Color(255,255,255));
        
        g2d.setFont(m_fFont);
        g2d.drawString("Press Enter to go to titlescreen", 330, 560);
        
    }

	//===================================================================================================================//
	// 							**Método que se llama cada frame para realizar acciones**								 //
	//===================================================================================================================//
    
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		repaint(); //Cada frame repintamos
	}
	
    private class TAdapter extends KeyAdapter 
    {
    	@Override
    	public void keyReleased(KeyEvent e) 
    	{
        	int key = e.getKeyCode();
        	if (key == KeyEvent.VK_SPACE)
        		{
        			
        		};
    	}
    	@Override
    	public void keyPressed(KeyEvent e) 
    	{
        	int key = e.getKeyCode();
        	if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_SPACE)
        	{
        		try 
        		{
					SceneManager.Instance().loadScene(1); //Cargamos la escena de la portada al pulsar Espacio
					m_timer.stop();
				} catch (ParserConfigurationException | SAXException | IOException | InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (FontFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}
    	}
	}
}
