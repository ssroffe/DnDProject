
import com.google.gson.*;
import dnd.Spell;
import java.util.*;
import java.io.*;

public class testSpells {

    public static void main(String[] args) {

        Gson gson = new Gson();
        Spell[] spell;

       
        try (Reader reader = new FileReader("spells.json")) {
            spell = gson.fromJson(reader,Spell[].class); 
            //System.out.println("here");
            for (int i = 0; i < spell.length; i++) {
                System.out.println(spell[i].printSpell());
                System.out.println();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
        
