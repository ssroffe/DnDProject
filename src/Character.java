/** written by
    Seth Roffe

    Character class for a Dungeons and dragons game

    v1.0: Basic stuff. character stats 
*/

package dnd;

import java.util.*;
import java.io.*;
import dnd.Spell;
import com.google.gson.Gson;

public class Character {
    //about character
    private String name;
    private String race;
    private String subrace;
    private String clss; //class
    private String subClss;
    private char gender;
    private ArrayList<String> weapons;
    private String background;

    //health and magic
	private int maxHP;
    private int currentHP;
    private int tempHP;

    private int level;
    private int exp;

    //stats
    private int armor;
    private int initiative;
    private int speed;
    private int proficiencyBonus;
    private int inspiration;
    private String hitdice;
    
    private int strength;
    private int constitution;
    private int dexterity;
    private int intelligence;
    private int wisdom;
    private int charisma;

    private double gold;

    private int deathRoll;
    private int saveRoll;
    

    //Spells
    private HashSet<Spell> cantrips;
    private HashSet<Spell> spells1;
    private int spellSlots1;
    private HashSet<Spell> spells2;
    private int spellSlots2;
    private HashSet<Spell> spells3;
    private int spellSlots3;
    private HashSet<Spell> spells4;
    private int spellSlots4;
    private HashSet<Spell> spells5;
    private int spellSlots5;
    private HashSet<Spell> spells6;
    private int spellSlots6;
    private HashSet<Spell> spells7;
    private int spellSlots7;
    private HashSet<Spell> spells8;
    private int spellSlots8;
    private HashSet<Spell> spells9;
    private int spellSlots9;


    private HashSet<String> ideals;
    private HashSet<String> bonds;
    private HashSet<String> flaws;
    private HashSet<String> languages;
    
    private HashSet<String> features;

    private ArrayList<String> inventory;
    private boolean[] proficiencies;
    private boolean[] savingThrows;
    
    private String description;
    private String notes;

    //////////////////////////
    /////// INSTANCES ////////
    //////////////////////////
    
    public Character(String name, String clss, String race, String subrace, char gender,
		     ArrayList<String> weapons, HashSet<Spell> cantrips, 
		     int maxHealth, int armor, int initiative, int speed, String hitdice,
		     int str, int cons, int dex, int intel, int wis, int charis, int level, int exp,
             HashSet<Spell> spells1,HashSet<Spell> spells2,HashSet<Spell> spells3,HashSet<Spell> spells4,
             HashSet<Spell> spells5,HashSet<Spell> spells6,HashSet<Spell> spells7,HashSet<Spell> spells8,
             HashSet<Spell> spells9,
             int spellSlots1,int spellSlots2,int spellSlots3,int spellSlots4,
             int spellSlots5,int spellSlots6,int spellSlots7,int spellSlots8, int spellSlots9,
		     int dRoll, int sRoll, HashSet<String> bonds, String background,
		     ArrayList<String> inventory, HashSet<String> features, int gold) {
	
	setName(name);
	setClss(clss);
	setRace(race);
	setSubrace(subrace);
	setGender(gender);
	setWeapons(weapons);
	setCantrips(cantrips);
	setMaxHP(maxHealth);
	setArmor(armor);
    setBackground(background);
	setInitiative(initiative);
	setSpeed(speed);
	setStr(str);
	setCons(cons);
	setDex(dex);
	setInt(intel);
	setWis(wis);
	setChar(charis);
	setInventory(inventory);
	setCurrentHP(maxHealth);
	setHitDice(hitdice);

    setSpells1(spells1);
    setSpells2(spells2);
    setSpells3(spells3);
    setSpells4(spells4);
    setSpells5(spells5);
    setSpells6(spells6);
    setSpells7(spells7);
    setSpells8(spells8);
    setSpells9(spells9);

    setSpellSlots1(spellSlots1);
    setSpellSlots2(spellSlots2);
    setSpellSlots3(spellSlots3);
    setSpellSlots4(spellSlots4);
    setSpellSlots5(spellSlots5);
    setSpellSlots6(spellSlots6);
    setSpellSlots7(spellSlots7);
    setSpellSlots8(spellSlots8);
    setSpellSlots9(spellSlots9);

    setSaveRoll(sRoll);
    setDeathRoll(dRoll);

    setBonds(bonds);

	setLevel(level);
	setExp(exp);

	setGold(gold);

	setFeatures(features);
    }
    
