package ServerLink;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import GettersAndSetters.ClassSchedule;
import GettersAndSetters.ClassTrainers;
import OtherClasses.SessionData;

public class ServerTrainers {

    private Database db;
    private ClassTrainers classTrainers;
    private ArrayList<ClassTrainers> list;

    public ServerTrainers() {
        db = new Database();
        classTrainers = new ClassTrainers();
        list = new ArrayList<>();
    }

    public String getAllRecords(String searchInput, int limit) {
        String flag;

        String sql = "select * from trainers where status = 'active' and CONCAT(train_id, name, phone) like '%" + searchInput + "%' and shd_id = " + classTrainers.getSchdId() + " limit " + (limit*10) + ", 10";

        if (classTrainers.getSchdId() == 0)
            sql = "select * from trainers where status = 'active' and CONCAT(train_id, name, phone) like '%" + searchInput + "%' limit " + (limit*10) + ", 10";

//        System.out.println(sql + " ******");
        if (db.getConn() != null) {

            ResultSet rs = db.executeQuery(sql);

            try {
                flag = "nodata";
                list = new ArrayList<>();

                while (rs.next()) {
                    flag = "success";
//                    System.out.println(rs.getString("name") + "******");
                    list.add(new ClassTrainers(
                            rs.getInt("train_id"),
                            rs.getString("name"),
                            rs.getString("dob"),
                            rs.getString("address"),
                            rs.getString("gender"),
                            rs.getString("phone"),
                            rs.getString("experience"),
                            rs.getBytes("picture"),
                            rs.getString("status"),
                            rs.getInt("shd_id")
                    ));
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

    public String Search() {
        String flag;

        String sql = "select * from trainers where status = 'active' and train_id = " + classTrainers.getId() + "";

        System.out.println(sql + " ******");

        if (db.getConn() != null) {

            ResultSet rs = db.executeQuery(sql);

            try {
                flag = "nodata";
                list = new ArrayList<>();

                if (rs.next()) {
                    flag = "success";
//                    System.out.println(rs.getString("name") + "******");
                    ClassTrainers classTrainers = new ClassTrainers(
                            rs.getInt("train_id"),
                            rs.getString("name"),
                            rs.getString("dob"),
                            rs.getString("address"),
                            rs.getString("gender"),
                            rs.getString("phone"),
                            rs.getString("experience"),
                            rs.getBytes("picture"),
                            rs.getString("status"),
                            rs.getInt("shd_id")
                    );

                    this.classTrainers = classTrainers;
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



    public ClassTrainers getClassTrainers() {
        return classTrainers;
    }

    public ArrayList<ClassTrainers> getList() {
        return list;
    }
}
