import java.util.Scanner;

/*This class is used to implement Role Based Access control implementation */

public class RBAC{
	
/*The same integers that were used in ACL class, numbers are translated into binary
4 = 100, 2 = 010 and 1 = 001
*/
	
	public static final int READ_FLAG = 4; 			
	public static final int WRITE_FLAG = 2;				
	public static final int EXECUTE_FLAG = 1;
	
	static byte[][][] rbac = new byte[3][5][6];;    /*3D array used where first part is the role, second is the user and
							third is the file chosen */
	
	
	

	//Assign the roles, where the user number is inputed to assign it a role with its specific access rights
	public static int assignRoles(int user_number){
		int user = user_number + 1;
		System.out.println("What role should user " + user + " be assigned too?\n 1) Sales \n 2) Technical Staff \n 3) Manager\n");
		Scanner scn = new Scanner(System.in);
		int role = scn.nextInt(); 
		if(role == 1)
			sales(user_number);
		if(role == 2)
			technicalStaff(user_number);
		if(role == 3)
			manager(user_number);
		
		return (role - 1);
	}
	
	////////////////////////////////////////
	
	
	//create the roles
	
	/*sets the roles of the user with the access rights each role provides, 
	ex sales(user1) would have the user 1 be set with READ_FLAG which means it can only read the files 	
	*/
	public static void sales (int user){
		for(int i = 0; i < 6; i++){
			rbac[0][user][i] = READ_FLAG;
		}

	}
	
	
	//sets users with technical staff access rights
	
	public static void technicalStaff (int user){
		for(int j = 0; j < 6; j++){
			rbac[1][user][j] = READ_FLAG | EXECUTE_FLAG;
		}

	}
	//sets user with manager access rights
	
	public static void manager (int user){
		if(checkManager() == false){
			for(int j = 0; j < 6; j++)
			rbac[2][user][j] = READ_FLAG | WRITE_FLAG | EXECUTE_FLAG;
		}
		else
		System.out.println("There is already a manager assgined.");

	}
	
	//checks whether a manager is already in the matrix, if so user cannot become manager
	public static boolean checkManager(){
		for(int i = 0; i < 5; i++){
			for(int j = 0; j < 6; j++){
				if(rbac[2][i][j] == 0){
					return false;
				}
			}
		}
		return true;
	
	}
	
	
	//checks to see whether the access request user asks for is available to them depending on there role	
	public static boolean canWrite(int right){
		return (right & WRITE_FLAG) == WRITE_FLAG;
	}
	
	public static boolean canRead(int right){
		return (right & READ_FLAG) == READ_FLAG;
	}
	
	public static boolean canExecute(int right){
		return (right & EXECUTE_FLAG) == EXECUTE_FLAG;
	}
	
	
	
//returns the rights of the user depending on role	
    public int getRight(int role, int user_number, int file_number) {
        return rbac[role][user_number][file_number];
    }


}
