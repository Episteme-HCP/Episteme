//////////////////////license & copyright header///////////////////////
//                                                                   //
//                Copyright (c) 1999 by Michel Van den Bergh         //
//                                                                   //
// This library is free software; you can redistribute it and/or     //
// modify it under the terms of the GNU Lesser General Public        //
// License as published by the Free Software Foundation; either      //
// version 2 of the License, or (at your option) any later version.  //
//                                                                   //
// This library is distributed in the hope that it will be useful,   //
// but WITHOUT ANY WARRANTY; without even the implied warranty of    //
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU //
// Lesser General Public License for more details.                   //
//                                                                   //
// You should have received a copy of the GNU Lesser General Public  //
// License along with this library; if not, write to the             //
// Free Software Foundation, Inc., 59 Temple Place, Suite 330,       //
// Boston, MA  02111-1307  USA, or contact the author:               //
//                                                                   //
//                  Michel Van den Bergh  <vdbergh@luc.ac.be>        //
//                                                                   //
////////////////////end license & copyright header/////////////////////

#include <windows.h>
#include <jni.h>
#include "org_jscience_net_ntp_LocalTimeManager.h"


JNIEXPORT void JNICALL Java_org_jscience_net_ntp_LocalTimeManager_nativeSetTime
  (JNIEnv *env, jobject obj, jint year, jint month, jint day, 
  jint hour, jint minute, jint second, jint millis)
{
	SYSTEMTIME temp;
	SYSTEMTIME *lpSystemTime;
	lpSystemTime=&temp;
	lpSystemTime->wYear=year;
	lpSystemTime->wMonth=month;
	lpSystemTime->wDay=day;
	lpSystemTime->wHour=hour;
	lpSystemTime->wMinute=minute;
	lpSystemTime->wSecond=second;
	lpSystemTime->wMilliseconds=millis;
	SetSystemTime(lpSystemTime);
}
