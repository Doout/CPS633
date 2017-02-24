#include "database.h"
#include "OLMHash.h"

/*Creates table of hashes being 4 characters long */

void hashAll() {
  int max = 26 * 26 * 26 * 26; // XXXX
  int index = -1;
  char *word = (char*) calloc(sizeof(char) , 12);
  while (index++ < max) {                             /*loop to create hashes */
    word[0] = 'A' + (index / 17576);
    word[1] = 'A' + (index / 676) % 26;
    word[2] = 'A' + (index / 26) % 26;
    word[3] = 'A' + (index % 26);
    char* pas = OLMHash_hashPassword(word);
    //     printf("%s\n", pas);
    Database_save(word , pas); /* Save Hash into databse */
    free(pas);
  }

}

void main(void) {
  Database_init("part3.txt", 4, 64);  /*takes part3.txt and reads file to initilze the database for the system */
 //hashAll() ;
  printf("Enter hash\n");

  //ask user for a hash to test against the database
  char* read = malloc(sizeof(char) * 30);
  scanf("%s", read); /*User input */
  char* password = Database_getFirstCol(read); /* Search table for actual password */
    if(password == NULL) {
          printf("can not find password\n");
          exit(0);
  }
  printf("%s\n", password );

}
