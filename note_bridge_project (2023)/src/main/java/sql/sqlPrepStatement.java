package sql;

public class sqlPrepStatement {

    public final String searchForName = "SELECT * " +
                                        "FROM person p " +
                                        "WHERE ? LIKE p.first_name OR ? LIKE p.surname";

    public final String getAddressWithName= "SELECT p.first_name , p.surname , a.house_nr ," +
                                            " a.postal_code , a.street , a.city " +
                                            "FROM person p , address  a" +
                                            "WHERE (? LIKE p.first_name OR ? LIKE p.surname) " +
                                            "AND (a.house_nr = p.house_nr AND a.postal_code = p.house_nr)";

    public final String getTeacherSchedule= "SELECT *" +
                                            "FROM schedule s , teacher t , person p " +
                                            "WHERE (? LIKE p.first_name OR ? LIKE p.surname) " +
                                            "AND (p.pid = t.pid AND s.teacher = t.tid)";
}
