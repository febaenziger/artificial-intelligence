import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;


public class searchAlgorithms {
	
	// global goal state
	static String GoalState = "123804765";
	
	// the main loop that controls user input and calls the functions
	public static void main(String args[]) {
		
		// Assigning and initializing variables
		int difficulty = 0; // 1-easy, 2-medium, 3-hard
		int algorithm = 0; // 1-BFS, 2-DFS, 3-uniform cost, 4-best first, 5-A1, 6-A2
		String easy = "134862705";
		String medium = "281043765";
		String hard = "567408321";
		String puzzle;
		
		System.out.println("Welcome to the Eight Puzzle Problem!");
		
		// Collecting user input! This is the text interface
		Scanner scanner = new Scanner(System.in);
		
		// Running a while loop to get user input for difficulty level
		boolean cond = true;
		while (cond) {
		System.out.println("\nPlease choose puzzle difficulty:\n1. Easy\n2. Medium\n3. Hard\n\nChoose a number:");
			difficulty = scanner.nextInt();
			if (difficulty > 0 && difficulty < 4) cond = false;
			else System.out.println("\nNot a valid number. Please try again.");
		}
		
		System.out.println("\n---------------------------------------\n");
		
		// Running a while loop to get user input for what algorithm
		cond = true;
		while (cond) {
			System.out.println("Please choose AI algorithm to solve the puzzle:\n"
					+ "1. BFS\n"
					+ "2. DFS\n"
					+ "3. Uniform Cost\n"
					+ "4. Best First\n"
					+ "5. A1\n"
					+ "6. A2\n\n"
					+ "Choose a number:");
			algorithm = scanner.nextInt();
			if (algorithm > 0 && algorithm < 7) cond = false;
		}
		scanner.close();
		
		// Assigning the puzzle difficulty
		if (difficulty == 1) puzzle = easy;
		else if (difficulty == 2) puzzle = medium;
		else puzzle = hard;
		
		
		// Create a queue of values, pass the queue, puzzle,
		// Calling the correct algorithm
		if (algorithm == 1) BFS(puzzle);
		else if (algorithm == 2) DFS(puzzle);
		else if (algorithm == 3) uniformCost(puzzle);
		else if (algorithm == 4) bestFirst(puzzle);
		else if (algorithm == 5) AStar(puzzle, 1);
		else AStar(puzzle, 2);
	}
	
	// Separate function to check if current state is equal to the goal state
	public static boolean checkGoalState(String state) {
		if (state.equals(GoalState)) return true;
		return false;
	}
	
	// Function that will find the child states of the current state
	public static ArrayList<AINode> getChildStates(AINode state) {
		// Creating an ArrayList of AI Nodes to return
		ArrayList<AINode> children = new ArrayList<AINode>();
		
		// Current game state
		String currentGameState = state.getGameState();
		
		// Creating char array to make moves easier
		char[] c = currentGameState.toCharArray();
		char[] tempU = new char[9];
		char[] tempD = new char[9];
		char[] tempR = new char[9];
		char[] tempL = new char[9];;
		
		System.arraycopy(c,  0, tempU, 0, 9);
		System.arraycopy(c,  0, tempD, 0, 9);
		System.arraycopy(c,  0, tempL, 0, 9);
		System.arraycopy(c,  0, tempR, 0, 9);
		
		// Finding the index of the empty space
		int i = currentGameState.indexOf('0');
		
		// Creating temp variables to create new game states
		String newGameState;
		char temp;
		String tempCharToInt;
		AINode newChild;
		int depth = state.getDepth();

		// UP
		if (!(i == 0 || i == 1 || i == 2)) {
			tempCharToInt = Character.toString(tempU[i-3]);
			
			temp = tempU[i];
			tempU[i] = tempU[i-3];
			tempU[i-3] = temp;
			
			newGameState = new String(tempU);
			newChild = new AINode(newGameState, state, new ArrayList<AINode>(), depth, Integer.parseInt(tempCharToInt), "UP", false, 0, 0, 0);
			children.add(newChild);
		}
		
		// DOWN
		if (!(i == 6 || i == 7 || i == 8)) {
			tempCharToInt = Character.toString(tempD[i+3]);

			temp = tempD[i];
			tempD[i] = tempD[i+3];
			tempD[i+3] = temp;
			
			newGameState = new String(tempD);
			newChild = new AINode(newGameState, state, new ArrayList<AINode>(), depth, Integer.parseInt(tempCharToInt), "DOWN", false, 0, 0, 0);
			children.add(newChild);
		}
		
		// LEFT
		if (!(i == 0 || i == 3 || i == 6)) {
			tempCharToInt = Character.toString(tempL[i-1]);
			
			temp = tempL[i];
			tempL[i] = tempL[i-1];
			tempL[i-1] = temp;
			
			newGameState = new String(tempL);
			newChild = new AINode(newGameState, state, new ArrayList<AINode>(), depth, Integer.parseInt(tempCharToInt), "LEFT", false, 0, 0, 0);
			children.add(newChild);
		}
		
		// RIGHT
		if (!(i == 2 || i == 5 || i == 8)) {
			tempCharToInt = Character.toString(tempR[i+1]);

			temp = tempR[i];
			tempR[i] = tempR[i+1];
			tempR[i+1] = temp;
			
			newGameState = new String(tempR);
			newChild = new AINode(newGameState, state, new ArrayList<AINode>(), depth, Integer.parseInt(tempCharToInt), "RIGHT", false, 0, 0, 0);
			children.add(newChild);
		}
		return children;
	}

