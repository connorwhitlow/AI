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

public class DepthFirstSearcher extends Searcher {

	
	
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
				s.push(child);
			}
		}

	}

}// DepthFirstSearcher


