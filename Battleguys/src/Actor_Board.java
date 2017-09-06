import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;

/*
 * Clase tablero con toda la información y funcionalidad del juego
 * */

public class Actor_Board 
{
	Random rnd;												//Variable random para los enfrentamientos entre piezas iguales
	boolean playing = true;									//Bandera de si el juego ha acabado o no.
	boolean turn = true;									//Turno actual. true=rojas, false=azules
	boolean playAgainstIA = true;							//Bandera de si se juega contra la IA o contra 2 jugadores
	public int winner = -1;
	Util_Const.TYPE_SQUARE [][] board;						//Array con el tipo de ficha que hay en cada posición. No es relevante prácticamente en el juego pero creo que será útil para el MINIMAX
	Actor_Token[] tokens;									//Array de piezas interactuables. 0-2 rojas, 3-5 azules. 
	Actor_Token[] blocks;									//Array de piezas que bloquean el camino.
	
	int boardScore = 0;
	int tokenSelected = -1;									//Pieza seleccionada para considerar movimiento y efectuarlo.
	enum Direction {UP, DOWN, LEFT, RIGHT, NONE} 			//Direcciones posibles.
	Direction directionSelected = Direction.NONE;			//Dirección tomada.
	boolean [] directionsPossible = new boolean[4];			//Direcciones posibles en un instante de tiempo para una pieza concreta.
	Image possible, leftArrow, upArrow, rightArrow, downArrow, controls, turn1, turn2;
	public boolean waitingForIA = false;
	
	int CurrentMovement = 0;
	
	//Constructor
	public Actor_Board(Random rnd, boolean againstIA)
	{
		// Se guarda si se pelea contra la ia o no
		this.playAgainstIA = againstIA;
		
		//Se guarda la referencia al random de la escena
		this.rnd = rnd;
	
		//Pintado de casillas verdes que marcan las direcciones posibles a una ficha concreta.
		ImageIcon ii = new ImageIcon(Util_Const.tokenPossible);
        possible = ii.getImage();
		
		ImageIcon u = new ImageIcon(Util_Const.upArrow);
		upArrow = u.getImage();
			
		ImageIcon r = new ImageIcon(Util_Const.rightArrow);
		rightArrow = r.getImage();
		
		ImageIcon l = new ImageIcon(Util_Const.leftArrow);
		leftArrow = l.getImage();
		
		ImageIcon d = new ImageIcon(Util_Const.downArrow);
		downArrow = d.getImage();
		
		ImageIcon c;
		if (playAgainstIA)
			c = new ImageIcon(Util_Const.controls);
		else
			c = new ImageIcon(Util_Const.controls2);
		
		controls = c.getImage();
		
		ImageIcon t1 = new ImageIcon(Util_Const.turn1);
		turn1 = t1.getImage();
		
		ImageIcon t2 = new ImageIcon(Util_Const.turn2);
		turn2 = t2.getImage();
		
		//Se inicializa el tablero...
		board = new Util_Const.TYPE_SQUARE[Util_Const.BOARDWIDTH][Util_Const.BOARDHEIGHT];
		for(int i = 0; i < 5; i++){
			for(int j = 0; j < 5; j++){
				board[i][j] = Util_Const.TYPE_SQUARE.EMPTY;
			}
		}
		
		//... y sus piezas.
		blocks = new Actor_Token[5];
		tokens = new Actor_Token[6];
		
		int numberOfBlocks = 0, i, j;
		while(numberOfBlocks < 5){
			i = rnd.nextInt(5);
			j = rnd.nextInt(5);
			if(board[i][j] == Util_Const.TYPE_SQUARE.EMPTY){
				board[i][j] = Util_Const.TYPE_SQUARE.BLOCKED;
				blocks[numberOfBlocks] = new Actor_Token(i, j, Util_Const.TYPE_SQUARE.BLOCKED, Util_Const.tokenBlock, 1, Util_Const.tokenSize, 0);
				numberOfBlocks++;
			}
		}
		
		int assignedTokens = 0;
		while(assignedTokens < 6){
			i = rnd.nextInt(5);
			j = rnd.nextInt(5);
			if(board[i][j] == Util_Const.TYPE_SQUARE.EMPTY){
				Util_Const.TYPE_SQUARE nextType = Util_Const.TYPE_SQUARE.EMPTY;
				String imagePath = "";
				switch(assignedTokens){
				case 0:
					nextType = Util_Const.TYPE_SQUARE.A_TRIANGLE;
					imagePath = Util_Const.tokenATriangle;
					break;
				case 1:
					nextType = Util_Const.TYPE_SQUARE.A_CIRCLE;
					imagePath = Util_Const.tokenACircle;
					break;
				case 2:
					nextType = Util_Const.TYPE_SQUARE.A_RECTANGLE;
					imagePath = Util_Const.tokenARectangle;
					break;
				case 3:
					nextType = Util_Const.TYPE_SQUARE.B_TRIANGLE;
					imagePath = Util_Const.tokenBTriangle;
					break;
				case 4:
					nextType = Util_Const.TYPE_SQUARE.B_CIRCLE;
					imagePath = Util_Const.tokenBCircle;
					break;
				case 5:
					nextType = Util_Const.TYPE_SQUARE.B_RECTANGLE;
					imagePath = Util_Const.tokenBRectangle;
					break;
				}
				board[i][j] = nextType;
				tokens[assignedTokens] = new Actor_Token(i, j, nextType, imagePath, 1, Util_Const.tokenSize, 0);
				assignedTokens++;
			}
		}
	}
	
