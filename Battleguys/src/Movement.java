
public class Movement 
{
	int score = 0, move = -1;
	
	public Movement(int bestScore, int bestMove)
	{
		score = bestScore;
		move = bestMove;
	}
	
	public int score()
	{
		return score;
	}
	
	public int move()
	{
		return move;
	}
}
