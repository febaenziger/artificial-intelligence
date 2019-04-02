import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;

public class babyNames {

	// Variable Set Up
	static double[][][][] p = new double[27][27][27][3];
	static String[] boy = new String[1000];
	static String[] girl = new String[1000];
	static String[] names;
	static int gender;
	static int min_name;
	static int max_name;
	static int order = 2;
	static int num_names;
	static Scanner scanner = new Scanner(System.in);
	
	// the main loop that controls user input and calls the functions
	public static void main(String[] args) throws FileNotFoundException {
		
		System.out.println("Welcome to the Baby Name Generator!");
		
		// Reading Boy names into ArrayList boy
		int i = 0;
		Scanner boy_scanner = new Scanner(new File("namesBoys.txt"));
		while (boy_scanner.hasNextLine()){
		    boy[i] = "__" + boy_scanner.nextLine().toLowerCase() + "__";
		    i++;
		}
		boy_scanner.close();
		
		// Reading Girl names into ArrayList girl
		i = 0;
		Scanner girl_scanner = new Scanner(new File("namesGirls.txt"));
		while (girl_scanner.hasNextLine()){
		    girl[i] = "__" + girl_scanner.nextLine().toLowerCase() + "__";
		    i++;
		}
		girl_scanner.close();
		
		// Printing out the names - change girl or boy to see each respective list
//		for (String name : girl) {
//			System.out.println(name);
//		}
		
		// Running a while loop to get user input for gender
		boolean cond = true;
		while (cond) {
			System.out.println("\nWould you like a girl (1) or boy (2) name? Enter selection number: ");
				gender = scanner.nextInt();
				if (gender == 1 || gender == 2) cond = false;
				else System.out.println("\nNot a valid selection. Please try again.");
			}

			System.out.println("\n---------------------------------------\n");
			
		// Running a while loop to get user input for min letters in name
		cond = true;
		while (cond) {
			System.out.println("Please enter minimum length for the name: ");
			min_name = scanner.nextInt();
			if (min_name > 0 && min_name < 20) cond = false;
		}
		
		System.out.println("\n---------------------------------------\n");
		
		// Running a while loop to get user input for max letters in name
		cond = true;
		while (cond) {
			System.out.println("Please enter maximum length for the name: ");
			max_name = scanner.nextInt();
			if (max_name > 2 && max_name < 20 && min_name < max_name) cond = false;
		}
		
//		System.out.println("\n---------------------------------------\n");
//		
		// Running a while loop to get user input for order of the algorithm
//		cond = true;
//		while (cond) {
//			System.out.println("Please enter order for the algorithm: ");
//			order = scanner.nextInt();
//			if (min_name > 0 && min_name < 20) cond = false;
//		}
		
		System.out.println("\n---------------------------------------\n");
		
		// Running a while loop to get user input for min letters in name
		cond = true;
		while (cond) {
			System.out.println("Please enter your desired number of names: ");
			num_names = scanner.nextInt();
			if (num_names > 0 && num_names < 10) cond = false;
		}
		
		names = new String[num_names];
		
		setUpProbabilitiesList();
		if (gender == 1) probabilities(girl);
		else probabilities(boy);
//		printProbabilities();
		
		// Calling the functions to create the names & printing them out
		names = createNamesMain();
		System.out.println("\nCongratulations! The created names are: ");
		for (String name : names) {
			System.out.println(name.substring(0, 1).toUpperCase()+name.substring(1).toLowerCase());
		}
	}
	
	private static void setUpProbabilitiesList() {
		for (int i = 0; i < 27; i++) {
			for (int j = 0; j < 27; j++) {
				for (int k = 0; k < 27; k++) {
					for (int l = 0; k < 3; k++) {
						p[i][j][k][l] = 0;
					}
				}
			}
		}
	}
	