	// To track how long the solution is
	public static int length(AINode child) {
		int count = 0;
		AINode temp;
		
		while (child.getParent() != null) {
			count++;
			temp = child.getParent();
			child = temp;
		}
		return count;
	}
	
	// To track the cost of the solution
	public static int cost(AINode child) {
		int totalCost = 0;
		AINode temp;
		
		while (child.getParent() != null) {
			totalCost += child.getPathCost();
			temp = child.getParent();
			child = temp;
		}
		return totalCost;
	}
	
	// Creating function to calculate amount of tiles out of place
	public static int misplacedTiles(String state) {
		char[] stateChar = state.toCharArray();
		char[] goalChar = GoalState.toCharArray();
		int count = 0;
		for (int i = 0; i < 9; i++) {
			if (stateChar[i] != goalChar[i]) count++;
		}
		return count;
	}
	
	// Creating recursive function to print solution
	public static void printSolution(AINode child) {
		if (child.getParent() != null) printSolution(child.getParent());
		System.out.println("Game State: " + child.getGameState() + " | Cost: " + child.getPathCost()  + " | Move: " + child.getActionTaken() + "\t | Total Cost: " + cost(child));
	}
	

	// Creating function to calculate Manhattan distance total
	public static int manhattanDistance(String state) {
		char[] stateChar = state.toCharArray();
		char[] goalChar = GoalState.toCharArray();
		
		int distance = 0;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (stateChar[i] == goalChar[j]) distance += (Math.abs((j%3)-(i%3)) + Math.abs((j/3)-(i/3)));
			}
		}
		return distance;
	}
	
	// Breadth First Search Algorithm, use a QUEUE for first in, first out
	public static void BFS (String puzzle) {
		int maxQueue = 0;
		
		// Creating a visited states field
		Set<String> visitedStates = new HashSet<String>();
		
		// Create the queue
		Queue<AINode> queue = new LinkedList<AINode>();
		
		// Create first node
		AINode first = new AINode(puzzle, null, new ArrayList<AINode>(), 0, 0, "ROOT", false, 0, 0, 0);
		
		// push first node onto queue
		queue.add(first);
		
		// creating condition to control while loop, is goal state = current game state
		boolean isGoalState = checkGoalState(first.getGameState());
		int iterations = 0;
		
		AINode current = first;
		
		while (!isGoalState) {
			iterations++;
			
			// Removing node from the queue
			current = queue.remove();
			visitedStates.add(current.getGameState());
			
			// Checking if current state is the goal state. If so, break.
			isGoalState = checkGoalState(current.getGameState());
			if (isGoalState) break;
			
			//System.out.println("\nEvaluating this node: " + current.getGameState());
			
			if (!current.getVisited()) {
				current.setVisited(true);
				ArrayList<AINode> children = getChildStates(current);

				for (AINode child: children) {
					if (visitedStates.contains(child.getGameState())) continue;
					//System.out.println("Potential next state: " + child.getGameState());
					child.setF(child.getPathCost() + child.getParent().getF());
					current.addChild(child);
					queue.add(child);
				}
				//System.out.println(current.getActionTaken() + ", Cost: " + current.getPathCost());
			}
			if (queue.size() > maxQueue) maxQueue = queue.size();
		}
		printSolution(current);
		System.out.println("\nSolved!"
				+ "\nGame State: " + current.getGameState()
				+ "\nLength: " + length(current)
				+ "\nCost: " + cost(current)
				+ "\nTime: " + iterations
				+ "\nSpace: " + maxQueue);
	}
	
	// Depth First Search Algorithm, use a STACK for first in, last out
	public static void DFS (String puzzle) {
		
		int maxStack = 0;
		
		// Creating a visited states field
		Set<String> visitedStates = new HashSet<String>();
		
		// Create the stack
		Stack<AINode> stack = new Stack<AINode>();
		
		// Create first node
		AINode first = new AINode(puzzle, null, new ArrayList<AINode>(), 0, 0, "ROOT", false, 0, 0, 0);
				
		// push first node onto queue
		stack.push(first);
				
		// creating condition to control while loop, is goal state = current game state
		boolean isGoalState = checkGoalState(first.getGameState());
		int iterations = 0;
				
		// Create current node holder
		AINode current = first;
		
		// Creating the while loop. Since I not doing it recursively and am using a stack
		while (!isGoalState) {
			iterations++;
			current = stack.pop();
			visitedStates.add(current.getGameState());
			
			// Checking if current state is the goal state. If so, break.
			isGoalState = checkGoalState(current.getGameState());
			if (isGoalState) break;
						
			//System.out.println("\nEvaluating this node: " + current.getGameState());
						
			if (!current.getVisited()) {
				current.setVisited(true);
				ArrayList<AINode> children = getChildStates(current);

				// visiting all the children and adding to the stack
				for (AINode child: children) {
					if (visitedStates.contains(child.getGameState())) continue;
					//System.out.println("Potential next state: " + child.getGameState());
					child.setF(child.getPathCost() + child.getParent().getF());
					current.addChild(child);
					stack.push(child);
				}
				//System.out.println(current.getActionTaken() + ", Cost: " + current.getPathCost());
			}
			if (stack.size() > maxStack) maxStack = stack.size();
		}
		//printSolution(current);
		System.out.println("\nSolved!"
				+ "\nGame State: " + current.getGameState()
				+ "\nLength: " + length(current)
				+ "\nCost: " + cost(current)
				+ "\nTime: " + iterations
				+ "\nSpace: " + maxStack);

	}
	
	// Assessing g = the distance from the start to N
	public static void uniformCost (String puzzle) {
		
		int maxPQueue = 0;
		
		// Creating a visited states field
		Set<String> visitedStates = new HashSet<String>();
		
		// Create first node
		AINode first = new AINode(puzzle, null, new ArrayList<AINode>(), 0, 0, "ROOT", false, 0, 0, 0);
		
		PriorityQueue<AINode> pqueue = new PriorityQueue<AINode>(9, new Comparator<AINode> () {
			public int compare (AINode a, AINode b) {
				if (a.getG() > b.getG()) return 1;
				else if (a.getG() < b.getG()) return -1;
				return 0;
			}
		});

		AINode current = first;
		boolean isGoalState = checkGoalState(first.getGameState());
		int iterations = 0;
		
		while (!isGoalState) {
			iterations++;
			visitedStates.add(current.getGameState());
			
			// Checking if current state is the goal state. If so, break.
			isGoalState = checkGoalState(current.getGameState());
			if (isGoalState) break;
			
			//System.out.println("\nEvaluating this node: " + current.getGameState());
			
			if (!current.getVisited()) {
				current.setVisited(true);
				ArrayList<AINode> children = getChildStates(current);

				// visiting all the children
				for (AINode child: children) {
					if (visitedStates.contains(child.getGameState())) continue;
					current.addChild(child);
					child.setG(child.getPathCost() + child.getParent().getG());
					pqueue.add(child);
				}
				//System.out.println(current.getActionTaken() + ", Cost: " + current.getPathCost() + ", Total Cost: " + current.getG());
			}
			if (pqueue.size() > maxPQueue) maxPQueue = pqueue.size();
			current = pqueue.remove();
		}
		printSolution(current);
		System.out.println("\nSolved!"
				+ "\nGame State: " + current.getGameState()
				+ "\nLength: " + length(current)
				+ "\nCost: " + cost(current)
				+ "\nTime: " + iterations
				+ "\nSpace: " + maxPQueue);
	}
	
	// Assessing h = # of tiles that are not in position
	public static void bestFirst (String puzzle) {
		
		int maxPQueue = 0;
		
		// Creating a visited states field
		Set<String> visitedStates = new HashSet<String>();
		
		// Create first node
		AINode first = new AINode(puzzle, null, new ArrayList<AINode>(), 0, 0, "ROOT", false, 0, 0, 0);
		
		PriorityQueue<AINode> pqueue = new PriorityQueue<AINode>(9, new Comparator<AINode> () {
			public int compare (AINode a, AINode b) {
				if (a.getF() > b.getF()) return 1;
				else if (a.getF() < b.getF()) return -1;
				return 0;
			}
		});

		AINode current = first;
		boolean isGoalState = checkGoalState(first.getGameState());
		int iterations = 0;
		
		while (!isGoalState) {
			iterations++;
			visitedStates.add(current.getGameState());
			
			// Checking if current state is the goal state. If so, break.
			isGoalState = checkGoalState(current.getGameState());
			if (isGoalState) break;
			
			
			//System.out.println("\nEvaluating this node: " + current.getGameState());
			
			// Visiting the node
			if (!current.getVisited()) {
				current.setVisited(true);
				ArrayList<AINode> children = getChildStates(current);

				// visiting all the children, 
				for (AINode child: children) {
					if (visitedStates.contains(child.getGameState())) continue;
					current.addChild(child);
					
					// Calculating costs
					child.setG(child.getPathCost() + child.getParent().getG());
					child.setH(misplacedTiles(child.getGameState()));
					child.setF(child.getG() + child.getH());
					
					pqueue.add(child);
				}
				//System.out.println(current.getActionTaken() + ", Cost: " + current.getPathCost() + ", Total Cost: " + current.getF());
			}
			if (pqueue.size() > maxPQueue) maxPQueue = pqueue.size();
			current = pqueue.remove();
		}
		printSolution(current);
		System.out.println("\nSolved!"
				+ "\nGame State: " + current.getGameState()
				+ "\nLength: " + length(current)
				+ "\nCost: " + cost(current)
				+ "\nTime: " + iterations
				+ "\nSpace: " + maxPQueue);
	}
	
	// The function for both A*1 and A*2 as it is the same function, but different heuristic
	public static void AStar (String puzzle, int heuristic) {
		
		int maxPQueue = 0;
		// Creating a visited states field
		Set<String> visitedStates = new HashSet<String>();
		
		// Create first node
		AINode first = new AINode(puzzle, null, new ArrayList<AINode>(), 0, 0, "ROOT", false, 0, 0, 0);
		
		PriorityQueue<AINode> pqueue = new PriorityQueue<AINode>(9, new Comparator<AINode> () {
			public int compare (AINode a, AINode b) {
				if (a.getF() > b.getF()) return 1;
				else if (a.getF() < b.getF()) return -1;
				return 0;
			}
		});

		AINode current = first;
		pqueue.add(first);
		boolean isGoalState = checkGoalState(first.getGameState());
		int iterations = 0;
		
		while (!isGoalState) {
			iterations++;
			visitedStates.add(current.getGameState());
			
			// Checking if current state is the goal state. If so, break.
			isGoalState = checkGoalState(current.getGameState());
			if (isGoalState) break;
			
			current = pqueue.remove();
			
			//System.out.println("\nEvaluating this node: " + current.getGameState());
			
			// Visiting the node
			if (!current.getVisited()) {
				current.setVisited(true);
				ArrayList<AINode> children = getChildStates(current);

				// visiting all the children, 
				for (AINode child: children) {
					if (visitedStates.contains(child.getGameState())) continue;
					current.addChild(child);
					
					// The total cost is g (path total so far (from parent) + child path cost) + h (# of misplaced tiles)
					child.setG(child.getPathCost() + child.getParent().getG());
					
					// changing the heuristic, calls to the specific function to calculate values
					if (heuristic == 1) child.setH(misplacedTiles(child.getGameState()));
					else if (heuristic == 2) child.setH(manhattanDistance(child.getGameState()));
					
					// setting the F value of the child equal to the sum of G & H
					child.setF(child.getG() + child.getH());
					
					pqueue.add(child);
				}
				//System.out.println(current.getActionTaken() + ", Cost: " + current.getPathCost() + ", Total Cost: " + current.getF());
			}
			if (pqueue.size() > maxPQueue) maxPQueue = pqueue.size();
		}
		printSolution(current);
		System.out.println("\nSolved!"
				+ "\nGame State: " + current.getGameState()
				+ "\nLength: " + length(current)
				+ "\nCost: " + cost(current)
				+ "\nTime: " + iterations
				+ "\nSpace: " + maxPQueue);
	}
	
	
	
}