    public Character(String name) {
	setName(name);
	setClss("");
    setSubClss("");
    setBackground("");
	
	setStr(0);
	setCons(0);
	setDex(0);
	setInt(0);
	setWis(0);
	setChar(0);

	setInitiative(0);
	setArmor(0);
	setSpeed(0);
	
	setMaxHP(0);

    setTempHP(0);

	setHitDice("0");
    setInspiration(0);
    setProficiencyBonus(0);
	setGold(0);
	
	setRace("");
	setSubrace("");
	setGender('m');
	setWeapons(new ArrayList<String>());


	setCantrips(new HashSet<Spell>());
	setFeatures(new HashSet<String>());
	setBonds(new HashSet<String>());
	setFlaws(new HashSet<String>());
	setIdeals(new HashSet<String>());
	setDescription("");
	setLanguages(new HashSet<String>());

	setInventory(new ArrayList<String>());

	setSpells1(new HashSet<Spell>());
	setSpells2(new HashSet<Spell>());
	setSpells3(new HashSet<Spell>());
	setSpells4(new HashSet<Spell>());
	setSpells5(new HashSet<Spell>());
	setSpells6(new HashSet<Spell>());
	setSpells7(new HashSet<Spell>());
	setSpells8(new HashSet<Spell>());
	setSpells9(new HashSet<Spell>());

    setSpellSlots1(0);
    setSpellSlots2(0);
    setSpellSlots3(0);
    setSpellSlots4(0);
    setSpellSlots5(0);
    setSpellSlots6(0);
    setSpellSlots7(0);
    setSpellSlots8(0);
    setSpellSlots9(0);
    setProficiencies(new boolean[18]);
    setSavingThrows(new boolean[6]);
	setLevel(1);
	setExp(0);
    
    setSaveRoll(0);
    setDeathRoll(0);
	
	setCurrentHP(0);
	
    }

public Character() {
	setName("");
	setClss("");
    setSubClss("");
    setBackground("");
	
	setStr(0);
	setCons(0);
	setDex(0);
	setInt(0);
	setWis(0);
	setChar(0);

	setInitiative(0);
    setProficiencyBonus(0);
    setInspiration(0);
	setArmor(0);
	setSpeed(0);
	
	setMaxHP(0);
    setTempHP(0);

	setHitDice("0");
	setGold(0);
	
	setRace("");
	setSubrace("");
	setGender('m');
	setWeapons(new ArrayList<String>());


	setCantrips(new HashSet<Spell>());
	setBonds(new HashSet<String>());
	setFeatures(new HashSet<String>());
	setFlaws(new HashSet<String>());
	setIdeals(new HashSet<String>());
	setDescription("");
    setNotes("");
	setLanguages(new HashSet<String>());

	setInventory(new ArrayList<String>());
    setProficiencies(new boolean[18]);
    setSavingThrows(new boolean[6]);

	setSpells1(new HashSet<Spell>());
	setSpells2(new HashSet<Spell>());
	setSpells3(new HashSet<Spell>());
	setSpells4(new HashSet<Spell>());
	setSpells5(new HashSet<Spell>());
	setSpells6(new HashSet<Spell>());
	setSpells7(new HashSet<Spell>());
	setSpells8(new HashSet<Spell>());
	setSpells9(new HashSet<Spell>());

    setSpellSlots1(0);
    setSpellSlots2(0);
    setSpellSlots3(0);
    setSpellSlots4(0);
    setSpellSlots5(0);
    setSpellSlots6(0);
    setSpellSlots7(0);
    setSpellSlots8(0);
    setSpellSlots9(0);

	setLevel(1);
	setExp(0);

    setSaveRoll(0);
    setDeathRoll(0);
	
    setCurrentHP(0);
	
    }

