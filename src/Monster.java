/** Seth Roffe
    Monster class for DnD
*/

import java.util.*;

public class Monster {

    private char difficulty;
    
    private int maxHP;
    private int currentHP;
    private int atkBonus;
    private int damage;
    private int armor;

    
    ////////////////////
    
    public Character(char diff, int maxHP, int atkBonus, int dmg, int armor) {
        setDifficulty(diff);
        setMaxHP(maxHP);
        setCurrentHP(maxHP);
        setAtkBonus(atkBonus);
        setDamage(dmg);
        setArmor(armor);
    }

    public Character() {
        setDifficulty('e');
        setMaxHP(0);
        setCurrentHP(0);
        setAtkBonus(0);
        setDamage(0);
        setArmor(0);
    }

    public Character(char diff) {
        setDifficultly(diff);
        setMaxHP(0);
        setCurrentHP(0);`
        setAtkBonus(0);
        setDamage(0);
        setArmor(0);
    }

    public void setDifficulty(char diff) {
        this.difficulty = diff;
    }
    public char getDifficulty() {
        return difficulty;
    }

    public void setMaxHP(int HP) {
        this.maxHP = HP;
    }
    public int getMaxHP() {
        return maxHP;
    }

    public void setCurrentHP(int HP) {
        this.currentHP = HP;
    }
    public int getCurrentHP() {
        return currentHP;
    }

    public void setAtkBonus(int atk) {
        this.atkBonus = atk;
    }
    public int getAtkBonus() {
        return atkBonus;
    }

    public void setDamage(int dmg) {
        this.damage = dmg;
    }
    public int getDamage() {
        return damage;
    }

    public void setArmor(int arm) {
        this.armor = arm;
    }
    public int getArmor() {
        return armor;
    }
    
    ////////////////////////
    
    public static void subtractHP(int n) {
        this.currentHP = this.currentHP - n
    }



}
