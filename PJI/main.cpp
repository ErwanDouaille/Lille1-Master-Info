/****************************************************************************
*                                                                           *
*  OpenNI 1.1 Alpha                                                         *
*  Copyright (C) 2011 PrimeSense Ltd.                                       *
*                                                                           *
*  This file is part of OpenNI.                                             *
*                                                                           *
*  OpenNI is free software: you can redistribute it and/or modify           *
*  it under the terms of the GNU Lesser General Public License as published *
*  by the Free Software Foundation, either version 3 of the License, or     *
*  (at your option) any later version.                                      *
*                                                                           *
*  OpenNI is distributed in the hope that it will be useful,                *
*  but WITHOUT ANY WARRANTY; without even the implied warranty of           *
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the             *
*  GNU Lesser General Public License for more details.                      *
*                                                                           *
*  You should have received a copy of the GNU Lesser General Public License *
*  along with OpenNI. If not, see <http://www.gnu.org/licenses/>.           *
*                                                                           *
****************************************************************************/
//---------------------------------------------------------------------------
// Includes
//---------------------------------------------------------------------------
#include <XnOpenNI.h>
#include <XnCodecIDs.h>
#include <XnCppWrapper.h>
#include <iostream>
#include <map>
#include <math.h>
#include "lo/lo.h"
using namespace std;

//---------------------------------------------------------------------------
// Globals
//---------------------------------------------------------------------------
// Kinect Globals
xn::Context g_Context;
xn::ScriptNode g_scriptNode;
xn::DepthGenerator g_DepthGenerator;
xn::UserGenerator g_UserGenerator;
xn::Player g_Player;
XnBool g_bNeedPose = FALSE;
XnBool startDrawing = FALSE;
XnChar g_strPose[20] = "";

// Server Globals
lo_address oscClient;
string clientAddress;
char* clientPort;
char* serverPort;

//---------------------------------------------------------------------------
//
//
//              KINECT PART
//
//
//---------------------------------------------------------------------------
void CleanupExit()
{
	g_scriptNode.Release();
	g_DepthGenerator.Release();
	g_UserGenerator.Release();
	g_Player.Release();
	g_Context.Release();

	exit (1);
}

//---------------------------------------------------------------------------
// Code skeleton detection, init, newuser, lost ...
//---------------------------------------------------------------------------
std::map<XnUInt32, std::pair<XnCalibrationStatus, XnPoseDetectionStatus> > m_Errors;
void XN_CALLBACK_TYPE MyCalibrationInProgress(xn::SkeletonCapability& capability, XnUserID id, XnCalibrationStatus calibrationError, void* pCookie)
{
	m_Errors[id].first = calibrationError;
}
void XN_CALLBACK_TYPE MyPoseInProgress(xn::PoseDetectionCapability& capability, const XnChar* strPose, XnUserID id, XnPoseDetectionStatus poseError, void* pCookie)
{
	m_Errors[id].second = poseError;
}

// Callback: New user was detected
void XN_CALLBACK_TYPE User_NewUser(xn::UserGenerator& generator, XnUserID nId, void* pCookie)
{
	printf("New User %d\n", nId);
	// New user found
	if (g_bNeedPose)
	{
		g_UserGenerator.GetPoseDetectionCap().StartPoseDetection(g_strPose, nId);
	}
	else
	{
		g_UserGenerator.GetSkeletonCap().RequestCalibration(nId, TRUE);
	}
}
// Callback: An existing user was lost
void XN_CALLBACK_TYPE User_LostUser(xn::UserGenerator& generator, XnUserID nId, void* pCookie)
{
	printf("Lost user %d\n", nId);
}
// Callback: Detected a pose
void XN_CALLBACK_TYPE UserPose_PoseDetected(xn::PoseDetectionCapability& capability, const XnChar* strPose, XnUserID nId, void* pCookie)
{
	printf("Pose %s detected for user %d\n", strPose, nId);
	g_UserGenerator.GetPoseDetectionCap().StopPoseDetection(nId);
	g_UserGenerator.GetSkeletonCap().RequestCalibration(nId, TRUE);
}
// Callback: Started calibration
void XN_CALLBACK_TYPE UserCalibration_CalibrationStart(xn::SkeletonCapability& capability, XnUserID nId, void* pCookie)
{
	printf("Calibration started for user %d\n", nId);
}

