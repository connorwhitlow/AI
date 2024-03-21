
public class PigSolitaireSolver {

	int goal;
	int turns;
	boolean[][][] computed;
	double[][][] p;
	boolean[][][] roll;

	public PigSolitaireSolver(int goal, int turns) {
		this.goal = goal;
		this.turns = turns;
		computed = new boolean[goal][turns][goal];
		p = new double[goal][turns][goal];
		roll = new boolean[goal][turns][goal];
		for (int i = 0; i < goal; i++) // for all i
			for (int j = 0; j < turns; j++) // for all j
				for (int k = 0; i + k < goal; k++) // for all k
					pWin(i, j, k);

	}

	public double pWin(int i,int j, int k) {

		if(j >= turns) {
			return 0;
		}
		if(i + k >=goal) {
			return 1;
		}
		if (computed[i][j][k]) return p[i][j][k];


		// Compute the probability of winning with a roll

		double pRoll = pWin(i, j + 1, 0);
		for (int roll = 2; roll <= 6; roll++) {
			pRoll += pWin(i, j, k + roll);
		}

		pRoll /= 6.0;


		double pHold = pWin(i+k, j+1, 0);

		roll[i][j][k] = pRoll > pHold;

		if(roll[i][j][k]) {
			p[i][j][k] = pRoll;
		}
		else {
			p[i][j][k] = pHold;
		}
		computed[i][j][k] = true;
		return  p[i][j][k];
	}

	public boolean shouldRoll(int i, int j, int k) {
		return roll[i][j][k];
	}

    
//	public static void main(String[] args) {
//		
//		PigSolitaireSolver solver = new PigSolitaireSolver(100,10);
//		boolean previousAction = false; // Assume holding initially
//        for (int i = 0; i < goal; i++) {
//            for (int j = 0; j < turns; j++) {
//                for (int k = 0; i + k < goal; k++) {
//                    boolean currentAction = solver.shouldRoll(i, j, k);
//                    if (currentAction != previousAction) {
//                        if (currentAction) {
//                            System.out.println("Roll when at " + i + " with " + (turns - j) + " turns left and cumulative score " + k);
//                        } else {
//                            System.out.println("Hold when at " + i + " with " + (turns - j) + " turns left and cumulative score " + k);
//                        }
//                        previousAction = currentAction;
//                    }
//                }
//            }
//        }
//	}

}
