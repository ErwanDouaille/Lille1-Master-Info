#include "Error.h"
#include <stdlib.h>
#include <iostream>
#include <string>
#include <iostream>
#include <sstream>
/*!
*
* @file
*
* @brief
* @author F. Aubert
*
*/
using namespace std;

Error::Error(string mesg, int line,string fichier) throw() {
        std::ostringstream oss;

            string base;
    unsigned int index;

#ifdef _WIN32
#define SEARCH_CHAR "\\"
#else
#define SEARCH_CHAR "/"
#endif
    index=fichier.rfind(SEARCH_CHAR);
    index=fichier.rfind(SEARCH_CHAR,index-1);
    base=fichier.substr(index+1,fichier.length()-index-1);


        oss << "Error in "<< base << " line " << line << " : " << mesg;
        this->message = oss.str();
}

const char * Error::what() const throw() {
     return this->message.c_str();
}


void Error::show() throw() {
    cout << message << endl;
}

Error::~Error() throw() {}
