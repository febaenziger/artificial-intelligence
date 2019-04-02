import java.util.ArrayList;

public class AINode {
	
	private String gameState;
	private AINode parent;
	private ArrayList<AINode> children;
	private int depth;
	private int pathCost;
	private String actionTaken;
	private boolean visited;
	private int f;
	private int g;
	private int h;

	public AINode(String gameState, AINode parent, ArrayList<AINode> children, int depth, int pathCost, String actionTaken, boolean visited, int f, int g, int h) {
		this.gameState = gameState;
		this.parent = parent;
		this.children = children;
		this.depth = depth;
		this.pathCost = pathCost;
		this.actionTaken = actionTaken;
		this.visited = visited;
		this.f = f;
		this.g = g;
		this.h = h;
	}
	// NEED
		// PARENT NODE
		//  DEPTH
		// PATH_COST
		// If it's expanded or not
		// Action Taken (U, D, L, R)
		// Children
	
	// Creating all the SET functions
	public void setGameState(String gameState) {
		this.gameState = gameState;
	}
	
	public void setParent(AINode parent) {
		this.parent = parent;
	}
	
	public void addChild(AINode child) {
		this.children.add(child);
	}
	
	public void setActionTaken(String actionTaken) {
		this.actionTaken = actionTaken;
	}
	
	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	
	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	public void setPathCost(int pathCost) {
		this.pathCost = pathCost;
	}
	
	public void setF(int f) {
		this.f = f;
	}
	
	public void setG(int g) {
		this.g = g;
	}
	
	public void setH(int h) {
		this.h = h;
	}
	
	// Creating all the GET functions
	
	public String getGameState() {
		return gameState;
	}
	
	public AINode getParent() {
		return parent;
	}
	
	public ArrayList<AINode> getChild() {
		return children;
	}
	
	public String getActionTaken() {
		return actionTaken;
	}
	
	public boolean getVisited() {
		return visited;
	}
	
	public int getDepth() {
		return depth;
	}
	
	public int getPathCost() {
		return pathCost;
	}
	
	public int getF() {
		return f;
	}
	
	public int getG() {
		return g;
	}
	
	public int getH() {
		return h;
	}
}
