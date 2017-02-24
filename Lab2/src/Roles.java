/*	
	Class to define the roles and set up there values within the RBAC matrix
*/
public class Roles{	

	public static final int READ_FLAG = 4; //
    public static final int WRITE_FLAG = 2;
    public static final int EXECUTE_FLAG = 1;
	
	static byte[][][] rbac;
   static  {
        rbac = new byte[3][5][6]; //Role, UserID, FileID
		
	}
	

	//create the roles
	
	/*Creates Sales role which only has READING privileges through all the files */
	public static void sales (int user){
		for(int j = 0; j < 6; j++){	
			rbac[0][user][j] = READ_FLAG;		//takes user ID and sets all its indexes in the array to be READING
		}
	}
	
	//Creates Technical Staff which READ and EXECUTES through all the files
	public static void technicalStaff (int user){
		for(int j = 0; j < 6; j++){
			rbac[1][user][j] = READ_FLAG | EXECUTE_FLAG; //takes user ID and sets all its indexes in the array to READ and EXECUTE
		}

	}
	
	//Creates Manager with all access priveleges
	public static void manager (int user){
		if(checkManager() == true){  //checks to make sure maanger index has not be assigned
			for(int j = 0; j < 6; j++)
				rbac[2][user][j] = READ_FLAG | WRITE_FLAG | EXECUTE_FLAG;
		}
		else
			System.out.println("There is already a manager assgined.");

	}
	
	//checks to make sure manager slot is empyt
	public static boolean checkManager(){
		for(int i = 0; i < 5; i++){
			for(int j = 0; j < 6; j++){
				if(rbac[2][i][j] == 0)
					return false;
			
			}
		}
		return true;
	}
	
}
