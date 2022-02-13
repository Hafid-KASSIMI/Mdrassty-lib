/*
 * Copyright (C) 2020 Sicut
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */


package net.mdrassty.sqlite;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Speedy
 *
 */
public class Connector {

    private String fileName;
    private java.sql.Connection mcon;
    private java.sql.Statement stmt;
    private final QueryFormatter qf;
    private boolean readyBatch;

    public Connector(String fileName) {
        this.fileName = fileName;
        readyBatch = false;
        qf = new QueryFormatter();
    }

    ;

  public Connector() {
        this.fileName = "";
        readyBatch = false;
        qf = new QueryFormatter();
    }

    ;

    public String getDatabase() {
        return fileName;
    }

    ;

    public void setDatabase(String fileName) {
        this.fileName = fileName;
    }

    ;

    public java.sql.Connection getObject() {
        return mcon;
    }

    public boolean connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            mcon = java.sql.DriverManager.getConnection("jdbc:sqlite:" + fileName);
            return true;
        } catch (ClassNotFoundException | SQLException se) {
            return false;
        }
    }

    ;

  public boolean prepareBatch() {
        try {
            stmt = mcon.createStatement();
            readyBatch = true;
        } catch (SQLException se) {
            readyBatch = false;
        }
        return readyBatch;
    }

    public boolean batch(String query) {
        if (readyBatch) {
            try {
                stmt.addBatch(query);
                return true;
            } catch (SQLException se) {
                return false;
            }
        } else {
            prepareBatch();
            return batch(query);
        }
    }

    public boolean batchUpdate(String table, HashMap<String, Object> fv_pairs, String whereClause) {
        qf.setTableName(table);
        if (readyBatch) {
            try {
                stmt.addBatch(qf.getUpdateQuery(fv_pairs, whereClause));
                return true;
            } catch (SQLException se) {
                return false;
            }
        } else {
            prepareBatch();
            return batch(qf.getUpdateQuery(fv_pairs, whereClause));
        }
    }

    public boolean batchInsert(String table, HashMap<String, Object> fv_pairs) {
        qf.setTableName(table);
        if (readyBatch) {
            try {
                stmt.addBatch(qf.getInsertQuery(fv_pairs));
                return true;
            } catch (SQLException se) {
                return false;
            }
        } else {
            prepareBatch();
            return batch(qf.getInsertQuery(fv_pairs));
        }
    }

    public boolean batchDelete(String table, String whereClause) {
        qf.setTableName(table);
        if (readyBatch) {
            try {
                stmt.addBatch(qf.getDeleteQuery(whereClause));
                return true;
            } catch (SQLException se) {
                return false;
            }
        } else {
            prepareBatch();
            return batch(qf.getDeleteQuery(whereClause));
        }
    }

    public boolean runBatch() {
        if (readyBatch) {
            try {
                stmt.executeBatch();
                stmt.clearBatch();
                readyBatch = false;
                return true;
            } catch (SQLException se) { 
            }
        }
        return false;
    }

    public boolean delete(String table, String idName, int idValue) {
        try {
            PreparedStatement ps;
            qf.setTableName(table);
            ps = mcon.prepareStatement(qf.getDeleteQuery(idName, idValue));
            ps.execute();
            return (ps.getUpdateCount() > 0);
        } catch (SQLException se) {
            return false;
        }

    }

    ;

  public boolean delete(String table, String whereClause) {
        try {
            PreparedStatement ps;
            qf.setTableName(table);
            ps = mcon.prepareStatement(qf.getDeleteQuery(whereClause));
            ps.execute();
            return (ps.getUpdateCount() > 0);
        } catch (SQLException se) {
            return false;
        }
    };

  public boolean insert(String table, String[] values) {
        try {
            PreparedStatement ps;
            qf.setTableName(table);
            ps = mcon.prepareStatement(qf.getInsertQuery(values));
            ps.execute();
            return (ps.getUpdateCount() > 0);
        } catch (SQLException se) {
            return false;
        }
    }

    ;

  public boolean insert(String table, HashMap<String, Object> fv_pairs) {
        try {
            PreparedStatement ps;
            qf.setTableName(table);
//            System.out.println(qf.getInsertQuery(fv_pairs));
            ps = mcon.prepareStatement(qf.getInsertQuery(fv_pairs));
            ps.execute();
            return (ps.getUpdateCount() > 0);
        } catch (SQLException se) {
            return false;
        }
    }

    ;

  public boolean update(String table, String idName, int idValue, HashMap<String, Object> fv_pairs) {
        try {
            PreparedStatement ps;
            qf.setTableName(table);
            ps = mcon.prepareStatement(qf.getUpdateQuery(idName, idValue, fv_pairs));
            ps.execute();
            return (ps.getUpdateCount() > 0);
        } catch (SQLException se) {
            return false;
        }
    }

    ;

  public boolean update(String table, HashMap<String, Object> fv_pairs, String whereClause, boolean[] INTFLAGS) {
        try {
            PreparedStatement ps;
            qf.setTableName(table);
            ps = mcon.prepareStatement(qf.getUpdateQuery(fv_pairs, whereClause, INTFLAGS));
            ps.execute();
            return (ps.getUpdateCount() > 0);
        } catch (SQLException se) {
            return false;
        }
    }

    ;

  public boolean update(String table, HashMap<String, Object> fv_pairs, String whereClause) {
        try {
            PreparedStatement ps;
            qf.setTableName(table);
            ps = mcon.prepareStatement(qf.getUpdateQuery(fv_pairs, whereClause));
            ps.execute();
            return (ps.getUpdateCount() > 0);
        } catch (SQLException se) {
            return false;
        }
    }

    ;

  public HashMap select(String table) {
        try {
            HashMap<String, ArrayList> data = new HashMap();
            ResultSet rs;
            qf.setTableName(table);
            rs = mcon.prepareStatement(qf.getSelectQuery()).executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int l = rsmd.getColumnCount();
            for (int i = 1; i <= l; i++) {
                data.put(rsmd.getColumnName(i), new ArrayList());
            }
            while (rs.next()) {
                for (int i = 1; i <= l; i++) {
                    data.get(rsmd.getColumnName(i)).add(rs.getObject(i));
                }
            }
            rs.close();
            return data;
        } catch (SQLException e) {
            return null;
        }
    }

    ;

  public HashMap<String, ArrayList> select(String table, String whereClause) {
        try {
            HashMap<String, ArrayList> data = new HashMap();
            ResultSet rs;
            qf.setTableName(table);
            rs = mcon.prepareStatement(qf.getSelectQuery(whereClause)).executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int l = rsmd.getColumnCount();
            for (int i = 1; i <= l; i++) {
                data.put(rsmd.getColumnName(i), new ArrayList());
            }
            while (rs.next()) {
                for (int i = 1; i <= l; i++) {
                    data.get(rsmd.getColumnName(i)).add(rs.getObject(i));
                }
            }
            rs.close();
            return data;
        } catch (SQLException e) {
            return null;
        }
    }

    ;

  public HashMap<String, ArrayList> select(String table, String[] fields, String whereClause) {
        try {
            HashMap<String, ArrayList> data = new HashMap();
            ResultSet rs;
            qf.setTableName(table);
//            System.out.println(qf.getSelectQuery(fields, whereClause));
            rs = mcon.prepareStatement(qf.getSelectQuery(fields, whereClause)).executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int l = rsmd.getColumnCount();
            for (int i = 1; i <= l; i++) {
                data.put(rsmd.getColumnName(i), new ArrayList());
            }
            while (rs.next()) {
                for (int i = 1; i <= l; i++) {
                    data.get(rsmd.getColumnName(i)).add(rs.getObject(i));
                }
            }
            rs.close();
            return data;
        } catch (SQLException e) {
            return null;
        }
    }

    ;

  public Object oneField(String table, String attribute) {
        ResultSet rs;
        try {
            Object field;
            qf.setTableName(table);
            rs = mcon.prepareStatement(qf.getSelect1FieldQuery(attribute)).executeQuery();
            rs.next();
            field = rs.getObject(1);
            rs.close();
            return field;
        } catch (SQLException e) {
            return "###";
        }
    }

    ;

  public Object oneField(String table, String attribute, String whereClause) {
        ResultSet rs;
        try {
            Object field;
            qf.setTableName(table);
            rs = mcon.prepareStatement(qf.getSelect1FieldQuery(attribute, whereClause)).executeQuery();
            rs.next();
            field = rs.getObject(1);
            rs.close();
            return field;
        } catch (SQLException e) {
            return "###";
        }
    }

    ;

  public void close() {
        try {
            mcon.close();
        } catch (SQLException ex) {
        }
    }
;
}
