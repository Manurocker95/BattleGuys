
/*
 * Clase ficha con la informacion individual de cada uno de los elementos interactuables
 * */
public class Actor_Token extends Util_Sprite 
{
	//Tipo actual de la ficha
	public Util_Const.TYPE_SQUARE actualType;
	//Posicion en el eje x e y
	public int row, column;
	//La ficha empieza "viva" y si la comen deja de estarlo
	public boolean alive;
	public String imagePath;
	public int id;
	
	public Actor_Token(int row, int column, Util_Const.TYPE_SQUARE actualType, String _img, int _numFrames, int _frameSize,int _id)
	{
		super (_img, _numFrames, _frameSize, _id);
		this.row = row;
		this.column = column;
		this.actualType = actualType;
		this.imagePath = _img;
		this.id = _id;
		alive = true;
	}
}
