#include "database.h"


//initial setup for the database to be used in the system for storage of user ID and hashed passwords
char* fileName;
int maxCol1Length;
int maxCol2Length;
int keepOpen;
FILE *file;

//Initilize the database into the system
void Database_init(char* file_name, int maxcol1length, int maxcol2length) {
     fileName = (char*)malloc(sizeof(char) * strlen(file_name));
     strcpy(fileName, file_name);
     maxCol1Length = maxcol1length;
     maxCol2Length = maxcol2length;
}

//Open the file
void Database_keepFileOpen(FILE f) {
     keepOpen = 1;
}

//Close the file
void Database_closeFile() {
     if (file != NULL)
          fclose(file);
}

//Save the database, rewrite it to the same filename as was used when we initilized and opened the file
void Database_save(char* col1, char* col2) {
     FILE*  f  = fopen (fileName, "a+");
     fprintf(f, "%s %s\n", col1 , col2);
     fclose(f);
}


//Read the user ID and matching hashed password from the file if it is found within the database
char *Database_readLine(FILE *file) {
     if (file == NULL) {
          printf("Error: file pointer is null.");
          exit(1);
     }

     int maximumLineLength = 128;
     char *lineBuffer = (char *)malloc(sizeof(char) * maximumLineLength);

     if (lineBuffer == NULL) {
          printf("Error allocating memory for line buffer.");
          exit(1);
     }

     char ch = fgetc(file);
     int count = 0;
     while ((ch != '\n') && (ch != EOF)) {
          if (count == maximumLineLength) {
               maximumLineLength += 128;
               lineBuffer = realloc(lineBuffer, maximumLineLength);
               if (lineBuffer == NULL) {
                    printf("Error reallocating space for line buffer.");
                    exit(1);
               }
          }
          lineBuffer[count] = ch;
          count++;
          ch = getc(file);
     }
     lineBuffer[count] = '\0';
     return lineBuffer;
}
//get the secondCol, hashed password, from the database using the function above
char* Database_getSecondCol(char* fristCol) {
     FILE * f = fopen (fileName, "r");
     if ( f == NULL)
          return NULL;
     char* col1 = (char*) malloc(sizeof(char) * maxCol1Length);
     char* col2 = (char*) malloc(sizeof(char) * maxCol2Length);
     while (!feof(f)) {
          char *line = Database_readLine(f);
          sscanf(line, "%s %s", col1, col2);
          if (strcmp(fristCol, col1) == 0) {
               free(col1);
               fclose(f);
               return col2;
          }
          free(line);
     }
     fclose(f);
     return NULL;
}
//getFirstCol, again with the readline function, to get the user ID from the database to be determined if the user is in the system or not
char* Database_getFirstCol(char* secondCol) {
     FILE * f = fopen (fileName, "r");
     char* col1 = (char*) malloc(sizeof(char) * maxCol1Length);
     char* col2 = (char*) malloc(sizeof(char) * maxCol2Length);
     while (!feof(f)) {
          char *line = Database_readLine(f);
          sscanf(line, "%s %s", col1, col2);
          if (strcmp(secondCol, col2) == 0) {
               free(line);
               free(col2);
               fclose(f);
               return col1;
          }
          free(line);
     }
     fclose(f);
     return NULL;
}

//Used to change the password associated with the user ID entered.
void Database_updateSecondCol(char* fristCol, char* secondCol) {
     char* replace = (char*) malloc(sizeof(char) * (strlen(fristCol) +  strlen(secondCol) + 1));
     sprintf(replace, "%s %s", fristCol , secondCol);
     char buf[1024];
     sprintf(buf, "sed -i 's/^%s .*/%s/' %s", fristCol , replace, fileName);
     printf("%s\n", buf);
     system(buf);
     system("clear");
     free(replace);
}