    //just a name
    public Character(String name,String clss) {
	setName(name);
	setClss(clss);
    setSubClss("");
    setBackground("");
	
	setStr(0);
	setCons(0);
	setDex(0);
	setInt(0);
	setWis(0);
	setChar(0);

	setInitiative(0);
    setProficiencyBonus(0);
    setInspiration(0);
	setArmor(0);
	setSpeed(0);

	setHitDice("0");
	setLevel(1);
	setExp(0);
	setGold(0);
	
	setMaxHP(0);
    setTempHP(0);

	setRace("");
	setSubrace("");
	setGender('m');
	setWeapons(new ArrayList<String>());

    setProficiencies(new boolean[18]);
    setSavingThrows(new boolean[6]);
	
	setCantrips(new HashSet<Spell>());
	setBonds(new HashSet<String>());
	setFeatures(new HashSet<String>());
	setFlaws(new HashSet<String>());
	setIdeals(new HashSet<String>());
	setDescription("");
    setNotes("");
	setLanguages(new HashSet<String>());

	setSpells1(new HashSet<Spell>());
	setSpells2(new HashSet<Spell>());
	setSpells3(new HashSet<Spell>());
	setSpells4(new HashSet<Spell>());
	setSpells5(new HashSet<Spell>());
	setSpells6(new HashSet<Spell>());
	setSpells7(new HashSet<Spell>());
	setSpells8(new HashSet<Spell>());
	setSpells9(new HashSet<Spell>());

    setSpellSlots1(0);
    setSpellSlots2(0);
    setSpellSlots3(0);
    setSpellSlots4(0);
    setSpellSlots5(0);
    setSpellSlots6(0);
    setSpellSlots7(0);
    setSpellSlots8(0);
    setSpellSlots9(0);

    setSaveRoll(0);
    setDeathRoll(0);

    setInventory(new ArrayList<String>());

	setCurrentHP(0);
    }

    // Copy a character instance
    public Character(Character c) {
        setName(c.getName());
        setRace(c.getRace());
        setBackground(c.getBackground());
        setClss(c.getClss());
        setSubClss(c.getSubClss());
        setSubrace(c.getSubrace());
        setGender(c.getGender());
        setWeapons(c.getWeapons());
        setArmor(c.getArmor());
        setCantrips(c.getCantrips());
        setMaxHP(c.getMaxHP());
        setTempHP(c.getTempHP());
        setInitiative(c.getInitiative());
        setSpeed(c.getSpeed());
        setHitDice(c.getHitDice());
        setStr(c.getStr());
        setCons(c.getCons());
        setDex(c.getDex());
        setInt(c.getInt());
        setWis(c.getWis());
        setChar(c.getChar());
        setLevel(c.getLevel());
        setExp(c.getExp());
        setInventory(c.getInventory());
        setLanguages(c.getLanguages());
        setProficiencyBonus(c.getProficiencyBonus());
        setInspiration(c.getInspiration());
        setProficiencies(c.getProficiencies());
        setSavingThrows(c.getSavingThrows());


        setSpells1(c.getSpells1());
        setSpells2(c.getSpells2());
        setSpells3(c.getSpells3());
        setSpells4(c.getSpells4());
        setSpells5(c.getSpells5());
        setSpells6(c.getSpells6());
        setSpells7(c.getSpells7());
        setSpells8(c.getSpells8());
        setSpells9(c.getSpells9());

        setSpellSlots1(c.getSpellSlots1());
        setSpellSlots2(c.getSpellSlots2());
        setSpellSlots3(c.getSpellSlots3());
        setSpellSlots4(c.getSpellSlots4());
        setSpellSlots5(c.getSpellSlots5());
        setSpellSlots6(c.getSpellSlots6());
        setSpellSlots7(c.getSpellSlots7());
        setSpellSlots8(c.getSpellSlots8());
        setSpellSlots9(c.getSpellSlots9());

        setSaveRoll(c.getSaveRoll());
        setDeathRoll(c.getDeathRoll());

        setBonds(c.getBonds());
        setFlaws(c.getFlaws());
        setIdeals(c.getIdeals());
        setDescription(c.getDescription());
        setNotes(c.getNotes());
        setCurrentHP(c.getCurrentHP());
        setGold(c.getGold());
        setFeatures(c.getFeatures());
    }
	
