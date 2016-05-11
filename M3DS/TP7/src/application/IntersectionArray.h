#ifndef INTERSECTIONARRAY_H
#define INTERSECTIONARRAY_H


#include "CsgTree.h"
#include <list>
#include "Intersection.h"


class IntersectionArray: public std::vector<Intersection *>  {
  public:
    void fusion(IntersectionArray &left,IntersectionArray &right,CsgTree::ENode op);

    IntersectionArray();
    void addIntersection(double lambda);
    void deleteAll();
    virtual ~IntersectionArray();
  protected:
  private:
};

#endif // INTERSECTIONARRAY_H

