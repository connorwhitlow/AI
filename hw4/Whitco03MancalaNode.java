
public class Whitco03MancalaNode extends MancalaNode{
	MancalaNode node;
	
	/**
	 * See corresponding <code>MancalaNode</code> copy constructor documentation.
	 * @param node node to be copied
	 */
	public Whitco03MancalaNode(MancalaNode node) {
		super(node);
		this.node = node;
	}
	
	@Override
	public double utility(){
		double utility = 0;
		
		if(state[MAX_SCORE_PIT] + state[MIN_SCORE_PIT] == NUM_PIECES) {
			return state[MAX_SCORE_PIT];
		}
		
		utility = state[MAX_SCORE_PIT] - state[MIN_SCORE_PIT];
		int possMoves = 0;
		int minPoss = 0;
		for(int i = 0; i < 6; ++i) {
			if (state[i] != 0) {
					++possMoves;
			}
			
			if(state[i + 7] != 0) {
				++minPoss;
			}
		}
		
		utility += (possMoves - minPoss)*2;
		
		return utility;
	}
}
