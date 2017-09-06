import java.awt.Image;
import javax.swing.ImageIcon;

/*======================================================================================================================//
 * 																													    *
 *  		Clase Sprite: De ella heredan los personajes. Tiene control sobre las imagenes de los mismos.			    *
 *  																													*
 * 		 Hecho para Wee por: Manuel Rodríguez Matesanz, Marcos López Tabernero y Diego del Castillo Torguet				*																	*
 *======================================================================================================================*/

public class Util_Sprite 
{

	//===================================================================================================================//
	// 											**Variables**															 //
	//===================================================================================================================//
	
  protected Image m_sprite; 		  // SpriteSheet de la que se recogen las imagenes.
  protected int[] m_offsets;  	  	  // Array de posiciones X e Y donde esta situado cada frame.
  protected int m_frameSize;  	      // Tamaño del frame.
  protected int m_currentFrame = 0;   // Frame actual de la animación.
  protected int m_id; 			      // Número correspondiente a cada frame.
  protected int m_numFrames; 		  // Número de frames de cada animación.
  
  public enum spriteState{ FLY, DESTROYED } 		    // Estados del personaje en cuanto a su posición y estado en el que comieza.
  protected spriteState m_state = spriteState.FLY;	// Estado actual del sprite
  
  
	//===================================================================================================================//
	// 											**Constructores**														 //
	//===================================================================================================================//
  
  public Util_Sprite()
  {
	  
  }
  
  public Util_Sprite(Image sprite, int numFrames, int frameSize, int id) // Constructor en caso de que la imagen no sea un SpriteSheet.
  {
	  this.m_sprite = sprite;			//Asignamos el sprite. Es una imagen con varios frames
	  this.m_frameSize = frameSize;	//Asignamos el tamaño de cada frame según la imagen con los frames
	  this.m_numFrames = numFrames;	//Asignamos el número de frames que dispone la animacion
	  this.m_id = id;					//Asignamos un id para varios personajes con una misma imagen
	  setOffsets();					//Asignamos los offsets según corresponda en la imagen
  }
  
  public Util_Sprite(String sprite, int numFrames, int frameSize, int id) // Constructor de la clase Sprite.
  {
	  ImageIcon ii = new ImageIcon(sprite); // Recogemos la imagen que le pasa el hijo como string 
      Image FinalSprite = ii.getImage();	// Recogemos la imagen de ese icóno
	  
      this.m_sprite = FinalSprite;	//Asignamos el sprite. Es una imagen con varios frames
	  this.m_frameSize = frameSize;	//Asignamos el tamaño de cada frame según la imagen con los frames
	  this.m_numFrames = numFrames;	//Asignamos el número de frames que dispone la animacion
	  this.m_id = id;					//Asignamos un id para varios personajes con una misma imagen. Es donde empieza su offset.
	  setOffsets();					//Asignamos los offsets según corresponda en la imagen
  }
  
	//===================================================================================================================//
	// 								**Property para el tamaño de los frames**											 //
	//===================================================================================================================//
  
  public void setFrameSize(int newSize)
  { 
	  this.m_frameSize = newSize;		//Establecemos el tamaño de los frames
  }
  
  public int getFrameSize()
  { 
	  return this.m_frameSize;			//Devolvemos el tamaño actual de los frames
  }
  
	//===================================================================================================================//
	// 								**Property para el estado del sprite**												 //
	//===================================================================================================================//
  
  public void setSpriteState(spriteState newState)
  { 
	  this.m_state = newState;		  //Establecemos el estado del personaje
  }
  
  public spriteState getSpriteState() //Devolvemos el estado del personaje.
  {
	  return this.m_state;
  }
  
	//===================================================================================================================//
	// 								**Property para el estado del sprite**												 //
	//===================================================================================================================//
  
  public int getCurrentFrame(){ // Método get para la variable currentFrame.
	  return this.m_currentFrame;
  }
  
  public void setCurrentFrame(int frame){ // Método set para la variable currentFrame.
	  this.m_currentFrame = frame;
  }
  
	//===================================================================================================================//
	// 					**Método que asigna los offset en la imagen con frames para la animación**						 //
	//===================================================================================================================//
  
  public void setOffsets() // Añadimos cada posición de los frames de la animación a un array de acuerdo al numero de frames de la misma.
  {
	  this.m_offsets = new int [this.m_numFrames];	//Tenemos tantos offsets como número de frames
	  int j = 0;
	  
	  for(int i = this.m_id; i < this.m_id + this.m_numFrames; i++)
	  {
		  this.m_offsets[j] = i; 					//Asignamos los offsets.
		  j++;
	  }

  }

	//===================================================================================================================//
	// 								**Property para devolver los offsets de la animación**								 //
	//===================================================================================================================//
  
  public int [] getOffsets() 
  {
	  return this.m_offsets;		// Devolvemos las posiciones de los frames. (Offsets)
  }
  
	//===================================================================================================================//
	// 								**Property para la imagen del personaje**											 //
	//===================================================================================================================//
  
  public void setSprite(Image sprite) 
  {
	  this.m_sprite = sprite;		// Asignamos el sprite
  }
  
  public Image getSprite() 
  {
	  return this.m_sprite;			// Devolvemos el sprite
  }
  
	//===================================================================================================================//
	// 								**Property para el número de frames**												 //
	//===================================================================================================================//  
  
  public void setNumFrames(int newNumFrames) 
  {
	  this.m_numFrames = newNumFrames;		//Asignamos el número de Frames
  }
  
  public int getNumFrames() 
  {
	  return this.m_numFrames;				//Devolvemos el número de Frames
  } 
}