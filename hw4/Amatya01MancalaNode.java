
public class Amatya01MancalaNode extends MancalaNode{
	
	/**
	 * See corresponding <code>MancalaNode</code> standard constructor documentation.
	 */
	public Amatya01MancalaNode() {
		super();
	}

	/**
	 * See corresponding <code>MancalaNode</code> copy constructor documentation.
	 * @param node node to be copied
	 */
	public Amatya01MancalaNode(MancalaNode node) {
		super(node);
	}

	/**
	 * See corresponding <code>MancalaNode</code> FairKalah constructor documentation.
	 * @param stateIndex FairKalah initial state index 
	 */
	public Amatya01MancalaNode(int stateIndex) {
		super(stateIndex);
	}


	@Override
	public double utility() {	
	    
	    if (gameOver()) {
	        return state[MAX_SCORE_PIT] - state[MIN_SCORE_PIT];
	    }
	    double utility = 0;
	    
	    // Calculate the progress towards completion based on remaining stones
	    double progress = (state[MAX_SCORE_PIT] + state[MIN_SCORE_PIT]) / (double) NUM_PIECES;
	    
	    // Difference in scores
	    utility += state[MAX_SCORE_PIT] - state[MIN_SCORE_PIT];
	    
	    // Bonus for having more stones in player's pits
	    for (int i = 0; i < PLAY_PITS; ++i) {
	        utility += state[i];
	    }
	    
	    // Potential for free moves
	    for (int i = 0; i < PLAY_PITS; ++i) {
	        if (getPlayer() == MAX && MAX_SCORE_PIT - i == state[i]) {
	            utility += 0.5;
	        }
	        if (getPlayer() == MIN && MIN_SCORE_PIT - i == state[MIN_SCORE_PIT - i]) {
	            utility -= 0.5;
	        }
	    }
	    
	    // Check for captures
	    for (int i = 0; i < PLAY_PITS; ++i) {
	        int targetPitMax = MAX_SCORE_PIT - i;
	        int targetPitMin = MIN_SCORE_PIT - i;
	        
	        if (state[targetPitMax] == 1 && state[PLAY_PITS - i] > 0) {
	            utility += state[PLAY_PITS - i];
	        }
	        
	        if (state[targetPitMin] == 1 && state[MIN_SCORE_PIT - i] > 0) {
	            utility -= state[MIN_SCORE_PIT - i];
	        }
	    }
	    
	    // Adjust utility based on game progress
	    utility *= progress;
	    
	    return utility;
	}

}