// Callback: Finished calibration
void XN_CALLBACK_TYPE UserCalibration_CalibrationEnd(xn::SkeletonCapability& capability, XnUserID nId, XnBool bSuccess, void* pCookie) {
	if (bSuccess){
		// Calibration succeeded
		printf("Calibration complete, start tracking user %d\n", nId);
		g_UserGenerator.GetSkeletonCap().StartTracking(nId);
	} else {
		// Calibration failed
		printf("Calibration failed for user %d\n", nId);
		if (g_bNeedPose) {
			g_UserGenerator.GetPoseDetectionCap().StartPoseDetection(g_strPose, nId);
		} else {
			g_UserGenerator.GetSkeletonCap().RequestCalibration(nId, TRUE);
		}
	}
}

void XN_CALLBACK_TYPE UserCalibration_CalibrationComplete(xn::SkeletonCapability& capability, XnUserID nId, XnCalibrationStatus eStatus, void* pCookie) {
	if (eStatus == XN_CALIBRATION_STATUS_OK) {
		// Calibration succeeded
		printf("Calibration complete, start tracking user %d\n", nId);
		g_UserGenerator.GetSkeletonCap().StartTracking(nId);
	} else {
		// Calibration failed
		printf("Calibration failed for user %d\n", nId);
		if (g_bNeedPose) {
			g_UserGenerator.GetPoseDetectionCap().StartPoseDetection(g_strPose, nId);
		} else {
			g_UserGenerator.GetSkeletonCap().RequestCalibration(nId, TRUE);
		}
	}
}


#define SAMPLE_XML_PATH "SamplesConfig.xml"
#define CHECK_RC(nRetVal, what)										\
	if (nRetVal != XN_STATUS_OK)									\
	{																\
		printf("%s failed: %s\n", what, xnGetStatusString(nRetVal));\
		return nRetVal;												\
	}

//---------------------------------------------------------------------------
//
//
//              SERVER PART
//
//
//---------------------------------------------------------------------------


int screen_handler(const char *path, const char *types, lo_arg **argv,
		    int argc, void *data, void *user_data)
{
	printf("/screen command received\n");

	int posX = argv[0]->i;
	int posY = argv[1]->i;
	int posZ = argv[2]->i;

	return 1;
}

void error_cb(int num, const char *msg, const char *path)
{
	printf("liblo server error %d in path %s: %s\n", num, path, msg);
}

void kinect_get_hands_and_send(void)
{
	float hand0_X, hand0_Y, hand0_Z;
	float hand1_X, hand1_Y, hand1_Z;
	XnUserID aUsers[15];
	XnUInt16 nUsers = 15;
	xn::SceneMetaData sceneMD;
	xn::DepthMetaData depthMD;

	// retrieve camera data
	g_DepthGenerator.GetMetaData(depthMD);
	g_Context.WaitOneUpdateAll(g_DepthGenerator);

	g_DepthGenerator.GetMetaData(depthMD);
	g_UserGenerator.GetUserPixels(0, sceneMD);

	// retrieve and store users, number
	g_UserGenerator.GetUsers(aUsers, nUsers);

	if (!g_UserGenerator.GetSkeletonCap().IsTracking(aUsers[0])) {
		hand0_X = 0.0;hand0_Y = 0.0;hand0_Z = 0.0;
		hand1_X = 0.0;hand1_Y = 0.0;hand1_Z = 0.0;
	} else {
		XnSkeletonJointPosition right, left;
		g_UserGenerator.GetSkeletonCap().GetSkeletonJointPosition(aUsers[0], XN_SKEL_RIGHT_HAND, right);
		g_UserGenerator.GetSkeletonCap().GetSkeletonJointPosition(aUsers[0], XN_SKEL_LEFT_HAND, left);

		hand0_X = right.position.X;
		hand0_Y = right.position.Y;
		hand0_Z = right.position.Z;
		hand1_X = left.position.X;
		hand1_Y = left.position.Y;
		hand1_Z = left.position.Z;
	}
	lo_send(oscClient,"/pointers","ffffff", hand0_X, hand0_Y, hand0_Z, hand1_X, hand1_Y, hand1_Z);
}

//---------------------------------------------------------------------------
//
//
//              MAIN PART
//
//
//---------------------------------------------------------------------------

//---------------------------------------------------------------------------
// Code Main, init kinect, throw errors, start skeleton detection
// and loop while trying detect new users. Init server and call kinect_get_hands_and_send(void)
// in this method righ and left hand positions will be send to the client
//---------------------------------------------------------------------------

