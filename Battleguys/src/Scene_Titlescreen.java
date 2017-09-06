import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter; 
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.Timer;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/*======================================================================================================================//
 * 																													    *
 *  		Clase Jugador: Hereda de personaje. Se controla por las flechas del ratón 								    *
 *  																													*
 * 		 Hecho para Wee por: Manuel Rodríguez Matesanz, Marcos López Tabernero y Diego del Castillo Torguet				*																	*
 *======================================================================================================================*/

public class Scene_Titlescreen extends Scene implements ActionListener
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
	public Scene_Titlescreen() throws FontFormatException, IOException
	{
		ImageIcon ii = new ImageIcon(Util_Const.titleScreenBGImage);
		m_imBackground = ii.getImage();
		m_fFont = new Font("space age.ttf",Font.BOLD,30);
		
		t = new TAdapter();
		
		setFoc(true);
		m_timer = new Timer(Util_Const.DELAY, this); 	// Creamos un Timer, que cada DELAY milisegundos, lanzará una acción, y llamará al método actionPerformed para calcular movimientos y repintar.
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
        g2d.setColor(new Color(0,0,0));
        g2d.setFont(m_fFont);
        g2d.drawString("Press Enter to play", 380, 610);
        
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
        	if (key == KeyEvent.VK_ENTER)
        		{
        			
        		};
    	}
    	@Override
    	public void keyPressed(KeyEvent e) 
    	{
        	int key = e.getKeyCode();
        	if (key == KeyEvent.VK_ENTER)
        	{
        		try 
        		{			
        			e.setKeyCode(40);
					SceneManager.Instance().loadScene(2);
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
