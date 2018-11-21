package ServerLink;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import GettersAndSetters.ClassExercise;
import GettersAndSetters.ClassNutritions;

public class ServerExercise {

    private Database db;
    private ClassExercise classExercise;
    private ArrayList<ClassExercise> list;

    public ServerExercise() {
        db = new Database();
        classExercise = new ClassExercise();
        list = new ArrayList<>();
    }

    public String getAllRecords(String searchInput, int limit) {
        String flag;

        String sql = "select * from exercise where status = 'active' and CONCAT(exc_id, name) like '%" + searchInput + "%' and shd_id = " + classExercise.getSchdId() + " limit " + (limit*15) + ", 15";

        if (classExercise.getSchdId() == 0)
            sql = "select * from exercise where status = 'active' and CONCAT(exc_id, name) like '%" + searchInput + "%' limit " + (limit*15) + ", 15";

//        System.out.println(sql + " ******");
        if (db.getConn() != null) {

            ResultSet rs = db.executeQuery(sql);

            try {
                flag = "nodata";
                list = new ArrayList<>();

                while (rs.next()) {
                    flag = "success";
//                    System.out.println(rs.getString("name") + "******");
                    list.add(new ClassExercise(
                            rs.getInt("exc_id"),
                            rs.getInt("shd_id"),
                            rs.getInt("sets"),
                            rs.getInt("times"),
                            rs.getString("name")
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

    public ClassExercise getClassExercise() {
        return classExercise;
    }

    public ArrayList<ClassExercise> getList() {
        return list;
    }
}
