#include "GLApplication.h"

#include <iostream>

using namespace std;

GLApplication::~GLApplication() {
}

GLApplication::GLApplication() {
    /*_trianglePosition = {
    -0.8,-0.5,0.0, // vertex 0
    -0.2,-0.5,0.0, // 1
    -0.5,0.5,0.0,  // 2

    0.2,0.5,0.0,  // 3
    0.8,0.5,0.0,  // 4
    0.5,-0.5,0.0  // 5
  };

    _trianglePosition = {
        -0.8,-0.5,0.0, // vertex 0 anciennement vertex 0
        0.8,0.5,0.0,  // 1 anciennement 4
        -0.5,0.5,0.0,  // 2 anciennement 2

        -0.2,-0.5,0.0, // 3 anciennement 1
        0.5,-0.5,0.0,  // 4 anciennement 5
        0.2,0.5,0.0  // 5 anciennement 3
    };

    _trianglePosition = {
        -0.5,-1,0.0,
        -0.5,1,0.0,
        -0.25,-1,0.0,
        -0.25,1,0.0,
        0,-1,0.0,
        0,1,0.0,
        0.25,-1,0.0,
        0.25,1,0.0
     };

    _triangleColor = {
        0.3,0,0.6,1,
        0.3,0,0.6,1,
        0.0,0.9,0.0,1,

        0.0,0.5,0.6,1,
        0.0,0.5,0.6,1,
        0.9,0.0,0.0,1
    };
    _elementData = {0,3,2,2,1,4};*/


//    _trianglePosition = {
//      -0.6,-0.8,0,
//      -0.6,0.8,0,
//      0.6,-0.8,0,
//      0.6,0.8,0
//    };
//    _triangleTexCoord = {
//        1,1,
//        1,0,
//        0,1,
//        0,0
//    };



    _triangleColor.clear();
    for(unsigned int i=0;i<_trianglePosition.size();++i) {
      _triangleColor.push_back(1);_triangleColor.push_back(0);_triangleColor.push_back(0);_triangleColor.push_back(1);
    }
    _bool = false;
    _coeff = 0;

}

/** ********************************************************************** **/
void GLApplication::initialize() {
    // appelée 1 seule fois à l'initialisation du contexte
    // => initialisations OpenGL
    glClearColor(1,1,1,1);

    glLineWidth(2.0);
    glPolygonMode(GL_FRONT_AND_BACK,GL_FILL);

    _shader0=initProgram("simple");
    //initStrip(10, -0.5, 0.5, -1, 1);
    initRing(30,0.4,0.8);
    initTriangleBuffer();
    initTriangleVAO();
    initTexture();

}

void GLApplication::resize(int width,int height) {
    // appelée à chaque dimensionnement du widget OpenGL
    // (inclus l'ouverture de la fenêtre)
    // => réglages liés à la taille de la fenêtre
    glViewport(0,0,width,height);
    // ...
}

void GLApplication::update() {
    // appelée toutes les 20ms (60Hz)
    // => mettre à jour les données de l'application
    // avant l'affichage de la prochaine image (animation)
    // ...
    if(_bool){
        _coeff += 0.01;
        if(_coeff>=1)
            _bool = !_bool;
    } else {
        _coeff -= 0.01;
        if(_coeff<=0)
            _bool = !_bool;
     }
}

void GLApplication::draw() {
    // appelée après chaque update
    // => tracer toute l'image
    glClear(GL_COLOR_BUFFER_BIT);

    glUseProgram(_shader0);
    glBindVertexArray(_triangleVAO);

    glUniform1f(glGetUniformLocation(_shader0,"coeff"),_coeff);// _coeff for animation rendering
    glActiveTexture(GL_TEXTURE0); // on travaille avec l'unité de texture 0
    glBindTexture(GL_TEXTURE_2D,_textureId); // l'unité de texture 0 correspond à la texture _textureId (image lagoon.jpg).
    glUniform1f(glGetUniformLocation(_shader0,"texture"),0); // on affecte la valeur du sampler2D du fragment shader : c'est l'unité 0.

    glDrawArrays(GL_TRIANGLE_STRIP,0,_trianglePosition.size()/3);
    //glDrawElements(GL_TRIANGLES,6,GL_UNSIGNED_INT,0);

    glBindVertexArray(0);
    glUseProgram(0);


    snapshot(); // capture opengl window if requested
}

/** ********************************************************************** **/
/** i = button number, s = button text
 */
void GLApplication::leftPanel(int i,const std::string &s) {
    cout << "GLApplication : button clicked " << i << " " << s << endl;
    /*
  switch (i) {
    case 0:menu0();break;
    case 1:menu1();break;
  }
  */
}




/** *********************************************************** **/
GLuint GLApplication::initProgram(const std::string &filename) {
    GLuint program;

    string vertexSource=p3d::readTextFile(filename+".vert");
    string fragmentSource=p3d::readTextFile(filename+".frag");

    program=glCreateProgram();
    GLuint vertexShader=glCreateShader(GL_VERTEX_SHADER);
    GLuint fragmentShader=glCreateShader(GL_FRAGMENT_SHADER);
    glAttachShader(program,vertexShader);
    glAttachShader(program,fragmentShader);

    const char *source;
    source=vertexSource.c_str();
    glShaderSource(vertexShader,1,&source,NULL);
    source=fragmentSource.c_str();
    glShaderSource(fragmentShader,1,&source,NULL);

    glCompileShader(vertexShader);
    glCompileShader(fragmentShader);

    glBindAttribLocation(program,0,"position");
    glBindAttribLocation(program,1,"couleur");
    glBindAttribLocation(program,2,"texCoord");

    glLinkProgram(program);
    return program;
}


