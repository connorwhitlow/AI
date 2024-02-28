
public class Whitco03MancalaPlayer implements MancalaPlayer{
	int totalMoves = 0;
	Whitco03MancalaNode searchNode;
	ScoreDiffMancalaNode searchNode1;
	
	@Override
	public int chooseMove(MancalaNode node, long timeRemaining) {
		// TODO - WARNING: This is a simple time management effort 
				// to distribute search time over course of game.  
				// It under-utilizes time, so you should design better time management.
				final double DEPTH_FACTOR = 1.3;
				int depthLimit = 0;
				if(totalMoves < 9) {
				depthLimit = (int) (DEPTH_FACTOR * Math.log((double) timeRemaining 
									/ piecesRemaining(node)));
				++totalMoves;
				}
				else {
					depthLimit = 10;
				}
				if (depthLimit < 1) depthLimit = 1;

				// Create an ab searcher.
				AlphaBetaSearch searcher = new AlphaBetaSearch(depthLimit);

				// Create a new copy of the input node that uses the
				// score difference heuristic evaluation function. 
				if(totalMoves < 9) {
					searchNode = new Whitco03MancalaNode(node);
					searcher.eval(searchNode);
				}
				else {
					searchNode1 = new ScoreDiffMancalaNode(node);
					searcher.eval(searchNode1);
				}

				return searcher.getBestMove();
	}
	
	/**
	 * Returns the number of pieces not yet captured.
	 * @return int - uncaptured pieces
	 * @param node MancalaNode - node to check
	 */
	public int piecesRemaining(MancalaNode node) {
		int pieces = 0;
		for (int i = 0; i < 6; i++) pieces += node.state[i];
		for (int i = 7; i < 13; i++) pieces += node.state[i];
		return pieces;
	}

}
