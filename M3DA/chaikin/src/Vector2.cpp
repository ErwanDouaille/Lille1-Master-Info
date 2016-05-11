#include "Vector2.h"
#include <iostream>
Vector2::Vector2()
{
}

Vector2::Vector2(double x, double y){
    this->setX(x);
    this->setY(y);
    this->setFixe(false);
}
Vector2::Vector2(double x, double y, bool value){
    std::cout << "x " << x << " y " << y << std::endl;
    this->setX(x);
    this->setY(y);
    this->setFixe(value);
}

Vector2 Vector2::operator +(const Vector2& right) const {
    Vector2 result;
    result.setX(this->getX() + right.getX());
    result.setY(this->getY() + right.getY());
    return result;
}

Vector2 Vector2::operator +(const double right) const {
    Vector2 result;
    result.setX(this->getX() + right);
    result.setY(this->getY() + right);
    return result;
}

Vector2 Vector2::operator *(const Vector2& right) const {
    Vector2 result;
    result.setX(this->getX() * right.getX());
    result.setY(this->getY() * right.getY());
    return result;
}

Vector2 Vector2::operator *(const double right) const {
    Vector2 result;
    result.setX(this->getX() * right);
    result.setY(this->getY() * right);
    return result;
}