void GLApplication::initTexture() {
    QImage img;
    img.load("../media/lagoon.jpg");
    img=img.convertToFormat(QImage::Format_ARGB32);

    glGenTextures(1,&_textureId);
    glBindTexture(GL_TEXTURE_2D,_textureId);
    glTexImage2D(GL_TEXTURE_2D,0,GL_RGBA,img.width(),img.height(),0,GL_BGRA,GL_UNSIGNED_BYTE,img.bits());

    glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
    glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);

    glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
    glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);
}


void GLApplication::initTriangleBuffer() {

    glGenBuffers(1,&_trianglePositionBuffer);
    glBindBuffer(GL_ARRAY_BUFFER,_trianglePositionBuffer);
    glBufferData(GL_ARRAY_BUFFER,_trianglePosition.size()*sizeof(float),_trianglePosition.data(),GL_STATIC_DRAW);

    glGenBuffers(1,&_triangleColorBuffer);
    glBindBuffer(GL_ARRAY_BUFFER,_triangleColorBuffer);
    glBufferData(GL_ARRAY_BUFFER,_triangleColor.size()*sizeof(float),_triangleColor.data(),GL_STATIC_DRAW);

    glGenBuffers(1,&_elementBuffer);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,_elementBuffer);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER,_elementData.size()*sizeof(unsigned int),_elementData.data(),GL_STATIC_DRAW);

    glGenBuffers(1,&_triangleTexCoordBuffer);
    glBindBuffer(GL_ARRAY_BUFFER,_triangleTexCoordBuffer);
    glBufferData(GL_ARRAY_BUFFER,_triangleTexCoord.size()*sizeof(float),_triangleTexCoord.data(),GL_STATIC_DRAW);

}


void GLApplication::initTriangleVAO() {
    glGenVertexArrays(1,&_triangleVAO);
    glBindVertexArray(_triangleVAO);

    glBindBuffer(GL_ARRAY_BUFFER,_trianglePositionBuffer);
    glVertexAttribPointer(0,3,GL_FLOAT,GL_FALSE,0,0);

    glBindBuffer(GL_ARRAY_BUFFER,_triangleColorBuffer);
    glVertexAttribPointer(1,4,GL_FLOAT,GL_FALSE,0,0);

    glBindBuffer(GL_ARRAY_BUFFER,_triangleTexCoordBuffer);
    glVertexAttribPointer(2,2,GL_FLOAT,GL_FALSE,0,0);

    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, _elementBuffer);

    glEnableVertexAttribArray(0);
    glEnableVertexAttribArray(1);
    glEnableVertexAttribArray(2);

    glBindVertexArray(0);
}




void GLApplication::initStrip(int nbSlice, float xmin, float xmax, float ymin, float ymax ){
    int n;
    float size;
    size = xmax-xmin;
    _trianglePosition.clear();
    _triangleColor.clear();
    for(n=0;n<nbSlice;n++){

        _trianglePosition.push_back((size/nbSlice)*n+xmin);
        _trianglePosition.push_back(ymin);
        _trianglePosition.push_back(0);

        _trianglePosition.push_back((size/nbSlice)*n+xmin);
        _trianglePosition.push_back(ymax);
        _trianglePosition.push_back(0);

        _triangleColor.push_back(0);
        _triangleColor.push_back((size/nbSlice*n));
        _triangleColor.push_back(0);
        _triangleColor.push_back(1);

        _triangleColor.push_back(0);
        _triangleColor.push_back(0);
        _triangleColor.push_back(1-(size/nbSlice*n));
        _triangleColor.push_back(1);

    }

//    for (int i = 0 ; i < _trianglePosition.size(); i=i+3)
//        cout << _trianglePosition[i] << " " << _trianglePosition[i+1] << " " << _trianglePosition[i+2] << endl;
//    cout << _trianglePosition.size()/3 << " size " << endl;
}

void GLApplication::initRing(int nbSlice,float r0,float r1){
    float pi = 3.14159;
    float pas;

    pas = (2*pi)/nbSlice;
    _trianglePosition.clear();
    _triangleColor.clear();
    _triangleTexCoord.clear();

    for(int n=0;n<nbSlice+1;n++){

        _trianglePosition.push_back(r0*cos(n*pas));
        _trianglePosition.push_back(r0*sin(pas*n));
        _trianglePosition.push_back(0);

        _trianglePosition.push_back(r1*cos(n*pas));
        _trianglePosition.push_back(r1*sin(pas*n));
        _trianglePosition.push_back(0);

        /** Color **/
        _triangleColor.push_back(0);
        _triangleColor.push_back(((float)n/(float)nbSlice));
        _triangleColor.push_back(0);
        _triangleColor.push_back(1);

        _triangleColor.push_back(0);
        _triangleColor.push_back(0);
        _triangleColor.push_back(1-((float)n/(float)nbSlice));
        _triangleColor.push_back(1);//

        /** texture  **/
//        _triangleTexCoord.push_back(((float)n/(float)nbSlice));
//        _triangleTexCoord.push_back(0);
//        _triangleTexCoord.push_back(((float)n/(float)nbSlice));
//        _triangleTexCoord.push_back(1);
        /** texture q24 **/
        _triangleTexCoord.push_back(((r0*cos(n*pas))+1)/2);
        _triangleTexCoord.push_back(1-((r0*sin(n*pas))+1)/2);
        _triangleTexCoord.push_back(((r1*cos(n*pas))+1)/2);
        _triangleTexCoord.push_back(1-((r1*sin(n*pas))+1)/2);

    }

//    for (int i = 0 ; i < _trianglePosition.size(); i=i+3)
//        cout << _trianglePosition[i] << " " << _trianglePosition[i+1] << " " << _trianglePosition[i+2] << endl;
//    cout << _trianglePosition.size()/3 << " size " << endl;

}
