import java.util.ArrayList;



public class GridNavigationNode extends HSearchNode{
	
	private int row;
	private int col;
	private int goalRow;
	private int goalCol;
	private boolean[][] isBlocked;
	private double distanceTravelled = 0;
	
	public GridNavigationNode(int startRow, int startCol, int goalRow, int goalCol, boolean[][] isBlocked) {
		
		row = startRow;
		col = startCol;
		this.goalRow = goalRow;
		this.goalCol = goalCol;

        this.isBlocked = new boolean[isBlocked.length][isBlocked[0].length];
		for(int i =0 ; i < isBlocked.length; ++i) {
			for(int j = 0; j < isBlocked[i].length; ++j) {
				this.isBlocked[i][j] =  isBlocked[i][j];
			}
		}
		System.out.println(isBlocked.toString()+"ISBLOCKED");
		
	}

	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	@Override
	public double getG() {
		// get the total Euclidean distance traveled to reach the node. Horizontal and vertical moves contribute 1.0, and diagonal moves contribute square root of 2.
		// The Euclidean distance is the square root of the sum of the squares of the horizontal and vertical distances.
		// The distance between two points (x1, y1) and (x2, y2) is sqrt((x2 - x1)^2 + (y2 - y1)^2).
		// returns the cost to reach node n

		return distanceTravelled;
	}

	@Override
	public double getH() {
		// Get an admissible estimate of the remaining distance to the goal row and column in the form of the Euclidean distance from this node to the goal.
		// The Euclidean distance is the square root of the sum of the squares of the horizontal and vertical distances.

		return Math.sqrt((goalRow - row) * (goalRow - row) + (goalCol - col) * (goalCol - col));
	}

	@Override
	public boolean isGoal() {
		if(goalRow == row && goalCol == col)
		{
			return true;
		}
		return false;
	}

	@Override
	public ArrayList<SearchNode> expand() {
		ArrayList<SearchNode> children = new ArrayList<>();
		
		int[] dx = {-1, 1, 0, 0, -1, -1, 1, 1};
	    int[] dy = {0, 0, -1, 1, -1, 1, -1, 1};
		
		for(int i = 0; i < 8; ++i) {
			int newRow = row + dx[i];
			int newCol = col + dy[i];
			if(newRow >= 0 && newRow < isBlocked.length && newCol >= 0 && newCol < isBlocked[0].length) {
				if(!isBlocked[newRow][newCol]) {
					double childDistance = Math.sqrt((newRow - row) * (newRow - row) + (newCol - col) * (newCol - col));
	                   
					distanceTravelled +=  childDistance;
					GridNavigationNode child = (GridNavigationNode) childClone();
					child.row = newRow;
					child.col = newCol;
//					child.goalCol = goalCol;
//					child.goalRow = goalRow;
					child.distanceTravelled = distanceTravelled;
					child.isBlocked = new boolean[isBlocked.length][isBlocked[0].length];
					 for (int k = 0; k < isBlocked.length; k++) {
				            for (int j = 0; j < isBlocked[k].length; j++) {
				                child.isBlocked[k][j] = isBlocked[k][j];
				            }
				        }
					children.add(child);
				}
			}
		}
		return children;
	}
	
	public String toString() {
		return  "(f:" + getF() + ",g:" + getG() + ",h:" + getH() + ")";
	}
}
