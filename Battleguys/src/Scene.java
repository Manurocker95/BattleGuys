import javax.swing.JPanel;

/*======================================================================================================================//
 * 																													    *
 *  		Clase Escena: Se preparaba para que el scene manager no dependiera de Game. De ella heredan las escenas.    *
 *  						Ej: Portada, Escenario, Ending...															*
 * 		 Hecho para Wee por: Manuel Rodríguez Matesanz, Marcos López Tabernero y Diego del Castillo Torguet				*																	*
 *======================================================================================================================*/

abstract class Scene extends JPanel 
{
	//===================================================================================================================//
	// 											**Variables**															 //
	//===================================================================================================================//
	
	private int id;
	
	//===================================================================================================================//
	// 								**Properties que heredan los hijos**												 //
	//===================================================================================================================//
	
	public int getID() { return id;}
	public void setID(int newid){id = newid;}
}
