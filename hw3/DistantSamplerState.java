import java.util.ArrayList;
import java.util.Arrays;

public class DistantSamplerState implements State {
	private double[][] data;
	private int numSamples;
	private int undoSample1;
	private int undoSample2;
	private int[] sampleIndices;

	public DistantSamplerState(double[][] data, int numSamples) {

		this.numSamples = numSamples;
		this.data = data;
		sampleIndices = new int[numSamples];

		ArrayList<Integer> indexList = new ArrayList<>();
		for (int i = 0; i < data.length; i++) {
			indexList.add(i);
		}
//		System.out.println("Initial indices: " + indexList); // Debug output

		for (int i = 0; i < numSamples; i++) {
			int randomIndex = (int) (Math.random() * indexList.size());
			sampleIndices[i] = indexList.get(randomIndex);
			indexList.remove(randomIndex);
		}
//		System.out.println("Sample indices: " + Arrays.toString(sampleIndices)); // Debug output
	}


	public void step() {
		int sample = (int) (Math.random() * numSamples);
		int otherSample = (int) (Math.random() * numSamples);
		while (sample == otherSample) {
			otherSample = (int) (Math.random() * numSamples);
		}
	
			int temp = sampleIndices[sample];
			sampleIndices[sample] = sampleIndices[otherSample];
			sampleIndices[otherSample] = temp;
			undoSample1 = sample;
			undoSample2 = otherSample;
	
	}

	public void undo() {
	        int temp = sampleIndices[undoSample1];
	        sampleIndices[undoSample1] = sampleIndices[undoSample2];
	        sampleIndices[undoSample2] = temp;
	
	}

	public double energy() {
		double totalEnergy = 0;
	
		for (int i = 0; i < numSamples - 1; ++i) { 
			double[] firstPoint = data[sampleIndices[i]];
			for (int j = i + 1; j < numSamples; ++j) { 
				double dist = 0;
				double[] secPoint = data[sampleIndices[j]];
				for (int k = 0; k < data[0].length; ++k) {
					dist += Math.pow(firstPoint[k] - secPoint[k], 2);
				}
				if (dist != 0) {
					totalEnergy += 1.0 / dist;
				}
			}
		}
		return totalEnergy;
	}


	public DistantSamplerState clone() {
	    try {
	        DistantSamplerState clone = (DistantSamplerState) super.clone();
	        clone.data = new double[data.length][];
	        clone.sampleIndices = sampleIndices.clone(); // Cloning the sampleIndices array
	        for (int i = 0; i < data.length; ++i) {
	            clone.data[i] = data[i].clone();
	        }
	        return clone;
	    } catch (CloneNotSupportedException e) {
	        e.printStackTrace();
	    }
	    return null;
	}

	public int[] getSampleIndices() {
		int[] indices = new int[sampleIndices.length];
		for (int i = 0; i < indices.length; ++i) {
			indices[i] = sampleIndices[i];
		}
		Arrays.sort(indices);
		return indices;
	}

	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("[");
	    for (int i = 0; i < sampleIndices.length; i++) {
	        if (i > 0) {
	            sb.append(", ");
	        }
	        int index = sampleIndices[i];
	        if (index >= 0 && index < data.length) {
	            sb.append(Arrays.toString(data[index]));
	        } else {
	            sb.append("Invalid index" + index +" " + data.length);
	        }
	    }
	    sb.append("]");
	    return sb.toString();
	}
}
