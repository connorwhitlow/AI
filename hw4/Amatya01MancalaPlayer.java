/**
 * SimpleMancalaPlayer - A simple example implementation of a Mancala
 * player with simple, poor time management.
 *
 * @author Todd W. Neller
 */

public class Amatya01MancalaPlayer implements MancalaPlayer {
	int totalMoves = 0;
	Whitco03MancalaNode searchNode;
	ScoreDiffMancalaNode searchNode1;


	public int chooseMove(MancalaNode node, long timeRemaining) {

		int depthLimit = 0;

		final double EARLY_GAME_DEPTH_FACTOR = 1.3;
		final double MID_GAME_DEPTH_FACTOR = 1.7;
		final double LATE_GAME_DEPTH_FACTOR = 2;

		final int MID_GAME_THRESHOLD = 10;
		final int LATE_GAME_THRESHOLD = 30;

		// Calculate the total number of moves made

		++totalMoves;
		// Determine the game phase based on the total number of moves
		if (totalMoves < MID_GAME_THRESHOLD) {
			// Early game phase
			depthLimit = (int) (EARLY_GAME_DEPTH_FACTOR * Math.log((double) timeRemaining / piecesRemaining(node)));
		} else if (totalMoves < LATE_GAME_THRESHOLD) {
			// Mid game phase
			depthLimit =  (int) (MID_GAME_DEPTH_FACTOR * Math.log((double) timeRemaining / piecesRemaining(node)));
		} else {
			// Late game phase
			depthLimit = (int) (LATE_GAME_DEPTH_FACTOR * Math.log((double) timeRemaining / piecesRemaining(node)));
		}
	
		if (depthLimit < 1) depthLimit = 1;

		AlphaBetaSearch searcher = new AlphaBetaSearch(depthLimit);


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

	public double gaussian(double x) {
		// Calculate the exponent
		int a = 22;
		int b = 12;
		int c = 4;

		double exponent = -((x - b) * (x - b)) / (2 * c * c);

		// Calculate the value of the Gaussian function
		double result = a * Math.exp(exponent);

		return result;
	}

}
