package ServerLink;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import GettersAndSetters.ClassCustomers;
import OtherClasses.SessionData;

public class ServerCustomer {
    private Database db;
    private ClassCustomers classCustomers;

    public ServerCustomer() {
        db = new Database();
        classCustomers = new ClassCustomers();
    }

    public String Search() {
        String flag;

        String sql = "select * from customers where email = '" + classCustomers.getEmail() + "' and status != 'delete'";

        if (db.getConn() != null) {

            ResultSet rs = db.executeQuery(sql);

            try {
                flag = "nodata";
                while (rs.next()) {
                    flag = "success";
                    classCustomers.setId(rs.getInt("cus_id"));
                    classCustomers.setTrainId(rs.getInt("train_id"));
                    classCustomers.setName(rs.getString("name"));
                    classCustomers.setDob(rs.getString("dob"));
                    classCustomers.setAddress(rs.getString("address"));
                    classCustomers.setGender(rs.getString("gender"));
                    classCustomers.setEmail(rs.getString("email"));
                    classCustomers.setPhone(rs.getString("phone"));
                    classCustomers.setHeight(rs.getDouble("height"));
                    classCustomers.setWeight(rs.getDouble("weight"));
                    classCustomers.setPassword(rs.getString("password"));
                    classCustomers.setPicture(rs.getBytes("picture"));
                    classCustomers.setStatus(rs.getString("status"));
                    classCustomers.setSchdId(rs.getInt("shd_id"));

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

    public int Save() {
        int result = 0;
        PreparedStatement st;

        try {

            st = db.executeUpdate("INSERT INTO customers " +
                    "(name, " +
                    "dob, " +
                    "address, " +
                    "gender, " +
                    "email, " +
                    "phone, " +
                    "height, " +
                    "weight, " +
                    "password, " +
                    "status, " +
                    "picture, " +
                    "shd_id) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, 'active', ?, ?)");

            st.setString(1, classCustomers.getName());
            st.setString(2, classCustomers.getDob());
            st.setString(3, classCustomers.getAddress());
            st.setString(4, classCustomers.getGender());
            st.setString(5, classCustomers.getEmail());
            st.setString(6, classCustomers.getPhone());
            st.setString(7, String.valueOf(classCustomers.getHeight()));
            st.setString(8, String.valueOf(classCustomers.getWeight()));
            st.setString(9, classCustomers.getPassword());
            st.setBytes(10, classCustomers.getPicture());
            st.setInt(11, classCustomers.getSchdId());
//            System.out.println(st);

            result = st.executeUpdate();
            st.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public int Update() {
        int result = 0;


        PreparedStatement st = db.executeUpdate("UPDATE customers SET " +
                "name = ?, " +
                "dob = ?, " +
                "address = ?, " +
                "gender = ?, " +
                "phone = ?, " +
                "height = ?, " +
                "weight = ?, " +
                "picture = ?, " +
                "shd_id = ? " +
                "WHERE email = ?");
        try {

            st.setString(1, classCustomers.getName());
            st.setString(2, classCustomers.getDob());
            st.setString(3, classCustomers.getAddress());
            st.setString(4, classCustomers.getGender());
            st.setString(5, classCustomers.getPhone());
            st.setString(6, String.valueOf(classCustomers.getHeight()));
            st.setString(7, String.valueOf(classCustomers.getWeight()));
            st.setBytes(8, classCustomers.getPicture());
            st.setInt(9, classCustomers.getSchdId());
            st.setString(10, classCustomers.getEmail());
//            System.out.println(classCustomers.getSchdId() + "**************");

            result = st.executeUpdate();
                      st.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }



        return result;
    }

    public int SelectTrainer() {
        int result = 0;


        PreparedStatement st = db.executeUpdate("UPDATE customers SET " +
                "train_id = ? " +
                "WHERE email = ?");
        try {

            st.setInt(1, classCustomers.getTrainId());
            st.setString(2, SessionData.cusEmail);
//            System.out.println(classCustomers.getSchdId() + "**************");

            result = st.executeUpdate();
            st.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }



        return result;
    }


    public ClassCustomers getClassCustomers() {
        return classCustomers;
    }

    public void setClassCustomers(ClassCustomers classCustomers) {
        this.classCustomers = classCustomers;
    }
}