	public Actor_Board(Util_Const.TYPE_SQUARE [][] _board, Actor_Token[] _blocks, Actor_Token [] _tokens, boolean _turn, int movement, Random _rnd)
	{
		CurrentMovement = movement;
		
		board = new Util_Const.TYPE_SQUARE[Util_Const.BOARDWIDTH][Util_Const.BOARDHEIGHT];
		for(int i = 0; i < 5; i++){
			for(int j = 0; j < 5; j++){
				board[i][j] = Util_Const.TYPE_SQUARE.EMPTY;
			}
		}
		
		//... y sus piezas.
		blocks = new Actor_Token[5];
		tokens = new Actor_Token[6];
		
		for (int i = 0; i < 5; i++)
		{
			blocks[i] = new Actor_Token(_blocks[i].row, _blocks[i].column, Util_Const.TYPE_SQUARE.BLOCKED, Util_Const.tokenBlock, 1, Util_Const.tokenSize, 0);
			board[blocks[i].row][blocks[i].column]=Util_Const.TYPE_SQUARE.BLOCKED;
		}
		
		for (int i = 0; i < 6; i++)
		{
			tokens[i] = new Actor_Token(_tokens[i].row, _tokens[i].column, _tokens[i].actualType,_tokens[i].imagePath, 1, Util_Const.tokenSize, _tokens[i].id);
			tokens[i].alive = _tokens[i].alive;
			
			if (tokens[i].alive)
				board[tokens[i].row][tokens[i].column]= _tokens[i].actualType;
		}
		
		turn = false;
		tokenSelected = -1;
		rnd = _rnd;
		
		//Pintado de casillas verdes que marcan las direcciones posibles a una ficha concreta.
		ImageIcon ii = new ImageIcon(Util_Const.tokenPossible);
        possible = ii.getImage();
		
		ImageIcon u = new ImageIcon(Util_Const.upArrow);
		upArrow = u.getImage();
			
		ImageIcon r = new ImageIcon(Util_Const.rightArrow);
		rightArrow = r.getImage();
		
		ImageIcon l = new ImageIcon(Util_Const.leftArrow);
		leftArrow = l.getImage();
		
		ImageIcon d = new ImageIcon(Util_Const.downArrow);
		downArrow = d.getImage();
		
		ImageIcon c;
		if (playAgainstIA)
			c = new ImageIcon(Util_Const.controls);
		else
			c = new ImageIcon(Util_Const.controls2);
		
		controls = c.getImage();
		
		switch(movement)
		{
			case 0:
				tokenSelected = 3;
				if (tokens[tokenSelected].alive)
				{
					calculateDirections();
					
					if (directionsPossible[0])
					{
						directionSelected = Direction.UP;
						moveDirection();
					}
				}
				
				break;
			case 1:
				tokenSelected = 3;
				if (tokens[tokenSelected].alive)
				{
					calculateDirections();
					
					if (directionsPossible[1])
					{
						directionSelected = Direction.DOWN;
						moveDirection();
					}
				}
				break;
			case 2:
				tokenSelected = 3;
				if (tokens[tokenSelected].alive)
				{
					calculateDirections();
					
					if (directionsPossible[2])
					{
						directionSelected = Direction.LEFT;
						moveDirection();
					}
				}
				break;
			case 3:

				tokenSelected = 3;
				if (tokens[tokenSelected].alive)
				{
					calculateDirections();
					
					if (directionsPossible[3])
					{
						directionSelected = Direction.RIGHT;
						moveDirection();
					}
				}
				break;
			case 4:
				tokenSelected = 4;
				if (tokens[tokenSelected].alive)
				{
					calculateDirections();
					
					if (directionsPossible[0])
					{
						directionSelected = Direction.UP;
						moveDirection();
					}
				}
				break;
			case 5:
				tokenSelected = 4;
				if (tokens[tokenSelected].alive)
				{
					calculateDirections();
					
					if (directionsPossible[1])
					{
						directionSelected = Direction.DOWN;
						moveDirection();
					}
				}
				break;
			case 6:
				tokenSelected = 4;
				if (tokens[tokenSelected].alive)
				{
					calculateDirections();
					
					if (directionsPossible[2])
					{
						directionSelected = Direction.LEFT;
						moveDirection();
					}
				}
				break;
			case 7:
				tokenSelected = 4;
				if (tokens[tokenSelected].alive)
				{
					calculateDirections();
					
					if (directionsPossible[3])
					{
						directionSelected = Direction.RIGHT;
						moveDirection();
					}
				}
				break;
			case 8:
				tokenSelected = 5;
				if (tokens[tokenSelected].alive)
				{
					calculateDirections();
					
					if (directionsPossible[0])
					{
						directionSelected = Direction.UP;
						moveDirection();
					}
				}
				break;
			case 9:
				tokenSelected = 5;
				if (tokens[tokenSelected].alive)
				{
					calculateDirections();
					
					if (directionsPossible[1])
					{
						directionSelected = Direction.DOWN;
						moveDirection();
					}
				}
				break;
			case 10:
				tokenSelected = 5;
				if (tokens[tokenSelected].alive)
				{
					calculateDirections();
					
					if (directionsPossible[2])
					{
						directionSelected = Direction.LEFT;
						moveDirection();
					}
				}
				break;
			case 11:
				
				tokenSelected = 5;
				if (tokens[tokenSelected].alive)
				{
					calculateDirections();
					
					if (directionsPossible[3])
					{
						directionSelected = Direction.RIGHT;
						moveDirection();
					}
				}
				break;
			default:
				System.out.println("New Board default");
				break;
			}
		
	}
	
	
	public ArrayList <Integer> PM()
	{
		ArrayList <Integer> ret = new ArrayList<Integer>();
		int counter = 0;
		
		for (int i = 3; i < 6; i++)
		{
			if (tokens[i].alive)
			{
				tokenSelected = i;
				calculateDirections();
				
				for (int j = 0; j < 4; j++)
				{
					if (directionsPossible[j])
					{
						ret.add(counter);
					}
					
					counter++;
				}
			}
		}
		
		return ret;
	}
	
