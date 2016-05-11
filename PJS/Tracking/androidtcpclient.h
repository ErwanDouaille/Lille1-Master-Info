#ifndef ANDROIDTCPCLIENT_H
#define ANDROIDTCPCLIENT_H

#undef UNICODE

#define WIN32_LEAN_AND_MEAN

#include <windows.h>
#include <winsock2.h>
#include <ws2tcpip.h>
#include <stdlib.h>
#include <stdio.h>
#include <string>
#include <vector>
#include <iostream>
#include <string>

#define DEFAULT_BUFLEN 512
#define DEFAULT_PORT "1234"
#define ACTIVATION_BOUTON "activation"
#define RETOUR_BOUTON "retour"

using namespace std;

struct infoServer {
    int id;
    bool editMode = false;
};

class AndroidTCPClient
{
public:
    // Constructeur
    AndroidTCPClient();
    bool createServer();
    SOCKET waitForClient();
    void listenClient();
    bool sendMessage(int identifiant, char* action);
    void deconnecterClient();
    void shutdownServer();
    infoServer getInfo() const { return info;}
    void stopEditMode() { info.editMode = false;}

    static void* run(void* arg);


private:
    WSADATA wsaData;
    int iResult;
    infoServer info;
    SOCKET clientSocket;
    SOCKET ListenSocket;

    struct addrinfo *result;
    struct addrinfo hints;

    int iSendResult;

};

#endif // ANDROIDTCPCLIENT_H
