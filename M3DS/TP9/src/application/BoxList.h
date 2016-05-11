#ifndef BOITELIST_H
#define BOITELIST_H

#include "Box.h"
#include "Vector3.h"

/**
@file
@author F. Aubert
@brief opérations sur OBB pour mini simu physique (collision+impulsion)

*/

class BoxList : public std::vector<Box *> {
  public:
    BoxList();
    virtual ~BoxList();

    void add(Box *b);

    void draw();

    void select(const p3d::Vector3 &p);
    Box *selected();

    void insertForce(const p3d::Vector3 &p,const p3d::Vector3 &f);
    double moment(const p3d::Vector3 &f,const p3d::Vector3 &a);

  protected:
  private:
    Box *_selected;

};

#endif // BOITELIST_H

