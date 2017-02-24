//CPS633 Lab 1 
//Part 2

#include "database.h"
#include "OLMHash.h"
#include <math.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <regex.h>
#include <time.h>
#define MaxTry 5


/*Function where the User, inputs a user ID into the program */

void getUserID(char* id)
{
getID:
    printf("Enter User ID:\n");
    scanf("%32s", id);
    if (strlen(id) < 4)
    {
        printf("User ID much be 4 or more chars long\n");
        goto getID;
    }
}

/* This is where the random 32 Bit Number is created, uses program internal clock
to make sure number is random everytime*/

int getRand()
{
    int r;
    srand((unsigned)time(NULL));
    return r = rand();
}

/*function to clear the data */
void clear (void) {
     while ( getchar() != '\n' );
}

/*Function for User to input password for there ID, function makes sure the input is only of letters
and numbers and that it is not null */

void getPassword(char* password)
{
    char * regexString = "[^a-zA-Z0-9]+";
    regex_t regexCompiled;
    regcomp(&regexCompiled, regexString, REG_EXTENDED);
    regmatch_t match;
    goto readPass;
getPass:
    printf("Enter password:\n");
readPass:
    scanf("%13s", password);
    if (strlen(password) == 13)
    {
        printf("Passwords can contain a maximum of 12 characters\n");
        clear();
        goto getPass;
    }
    int r = regexec(&regexCompiled, password, 1, &match, 0);
    if (r == 0)
    {
        printf("Invalid entry Only letters and numbers\n");
        goto getPass;
    }
}

/*Converts the Hexadecimal password in the databse to a decimal number  */

long hexConvert(char* hexadecimal)
{
    long long decimalNumber=0;
    char hexDigits[16] = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
                          '9', 'A', 'B', 'C', 'D', 'E', 'F'
                         };
    int i, j, power=0;


    /* Converting hexadecimal number to decimal number */
    for(i=strlen(hexadecimal)-1; i >= 0; i--)
    {
        /*search currect character in hexDigits array */
        for(j=0; j<16; j++)
        {
            if(hexadecimal[i] == hexDigits[j])
            {
                decimalNumber += j*pow(16, power);
            }
        }
        power++;
    }

    return (decimalNumber);

}

/*Compares password from user with password in database and if they are equal
user is granted access, if not than it is denied */

void server( long F, int R, char* id)
{


    long long crackedHash = hexConvert(Database_getSecondCol(id));
    long FPrime = (R) ^ (crackedHash); /*XOR function */

    if(FPrime == F)
    {
        printf("Access Granted\n");
    }
    else
    {
        printf("Access Denied\n");
    }
}



int main()
{
	
	//initilize variables for storage and use of database and values from user
    long long crackedHash;
    Database_init("part1.txt", 32, 64); //32 char for id and 12 char for password

    char* id = (char*) malloc(sizeof(char) * 32);
    char* password = (char*) malloc(sizeof(char) * 64);
    //1. User sends user id to the server
    getUserID(id);
    char* pass = Database_getSecondCol(id);

	//determine if the user is new or old
    if (pass == NULL)
    {
        printf("User not found, making new user\n");
        printf("Please enter a password\n");
        getPassword(password);
        char* hashpassword = OLMHash_hashPassword(password);
        Database_save(id, hashpassword);
        printf("User have been added\n");
        exit(0); //add possible loop back
    }
    else
    {	
		//if the user exsists get the password and authenticate it
        int try = 0;
        while (try++ < MaxTry)
            {
                printf("Please enter your password\n");
                getPassword(password);
                char* hashpassword = OLMHash_hashPassword(password);
					//2. get random number from the server
                    int R = getRand();
					//3. User side will computer the hash of the password
                    crackedHash = hexConvert(hashpassword);
					//f(r,h) is computer and sent to the server
                    long F = (R) ^ (crackedHash);
					//4. Server gets the hash from the user and compares it with what it had stored for authentication 
                    server(F,R,id);

                    exit(0);

            }
        if(try > MaxTry)
            {
                printf("Too many unsuccessful attempts\n");
            }
    }



    free(id);
    free(password);
    // printf("%s\n %d \n %d \n", hashpassword, F, R);
    return (0);

}
