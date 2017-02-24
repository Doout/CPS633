import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
* Main class to run program
*/
public class Main {


	final static int[] THR = {80, 66, 71, 73, 75};


	public static void main(String... arg) throws Exception {
		boolean choice = false, change_rights = true;
		int userID, fileID, change_userID, change_fileID, role;
		char request = 'x', edit;
		int byte_request=0, byte_access, system_choice;
		String s_role = new String();
		String rights = new String();
		HashMap<Character,String> access;
		access = new HashMap<>(3); //hashmap for easy translation of the users input
		access.put('r',"READ");
		access.put('w',"WRITE");
		access.put('x',"EXECUTE");
		ACL acl = new ACL();
		ACL2 ACL2 = new ACL2();
		RBAC rbac= new RBAC();
		ArrayList<User> users = new ArrayList<>(5);
		users.add(User.loadUser("res/User1.txt"));
		users.add(User.loadUser("res/User2.txt"));
		users.add(User.loadUser("res/User3.txt"));
		users.add(User.loadUser("res/User4.txt"));
		users.add(User.loadUser("res/User5.txt"));
		
		
		
		System.out.println("Enter your UserID and the file number you wish to access:\n"); //User enters id and file to be access
		Scanner sc = new Scanner(System.in);
		userID = sc.nextInt() - 1;
		fileID = sc.nextInt() - 1;
		int file = fileID + 1;
		
		User loginUser = users.remove(userID);
		
		//algorithm from lab files to authenticate user
		double FAR = User.calculateFAR(loginUser, 6, THR[userID]);
		double FRR = User.calculateFRR(loginUser, users.toArray(new User[users.size()]), 6, THR[userID]);


		if (FAR == FRR) {            //FAR and FRR must be equal to be authenticated meaning it is actually the user
			System.out.println("User verified\n");
			System.out.println("Access \n 1) ACL \n 2) RBAC \n");
			
			system_choice  = sc.nextInt();
			
			if(system_choice == 1){  //This choice means the user is using the ACL implementation
				
				while(choice == false){
					System.out.println("What is your request?\n r for read\t w for write\t x for execute");
					request = sc.next().charAt(0);   //checks the char that was inputed and equates to a number
					if (request == 'r'){
						byte_request = 4;
						choice = true;
					}
					else if (request == 'w'){
						byte_request = 2;
						choice = true;
					}
					else if (request == 'x'){
						byte_request = 1;
						choice = true;
					}
					else
					System.out.println("Not a valid choice.");
				}

/*byte request is a number that represents the char chosen and the method canREAD, canWrite and CanExecute contains the
binary number that means the user has that access privelege they request, otherwise they do not have that privilege
*/
				byte_access = byte_request & acl.getRight(userID, fileID); //

				if (byte_access == 0) {				
					byte_access = acl.getRight(userID, fileID); //get right returns the rights of file chosen
					rights = "";
					if (ACL.canRead(byte_access))
					rights += "Read-";
					if (ACL.canWrite(byte_access))
					rights += "Write-";
					if (ACL.canExecute(byte_access))
					rights += "Execute-";
					rights = rights.substring(0, rights.length() - 1);
					System.out.println("You only have " + rights + " access rights to the file " + file + ".\n");
					
				} 
				else {
					System.out.println("Your " + access.get(request) + " access request to the file " + file + " is granted");
				}
				while(change_rights == true){ //edits the rights and changes them within the matrix
					System.out.println("Would you like to change your rights?\n y for yes\t n for no\n");
					edit = sc.next().charAt(0);
					
					if(edit == 'y'){
						ACL2.changeRights(userID, fileID);
						System.out.println("Your rights have been changed.");
						break;
					}
					else if(edit == 'n')
					break;
					else{
						System.out.println("Not a valid choice.");
					}
				}
			}
			else if(system_choice == 2){ 	//user would like to access the RBAC implementation 
				System.out.println("Accessing RBAC...");
				role = RBAC.assignRoles(userID);  	//assigns the role to user
				
				if(role == 0)
					s_role = "sales";
				if(role == 1)
					s_role = "technical staff";
				if(role == 2)
					s_role = "manager";
				
				while(choice == false){
					System.out.println("What is your request?\n r for read\t w for write\t x for execute");
					request = sc.next().charAt(0);		//same as ACL choice
					if (request == 'r'){
						byte_request = 4;
						choice = true;
					}
					else if (request == 'w'){
						byte_request = 2;
						choice = true;
					}
					else if (request == 'x'){
						byte_request = 1;
						choice = true;
					}
					else
					System.out.println("Not a valid choice.");
				}
				
				byte_access = byte_request & rbac.getRight(role, userID, fileID);

				if (byte_access == 0) {			/*same as ACL choice, retrieves the users rights and check if
									the roles they are in have those privileges
									*/
					byte_access = rbac.getRight(role, userID, fileID);
					rights = "";
					if (RBAC.canRead(byte_access))
					rights += "Read-";
					if (RBAC.canWrite(byte_access))
					rights += "Write-";
					if (RBAC.canExecute(byte_access))
					rights += "Execute-";
					rights = rights.substring(0, rights.length() - 1);
					System.out.println("You only have " + rights + " access rights to the file " + file + ".\n");
					
				} 
				else {
					System.out.println("Your " + s_role + " access request to the file " + file + " is granted");
				}
				
			}
			else
			System.out.println("Invalid choice!");
		}
		
	}
}


