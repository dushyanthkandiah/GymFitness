package ServerLink;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import GettersAndSetters.ClassSchedule;

public class ServerSchedule {

    private Database db;
    private ClassSchedule classSchedule;
    private ArrayList<ClassSchedule> list;

    public ServerSchedule() {
        db = new Database();
        classSchedule = new ClassSchedule();
        list = new ArrayList<>();
    }

    public String getAllRecords() {
        String flag;

        String sql = "select * from schedule";

        if (db.getConn() != null) {

            ResultSet rs = db.executeQuery(sql);

            try {
                flag = "nodata";
                list = new ArrayList<>();

                while (rs.next()) {
                    flag = "success";

                    list.add(new ClassSchedule(rs.getInt("shd_id"), rs.getString("type"), rs.getInt("time_period")));
                }


            } catch (SQLException e) {
                e.printStackTrace();
                flag = "nodata";
            }


        } else {
            flag = "failed";
        }

        return flag;
    }

    public String Search(String search) {
        String flag;

        String sql = "select `customers`.cus_id, `schedule`.shd_id, `schedule`.`type`, `schedule`.time_period from customers inner join `schedule` on customers.shd_id = `schedule`.shd_id where customers.email = '" + search + "'";

        if (db.getConn() != null) {

            ResultSet rs = db.executeQuery(sql);

            try {
                flag = "nodata";
                list = new ArrayList<>();

                while (rs.next()) {
                    flag = "success";

                    list.add(new ClassSchedule(rs.getInt("shd_id"), rs.getString("type"), rs.getInt("time_period")));
                }


            } catch (SQLException e) {
                e.printStackTrace();
                flag = "nodata";
            }


        } else {
            flag = "failed";
        }

        return flag;
    }

    public ClassSchedule getClassSchedule() {
        return classSchedule;
    }

    public ArrayList<ClassSchedule> getList() {
        return list;
    }
}
