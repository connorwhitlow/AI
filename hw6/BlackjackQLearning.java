import java.util.TreeMap;

import java.util.Map;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class BlackjackQLearning {
	final int HIT = 1, STAY = 0, GOAL = 21, CARDS = 13, MAX = 10;
	Map<Integer, double[]> q = new TreeMap<>();
	
	class State{
		int m, d;
		boolean u;

		//i copied and pasted our class code then told chat gpt to change the variable names to m, d and u 
		public State(int m, int d, boolean u) {
	        this.m = m; // Corrected from `this.u = i;`
	        this.d = d; // Corrected from `this.t = t;`
	        this.u = u; // Added to match the provided parameters.
	    }

	    public State() {
	    	int o = (int) (Math.random() * CARDS) + 1; // Corrected from `i = ...;`
	    	int t = (int) (Math.random() * CARDS) + 1; // Corrected from `i = ...;`
	    	o = (o > MAX) ? 10 : o;
	    	t = (t > MAX) ? 10 : t;
	    	if(o == 1 && t != 1 && t + 11 <= 21) {
	    		m = t + 11;
	    		u = true;
	    	}
	    	else if(t == 1 && o != 1) {
	    		m = o + 11;
	    		u = true;
	    	}
	    	else if (o == 1 && t == 1){
	    		m = 12;
	    		u = true;
	    	}
	    	else {
	    		m = o + t; // Corrected from `i = ...;`
	    	}
	        d = (int) (Math.random() * CARDS) + 1; // Corrected from `t = ...;`
	        d = (d > MAX) ? 10 : d;
	        d = d == 1 ? 11 : d;
	    }

	    public State(State state) {
	        this.m = state.m; // Corrected from `i = state.i;`
	        this.d = state.d; // Corrected from `t = state.t;`
	        this.u = state.u; // Corrected to match the variable name.
	    }

	    @Override
	    public State clone() {
	        return new State(this);
	    }

	    @Override
	    public String toString() {
	        return "State [Total = " + m + ", Dealer Card = " + d + ", Usable Ace = " + u + "]";
	    }

	    boolean isTerminal() {
	        return (!u && m > GOAL) || m == GOAL || d >=21; 
	    }
		
		@Override 
		public int hashCode() {
//			final int prime = 31;
//			int result = 1;
//			result = prime * result + Objects.hash(i, k , t);
//			return result;
			return 1000000*m + 1000*d + (u ? 1 : 0);
		}
		
		@Override
	    public boolean equals(Object obj) {
	        if (this == obj)
	            return true;
	        if (obj == null || getClass() != obj.getClass())
	            return false;
	        State other = (State) obj;
	        return m == other.m && d == other.d && u == other.u;
	    }
		
	}
	
	private double env(State s, int a) {
		if(a == HIT) {
			int card = (int) (Math.random()*CARDS + 1);
			card = (card > MAX) ? MAX : card;
			if(card == 1) {
				if(s.m + 11 > GOAL) {
					++s.m;
					s.u = false;
				}
				else {
					s.m += 11;
					s.u = true;
				}
			}
			else {
				if(s.m + card > GOAL && s.u) {
					s.m -= 10;
					s.u = false;
				}
				s.m += card;
			}
		}
		int dealerTotal = s.d;
		boolean dealerSoft = s.d == 11;
		while(dealerTotal < 17 || (dealerTotal == 17 && dealerSoft == true)) {
			 
			int dealerHit = (int) (Math.random()*CARDS + 1);
			
			dealerHit = dealerHit > 10 ? 10 : dealerHit;
			if(dealerHit == 1 && dealerTotal + 11 <= 21) {
				dealerSoft = true;
				dealerTotal += 11;
			}
			else if(dealerHit == 1 && dealerTotal + 11 > 21) {
				dealerSoft = false;
				++dealerTotal;
			}
			else
				dealerTotal += dealerHit;
		}
		
//		if(dealerTotal > GOAL)
//			return 1;
//		else if(dealerTotal == s.m)
//			return 0;
//		else if(dealerTotal > s.m)
//			return -1;
		
		return s.m == GOAL ? 1.5 : (s.m > GOAL ? -2 : (s.m > dealerTotal) ? 1 : -1);
	}
	
	double[] getQS(State s) {
		int hashCode = s.hashCode();
		double[] qActions = q.get(hashCode);
		if(qActions == null) {
			qActions = new double[2];
			q.put(hashCode, qActions);
		}
		return qActions;
	}
	
	int getEpsilonGreedyAction(double[] qActions, double epsilon) {
		ArrayList<Integer> bestActions = new ArrayList<>();
		bestActions.add(0);
		double bestQ = qActions[0];
		for(int i = 1; i < qActions.length; ++i) {
			double nextQ = qActions[i];
			if(nextQ >= bestQ) {
				if(nextQ > bestQ) {
					bestQ = nextQ;
					bestActions.clear();
				}
				bestActions.add(i);
			}
		}
		//Epsilon-greedy action
		int action = bestActions.get((int) (Math.random()*bestActions.size()));
		if(Math.random() < epsilon) {
			action = (int) (Math.random()*qActions.length);
		}
		return action;
	}
	
	
	public BlackjackQLearning(double alpha, double epsilon, double gamma, double decay, int numIter) {
		//initialize Q(s,a) - implicitly done in getQS() 
		//Loop for each episode:
		for(int i = 0; i < numIter; ++i) {
			//Initialize s
			State s = new State();
			double[] qActions = getQS(s);
			//loop for each step of episode until s is terminal
			while(!s.isTerminal()) {
				
				//Choose A from using policy derived from Q (e.g. epsilon-greedy)
				int action = getEpsilonGreedyAction(qActions, epsilon);
				//Take Action A, Observe R, S'
				double reward = env(s, action);
				//Update Q(S, A)
				double[] nextQActions = getQS(s);
				double maxQ = 0;
				if(!s.isTerminal())
					maxQ = nextQActions[getEpsilonGreedyAction(nextQActions, 0)];
				qActions[action] = (1 - alpha)*(qActions[action]) + alpha*(reward + (gamma*maxQ));
				qActions = nextQActions;
			}
			if(i % 10000 == 0)
				System.out.println(s);
			alpha *= decay;
			epsilon *= decay;
		}
	}
	
	double getSimWinRate(int numIter) {
		int wins = 0;
		for(int i = 0; i < numIter; ++i) {
			if(simulate())
				++wins;
		}
		return (double) wins / numIter;
	}
	
	public boolean simulate() {
		State s = new State();
		double res = 0;
		int iterations = 0;
		while(!s.isTerminal()) {
			int action = getEpsilonGreedyAction(getQS(s), 0);
//			System.out.println("inside");	
			res = env(s, action);
//			System.out.println("inside"+res);	
			if (iterations >= 1000) { // Safeguard to prevent infinite loop
		        break;
		    }
		    // Existing logic to choose an action and update the state
		    iterations++;
		}
	
		return res > 0;
	}
	
	public void summarize() {
		//minimum hold values for row i, column t, turn total k
				for(int u = 0; u < 2; ++u) {
					if(u == 0)
						System.out.println("HARD:");
					else
						System.out.println("SOFT:");
					for(int m = 0; m < CARDS; ++m) {
						for(int d = 0; d < CARDS; ++d) {
							double[] qActions = getQS(new State(m, d, (u == 0) ? false : true));
							System.out.printf("%b, ", qActions[HIT] > qActions[STAY]);
						}
						System.out.println();
					}
				}
	}
	
	//chatgpt: based off the class called BlackjackQLearning, write a method called summarize that will produce two tables in a csv file, one table has a label over it that says "hard" The x axis says "Dealer card" is starts at A, then 2, 3, 4, ... 10, J, Q, K. The Y axis says "Total" is starts at 17 and goes down to 8. If qActions hit > q actions stand at that cell, turn the cell green. otherwise, make the cell red. Then, do the same with a new table with a label over it named "Soft:" The x axis says "Dealer card" is starts at A, then 2, 3, 4, ... 10, J, Q, K. The Y axis says "Total" is starts at 20 and goes down to 13. (I then gave it the class)
	public void summarizeToCSV() {
	    String[] dealerCards = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
	    try (FileWriter writer = new FileWriter("blackjack_strategy.csv")) {
	        for (int u = 0; u < 2; u++) {
	            if (u == 0) {
	                writer.append("HARD\n");
	            } else {
	                writer.append("\nSOFT\n");
	            }
	            writer.append("Dealer Card,");
	            for (String card : dealerCards) {
	                writer.append(card).append(",");
	            }
	            writer.append("\n");

	            int startTotal = (u == 0) ? 17 : 20;
	            int endTotal = (u == 0) ? 8 : 13;

	            for (int m = startTotal; m >= endTotal; m--) {
	                writer.append("Total ").append(Integer.toString(m)).append(",");
	                for (int d = 1; d <= CARDS; d++) {
	                    State tempState = new State(m, d, u == 1);
	                    double[] qActions = getQS(tempState);
	                    String actionColor = qActions[HIT] > qActions[STAY] ? "H" : "S";
	                    writer.append(actionColor).append(",");
	                }
	                writer.append("\n");
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	    
	public static void main(String[] args) {
		double alpha = .1;
		double epsilon = .1;
		double gamma = 1;
		double decay = .99999999;
		int iter = 1000000;
		BlackjackQLearning bj = new BlackjackQLearning(alpha, epsilon, gamma, decay, iter);
		//System.out.println("hi");
		//bj.summarize();
		System.out.println(bj.getSimWinRate(iter));
		bj.summarizeToCSV();
		
	}
}
