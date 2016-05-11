#ifndef VECTOR2_H
#define VECTOR2_H
class Vector2
{
private:

    double _x;
    double _y;
    double _w;

public:

    Vector2();
    Vector2(double x, double y);
    Vector2(double x, double y, double w);
    Vector2 operator +(const Vector2& right) const;
    Vector2 operator *(const Vector2& right) const;
    Vector2 operator +(const double right) const;
    Vector2 operator *(const double right) const;
    void x(double x);
    void y(double y);
    void w(double w);
    double x() const;
    double y() const;
    double w() const;
};
#endif // VECTOR2_H
