
public class RecursiveDepthFirstSearcher extends Searcher{
	
	public boolean search(SearchNode node) {
		++nodeCount;
		if(node.isGoal())
		{
			goalNode = node;
			return true;
		}
		
		
		for(SearchNode child: node.expand())
		{
			if(search(child))
				return true;
		}
		return false;
	}
}