	public int [] PossibleMovements()
	{
		
		int [] possibleMovements = new int [12];
		
		for (int i = 0; i < 12; i++)
		{
			possibleMovements[i] = -1;
		}
		
		int counter = 0;
		
		for (int i = 3; i < 6; i++)
		{
			if (tokens[i].alive)
			{
				tokenSelected = i;
				calculateDirections();
				
				for (int j = 0; j < 4; j++)
				{
					if (directionsPossible[j])
					{
						possibleMovements[counter] = counter;
					}
					else
					{
						possibleMovements[counter] = -1;
					}
					
					counter++;
				}
			}
			else
			{
				for (int j = 0; j < 4; j++)
				{
					possibleMovements[counter] = -1;
					counter++;
				}
			}
		}
		
		return possibleMovements;
	}
	
	 // Copia el tablero en el que estamos y devuelve el tablero resultante de realizar el movimiento que nos entregan por parámetro.
	 public Actor_Board NewBoard (int movement) 
	 {
		 Actor_Board ret = new Actor_Board(board, blocks, tokens, false, movement, rnd);
		 return ret;
	 }

	// Función de evaluación estática. Devuelve el mejor score evaluando el tablero desde el punto de vista de jugador.
	public int evaluate()
	{
		// int ret = rnd.nextInt(10);
		
		System.out.println("Movement: " + CurrentMovement);
		int ret = 0;
		
		int posX_RedTriangle = 0;
		int posY_RedTriangle = 0;
		int posX_RedRectangle = 0;
		int posY_RedRectangle = 0;
		int posX_RedCircle = 0;
		int posY_RedCircle = 0;
		
		int posX_BlueTriangle = 0;
		int posY_BlueTriangle = 0;
		int posX_BlueRectangle = 0;
		int posY_BlueRectangle = 0;
		int posX_BlueCircle = 0;
		int posY_BlueCircle = 0;
		
		for(int i = 0; i < 5; i++){
			for(int j = 0; j < 5; j++){
				
				if(board[i][j] == Util_Const.TYPE_SQUARE.A_TRIANGLE)
				{
					posX_RedTriangle = i;
					posY_RedTriangle = j;
					
				}
				
				if(board[i][j] == Util_Const.TYPE_SQUARE.A_CIRCLE)
				{
					posX_RedCircle = i;
					posY_RedCircle = j;
				}
				
				if(board[i][j] == Util_Const.TYPE_SQUARE.A_RECTANGLE)
				{
					posX_RedRectangle = i;
					posY_RedRectangle = j;
				}
				
				if(board[i][j] == Util_Const.TYPE_SQUARE.B_TRIANGLE)
				{
					posX_BlueTriangle = i;
					posY_BlueTriangle = j;
					//System.out.println("posX_BlueTriangle: " + posX_BlueTriangle);
					//System.out.println("posY_BlueTriangle: " + posY_BlueTriangle);
				}
				
				if(board[i][j] == Util_Const.TYPE_SQUARE.B_CIRCLE)
				{
					posX_BlueCircle = i;
					posY_BlueCircle = j;
				}
				
				if(board[i][j] == Util_Const.TYPE_SQUARE.B_RECTANGLE)
				{
					posX_BlueRectangle = i;
					posY_BlueRectangle = j;
				}
			}
		}
		
		
		//int Distance = 10;
		
		int [] distances;
		distances = new int[9];
		
		for(int i = 0; i < distances.length; i++){
			
			/*distances[0] = Distance - ((posX_BlueTriangle-posY_BlueTriangle)^2 + (posX_RedTriangle-posY_RedTriangle)^2); // AT_BT
			distances[1] = Distance - ((posX_BlueTriangle-posY_BlueTriangle)^2 + (posX_RedCircle-posY_RedCircle)^2) - 1; // AT_BC
			distances[2] = Distance - ((posX_BlueTriangle-posY_BlueTriangle)^2 + (posX_RedRectangle-posY_RedRectangle)^2) + 1; // AT_BR
			
			distances[3] = Distance - ((posX_BlueCircle-posY_BlueCircle)^2 + (posX_RedTriangle-posY_RedTriangle)^2) + 1; // AC_BT
			distances[4] = Distance - ((posX_BlueCircle-posY_BlueCircle)^2 + (posX_RedCircle-posY_RedCircle)^2); // AC_BC
			distances[5] = Distance - ((posX_BlueCircle-posY_BlueCircle)^2 + (posX_RedRectangle-posY_RedRectangle)^2) - 1; // AC_BR
			
			distances[6] = Distance - ((posX_BlueRectangle-posY_BlueRectangle)^2 + (posX_RedCircle-posY_RedCircle)^2) + 1; // AR_BC
			distances[7] = Distance - ((posX_BlueRectangle-posY_BlueRectangle)^2 + (posX_RedTriangle-posY_RedTriangle)^2) - 1; // AR_BT
			distances[8] = Distance - ((posX_BlueRectangle-posY_BlueRectangle)^2 + (posX_RedRectangle-posY_RedRectangle)^2); // AR_BR*/
			
			distances[0] = ((posX_BlueTriangle-posY_BlueTriangle)^2 + (posX_RedTriangle-posY_RedTriangle)^2) + 5; // AT_BT
			distances[1] = ((posX_BlueTriangle-posY_BlueTriangle)^2 + (posX_RedCircle-posY_RedCircle)^2) + 10; // AT_BC
			distances[2] = ((posX_BlueTriangle-posY_BlueTriangle)^2 + (posX_RedRectangle-posY_RedRectangle)^2) - 10; // AT_BR
			
			distances[3] = ((posX_BlueCircle-posY_BlueCircle)^2 + (posX_RedTriangle-posY_RedTriangle)^2) - 10; // AC_BT
			distances[4] = ((posX_BlueCircle-posY_BlueCircle)^2 + (posX_RedCircle-posY_RedCircle)^2) + 5; // AC_BC
			distances[5] = ((posX_BlueCircle-posY_BlueCircle)^2 + (posX_RedRectangle-posY_RedRectangle)^2) + 10; // AC_BR
			
			distances[6] = ((posX_BlueRectangle-posY_BlueRectangle)^2 + (posX_RedCircle-posY_RedCircle)^2) - 10; // AR_BC
			distances[7] = ((posX_BlueRectangle-posY_BlueRectangle)^2 + (posX_RedTriangle-posY_RedTriangle)^2) + 10; // AR_BT
			distances[8] = ((posX_BlueRectangle-posY_BlueRectangle)^2 + (posX_RedRectangle-posY_RedRectangle)^2) + 5; // AR_BR
			
			//System.out.println(distances[0]);
			//System.out.println((posX_BlueTriangle-posY_BlueTriangle)^2 + (posX_RedTriangle-posY_RedTriangle)^2);
		}
		
		for (int i = 0 ; i < distances.length - 1 ; i++) {
            int max = i;
 
            //buscamos el mayor número
            for (int j = i + 1 ; j < distances.length ; j++) {
                if (distances[j] > distances[max]) {
                    max = j;    //encontramos el mayor número
                }
            }
 
            if (i != max) {
                //permutamos los valores
                int aux = distances[i];
                distances[i] = distances[max];
                distances[max] = aux;
                ret = distances[0];
            }
        }
		
		//int ret = boardScore;
		System.out.println(ret);
		
		return ret;
	}

