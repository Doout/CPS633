/**
 *
* This class is an ACL with 5 user and 6 files only.
 * Can not be use with another size
 */
public class ACL {
    // 5 user with 6 files

    static final int READ_FLAG = 4; //The Read Instruction is 4 which in bit is equal to 100
    static final int WRITE_FLAG = 2; //The Write Instruction is 2 Which is equal to 010
    static final int EXECUTE_FLAG = 1; //Execute is equal to 001
	
/*
The reason we are using byte is because we can compare the users and commands with bitwise functions to check if the userId 
contains the necessary access privileges which will be shown in the main class
*/
	
    byte[][] acl;

    {
        acl = new byte[5][6];
        acl[0][0] = READ_FLAG | WRITE_FLAG | EXECUTE_FLAG; 	//This is User 1 File 1
        acl[0][1] = READ_FLAG | WRITE_FLAG | EXECUTE_FLAG; 	//User 1 File 2  etc
        acl[0][2] = READ_FLAG | WRITE_FLAG | EXECUTE_FLAG; 
        acl[0][3] = READ_FLAG | WRITE_FLAG | EXECUTE_FLAG; 
        acl[0][4] = READ_FLAG | WRITE_FLAG | EXECUTE_FLAG;
        acl[0][5] = READ_FLAG | WRITE_FLAG | EXECUTE_FLAG;

        acl[1][0] = READ_FLAG | WRITE_FLAG;		//User 2 File 1			
        acl[1][2] = READ_FLAG | EXECUTE_FLAG;		//User 2 File 3			
        acl[1][4] = READ_FLAG;					
	    
	 //There is a gap between some files because the specific user might not have access  to the file 

        acl[2][2] = READ_FLAG | WRITE_FLAG;
        acl[2][3] = READ_FLAG | WRITE_FLAG;

        acl[3][0] = READ_FLAG | EXECUTE_FLAG;
        acl[3][3] = READ_FLAG;
        acl[3][4] = READ_FLAG;

        acl[4][1] = READ_FLAG | WRITE_FLAG | EXECUTE_FLAG;
        acl[4][3] = READ_FLAG;
        acl[4][5] = READ_FLAG;

    }

	
/* These are the boolean statements that check whether the specific user has the access rights
   to the file they chose
*/
    /*Users the bitwise and function to determine whether the
								int right (which is the chosen access request the user specifies)
								for example if the user presses r for Read, r is equated to 4 which is
								100, and it compares 100 with the file it is in to see if that specific
								user has the access rights its requesting. if the user has a bit acess
								of 101, then 100 & 101 = 100 which is equal to Read_Flag
								*/
	public static boolean canWrite(int right){
		return (right & WRITE_FLAG) == WRITE_FLAG;
		
	}
	
	public static boolean canRead(int right){
		return (right & READ_FLAG) == READ_FLAG;
	}
	
	public static boolean canExecute(int right){
		return (right & EXECUTE_FLAG) == EXECUTE_FLAG;
	}
	
	
/* returns the rights of that user and the file it is acessing 
*/	
    public int getRight(int user_number, int file_number) {
        return acl[user_number][file_number];
    }

	
}
