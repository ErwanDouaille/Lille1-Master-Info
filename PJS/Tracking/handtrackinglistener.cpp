#include "handtrackinglistener.h"

using namespace std;

HandTrackingListener::HandTrackingListener()
{
    slen = sizeof(si_other) ;
    duration=0.0;
    if (WSAStartup(MAKEWORD(2,2),&wsa) != 0) {
        printf("Failed. Error Code : %d",WSAGetLastError());
        exit(EXIT_FAILURE);
    }

    if((s = socket(AF_INET , SOCK_DGRAM , 0 )) == INVALID_SOCKET) {
        printf("Could not create socket : %d" , WSAGetLastError());
        exit(EXIT_FAILURE);
    }

    server.sin_family = AF_INET;
    server.sin_addr.s_addr = INADDR_ANY;
    server.sin_port = htons( PORT );

    if( bind(s ,(struct sockaddr *)&server , sizeof(server)) == SOCKET_ERROR) {
        printf("Bind failed with error code : %d" , WSAGetLastError());
        exit(EXIT_FAILURE);
    }

}

static void split(const string& str, char delimiter, vector<string>& result) {
    string::size_type i = 0;
    string::size_type j = str.find(delimiter);

    while (j != string::npos) {
        result.push_back(str.substr(i, j-i));
        i = ++j;
        j = str.find(delimiter, j);

        if (j == string::npos)
            result.push_back(str.substr(i, str.length()));
    }
}

void HandTrackingListener::dataToContainer(string data) {
    vector<string> handsSTR;
    split(data,'/', handsSTR);
    vector<Hand> hands;
    for (size_t i=1; i<handsSTR.size(); i++) {
        vector<string> handPosFinger;
        split(handsSTR.at(i),':', handPosFinger);

        Hand hand;
        float handX, handY, handZ;
        handX = atof(handPosFinger.at(1).c_str());
        handY = atof(handPosFinger.at(2).c_str());
        handZ = atof(handPosFinger.at(3).c_str());

        hand.setHandPosition(Point3D(handX, handY, handZ));
        for (unsigned int i = 4 ; i < handPosFinger.size() ; i++){
            if (handPosFinger.at(i) == "finger"){
                Point3D finger(atof(handPosFinger.at(i+1).c_str()), atof(handPosFinger.at(i+2).c_str()), atof(handPosFinger.at(i+3).c_str()));
                hand.getFingerPositions().push_back(finger);
            }
        }
        hands.push_back(hand);
    }
    this->setHands(hands);
}

void* HandTrackingListener::run(void* arg) {

    HandTrackingListener * listener = (HandTrackingListener*) arg;

    while (true) {
        memset(listener->buf,'\0', 512);
        if ((listener->recv_len = recvfrom(listener->s, listener->buf, 512, 0, (struct sockaddr *) &(listener->si_other), &(listener->slen))) == SOCKET_ERROR) {
            printf("recvfrom() failed with error code : %d" , WSAGetLastError());
        }
        listener->dataToContainer(listener->buf);
    }
    return 0;
}

HandTrackingListener::~HandTrackingListener()
{

}