    public Character(String name, String clss, String background, int maxHealth, int armor, int initiative, int speed,
		     int str, int cons, int dex, int intel, int wis, int charis) {
	setName(name);
	setClss(clss);
    setSubClss("");
    setBackground(background);
	setMaxHP(maxHealth);
	setArmor(armor);
	setInitiative(initiative);
	setSpeed(speed);
	setStr(str);
	setCons(cons);
	setDex(dex);
	setInt(intel);
	setWis(wis);
	setChar(charis);

	setRace("");
	setSubrace("");
	setGender('m');
	
	setCantrips(new HashSet<Spell>());
	setBonds(new HashSet<String>());
	setFeatures(new HashSet<String>());
	setLanguages(new HashSet<String>());

    setSpells1(new HashSet<Spell>());
	setSpells2(new HashSet<Spell>());
	setSpells3(new HashSet<Spell>());
	setSpells4(new HashSet<Spell>());
	setSpells5(new HashSet<Spell>());
	setSpells6(new HashSet<Spell>());
	setSpells7(new HashSet<Spell>());
	setSpells8(new HashSet<Spell>());
	setSpells9(new HashSet<Spell>());

    setSpellSlots1(0);
    setSpellSlots2(0);
    setSpellSlots3(0);
    setSpellSlots4(0);
    setSpellSlots5(0);
    setSpellSlots6(0);
    setSpellSlots7(0);
    setSpellSlots8(0);
    setSpellSlots9(0);

	setWeapons(new ArrayList<String>());

    setSaveRoll(0);
    setDeathRoll(0);

    setProficiencyBonus(0);
	setLevel(1);
	setExp(0);
	setGold(0);
	
    setTempHP(0);
	setHitDice("0");
	setInventory(new ArrayList<String>());
	setCurrentHP(maxHealth);
    }


    /////////////////////////////////
    ////// SET AND GET METHODS //////
    /////////////////////////////////
    
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

    public void setExp(int e) {
	this.exp = e;
    }
    public int getExp() {
	return exp;
    }
    
    public void setClss(String c) {
	this.clss = c;
    }
    public String getClss() {
	return clss;
    }

    public void setSubClss(String c) {
	this.clss = c;
    }
    public String getSubClss() {
	return subClss;
    }
    
    public void setRace(String r) {
	this.race = r;
    }
    public String getRace() {
	return race;
    }
    
    public void setBackground(String bg) {
        this.background = bg;
    }
    public String getBackground() {
        return background;
    }

    public void setLanguages(HashSet<String> l) {
	this.languages = l;
    }
    public HashSet<String> getLanguages() {
	return languages;
    }

    public void setSubrace(String sr) {
	this.subrace = sr;
    }
    public String getSubrace() {
	return subrace;
    }

    public void setNotes(String n) {
        this.notes = n;
    }
    public String getNotes() {
        return notes;
    }

    public void setGender(char g) {
	this.gender = g;
    }
    public char getGender() {
	return gender;
    }

    public void setWeapons(ArrayList<String> w) {
	this.weapons = w;
    }
    public ArrayList<String> getWeapons() {
	return weapons;
    }

    public void setMaxHP(int mHP) {
	this.maxHP = mHP;
    }
    public int getMaxHP() {
	return maxHP;
    }

    public void setCurrentHP(int cHP) {
	this.currentHP = cHP;
    }
    public int getCurrentHP() {
	return currentHP;
    }

    public void setTempHP(int tHP) {
	this.tempHP = tHP;
    }
    public int getTempHP() {
	return tempHP;
    }

    public void setGold(double g) {
	this.gold = g;
    }
    public double getGold() {
	return gold;
    }
    
    public void setArmor(int a) {
	this.armor = a;
    }
    public int getArmor() {
	return armor;
    }

    public void setProficiencyBonus(int pb) {
        this.proficiencyBonus = pb;
    }
    public int getProficiencyBonus() {
        return proficiencyBonus;
    }

