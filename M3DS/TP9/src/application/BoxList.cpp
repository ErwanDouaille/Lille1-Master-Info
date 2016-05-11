#include "BoxList.h"

#include "Vector3.h"
#include <iostream>

/**
@file
@author F. Aubert
@brief opérations sur OBB pour mini simu physique (collision+impulsion)

*/

using namespace std;
using namespace p3d;

BoxList::BoxList() {
  //ctor
  _selected=NULL;
}

BoxList::~BoxList() {
  //dtor
  for(unsigned int i=0;i<size();i++) {
    delete at(i);
  }
}


void BoxList::select(const Vector3 &p) {
  Box *b_found=NULL;
  for(unsigned int i=0;!b_found && i<size();i++) {
    Box *b=(*this)[i];
    if (b->isInside(p)) {
      b_found=b;
    }
  }
  _selected=b_found;
}

Box *BoxList::selected() {
  return _selected;
}

void BoxList::add(Box *b) {
  push_back(b);
}

void BoxList::draw() {
  for(unsigned int i=0;i<size();i++) {
    this->at(i)->draw();
  }
}


