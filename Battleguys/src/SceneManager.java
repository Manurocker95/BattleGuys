import java.awt.FontFormatException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/*======================================================================================================================//
 * 																													    *
 *  		Clase Controladora de escenas. Ella pone en juego las escena. Al tener un singleton, solo hay una		    *
 *  			instancia de esta clase, que se llama desde el juego cuando es necesario o desde otras escenas.			*
 * 		 Hecho para Wee por: Manuel Rodríguez Matesanz, Marcos López Tabernero y Diego del Castillo Torguet				*																	
 *======================================================================================================================*/

public class SceneManager extends JFrame
{
	
	//===================================================================================================================//
	// 											**Variables**															 //
	//===================================================================================================================//
	
	private static SceneManager instance; //Instancia singleton
	public static SceneManager Instance;
	private Game m_gameReference;		  //Referencia a la clase Game para añadir escenas al JFrame
	private Scene_Titlescreen p;				  //Referencia a la portada/TitleScreen
	private Scene_MainGame es;				  //Referencia a Escenario/MainGame. Se elimina el objeto anterior del recipiente y se crea uno nuevo al cargar la escena.
	private Scene_Ending en;					  //Referencia a la pantalla de Game Over.
	private Util_Music music;
	
	boolean m_musicON = false; 			  // Comprobador de si la música esta ya activa. 
	
	
	//===================================================================================================================//
	// 										**Constructor Singleton**													 //
	//===================================================================================================================//
	
	public static SceneManager Instance()
	{
		return instance;
	}	
	
	public SceneManager()
	{
		if (instance == null)
		{
			instance = this;		
		}
		else
		{
			System.out.println("Ya hay una instancia del scene manager");
			return;
		}
	}
	
	//===================================================================================================================//
	// 										**Método que guarda la referencia a juego**									 //
	//===================================================================================================================//
	
	public void game(Game game)
	{
		m_gameReference = game;		//Asignamos la referencia a la clase Game
	}

	//===================================================================================================================//
	// 									**Método que carga la escena que queramos**										 //
	//===================================================================================================================//
	
	public void loadScene(int id) throws ParserConfigurationException, SAXException, IOException, InterruptedException, FontFormatException
	{
		ArrayList worldInfo = new ArrayList<String>(); //Guardamos la info del xml y se la mandamos a Escenario/MainGame
		switch (id)
		{
			case 1: //portada
				
				if (music != null)
					music.stop();
				music = new Util_Music(Util_Const.startBGM);			//Reproducimos la música de fondo
				music.loop();							//La ponemos en loop
				
				if (es != null)
				{
					es.setFoc(false); 	//Quitamos los focus si existen para que no se activen los listeners
					en.setFoc(false);	//Quitamos los focus si existen para que no se activen los listeners
				}
				p = new Scene_Titlescreen();
				m_gameReference.removeAllScenesAndAdd(p); 
				break;
				
			case 2: //Juego
				
				music.stop();
				music = new Util_Music(Util_Const.gameBGM);	//Reproducimos la música de fondo
				music.loop();	
				p.setFoc(false);
				if (en != null)
					en.setFoc(false);
				System.out.println("Loading Game");
				es = new Scene_MainGame(); 
		    	es.setFoc(true);
		    	m_gameReference.removeAllScenesAndAdd(es);
				break;
				
			case 3: //Final = GameOver
				
				music.stop();
				music = new Util_Music(Util_Const.endBGM);	//Reproducimos la música de fondo
				music.loop();
				p.setFoc(false); //Quitamos los focus
				es.setFoc(false);
				en = new Scene_Ending();
				en.setFoc(true);
				m_gameReference.removeAllScenesAndAdd(en);
				break;
		}
	}
}
