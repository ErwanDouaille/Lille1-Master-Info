#ifndef HERMITE_H
#define HERMITE_H

#include "Vector3.h"
#include "InteractPosition.h"
#include <iostream>

/**
@author F. Aubert
*/

class Hermite : public p3d::InteractPosition
{
  public:
    Hermite();
    virtual ~Hermite();
    Hermite(const p3d::Vector3 &a,const p3d::Vector3 &na,const p3d::Vector3 &b,const p3d::Vector3 &nb);
    void set(const p3d::Vector3 &a, const p3d::Vector3 &ta, const p3d::Vector3 &b, const p3d::Vector3 &tb);
    p3d::Vector3 eval(double t);
    void draw();
    void drawControl();

    ///////
    p3d::Vector3 *interactPoint(unsigned int i);
    unsigned int interactSize() {return _nbInput;}
    void interactInsert(unsigned int i,const p3d::Vector3 &p);
    void interactUpdate(unsigned int i);


    void nbInput(int n)  {_nbInput=n;}
    void incNbInput() {_nbInput++;}
    int nbInput() {return _nbInput;}

    void a(const p3d::Vector3 &p) {_a=p;}
    void b(const p3d::Vector3 &p) {_b=p;}
    void ta(const p3d::Vector3 &p) {_ta=p;}
    void tb(const p3d::Vector3 &p) {_tb=p;}

    p3d::Vector3 a() {return _a;}
    p3d::Vector3 b() {return _b;}
    p3d::Vector3 ta() {return _ta;}
    p3d::Vector3 tb() {return _tb;}

  protected:
  private:
    p3d::Vector3 _a;
    p3d::Vector3 _b;
    p3d::Vector3 _ta;
    p3d::Vector3 _tb;

    int _nbInput;
    p3d::Vector3 _interactTa;
    p3d::Vector3 _interactTb;
};

#endif // HERMITE_H

