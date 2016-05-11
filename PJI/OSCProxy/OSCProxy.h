...
#include <lo/lo.h>
...

class OSCProxy
{
public:
	OSCProxy(const char* port);
	~OSCProxy(void);

	bool valid(void) { return osc_valid; }
	int receive(void);
	
	bool pointers_position(float &x0, float&y0, float &z0,
	                       float &x1, float&y1, float &z1);

private:
	lo_server osc_server;
	int osc_fd;                  // osc socket descriptor
	bool osc_valid;

	float hand0_X, hand0_Y, hand0_Z;
	float hand1_X, hand1_Y, hand1_Z;
};