	//Método empleado para renderizar los elementos por pantalla.
	public void paint (Graphics2D g2d, ImageObserver observer)
	{
		
		//Pintamos los controles
		g2d.drawImage(controls, 36, 30, observer);
		
		if (turn)
			g2d.drawImage(turn1, 780, 30, observer);
		else
			g2d.drawImage(turn2, 780, 30, observer);
		
		//Pintado de las fichas que siguen vivas.
		for(int i = 0; i < tokens.length; i++){
			if(tokens[i].alive)
				g2d.drawImage(tokens[i].m_sprite, Util_Const.boardOffsetX+tokens[i].row * Util_Const.tokenSize, Util_Const.boardOffsetY+tokens[i].column * Util_Const.tokenSize, observer);
		}
		//Pintado de bloques
		for(int i = 0; i < blocks.length; i++){
			g2d.drawImage(blocks[i].m_sprite, Util_Const.boardOffsetX+blocks[i].row * Util_Const.tokenSize, Util_Const.boardOffsetY+blocks[i].column * Util_Const.tokenSize, observer);
		}
        
		if(tokenSelected != -1)
		{
			if(directionsPossible[0])
			{
				g2d.drawImage(possible, Util_Const.boardOffsetX+tokens[tokenSelected].row * Util_Const.tokenSize, Util_Const.boardOffsetY+tokens[tokenSelected].column * Util_Const.tokenSize - Util_Const.tokenSize, observer);
				g2d.drawImage(upArrow, Util_Const.boardOffsetX+tokens[tokenSelected].row * Util_Const.tokenSize, Util_Const.boardOffsetY+tokens[tokenSelected].column * Util_Const.tokenSize - Util_Const.tokenSize, observer);
			}
			if(directionsPossible[1]){
				g2d.drawImage(possible, Util_Const.boardOffsetX+tokens[tokenSelected].row * Util_Const.tokenSize, Util_Const.boardOffsetY+tokens[tokenSelected].column * Util_Const.tokenSize + Util_Const.tokenSize, observer);
				g2d.drawImage(downArrow, Util_Const.boardOffsetX+tokens[tokenSelected].row * Util_Const.tokenSize, Util_Const.boardOffsetY+tokens[tokenSelected].column * Util_Const.tokenSize + Util_Const.tokenSize, observer);
			}
			if(directionsPossible[2]){
				g2d.drawImage(possible, Util_Const.boardOffsetX+tokens[tokenSelected].row * Util_Const.tokenSize - Util_Const.tokenSize, Util_Const.boardOffsetY+tokens[tokenSelected].column * Util_Const.tokenSize, observer);
				g2d.drawImage(leftArrow, Util_Const.boardOffsetX+tokens[tokenSelected].row * Util_Const.tokenSize - Util_Const.tokenSize, Util_Const.boardOffsetY+tokens[tokenSelected].column * Util_Const.tokenSize, observer);
			}
			if(directionsPossible[3]){
				g2d.drawImage(possible, Util_Const.boardOffsetX+tokens[tokenSelected].row * Util_Const.tokenSize + Util_Const.tokenSize, Util_Const.boardOffsetY+tokens[tokenSelected].column * Util_Const.tokenSize, observer);
				g2d.drawImage(rightArrow, Util_Const.boardOffsetX+tokens[tokenSelected].row * Util_Const.tokenSize + Util_Const.tokenSize, Util_Const.boardOffsetY+tokens[tokenSelected].column * Util_Const.tokenSize, observer);
			}
		}
	}
	
