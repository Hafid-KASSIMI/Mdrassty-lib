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
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;
import net.mdrassty.sqlite.Table;

/**
 *
 * @author Sicut
 */
public class Translator extends ResourceBundle {
    
    private final HashMap<String, String> bundle;
    private final String i18nDbPath;
    private String lang;
    
    public Translator(String lang, String i18nDbPath) {
        super();
        bundle = new HashMap();
        this.lang = lang;
        this.i18nDbPath = i18nDbPath;
        getKeys();
    }
    
    @Override
    protected Object handleGetObject(String key) {
        return bundle.get(key);
    }

    @Override
    public Enumeration<String> getKeys() {
        HashMap<String, ArrayList> res;
        Table t = new Table();
        String[] fields = {"i18n.libelle lib", "synonym syn"};
        t.useTranslateJoins();
        t.setTableJoinsLanguage(lang);
        t.setDatabase(i18nDbPath);
        res = t.select(fields, t.getTableJoins());
        for ( int i = 0, n = res.get("lib").size(); i < n; i++ ) {
            bundle.put(res.get("lib").get(i).toString(), res.get("syn").get(i).toString());
        }
        return Collections.enumeration(bundle.keySet());
    }
    
    public void translate(String lang) {
        if ( this.lang.equals(lang) )
            return;
        HashMap<String, ArrayList> res;
        Table t = new Table();
        String[] fields = {"i18n.libelle lib", "synonym syn"};
        this.lang = lang;
        t.useTranslateJoins();
        t.setTableJoinsLanguage(lang);
        t.setDatabase(i18nDbPath);
        res = t.select(fields, t.getTableJoins());
        bundle.clear();
        for ( int i = 0, n = res.get("lib").size(); i < n; i++ )
            bundle.put(res.get("lib").get(i).toString(), res.get("syn").get(i).toString());
    }
}
