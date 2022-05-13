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

import java.util.HashMap;

public class Table {

    protected Connector con;
    protected HashMap<String, Object> fv_pairs; // fields -> values
    protected String name, idLib;
    protected String translation = "i18n, language l, translate t";
    protected String tableJoins = "idLibelle = i18n.idLibelle and i18n.idLibelle = t.idLibelle and t.idLang = l.idLang and l.abbrev like '%lang%'";

    public Table() {
        con = new Connector();
        fv_pairs = new HashMap();
    };

    public String getDatabase() {
        return con.getDatabase();
    };

    public final void setDatabase(String fileName) {
        con.setDatabase(fileName);
    };

    public Connector getConnector() {
        return con;
    };

    public boolean delete(int idValue) {
        boolean result;
        con.connect();
        result = con.delete(name, idLib, idValue);
        con.close();
        return result;
    };

    public boolean delete(String idLib, int idValue) {
        boolean result;
        con.connect();
        result = con.delete(name, idLib, idValue);
        con.close();
        return result;
    };

    public boolean delete2(String whereClause) {
        boolean result;
        con.connect();
        result = con.delete(name, whereClause);
        con.close();
        return result;
    };

    public boolean batchDelete(String whereClause) {
        return con.batchDelete(name, whereClause);
    };

    public boolean insert() {
        boolean result;
        con.connect();
        result = con.insert(name, fv_pairs);
        con.close();
        return result;
    };

    public boolean update(int idValue) {
        boolean result;
        con.connect();
        result = con.update(name, idLib, idValue, fv_pairs);
        con.close();
        return result;
    };

    public boolean update(String idLib, int idValue) {
        boolean result;
        con.connect();
        result = con.update(name, idLib, idValue, fv_pairs);
        con.close();
        return result;
    };
    
    public boolean update2(String whereClause) {
        boolean result;
        con.connect();
        result = con.update(name, fv_pairs, whereClause);
        con.close();
        return result;
    };
    
    public void connect() {
        con.connect();
    }
    
    public boolean batchInsert() {
        return con.batchInsert(name, fv_pairs);
    }
    
    public boolean batchUpdate(String whereClause) {
        return con.batchUpdate(name, fv_pairs, whereClause);
    }
    
    public boolean runBatch() {
        return con.runBatch();
    };
    
    public void close() {
        con.close();
    }
    
    public boolean update(String whereClause, boolean[] flags) {
        boolean result;
        con.connect();
        result = con.update(name, fv_pairs, whereClause, flags);
        con.close();
        return result;
    };

    public HashMap select() {
        HashMap hm;
        con.connect();
        hm = con.select(name);
        con.close();
        return hm;
    };

    public HashMap select(String whereClause) {
        HashMap hm;
        con.connect();
        hm = con.select(name, whereClause);
        con.close();
        return hm;
    };

    public HashMap select(String[] fields, String whereClause) {
        HashMap hm;
        con.connect();
        hm = con.select(name, fields, whereClause);
        con.close();
        return hm;
    };

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getTableJoins() {
        return tableJoins;
    }

    public void setTableJoins(String tableJoins) {
        this.tableJoins = tableJoins;
    }

    public void setTableJoinsLanguage(String language) {
        tableJoins = tableJoins.replace("%lang%", language);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    // when you want just to get translated libelles
    public void useTranslateJoins() {
        name = translation;
        tableJoins = tableJoins.replace("idLibelle = i18n.idLibelle and ", "");
    }

    public void setFv_pairs(HashMap<String, Object> fv_pairs) {
        this.fv_pairs = fv_pairs;
    }
    

}
