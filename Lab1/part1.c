//CPS633 Lab 1 
//Part 2

#include "database.h"
#include "OLMHash.h"
#include <regex.h>

#define MaxTry 5
void clear(void);


//get the username
void getUserID(char* id) {
getID: //keep on get a user ID if it is not vaild
     printf("Enter User ID:\n");
     scanf("%32s" , id);
     if (strlen(id) < 4) {
          printf("User ID much be 4 or more chars long\n");
          goto getID;
     }
}
//get the password

void getPassword(char* password) {
     char * regexString = "[^a-zA-Z0-9]+";
     regex_t regexCompiled;
     regcomp(&regexCompiled, regexString, REG_EXTENDED);
     regmatch_t match;
     goto readPass;
getPass: // skip this for now. Only do if the frist try is was invaild/non match
     printf("Enter password:\n");
readPass:
     scanf("%13s" , password); // read only 13 char
     if (strlen(password) == 13) { //If there is 13 char than it is over
          printf("Passwords can contain a maximum of 12 characters\n");
          clear(); // remove all char left in the buffer
          goto getPass; //retry
     }
     int r = regexec(&regexCompiled, password, 1, &match, 0);
     if (r == 0) { //check if all char in the password are only number/letter
          printf("Invalid entry Only letters and numbers\n");
          goto getPass;
     }
}


void clear (void) { // clear
     while ( getchar() != '\n' );
}


int main(int argc, char *argv[]) {
	
	//intilization of variables and database to be used

     Database_init("part1.txt", 32, 64); //32 char for id and 12 char for password
     char* id = (char*) malloc(sizeof(char) * 32);
     char* password = (char*) malloc(sizeof(char) * 64);
	
	//get the user ID 
     getUserID(id);
	
	//get the associated password with the userID
     char* pass = Database_getSecondCol(id);
	
	//Determine if the user is in the system or not
     if (pass == NULL) {
          printf("User not found, making new user\n");
          printf("Please enter a password\n");
          getPassword(password);
          char* hashpassword = OLMHash_hashPassword(password);
          Database_save(id, hashpassword);
          printf("User have been added\n");
          exit(0);
     } else {
		 //if the user is in the system ask for the password, you have MaxTry attempts at a successful login
          int try = 0;
          while (try++ < MaxTry){
               printf("Please enter your password\n");
               getPassword(password);
               char* hashpassword = OLMHash_hashPassword(password);
               if(strcmp(hashpassword,pass) == 0){
                    printf("Please enter a new password\n");
                    getPassword(password);
                    char* hashpassword = OLMHash_hashPassword(password);
                    Database_updateSecondCol(id,hashpassword);
                    exit(0);
               }
          }
          if(try > MaxTry){
               printf("Too many unsuccessful attempts\n");
          }
     }

     free(id);
     free(password);
	 
	 return (0);

}