    public void setProficiencies(boolean[] b) {
        this.proficiencies = b;
    }
    public boolean[] getProficiencies() {
        return proficiencies;
    }
    public void setProficiencyVal(boolean b,int index) {
        this.proficiencies[index] = b;
    }

    public void setSavingThrows(boolean[] s) {
        this.savingThrows = s;
    }
    public boolean[] getSavingThrows() {
        return savingThrows;
    }
    public void setSavingThrowsVal(boolean s, int index) {
        this.savingThrows[index] = s;
    }

    public void setInspiration(int i) {
        this.inspiration = i;
    }
    public int getInspiration() {
        return inspiration;
    }

    public void setInitiative(int i) {
	this.initiative = i;
    }
    public int getInitiative() {
	return initiative;
    }

    public void setSpeed(int s) {
	this.speed = s;
    }
    public int getSpeed() {
	return speed;
    }

    public void setHitDice(String d) {
	this.hitdice = d;
    }
    public String getHitDice() {
	return hitdice;
    }

    public void setSaveRoll(int s) {
        this.saveRoll = s;
    }
    public int getSaveRoll() {
        return saveRoll;
    }

    public void setDeathRoll(int d) {
        this.deathRoll = d;
    }
    public int getDeathRoll() {
        return deathRoll;
    }

    public void setStr(int s) {
	this.strength = s;
    }
    public int getStr() {
	return strength;
    }

    public void setCons(int c) {
	this.constitution = c;
    }
    public int getCons() {
	return constitution;
    }

    public void setDex(int d) {
	this.dexterity = d;
    }
    public int getDex() {
	return dexterity;
    }

    public void setInt(int i) {
	this.intelligence = i;
    }
    public int getInt() {
	return intelligence;
    }

    public void setWis(int w) {
	this.wisdom = w;
    }
    public int getWis() {
	return wisdom;
    }

    public void setChar(int c) {
        this.charisma = c;
    }
    public int getChar() {
        return charisma;
    }

    public void setCantrips(HashSet<Spell> c) {
        this.cantrips = c;
    }
    public HashSet<Spell> getCantrips() {
        return cantrips;
    }

    public void setSpells1(HashSet<Spell> s) {
        this.spells1 = s;
    }

    public HashSet<Spell> getSpells1() {
        return spells1;
    }

    public void setBonds(HashSet<String> b) {
        this.bonds = b;
    }
    public HashSet<String> getBonds() {
        return bonds;
    }

    public void setFlaws(HashSet<String> f) {
	this.flaws = f;
    }
    public HashSet<String> getFlaws() {
	return flaws;
    }

    public void setIdeals(HashSet<String> i) {
	this.ideals = i;
    }
    public HashSet<String> getIdeals() {
	return ideals;
    }

    public void setDescription(String d) {
	this.description = d;
    }
    public String getDescription() {
	return description;
    }

    public void setSpells2(HashSet<Spell> s) {
        this.spells2 = s;
    }

    public HashSet<Spell> getSpells2() {
        return spells2;
    }

    public void setSpells3(HashSet<Spell> s) {
        this.spells3 = s;
    }

    public HashSet<Spell> getSpells3() {
        return spells3;
    }

    public void setSpells4(HashSet<Spell> s) {
        this.spells4 = s;
    }

    public HashSet<Spell> getSpells4() {
        return spells4;
    }

    public void setSpells5(HashSet<Spell> s) {
        this.spells5 = s;
    }

    public HashSet<Spell> getSpells5() {
        return spells5;
    }

    public void setSpells6(HashSet<Spell> s) {
        this.spells6 = s;
    }

    public HashSet<Spell> getSpells6() {
        return spells6;
    }

    public void setSpells7(HashSet<Spell> s) {
        this.spells7 = s;
    }

    public HashSet<Spell> getSpells7() {
        return spells7;
    }

    public void setSpells8(HashSet<Spell> s) {
        this.spells8 = s;
    }

    public HashSet<Spell> getSpells8() {
        return spells8;
    }
    
    public void setSpells9(HashSet<Spell> s) {
        this.spells9 = s;
    }

    public HashSet<Spell> getSpells9() {
        return spells9;
    }

    public void setSpellSlots1(int s) {
        this.spellSlots1 = s;
    }

