import java.util.ArrayList;
import java.util.Arrays;

public class ReversePerfectShuffleNode extends SearchNode {
	int[] cards;
	
	public ReversePerfectShuffleNode()
	{
		cards = new int[52];
		for(int i = 0; i < 52; ++i)
		{
			cards[i] = i;
		}
	}
	
	@Override
	public boolean isGoal() 
	{
		if(cards[4] == 0 && cards[9] == 1 && cards[14] == 2 && cards[19] == 3)
			return true;
		return false;
	}

	@Override
	public ArrayList<SearchNode> expand() 
	{
		ArrayList<SearchNode> children = new ArrayList<>();
		int size = cards.length;
		int half = size/2;
		ReversePerfectShuffleNode out = (ReversePerfectShuffleNode) this.childClone();
		ReversePerfectShuffleNode in = (ReversePerfectShuffleNode) this.childClone();
		
		//antifaro out 
		for(int i = 0; i < size; ++i)
		{
			if(i < half)
				out.cards[i] = cards[i*2];
			else
				out.cards[i] = cards[((i-half)*2) + 1];
		}
		
		//antifaro in
		for(int i = 0; i < size; ++i)
		{
			if(i < half)
				in.cards[i] = cards[(i*2) + 1];
			else
				in.cards[i] = cards[(i - half)*2];
		}
		
		children.add(out);
		children.add(in);
		return children;
	}

	@Override
	public ReversePerfectShuffleNode clone()
	{
		ReversePerfectShuffleNode node = new ReversePerfectShuffleNode();
		for(int i = 0; i < 52; ++i)
		{
			node.cards[i] = cards[i];
		}
		return node;
	}
	
	public String toString()
	{
		String result = Arrays.toString(cards);
		result = result.substring(1, 197);
		return result;
	}
	
	public static void main(String[] args)
	{
		ReversePerfectShuffleNode node = new ReversePerfectShuffleNode();
		System.out.println(node.toString());
		
		for(SearchNode c: node.expand())
		{
			System.out.println(c.toString());
		}
		BreadthFirstSearcher bfs = new BreadthFirstSearcher();
		bfs.search(node);
		
		if (bfs.search(node)) {
			// successful search
			System.out.println("Goal node found in " + bfs.getNodeCount() 
			+ " nodes.");
			System.out.println("Goal path:");
			bfs.printGoalPath();
		} else {
			// unsuccessful search
			System.out.println("Goal node not found in " 
					+ bfs.getNodeCount() + " nodes.");
		}
	}
}

