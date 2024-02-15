import java.util.Arrays;

public class DistantSamplerState implements State{
	private double[][] data;
	private int numSamples;
	private int undoSample1;
	private int undoSample2;

	private int[] sampleIndices;


	public DistantSamplerState(double[][] data, int numSamples) {
		this.numSamples = numSamples;
		this.data = data;
		sampleIndices = new int[numSamples];

		int[] indices = new int[data.length];
		for (int i = 0; i < data.length; i++) {
			indices[i] = i;
		}
		for (int i = 0; i < numSamples; i++) {
			int randomIndex = (int) (Math.random() * indices.length);
			sampleIndices[i] = indices[randomIndex];
			// Remove the selected index to ensure uniqueness
			indices[randomIndex] = indices[indices.length - 1];
			indices = Arrays.copyOf(indices, indices.length - 2);
		}

	}


	public void step(){

		int sample = (int)(Math.random() * numSamples);

		int otherSample = (int)(Math.random() * numSamples);
		while(sample == otherSample){
			otherSample = (int)(Math.random() * numSamples);
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
		for(int i = 0; i < data.length; ++i) {
			for(int j = i+1; j < data.length; ++j) {

				for(int k = 0; k < data[0].length; ++k) {
					double diff = data[i][k] - data[j][k];
					totalEnergy += 1/(diff);
				}


			}
		}
		return totalEnergy;
	}

	public DistantSamplerState clone() {
		try {
			DistantSamplerState clone = (DistantSamplerState) super.clone();
			clone.data = new double[numSamples][];
			for (int i = 0; i < numSamples; ++i) {
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
		for(int i = 0; i < indices.length; ++i) {
			indices[i] = sampleIndices[i];
		}
		Arrays.sort(indices);
		return indices;
	}

	public String toString() {
		return data.toString();
	}
}
