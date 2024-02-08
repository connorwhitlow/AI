import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class HPancakeSortNode extends HSearchNode{
	int cost;
	int lastFlip;
	int[] pancake;
	
	public HPancakeSortNode(int[] pancake)
	{
		cost = 0;
		this.pancake = new int[pancake.length];
		for(int i = 0; i < pancake.length; ++i)
		{
			this.pancake[i] = pancake[i];
		}
	}

	public HPancakeSortNode(int size, int numFlips)
	{
		pancake = new int[size];
		for(int i = 0; i < size; ++i)
		{
			pancake[i] = i;
		}
		
		for(int i = 0; i < numFlips; ++i)
		{
			Random rand = new Random(42);
			flip(rand.nextInt(size-1)+2);
		}
	}
	
	public void flip(int n)
	{
		int j = n - 1;
		int i = 0;
		while(i < j)
		{
			int temp = pancake[j];
			pancake[j] = pancake[i];
			pancake[i] = temp;
			++i;
			--j;
		}
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Cost: " + cost);
		sb.append(", Last Flip: " + lastFlip);
		sb.append(", Pancake Order: " + Arrays.toString(pancake));
		return sb.toString();
	}

	@Override
	public double getG() {
		return this.cost;
	}

	@Override
	public double getH() {
		int h = 0;
		for(int i = 1; i < pancake.length; ++i)
		{
			if(Math.abs(pancake[i] - pancake[i-1]) > 1)
				h += 2;
		}
		return h;
	}

	@Override
	public boolean isGoal() {
		for(int i = 0; i < pancake.length; ++i)
		{
			if(pancake[i] != i)
				return false;
		}
		return true;
	}

	@Override 
	public HPancakeSortNode clone()
	{
		HPancakeSortNode copy = (HPancakeSortNode) super.clone();
		int size = pancake.length;
		copy.pancake = new int[size];
		for(int i = 0; i < size; ++i)
		{
			copy.pancake[i] = pancake[i];
		}
		return copy;
	}
	
	@Override
	public ArrayList<SearchNode> expand() {
		ArrayList<SearchNode> children = new ArrayList<>();
		for(int i = 2; i < pancake.length + 1; ++i)
		{
			HPancakeSortNode child = (HPancakeSortNode) childClone();
			child.flip(i);
			child.lastFlip = i;
			child.cost = this.cost + i;
			children.add(child);
		}
		return children;
	}
	
	static HPancakeSortNode getGoalNode(int[] pancake)
	{
		HPancakeSortNode root = new HPancakeSortNode(pancake);
		Searcher searcher = new BestFirstSearcher(new AStarComparator());
		if (searcher.search(root)) {
		    // successful search
		    System.out.println("Goal node found in " + searcher.getNodeCount() 
				       + " nodes.");
		    System.out.println("Goal path:");
		    searcher.printGoalPath();
		} else {
		    // unsuccessful search
		    System.out.println("Goal node not found in " 
				       + searcher.getNodeCount() + " nodes.");
		}
		return (HPancakeSortNode) searcher.goalNode;
	}
	
	public int getLastFlip()
	{
		return lastFlip;
	}
	
	public static void main(String[] args)
	{
		int[] trial = {5, 2, 7, 4, 1, 3, 6, 0}; 
		getGoalNode(trial);
	}
}
