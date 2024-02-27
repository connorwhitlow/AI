import java.util.ArrayList;

public class AlphaBetaSearch implements GameTreeSearcher{
	int depthLimit;
	int bestMove;
	int nodeCount;
	
	public AlphaBetaSearch(int depthLimit) {
		this.depthLimit = depthLimit;
	}
	
	public double absearch(GameNode node, int depthLeft, double a, double b) {
		int localBestMove =  GameNode.UNDEFINED_MOVE;
		boolean maximizing = (node.getPlayer() == GameNode.MAX);
		double bestUtility = 
				maximizing ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
		nodeCount++;
		double alpha = a;
		double beta = b;
		
		ArrayList<GameNode> children = node.expand();
		
		// Return utility if game over or depth limit reached
		if (node.gameOver() || depthLeft == 0)
			return node.utility();
				
		for(GameNode child: children) {
			double childUtility = absearch(child, depthLeft - 1, alpha, beta);
			if (maximizing) {
				if(childUtility > bestUtility) {
				bestUtility = childUtility;
				localBestMove = child.prevMove;
				}
				alpha = Math.max(alpha, bestUtility);
			}
			else {
				if (childUtility < bestUtility) {
					bestUtility = childUtility;
					localBestMove = child.prevMove;
				}
				beta = Math.min(bestUtility, beta);
			}
		
		if(alpha >= beta) {
			break;
		}
		
		}
		
		bestMove = localBestMove;
		return bestUtility;
		
	}
	
	/**
	 * <code>getBestMove</code> - Return the best move for the
	 * node most recently evaluated.
	 *
	 * @return an <code>int</code> value encoding the move */
	public int getBestMove() 
	{
		return bestMove;
	}

	/**
	 * <code>getNodeCount</code> - returns the number of nodes
	 * searched for the previous node evaluation
	 *
	 * @return an <code>int</code> value - number of nodes
	 * searched */
	public int getNodeCount() 
	{
		return nodeCount;
	}

	@Override
	public double eval(GameNode node) {
		nodeCount = 0;
		return absearch(node, depthLimit, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
	}

}
