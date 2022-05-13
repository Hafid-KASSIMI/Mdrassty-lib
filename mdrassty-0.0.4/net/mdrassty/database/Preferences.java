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

package net.mdrassty.database;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import net.mdrassty.sqlite.Connector;
import net.mdrassty.sqlite.Table;

/**
 *
 * @author Sicut
 */
public class Preferences implements Cloneable {
    
    private final HashMap<String, String> bundle;
    private ResourceBundle defaultProperties = null;
    private final ArrayList<String> updatedKeys, newKeys;
    private final Table t;
    
    public Preferences(String prefDb, String homeDir) {
        bundle = new HashMap();
        prepare(prefDb, homeDir);
        t = new Table();
        updatedKeys = new ArrayList();
        newKeys = new ArrayList();
        t.setName("preferences");
        t.setDatabase(prefDb);
        HashMap<String, ArrayList> res;
        res = t.select();
        for ( int i = 0, n = res.get("id").size(); i < n; i++ ) {
            bundle.put(res.get("libelle").get(i).toString(), res.get("value").get(i).toString());
        }
    }
    
    public Preferences(String prefDb, String homeDir, ResourceBundle defaultProperties) {
        this(prefDb, homeDir);
        this.defaultProperties = defaultProperties;
    }
    
    private void prepare(String prefDb, String homeDir) {
        if ( !new File(prefDb).exists() ) {
            (new File(homeDir)).mkdir();
            Connector con = new Connector();
            con.setDatabase(prefDb);
            if ( !con.connect() )   return;
            con.prepareBatch();
            con.batch("CREATE TABLE IF NOT EXISTS `preferences` ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `libelle` VARCHAR ( 128 ), `value` TEXT )");
            con.runBatch();
            con.close();
        }
    }
    
    public String get(String key) {        
        if ( !bundle.containsKey(key) ) {
            String value;
            try {
                value = defaultProperties.getString(key);
            } catch ( NullPointerException | MissingResourceException | ClassCastException e ) {
                value = "";
            }
            bundle.put(key, value);
            newKeys.add(key);
            return value;
        }
        return bundle.get(key);
    }
    
    public void update(String key, String value) {
        if ( !updatedKeys.contains(key) )
            updatedKeys.add(key);
        bundle.put(key, value);
    }
    
    public void commit() {
        if( updatedKeys.isEmpty() && newKeys.isEmpty() )
            return;
        Platform.runLater(() -> {
            t.connect();
            HashMap<String, Object> fv_pairs = new HashMap();
            t.setFv_pairs(fv_pairs);
            updatedKeys.forEach(key -> {
                fv_pairs.put("value", bundle.get(key));
                t.batchUpdate("libelle = '" + key + "'");
                fv_pairs.clear();
            });
            newKeys.forEach(key -> {
                fv_pairs.put("libelle", key);
                fv_pairs.put("value", bundle.get(key));
                t.batchInsert();
                fv_pairs.clear();
            });
            t.runBatch();
            t.close();
        });
       
    }
    
    public void commitAndExit() {
        if( updatedKeys.isEmpty() && newKeys.isEmpty() )
            return;
        Platform.runLater(() -> {
            t.connect();
            HashMap<String, Object> fv_pairs = new HashMap();
            t.setFv_pairs(fv_pairs);
            updatedKeys.forEach(key -> {
                fv_pairs.put("value", bundle.get(key));
                t.batchUpdate("libelle = '" + key + "'");
                fv_pairs.clear();
            });
            newKeys.forEach(key -> {
                fv_pairs.put("libelle", key);
                fv_pairs.put("value", bundle.get(key));
                t.batchInsert();
                fv_pairs.clear();
            });
            t.runBatch();
            t.close();
            System.exit(0);
        });
       
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public ResourceBundle getDefaultProperties() {
        return defaultProperties;
    }

    public void setDefaultProperties(ResourceBundle defaultProperties) {
        this.defaultProperties = defaultProperties;
    }
    
    
}