	//Recogemos de quien es el turno
	public int getTurnsPlayer()
	{
		int ret = 0;	
		if (turn) 		// Jugador 1 
		{
			ret = 0;
		}
		else 			// IA en caso de que se juegue contra la IA
		{
			ret = 1;
		}
		
		return ret;
	}
	
	public boolean IAActivated()
	{
		return playAgainstIA;
	}
	
	public void IAInput()
	{
		tokenSelected=-1;
		// Va de 0 a 8: 0,1,2,3 para la primera ficha, 4,5,6,7 para la segunda, 8,9,10,11 para la tercera
		int newMovement = MiniAlgoritm.Instance().negamaxAB(this, 0, Util_Const.MENOS_INFINITO, Util_Const.MAS_INFINITO).move();
		
		System.out.println("The new move is: "+newMovement);
		
		switch(newMovement)
		{
			case 0:
				if(tokens[3].alive)
				{
					tokenSelected = 3;
					calculateDirections();
					if(directionsPossible[0])
					{
						directionSelected = Direction.UP;
						moveDirection();
					}
					else
					{
						IAInput();
					}
				}
				else
				{
					IAInput();
				}
				break;
			case 1:
				if(tokens[3].alive)
				{
					tokenSelected = 3;
					calculateDirections();
					if(directionsPossible[1])
					{
						directionSelected = Direction.DOWN;
						moveDirection();
					}
					else
					{
						IAInput();
					}
				}
				else
				{
					IAInput();
				}
				break;
			case 2:
				if(tokens[3].alive)
				{
					tokenSelected = 3;
					calculateDirections();
					if(directionsPossible[2])
					{
						directionSelected = Direction.LEFT;
						moveDirection();
					}
					else
					{
						IAInput();
					}
				}
				else
				{
					IAInput();
				}
				break;
			case 3:
				if(tokens[3].alive)
				{
					tokenSelected = 3;
					calculateDirections();
					if(directionsPossible[3])
					{
						directionSelected = Direction.RIGHT;
						moveDirection();
					}
					else
					{
						IAInput();
					}
				}
				else
				{
					IAInput();
				}
				break;
			case 4:
				if(tokens[4].alive)
				{
					tokenSelected = 4;
					calculateDirections();
					if(directionsPossible[0])
					{
						directionSelected = Direction.UP;
						moveDirection();
					}
					else
					{
						IAInput();
					}
				}
				else
				{
					IAInput();
				}
				break;
			case 5:
				if(tokens[4].alive)
				{
					tokenSelected =4;
					calculateDirections();
					if(directionsPossible[1])
					{
						directionSelected = Direction.DOWN;
						moveDirection();
					}
					else
					{
						IAInput();
					}
				}
				else
				{
					IAInput();
				}
				break;
			case 6:
				if(tokens[4].alive)
				{
					tokenSelected = 4;
					calculateDirections();
					if(directionsPossible[2])
					{
						directionSelected = Direction.LEFT;
						moveDirection();
					}
					else
					{
						IAInput();
					}
				}
				else
				{
					IAInput();
				}
				break;
			case 7:
				if(tokens[4].alive)
				{
					tokenSelected = 4;
					calculateDirections();
					if(directionsPossible[3])
					{
						directionSelected = Direction.RIGHT;
						moveDirection();
					}
					else
					{
						IAInput();
					}
				}
				else
				{
					IAInput();
				}
				break;
			case 8:
				if(tokens[5].alive)
				{
					tokenSelected = 5;
					calculateDirections();
					if(directionsPossible[0])
					{
						directionSelected = Direction.UP;
						moveDirection();
					}
					else
					{
						IAInput();
					}
				}
				else
				{
					IAInput();
				}
				break;
			case 9:
				if(tokens[5].alive)
				{
					tokenSelected = 5;
					calculateDirections();
					if(directionsPossible[1])
					{
						directionSelected = Direction.DOWN;
						moveDirection();
					}
					else
					{
						IAInput();
					}
				}
				else
				{
					IAInput();
				}
				break;
			case 10:
				if(tokens[5].alive)
				{
					tokenSelected = 5;
					calculateDirections();
					if(directionsPossible[2])
					{
						directionSelected = Direction.LEFT;
						moveDirection();
					}
					else
					{
						IAInput();
					}
				}
				else
				{
					IAInput();
				}
				break;
			case 11:
				if(tokens[5].alive)
				{
					tokenSelected =5;
					calculateDirections();
					if(directionsPossible[3])
					{
						directionSelected = Direction.RIGHT;
						moveDirection();
					}
					else
					{
						IAInput();
					}
				}
				else
				{
					IAInput();
				}
				break;
			default:
				
				tokenSelected = -1;
				directionSelected = Direction.NONE;
				System.out.println("NOOONEEE");
				break;
		}
	}
	
