import java.util.ArrayList;



public class GridNavigationNode extends HSearchNode{
	
	private int row;
	private int col;
	private int goalRow;
	private int goalCol;
	private boolean[][] isBlocked;
	private boolean[][] isVisited;
	private double distanceTravelled = 0;
	
	public GridNavigationNode(int startRow, int startCol, int goalRow, int goalCol, boolean[][] isBlocked) {
		
		row = startRow;
		col = startCol;
		this.goalRow = goalRow;
		this.goalCol = goalCol;
		isVisited = new boolean[isBlocked.length][isBlocked[0].length];

        this.isBlocked = new boolean[isBlocked.length][isBlocked[0].length];
		for(int i =0 ; i < isBlocked.length; ++i) {
			for(int j = 0; j < isBlocked[i].length; ++j) {
				this.isBlocked[i][j] =  isBlocked[i][j];
			}
		}
		
	}

	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	@Override
	public double getG() {
		return distanceTravelled;
	}

	@Override
	public double getH() {
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
				if(!isBlocked[newRow][newCol] && !isVisited[newRow][newCol]) {
					double stepDistance = (dx[i] == 0 || dy[i] == 0) ? 1.0 : Math.sqrt(2);
					isVisited[newRow][newCol] = true;
					GridNavigationNode child = (GridNavigationNode) childClone();
					child.row = newRow;
					child.col = newCol;
					child.distanceTravelled += stepDistance;
					child.isVisited = isVisited;
					child.isBlocked = new boolean[isBlocked.length][isBlocked[0].length];
					 for (int k = 0; k < isBlocked.length; k++) {
				            for (int j = 0; j < isBlocked[0].length; j++) {
				                child.isBlocked[k][j] = isBlocked[k][j];
				            }
				        }
					children.add(child);
				}
			}
		}
		return children;
	}
}
