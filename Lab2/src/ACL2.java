import java.util.Scanner;
import java.io.*;
/**
 * This class is an ACL with 5 user and 6 files only.
 * Can not be use with another size
 */
public class ACL2 {
    // 5 user with 6 files
	//This ACL, ALC2, will be used as one to save and read user rights
	//Same as ACL2 but can save the file

    static final int READ_FLAG = 4; //
    static final int WRITE_FLAG = 2;
    static final int EXECUTE_FLAG = 1;


    byte[][] acl2;

    {
        acl2 = new byte[5][6];
        acl2[0][0] = READ_FLAG | WRITE_FLAG | EXECUTE_FLAG; 
        acl2[0][1] = READ_FLAG | WRITE_FLAG | EXECUTE_FLAG; 
        acl2[0][2] = READ_FLAG | WRITE_FLAG | EXECUTE_FLAG; 
        acl2[0][3] = READ_FLAG | WRITE_FLAG | EXECUTE_FLAG; 
        acl2[0][4] = READ_FLAG | WRITE_FLAG | EXECUTE_FLAG;
        acl2[0][5] = READ_FLAG | WRITE_FLAG | EXECUTE_FLAG;

        acl2[1][0] = READ_FLAG | WRITE_FLAG;
        acl2[1][2] = READ_FLAG | EXECUTE_FLAG;
        acl2[1][4] = READ_FLAG;

        acl2[2][2] = READ_FLAG | WRITE_FLAG;
        acl2[2][3] = READ_FLAG | WRITE_FLAG;

        acl2[3][0] = READ_FLAG | EXECUTE_FLAG;
        acl2[3][3] = READ_FLAG;
        acl2[3][4] = READ_FLAG;

        acl2[4][1] = READ_FLAG | WRITE_FLAG | EXECUTE_FLAG;
        acl2[4][3] = READ_FLAG;
        acl2[4][5] = READ_FLAG;

    }
	
	


	//change the rights of the user based on what they input. first read the file
	public void changeRights(int user_number, int file_number) throws IOException{
		File dir = new File(".");
		File fin = new File(dir.getCanonicalPath() + File.separator + "res/ACL2.txt");

		
		FileInputStream fis = new FileInputStream(fin);
 
		//Construct BufferedReader from InputStreamReader
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
	 
		String line = null;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
	 
		br.close();
		
		String new_rights = new String();
		int i = 0;
	
	
		System.out.println("Enter your desired rights:\n r for read\t w for write\t x for execute");

		Scanner scanner = new Scanner(System.in);
		//set the rights
		new_rights += scanner.nextLine();
		acl2[user_number][file_number] = 0;
		for(i = 0; i < new_rights.length(); i++){
			if (new_rights.charAt(i) == 'r')
				acl2[user_number][file_number] += 4;
			if (new_rights.charAt(i) == 'w')
				acl2[user_number][file_number] += 2;
			if (new_rights.charAt(i) == 'x')
				acl2[user_number][file_number] += 1;
		
		
		}	
	
		//System.out.println(acl2[user_number][file_number]);
		//Write ALC2 to file
	
		FileOutputStream fout = new FileOutputStream(new File("ACL2.txt"));

		for (i = 0; i < 5; i++) {
			for (int j = 0; j < 6; j++) {
				// get the string value of your byte and print it out
				fout.write(String.valueOf(acl2[i][j]).getBytes());
			}
			// write out a new line. For different systems the character changes
			fout.write(System.getProperty("line.separator").getBytes());
		}
		// close the output stream
		fout.close();
		
	}

	
}
