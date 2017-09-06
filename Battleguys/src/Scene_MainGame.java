import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter; 
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

//ActionListener nos permitirá responder a eventos que se producen.
/*======================================================================================================================//
 * 																													    *
 *  		Clase Escenario/Juego principal. Es donde se juega realmente. Todo el core del juego está aquí.			    *
 *  								Es un tipo de escena y tiene escuchadores de eventos y acciones.					*
 *  			El juego consiste en salvar a los prisioneros, dejándolos en las plataformas de salvamento.				*
 * 		 Hecho para Wee por: Manuel Rodríguez Matesanz, Marcos López Tabernero y Diego del Castillo Torguet				*																	*
 *======================================================================================================================*/

public class Scene_MainGame extends Scene implements ActionListener 
{

	//===================================================================================================================//
	// 											**Variables**															 //
	//===================================================================================================================//
	
	
	private static Random rnd = new Random();				//Random para generar números aleatorios
    private Timer timer;									//Timer, permite tener un contador en milisegundos para JFrame
   
    private TAdapter t;						//TAdapter de la clase
    /*private Color textColor;				//Color del texto
    private Font font;						//Fuente del texto*/

    private Image bg,textimg;			//Imagen de fondo
    private Image boardImage, winner1, winner2;	//Imagen de tablero;	//Imagen de tablero
    private Actor_Board board;	//Tablero con toda la información y funcionalidad
	private int iaTime = Util_Const.IA_WAITINGTIME;
    
	private boolean selectingPlayers = true; 
	
	//===================================================================================================================//
	// 											**Constructor**														 //
	//===================================================================================================================//
    
    public Scene_MainGame()
    {
    	//Inicializamos los valores necesarios
    	/*textColor = new Color(255,255,255);
    	font = new Font("Impact",Font.BOLD,20);*/
    	rnd.setSeed(System.currentTimeMillis());
    	
    	SelectPlayers();
    	
    	t = new TAdapter();
    	setFoc(true);
    	timer = new Timer(Util_Const.DELAY, this); 	// Creamos un Timer, que cada DELAY milisegundos, lanzará una acción, y llamará al método actionPerformed para calcular movimientos y repintar.
    	timer.start();								// Activamos el Timer. 
    }
    
    void initialize(boolean againstIA)
    {
        ImageIcon ii = new ImageIcon(Util_Const.boardImg);
        boardImage = ii.getImage();
        
        ImageIcon w1 = new ImageIcon(Util_Const.winner1);
        winner1 = w1.getImage();
		
		ImageIcon w2 = new ImageIcon(Util_Const.winner2);
		winner2 = w2.getImage();
        
        board = new Actor_Board(rnd, againstIA);
    }
    
    void SelectPlayers()
    {
    	ImageIcon ii = new ImageIcon(Util_Const.gameBGImage); // Recogemos la imagen que le pasamos como string
        bg = ii.getImage();	// Recogemos la imagen de ese icóno
       
        ImageIcon i = new ImageIcon(Util_Const.selec); // Recogemos la imagen que le pasamos como string
        textimg = i.getImage();
    }
    
    @Override
    // Cuando se produce una acción, con cada tic del timer, Java invocará este método. 
    public void actionPerformed(ActionEvent e) 
    {	
        repaint();  
        Update();
    }
    
    private void Update()
    {
    	if (!selectingPlayers)
    	{
	    	if(board.isPlaying())
			{
				if (board.IAActivated() && board.getTurnsPlayer() != 0) // Turno de la IA
				{
					if (board.waitingForIA)
					{
						if (iaTime > 0)
						{
							iaTime -= Util_Const.DELAY;
						}
						else
						{
							board.IAInput();
							iaTime = Util_Const.IA_WAITINGTIME;
							board.waitingForIA = false;
						}
						
					}
				}
			}
    	}
		
			
    }
    
    private void doDrawing(Graphics g)
    {
    	Graphics2D g2d = (Graphics2D) g;

        if (!selectingPlayers)
        {
	        g2d.drawImage(boardImage, 0, 0, this);
	        
	        board.paint(g2d, this);
	        
	        if (!board.isPlaying())
	        {
	        	if (board.winner == 0)
	        		g2d.drawImage(winner1, 280, 300, this);
	        	else
	        		g2d.drawImage(winner2, 280, 300, this);
	        }
        }
        else
        {
        	//imagen del fondo (Espacio)
        	g2d.drawImage(bg, 0, 0, this);
        	g2d.drawImage(textimg,120,230, this);
        }

    }
    
    @Override
    public void paintComponent(Graphics g) 
    {	// Método que será llamado cuando Java determina que hay que pintar el escenario.
        super.paintComponent(g);				// Llamamos al método de su padre...

        doDrawing(g);							// ... y añadimos nuestra función de pintado de elementos del escenario.

        Toolkit.getDefaultToolkit().sync();		// Forzamos el dibujado de todos los elementos de forma adecuada. Necesario para algunos sistemas.
    }
    
    private class TAdapter extends KeyAdapter 
    {
    	@Override
    	public void keyPressed(KeyEvent e) 
    	{
    		if (selectingPlayers)
    		{
    			switch(e.getKeyCode())
    			{
    			case KeyEvent.VK_F1:
    	    		selectingPlayers = false;
    	    		initialize(false);
    	    		break;
    			case KeyEvent.VK_F2:
    	    		selectingPlayers = false;
    	    		initialize(true);
    	    		break;
    			}
    		}
    		else
    		{
    			if(board.isPlaying())
        		{
        			// Si estamos en modo dos jugadores
        			if (!board.IAActivated())
        			{
        				// Solo recibimos inputs de teclado
        				board.input(e.getKeyCode());
        			}
        			else // Si estamos contra la IA
        			{
        				// Si es turno del jugador, damos pie al input, sino, lo decide la máquina
        				if (board.getTurnsPlayer() == 0)
        				{
        					board.input(e.getKeyCode());
        				}
        			}
        		}
        			
        		else{
        			if(e.getKeyCode() == KeyEvent.VK_ENTER){
        				try {
                			timer.stop();
        					SceneManager.Instance().loadScene(3);
        				} catch (ParserConfigurationException | SAXException | IOException | InterruptedException
        						| FontFormatException e1) {
        					e1.printStackTrace();
        				}
        			}
        		}
    		}
    		
    	}
    }

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
    
    public static int GetRandomInt(int max)
    {
    	return rnd.nextInt(max);
    }
}