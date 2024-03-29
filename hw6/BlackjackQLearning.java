import java.util.TreeMap;

import java.util.Map;
import java.util.ArrayList;

public class BlackjackQLearning {
	final int HIT = 1, STAY = 0, GOAL = 21, CARDS = 13;
	Map<Integer, double[]> q = new TreeMap<>();
	
	class State{
		int m, d;
		boolean u;

		public State(int m, int d, boolean u) {
	        this.m = m; // Corrected from `this.u = i;`
	        this.d = d; // Corrected from `this.t = t;`
	        this.u = u; // Added to match the provided parameters.
	    }

	    public State() {
	    	int o = (int) (Math.random() * CARDS) + 1; // Corrected from `i = ...;`
	    	int t = (int) (Math.random() * CARDS) + 1; // Corrected from `i = ...;`
	        m = o + t; // Corrected from `i = ...;`
	        d = (int) (Math.random() * CARDS) + 1; // Corrected from `t = ...;`
	        if(o == 1 || t == 1)
	        	u = Math.random() < 0.5; // Assuming you want a random boolean value for `u`
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
	        return (!u && m > GOAL) || m == GOAL; 
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
			if(card == 1) {
				if(s.m + 11 > GOAL)
					++s.m;
				else
					s.m += 11;
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
		while(dealerTotal < 17) {
			dealerTotal += (int) (Math.random()*CARDS + 1);
		}
		
		if(dealerTotal > GOAL)
			return 1;
		else if(dealerTotal == s.m)
			return 0;
		else if(dealerTotal > s.m)
			return -1;
		
		return s.m == GOAL ? 1 : (s.m > GOAL ? -1 : (s.m > dealerTotal) ? 1 : -1);
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
		while(!s.isTerminal()) {
			env(s, getEpsilonGreedyAction(getQS(s), 0));
		}
		return s.m == GOAL;
	}
	
	public void summarize() {
		//minimum hold values for row i, column t, turn total k
				for(int u = 0; u < 2; ++u) {
					for(int m = 0; m < CARDS; ++m) {
						for(int d = 0; d < CARDS; ++d) {
							double[] qActions = getQS(new State(m, d, (u == 0) ? false : true));
							System.out.printf("%b, ", qActions[HIT] > qActions[STAY]);
						}
					}
					System.out.println();
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
		bj.summarize();
		System.out.println(bj.getSimWinRate(iter));
	}
}
