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

import java.util.*;

/**
 *
 * @author Speedy
 */
public class QueryFormatter {
    private String name;

    public QueryFormatter(){
            name = "";
    };

    QueryFormatter(String tableName){
        name = tableName;
    };

    String getTableName(){
        return name;
    };

    void setTableName(String tableName){
        name = tableName;
    };

    String getSelectQuery(String[] fields){
        String query = "select ";
        for (String field : fields)
            query += field + ", ";
        return query.substring(0, query.length() - 2) + " from " + name;
    };

    String getSelect1FieldQuery(String field){
        return "select " + field + " from " + name;
    };

    String getSelect1FieldQuery(String field, String whereClause){
        return "select " + field + " from " + name + " where " + whereClause;
    };

    String getSelectQuery(){
        return "select * from " + name;
    };
    
    String getSelectQuery(String[] fields,
                               String whereClause){
        String query = "select ";
        for (String field : fields)  query += field + ", ";
        return query.substring(0, query.length() - 2) + " from " + name +
                " where " + whereClause;
    };

    String getSelectQuery(String whereClause){
        return "select * from " + name + " where " + whereClause;
    };

    String getUpdateQuery(HashMap<String, Object> fv_pairs, String whereClause, boolean[] INTFLAGS){
        String query = "update " + name + " set ";
        short i = 0;
        for ( String key : fv_pairs.keySet() ){
            String quote = ( ( INTFLAGS[i] ) ? "" : "'" );
            query += key + " = " + quote + fv_pairs.get(key).toString() + quote + ", ";
            i++;
        }
        return query.substring(0, query.length() - 2) + " where " + whereClause;
    };

    String getUpdateQuery(String idName, int idValue, HashMap<String, Object> fv_pairs, boolean[] INTFLAGS){
        String query = "update " + name + " set ";
        short i = 0;
        for ( String key : fv_pairs.keySet() ){
            String quote = ( ( INTFLAGS[i] ) ? "" : "'" );
            query += key + " = " + quote + fv_pairs.get(key).toString() + quote + ", ";
            i++;
        }
        String quote = ((INTFLAGS[i])?"":"'");
        return query.substring(0, query.length() - 2) + " where " + idName + " like " + quote + idValue + quote;
    };
    
    String getUpdateQuery(String idName, int idValue, HashMap<String, Object> fv_pairs){
        String query = "update " + name + " set ";
        query = fv_pairs.keySet().stream().map((key) -> key + " = '" + fv_pairs.get(key).toString() + "', ").reduce(query, String::concat);
        return query.substring(0, query.length() - 2) + " where " +
                    idName + " like '" + idValue + "'";
    };

    String getUpdateQuery(HashMap<String, Object> fv_pairs, String whereClause){
        String query = "update " + name + " set ";
        short i = 0;
        for ( String key : fv_pairs.keySet() ){
            query += key + " = '" + fv_pairs.get(key).toString() + "', ";
            i++;
        }
        return query.substring(0, query.length() - 2) + " where " + whereClause;
    };

    String getDeleteQuery(String idName, int idValue){
        return "delete from " + name + " where " +
                idName + " like '" + idValue + "'";
    };

    String getDeleteQuery(String whereClause){
        return "delete from " + name + " where " + whereClause;
    };

    String getInsertQuery(HashMap<String, Object> fv_pairs){
        String p1 = "insert into " + name + " ( ";
        String p2 = "values ( ";
        for ( String key : fv_pairs.keySet() ) { 
            p1 += " " + key + ", ";
            p2 += " '" + fv_pairs.get(key).toString() + "', ";
        }
        return p1.substring(0, p1.length() - 2) + " ) " + p2.substring(0, p2.length() - 2) + " ) ";
    };

    String getInsertQuery(String[] values){
        String query = "insert into " + name + " values ( ";
        for (String value : values) query += " '" + value + "', ";
        return query.substring(0, query.length() - 2) + ")";
    };
    
}
