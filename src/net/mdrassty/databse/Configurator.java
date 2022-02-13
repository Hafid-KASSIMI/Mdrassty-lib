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

package net.mdrassty.databse;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import net.mdrassty.sqlite.Connector;

/**
 *
 * @author Sicut
 */
public final class Configurator {
    private final Connector con;
    
    public Configurator() {
        con = new Connector();
    }
    
    public Configurator(Connector con) {
        this.con = con;
    }
    /**
    * This method search for preferences and properties saved previously.
    * If nothing's found, it creates properties and preferences dbs with some default values.
    * @param prefDb The preferences sqlite database path.
    * @param homeDir The preferences and properties sqlite databases parent folder path.
    * @param prefSql The sql source code that is used to create the preferences database.
    */
    public void prepare(String prefDb, String homeDir, InputStream prefSql) {
        File db = new File(prefDb);
        if ( !db.exists() || db.length() == 0 ) {
            (new File(homeDir)).mkdir();
            con.setDatabase(prefDb);
            createDB(prefSql);
        }
    }
    
    public void prepareData(String database, InputStream sql) {
        File db = new File(database);
        if ( !db.exists() || db.length() == 0 ) {
            con.setDatabase(database);
            createDB(sql);
        }
    }
    
    private void createDB(InputStream src) {
        String crQuery = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(src, java.nio.charset.StandardCharsets.UTF_8));
            if ( !con.connect() )   return;
            String line;
            con.prepareBatch();
            while ((line = br.readLine()) != null) {
                if ( line.trim().startsWith("--"))
                    continue;
                crQuery += line;
                if ( line.trim().endsWith(";") ) {
                    con.batch(crQuery);
                    crQuery = "";
                }
            }
            con.runBatch();
            con.close();
        } 
        catch (IOException ex) { }
    }
}
