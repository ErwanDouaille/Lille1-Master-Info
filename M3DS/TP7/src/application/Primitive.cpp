#include "Primitive.h"
#include <math.h>
#include <iostream>
#include "Matrix4.h"
#include "Material.h"

/**
@file
@author Fabrice Aubert
*/

using namespace std;
using namespace p3d;

Primitive::~Primitive() {}

Primitive::Primitive() {
}

void Primitive::material(const Material &mat) {
	_mat=mat;
}



const Material &Primitive::material() const {
    return _mat;
}


void Primitive::materialGL() {
  _mat.setGL();
}

