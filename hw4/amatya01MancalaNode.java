
public class amatya01MancalaNode extends MancalaNode{
	
	/**
	 * See corresponding <code>MancalaNode</code> standard constructor documentation.
	 */
	public amatya01MancalaNode() {
		super();
	}

	/**
	 * See corresponding <code>MancalaNode</code> copy constructor documentation.
	 * @param node node to be copied
	 */
	public amatya01MancalaNode(MancalaNode node) {
		super(node);
	}

	/**
	 * See corresponding <code>MancalaNode</code> FairKalah constructor documentation.
	 * @param stateIndex FairKalah initial state index 
	 */
	public amatya01MancalaNode(int stateIndex) {
		super(stateIndex);
	}


	@Override
	public double utility() {
		if(gameOver()) {
			return state[MAX_SCORE_PIT] - state[MIN_SCORE_PIT];
		}
		
		double utility = 0;
		if(getPlayer() == MAX) {
			utility += state[MAX_SCORE_PIT] - state[MIN_SCORE_PIT];
			for(int i = 0; i < PLAY_PITS;++i) {
				// If free move
				if(MAX_SCORE_PIT-i ==state[i]){
//					System.out.println("Free move");
					utility++;
				}
			}
		}
		else {
			utility += state[MAX_SCORE_PIT] - state[MIN_SCORE_PIT];
			for(int i = MAX_SCORE_PIT + 1; i < MIN_SCORE_PIT; ++i) {
				// If free move
				if(MIN_SCORE_PIT - i == state[i]) {
					utility++;
				}
					
			}
			
		}
	
		
		
		return utility;
	}

}
