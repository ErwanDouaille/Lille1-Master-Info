#include "Shader.h"
#include "Matrix4.h"
#include "Matrix3.h"
#include "Vector3.h"
#include "Vector4.h"
#include "Tools.h"

#include <sstream>
#include <iostream>
#include <fstream>

using namespace p3d;
using namespace std;

/*!
*
* @file
*
* @brief
* @author F. Aubert
*
*/

Shader *Shader::_current=NULL;

Shader::Shader() {
  _name="";
  _isInit=false;
}

Shader::~Shader() {
  glDeleteShader(_vertexId);
  glDeleteShader(_fragmentId);
  glDeleteProgram(_programId);
}

void Shader::init() {
  _programId=glCreateProgram();
  _fragmentId=glCreateShader(GL_FRAGMENT_SHADER);
  _vertexId=glCreateShader(GL_VERTEX_SHADER);

  _vertexSource="";
  _fragmentSource="";

  glAttachShader(_programId,_vertexId);
  glAttachShader(_programId,_fragmentId);


  _isInit=true;
}

void Shader::readFile(const string &resourceName,string *res) {
  QFileInfo resource=p3d::resourceFile(resourceName);

  ifstream f(resource.filePath().toStdString().c_str());
  if (f.fail()) throw ErrorD("fichier shader inexistant");
  char readChar[1000];
  string readString;
  while (f.good()) {
    f.getline(readChar,1000);
    readString=string(readChar);
    *res+=readString+"\n";
  }
  f.close();
}


void Shader::checkCompileError(GLuint id,const std::string &message) {
  int compile_ok;
  int info_length;

  glGetShaderiv(id,GL_COMPILE_STATUS,&compile_ok);
  glGetShaderiv(id,GL_INFO_LOG_LENGTH,&info_length);


  if (!compile_ok) {
    cout << "=============================" << endl;
    cout << "GLSL Error : " << message << endl;
    char *info=new char[info_length];
    glGetShaderInfoLog(id,info_length,NULL,info);
    cout << info;
    cout << endl;
    delete info;
    throw ErrorD("Shader compilation error");
  }
}


void Shader::compile(GLuint shader,const string &source) {
  const char *buffer=source.c_str();
  glShaderSource(shader,1,&buffer,NULL);
  glCompileShader(shader);
}

void Shader::read(const string &name) {
  _name=name;
  if (!_isInit) init();
  readFile(name+".vert",&_vertexSource);
  readFile(name+".frag",&_fragmentSource);
  compile(_vertexId,_vertexSource);
  checkCompileError(_vertexId,"in "+name+".vert");
  compile(_fragmentId,_fragmentSource);
  checkCompileError(_fragmentId,"in "+name+".frag");
  link();
  use();
}

void Shader::link() {
  for(auto attrib:_attribute) {
    glBindAttribLocation(_programId,attrib.second,attrib.first.c_str());
  }
  glLinkProgram(_programId);

  int link_ok,info_length;

  glGetProgramiv(_programId,GL_LINK_STATUS,&link_ok);
  glGetProgramiv(_programId,GL_INFO_LOG_LENGTH,&info_length);

  if (!link_ok) {
    char *info=new char[info_length];
    glGetProgramInfoLog(_programId,info_length,NULL,info);
    cout << "Info Log :" << endl;
    cout << info;
    cout << endl;
    delete info;
    throw ErrorD("Link shader program error");
  }
}

void Shader::use() {
  glUseProgram(_programId);
  Shader::_current=this;
}

void Shader::disable() {
  glUseProgram(0);
  Shader::_current=NULL;
}


int Shader::uniform(string nom) {
  int res=glGetUniformLocation(_programId,nom.c_str());
  return res;
}

void Shader::uniform(const string &nom,float value) {
  int loc=uniform(nom);
  glUniform1f(loc,value);
}

void Shader::uniform(int loc,float value) {
  glUniform1f(loc,value);
}

void Shader::uniform(const string &nom,int value) {
  int loc=uniform(nom);
  glUniform1i(loc,value);
}

void Shader::uniform(int loc,int value) {
  glUniform1i(loc,value);
}


void Shader::uniform(const string &nom,const Vector3 &v) {
  int loc=uniform(nom);
  glUniform3fv(loc,1,v.fv());
}

void Shader::uniform(int loc,const Vector3 &v) {
  glUniform3fv(loc,1,v.fv());
}

void Shader::uniform(const string &nom,const Matrix4 &m) {
  int loc=uniform(nom);
  glUniformMatrix4fv(loc,1,GL_FALSE,m.fv());
}

void Shader::uniform(int loc,const Matrix4 &m) {
  glUniformMatrix4fv(loc,1,GL_FALSE,m.fv());
}


void Shader::uniform(const string &nom,const Vector4 &p,int offset) {
  int loc=uniform(nom);
  glUniform4fv(loc+offset,1,p.fv());
}

void Shader::uniform(int loc,const Vector4 &p,int offset) {
  glUniform4fv(loc+offset,1,p.fv());
}

void Shader::uniform(const string &nom,const vector<int> &t) {
  int loc=uniform(nom+"[0]");
  uniform(loc,t);
}

void Shader::uniform(int loc,const vector<int> &t) {
  glUniform1iv(loc,t.size(),t.data());
}

// must be called **before** link !! (or recall link after attributes are set).
void Shader::attribute(const string &name,GLuint loc) {
  _attribute[name]=loc;
  //glBindAttribLocation(_programId,loc,name.c_str());
}

GLint Shader::attribute(const string &name) {
  return glGetAttribLocation(_programId,name.c_str());
}

void Shader::uniform(const string &nom,const Matrix3 &m) {
  int loc=uniform(nom);
  glUniformMatrix3fv(loc,1,GL_FALSE,m.fv());
}

void Shader::uniform(int loc,const Matrix3 &m) {
  glUniformMatrix3fv(loc,1,GL_FALSE,m.fv());
}

void Shader::uniform(const string &name,const vector<Matrix4> &m) {
  int loc=uniform(name+"[0]");
  uniform(loc,m);
}

void Shader::uniform(int loc,const vector<Matrix4> &m) {
  vector<float> mf;
  mf.resize(m.size()*16);
  int i=0,j=0;
  for(auto &v:mf) {
    v=m[i](j);
    if (j==15) {++i;j=0;}
    else ++j;
  }
  glUniformMatrix4fv(loc,m.size(),GL_FALSE,mf.data());
}



GLuint Shader::id() {
  return _programId;
}

Shader *Shader::current() {
  return Shader::_current;
}






