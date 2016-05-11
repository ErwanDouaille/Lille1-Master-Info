#ifndef VECTOR2_H
#define VECTOR2_H


class Vector2
{
public:
    Vector2();
    Vector2(double x, double y);
    Vector2(double x, double y, bool value);
    void setX(double x) { this->x = x;}
    void setY(double y) { this->y = y;}
    void setFixe(bool value) { this->fixe = value;}
    double getX() const { return this->x;}
    double getY() const { return this->y;}
    double isFixe() const { return this->fixe;}
    Vector2 operator +(const Vector2& right) const;
    Vector2 operator *(const Vector2& right) const;
    Vector2 operator +(const double right) const;
    Vector2 operator *(const double right) const;

private:

    double x;
    double y;
    bool fixe;

};

#endif // VECTOR2_H
