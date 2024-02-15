import java.util.Random;

public class SimulatedAnnealer {
	State state;
	double energy;
	State minState;
	double minEnergy;
	double acceptRate = 0;
	double initTemp;
	double decayRate;
	
	public SimulatedAnnealer(State state, double initTemp, double decayRate) {
		this.state = state;
		energy = state.energy();
		minState = (State) state.clone();
		minEnergy = energy;
		this.initTemp = initTemp;
		this.decayRate = decayRate;
	}
	
	public State search(int iterations) {
		int badCounter = 0;
		Random random = new Random();
		double temperature = initTemp;
		for(int i = 0; i < iterations; ++i) {
			if(i % 1000 == 0)
				System.out.println(minEnergy + "\t" + energy);
			state.step();
			double nextEnergy = state.energy();
			if(nextEnergy <= energy || random.nextDouble() < Math.exp((energy - nextEnergy) / temperature)) {
				if(nextEnergy > -18)
					++badCounter;
				if(badCounter > 320) {
					System.out.println("working");
					temperature *= 1.29;
					badCounter = 0;
				}
				energy = nextEnergy;
				minState = (State) state.clone();
				minEnergy = nextEnergy;
			}
			else
				state.undo();
			temperature *= decayRate;
		}
		return minState;
	}
}