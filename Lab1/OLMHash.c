
#include "OLMHash.h"

/* Hash algorithm that creates our hashed passwords */

void OLMHash_E(char *in, char *out) {
	char c0 = toupper(in[0]);      /* Must be upper case */
	char c1 = toupper(in[1]);
	char c2 = toupper(in[2]);
	char c3 = toupper(in[3]);

	out[0] = (c0 & 0x80) ^ (((c0 >> 1) & 0x7F) ^ ((c0) & 0x7F));
	out[1] = ((c1 & 0x80) ^ ((c0 << 7) & 0x80)) ^ (((c1 >> 1) & 0x7F) ^ ((c1) & 0x7F));
	out[2] = ((c2 & 0x80) ^ ((c1 << 7) & 0x80)) ^ (((c2 >> 1) & 0x7F) ^ ((c2) & 0x7F));
	out[3] = ((c3 & 0x80) ^ ((c2 << 7) & 0x80)) ^ (((c3 >> 1) & 0x7F) ^ ((c3) & 0x7F));
}

/*Takes user password and hashes */

char* OLMHash_hashPassword(char* password) {
	char* hash =  (char*) calloc(sizeof(char) , 12);
	char* buf =  (char*) calloc(sizeof(char) , 100);
	int i;
	for (i = 0; i < 4; i++) {           /*Hash must be 4 characters long */
 		OLMHash_E(&(password[i * 4]), &(hash[i * 4])); /*Takes password and outputs the hash */
	}
	long hashNumber = (int) hash[0];        
	int n = 1;
	for (n = 1; n <= 12; n++) {
		if (hash[n] == '\0')
			break;
		hashNumber = (hashNumber * 953 + (int) hash[n]);
	}
	sprintf(buf, "%lX", hashNumber);
	free(hash);
	return buf;
}

