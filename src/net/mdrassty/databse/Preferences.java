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

import java.util.ArrayList;
import java.util.HashMap;
import javafx.application.Platform;
import net.mdrassty.sqlite.Table;

/**
 *
 * @author Sicut
 */
public class Preferences implements Cloneable {
    
    private final HashMap<String, String> bundle;
    private final ArrayList<String> updatedKeys;
    private final Table t;
    
    public Preferences(String prefDb) {
        bundle = new HashMap();
        t = new Table();
        updatedKeys = new ArrayList();
        t.setName("preferences");
        t.setDatabase(prefDb);
        HashMap<String, ArrayList> res;
        res = t.select();
        for ( int i = 0, n = res.get("id").size(); i < n; i++ ) {
            bundle.put(res.get("libelle").get(i).toString(), res.get("value").get(i).toString());
        }
    }
    
    public String get(String key) {        
        if ( !bundle.containsKey(key) ) {
            if ( insert(key, "") ) {
                bundle.put(key, "");
                updatedKeys.add(key);
            }
        }
        return bundle.get(key);
    }
    
    private boolean insert(String key, String value) {
        HashMap<String, Object> fv_pairs = new HashMap();
        fv_pairs.put("value", value);
        fv_pairs.put("libelle", key);
        t.setFv_pairs(fv_pairs);
        return t.insert();
    }
    
    public void update(String key, String value) {
        if ( !updatedKeys.contains(key) )
            updatedKeys.add(key);
        bundle.put(key, value);
    }
    
    public void commit() {
        if( updatedKeys.isEmpty() )
            return;
        Platform.runLater(() -> {
            t.connect();
            HashMap<String, Object> fv_pairs = new HashMap();
            t.setFv_pairs(fv_pairs);
            updatedKeys.forEach(key -> {
                fv_pairs.put("value", bundle.get(key));
                t.batchUpdate("libelle = '" + key + "'");
            });
            t.runBatch();
            t.close();
        });
       
    }
    
    public void commitAndExit() {
        if( updatedKeys.isEmpty() )
            return;
        Platform.runLater(() -> {
            t.connect();
            HashMap<String, Object> fv_pairs = new HashMap();
            t.setFv_pairs(fv_pairs);
            for ( String key : updatedKeys ) {
                fv_pairs.put("value", bundle.get(key));
                t.batchUpdate("libelle = '" + key + "'");
            }
            t.runBatch();
            t.close();
            System.exit(0);
        });
       
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    
}
