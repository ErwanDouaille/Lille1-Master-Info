#ifndef HANDTRACKINGLISTENER_H
#define HANDTRACKINGLISTENER_H

#include <winsock2.h>
#include <iostream>
#include <string>
#include <vector>

#include "hand.h"

#define PORT 20592

class HandTrackingListener
{

private:
    WSADATA wsa;

public:
    SOCKET s;
    struct sockaddr_in server, si_other;
    int slen , recv_len;
    char buf[512];
    double duration;
    std::vector<Hand> _hands;

    HandTrackingListener();
    ~HandTrackingListener();

    static void* run(void* arg);
    void dataToContainer(std::string data);
    const std::vector<Hand> getHands() {return _hands;}
    void setHands(std::vector<Hand> hands) { _hands = hands;}
};

#endif // HANDTRACKINGLISTENER_H