	//Procesado de input de teclas.
	public void input(int keyCode){
		//En caso de que se esté jugando, procesar.
		if(playing)
		{
			switch(keyCode){
			//Si es el turno adecuado y la ficha sigue viva, seleccionar la ficha y calcular sus direcciones posibles.
			case KeyEvent.VK_1:
				if(tokens[0].alive && turn){
					tokenSelected = 0;
					calculateDirections();
				}
				break;
			case KeyEvent.VK_2:
				if(tokens[1].alive && turn){
					tokenSelected = 1;
					calculateDirections();
				}
				break;
			case KeyEvent.VK_3:
				if(tokens[2].alive && turn){
					tokenSelected = 2;
					calculateDirections();
				}
				break;
				//
			case KeyEvent.VK_4:
				if(tokens[3].alive && !turn && !playAgainstIA){
					tokenSelected = 3;
					calculateDirections();
				}
				break;
			case KeyEvent.VK_5:
				if(tokens[4].alive && !turn && !playAgainstIA){
					tokenSelected = 4;
					calculateDirections();
				}
				break;
			case KeyEvent.VK_6:
				if(tokens[5].alive && !turn && !playAgainstIA){
					tokenSelected = 5;
					calculateDirections();
				}
				break;
				
				//Si se ha seleccionado una ficha correctamente y la dirección es posible, se ejecuta el movimiento.
			case KeyEvent.VK_UP:
				if(tokenSelected != -1 && directionsPossible[0]){
					directionSelected = Direction.UP;
					moveDirection();
				}
				break;
			case KeyEvent.VK_DOWN:
				if(tokenSelected != -1 && directionsPossible[1]){
					directionSelected = Direction.DOWN;
					moveDirection();
				}
				break;
			case KeyEvent.VK_LEFT:
				if(tokenSelected != -1 && directionsPossible[2]){
					directionSelected = Direction.LEFT;
					moveDirection();
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(tokenSelected != -1 && directionsPossible[3]){
					directionSelected = Direction.RIGHT;
					moveDirection();
				}
				break;
			default:
				break;
			}
		}
	}
	
	//Método empleado para comprobar las direcciones posibles a una ficha elegida existente. Se tiene en cuenta que la dirección no de a un bloqueo o una ficha del mismo color.
	void calculateDirections()
	{
		if (tokenSelected == -1)
		{
			return;
		}
		
		directionsPossible[2] = (tokens[tokenSelected].row -1 >= 0) && 
								(board[tokens[tokenSelected].row-1][tokens[tokenSelected].column] != Util_Const.TYPE_SQUARE.BLOCKED) &&
								(((turn) &&
								(board[tokens[tokenSelected].row-1][tokens[tokenSelected].column] != Util_Const.TYPE_SQUARE.A_TRIANGLE) &&
								(board[tokens[tokenSelected].row-1][tokens[tokenSelected].column] != Util_Const.TYPE_SQUARE.A_RECTANGLE) &&
								(board[tokens[tokenSelected].row-1][tokens[tokenSelected].column] != Util_Const.TYPE_SQUARE.A_CIRCLE)) ||
								((!turn) &&
								(board[tokens[tokenSelected].row-1][tokens[tokenSelected].column] != Util_Const.TYPE_SQUARE.B_TRIANGLE) &&
								(board[tokens[tokenSelected].row-1][tokens[tokenSelected].column] != Util_Const.TYPE_SQUARE.B_RECTANGLE) &&
								(board[tokens[tokenSelected].row-1][tokens[tokenSelected].column] != Util_Const.TYPE_SQUARE.B_CIRCLE)));
		directionsPossible[3] = (tokens[tokenSelected].row +1 < 5) &&
								(board[tokens[tokenSelected].row+1][tokens[tokenSelected].column] != Util_Const.TYPE_SQUARE.BLOCKED) &&
								(((turn) &&
								(board[tokens[tokenSelected].row+1][tokens[tokenSelected].column] != Util_Const.TYPE_SQUARE.A_TRIANGLE) &&
								(board[tokens[tokenSelected].row+1][tokens[tokenSelected].column] != Util_Const.TYPE_SQUARE.A_RECTANGLE) &&
								(board[tokens[tokenSelected].row+1][tokens[tokenSelected].column] != Util_Const.TYPE_SQUARE.A_CIRCLE)) ||
								((!turn) &&
								(board[tokens[tokenSelected].row+1][tokens[tokenSelected].column] != Util_Const.TYPE_SQUARE.B_TRIANGLE) &&
								(board[tokens[tokenSelected].row+1][tokens[tokenSelected].column] != Util_Const.TYPE_SQUARE.B_RECTANGLE) &&
								(board[tokens[tokenSelected].row+1][tokens[tokenSelected].column] != Util_Const.TYPE_SQUARE.B_CIRCLE)));
		directionsPossible[0] = (tokens[tokenSelected].column -1 >= 0) && 
								(board[tokens[tokenSelected].row][tokens[tokenSelected].column-1] != Util_Const.TYPE_SQUARE.BLOCKED) &&
								(((turn) &&
								(board[tokens[tokenSelected].row][tokens[tokenSelected].column-1] != Util_Const.TYPE_SQUARE.A_TRIANGLE) &&
								(board[tokens[tokenSelected].row][tokens[tokenSelected].column-1] != Util_Const.TYPE_SQUARE.A_RECTANGLE) &&
								(board[tokens[tokenSelected].row][tokens[tokenSelected].column-1] != Util_Const.TYPE_SQUARE.A_CIRCLE)) ||
								((!turn) && 
								(board[tokens[tokenSelected].row][tokens[tokenSelected].column-1] != Util_Const.TYPE_SQUARE.B_TRIANGLE) &&
								(board[tokens[tokenSelected].row][tokens[tokenSelected].column-1] != Util_Const.TYPE_SQUARE.B_RECTANGLE) &&
								(board[tokens[tokenSelected].row][tokens[tokenSelected].column-1] != Util_Const.TYPE_SQUARE.B_CIRCLE)));
		directionsPossible[1] = (tokens[tokenSelected].column +1 < 5) && 
								(board[tokens[tokenSelected].row][tokens[tokenSelected].column+1] != Util_Const.TYPE_SQUARE.BLOCKED) &&
								(((turn) &&
								(board[tokens[tokenSelected].row][tokens[tokenSelected].column+1] != Util_Const.TYPE_SQUARE.A_TRIANGLE) &&
								(board[tokens[tokenSelected].row][tokens[tokenSelected].column+1] != Util_Const.TYPE_SQUARE.A_RECTANGLE) &&
								(board[tokens[tokenSelected].row][tokens[tokenSelected].column+1] != Util_Const.TYPE_SQUARE.A_CIRCLE)) ||
								((!turn) && 
								(board[tokens[tokenSelected].row][tokens[tokenSelected].column+1] != Util_Const.TYPE_SQUARE.B_TRIANGLE) &&
								(board[tokens[tokenSelected].row][tokens[tokenSelected].column+1] != Util_Const.TYPE_SQUARE.B_RECTANGLE) &&
								(board[tokens[tokenSelected].row][tokens[tokenSelected].column+1] != Util_Const.TYPE_SQUARE.B_CIRCLE)));
	}
	
	//Método empleado para ejecutar el movimiento.
	void moveDirection()
	{
		//Se establece qué fichas del array hay que comprobar como enemigas (rojas, azules) según el turno.
		int leftMargin, rightMargin;
		if(turn){
			leftMargin = 3;
			rightMargin = 6;
		} else {
			leftMargin = 0;
			rightMargin = 3;
		}
		switch(directionSelected){
		case LEFT:
			board[tokens[tokenSelected].row][tokens[tokenSelected].column] = Util_Const.TYPE_SQUARE.EMPTY;					//Se limpia la casilla anterior
			tokens[tokenSelected].row--;																					//Se desplaza la ficha
			for(int i = leftMargin; i < rightMargin; i++){
				if(tokens[tokenSelected].row == tokens[i].row && tokens[tokenSelected].column == tokens[i].column){			//En caso de colisionar con una ficha contraria se analiza el tipo.
					if(resolveFight(tokens[i])){																			//Si es true se elimina la ficha del movimiento
						board[tokens[tokenSelected].row][tokens[tokenSelected].column] = tokens[i].actualType;
						tokens[tokenSelected].alive = false;
						tokens[tokenSelected].row = tokens[tokenSelected].column = -1;
					} else {
						board[tokens[tokenSelected].row][tokens[tokenSelected].column] = tokens[tokenSelected].actualType;	//Si es false se elimina la ficha que había.
						tokens[i].alive = false;
						tokens[i].row = tokens[i].column = -1;
					}
				}
				else
				{
					if (!turn)
						boardScore = Util_Const.NEUTRALSCORE;
				}
			}
			break;
		case RIGHT:
			board[tokens[tokenSelected].row][tokens[tokenSelected].column] = Util_Const.TYPE_SQUARE.EMPTY;
			tokens[tokenSelected].row++;
			board[tokens[tokenSelected].row][tokens[tokenSelected].column] = tokens[tokenSelected].actualType;
			for(int i = leftMargin; i < rightMargin; i++){
				if(tokens[tokenSelected].row == tokens[i].row && tokens[tokenSelected].column == tokens[i].column){
					if(resolveFight(tokens[i])){
						board[tokens[tokenSelected].row][tokens[tokenSelected].column] = tokens[i].actualType;
						tokens[tokenSelected].alive = false;
						tokens[tokenSelected].row = tokens[tokenSelected].column = -1;
					} else {
						board[tokens[tokenSelected].row][tokens[tokenSelected].column] = tokens[tokenSelected].actualType;
						tokens[i].alive = false;
						tokens[i].row = tokens[i].column = -1;
					}
				}
				else
				{
					if (!turn)
						boardScore = Util_Const.NEUTRALSCORE;
				}
			}
			break;
		case UP:
			board[tokens[tokenSelected].row][tokens[tokenSelected].column] = Util_Const.TYPE_SQUARE.EMPTY;
			tokens[tokenSelected].column--;
			board[tokens[tokenSelected].row][tokens[tokenSelected].column] = tokens[tokenSelected].actualType;
			for(int i = leftMargin; i < rightMargin; i++){
				if(tokens[tokenSelected].row == tokens[i].row && tokens[tokenSelected].column == tokens[i].column){
					if(resolveFight(tokens[i])){
						board[tokens[tokenSelected].row][tokens[tokenSelected].column] = tokens[i].actualType;
						tokens[tokenSelected].alive = false;
						tokens[tokenSelected].row = tokens[tokenSelected].column = -1;
					} else {
						board[tokens[tokenSelected].row][tokens[tokenSelected].column] = tokens[tokenSelected].actualType;
						tokens[i].alive = false;
						tokens[i].row = tokens[i].column = -1;
					}
				}
				else
				{
					if (!turn)
						boardScore = Util_Const.NEUTRALSCORE;
				}
			}
			break;
		case DOWN:
			board[tokens[tokenSelected].row][tokens[tokenSelected].column] = Util_Const.TYPE_SQUARE.EMPTY;
			tokens[tokenSelected].column++;
			board[tokens[tokenSelected].row][tokens[tokenSelected].column] = tokens[tokenSelected].actualType;
			for(int i = leftMargin; i < rightMargin; i++){
				if(tokens[tokenSelected].row == tokens[i].row && tokens[tokenSelected].column == tokens[i].column){
					if(resolveFight(tokens[i])){
						board[tokens[tokenSelected].row][tokens[tokenSelected].column] = tokens[i].actualType;
						tokens[tokenSelected].alive = false;
						tokens[tokenSelected].row = tokens[tokenSelected].column = -1;
					} else {
						board[tokens[tokenSelected].row][tokens[tokenSelected].column] = tokens[tokenSelected].actualType;
						tokens[i].alive = false;
						tokens[i].row = tokens[i].column = -1;
					}
				}
				else
				{
					if (!turn)
						boardScore = Util_Const.NEUTRALSCORE;
				}
			}
			break;
		default:
			break;
		}
		//Se resetean los valores necesarios para el turno siguiente.
		tokenSelected = -1;
		directionSelected = Direction.NONE;
		checkEndGame();
		
		if (!waitingForIA)
			waitingForIA = true;
		
		turn = !turn;
	}
	
	//Se comparan los tipos de ambas fichas y se decide quién vence.
	//	- Triángulo gana Cuadrado
	//	- Cuadrado gana Círculo
	//	- Círculo gana Triángulo
	//En caso de ser del mismo tipo se lanza un random.
	boolean resolveFight(Actor_Token enemy){
		if(turn){
			if((tokens[tokenSelected].actualType == Util_Const.TYPE_SQUARE.A_TRIANGLE && enemy.actualType == Util_Const.TYPE_SQUARE.B_TRIANGLE)||
			  (tokens[tokenSelected].actualType == Util_Const.TYPE_SQUARE.A_CIRCLE && enemy.actualType == Util_Const.TYPE_SQUARE.B_CIRCLE)||
			  (tokens[tokenSelected].actualType == Util_Const.TYPE_SQUARE.A_RECTANGLE && enemy.actualType == Util_Const.TYPE_SQUARE.B_RECTANGLE))
			{
				return rnd.nextBoolean();
			}
			
			if((tokens[tokenSelected].actualType == Util_Const.TYPE_SQUARE.A_TRIANGLE && enemy.actualType == Util_Const.TYPE_SQUARE.B_CIRCLE) ||
	          (tokens[tokenSelected].actualType == Util_Const.TYPE_SQUARE.A_CIRCLE && enemy.actualType == Util_Const.TYPE_SQUARE.B_RECTANGLE) ||
			  (tokens[tokenSelected].actualType == Util_Const.TYPE_SQUARE.A_RECTANGLE && enemy.actualType == Util_Const.TYPE_SQUARE.B_TRIANGLE))
			{
				return true;
			}
			
			return false;
		} 
		else {
			if((tokens[tokenSelected].actualType == Util_Const.TYPE_SQUARE.B_TRIANGLE && enemy.actualType == Util_Const.TYPE_SQUARE.A_TRIANGLE)||
			  (tokens[tokenSelected].actualType == Util_Const.TYPE_SQUARE.B_CIRCLE && enemy.actualType == Util_Const.TYPE_SQUARE.A_CIRCLE)||
			  (tokens[tokenSelected].actualType == Util_Const.TYPE_SQUARE.B_RECTANGLE && enemy.actualType == Util_Const.TYPE_SQUARE.A_RECTANGLE))
			{
				
				boardScore = Util_Const.EATANDEATENSCORE;
				return rnd.nextBoolean();
			}
			
			if((tokens[tokenSelected].actualType == Util_Const.TYPE_SQUARE.B_TRIANGLE && enemy.actualType == Util_Const.TYPE_SQUARE.A_CIRCLE) ||
	          (tokens[tokenSelected].actualType == Util_Const.TYPE_SQUARE.B_CIRCLE && enemy.actualType == Util_Const.TYPE_SQUARE.A_RECTANGLE) ||
			  (tokens[tokenSelected].actualType == Util_Const.TYPE_SQUARE.B_RECTANGLE && enemy.actualType == Util_Const.TYPE_SQUARE.A_TRIANGLE))
			{
				boardScore = Util_Const.EATSCORE;
				return true;
			}
			
			boardScore = Util_Const.EATENSCORE;
			return false;
		}
	}
	
	//Método auxiliar para saber si todavía se está jugando.
	public boolean isPlaying(){
		return playing;
	}
	
	//Se comprueba si se ha acabado con todas las fichas de un equipo.
	void checkEndGame()
	{
		if(!tokens[0].alive && !tokens[1].alive && !tokens[2].alive)
		{
			winner = 1;
			playing = false;
		}
		if (!tokens[3].alive && !tokens[4].alive && !tokens[5].alive)
		{
			winner = 0;
			playing = false;
		}
	}
}
