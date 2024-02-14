import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

//used chatgpt for a "skeleton" of the class
public class RookJumpingMaze extends java.lang.Object implements State, java.lang.Cloneable {

    // Field summary
    public static final int UNREACHED; // low-value integer constant indicating that a grid position cannot be reached by search.
    int[][] grid;
    int size;
    int energy;
    int oldCol, oldRow, oldJump;
    ArrayList<String> visited;

    // Constructor
    /**
     * Constructs a RookJumpingMaze that is a size-by-size grid with random, legal jump numbers for each position.
     * @param size - number of rows/columns
     */
    public RookJumpingMaze(int size) {
    	energy = 0;
    	visited = new ArrayList<>();
    	this.size = size;
        grid = new int[size][size];
        Random rand = new Random();
        
        for (int r = 0; r < size; ++r) {
            for (int c = 0; c < size; ++c) {
                int maxJump = Math.max(size - r - 1, r); // Maximum jump based on row
                int maxColJump = Math.max(size - c - 1, c); // Maximum jump based on column
                int legalJump = Math.max(maxJump, maxColJump); // The larger of the two determines the max legal jump

                if (legalJump > 0) { // Ensure there's room to jump
                    grid[r][c] = rand.nextInt(legalJump) + 1; // Assign a random legal jump (1 to legalJump)
                } else {
                    grid[r][c] = 1; // If no room to jump (corner cells), assign the minimum legal jump
                }
            }
        }
        grid[size-1][size-1] = 0;
    }

    // Method summary

    /**
     * Returns the jump number for the given row and column.
     * @param row the given row
     * @param col the given column
     * @return the jump number for the given row and column
     */
    public int getJump(int row, int col) {
        return grid[row][col]; // placeholder return
    }

    /**
     * Returns the size of the square maze grid.
     * @return size of the square maze grid
     */
    public int getSize() {
        return size;
    }

    /**
     * Returns the negated distance from the start position to the goal grid position.
     * @return negated distance to the goal or RookJumpingMaze.UNREACHED
     */
    public double energy() {
    	Pair root = new Pair(0,0);
    	Queue<Pair> q = new LinkedList<>();
    	q.add(root);
    	while(true) {
    		
    		if(q.isEmpty()) {
    			return UNREACHED;
    		}
    		
    		Pair cur = q.poll();
    		
    		if(isGoal(cur)) {
    			return energy;
    		}
    		else{
    			int added = 0;
    			for(Pair r: expand(cur)) {
    				if(!visited.contains(r.toString())) {
    					++added;
    					q.add(r);
    					visited.add(cur.toString());
    				}
    				if(added == 1) {
    					--energy;
    				}
    			}
    		}
    	}
    }

    /**
     * Picks a non-goal grid position at random and changes it to a different legal jump number.
     */
    public void step() {
    	Random rand = new Random();
    	int replaceRow = rand.nextInt(size);
    	int replaceCol = rand.nextInt(size);
    	while(replaceRow == size - 1 && replaceCol == size - 1) {
    		replaceRow = rand.nextInt(size);
    		replaceCol = rand.nextInt(size);
    	}
    	int maxJump = Math.max(size - replaceRow - 1, replaceRow); // Maximum jump based on row
        int maxColJump = Math.max(size - replaceCol - 1, replaceCol); // Maximum jump based on column
        int legalJump = Math.max(maxJump, maxColJump); // The larger of the two determines the max legal jump
        int newJump = rand.nextInt(legalJump) + 1;
        while (newJump ==  grid[replaceRow][replaceCol]) { // Ensure there's room to jump
        	newJump = rand.nextInt(legalJump) + 1; // Assign a random legal jump (1 to legalJump)
        }
        oldJump = grid[replaceRow][replaceCol];
        oldRow = replaceRow;
        oldCol = replaceCol;
        grid[replaceRow][replaceCol] = newJump;
    }

    /**
     * Undoes the change of the previous step() call.
     */
    public void undo() {
    	grid[oldRow][oldCol] = oldJump;
    }

    /**
     * Performs a deep clone of this RookJumpingMaze, such that changes to the copy do not affect the original or vice versa.
     * @return a deep clone of this RookJumpingMaze
     */
    @Override
    public RookJumpingMaze clone() {
        RookJumpingMaze clone;
		try {
			clone = (RookJumpingMaze) super.clone();
			 for (int r = 0; r < size; ++r) {
		            for (int c = 0; c < size; ++c) {
		            	clone.grid[r][c] = grid[r][c];
		            }
			 }
			 clone.oldRow = oldRow;
			 clone.oldJump = oldJump;
			 clone.oldCol = oldCol;
			 clone.energy = energy;
			 clone.size = size;
			return clone;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
    }

    /**
     * Returns a string representation of the RookJumpingMaze.
     * For each column, append the jump number and a trailing space.
     * At the end of each row, append a newline character.
     * @return string representation of the maze
     */
    @Override
    public java.lang.String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; ++i) {
        	for(int c = 0; c < size; ++c)
        		sb.append(grid[i][c] + " ");
        	sb.append("\n");
        }
        return sb.toString(); // placeholder return
    }

    // Initialize static fields
    static {
        UNREACHED = 80; // example initialization, adjust as needed
    }
    
    public boolean isGoal(Pair p) {
    	return p.r == (size - 1) && p.c == (size - 1);
    }
    
    //cgpt: given the implementation of step, return an arraylist of all legal moves
    public ArrayList<Pair> expand(Pair p) {
        ArrayList<Pair> neighbors = new ArrayList<>();
        int[] dRow = {0, 0, -1, 1}; // Directions: Up, Down
        int[] dCol = {-1, 1, 0, 0}; // Directions: Left, Right

        for (int i = 0; i < 4; i++) {
        	
            int nextRow = p.r + (dRow[i] * getJump(p.r, p.c));
            int nextCol = p.c + (dCol[i] * getJump(p.r, p.c));
            // Check if the move is within the grid bounds
            if (nextRow >= 0 && nextRow < size && nextCol >= 0 && nextCol < size) {
                Pair neighbor = p.clone(); // Clone the current state
                neighbor.r = nextRow; // Update the row
                neighbor.c = nextCol; // Update the column
                neighbors.add(neighbor); // Add the new state to the list
            }
        }
        return neighbors;
    }
}

class Pair implements Cloneable {
	int r;
	int c;
	
	public Pair(int r, int c) {
		this.r = r;
		this.c = c;
	}

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	public int getC() {
		return c;
	}

	public void setC(int c) {
		this.c = c;
	}
	
	public Pair clone() {
		Pair clone = new Pair(r, c);
		return clone;
	}
	
	public String toString()
	{
		return r + "," + c;
	}
}

