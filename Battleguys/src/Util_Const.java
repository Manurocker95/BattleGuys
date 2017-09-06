public class Util_Const 
{
	
// Settings/ Variables configurables
	
public static final int MAX_PROFUNDIDAD = 1;					// Máxima profundidad a la que llega la recursión
public static final int MAS_INFINITO = Integer.MAX_VALUE;
public static final int MENOS_INFINITO = Integer.MIN_VALUE;
	
 public static final int WINDOW_WIDTH = 1024;
 public static final int WINDOW_HEIGHT = 768;
 
 public static final int BOARDWIDTH = 5;
 public static final int BOARDHEIGHT = 5;

 public static final int EATSCORE = 2; 			// El jugador IA come ficha
 public static final int NEUTRALSCORE = 0;		// el movimiento es neutro
 public static final int EATENSCORE = -1;		// El jugador IA es comido
 public static final int EATANDEATENSCORE = 1;	// Ambos jugadores se comen
 
 public static final int DELAY = 24;							//retraso para refrescar el JFrame
 public static final int IA_WAITINGTIME = 1000;		// Frames
 public static final int tokenSize = 99;
 public static final int boardOffsetX = 262;
 public static final int boardOffsetY = 134;
 
 //Una ficha de misma forma pero diferente jugador ha de ser considerado diferente para el MINIMAX
 public static enum TYPE_SQUARE {EMPTY, BLOCKED, A_TRIANGLE, A_CIRCLE, A_RECTANGLE, B_TRIANGLE, B_CIRCLE, B_RECTANGLE};
 
// Rutas para los sprites
 public static final String boardImg = "Graphics/Sprites/tablero.png"; 
 public static final String gameBGImage = "Graphics/Legacy/Background/bg.png";
 public static final String titleScreenBGImage = "Graphics/Legacy/Background/titlescreen.png"; //Ruta de la imagen de fondo
 public static final String endBGImage = "Graphics/Legacy/Background/end.png"; //Ruta de la imagen de fondo
 public static final String tokenAImg = "Graphics/Legacy/Sprites/leaderShip.png";
 public static final String tokenBImg = "Graphics/Legacy/Sprites/regularShip.png";
 public static final String tokenBlock = "Graphics/Sprites/nblock.png";
 public static final String tokenACircle = "Graphics/Sprites/circA.png";
 public static final String tokenARectangle = "Graphics/Sprites/cuadA.png";
 public static final String tokenATriangle = "Graphics/Sprites/trianA.png";
 public static final String tokenBCircle = "Graphics/Sprites/circB.png";
 public static final String tokenBRectangle = "Graphics/Sprites/cuadB.png";
 public static final String tokenBTriangle = "Graphics/Sprites/trianB.png";
 public static final String tokenPossible = "Graphics/Sprites/nposible.png";
 public static final String upArrow = "Graphics/Sprites/upArrow.png";
 public static final String rightArrow = "Graphics/Sprites/rightArrow.png";
 public static final String leftArrow = "Graphics/Sprites/leftArrow.png";
 public static final String downArrow = "Graphics/Sprites/downArrow.png";
 public static final String controls = "Graphics/Sprites/controls.png";
 public static final String controls2 = "Graphics/Sprites/controls2.png";
 public static final String turn1 = "Graphics/Sprites/turn1.png";
 public static final String turn2 = "Graphics/Sprites/turn2.png";
 public static final String winner1 = "Graphics/Sprites/winner1.png";
 public static final String winner2 = "Graphics/Sprites/winner2.png";
 public static final String selec = "Graphics/Sprites/selec.png";
 
 //ombres de la música ya que la ruta no hace falta
 public static final String startBGM = "Start.wav";
 public static final String gameBGM = "music.wav";
 public static final String endBGM = "Game Over.wav";
 
 
}