/** written by
    Seth Roffe

    Spell Class for Dungeons and Dragons 5e
*/

package dnd;

import java.util.*;
import java.io.*;

public class Spell {
    private String name;
    private ArrayList<String> desc;
    private String range;
    private ArrayList<String> components;
    private String ritual;
    private String duration;
    private String concentration;
    private String casting_time;
    private int level;
    private String school;
    private HashSet<String> clss;
    private HashSet<String> subclss;
    
    // Constructors
    public Spell(String name, ArrayList<String> desc, String range, ArrayList<String> components, String ritual, String duration,
            String concentration, String casting_time, int level, String school, HashSet<String> clss, HashSet<String> subclss) {
        setName(name);
        setDesc(desc);
        setRange(range);
        setComponents(components);
        setRitual(ritual);
        setDuration(duration);
        setConcentration(concentration);
        setCastingTime(casting_time);
        setLevel(level);
        setSchool(school);
        setClss(clss);
        setSubClss(subclss);
    }

    public Spell(String name) {
        setName(name);
        setDesc(new ArrayList<String>());
        setSchool("");
        setLevel(0);
        setRitual("");
        setCastingTime("");
        setClss(new HashSet<String>());
        setSubClss(new HashSet<String>());
        setRange("");
        setComponents(new ArrayList<String>());
        setDuration("");
        setConcentration("");
    }

    public Spell() {
        setSchool("");
        setDesc(new ArrayList<String>());
        setName("");
        setLevel(0);
        setRitual("");
        setCastingTime("");
        setClss(new HashSet<String>());
        setSubClss(new HashSet<String>());
        setRange("");
        setComponents(new ArrayList<String>());
        setDuration("");
        setConcentration("");
    }
    public Spell(Spell s) {
        setSchool(s.getSchool());
        setDesc(s.getDesc());
        setName(s.getName());
        setLevel(s.getLevel());
        setRitual(s.getRitual());
        setCastingTime(s.getCastingTime());
        setClss(s.getClss());
        setSubClss(s.getSubClss());
        setRange(s.getRange());
        setComponents(s.getComponents());
        setDuration(s.getDuration());
        setConcentration(s.getConcentration());
    }

    // Set and Get Methods
    public void setSchool(String s) {
        this.school = s;
    }
    public String getSchool() {
        return school;
    }

    public void setName(String n) {
        this.name = n;
    }
    public String getName() {
        return name;
    }

    public void setLevel(int l) {
        this.level = l;
    }
    public int getLevel() {
        return level;
    }

    public void setRitual(String r) {
        this.ritual = r;
    }
    public String getRitual() {
        return ritual;
    }
    public boolean getRitualTruth() {
        if (this.ritual.equalsIgnoreCase("yes")) {
            return true;
        }
        else {
            return false;
        }
    }

    public void setCastingTime(String ct) {
        this.casting_time = ct;
    }
    public String getCastingTime() {
        return casting_time;
    }

    public void setClss(HashSet<String> c) {
        this.clss = c;
    }
    public HashSet<String> getClss() {
        return clss;
    }

    public void setSubClss(HashSet<String> c) {
        this.subclss = c;
    }
    public HashSet<String> getSubClss() {
        return subclss;
    }

    public void setRange(String r) {
        this.range = r;
    }
    public String getRange() {
        return range;
    }

    public void setComponents(ArrayList<String> c) {
        this.components = c;
    }
    public ArrayList<String> getComponents() {
        return components;
    }

    public void setDuration(String d) {
        this.duration = d;
    }
    public String getDuration() {
        return duration;
    }

    public void setConcentration(String c) {
        this.concentration = c;
    }
    public String getConcentration() {
        return concentration;
    }
    public boolean getConcentrationTruth() {
        if (this.concentration.equalsIgnoreCase("yes")) {
            return true;
        }
        else {
            return false;
        }
    }

    public void setDesc(ArrayList<String> d) {
        this.desc = d;
    }
    public ArrayList<String> getDesc() {
        return desc;
    }

    public String toString() {
        return name;
    }
}