	private static void probabilities(String[] names) {
		// a temp array to count how many times a letter occurs
		int[][] temp_num = new int[27][27];

		// A for loop to go through a name and create the probability of certain things occuring
		for (String name : names) {
			char[] temp_name = name.toCharArray();
			for (int i = 0; i < temp_name.length-1; i++) {
				if (i == 0) continue;
				if (temp_name[i] == '_' && temp_name[i+1] == '_') continue;
				
				// Getting the numeric values of things to then put into the array
				int f_letter = (temp_name[i-1] != '_') ? Character.getNumericValue(temp_name[i-1])-10 : 26 ;
				int s_letter = (temp_name[i] != '_') ? Character.getNumericValue(temp_name[i])-10 : 26;
				int t_letter = (temp_name[i+1] != '_') ? Character.getNumericValue(temp_name[i+1])-10 : 26;
				
				// Updating the number of occurrences and thus the probability of something happening
				temp_num[f_letter][s_letter] = (temp_num[f_letter][s_letter] != 0) ? temp_num[f_letter][s_letter] + 1 : 1;
				
				p[f_letter][s_letter][t_letter][0] = t_letter;
//				System.out.print(t_letter + " : ");
//				System.out.print(Character.forDigit(t_letter+10, 36));
//				System.out.println();
				p[f_letter][s_letter][t_letter][1] += 1;
				p[f_letter][s_letter][t_letter][2] = p[f_letter][s_letter][t_letter][1]/temp_num[f_letter][s_letter];
			}
		}
		
		// Sorting the lists
		for (int f = 0; f < 27; f++) {
			for (int s = 0; s < 27; s++) {
				double[][] temp_u = p[f][s];
				Arrays.sort(temp_u, new Comparator<double[]>(){
					@Override
				    public int compare(double[] a, double[] b) {
				        return Double.compare(b[2], a[2]);
				    }
				});
				p[f][s] = temp_u;
			}
		}
		//printArray();
		//System.out.println("Probabilities are done...");
	}
	
	private static void printArray() {
		for (int f = 0; f < 27; f++) {
			System.out.print(f + " ");
			for (int s = 0; s < 27; s++) {
				System.out.print(s + " ");
				for (int t = 0; t < 27; t++) {
					System.out.println("[0]: " + p[f][s][t][0] + " [1]: " + p[f][s][t][1] + " [2]: " + p[f][s][t][2]);
				}
			}
		}
	}

	
	private static boolean nameExists(String name) {
		for (int k = 0; k < names.length; k++) {
			if (names[k] == name) return true;
		}
		for (int j = 0; j < 1000; j++) {
			if (gender == 1) if (girl[j] == name) return true;
			else if (gender == 2) if (boy[j] == name) return true;
		}
		return false;
	}
	
	private static String[] createNamesMain() {
		String temp_name;
		int temp_num_names = 0;
		String[] temp_names = new String[num_names];
		int index = 0;
		
		while (temp_num_names < num_names) {
			temp_name = createName();
			
			// Checking that the name exists AND it's not in the list already
			if (temp_name == "") continue;
			if (nameExists(temp_name)) continue;
			
			//System.out.println(temp_name);
			temp_names[index] = temp_name;
			index++;
			temp_num_names++;
		}
		return temp_names;
	}
	
	private static String createName() {

		String create_a_name = "";
		int n = 0;
		int fn = 26;
		int sn = 26;
		int tn;
		boolean cond = true;
		
		while (cond) {		
			
			// Finding a random index using weighted probabilities
			double tw = 0.0d;
			for (int cw1 = 0; cw1 < 27; cw1++) {
				tw += p[fn][sn][cw1][2];
			}

			int rand_index = -1;
			double r = Math.random() * tw;
			for (int wn = 0; wn < 27; ++wn) {
				r -= p[fn][sn][wn][2];
				if (r <= 0.0d) {
					rand_index = wn;
					break;
				}
			}
			tn = (sn == 26 && n != 0) ? 26 : (int) p[fn][sn][rand_index][0];
			
			create_a_name += Character.forDigit(tn+10, 36);
						
			fn = sn;
			sn = tn;
			
			// if both are '_' then condition = false
			if (sn == 26 && tn == 26) cond = false;
		}
		
		
		if (min_name < create_a_name.length() && create_a_name.length() < max_name) return create_a_name;
		else return "";
	}
	
//	private static void printProbabilities() {
//		for (int i = 0; i < 27; i++) {
//			for (int j = 0; j < 27; j++) {
//				System.out.print("\np[][][]: " + p[i][j][0] + " " + p[i][j][1]);
//			}
//			System.out.println();
//		}
//	}

}
