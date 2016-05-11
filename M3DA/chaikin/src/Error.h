#ifndef ERROR_H_INCLUDED
#define ERROR_H_INCLUDED

#include <string>
#include <exception>

/**
@author F. Aubert

Usage : throw ErrorD(message)
*/

// exception
class Error:public std::exception {
    std::string message;
    public:
    Error(std::string msg,int line,std::string fichier) throw();
    virtual ~Error() throw();

    virtual const char * what() const throw();

    void show() throw();

};

#define ErrorD(s) (Error(s,__LINE__,__FILE__))

#endif
