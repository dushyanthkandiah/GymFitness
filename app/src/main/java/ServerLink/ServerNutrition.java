package ServerLink;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import GettersAndSetters.ClassNutritions;

public class ServerNutrition {

    private Database db;
    private ClassNutritions classNutritions;
    private ArrayList<ClassNutritions> list;

    public ServerNutrition() {
        db = new Database();
        classNutritions = new ClassNutritions();
        list = new ArrayList<>();
    }

    public String getAllRecords(String searchInput, int limit) {
        String flag;

        String sql = "select * from nutrition where CONCAT(nut_id, food, type) like '%" + searchInput + "%' and shd_id = " + classNutritions.getSchdId() + " limit " + (limit*5) + ", 5";

        if (classNutritions.getSchdId() == 0)
            sql = "select * from nutrition where CONCAT(nut_id, food, type) like '%" + searchInput + "%' limit " + (limit*5) + ", 5";

//        System.out.println(sql + " ******");
        if (db.getConn() != null) {

            ResultSet rs = db.executeQuery(sql);

            try {
                flag = "nodata";
                list = new ArrayList<>();

                while (rs.next()) {
                    flag = "success";
//                    System.out.println(rs.getString("name") + "******");
                    list.add(new ClassNutritions(
                            rs.getInt("nut_id"),
                            rs.getInt("shd_id"),
                            rs.getString("food"),
                            rs.getString("type"),
                            rs.getString("status"),
                            rs.getBytes("picture")
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

    public ClassNutritions getClassNutritions() {
        return classNutritions;
    }

    public ArrayList<ClassNutritions> getList() {
        return list;
    }

}
