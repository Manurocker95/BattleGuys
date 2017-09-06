
public class MiniAlgoritm 
{
	// Singleton
	private static MiniAlgoritm instance;
	public static MiniAlgoritm Instance() 
	{
		return instance;
	}
	
	public MiniAlgoritm()
	{
		if (instance == null)
		{
			instance = this;
		}
		else
		{
			System.out.println("Ya hay una instancia del MiniAlgoritm");
			return;
		}
	}
	
	protected Movement negamaxAB (Actor_Board board, int depth, int alfa, int beta) 
	{		
		int bestMove = -1;
		int bestScore = 0;
		int scoreActual = 0;
		
		Movement mv; // score, movimiento
		Actor_Board newBoard;
		
		// Comprobar si hemos terminado de hacer recursión
		if (!board.isPlaying() || depth == Util_Const.MAX_PROFUNDIDAD)
		{
			mv = new Movement (board.evaluate(), -1); //Guardamos el último score y -1 en movimiento, es decir, no válido.
		}
		else
		{
			bestMove = -1;
			bestScore = Util_Const.MENOS_INFINITO;
			
			for (int move : board.PossibleMovements()) 
			{
				if (move > -1) // Es posible el movimiento
				{
					newBoard = board.NewBoard(move);
					
					// Recursividad. Como es negamax, en cada nivel, invertimos el signo. El nuevo alfa, es el inverso de beta, y el nuevo beta es acotado por el mejorScore encontrado hasta el momento en el nivel.
					mv = negamaxAB (newBoard, depth + 1, -beta, -Math.max(alfa, bestScore));
					scoreActual = -mv.score;
					
					// Actualizar mejor score.
					if (scoreActual > bestScore ) 
					{
						bestScore = scoreActual;
						bestMove = move;
					}
					
					// Si encontramos un mejor score que beta, dejamos de recorrer el resto de hermanos y devolvemos la mejor Jugada.
					if (bestScore >= beta) 
					{
						mv = new Movement (bestScore, bestMove);
						return mv;
					}	
				}
			}
			
			mv = new Movement (bestScore, bestMove);
		}
		
		return mv;
	}
		
}
