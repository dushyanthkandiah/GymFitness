package OtherClasses;

import java.util.ArrayList;

import GettersAndSetters.ClassSchedule;

public class SessionData {

    public static String sheduleId = "1", cusEmail = "s";
            public static int cusId = 11;

    public static ArrayList<ClassSchedule> schedulesList;

    public static String currentFragment = "trainer";

    public static void clearEveything(){
        schedulesList = null;
    }

}
