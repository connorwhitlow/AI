import java.util.Stack;

public class DepthLimitedSearcher extends Searcher{
	int limit;
	
	public DepthLimitedSearcher(int limit)
	{
		this.limit = limit;
	}
	
	/**
	 * <code>search</code> - given an initial node, perform
	 * depth-first search (DFS).  This particular implementation of
	 * DFS is iterative.
	 *
	 * @param rootNode a <code>SearchNode</code> value - the initial node
	 * @return a <code>boolean</code> value - whether or not goal node
	 * was found */
	public boolean search(SearchNode rootNode) {
		// IMPLEMENT:

		// Initialize search variables.
		Stack<SearchNode> s = new Stack<>();
		s.add(rootNode);
		nodeCount = 0;

		// Main search loop.
		while (true) {

			// If the search stack is empty, return with failure
			if(s.isEmpty())
				return false;

			// Otherwise pop the next search node from the top of
			// the stack.
			SearchNode node = s.pop();
			++nodeCount;

			// If the search node is a goal node, store it and return
			// with success (true).
			if(node.isGoal())
			{
				goalNode = node;
				return true;
			}
				
			// Otherwise, expand the node and push each of its
			// children into the stack.
			for(SearchNode child: node.expand())
			{
				if(child.depth <= limit)
					s.push(child);
			}
		}

	}
}