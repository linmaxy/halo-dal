package unittest;

import halo.dal.DALCustomInfo;
import halo.dal.analysis.SQLStruct;
import halo.dal.analysis.def.BasicSQLInfo;
import halo.dal.analysis.def.DefSQLAnalyzer;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class SQLAnalyzerForUpperTest {

    DefSQLAnalyzer sqlAnalyzer = new DefSQLAnalyzer();

    Map<String, Object> context;

    @Before
    public void before() {
        context = new HashMap<String, Object>();
    }

    @Test
    public void delete() {
        String sql = "DELETE FROM USER WHERE UID=? AND (AGE>=? OR AGE<=?) AND (SEX=? OR SEX=?) AND TIME<=SYSDATE()";
        Object[] values = new Object[] { 1, 50, 10, 1, 2 };
        SQLStruct sqlStruct = sqlAnalyzer.parse(sql, context);
        BasicSQLInfo sqlInfo = (BasicSQLInfo) sqlAnalyzer.analyse(sql,
                sqlStruct, values, context);
        Assert.assertEquals(1, sqlStruct.getTableNames().size());
        Assert.assertEquals("USER", sqlStruct.getTableNames().get(0));
        String sql2 = sqlAnalyzer.outPutSQL(sql, sqlInfo, sqlStruct, null);
        Assert.assertEquals(sql, sql2);
        // no where
        sql = "DELETE FROM USER";
        sqlStruct = sqlAnalyzer.parse(sql, context);
        sqlInfo = (BasicSQLInfo) sqlAnalyzer.analyse(sql, sqlStruct, null,
                context);
        Assert.assertEquals(1, sqlStruct.getTableNames().size());
        Assert.assertEquals("USER", sqlStruct.getTableNames().get(0));
        sql2 = sqlAnalyzer.outPutSQL(sql, sqlInfo, sqlStruct, null);
        Assert.assertEquals(sql, sql2);
    }

    @Test
    public void delete2() {
        String sql = "DELETE FROM USER WHERE UID=? AND (AGE>=? OR AGE<=?) AND (SEX=? OR SEX=?) AND TIME<=SYSDATE()";
        Object[] values = new Object[] { 1, 50, 10, 1, 2 };
        SQLStruct sqlStruct = sqlAnalyzer.parse(sql, context);
        BasicSQLInfo sqlInfo = (BasicSQLInfo) sqlAnalyzer.analyse(sql,
                sqlStruct, values, context);
        sqlInfo.setRealTable("USER", "user1");
        Assert.assertEquals(1, sqlStruct.getTableNames().size());
        Assert.assertEquals("user1", sqlInfo.getRealTable("USER"));
        String sql2 = sqlAnalyzer.outPutSQL(sql, sqlInfo, sqlStruct, null);
        Assert.assertEquals(
                "DELETE FROM user1 WHERE UID=? AND (AGE>=? OR AGE<=?) AND (SEX=? OR SEX=?) AND TIME<=SYSDATE()",
                sql2);
        // no where
        sql = "DELETE FROM USER";
        sqlStruct = sqlAnalyzer.parse(sql, context);
        sqlInfo = (BasicSQLInfo) sqlAnalyzer.analyse(sql, sqlStruct, null,
                context);
        sqlInfo.setRealTable("USER", "user2");
        Assert.assertEquals(1, sqlStruct.getTableNames().size());
        Assert.assertEquals("USER", sqlStruct.getTableNames().get(0));
        Assert.assertEquals("user2", sqlInfo.getRealTable("USER"));
        sql2 = sqlAnalyzer.outPutSQL(sql, sqlInfo, sqlStruct, null);
        Assert.assertEquals("DELETE FROM user2", sql2);
    }

    @Test
    public void insert() {
        String sql = "INSERT INTO USER(USERID,NICKNAME,SEX) VLAUES(?,?,?)";
        Object[] values = new Object[] { 4, "jack", 22 };
        SQLStruct sqlStruct = sqlAnalyzer.parse(sql, context);
        BasicSQLInfo sqlInfo = (BasicSQLInfo) sqlAnalyzer.analyse(sql,
                sqlStruct, values, context);
        Assert.assertEquals(sqlStruct.getTableNames().size(), 1);
        Assert.assertEquals(sqlStruct.getTableNames().get(0), "USER");
        String sql2 = sqlAnalyzer.outPutSQL(sql, sqlInfo, sqlStruct, null);
        Assert.assertEquals(sql, sql2);
        Assert.assertEquals("userid",
                sqlInfo.getSQLExpressions("user.userid")[0].getColumn());
        Assert.assertEquals(4,
                sqlInfo.getSQLExpressions("user.userid")[0].getValue());
        Assert.assertEquals("nickname",
                sqlInfo.getSQLExpressions("user.nickname")[0].getColumn());
        Assert.assertEquals("jack",
                sqlInfo.getSQLExpressions("user.nickname")[0].getValue());
        Assert.assertEquals("sex",
                sqlInfo.getSQLExpressions("user.sex")[0].getColumn());
        Assert.assertEquals(22, sqlInfo.getSQLExpressions("sex")[0].getValue());
    }

    @Test
    public void insert2() {
        String sql = "INSERT INTO USER(USERID,NICKNAME,SEX) VLAUES(?,?,?)";
        Object[] values = new Object[] { 4, "jack", 22 };
        SQLStruct sqlStruct = sqlAnalyzer.parse(sql, context);
        BasicSQLInfo sqlInfo = (BasicSQLInfo) sqlAnalyzer.analyse(sql,
                sqlStruct, values, context);
        sqlInfo.setRealTable("USER", "user2");
        Assert.assertEquals(1, sqlStruct.getTableNames().size());
        Assert.assertEquals("USER", sqlStruct.getTableNames().get(0));
        Assert.assertEquals("user2", sqlInfo.getRealTable("USER"));
        String sql2 = sqlAnalyzer.outPutSQL(sql, sqlInfo, sqlStruct, null);
        Assert.assertEquals(
                "INSERT INTO user2(USERID,NICKNAME,SEX) VLAUES(?,?,?)", sql2);
        Assert.assertEquals("userid",
                sqlInfo.getSQLExpressions("user.userid")[0].getColumn());
        Assert.assertEquals(4,
                sqlInfo.getSQLExpressions("user.userid")[0].getValue());
        Assert.assertEquals("nickname",
                sqlInfo.getSQLExpressions("user.nickname")[0].getColumn());
        Assert.assertEquals("jack",
                sqlInfo.getSQLExpressions("user.nickname")[0].getValue());
        Assert.assertEquals("sex",
                sqlInfo.getSQLExpressions("user.sex")[0].getColumn());
        Assert.assertEquals(22,
                sqlInfo.getSQLExpressions("user.sex")[0].getValue());
    }

    @Test
    public void update() {
        String sql = "UPDATE USER SET UID=? , O=? WHERE NAME=?";
        Object[] values = new Object[] { 1, 3, "43" };
        SQLStruct sqlStruct = sqlAnalyzer.parse(sql, context);
        BasicSQLInfo sqlInfo = (BasicSQLInfo) sqlAnalyzer.analyse(sql,
                sqlStruct, values, context);
        Assert.assertEquals(sqlStruct.getTableNames().size(), 1);
        Assert.assertEquals(sqlStruct.getTableNames().get(0), "USER");
        String sql2 = sqlAnalyzer.outPutSQL(sql, sqlInfo, sqlStruct, null);
        Assert.assertEquals(sql, sql2);
        Assert.assertEquals(
                sqlInfo.getSQLExpressions("user.uid")[0].getValue(), 1);
        Assert.assertEquals(sqlInfo.getSQLExpressions("user.o")[0].getValue(),
                3);
        Assert.assertEquals(
                sqlInfo.getSQLExpressions("user.name")[0].getValue(), "43");
    }

    @Test
    public void update2() {
        String sql = "UPDATE USER SET UID=? , O=? WHERE NAME=?";
        Object[] values = new Object[] { 1, 3, "43" };
        SQLStruct sqlStruct = sqlAnalyzer.parse(sql, context);
        BasicSQLInfo sqlInfo = (BasicSQLInfo) sqlAnalyzer.analyse(sql,
                sqlStruct, values, context);
        sqlInfo.setRealTable("USER", "user2");
        Assert.assertEquals(1, sqlStruct.getTableNames().size());
        Assert.assertEquals("USER", sqlStruct.getTableNames().get(0));
        Assert.assertEquals("user2", sqlInfo.getRealTable("USER"));
        String sql2 = sqlAnalyzer.outPutSQL(sql, sqlInfo, sqlStruct, null);
        Assert.assertEquals("UPDATE user2 SET UID=? , O=? WHERE NAME=?", sql2);
        Assert.assertEquals(
                sqlInfo.getSQLExpressions("user.uid")[0].getValue(), 1);
        Assert.assertEquals(sqlInfo.getSQLExpressions("user.o")[0].getValue(),
                3);
        Assert.assertEquals(
                sqlInfo.getSQLExpressions("user.name")[0].getValue(), "43");
    }

    @Test
    public void select() {
        String sql = "SELECT * FROM USER U,MEMBER AS M "
                + "WHERE U.UID=M.UID AND U.SEX=? OR M.AGE>? " + "GROUP BY SEX "
                + "ORDER BY SEX " + "HAVING NAME=?";
        Object[] values = new Object[] { 1, 5, "akwei" };
        SQLStruct sqlStruct = sqlAnalyzer.parse(sql, context);
        BasicSQLInfo sqlInfo = (BasicSQLInfo) sqlAnalyzer.analyse(sql,
                sqlStruct, values, context);
        Assert.assertEquals("USER", sqlStruct.getTableNames().get(0));
        Assert.assertEquals("MEMBER", sqlStruct.getTableNames().get(1));
        Assert.assertEquals(1,
                sqlInfo.getSQLExpressions("user.sex")[0].getValue());
        Assert.assertEquals(5,
                sqlInfo.getSQLExpressions("member.age")[0].getValue());
    }

    @Test
    public void selectForIn() {
        String sql = "SELECT * FROM USER U,MEMBER AS M "
                + "WHERE U.UID=M.UID AND U.SEX=? OR M.AGE>? ID IN(1,2,3)"
                + "GROUP BY SEX " + "ORDER BY SEX " + "HAVING NAME=?";
        Object[] values = new Object[] { 1, 5, "akwei" };
        SQLStruct sqlStruct = sqlAnalyzer.parse(sql, context);
        BasicSQLInfo sqlInfo = (BasicSQLInfo) sqlAnalyzer.analyse(sql,
                sqlStruct, values, context);
        String sql2 = sqlAnalyzer.outPutSQL(sql, sqlInfo, sqlStruct, null);
        Assert.assertEquals(sql, sql2);
    }

    @Test
    public void selectNoWhere() {
        String sql = "SELECT * FROM USER";
        SQLStruct sqlStruct = sqlAnalyzer.parse(sql, context);
        BasicSQLInfo sqlInfo = (BasicSQLInfo) sqlAnalyzer.analyse(sql,
                sqlStruct, null, context);
        DALCustomInfo dalCustomInfo = new DALCustomInfo();
        dalCustomInfo.setRealTable("USER", "user2");
        String sql2 = sqlAnalyzer.outPutSQL(sql, sqlInfo, sqlStruct,
                dalCustomInfo);
        Assert.assertEquals("SELECT * FROM user2", sql2);
    }

    @Test
    public void select2() {
        String sql = "SELECT * FROM USER U,MEMBER AS M "
                + "WHERE U.UID=M.UID AND U.SEX=? OR M.AGE>? " + "GROUP BY SEX "
                + "ORDER BY SEX " + "HAVING NAME=?";
        Object[] values = new Object[] { 1, 5, "akwei" };
        SQLStruct sqlStruct = sqlAnalyzer.parse(sql, context);
        BasicSQLInfo sqlInfo = (BasicSQLInfo) sqlAnalyzer.analyse(sql,
                sqlStruct, values, context);
        sqlInfo.setRealTable("USER", "user2");
        sqlInfo.setRealTable("MEMBER", "member5");
        Assert.assertEquals("USER", sqlStruct.getTableNames().get(0));
        Assert.assertEquals("user2", sqlInfo.getRealTable("USER"));
        Assert.assertEquals("MEMBER", sqlStruct.getTableNames().get(1));
        Assert.assertEquals("member5", sqlInfo.getRealTable("MEMBER"));
        Assert.assertEquals(1,
                sqlInfo.getSQLExpressions("user.sex")[0].getValue());
        Assert.assertEquals(5,
                sqlInfo.getSQLExpressions("member.age")[0].getValue());
        String sql2 = sqlAnalyzer.outPutSQL(sql, sqlInfo, sqlStruct, null);
        Assert.assertEquals("SELECT * FROM user2 U,member5 AS M "
                + "WHERE U.UID=M.UID AND U.SEX=? OR M.AGE>? " + "GROUP BY SEX "
                + "ORDER BY SEX " + "HAVING NAME=?", sql2);
    }

    @Test
    public void selectForHibernate() {
        String sql = "SELECT GATEWAYEVE0_.ID AS ID1_, GATEWAYEVE0_.ADAPTER_ID AS ADAPTER2_1_, "
                + "GATEWAYEVE0_.ADAPTER_MEMO AS ADAPTER3_1_, GATEWAYEVE0_.ADAPTER_NAME AS ADAPTER4_1_, "
                + "GATEWAYEVE0_.CREATETIME AS CREATETIME1_, GATEWAYEVE0_.END_DATE AS END6_1_, "
                + "GATEWAYEVE0_.EVENT_ID AS EVENT7_1_, GATEWAYEVE0_.EVENT_STATUS AS EVENT8_1_, "
                + "GATEWAYEVE0_.EVENT_TYPE AS EVENT9_1_, GATEWAYEVE0_.LASTUPDTIME AS LASTUPD10_1_, "
                + "GATEWAYEVE0_.MERCHANT_ID AS MERCHANT11_1_, GATEWAYEVE0_.MERCHANT_NAME AS MERCHANT12_1_, "
                + "GATEWAYEVE0_.NAME AS NAME1_, GATEWAYEVE0_.OPRID AS OPRID1_, "
                + "GATEWAYEVE0_.START_DATE AS START15_1_ "
                + "FROM GATEWAY_EVENT GATEWAYEVE0_ "
                + "WHERE 1=1 AND GATEWAYEVE0_.EVENT_STATUS=?";
        Object[] values = new Object[] { 5 };
        SQLStruct sqlStruct = sqlAnalyzer.parse(sql, context);
        BasicSQLInfo sqlInfo = (BasicSQLInfo) sqlAnalyzer.analyse(sql,
                sqlStruct, values, context);
        DALCustomInfo dalCustomInfo = new DALCustomInfo();
        dalCustomInfo.setRealTable("GATEWAY_EVENT", "gateway_event1");
        Assert.assertEquals("GATEWAY_EVENT", sqlStruct.getTableNames().get(0));
        Assert.assertEquals(5, sqlInfo
                .getSQLExpressions("gateway_event.EVENT_STATUS")[0].getValue());
        String sql2 = sqlAnalyzer.outPutSQL(sql, sqlInfo, sqlStruct,
                dalCustomInfo);
        Assert.assertEquals(
                "SELECT GATEWAYEVE0_.ID AS ID1_, GATEWAYEVE0_.ADAPTER_ID AS ADAPTER2_1_, "
                        + "GATEWAYEVE0_.ADAPTER_MEMO AS ADAPTER3_1_, GATEWAYEVE0_.ADAPTER_NAME AS ADAPTER4_1_, "
                        + "GATEWAYEVE0_.CREATETIME AS CREATETIME1_, GATEWAYEVE0_.END_DATE AS END6_1_, "
                        + "GATEWAYEVE0_.EVENT_ID AS EVENT7_1_, GATEWAYEVE0_.EVENT_STATUS AS EVENT8_1_, "
                        + "GATEWAYEVE0_.EVENT_TYPE AS EVENT9_1_, GATEWAYEVE0_.LASTUPDTIME AS LASTUPD10_1_, "
                        + "GATEWAYEVE0_.MERCHANT_ID AS MERCHANT11_1_, GATEWAYEVE0_.MERCHANT_NAME AS MERCHANT12_1_, "
                        + "GATEWAYEVE0_.NAME AS NAME1_, GATEWAYEVE0_.OPRID AS OPRID1_, "
                        + "GATEWAYEVE0_.START_DATE AS START15_1_ "
                        + "FROM gateway_event1 GATEWAYEVE0_ "
                        + "WHERE 1=1 AND GATEWAYEVE0_.EVENT_STATUS=?", sql2);
    }
}