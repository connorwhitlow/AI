
public class PigSolver {
	int goal;
	double epsilon;
	double[][][] p;
	boolean[][][] roll;

	public PigSolver(int goal, double epsilon) {
		this.goal = goal;
		this.epsilon = epsilon;
		p = new double[goal][goal][goal];
		roll = new boolean[goal][goal][goal];

		valueIterate();
	}
	
	 void valueIterate() {
	      double maxChange;
	        do {
	            maxChange = 0.0;
	            for (int i = 0; i < goal; i++) // for all i
	                for (int j = 0; j < goal; j++) // for all j
	                    for (int k = 0; k < goal - i; k++) { // for all k
	                    	
	                        double oldProb = p[i][j][k];
	                        double pRoll = 1.0 - pWin(j, i, 0);
	                        for (int roll = 2; roll <= 6; roll++)
	                            pRoll += pWin(i, j, k + roll);
	                        pRoll /= 6.0;
//	                        double pRoll = (1.0 - pWin(j, i, 0) + pWin(i, j, k + 1)) / 2;
	                        double pHold = 1.0 - pWin(j, i + k, 0);
	                        p[i][j][k] = Math.max(pRoll, pHold);
	                        roll[i][j][k] = pRoll > pHold;
	                        double change = Math.abs(p[i][j][k] - oldProb);
	                        maxChange = Math.max(maxChange, change);
	                    }
	        } while (maxChange >= epsilon);
	    }


	public double pWin(int i, int j, int k) {
		if (i + k >= goal) return 1.0;
		if (j >= goal) return 0.0;
		return p[i][j][k];
	}
	
	public boolean shouldRoll(int i, int j, int k) {
		return roll[i][j][k];
	}
	
    public void outputHoldValues() {
        for (int i = 0; i < goal; i++) {
            for (int j = 0; j < goal; j++) {
                int k = 0;
                while (k < goal - i && roll[i][j][k])
                    k++;    
                System.out.print(k + " ");
            }
            System.out.println();
        }
    }
    
    public void outputRollValues() {
        for(int i = 0; i < goal; ++i) {
        	for(int j  = 0; j < goal; ++j) {
        		for(int k = 0; k <= goal; ++k) {
        			if(i + k == goal || !roll[i][j][k]) {
        				System.out.println(i + " " + j + " " + k + " " + p[i][j][k]);
        				break;
        			}
        		}
        	}
        }
    }
    

    public void summarize() {
        System.out.println("p[0][0][0] = " + p[0][0][0]);
        System.out.println();
        System.out.println("i\tj\tPolicy changes at k =");
        for (int i = 0; i < goal; i++) // for all i
            for (int j = 0; j < goal; j++) { // for all j
                int k = 0;
                System.out.print(i + "\t" + j + "\t" + (roll[i][j][k] ? "roll " : "hold "));
                for (k = 1; i + k < goal; k++) // for all valid k
                    if (roll[i][j][k] != roll[i][j][k-1])
                        System.out.print(k + " " + (roll[i][j][k] ? "roll " : "hold "));
                System.out.println();
            }
    }
	
//	public static void main(String[] args){
//		
//		new PigSolver(100, 1e-11).outputRollValues();
//
//	}

}
