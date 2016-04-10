package com.yoyoyee.zerodistance.app;

public class AppConfig {
	// Server user login url
	public static String URL_LOGIN = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/login";

	// Server user register url
	public static String URL_STUDENT_REGISTER = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/register/studentRegister";

	// Server user register url
	public static String URL_TEACHER_REGISTER = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/register/teacherRegister";

	// Server schools url
	public static String URL_SCHOOLS = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/schools";

	// Server publish missions url
	public static String URL_PUBLISH_MISSION = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/publish/publishMission";

    // Server publish missions QA url
    public static String URL_PUBLISH_MISSION_QA = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/publish/publishMissionQA";

	// Server publish groups url
	public static String URL_PUBLISH_GROUP = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/publish/publishGroup";

    // Server publish groups QA url
    public static String URL_PUBLISH_GROUP_QA = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/publish/publishGroupQA";

	// Server get missions url
	public static String URL_GET_USER_MISSIONS = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/update/getUserMissions";

	// Server get groups url
	public static String URL_GET_USER_GROUPS = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/update/getUserGroups";

	// Server get mission qa url
	public static String URL_GET_MISSION_QA = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/update/getMissionQA";

	// Server get group qa url
	public static String URL_GET_GROUP_QA = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/update/getGroupQA";

	// Server publish QA answer url
	public static String URL_PUBLISH_QA_ANSWER = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/publish/publishQAAnswer";

}
