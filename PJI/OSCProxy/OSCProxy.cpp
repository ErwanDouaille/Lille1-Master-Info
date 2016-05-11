#include "OSCProxy.h"

...

static void error_cb(int num, const char *msg, const char *path)
{
	printf("liblo server error %d in path %s: %s\n", num, path, msg);
}

static int pointer_handler(const char *path, const char *types, lo_arg **argv, int argc,
                           lo_message msg, void *user_data)
{
	OSCProxy *osc_proxy = (OSCProxy*)user_data;
  
	osc_proxy->hand0_X = argv[0]->f;
	osc_proxy->hand0_Y = argv[1]->f;
	osc_proxy->hand0_Z = argv[2]->f;
  
	osc_proxy->hand1_X = argv[3]->f;
	osc_proxy->hand2_Y = argv[4]->f;
	osc_proxy->hand3_Z = argv[5]->f;

	return 0;
}

OSCProxy::OSCProxy(const char* oscport) : osc_valid(false)
{
	/* start a new server */
	osc_server = lo_server_new(oscport, error_cb);
	if (!osc_server)
		return;

	if (!lo_server_add_method(osc_server, "/pointers", "ffffff", pointer_handler, this)) {
		lo_server_free(osc_server);
		return;
	}

	/* get the file descriptor of the server socket, if supported */
	osc_fd = lo_server_get_socket_fd(osc_server);

	osc_valid = true;
}

OSCProxy::~OSCProxy()
{
	lo_server_free(osc_server);
}

int OSCProxy::receive()
{
	int err;
	fd_set set;
	struct timeval tout;
	int len;
  
	if (!valid()) return false;

	FD_ZERO(&set);
	FD_SET(osc_fd, &set);

	tout.tv_sec = 0; // 'select' returns immediately
	tout.tv_usec = 0;

	switch ((err = select(osc_fd + 1, &set, NULL, NULL, &tout))) {
	case 1:
		break;        // data available
	case 0:
		return 0;     // nothing
	default:
		return -2;    // error
	}

	len = lo_server_recv_noblock(osc_server, 0);
  
	return len;
}

bool OSCProxy::pointer_position(float &x0, float &y0, float &z0,
                                float &x1, float &y1, float &z1)
{
	static float old_x0 = -1.0;
	static float old_y0 = -1.0;
	static float old_z0 = -1.0;
	static float old_x1 = -1.0;
	static float old_y1 = -1.0;
	static float old_z1 = -1.0;

	if (fabsf(hand0_X - old_x0) < 0.01 &&
	    fabsf(hand0_Y - old_y0) < 0.01 &&
	    fabsf(hand0_Z - old_z0) < 0.01 &&
	    fabsf(hand1_X - old_x1) < 0.01 &&
	    fabsf(hand1_Y - old_y1) < 0.01 &&
	    fabsf(hand1_Z - old_z1) < 0.01)
		return false;

	x0 = old_x0 = hand0_X;
	y0 = old_y0 = hand0_Y;
	z0 = old_z0 = hand0_Z;
  
	x1 = old_x1 = hand1_X;
	y1 = old_y1 = hand1_Y;
	z1 = old_z1 = hand1_Z;

	return true;
}