int main(int argc, char **argv) {

	//SERVER INIT
	if (argc < 4) {
		printf("Usage : port_to_listen_on [address_to_send_to] port_to_send_to.\n");
		exit(0);
	}
	serverPort = argv[1];
	clientAddress = argv[2];
	clientPort = argv[3];

	lo_server_thread st = lo_server_thread_new(serverPort, error_cb);
	lo_server_thread_add_method(st, "/screen", "iii", screen_handler, NULL);
	lo_server_thread_start(st);

	oscClient = lo_address_new(clientAddress.c_str(), clientPort);


	//KINECT INIT
	//throw errors
	XnStatus nRetVal = XN_STATUS_OK;
	if (argc > 5) {
		nRetVal = g_Context.Init();
		CHECK_RC(nRetVal, "Init");
		nRetVal = g_Context.OpenFileRecording(argv[1], g_Player);
		if (nRetVal != XN_STATUS_OK) {
			printf("Can't open recording %s: %s\n", argv[1], xnGetStatusString(nRetVal));
			return 1;
		}
	} else {
		xn::EnumerationErrors errors;
		nRetVal = g_Context.InitFromXmlFile(SAMPLE_XML_PATH, g_scriptNode, &errors);
		if (nRetVal == XN_STATUS_NO_NODE_PRESENT) {
			XnChar strError[1024];
			errors.ToString(strError, 1024);
			printf("%s\n", strError);
			return (nRetVal);
		} else if (nRetVal != XN_STATUS_OK) {
			printf("Open failed: %s\n", xnGetStatusString(nRetVal));
			return (nRetVal);
		}
	}

	//init kinect cameras
	nRetVal = g_Context.FindExistingNode(XN_NODE_TYPE_DEPTH, g_DepthGenerator);
	CHECK_RC(nRetVal, "Find depth generator");
	nRetVal = g_Context.FindExistingNode(XN_NODE_TYPE_USER, g_UserGenerator);
	if (nRetVal != XN_STATUS_OK) {
		nRetVal = g_UserGenerator.Create(g_Context);
		CHECK_RC(nRetVal, "Find user generator");
	}

	XnCallbackHandle hUserCallbacks, hCalibrationStart, hCalibrationComplete, hPoseDetected, hCalibrationInProgress, hPoseInProgress;

	// error if skeleton isn't available
	if (!g_UserGenerator.IsCapabilitySupported(XN_CAPABILITY_SKELETON)) {
		printf("Supplied user generator doesn't support skeleton\n");
		return 1;
	}

	// init skeleton detection
	nRetVal = g_UserGenerator.RegisterUserCallbacks(User_NewUser, User_LostUser, NULL, hUserCallbacks);
	CHECK_RC(nRetVal, "Register to user callbacks");
	nRetVal = g_UserGenerator.GetSkeletonCap().RegisterToCalibrationStart(UserCalibration_CalibrationStart, NULL, hCalibrationStart);
	CHECK_RC(nRetVal, "Register to calibration start");
	nRetVal = g_UserGenerator.GetSkeletonCap().RegisterToCalibrationComplete(UserCalibration_CalibrationComplete, NULL, hCalibrationComplete);
	CHECK_RC(nRetVal, "Register to calibration complete");

	// does we need pose ? yes
	if (g_UserGenerator.GetSkeletonCap().NeedPoseForCalibration()) {
		g_bNeedPose = TRUE;
		if (!g_UserGenerator.IsCapabilitySupported(XN_CAPABILITY_POSE_DETECTION)) {
			printf("Pose required, but not supported\n");
			return 1;
		}
		nRetVal = g_UserGenerator.GetPoseDetectionCap().RegisterToPoseDetected(UserPose_PoseDetected, NULL, hPoseDetected);
		CHECK_RC(nRetVal, "Register to Pose Detected");
		g_UserGenerator.GetSkeletonCap().GetCalibrationPose(g_strPose);
	}

	// tracking pose
	g_UserGenerator.GetSkeletonCap().SetSkeletonProfile(XN_SKEL_PROFILE_ALL);

	nRetVal = g_UserGenerator.GetSkeletonCap().RegisterToCalibrationInProgress(MyCalibrationInProgress, NULL, hCalibrationInProgress);
	CHECK_RC(nRetVal, "Register to calibration in progress");

	nRetVal = g_UserGenerator.GetPoseDetectionCap().RegisterToPoseInProgress(MyPoseInProgress, NULL, hPoseInProgress);
	CHECK_RC(nRetVal, "Register to pose in progress");

	nRetVal = g_Context.StartGeneratingAll();
	CHECK_RC(nRetVal, "StartGenerating");

	// loop than check if users has been detected, and send hands positions to client 
	while (true){
		kinect_get_hands_and_send();
	}
}
