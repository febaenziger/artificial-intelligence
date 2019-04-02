import java.util.ArrayList;

public class dbNode {

	private dbNode parent;
	private int[][] board;
	private ArrayList<dbNode> children;
	private int cost;
	private int comp_score;
	private int player_score;
	private int[] bestXY;
	private int player;
	
	// Creating a custom node class to carry all the information that I need
	public dbNode(dbNode parent, int[][] board, ArrayList<dbNode> children, int cost, int[] bestXY, int player) {
		this.parent = parent;
		this.board = board;
		this.children = children;
		this.cost = cost;
		this.bestXY = bestXY;
		this.player = player;
//		this.comp_score = comp_score;
//		this.player_score = player_score;
	}
	
	// Creating all the SET functions
	public void setParent(dbNode parent) {
		this.parent = parent;
	}
	
	public void setBoard(int[][] current_board) {
		this.board = board;
	}
	
	public void setChildren(ArrayList<dbNode> children) {
		this.children = children;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}
	
	public void setBestXY(int[] bestXY) {
		this.bestXY = bestXY;
	}

	public void setPlayer(int player) {
		this.player = player; // 1 to indicate user, 2 to indicate computer
	}
	
	// Creating all the GET functions
	public dbNode getParent() {
		return this.parent;
	}
	
	public int[][] getBoard() {
		return this.board;
	}
	
	public ArrayList<dbNode> getChildren() {
		return this.children;
	}

	public int getCost() {
		return this.cost;
	}
	
	public int[] getBestXY() {
		return this.bestXY;
	}

	public int getPlayer() {
		return this.player; // 1 to indicate user, 2 to indicate computer
	}
}