    public int getSpellSlots1() {
        return spellSlots1;
    }

    public void setSpellSlots2(int s) {
        this.spellSlots2 = s;
    }

    public int getSpellSlots2() {
        return spellSlots2;
    }

    public void setSpellSlots3(int s) {
        this.spellSlots3 = s;
    }

    public int getSpellSlots3() {
        return spellSlots3;
    }

    public void setSpellSlots4(int s) {
        this.spellSlots4 = s;
    }

    public int getSpellSlots4() {
        return spellSlots4;
    }

    public void setSpellSlots5(int s) {
        this.spellSlots5 = s;
    }

    public int getSpellSlots5() {
        return spellSlots5;
    }

    public void setSpellSlots6(int s) {
        this.spellSlots6 = s;
    }

    public int getSpellSlots6() {
        return spellSlots6;
    }

    public void setSpellSlots7(int s) {
        this.spellSlots7 = s;
    }

    public int getSpellSlots7() {
        return spellSlots7;
    }

    public void setSpellSlots8(int s) {
        this.spellSlots8 = s;
    }

    public int getSpellSlots8() {
        return spellSlots8;
    }

    public void setSpellSlots9(int s) {
        this.spellSlots9 = s;
    }

    public int getSpellSlots9() {
        return spellSlots9;
    }

    public void setFeatures(HashSet<String> f) {
        this.features = f;
    }
    public HashSet<String> getFeatures() {
        return features;
    }
    
    public void setInventory(ArrayList<String> i) {
        this.inventory = i;
    }
    public ArrayList<String> getInventory() {
        return inventory;
    }
    
    public void clearInventory() {
        this.inventory = new ArrayList<String>();
    }

    ///////////////////////////////
    /////// OTHER FUNCTIONS ///////
    ///////////////////////////////
    
    public void addToInventory(String item) {
		this.inventory.add(item);
    }

    public void removeFromInventory(String item) {
		this.inventory.remove(item);
    }
    public int inventorySize() {
		return inventory.size();
    }

    public void addLevel() {
		this.level++;
    }
    
    public void addFeature(String item) {
		this.features.add(item);
    }

    public void removeFeature(String item) {
		this.features.remove(item);
    }

    public void addExp(int n) {
		this.exp = this.exp + n;
    }

	public void subtractHealth(int n) {
		this.currentHP = this.currentHP - n;
	}	
    
	public void addHealth(int n) {
		this.currentHP = this.currentHP + n;
	}


	///////////////////////////////
	//////// Saving/Loading ///////
	///////////////////////////////

    public static void SaveCharacter(Character character,String fileName) {

        Gson gson = new Gson();

        String name = character.getName();
	
        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(character,writer);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Character LoadCharacter(String CNamePath) {

        Gson gson = new Gson();

        Character character = new Character();
        File f = new File(CNamePath);
        String fileName = f.getName();
        String name = fileName.substring(0,fileName.lastIndexOf("."));

        try (Reader reader = new FileReader(CNamePath)) {
	    
            character = gson.fromJson(reader, Character.class);	   

        } catch (IOException e) {
            e.printStackTrace();
        }
	
        return character;
    }

    ///////////////////////////////////////
    ///////////////////////////////////////

    /* 
    public String printCharacter() {
	return "Character: " + name + "level " + level + " [Class=" + clss + ", race=" + race + ", subrace=" + subrace + ", gender=" + gender +
	    ", weapon=" + weapon + ", maxHP=" + maxHP + ", currentHP=" + currentHP + ", maxMP=" + maxMP +
	    ", currentMP=" + currentMP + ", armor class=" + armor + ", initiative=" + initiative +
	    ", speed=" + speed + ", hitdice=" + hitdice + ", strength=" + strength + ", constitution=" + constitution +
	    ", dexterity=" + dexterity + ", intelligence=" + intelligence + ", wisdom=" + wisdom +
	    ", charisma=" + charisma + ", cantrips =" + cantrips + ", spells=" + spells +
	    ", inventory=" + Arrays.toString(inventory.toArray()) + ", Gold=" + gold + "]";
    }
    */
}
