//database header file
#include <stdio.h>
#include <stdlib.h>
#include <locale.h>
#include <string.h>

void Database_init(char* filename, int col1Length,int col2Length);
void Database_save(char* col1,char* col2);
char* Database_getSecondCol(char* fristCol);
char* Database_getFirstCol(char* secondCol);
void Database_updateSecondCol(char* fristCol,char* secondCol);

void Database_keepFileOpen();
void Database_closeFile();
