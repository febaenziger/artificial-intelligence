import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class dotsAndBoxes {
	
	// Board Set-Up
	static int[][] board;
	static int[][] complete_boxes; // 0 to indicate none, 1 to indicate player, 2 to indicate computer
	static int board_size; // how large the character array needs to be
	static int max_depth;
	static Scanner scanner = new Scanner(System.in);
	
	// Game States
	static int player_score = 0;
	static int computer_score = 0;
	
	// Score Stats
	static int user;
	static int computer;
	
	// the main loop that controls user input and calls the functions
	public static void main(String args[]) {
		
		int size = 0; // user input of size
		
		System.out.println("Welcome to Dots and Boxes!");
		
		// Running a while loop to get user input for difficulty level
		boolean cond = true;
		while (cond) {
		System.out.println("\nPlease choose puzzle size (2 - 20): ");
			size = scanner.nextInt();
			if (size > 0 && size < 21) cond = false;
			else System.out.println("\nNot a valid number. Please try again.");
		}
		
		board_size = (size*2) + 1;
		
		System.out.println("\n---------------------------------------\n");
		
		// Running a while loop to get user input for what algorithm
		cond = true;
		while (cond) {
			System.out.println("Please enter depth of AI algorithm (1 - 10): ");
			max_depth = scanner.nextInt();
			if (max_depth > 0 && max_depth < 11) cond = false;
		}
		
		// Calling function to create the array
		board = createBoard();
		dbNode root = new dbNode(null, board, new ArrayList<dbNode>(), 0, null, 1);
		
		// Calling the algorithm with the user inputs as arguments

		while (!gameOver()) {
			int depth = 0;
			playerMove();
			dbNode computerMove = min(root, depth);
			placeLine(computerMove.getBestXY()[1], computerMove.getBestXY()[0]);
			computer_score += completedBoxes(computerMove.getBestXY()[1], computerMove.getBestXY()[0], 2);
		}
		
		// THE FINAL STAGE
		int w = winner();
		score();
		System.out.println("\n---------------------------------------\n");
		System.out.println("\nFINAL SCORE: \nPlayer: " + player_score + "\nComputer Score: " + computer_score);
		if (w > 0) System.out.println("\nCongratulations! You won.");
		else if (w < 0) System.out.println("\nBoo! You lost. Better luck next time.");
		else System.out.println("\nIt's a draw...");
	}
	
	// the function to create the board
	public static int[][] createBoard() {
		// Creating the random number generator
		Random random = new Random();
		
		// Creating the board(s)
		int[][] board = new int[board_size][board_size];
		int[][] temp_complete_boxes = new int[board_size][board_size];
		
		// Looping through and creating the board
		for (int i = 0; i < board_size; i++) {
			for (int j = 0; j < board_size; j++) {
				// initializing the board to distinguish who won each box
				temp_complete_boxes[i][j] = 0;
				
				// The value at board[i][j] is either getting assigned to a * or a random number
				if (i%2 == 0) board[i][j] = (j%2 == 0) ? 0 : 9;
				else board[i][j] = (j%2 == 0) ? 9 : random.nextInt(4) + 1;
			}
		}
		complete_boxes = temp_complete_boxes;
		return board;
	}
	
	// the function to print the board
	public static void printBoard() {
		System.out.println();
		for (int i = 0; i < board_size; i++) {
			for (int j = 0; j < board_size; j++) {
				if (board[i][j] == 0) System.out.print("*");
				else if (board[i][j] == 7) System.out.print("|");
				else if (board[i][j] == 8) System.out.print("-");
				else if (board[i][j] == 9) System.out.print(" ");
				else System.out.print(board[i][j]);
				System.out.print(" ");
			}
			System.out.println();
		}
	}
	
	// Check to see if it's game over
	public static boolean gameOver() {
		for (int i = 0; i < board_size; i++) {
			for (int j = 0; j < board_size; j++) {
				if (board[i][j] == 9) {
					return false;
				}
			}
		} 
		return true;
	}
	
	// returns integer representing winner of game
	public static int winner() {
		// returns -1 if computer wins and +1 if player wins and 0 if it's a draw
		if (computer_score - player_score == 0) return 0;
		return (computer_score - player_score > 0) ? -1 : 1;
	}
	
	// function that will place the line
	public static void placeLine(int x, int y) {
		if (x%2 == 1 && y%2 == 0) board[x][y] = 7;
		else if (x%2 == 0 && y%2 == 1) board[x][y] = 8;
	}
	
	// function that will unplace the line
	public static void unplaceLine(int x, int y) {
		board[x][y] = 9;
	}
	
	// a function that returns potential boxes
	public static ArrayList<int[]> potentialBoxes (int x, int y) {
		ArrayList<int[]> potentialBoxes = new ArrayList<int[]>();
		
		boolean cond = true;
		int[] box1 = new int[2];
		int[] box2 = new int[2];
		
		// if the move is |
		if (x%2 == 0) {  
			if (x != board_size - 1) { box1[0] = x + 1; box1[1] = y; potentialBoxes.add(box1); }
			if (x != 0) { box2[0] = x - 1; box2[1] = y; potentialBoxes.add(box2); }
		}
		
		// if the move is -
		if (y%2 == 0) {
			if (y != 0) { box2[0] = x; box2[1] = y - 1; potentialBoxes.add(box1); } 
			if (y != board_size - 1) { box1[0] = x; box1[1] = y + 1; potentialBoxes.add(box2); }
		}
		return potentialBoxes;
	}
	
	// a function that returns score of completed boxes
	public static int completedBoxes(int x, int y, int player) {	
		int level_score = 0;
		
		// initializing arrays to check for completed boxes
		ArrayList<int[]> potentialBoxes = potentialBoxes(y, x);
		
		// Searching through potential boxes and seeing if they have been completed
		for (int[] box : potentialBoxes) {
			int box_x = box[0]; int box_y = box[1];
			if (box_y > 0) if (board[box_x][box_y - 1] == 9) continue; // UP
			if (box_y < board_size-1) if (board[box_x][box_y + 1] == 9) continue; // DOWN
			if (box_x < board_size-1) if (board[box_x + 1][box_y] == 9) continue; // RIGHT
			if (box_x > 0) if (board[box_x - 1][box_y] == 9) continue; // LEFT
		
			level_score += board[box_x][box_y];
		}
		return level_score;
	}
	
	// function for the player move
	public static void playerMove() {
		printBoard();
		System.out.println("\nYour turn - Choose wisely. [COLUMN #, ENTER, ROW # ENTER]:");
		
		// creating condition for while loop to ensure valid user input
		boolean cond = true;
		while (cond) {
			int col = 0;
			int row = 0;
			
			col = scanner.nextInt();
			row = scanner.nextInt();
		
			// is the board open? if so: place line.
			if (board[row][col] == 9) {
				placeLine(row, col);
				player_score += completedBoxes(row, col, 1);
				cond = false;
				
			}
			else System.out.println("Not a valid selection. Try again.");
		}
		System.out.println();
	}
	
	// the function that is going to evaluate leaves of the tree
	public static int score() {
		user = 0;
		computer = 0;
		
		for (int i = 0; i < board_size; i++) {
			for (int j = 0; j < board_size; j++) {
				if (complete_boxes[i][j] == 2) user += board[i][j];
				else if (complete_boxes[i][j] == 1) computer += board[i][j];
			}
		}
		return computer - user;
	}
	
	public static ArrayList<dbNode> getChildStates(dbNode current, int player) {
		ArrayList<dbNode> children = new ArrayList<dbNode>();
		int[][] current_board = current.getBoard();
		for (int i = 0; i < board_size; i++) {
			for (int j = 0; j < board_size; j++) {
		
				// if the space is open, create a new child node of that move
				if (current_board[j][i] == 9) {
					placeLine(j, i);
					int score = completedBoxes(j, i, player);
					int[] move = {i, j};
					dbNode new_child = new dbNode(current, board, new ArrayList<dbNode>(), score(), move, player);
					children.add(new_child);
					unplaceLine(j, i);
				}
			}
		}
		return children;
	}

	// Return highest score from max player
	public static dbNode max(dbNode current, int depth) {
		if (gameOver()) return current;
		else if (depth == max_depth) return current;
		int max_score = 0;
		int best_score = -9999;
		dbNode max_move; dbNode best_move = current;
		
		ArrayList<dbNode> children = getChildStates(current, 2);
		current.setChildren(children);
		for (dbNode child: children) {
			max_move = min(child, depth+1);
			max_score = max_move.getCost();
			if (max_score > best_score) { best_score = max_score; best_move = max_move; }
		}
		return best_move;
	}
	
	// Returns lowest score from min player
	public static dbNode min(dbNode current, int depth) {
		if (gameOver()) return current;
		else if (depth == max_depth) return current; // eval?
		int min_score = 0;
		int best_score = 9999;
		dbNode min_move; dbNode best_move = current;
	 		
	 	ArrayList<dbNode> children = getChildStates(current, 1);
		current.setChildren(children);
		for (dbNode child: children) {
			min_move = max(child, depth+1);
			min_score = min_move.getCost();
			if (min_score < best_score) { best_score = min_score; best_move = min_move; }
		} 
		return best_move;
	}
}

