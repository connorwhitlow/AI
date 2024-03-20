import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

//chatgpt: make a class skeleton according to this specification, add fields and method outlines
public class TwoDicePigSolver extends java.lang.Object {
	 int goal;
	 double theta;
	 double[][][] p;
	 boolean[][][] roll;

    // Constructor
    public TwoDicePigSolver(int goal, double theta) {
    	this.goal = goal;
        this.theta = theta;
        this.p = new double[goal][goal][goal];
        this.roll = new boolean[goal][goal][goal];
        valueIterate();
    }

    // Method to return the probability of winning given my score, the opponent's score, and my turn total.
    public double pWin(int myScore, int otherScore, int turnScore) {
    	if (myScore + turnScore >= goal) return 1.0;
    	if (otherScore >= goal) return 0.0;
    	
    	
        
        return p[myScore][otherScore][turnScore]; 
    }

    void valueIterate() {
        double maxChange;
          do {
              maxChange = 0.0;
              for (int i = 0; i < goal; i++) // for all i
                  for (int j = 0; j < goal; j++) // for all j
                      for (int k = 0; k < goal - i; k++) { // for all k
                          double oldProb = p[i][j][k];
                          double pRoll = 0;
                          double pHold = 1.0 - pWin(j, i + k, 0);
                          if(i + k >= goal) {
                        	  p[i][j][k] = 1;
                        	  roll[i][j][k] = false;
                        	  double change = Math.abs(p[i][j][k] - oldProb);
                              maxChange = Math.max(maxChange, change);
                          }
                          else {
                          for(int d1 = 1; d1 <= 6; ++d1) {
                          	for(int d2 = 1; d2<= 6; ++d2) {
                          		if(d1 == 1 && d2 == 1) {
                          			pRoll += (1 - pWin(j, 0, 0));
                          		}
                          		else if(d1 == 1 || d2 == 1) {
                          			pRoll += (1 - pWin(j, i, 0));
                          		}
                          		else {
                          			pRoll += pWin(i, j, k + (d1 + d2));
                          		}
                          	}
                          }
                          pRoll /= 36;
                          
                          //System.out.println(i + " " + j + " " + k + " " + pRoll);
                          p[i][j][k] = Math.max(pRoll, pHold);
                          roll[i][j][k] = pRoll > pHold;
                          double change = Math.abs(p[i][j][k] - oldProb);
                          maxChange = Math.max(maxChange, change);
                          }
                      }
          } while (maxChange >= theta);
      }

    //used chatgpt to debug finding min hold value
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
    
    public static void main(String[] args) {
    	new TwoDicePigSolver(100, 1e-11).outputRollValues();
    }
}

