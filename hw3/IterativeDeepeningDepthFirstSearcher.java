import java.util.Queue;
import java.util.Stack;

/**
 * DepthFirstSearcher.java - a simple iterative implementation of
 * depth-first search.
 *
 * @author Todd Neller
 * @version 1.1
 *

Copyright (C) 2006 Todd Neller

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

Information about the GNU General Public License is available online at:
  http://www.gnu.org/licenses/
To receive a copy of the GNU General Public License, write to the Free
Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
02111-1307, USA.

 */

public class IterativeDeepeningDepthFirstSearcher extends Searcher {

	/**
	 * <code>search</code> - given an initial node, perform
	 * depth-first search (DFS).  This particular implementation of
	 * DFS is iterative.
	 *
	 * @param rootNode a <code>SearchNode</code> value - the initial node
	 * @return a <code>boolean</code> value - whether or not goal node
	 * was found */
	
	public boolean search(SearchNode rootNode)
	{
		int limit = Integer.MAX_VALUE;
		nodeCount = 0;
		for(int i = 0; i <= limit; ++i)
		{
			DepthLimitedSearcher dl = new DepthLimitedSearcher(i);
			if(dl.search(rootNode))
			{
				nodeCount += dl.getNodeCount();
				goalNode = dl.getGoalNode();
				return true;
			}
			nodeCount += dl.getNodeCount();
		}
		return false;
	}
}// IDDepthFirstSearcher
