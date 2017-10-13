/** Written by
 * Seth Roffe
*/
package dnd;

import com.google.gson.*;
import java.util.*;
import java.io.*;
import java.text.DecimalFormat;
import javafx.application.Application;
import javafx.collections.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.paint.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.beans.value.*;
import javafx.stage.FileChooser;
import javafx.fxml.FXML;
import javafx.geometry.*;

import dnd.Character;
import dnd.Spell;

public class CharacterMgr extends Application {

    private String fileName;
    private DecimalFormat fmt = new DecimalFormat("+0;-0");
    private HashSet<String> classes = new HashSet<String>(Arrays.asList( "Barbarian", "Bard", "Cleric", "Druid", "Fighter", "Monk", "Paladin", "Ranger", "Rogue", "Sorcerer", "Warlock", "Wizard"));

    private Character c;
    private int row = 0;

    //start the first page - new or load character
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        NewLoadScreen();
    }

    public void NewLoadScreen() {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("DnD Character Manager -- New or Load");

        GridPane grid = new GridPane();

        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        VBox vbWelcome = new VBox(10);
        vbWelcome.setAlignment(Pos.CENTER);

        grid.setPadding(new Insets(25,25,25,25));

        Scene scene1 = new Scene(grid, 600, 300);
        scene1.getStylesheets().add("lib/NewLoadScreen.css");
        primaryStage.setScene(scene1);


        Text welcome = new Text("Welcome to D&D!");
        //grid.add(welcome, 0,0,3,1);
        //grid.add(welcome,0,0);
        welcome.setId("welcometitle");

        Label loadLabel = new Label("Would you like to Load a Character or Make a New one?");
        //grid.add(loadLabel,0,1,1,2);

        Button lbtn = new Button("Load Character");
        Button nbtn = new Button("New Character");

        HBox nlbtn = new HBox(10);
        nlbtn.setAlignment(Pos.CENTER);
        nlbtn.getChildren().addAll(lbtn,nbtn);

        //grid.add(nlbtn,0,3);

        vbWelcome.getChildren().addAll(welcome,loadLabel,nlbtn);
        grid.add(vbWelcome,0,0);

        //Load Old Character
        lbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                fileName = LoadCharacterFile(primaryStage);
                if (!fileName.isEmpty()) {
                    primaryStage.close();
                    c = Character.LoadCharacter(fileName);
                    CharacterSheet();
                }
                else {
                    final Text noCharSel = new Text("No Character Selected");
                    noCharSel.setFill(Color.FIREBRICK);
                    grid.add(noCharSel,0,4);
                }
            }
        });

        //Make a new Character
        nbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                primaryStage.close();
                NewCharacter();
            }
        });

        primaryStage.show();

    }
    
    public void NewCharacter() {
        
        Stage stage = new Stage();
        stage.setTitle("Make a new Character");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25,25,25,25));

        Scene scene = new Scene(grid, 500, 300);

        //scene.getStylesheets().add(this.getClass().getResource("NewCharacter.css"));
        stage.setScene(scene);

        Text insertName = new Text("Please insert the name of your character (Class name optional).");
        grid.add(insertName,0,0);

        TextField nameTf = new TextField();
        nameTf.setPromptText("Input Character Name");
        grid.add(nameTf,0,1);
        

        Label clss = new Label("Class name:");

        ObservableList<String> classOptions = FXCollections.observableArrayList( "Barbarian", "Bard", "Cleric", "Druid", "Fighter", "Monk", "Paladin", "Ranger", "Rogue", "Sorcerer", "Warlock", "Wizard");

        ComboBox<String> clssTf = new ComboBox<String>(classOptions);
        clssTf.setEditable(true);

        clssTf.setPromptText("Select a Class");

        HBox hbClss = new HBox(10);
        hbClss.setAlignment(Pos.CENTER);
        hbClss.getChildren().addAll(clss,clssTf);
        grid.add(hbClss,0,2);
        
        Button submit = new Button("Submit");
        Button cancel = new Button("Cancel");

        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER);
        hbBtn.getChildren().addAll(submit,cancel);
        grid.add(hbBtn,0,3);

        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (nameTf.getText() == null || nameTf.getText().isEmpty()) {
                    final Text noName = new Text("No name was inputted!");
                    noName.setFill(Color.FIREBRICK);
                    grid.add(noName,0,4);
                }
                else if (clssTf.getValue().toString() == null || clssTf.getValue().toString().isEmpty()) {
                    c = new Character(nameTf.getText());
                    c.setProficiencyBonus(2);
                    stage.close();
                    CharacterSheet();
                }
                else {
                    c = new Character(nameTf.getText(),clssTf.getValue().toString());
                    c.setProficiencyBonus(2);
                    stage.close();
                    CharacterSheet();
                }
            }
        });

        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                stage.close();
                NewLoadScreen();
            }
        });
        stage.show();
    }  
        

        


    public void CharacterSheet() {

        Stage stage = new Stage();
        stage.setTitle(c.getName() + " -- Character page");


        BorderPane border = new BorderPane();
        border.setPadding(new Insets(20));

        ScrollPane scroll = new ScrollPane();
        scroll.setContent(border);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        grid.setPadding(new Insets(25,25,25,25));

        Scene scene = new Scene(scroll,840,840);
        
        scene.getStylesheets().add("lib/CharacterSheet.css");

        stage.setScene(scene);
        int row = 1;
        Text characterName = new Text(c.getName());
        characterName.setId("characterName");
        //grid.add(characterName,1,0,2,1);


        //////////// Level ////////////////

        Label levelLabel = new Label("Level:");
        grid.add(levelLabel,0,row);
        Label level = new Label(Integer.toString(c.getLevel()));
        grid.add(level,1,row);
        Button addLevel = new Button("Add Level");
        Button lowLevel = new Button("Lower Level");
        HBox hblvl = new HBox(10);
        hblvl.getChildren().addAll(addLevel,lowLevel);
        grid.add(hblvl,2,row);

        TextField profTf = new TextField(); // for auto proficiency calculation

        addLevel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                c.setLevel(c.getLevel() + 1);
                level.setText(Integer.toString(c.getLevel()));
                if (c.getLevel() == 5 || c.getLevel() == 9 || c.getLevel() == 13 || c.getLevel() == 17) {
                    c.setProficiencyBonus(c.getProficiencyBonus() + 1);
                    profTf.setText(fmt.format(c.getProficiencyBonus()));
                }
            }
        });
 
        lowLevel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (c.getLevel() != 1) {
                    c.setLevel(c.getLevel() - 1);
                    level.setText(Integer.toString(c.getLevel()));
                }
                if (c.getLevel() == 4 || c.getLevel() == 8 || c.getLevel() == 12 || c.getLevel() == 16) {
                    c.setProficiencyBonus(c.getProficiencyBonus() - 1);
                    profTf.setText(fmt.format(c.getProficiencyBonus()));
                }
            }
        });       

        row++;
        
        ////////////// Exp ////////////////
        Label expLabel = new Label("Exp:");
        grid.add(expLabel,0,row);
        TextField expTf = new TextField();
        expTf.setId("locked-tf");
        expTf.setText(Integer.toString(c.getExp()));
        expTf.setEditable(false);
        grid.add(expTf,1,row);
        final int expRow = row;

        ToggleButton expbtn = new ToggleButton("edit");
        expbtn.setOnAction(new EventHandler<ActionEvent>() {
            //for checking valid new max HP
            int previousExp = c.getExp();
            Text errMsg = new Text("New Exp was not an integer!");
            @Override
            public void handle(ActionEvent e) {
                if (expbtn.isSelected()) {
                    previousExp = c.getExp();
                    expTf.setEditable(true);
                    expTf.setId("unlocked-tf");
                }
                else {
                    previousExp = c.getExp();
                    expTf.setEditable(false);
                    expTf.setId("locked-tf");
                    boolean isInt = isInteger(expTf.getText());
                    if (isInt) {
                        c.setExp(Integer.parseInt(expTf.getText()));
                        grid.getChildren().remove(errMsg);
                    }
                    else {
                        errMsg.setFill(Color.FIREBRICK);
                        grid.getChildren().remove(errMsg);
                        grid.add(errMsg,3,expRow);
                        expTf.setText(Integer.toString(previousExp));
                    }
                }
            }
        });
        grid.add(expbtn,2,row);

        row++;

        //////////// Class ////////////////
        ObservableList<String> classOptions = FXCollections.observableArrayList( "Barbarian", "Bard", "Cleric", "Druid", "Fighter", "Monk", "Paladin", "Ranger", "Rogue", "Sorcerer", "Warlock", "Wizard");
        Label clssLabel = new Label("Class:");
        grid.add(clssLabel,0,row);
        ComboBox<String> clssTf = new ComboBox<String>(classOptions);
        clssTf.setEditable(true);
        clssTf.setValue(c.getClss());
        grid.add(clssTf,1,row);

        ObservableList<String> subClassOptions = FXCollections.observableArrayList();
        if (c.getClss().equalsIgnoreCase("Barbarian")) {
            subClassOptions.addAll("Path of the Berserker", "Path of the Totem Warrior");
        }
        else if (c.getClss().equalsIgnoreCase("Bard")) {
            subClassOptions.addAll("College of Lore", "College of Valor");
        }
        else if (c.getClss().equalsIgnoreCase("Cleric")) {
            subClassOptions.addAll("Knowledge Domain","Life Domain","Light Domain","Nature Domain","Tempest Domain","Trickery Domain","War Domain");
        }
        else if (c.getClss().equalsIgnoreCase("Druid")) {
            subClassOptions.addAll("Circle of the Land", "Circle of the Moon");
        }
        else if (c.getClss().equalsIgnoreCase("Fighter")) {
            subClassOptions.addAll("Champion","Battle Master","Eldritch Knight");
        }
        else if (c.getClss().equalsIgnoreCase("Monk")) {
            subClassOptions.addAll("Way of the Open Hand", "Way of Shadow","Way of the Four Elements");
        }
        else if (c.getClss().equalsIgnoreCase("Paladin")) {
            subClassOptions.addAll("Oath of Devotion","Oath of the Ancients", "Oath of Vengeance");
        }
        else if (c.getClss().equalsIgnoreCase("Ranger")) {
            subClassOptions.addAll("Hunter","Beast Master");
        }
        else if (c.getClss().equalsIgnoreCase("Rogue")) {
            subClassOptions.addAll("Thief","Assassin","Arcane Trickster");
        }
        else if (c.getClss().equalsIgnoreCase("Sorcerer")) {
            subClassOptions.addAll("Draconic Bloodline", "Wild Magic");
        }
        else if (c.getClss().equalsIgnoreCase("Warlock")) {
            subClassOptions.addAll("The Archfey", "The Fiend", "The Great Old One");
        }
        else if (c.getClss().equalsIgnoreCase("Wizard")) {
            subClassOptions.addAll("School of Abjuration", "School of Conjuration", "School of Divination", "School of Enchantment", "School of Evocation", "School of Illusion", "School of Necromancy", "School of Transmutation");
        }
        else {
            subClassOptions.clear();
        }


        ComboBox<String> subClssTf = new ComboBox<String>(subClassOptions);
        subClssTf.setEditable(true);

        String[] subClssSplit = c.getSubClss().split("--");
        subClssTf.setValue(subClssSplit[0]);

        /////// Sub Sub Class stuff
        ComboBox<String> subsubClssTf = new ComboBox<String>();
        Label subsubClssLabel = new Label();
        subsubClssTf.setEditable(true);

        HBox subClssTfHb = new HBox(10);
        subClssTfHb.getChildren().add(subClssTf);

        try {
            String subSubClss = subClssSplit[1];
            subsubClssTf.setValue(subSubClss);
        }
        catch (ArrayIndexOutOfBoundsException aioe) {
        }

        if (c.getClss().equalsIgnoreCase("Warlock") || subClssSplit[0].equalsIgnoreCase("Circle of the Land") || subClssSplit[0].equalsIgnoreCase("Draconic Bloodline")) {
            if (c.getClss().equalsIgnoreCase("Warlock")) {
                subsubClssLabel.setText("Pact Boon:");
                ObservableList<String> boons = FXCollections.observableArrayList("Pact of the Chain", "Pact of the Blade", "Pact of the Tome");
                subsubClssTf.setItems(boons);

            }
            else if (subClssSplit[0].equalsIgnoreCase("Circle of the Land")) {
                subsubClssLabel.setText("Land type:");
                ObservableList<String> lands = FXCollections.observableArrayList("Arctic","Coast","Desert","Forest","Grassland","Mountain","Swamp","Underdark");
                subsubClssTf.setItems(lands);
            }
            else {
                subsubClssLabel.setText("Dragon blood:");
                ObservableList<String> bloods = FXCollections.observableArrayList("Black (Acid)", "Blue (Lightning)","Brass (Fire)","Bronze (Lightning)","Copper (Acid)","Gold (Fire)","Green (Poison)", "Red (Fire)", "Silver (Cold)", "White (Cold)");
                subsubClssTf.setItems(bloods);
            }
            subClssTfHb.getChildren().addAll(subsubClssLabel,subsubClssTf);
        }

        clssTf.valueProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue ov, String oldVal, String newVal) {
                c.setClss(newVal);
                // Redefine Subclasses
                if (c.getClss().equalsIgnoreCase("Barbarian")) {
                    subClassOptions.clear();
                    subClassOptions.addAll("Path of the Berserker", "Path of the Totem Warrior");
                }
                else if (c.getClss().equalsIgnoreCase("Bard")) {
                    subClassOptions.clear();
                    subClassOptions.addAll("College of Lore", "College of Valor");
                }
                else if (c.getClss().equalsIgnoreCase("Cleric")) {
                    subClassOptions.clear();
                    subClassOptions.addAll("Knowledge Domain","Life Domain","Light Domain","Nature Domain","Tempest Domain","Trickery Domain","War Domain");
                }
                else if (c.getClss().equalsIgnoreCase("Druid")) {
                    subClassOptions.clear();
                    subClassOptions.addAll("Circle of the Land", "Circle of the Moon");
                }
                else if (c.getClss().equalsIgnoreCase("Fighter")) {
                    subClassOptions.clear();
                    subClassOptions.addAll("Champion","Battle Master","Eldritch Knight");
                }
                else if (c.getClss().equalsIgnoreCase("Monk")) {
                    subClassOptions.clear();
                    subClassOptions.addAll("Way of the Open Hand", "Way of Shadow","Way of the Four Elements");
                }
                else if (c.getClss().equalsIgnoreCase("Paladin")) {
                    subClassOptions.clear();
                    subClassOptions.addAll("Oath of Devotion","Oath of the Ancients", "Oath of Vengeance");
                }
                else if (c.getClss().equalsIgnoreCase("Ranger")) {
                    subClassOptions.clear();
                    subClassOptions.addAll("Hunter","Beast Master");
                }
                else if (c.getClss().equalsIgnoreCase("Rogue")) {
                    subClassOptions.clear();
                    subClassOptions.addAll("Thief","Assassin","Arcane Trickster");
                }
                else if (c.getClss().equalsIgnoreCase("Sorcerer")) {
                    subClassOptions.clear();
                    subClassOptions.addAll("Draconic Bloodline", "Wild Magic");
                }
                else if (c.getClss().equalsIgnoreCase("Warlock")) {
                    subClassOptions.clear();
                    subClassOptions.addAll("The Archfey", "The Fiend", "The Great Old One");
                }
                else if (c.getClss().equalsIgnoreCase("Wizard")) {
                    subClassOptions.clear();
                    subClassOptions.addAll("School of Abjuration", "School of Conjuration", "School of Divination", "School of Enchantment", "School of Evocation", "School of Illusion", "School of Necromancy", "School of Transmutation");
                }
                else {
                    subClassOptions.clear();
                }

                if (newVal.equalsIgnoreCase("Warlock")) {
                    subsubClssLabel.setText("Pact Boon:");
                    ObservableList<String> boons = FXCollections.observableArrayList("Pact of the Chain", "Pact of the Blade", "Pact of the Tome");
                    subsubClssTf.setItems(boons);
                    
                    subClssTfHb.getChildren().addAll(subsubClssLabel,subsubClssTf);
                }
                else {
                    subClssTfHb.getChildren().remove(subsubClssTf);
                    subClssTfHb.getChildren().remove(subsubClssLabel);
                }
                subClssTf.setItems(subClassOptions);
                subClssTf.setValue("");
            }
        });

        row++;

        /////////// Sub Class ///////////
        Label subClssLabel = new Label("Subclass:");
        grid.add(subClssLabel,0,row);
        grid.add(subClssTfHb,1,row);
        subClssTf.valueProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue ov, String oldVal, String newVal) {
                subClssSplit[0] = newVal;
                c.setSubClss(newVal);
                if (newVal != null) {
                    if (newVal.equalsIgnoreCase("Circle of the Land") || newVal.equalsIgnoreCase("Draconic Bloodline") || c.getClss().equalsIgnoreCase("Warlock")) {
                        if (newVal.equalsIgnoreCase("Circle of the Land")) {
                            subsubClssLabel.setText("Land type:");
                            ObservableList<String> lands = FXCollections.observableArrayList("Arctic","Coast","Desert","Forest","Grassland","Mountain","Swamp","Underdark");
                            subsubClssTf.setItems(lands);
                            subClssTfHb.getChildren().addAll(subsubClssLabel,subsubClssTf);
                            subsubClssTf.setValue("");
                        }
                        else if (newVal.equalsIgnoreCase("Draconic Bloodline")){
                            subsubClssLabel.setText("Ancestry:");
                            ObservableList<String> bloods = FXCollections.observableArrayList("Black (Acid)", "Blue (Lightning)","Brass (Fire)","Bronze (Lightning)","Copper (Acid)","Gold (Fire)","Green (Poison)", "Red (Fire)", "Silver (Cold)", "White (Cold)");
                            subsubClssTf.setItems(bloods);
                            subClssTfHb.getChildren().addAll(subsubClssLabel,subsubClssTf);
                            subsubClssTf.setValue("");
                        }
                    }
                    else {
                        subsubClssTf.setValue(null);
                        subClssTfHb.getChildren().remove(subsubClssLabel);
                        subClssTfHb.getChildren().remove(subsubClssTf);
                    }
                }
            }
        });
        subsubClssTf.valueProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue ov, String oldVal, String newVal) {
                c.setSubClss(subClssTf.getValue() + "--" + newVal);
            }
        });
        row++;

        ///////////// Race //////////////
        Label raceLabel = new Label("Race:");
        grid.add(raceLabel,0,row);
        ObservableList<String> raceOptions = FXCollections.observableArrayList("Dwarf","Elf","Halfling","Human","Dragonborn","Gnome","Half-Elf","Half-Orc","Tiefling"); 
        ComboBox<String> raceTf = new ComboBox<String>(raceOptions);
        raceTf.setEditable(true);
        raceTf.setValue(c.getRace());
        grid.add(raceTf,1,row);

        ObservableList<String> subRaceOptions = FXCollections.observableArrayList();
        if (c.getRace().equalsIgnoreCase("Dwarf")) {
            subRaceOptions.addAll("Hill Dwarf", "Mountain Dwarf");
        }
        else if (c.getRace().equalsIgnoreCase("Elf")) {
            subRaceOptions.addAll("High Elf", "Wood Elf", "Dark Elf");
        }
        else if (c.getRace().equalsIgnoreCase("Halfling")) {
            subRaceOptions.addAll("Lightfoot","Stout");
        }
        else if (c.getRace().equalsIgnoreCase("Dragonborn")) {
            subRaceOptions.addAll("Black (Acid)", "Blue (Lightning)","Brass (Fire)","Bronze (Lightning)","Copper (Acid)","Gold (Fire)","Green (Poison)", "Red (Fire)", "Silver (Cold)", "White (Cold)");
        }
        else if (c.getRace().equalsIgnoreCase("Gnome")) {
            subRaceOptions.addAll("Forest Gnome", "Rock Gnome");
        }
        else {
            subRaceOptions.addAll("");
        }
        
        ComboBox<String> subRaceTf = new ComboBox<String>(subRaceOptions);
        subRaceTf.setEditable(true);
        subRaceTf.setValue(c.getSubrace());

        raceTf.valueProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue ov, String oldVal, String newVal) {
                c.setRace(newVal);
                if (c.getRace().equalsIgnoreCase("Dwarf")) {
                    subRaceOptions.clear();
                    subRaceOptions.addAll("Hill Dwarf", "Mountain Dwarf");
                }
                else if (c.getRace().equalsIgnoreCase("Elf")) {
                    subRaceOptions.clear();
                    subRaceOptions.addAll("High Elf", "Wood Elf", "Dark Elf");
                }
                else if (c.getRace().equalsIgnoreCase("Halfling")) {
                    subRaceOptions.clear();
                    subRaceOptions.addAll("Lightfoot","Stout");
                }
                else if (c.getRace().equalsIgnoreCase("Dragonborn")) {
                    subRaceOptions.clear();
                    subRaceOptions.addAll("Black (Acid)", "Blue (Lightning)","Brass (Fire)","Bronze (Lightning)","Copper (Acid)","Gold (Fire)","Green (Poison)", "Red (Fire)", "Silver (Cold)", "White (Cold)");
                }
                else if (c.getRace().equalsIgnoreCase("Gnome")) {
                    subRaceOptions.clear();
                    subRaceOptions.addAll("Forest Gnome", "Rock Gnome");
                }
                else {
                    subRaceOptions.clear();
                }

                subRaceTf.setValue("");
                c.setSubrace("");
                subRaceTf.setItems(subRaceOptions);

            }
        });
	row++;

        ///////////// SubRace //////////////
        Label subRaceLabel = new Label("SubRace:");
        grid.add(subRaceLabel,0,row);
        grid.add(subRaceTf,1,row);
        subRaceTf.valueProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue ov, String oldval, String newval) {
                c.setSubrace(newval);
            }
        });

        row++;

        ///////////// Background //////////////
        Label backgroundLabel = new Label("Background:");
        grid.add(backgroundLabel,0,row);
        ObservableList<String> backgroundOptions = FXCollections.observableArrayList("Acolyte","Charlatan","Criminal","Entertainer","Folk Hero","Guild Artisan","Hermit","Noble","Outlander","Sage","Sailor","Soldier","Urchin");
        ComboBox<String> backgroundTf = new ComboBox<String>(backgroundOptions);
        backgroundTf.setEditable(true);
        backgroundTf.setValue(c.getBackground());
        grid.add(backgroundTf,1,row);

        backgroundTf.valueProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue ov, String oldVal, String newVal) {
                c.setBackground(newVal);
            }
        });
	row++;

    stage.show();

    ///////////// Gold /////////////////
    //////////// Currency //////////////
    /*
    Label goldLabel = new Label("Gold:");
    grid.add(goldLabel,0,row);
    Label gold = new Label(Double.toString(c.getGold()));
    grid.add(gold,1,row);
    gold.setId("gold");
    */ 
    int[] currency = c.getCurrency();
    Label currencyLabel = new Label("Currencyi:");
    HBox currencyHb = new HBox(10);

    Label cpLabel = new Label("cp:");
    cpLabel.setId("currLabel");
    Label cp = new Label(Integer.toString(currency[0]));
    Label spLabel = new Label("sp:");
    spLabel.setId("currLabel");
    Label sp = new Label(Integer.toString(currency[1]));
    Label epLabel = new Label("ep:");
    epLabel.setId("currLabel");
    Label ep = new Label(Integer.toString(currency[2]));
    Label gpLabel = new Label("gp:");
    gpLabel.setId("currLabel");
    Label gp = new Label(Integer.toString(currency[3]));
    Label ppLabel = new Label("pp:");
    ppLabel.setId("currLabel");
    Label pp = new Label(Integer.toString(currency[4]));

    currencyHb.getChildren().addAll(cpLabel,cp,spLabel,sp,epLabel,ep,gpLabel,gp,ppLabel,pp);
    grid.add(currencyLabel,0,row);
    grid.add(currencyHb,1,row);

    row++;

    ////////// ADD/LOSE CURRENCY///////////
    ObservableList<String> currencyOptions = FXCollections.observableArrayList("","cp","sp","ep","gp","pp");
    ComboBox<String> currencyChoice = new ComboBox<String>(currencyOptions);
    currencyChoice.setValue("");
    HBox addCurrencyHb = new HBox(10);
    TextField pmGoldTf = new TextField();
    pmGoldTf.setPromptText("Add or Subtract Currency");
    addCurrencyHb.getChildren().addAll(pmGoldTf,currencyChoice);
    grid.add(addCurrencyHb,1,row);

    Button plusGold = new Button("Add");
    Button minusGold = new Button("Spend");
    HBox hbpmGold = new HBox(10);
    hbpmGold.getChildren().addAll(plusGold,minusGold);
    grid.add(hbpmGold,2,row);

    final int addGoldRow = row;

    Text errMsg = new Text("Inputted Value was not a number!");
    Text errMsg2 = new Text("Please input a currency type!");

    plusGold.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            boolean isDbl = isDouble(pmGoldTf.getText());
            grid.getChildren().remove(errMsg);
            grid.getChildren().remove(errMsg2);
            int[] currCurrency = c.getCurrency();
            if (!currencyChoice.getValue().isEmpty()) {
                if (isDbl) {
                    if (currencyChoice.getValue().equalsIgnoreCase("cp")) {
                        currCurrency[0] = Integer.parseInt(pmGoldTf.getText()) + currCurrency[0];
                        c.setCurrency(currCurrency);
                        cp.setText(Integer.toString(currCurrency[0]));
                        pmGoldTf.setText("");
                        currencyChoice.setValue("");
                    }
                    else if (currencyChoice.getValue().equalsIgnoreCase("sp")) {
                        currCurrency[1] = Integer.parseInt(pmGoldTf.getText()) + currCurrency[1];
                        c.setCurrency(currCurrency);
                        sp.setText(Integer.toString(currCurrency[1]));
                        pmGoldTf.setText("");
                        currencyChoice.setValue("");
                    }
                    else if (currencyChoice.getValue().equalsIgnoreCase("ep")) {
                        currCurrency[2] = Integer.parseInt(pmGoldTf.getText()) + currCurrency[2];
                        c.setCurrency(currCurrency);
                        ep.setText(Integer.toString(currCurrency[2]));
                        pmGoldTf.setText("");
                        currencyChoice.setValue("");
                    }
                    else if (currencyChoice.getValue().equalsIgnoreCase("gp")) {
                        currCurrency[3] = Integer.parseInt(pmGoldTf.getText()) + currCurrency[3];
                        c.setCurrency(currCurrency);
                        gp.setText(Integer.toString(currCurrency[3]));
                        pmGoldTf.setText("");
                        currencyChoice.setValue("");
                    }
                    else if (currencyChoice.getValue().equalsIgnoreCase("pp")) {
                        currCurrency[4] = Integer.parseInt(pmGoldTf.getText()) + currCurrency[4];
                        c.setCurrency(currCurrency);
                        pp.setText(Integer.toString(currCurrency[4]));
                        pmGoldTf.setText("");
                        currencyChoice.setValue("");
                    }
                }
                else {
                    errMsg.setFill(Color.FIREBRICK);
                    grid.add(errMsg,3,addGoldRow);
                }
            }
            else {
                errMsg2.setFill(Color.FIREBRICK);
                grid.add(errMsg2,3,addGoldRow);
            }

        }
    });

    minusGold.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            boolean isDbl = isDouble(pmGoldTf.getText());
            grid.getChildren().remove(errMsg);
            grid.getChildren().remove(errMsg2);
            int[] currCurrency = c.getCurrency();
            if (!currencyChoice.getValue().isEmpty()) {
                if (isDbl) {
                    if (currencyChoice.getValue().equalsIgnoreCase("cp")) {
                        currCurrency[0] = currCurrency[0] - Integer.parseInt(pmGoldTf.getText());
                        c.setCurrency(currCurrency);
                        cp.setText(Integer.toString(currCurrency[0]));
                        pmGoldTf.setText("");
                        currencyChoice.setValue("");
                    }
                    else if (currencyChoice.getValue().equalsIgnoreCase("sp")) {
                        currCurrency[1] = currCurrency[1] - Integer.parseInt(pmGoldTf.getText());
                        c.setCurrency(currCurrency);
                        sp.setText(Integer.toString(currCurrency[1]));
                        pmGoldTf.setText("");
                        currencyChoice.setValue("");
                    }
                    else if (currencyChoice.getValue().equalsIgnoreCase("ep")) {
                        currCurrency[2] = currCurrency[2] - Integer.parseInt(pmGoldTf.getText());
                        c.setCurrency(currCurrency);
                        ep.setText(Integer.toString(currCurrency[2]));
                        pmGoldTf.setText("");
                        currencyChoice.setValue("");
                    }
                    else if (currencyChoice.getValue().equalsIgnoreCase("gp")) {
                        currCurrency[3] = currCurrency[3] - Integer.parseInt(pmGoldTf.getText());
                        c.setCurrency(currCurrency);
                        gp.setText(Integer.toString(currCurrency[3]));
                        pmGoldTf.setText("");
                        currencyChoice.setValue("");
                    }
                    else if (currencyChoice.getValue().equalsIgnoreCase("pp")) {
                        currCurrency[4] = currCurrency[4] - Integer.parseInt(pmGoldTf.getText());
                        c.setCurrency(currCurrency);
                        pp.setText(Integer.toString(currCurrency[4]));
                        pmGoldTf.setText("");
                        currencyChoice.setValue("");
                    }
                }
                else {
                    errMsg.setFill(Color.FIREBRICK);
                    grid.add(errMsg,3,addGoldRow);
                }
            }
            else {
                errMsg2.setFill(Color.FIREBRICK);
                grid.add(errMsg2,3,addGoldRow);
            }

        }
    });

    // Separate Descriptions from Health
    row++;
    row++;
    row++;

	////////////////////////////////////////////////
	////////////////////////////////////////////////
	
	//////////// Health //////////////
	Label maxHealthLabel = new Label("Max HP:");
	grid.add(maxHealthLabel,0,row);
	TextField maxHealthTf = new TextField();
    maxHealthTf.setId("locked-tf");
	maxHealthTf.setText(Integer.toString(c.getMaxHP()));
	maxHealthTf.setEditable(false);
	grid.add(maxHealthTf,1,row);
	final int maxHealthRow = row;
	
	ToggleButton maxHealthbtn = new ToggleButton("edit");
	maxHealthbtn.setOnAction(new EventHandler<ActionEvent>() {
		//for checking valid new max HP
		int previousMaxHP = c.getMaxHP();
		Text errMaxHP = new Text("New Max HP was not an integer!");
		@Override
		public void handle(ActionEvent e) {
		    if (maxHealthbtn.isSelected()) {
                previousMaxHP = c.getMaxHP();
                maxHealthTf.setEditable(true);
                maxHealthTf.setId("unlocked-tf");
		    }
		    else {
                previousMaxHP = c.getMaxHP();
				maxHealthTf.setEditable(false);
				maxHealthTf.setId("locked-tf");
				boolean isInt = isInteger(maxHealthTf.getText());
				if (isInt) {
					c.setMaxHP(Integer.parseInt(maxHealthTf.getText()));
					grid.getChildren().remove(errMaxHP);
				}
				else {
					errMaxHP.setFill(Color.FIREBRICK);
					grid.getChildren().remove(errMaxHP);
					grid.add(errMaxHP,3,maxHealthRow);
					maxHealthTf.setText(Integer.toString(previousMaxHP));
				}
			}
		}
	});
	grid.add(maxHealthbtn,2,row);

    row++;

    Label currHealthLabel = new Label("Current HP:");
    grid.add(currHealthLabel, 0, row);
    Label currHealth = new Label();
    currHealth.setText(Integer.toString(c.getCurrentHP()));
    grid.add(currHealth,1,row);

    final int currHPRow = row;

    row++;

    /////// ADD/LOSE HP ////////

    TextField pmHPTf = new TextField();
    pmHPTf.setPromptText("Add or Subtract HP");
    grid.add(pmHPTf,1,row);

    Button plusHP = new Button("add HP");
    Button minusHP = new Button("lose HP");
    HBox hbpmHP = new HBox(10);
    hbpmHP.getChildren().addAll(plusHP,minusHP);
    grid.add(hbpmHP,2,row);

    final int addHPRow = row;

    plusHP.setOnAction(new EventHandler<ActionEvent>() {
        Text errMsg = new Text("Inputted Value was not an Integer!");
        @Override
        public void handle(ActionEvent e) {
            boolean isInt = isInteger(pmHPTf.getText());
            boolean notempty = !(pmHPTf.getText().isEmpty());
            if (isInt && notempty) {
                int addHPVal = Integer.parseInt(pmHPTf.getText()) + c.getCurrentHP();
                if (addHPVal >= c.getMaxHP()) {
                    c.setCurrentHP(c.getMaxHP());
                    currHealth.setText(Integer.toString(c.getMaxHP()));
                }
                else {
                    c.setCurrentHP(addHPVal);
                    currHealth.setText(Integer.toString(c.getCurrentHP()));
                }
                grid.getChildren().remove(errMsg);
            }
            else {
                errMsg.setFill(Color.FIREBRICK);
                grid.getChildren().remove(errMsg);
                grid.add(errMsg,3,addHPRow);
            }
            pmHPTf.clear();

        }
    });

    Button deathRollsBtn = new Button(); //Create for a fire() event when knocked out

    minusHP.setOnAction(new EventHandler<ActionEvent>() {
        Text errMsg = new Text("Inputted Value was not an Integer!");
        @Override
        public void handle(ActionEvent e) {
            boolean isInt = isInteger(pmHPTf.getText());
            boolean notempty = !(pmHPTf.getText().isEmpty());
            if (isInt && notempty) {
                int loseHPVal = c.getCurrentHP() - Integer.parseInt(pmHPTf.getText());
                if (loseHPVal > 0) {
                    c.setCurrentHP(loseHPVal);
                    currHealth.setText(Integer.toString(c.getCurrentHP()));
                }
                else {
                    c.setCurrentHP(0);
                    currHealth.setText(Integer.toString(c.getCurrentHP()));
                    deathRollsBtn.fire();
                }
                grid.getChildren().remove(errMsg);
            }
            else {
                errMsg.setFill(Color.FIREBRICK);
                grid.add(errMsg,3,addHPRow);
            }
            pmHPTf.clear();
        }
    });

    row++;

    Button setmaxhp = new Button("Refill HP");
    grid.add(setmaxhp,1,row);
    setmaxhp.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            c.setCurrentHP(c.getMaxHP());
            currHealth.setText(Integer.toString(c.getCurrentHP()));
            pmHPTf.clear();
        }
    });

    ////////////////////////////
    //////// DEATH ROLLS ///////
    ////////////////////////////

    deathRollsBtn.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            Button revive = new Button("Revive");
            Label deathSavesLabel = new Label("Death Saves");
            deathSavesLabel.setId("deathSavesLabel");
            Label fails = new Label("Failures:   ");
            Label saves = new Label("Successes:");
            HBox savesHb = new HBox(10);
            HBox failsHb = new HBox(10);

            VBox sflabels = new VBox(10);
            sflabels.getChildren().addAll(saves,fails);
            VBox cbs = new VBox(10);
            cbs.getChildren().addAll(savesHb,failsHb);

            HBox vbs = new HBox(10);
            vbs.getChildren().addAll(sflabels,cbs);
            
            CheckBox[] savesArray = new CheckBox[3];
            CheckBox[] failsArray = new CheckBox[3];
            Text saved = new Text("You are stable.");
            saved.setFill(Color.GREEN);
            Text died = new Text("You have died.");
            died.setFill(Color.FIREBRICK);

            for (int i = 0; i < 3; i++) {
                CheckBox savescb = new CheckBox();
                CheckBox failscb = new CheckBox();
                failscb.setAllowIndeterminate(false);
                savescb.setAllowIndeterminate(false);
                failsArray[i] = failscb;
                savesArray[i] = savescb;

                savesHb.getChildren().add(savescb);
                failsHb.getChildren().add(failscb);

                savescb.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    public void changed(ObservableValue<? extends Boolean> ov, Boolean oldVal, Boolean newVal) {
                        if (newVal) {
                            c.setSaveRoll(c.getSaveRoll() + 1);
                        }
                        else {
                            c.setSaveRoll(c.getSaveRoll() - 1);
                        }
                        if (c.getSaveRoll() == 3) {
                            c.setSaveRoll(0);
                            c.setDeathRoll(0);
                            grid.getChildren().remove(died);
                            grid.add(saved,2,addHPRow);
                            for (int j=0; j < 3; j++) {
                                savesArray[j].setDisable(true);
                                failsArray[j].setDisable(true);
                            }
                        }
                    }
                });
                failscb.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    public void changed(ObservableValue<? extends Boolean> ov, Boolean oldVal, Boolean newVal) {
                        if (newVal) {
                            c.setDeathRoll(c.getDeathRoll() + 1);
                        }
                        else {
                            c.setDeathRoll(c.getDeathRoll() - 1);
                        }
                        if (c.getDeathRoll() == 3) {
                            c.setSaveRoll(0);
                            c.setDeathRoll(0);
                            grid.getChildren().remove(saved);
                            grid.add(died,2,addHPRow);
                            for (int j=0; j < 3; j++) {
                                savesArray[j].setDisable(true);
                                failsArray[j].setDisable(true);
                            }
                        }
                    }
                });
            }
            

            grid.getChildren().remove(currHealth);
            grid.getChildren().remove(currHealthLabel);
            grid.getChildren().remove(pmHPTf);
            grid.getChildren().remove(setmaxhp);
            grid.getChildren().remove(saved);
            grid.getChildren().remove(died);
            grid.getChildren().remove(hbpmHP);
            grid.add(vbs,1,currHPRow,1,2);
            grid.add(deathSavesLabel,0,currHPRow);
            grid.add(revive,2,currHPRow);

            revive.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    c.setSaveRoll(0);
                    c.setDeathRoll(0);
                    c.setCurrentHP(1);

                    grid.getChildren().remove(deathSavesLabel);
                    grid.getChildren().remove(vbs);
                    grid.getChildren().remove(revive);
                    grid.getChildren().remove(saved);
                    grid.getChildren().remove(died);
                    grid.add(currHealthLabel,0,currHPRow);
                    currHealth.setText(Integer.toString(c.getCurrentHP()));
                    grid.add(currHealth,1,currHPRow);
                    
                    grid.add(pmHPTf,1, addHPRow);
                    grid.add(hbpmHP,2,addHPRow);

                    grid.add(setmaxhp,1,addHPRow+1);
                }
            });
                    
        }
    });

    row++;
    /////////// TEMP HP ///////////
	Label tempHealthLabel = new Label("Temp HP:");
	grid.add(tempHealthLabel,0,row);
	TextField tempHealthTf = new TextField();
    tempHealthTf.setId("locked-tf");
    tempHealthTf.setPrefWidth(60);
	tempHealthTf.setText(Integer.toString(c.getTempHP()));
	tempHealthTf.setEditable(false);
	grid.add(tempHealthTf,1,row);
	final int tempHealthRow = row;
	
	ToggleButton tempHealthbtn = new ToggleButton("edit");
	tempHealthbtn.setOnAction(new EventHandler<ActionEvent>() {
		//for checking valid new max HP
		int previousTempHP = c.getTempHP();
		Text errTempHP = new Text("New Temp HP was not an integer!");
		@Override
		public void handle(ActionEvent e) {
		    if (tempHealthbtn.isSelected()) {
                previousTempHP = c.getTempHP();
                tempHealthTf.setEditable(true);
                tempHealthTf.setId("unlocked-tf");
		    }
		    else {
                previousTempHP = c.getTempHP();
				tempHealthTf.setEditable(false);
				tempHealthTf.setId("locked-tf");
				boolean isInt = isInteger(tempHealthTf.getText());
				if (isInt) {
					c.setTempHP(Integer.parseInt(tempHealthTf.getText()));
					grid.getChildren().remove(errTempHP);
				}
				else {
					errTempHP.setFill(Color.FIREBRICK);
					grid.getChildren().remove(errTempHP);
					grid.add(errTempHP,3,tempHealthRow);
					tempHealthTf.setText(Integer.toString(previousTempHP));
				}
			}
		}
	});
	grid.add(tempHealthbtn,2,row);

    row++;
    
    //////////////////////////////////////////
    //////////////////////////////////////////
    //////////////////////////////////////////
    
    // Give some space before stats
    row++;
    row++;
    row++;

    //////////////////////////////////////////
    //////////////// STATS ///////////////////
    //////////////////////////////////////////
    
    Label insp = new Label("Inspiration");
    Label prof = new Label("Proficiency");
    Label str = new Label("Strength");
    Label con = new Label("Constitution");
    Label dex = new Label("Dexterity");
    Label intel = new Label("Intelligence");
    Label wis = new Label("Wisdom");
    Label cha = new Label("Charisma");

    TextField inspTf = new TextField();
    inspTf.setId("locked-tf");
    inspTf.setText(Integer.toString(c.getInspiration()));
    inspTf.setEditable(false);
    inspTf.setPrefWidth(40);

    profTf.setId("locked-tf");
    profTf.setText(fmt.format(c.getProficiencyBonus()));
    profTf.setEditable(false);
    profTf.setPrefWidth(40);

    TextField strTf = new TextField();
    strTf.setId("locked-tf");
    strTf.setText(Integer.toString(c.getStr()));
    Label strMod = new Label(fmt.format(calcMod(c.getStr())));
    strTf.setEditable(false);
    strTf.setPrefWidth(40);
    HBox strhb = new HBox(10);
    strhb.getChildren().addAll(strTf,strMod);

    TextField conTf = new TextField();
    conTf.setId("locked-tf");
    conTf.setText(Integer.toString(c.getCons()));
    Label conMod = new Label(fmt.format(calcMod(c.getCons())));
    conTf.setEditable(false);
    conTf.setPrefWidth(40);
    HBox conhb = new HBox(10);
    conhb.getChildren().addAll(conTf,conMod);

    TextField dexTf = new TextField();
    dexTf.setId("locked-tf");
    dexTf.setText(Integer.toString(c.getDex()));
    Label dexMod = new Label(fmt.format(calcMod(c.getDex())));
    dexTf.setEditable(false);
    dexTf.setPrefWidth(40);
    HBox dexhb = new HBox(10);
    dexhb.getChildren().addAll(dexTf,dexMod);

    TextField intelTf = new TextField();
    intelTf.setId("locked-tf");
    intelTf.setText(Integer.toString(c.getInt()));
    Label intelMod = new Label(fmt.format(calcMod(c.getInt())));
    intelTf.setEditable(false);
    intelTf.setPrefWidth(40);
    HBox intelhb = new HBox(10);
    intelhb.getChildren().addAll(intelTf,intelMod);

    TextField wisTf = new TextField();
    wisTf.setId("locked-tf");
    wisTf.setText(Integer.toString(c.getWis()));
    Label wisMod = new Label(fmt.format(calcMod(c.getWis())));
    wisTf.setEditable(false);
    wisTf.setPrefWidth(40);
    HBox wishb = new HBox(10);
    wishb.getChildren().addAll(wisTf,wisMod);

    TextField chaTf = new TextField();
    chaTf.setId("locked-tf");
    chaTf.setText(Integer.toString(c.getChar()));
    Label chaMod = new Label(fmt.format(calcMod(c.getChar())));
    chaTf.setEditable(false);
    chaTf.setPrefWidth(40);
    HBox chahb = new HBox(10);
    chahb.getChildren().addAll(chaTf,chaMod);


    VBox statvb1 = new VBox(10);
    statvb1.getChildren().addAll(insp,inspTf,prof,profTf,str,strhb,con,conhb,dex,dexhb,intel,intelhb,wis,wishb,cha,chahb);


    ToggleButton statbtn = new ToggleButton("Edit Stats");
    statvb1.getChildren().add(statbtn);

    statbtn.setOnAction(new EventHandler<ActionEvent>() {
        int previousStr = c.getStr();
        int previousCon = c.getCons();
        int previousDex = c.getDex();
        int previousInt = c.getInt();
        int previousWis = c.getWis();
        int previousCha = c.getChar();
        int previousProf = c.getProficiencyBonus();
        int previousInsp = c.getInspiration();
        Text errMsg = new Text("One of the Stats was not an integer!");
        @Override
        public void handle(ActionEvent e) {
           if (statbtn.isSelected()) {
                previousStr = c.getStr();
                previousCon = c.getCons();
                previousDex = c.getDex();
                previousInt = c.getInt();
                previousWis = c.getWis();
                previousCha = c.getChar();
                previousProf = c.getProficiencyBonus();
                previousInsp = c.getInspiration();

                strTf.setEditable(true);
                conTf.setEditable(true);
                dexTf.setEditable(true);
                intelTf.setEditable(true);
                wisTf.setEditable(true);
                chaTf.setEditable(true);
                profTf.setEditable(true);
                inspTf.setEditable(true);

                strTf.setId("unlocked-tf");
                conTf.setId("unlocked-tf");
                dexTf.setId("unlocked-tf");
                intelTf.setId("unlocked-tf");
                wisTf.setId("unlocked-tf");
                chaTf.setId("unlocked-tf");
                profTf.setId("unlocked-tf");
                inspTf.setId("unlocked-tf");

            }
            else {
                strTf.setEditable(false);
                conTf.setEditable(false);
                dexTf.setEditable(false);
                intelTf.setEditable(false);
                wisTf.setEditable(false);
                chaTf.setEditable(false);
                profTf.setEditable(false);
                inspTf.setEditable(false);

                strTf.setId("locked-tf");
                conTf.setId("locked-tf");
                dexTf.setId("locked-tf");
                intelTf.setId("locked-tf");
                wisTf.setId("locked-tf");
                chaTf.setId("locked-tf");
                profTf.setId("locked-tf");
                inspTf.setId("locked-tf");

                boolean strIsInt = isInteger(strTf.getText());
                boolean conIsInt = isInteger(conTf.getText());
                boolean dexIsInt = isInteger(dexTf.getText());
                boolean intelIsInt = isInteger(intelTf.getText());
                boolean wisIsInt = isInteger(wisTf.getText());
                boolean chaIsInt = isInteger(chaTf.getText());
                boolean profIsInt = isInteger(profTf.getText());
                boolean inspIsInt = isInteger(inspTf.getText());

                if (strIsInt && conIsInt && dexIsInt && intelIsInt && wisIsInt && chaIsInt && profIsInt && inspIsInt) {
                    c.setStr(Integer.parseInt(strTf.getText()));
                    c.setCons(Integer.parseInt(conTf.getText()));
                    c.setDex(Integer.parseInt(dexTf.getText()));
                    c.setInt(Integer.parseInt(intelTf.getText()));
                    c.setWis(Integer.parseInt(wisTf.getText()));
                    c.setChar(Integer.parseInt(chaTf.getText()));

                    strTf.setText(strTf.getText().replaceFirst("^0+(?!$)",""));
                    conTf.setText(conTf.getText().replaceFirst("^0+(?!$)",""));
                    dexTf.setText(dexTf.getText().replaceFirst("^0+(?!$)",""));
                    intelTf.setText(intelTf.getText().replaceFirst("^0+(?!$)",""));
                    wisTf.setText(wisTf.getText().replaceFirst("^0+(?!$)",""));
                    chaTf.setText(chaTf.getText().replaceFirst("^0+(?!$)",""));
                    inspTf.setText(inspTf.getText().replaceFirst("^0+(?!$)",""));

                    strMod.setText(fmt.format(calcMod(Integer.parseInt(strTf.getText()))));
                    conMod.setText(fmt.format(calcMod(Integer.parseInt(conTf.getText()))));
                    dexMod.setText(fmt.format(calcMod(Integer.parseInt(dexTf.getText()))));
                    intelMod.setText(fmt.format(calcMod(Integer.parseInt(intelTf.getText()))));
                    wisMod.setText(fmt.format(calcMod(Integer.parseInt(wisTf.getText()))));
                    chaMod.setText(fmt.format(calcMod(Integer.parseInt(chaTf.getText()))));

                    c.setProficiencyBonus(Integer.parseInt(profTf.getText()));

                    profTf.setText(fmt.format(c.getProficiencyBonus()));
                    c.setInspiration(Integer.parseInt(inspTf.getText()));
                    statvb1.getChildren().remove(errMsg);
                }
                else {
                    errMsg.setFill(Color.FIREBRICK);
                    statvb1.getChildren().remove(errMsg);
                    statvb1.getChildren().add(errMsg);
                    strTf.setText(Integer.toString(previousStr));
                    conTf.setText(Integer.toString(previousCon));
                    dexTf.setText(Integer.toString(previousDex));
                    intelTf.setText(Integer.toString(previousInt));
                    wisTf.setText(Integer.toString(previousWis));
                    chaTf.setText(Integer.toString(previousCon));
                    profTf.setText(fmt.format(previousProf));
                    inspTf.setText(Integer.toString(previousInsp));

                    strMod.setText(fmt.format(calcMod(Integer.parseInt(strTf.getText()))));
                    conMod.setText(fmt.format(calcMod(Integer.parseInt(conTf.getText()))));
                    dexMod.setText(fmt.format(calcMod(Integer.parseInt(dexTf.getText()))));
                    intelMod.setText(fmt.format(calcMod(Integer.parseInt(intelTf.getText()))));
                    wisMod.setText(fmt.format(calcMod(Integer.parseInt(wisTf.getText()))));
                    chaMod.setText(fmt.format(calcMod(Integer.parseInt(chaTf.getText()))));
                }
            }
        }
    });
    
    ///////////////////////////////////////
    ////////// OTHER STATS ////////////////
    ///////////////////////////////////////
    Label armor = new Label("Armor:");
    Label init = new Label("Initiative:");
    Label speed = new Label("Speed:");
    Label hitdice = new Label("Hit Dice:");

    TextField armorTf = new TextField();
    armorTf.setId("locked-tf");
    armorTf.setText(Integer.toString(c.getArmor()));
    armorTf.setEditable(false);
    armorTf.setPrefWidth(40);

    TextField initTf = new TextField();
    initTf.setId("locked-tf");
    initTf.setText(fmt.format(c.getInitiative()));
    initTf.setEditable(false);
    initTf.setPrefWidth(40);

    TextField speedTf = new TextField();
    speedTf.setId("locked-tf");
    speedTf.setText(Integer.toString(c.getSpeed()));
    speedTf.setEditable(false);
    speedTf.setPrefWidth(40);

    TextField hdTf = new TextField();
    hdTf.setId("locked-tf");
    hdTf.setText(c.getHitDice());
    hdTf.setEditable(false);
    hdTf.setPrefWidth(60);

    HBox stathb0 = new HBox(10);
    stathb0.getChildren().addAll(armor,armorTf,init,initTf,speed,speedTf,hitdice,hdTf);

    grid.add(stathb0,1,row);

    final int statsRow = row;

    ToggleButton otherStatbtn = new ToggleButton("edit");
    statvb1.getChildren().add(otherStatbtn);
    grid.add(otherStatbtn,2,row);

    otherStatbtn.setOnAction(new EventHandler<ActionEvent>() {
        int previousArmor = c.getArmor();
        int previousInit = c.getInitiative();
        int previousSpeed = c.getSpeed();
        String previousHitDice = c.getHitDice();
        Text errMsg = new Text("One of the Stats was not an integer!");
        @Override
        public void handle(ActionEvent e) {
           if (otherStatbtn.isSelected()) {
                previousArmor = c.getArmor();
                previousInit = c.getInitiative();
                previousSpeed = c.getSpeed();
                previousHitDice = c.getHitDice();
                armorTf.setEditable(true);
                initTf.setEditable(true);
                speedTf.setEditable(true);
                hdTf.setEditable(true);

                armorTf.setId("unlocked-tf");
                initTf.setId("unlocked-tf");
                speedTf.setId("unlocked-tf");
                hdTf.setId("unlocked-tf");
           }
           else {
               armorTf.setEditable(false);
               initTf.setEditable(false);
               speedTf.setEditable(false);
               hdTf.setEditable(false);
               armorTf.setId("locked-tf");
               speedTf.setId("locked-tf");
               initTf.setId("locked-tf");
               hdTf.setId("locked-tf");

               boolean armorIsInt = isInteger(armorTf.getText());
               boolean speedIsInt = isInteger(speedTf.getText());
               boolean initIsInt = isInteger(initTf.getText());
               if (armorIsInt && speedIsInt && initIsInt) {
                   c.setArmor(Integer.parseInt(armorTf.getText()));
                   armorTf.setText(armorTf.getText().replaceFirst("^0+(?!$)",""));

                   c.setInitiative(Integer.parseInt(initTf.getText()));
                   initTf.setText(fmt.format(c.getInitiative()));

                   c.setSpeed(Integer.parseInt(speedTf.getText()));
                   speedTf.setText(speedTf.getText().replaceFirst("^0+(?!$)",""));

                   c.setHitDice(hdTf.getText());
                   grid.getChildren().remove(errMsg);
               }
               else {
                   errMsg.setFill(Color.FIREBRICK);
                   grid.getChildren().remove(errMsg);
                   grid.add(errMsg,3,statsRow);
                   armorTf.setText(Integer.toString(previousArmor));
                   speedTf.setText(Integer.toString(previousSpeed));
                   initTf.setText(fmt.format(previousInit));
                   hdTf.setText(previousHitDice);
               }
           }
        }
    });
    row++;
    row++;

    ///////////////////////////////////////////
    ///////////////////////////////////////////

    Button skillsBtn = new Button("Skills");
    Button spellsBtn = new Button("Spells");
    Button inventoryBtn = new Button("Inventory"); 
    Button idealsBtn = new Button("Ideals");
    Button descriptionBtn = new Button("Description");
    Button weaponsBtn = new Button("Weapons");
    Button featuresBtn = new Button("Features");
    Button languagesBtn = new Button("Languages");
    Button bondsBtn = new Button("Bonds");
    Button flawsBtn = new Button("Flaws");
    Button savingThrowsBtn = new Button("Saving Throws");
    Button notesBtn = new Button("Notes");


    HBox hbbtns1 = new HBox(10);
    hbbtns1.setAlignment(Pos.CENTER);
    hbbtns1.getChildren().addAll(skillsBtn,spellsBtn,weaponsBtn,inventoryBtn,languagesBtn);
    HBox hbbtns2 = new HBox(10);
    hbbtns2.setAlignment(Pos.CENTER);
    hbbtns2.getChildren().addAll(featuresBtn,idealsBtn,bondsBtn,flawsBtn,descriptionBtn);

    HBox hbbtns3 = new HBox(10);
    hbbtns3.setAlignment(Pos.CENTER);
    hbbtns3.getChildren().addAll(savingThrowsBtn,notesBtn);

    VBox vbbtns = new VBox(10);
    vbbtns.setAlignment(Pos.CENTER);
    vbbtns.getChildren().addAll(hbbtns1,hbbtns2,hbbtns3);
    grid.add(vbbtns,1,row);

    row++;

    ///////////// Save Button /////////////////
    Button save = new Button("Save Character");
    Button saveAs = new Button("Save As");
    save.setId("saveBtn");
    HBox hbsave = new HBox(10);
    hbsave.getChildren().addAll(saveAs,save);
    hbsave.setAlignment(Pos.BOTTOM_RIGHT);

    grid.add(hbsave,1,row);
    int saveRow = row;

    saveAs.setOnAction(new EventHandler<ActionEvent>() {
        Text notice = new Text("Character was saved");
        public void handle(ActionEvent e) {
            String file = saveCharacterAs(stage);
            if (!file.isEmpty()) {
                Character.SaveCharacter(c,file);
                fileName = file;
                notice.setFill(Color.FIREBRICK);
                grid.getChildren().remove(notice);
                grid.add(notice,1,saveRow+1);
            }
        }
    });
            

    save.setOnAction(new EventHandler<ActionEvent>() {
        Text notice = new Text("Character was saved.");
        @Override
        public void handle(ActionEvent e) {
            if (fileName == null || fileName.isEmpty()) {
                saveAs.fire();
            }
            else {
                Character.SaveCharacter(c,fileName);
                notice.setFill(Color.FIREBRICK);
                grid.getChildren().remove(notice);
                grid.add(notice,1,saveRow + 1);
            }
        }
    });


    ///////////////////////////////
    ///////// SKILLS PAGE /////////
    ///////////////////////////////

    Stage skillsStage = new Stage();
    skillsBtn.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            SkillsPage(skillsStage);
        }
    });

    ///////////////////////////////
    ///////// SAVINGTHROWS PAGE /////////
    ///////////////////////////////

    Stage savingThrowsStage = new Stage();
    savingThrowsBtn.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            SavingThrowsPage(savingThrowsStage);
        }
    });
    //////////////////////////////////////
    ////////// Inventory Page ////////////
    //////////////////////////////////////

    Stage inventoryStage = new Stage();
    inventoryBtn.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            InventoryPage(inventoryStage);
        }
    });

    //////////////////////////////////
    ///////// LANGUAGES PAGE /////////
    //////////////////////////////////

    Stage languagesStage = new Stage();
    languagesBtn.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            LanguagesPage(languagesStage);
        }
    });

    ///////////////////////////////
    //////// WEAPONS PAGE /////////
    ///////////////////////////////

    Stage weaponsStage = new Stage();
    weaponsBtn.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            WeaponsPage(weaponsStage);
        }
    });




    //////////////////////////////////
    ///////// IDEALS PAGE ////////////
    //////////////////////////////////

    Stage idealsStage = new Stage();
    idealsBtn.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            IdealsPage(idealsStage);
        }
    });

    //////////////////////////////////
    ///////// FLAWS PAGE /////////////
    //////////////////////////////////

    Stage flawsStage = new Stage();
    flawsBtn.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            FlawsPage(flawsStage);
        }
    });

    //////////////////////////////////
    ///////// BONDS PAGE /////////////
    //////////////////////////////////

    Stage bondsStage = new Stage();
    bondsBtn.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            BondsPage(bondsStage);
        }
    });
    
    /////////////////////////////////////
    ///////// FEATURES PAGE /////////////
    /////////////////////////////////////

    Stage featuresStage = new Stage();
    featuresBtn.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            FeaturesPage(featuresStage);
        }
    });
    
    ////////////////////////////////////
    ////////////// NOTES ///////////////
    ////////////////////////////////////
    
    Stage notesStage = new Stage();
    notesBtn.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            NotesPage(notesStage);
        }
    });
    
    //////////////////////////////////////////
    ////////////// DESCRIPTION ///////////////
    //////////////////////////////////////////
    
    Stage descriptionStage = new Stage();
    descriptionBtn.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            DescriptionPage(descriptionStage);
        }
    });


    ////////////////////////////////////
    /////////// SPELLS PAGE ////////////
    ////////////////////////////////////
    
    Stage spellsStage = new Stage();
    spellsBtn.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            SpellsPage(spellsStage,spellsBtn);
        }
    });
       


    ////////////////////////////////////////////
    ////////////////////////////////////////////
    
    ////////////////////////////////////////////
    ///////////// Border Setup /////////////////
    ////////////////////////////////////////////
    

    border.setMargin(characterName, new Insets(10,10,10,10));
    
    border.setTop(characterName);
    border.setCenter(grid);
    border.setBottom(hbsave);
    border.setLeft(statvb1);

    ///////////////////////////////////////////
    ///////////////////////////////////////////
    
    ////////////////////////////////////////
    ///////////// SAVE ON EXIT /////////////
    ////////////////////////////////////////
    
    Stage saveExitStage = new Stage();
    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
        public void handle(WindowEvent we) {
            we.consume();
            saveExitStage.close();
            saveExitStage.setTitle("Save Character?");
            GridPane segrid = new GridPane();
            segrid.setAlignment(Pos.CENTER);
            segrid.setHgap(10);
            segrid.setVgap(10);
            Scene seScene = new Scene(segrid,400,150);
            saveExitStage.setScene(seScene);
            saveExitStage.show();

            Label seLabel = new Label("Save before closing?");
            segrid.add(seLabel,0,0);
            HBox sebtns = new HBox(10);
            Button yesSE = new Button("Yes");
            Button noSE = new Button("No");
            Button cancelSE = new Button("Cancel");
            sebtns.getChildren().addAll(yesSE,noSE,cancelSE);
            segrid.add(sebtns,0,1);

            yesSE.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    if (fileName.isEmpty()) {
                        String file = saveCharacterAs(saveExitStage);
                        Character.SaveCharacter(c,fileName);
                    }
                    else {
                        Character.SaveCharacter(c,fileName);
                    }
                    saveExitStage.close();
                    stage.close();
                    System.exit(0);
                }
            });

            noSE.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    saveExitStage.close();
                    stage.close();
                    System.exit(0);
                }
            });
            cancelSE.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    saveExitStage.close();
                }
            });
        }
    });


	stage.show();	
	}

    public void SkillsPage(Stage skillsStage) {
        skillsStage.close();
        skillsStage.setTitle("Skills Page");

        Label skillsTitle = new Label("Skills");
        skillsTitle.setId("title");
        VBox vbSkills = new VBox(10);

        boolean[] skillsList = c.getProficiencies();
        String[] skills = {"Acrobatics (Dex)", "Animal Handling (Wis)", "Arcana (Int)",
            "Athletics (Str)", "Deception (Cha)", "History (Int)", "Insight (Wis)",
            "Intimidation (Cha)", "Investigation (Int)", "Medicine (Wis)", "Nature (Int)",
            "Perception (Wis)", "Performance (Cha)", "Persuasion (Cha)", "Religion (Int)",
            "Sleight of Hand (Dex)", "Stealth (Dex)", "Survival (Wis)"};

        int[] skillsMod = {calcMod(c.getDex()), calcMod(c.getWis()), calcMod(c.getInt()),
            calcMod(c.getStr()), calcMod(c.getChar()), calcMod(c.getInt()), calcMod(c.getWis()),
            calcMod(c.getChar()), calcMod(c.getInt()), calcMod(c.getWis()), calcMod(c.getInt()),
            calcMod(c.getWis()), calcMod(c.getChar()), calcMod(c.getChar()), calcMod(c.getInt()),
            calcMod(c.getDex()), calcMod(c.getDex()), calcMod(c.getWis())};

        for (int i = 0; i < skillsList.length; i++) {

            String nxtItem = skills[i];
            Label skillsLabel = new Label(nxtItem);
            Label modLabel = new Label();
            if (c.getProficiencies()[i]) {
                modLabel.setText(fmt.format(skillsMod[i] + c.getProficiencyBonus()));
            }
            else {
                modLabel.setText(fmt.format(skillsMod[i]));
            }
            HBox hbSkillsList = new HBox(10);
            CheckBox isProficient = new CheckBox();
            isProficient.setSelected(c.getProficiencies()[i]);
            isProficient.setAllowIndeterminate(false);
            final int j = i;
            isProficient.selectedProperty().addListener(new ChangeListener<Boolean>() {
                public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
                    c.setProficiencyVal(new_val,j);
                    if (new_val) {
                        modLabel.setText(fmt.format(skillsMod[j] + c.getProficiencyBonus()));
                    }
                    else if (!new_val) {
                        modLabel.setText(fmt.format(skillsMod[j]));
                    }
                }
            });

            hbSkillsList.getChildren().addAll(isProficient,modLabel, skillsLabel);

            vbSkills.getChildren().add(hbSkillsList);


        }

        Button doneSkills = new Button("Done");
        HBox hbaddSkills = new HBox(10);
        VBox vbskillsBtns = new VBox(10);
        vbskillsBtns.getChildren().addAll(doneSkills);


        BorderPane bpSkills = new BorderPane();
        ScrollPane skillsRoot = new ScrollPane();
        bpSkills.setPadding(new Insets(20));
        bpSkills.setMargin(skillsTitle,new Insets(12,12,12,12));
        bpSkills.setMargin(vbSkills,new Insets(10,10,10,10));
        bpSkills.setTop(skillsTitle);
        bpSkills.setCenter(vbSkills);
        bpSkills.setBottom(vbskillsBtns);

        skillsRoot.setContent(bpSkills);

        Scene skillsscene = new Scene(skillsRoot);
        skillsscene.getStylesheets().add("lib/ListPage.css");

        skillsStage.setScene(skillsscene);

        doneSkills.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                skillsStage.close();
            }
        });

        skillsStage.show();


    }

    public void SavingThrowsPage(Stage savingThrowsStage) {
        savingThrowsStage.close();
        savingThrowsStage.setTitle("Saving Throws Page");

        Label savingThrowsTitle = new Label("Saving Throws");
        savingThrowsTitle.setId("title");
        VBox vbSavingThrows = new VBox(10);

        boolean[] savingThrowsList = c.getSavingThrows();
        String[] savingThrows = { "Strength", "Dexterity", "Constitution", "Intelligence", "Wisdom", "Charisma" };

        int[] savingThrowsMod = { calcMod(c.getStr()), calcMod(c.getDex()), calcMod(c.getCons()), calcMod(c.getInt()),
            calcMod(c.getWis()), calcMod(c.getChar()) };

        for (int i = 0; i < savingThrowsList.length; i++) {

            String nxtItem = savingThrows[i];
            Label savingThrowsLabel = new Label(nxtItem);
            Label modLabel = new Label();
            if (c.getSavingThrows()[i]) {
                modLabel.setText(fmt.format(savingThrowsMod[i] + c.getProficiencyBonus()));
            }
            else {
                modLabel.setText(fmt.format(savingThrowsMod[i]));
            }
            HBox hbSavingThrowsList = new HBox(10);
            CheckBox isProficient = new CheckBox();
            isProficient.setSelected(c.getSavingThrows()[i]);
            isProficient.setAllowIndeterminate(false);
            final int j = i;
            isProficient.selectedProperty().addListener(new ChangeListener<Boolean>() {
                public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
                    c.setSavingThrowsVal(new_val,j);
                    if (new_val) {
                        modLabel.setText(fmt.format(savingThrowsMod[j] + c.getProficiencyBonus()));
                    }
                    else if (!new_val) {
                        modLabel.setText(fmt.format(savingThrowsMod[j]));
                    }
                }
            });

            hbSavingThrowsList.getChildren().addAll(isProficient,modLabel, savingThrowsLabel);

            vbSavingThrows.getChildren().add(hbSavingThrowsList);


        }

        Button doneSavingThrows = new Button("Done");
        HBox hbaddSavingThrows = new HBox(10);
        VBox vbsavingThrowsBtns = new VBox(10);
        vbsavingThrowsBtns.getChildren().addAll(doneSavingThrows);


        BorderPane bpSavingThrows = new BorderPane();
        ScrollPane savingThrowsRoot = new ScrollPane();
        bpSavingThrows.setPadding(new Insets(20));
        bpSavingThrows.setMargin(savingThrowsTitle,new Insets(12,12,12,12));
        bpSavingThrows.setMargin(vbSavingThrows,new Insets(10,10,10,10));
        bpSavingThrows.setTop(savingThrowsTitle);
        bpSavingThrows.setCenter(vbSavingThrows);
        bpSavingThrows.setBottom(vbsavingThrowsBtns);

        savingThrowsRoot.setContent(bpSavingThrows);

        Scene savingThrowsscene = new Scene(savingThrowsRoot);
        savingThrowsscene.getStylesheets().add("lib/ListPage.css");

        savingThrowsStage.setScene(savingThrowsscene);

        doneSavingThrows.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                savingThrowsStage.close();
            }
        });

        savingThrowsStage.show();

    }

    public void InventoryPage(Stage inventoryStage) {
        inventoryStage.close();
        inventoryStage.setTitle("Inventory Page");



        Label inventoryTitle = new Label("Inventory");
        inventoryTitle.setId("title");
        VBox vbInventory = new VBox(10);

        ArrayList<String> inventoryList = c.getInventory();
        Iterator<String> itr = inventoryList.iterator();
        int index = 0;
        while (itr.hasNext()) {
            String nxtItem = itr.next();
            final int currIndex = index;
            String[] splitItem = nxtItem.split("--");
            TextArea description = new TextArea();
            description.setWrapText(true);
            description.setPromptText("Item Description");
            try {
                description.setText(splitItem[1]); 
            }
            catch (ArrayIndexOutOfBoundsException ioe) {
                description.setText("");
            }
            Label inventorylabel = new Label(splitItem[0]);
            HBox hbInventoryList = new HBox(10);


            Button rm = new Button("remove");
            Button info = new Button("info");
            hbInventoryList.getChildren().addAll(inventorylabel,info,rm);

            vbInventory.getChildren().add(hbInventoryList);
            index++;

            ////// Remove button ///////
            Stage confirmRm = new Stage();
            rm.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    confirmRm.close();
                    confirmRm.setTitle("Are you sure?");
                    GridPane rmgrid = new GridPane();
                    rmgrid.setAlignment(Pos.CENTER);
                    rmgrid.setHgap(10);
                    rmgrid.setVgap(10);
                    Scene rmscene = new Scene(rmgrid,400,150);
                    confirmRm.setScene(rmscene);
                    confirmRm.show();

                    Label rmLabel = new Label("remove " + splitItem[0] + ". Are you sure?");
                    rmgrid.add(rmLabel,0,0);
                    Button yesRm = new Button("Yes");
                    Button noRm = new Button("Cancel");
                    HBox hbynrm = new HBox(10);
                    hbynrm.getChildren().addAll(yesRm,noRm);
                    rmgrid.add(hbynrm,0,1);

                    yesRm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            vbInventory.getChildren().remove(hbInventoryList);
                            inventoryList.remove(nxtItem);
                            c.setInventory(inventoryList);
                            confirmRm.close();
                        }
                    });
                    noRm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            confirmRm.close();
                        }
                    });
                }
            });

            Stage infoStage = new Stage();
            info.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    infoStage.close();
                    GridPane infoGrid = new GridPane();
                    infoGrid.setAlignment(Pos.CENTER);
                    infoGrid.setHgap(10);
                    infoGrid.setVgap(10);
                    Scene infoScene = new Scene(infoGrid);
                    infoStage.setScene(infoScene);

                    infoScene.getStylesheets().add("lib/TextAreaPage.css");
                    Label itemName = new Label();
                    itemName.setText(inventorylabel.getText());

                    Button done = new Button("done");
                    Button saveDesc = new Button("Save");
                    HBox descBtns = new HBox(10);
                    descBtns.getChildren().addAll(done,saveDesc);

                    infoGrid.add(itemName,0,0);
                    infoGrid.add(description,0,1);
                    infoGrid.add(descBtns,0,2);

                    done.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            String newItem = itemName.getText() + "--" + description.getText();
                            inventoryList.set(currIndex,newItem);
                            c.setInventory(inventoryList);
                            infoStage.close();
                        }
                    });
                    saveDesc.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            String newItem = itemName.getText() + "--" + description.getText();
                            inventoryList.set(currIndex,newItem);
                            c.setInventory(inventoryList);
                            Text msg = new Text("Saved");
                            msg.setFill(Color.FIREBRICK);
                            descBtns.getChildren().remove(msg);
                            descBtns.getChildren().add(msg);
                        }
                    });
                    infoStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        public void handle(WindowEvent we) {
                            String newItem = itemName.getText() + "--" + description.getText();
                            inventoryList.set(currIndex,newItem);
                            c.setInventory(inventoryList);
                        }
                    });
                    infoStage.show();
                }
            });

        }

        TextField addInventoryTf = new TextField();
        addInventoryTf.setPromptText("Add an Item");
        Button addInventory = new Button("Add Item");
        Button doneInventory = new Button("Done");
        HBox hbaddInventory = new HBox(10);
        hbaddInventory.getChildren().addAll(addInventoryTf,addInventory);
        VBox vbinventoryBtns = new VBox(10);
        vbinventoryBtns.getChildren().addAll(hbaddInventory,doneInventory);

        ScrollPane inventorySp = new ScrollPane();
        inventorySp.setPrefHeight(300);
        inventorySp.setContent(vbInventory);

        BorderPane bpInventory = new BorderPane();
        bpInventory.setPadding(new Insets(20));
        bpInventory.setMargin(inventoryTitle,new Insets(12,12,12,12));
        bpInventory.setMargin(inventorySp,new Insets(10,10,10,10));
        bpInventory.setTop(inventoryTitle);
        bpInventory.setCenter(inventorySp);
        bpInventory.setBottom(vbinventoryBtns);


        Scene inventoryscene = new Scene(bpInventory);
        inventoryscene.getStylesheets().add("lib/ListPage.css");

        inventoryStage.setScene(inventoryscene);

        //////// Add Inventory //////////
        addInventory.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (!addInventoryTf.getText().isEmpty()) {
                    inventoryList.add(addInventoryTf.getText());
                    int newRow = inventoryList.size() - 1;
                    c.setInventory(inventoryList);

                    Label newInventory = new Label(addInventoryTf.getText());
                    String newItem = newInventory.getText();

                    TextArea description = new TextArea();
                    description.setWrapText(true);
                    description.setPromptText("Item Description");

                    Button rm = new Button("remove");
                    Button info = new Button("info");
                    HBox hbNewInventory = new HBox(10);
                    hbNewInventory.getChildren().addAll(newInventory,info,rm);
                    vbInventory.getChildren().add(hbNewInventory);
                    addInventoryTf.clear();

                    ////// Remove button ///////
                    Stage confirmRm = new Stage();
                    rm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            confirmRm.close();
                            confirmRm.setTitle("Are you sure?");
                            GridPane rmgrid = new GridPane();
                            rmgrid.setAlignment(Pos.CENTER);
                            rmgrid.setHgap(10);
                            rmgrid.setVgap(10);
                            Scene rmscene = new Scene(rmgrid,400,150);
                            confirmRm.setScene(rmscene);
                            confirmRm.show();

                            Label rmLabel = new Label("remove " + newItem + ". Are you sure?");
                            rmgrid.add(rmLabel,0,0);
                            Button yesRm = new Button("Yes");
                            Button noRm = new Button("Cancel");
                            HBox hbynrm = new HBox(10);
                            hbynrm.getChildren().addAll(yesRm,noRm);
                            rmgrid.add(hbynrm,0,1);

                            yesRm.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent e) {
                                    vbInventory.getChildren().remove(hbNewInventory);
                                    inventoryList.remove(newInventory.getText());
                                    c.setInventory(inventoryList);
                                    confirmRm.close();
                                }
                            });
                            noRm.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent e) {
                                    confirmRm.close();
                                }
                            });
                        }
                    });

                    Stage infoStage = new Stage();
                    info.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            infoStage.close();

                            GridPane infoGrid = new GridPane();
                            infoGrid.setAlignment(Pos.CENTER);
                            infoGrid.setHgap(10);
                            infoGrid.setVgap(10);
                            Scene infoScene = new Scene(infoGrid);
                            infoStage.setScene(infoScene);

                            infoScene.getStylesheets().add("lib/TextAreaPage.css");

                            Label itemName = new Label();
                            itemName.setText(newInventory.getText());

                            Button done = new Button("done");
                            Button saveDesc = new Button("Save");
                            HBox descBtns = new HBox(10);
                            descBtns.getChildren().addAll(done,saveDesc);

                            infoGrid.add(itemName,0,0);
                            infoGrid.add(description,0,1);
                            infoGrid.add(descBtns,0,2);

                            done.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent e) {
                                    String newItem = itemName.getText() + "--" + description.getText();
                                    inventoryList.set(newRow,newItem);
                                    c.setInventory(inventoryList);
                                    infoStage.close();
                                }
                            });
                            saveDesc.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent e) {
                                    String newItem = itemName.getText() + "--" + description.getText();
                                    inventoryList.set(newRow,newItem);
                                    c.setInventory(inventoryList);
                                    Text msg = new Text("Save");
                                    msg.setFill(Color.FIREBRICK);
                                    descBtns.getChildren().remove(msg);
                                    descBtns.getChildren().add(msg);
                                }
                            });
                            infoStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                                public void handle(WindowEvent we) {
                                    String newItem = itemName.getText() + "--" + description.getText();
                                    inventoryList.set(newRow,newItem);
                                    c.setInventory(inventoryList);
                                }
                            });
                            infoStage.show();
                        }
                    });

                }
            }
        });



        doneInventory.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                c.setInventory(inventoryList);
                inventoryStage.close();
            }
        });

        inventoryStage.show();

    }

    public void LanguagesPage(Stage languagesStage) {
        languagesStage.close();
        languagesStage.setTitle("Languages Page");



        Label languagesTitle = new Label("Languages");
        languagesTitle.setId("title");
        VBox vbLanguages = new VBox(10);

        HashSet<String> languagesList = c.getLanguages();
        Iterator<String> itr = languagesList.iterator();

        while (itr.hasNext()) {
            String nxtItem = itr.next();
            Label languageslabel = new Label(nxtItem);
            HBox hbLanguagesList = new HBox(10);

            Button rm = new Button("remove");
            hbLanguagesList.getChildren().addAll(languageslabel,rm);

            vbLanguages.getChildren().add(hbLanguagesList);

            ////// Remove button ///////
            Stage confirmRm = new Stage();
            rm.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    confirmRm.close();
                    confirmRm.setTitle("Are you sure?");
                    GridPane rmgrid = new GridPane();
                    rmgrid.setAlignment(Pos.CENTER);
                    rmgrid.setHgap(10);
                    rmgrid.setVgap(10);
                    Scene rmscene = new Scene(rmgrid,400,150);
                    confirmRm.setScene(rmscene);
                    confirmRm.show();

                    Label rmLabel = new Label("remove " + nxtItem + ". Are you sure?");
                    rmgrid.add(rmLabel,0,0);
                    Button yesRm = new Button("Yes");
                    Button noRm = new Button("Cancel");
                    HBox hbynrm = new HBox(10);
                    hbynrm.getChildren().addAll(yesRm,noRm);
                    rmgrid.add(hbynrm,0,1);

                    yesRm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            vbLanguages.getChildren().remove(hbLanguagesList);
                            languagesList.remove(languageslabel.getText());
                            c.setLanguages(languagesList);
                            confirmRm.close();
                        }
                    });
                    noRm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            confirmRm.close();
                        }
                    });
                }
            });

        }

        ObservableList<String> options = FXCollections.observableArrayList( "Common", "Dwarvish", "Elvish",
                "Giant", "Gnomish", "Goblin",
                "Halfling", "Orc", "Abyssal",
                "Celestial", "Draconic", "Deep Speech",
                "Infernal", "Primordial", "Sylvan", "Undercommon");
        ComboBox<String> addLanguagesTf = new ComboBox<String>(options);
        addLanguagesTf.setEditable(true);
        addLanguagesTf.setTooltip(new Tooltip());
        addLanguagesTf.setPromptText("Add a Language");
        Button addLanguages = new Button("Add Language");
        Button doneLanguages = new Button("Done");
        HBox hbaddLanguages = new HBox(10);
        hbaddLanguages.getChildren().addAll(addLanguagesTf,addLanguages);
        VBox vblanguagesBtns = new VBox(10);
        vblanguagesBtns.getChildren().addAll(hbaddLanguages,doneLanguages);

        ScrollPane languagesSp = new ScrollPane();
        languagesSp.setContent(vbLanguages);

        BorderPane bpLanguages = new BorderPane();
        bpLanguages.setPadding(new Insets(20));
        bpLanguages.setMargin(languagesTitle,new Insets(12,12,12,12));
        bpLanguages.setMargin(languagesSp,new Insets(10,10,10,10));
        bpLanguages.setTop(languagesTitle);
        bpLanguages.setCenter(languagesSp);
        bpLanguages.setBottom(vblanguagesBtns);


        Scene languagesscene = new Scene(bpLanguages);
        languagesscene.getStylesheets().add("lib/ListPage.css");

        languagesStage.setScene(languagesscene);

        //////// Add Languages //////////
        addLanguages.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (!addLanguagesTf.getValue().toString().isEmpty()) {
                    languagesList.add(addLanguagesTf.getValue().toString());
                    int newRow = languagesList.size() - 1;
                    c.setLanguages(languagesList);
                    Label newLanguages = new Label(addLanguagesTf.getValue().toString());
                    Button rm = new Button("remove");
                    HBox hbnewLanguages = new HBox(10);
                    hbnewLanguages.getChildren().addAll(newLanguages,rm);
                    vbLanguages.getChildren().add(hbnewLanguages);
                    addLanguagesTf.setValue(null);

                    ////// Remove button ///////
                    Stage confirmRm = new Stage();
                    rm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            confirmRm.close();
                            confirmRm.setTitle("Are you sure?");
                            GridPane rmgrid = new GridPane();
                            rmgrid.setAlignment(Pos.CENTER);
                            rmgrid.setHgap(10);
                            rmgrid.setVgap(10);
                            Scene rmscene = new Scene(rmgrid,400,150);
                            confirmRm.setScene(rmscene);
                            confirmRm.show();

                            Label rmLabel = new Label("remove " +newLanguages.getText()+ ". Are you sure?");
                            rmgrid.add(rmLabel,0,0);
                            Button yesRm = new Button("Yes");
                            Button noRm = new Button("Cancel");
                            HBox hbynrm = new HBox(10);
                            hbynrm.getChildren().addAll(yesRm,noRm);
                            rmgrid.add(hbynrm,0,1);

                            yesRm.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent e) {
                                    vbLanguages.getChildren().remove(hbnewLanguages);
                                    languagesList.remove(newLanguages.getText());
                                    c.setLanguages(languagesList);
                                    confirmRm.close();
                                }
                            });
                            noRm.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent e) {
                                    confirmRm.close();
                                }
                            });
                        }
                    });
                }
            }
        });




        doneLanguages.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                c.setLanguages(languagesList);
                languagesStage.close();
            }
        });

        languagesStage.show();


    }

    public void WeaponsPage(Stage weaponsStage) {
        weaponsStage.close();
        weaponsStage.setTitle("Weapons Page");

        Label weaponsTitle = new Label("Weapons");
        weaponsTitle.setId("title");

        VBox weaponsHeader = new VBox(10);

        VBox vbWeapons = new VBox(10);

        ArrayList<String> weaponsList = c.getWeapons();
        Iterator<String> itr = weaponsList.iterator();

        HBox labelsHb = new HBox(80);
        Label proficient = new Label("Prof? Mod \t Weapon");
        Label weaponName = new Label("Weapon");
        Label damageLabel = new Label("\tDamage");
        //Label hitLabel = new Label("Modifier");
        Label dexWeaponLabel = new Label("\tFinesse?");

        labelsHb.getChildren().addAll(proficient,damageLabel,dexWeaponLabel);

        vbWeapons.getChildren().addAll(weaponsTitle,labelsHb);

        int index = 0;

        while (itr.hasNext()) {
            final int currIndex = index;
            String nxtItem = itr.next();
            String[] splitItem = nxtItem.split("--");

            CheckBox isProficient = new CheckBox();
            isProficient.setAllowIndeterminate(false);

            CheckBox isFinesse = new CheckBox();
            isFinesse.setAllowIndeterminate(false);

            TextField weaponsTf = new TextField(splitItem[0]);
            weaponsTf.setPromptText("Weapon Name");
            weaponsTf.setEditable(false);
            weaponsTf.setId("locked-tf");

            TextField damage = new TextField();
            damage.setPromptText("Damage");
            damage.setEditable(false);
            damage.setId("locked-tf");

            Label damageMod = new Label();
            ToggleButton editBtn = new ToggleButton("edit");

            Label hitMod = new Label();

            try {
                damage.setText(splitItem[1]);
                isProficient.setSelected(Boolean.parseBoolean(splitItem[2]));
                isFinesse.setSelected(Boolean.parseBoolean(splitItem[3]));
            }
            catch (ArrayIndexOutOfBoundsException ioe) {
                damage.setText("");
                isProficient.setSelected(false);
                isFinesse.setAllowIndeterminate(false);
            }    


            if (isProficient.isSelected() && isFinesse.isSelected()) {
                hitMod.setText(fmt.format(calcMod(c.getDex()) + c.getProficiencyBonus()));
            }
            else if (!isProficient.isSelected() && isFinesse.isSelected()) {
                hitMod.setText(fmt.format(calcMod(c.getDex())));
            }
            else if (isProficient.isSelected() && !isFinesse.isSelected()) {
                hitMod.setText(fmt.format(calcMod(c.getStr()) + c.getProficiencyBonus()));
            }
            else {
                hitMod.setText(fmt.format(calcMod(c.getStr())));

            }

            if (isFinesse.isSelected()) {
                damageMod.setText(fmt.format(calcMod(c.getDex())));
            }
            else {
                damageMod.setText(fmt.format(calcMod(c.getStr())));
            }

            HBox hbWeaponsList = new HBox(10);

            Button rm = new Button("remove");
            hbWeaponsList.getChildren().addAll(isProficient,hitMod,weaponsTf,damage,damageMod,isFinesse,rm,editBtn);

            vbWeapons.getChildren().add(hbWeaponsList);

            ////// Edit button //////

            editBtn.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    if (editBtn.isSelected()) {
                        damage.setEditable(true);
                        damage.setId("unlocked-tf");
                        weaponsTf.setEditable(true);
                        weaponsTf.setId("unlocked-tf");
                    }
                    else {
                        damage.setEditable(false);
                        damage.setId("locked-tf");
                        weaponsTf.setEditable(false);
                        weaponsTf.setId("locked-tf");
                        weaponsList.set(currIndex,weaponsTf.getText() + "--" + damage.getText() + "--" + Boolean.toString(isProficient.isSelected()) + "--" + Boolean.toString(isFinesse.isSelected()));
                        c.setWeapons(weaponsList);
                    }
                }
            });

            isProficient.selectedProperty().addListener(new ChangeListener<Boolean>() {
                public void changed(ObservableValue<? extends Boolean> ov, Boolean oldVal, Boolean newVal) {

                    weaponsList.set(currIndex,weaponsTf.getText() + "--" + damage.getText() + "--" + Boolean.toString(newVal) + "--" + Boolean.toString(isFinesse.isSelected()));
                    c.setWeapons(weaponsList);

                    if (newVal && isFinesse.isSelected()) {
                        hitMod.setText(fmt.format(calcMod(c.getDex()) + c.getProficiencyBonus()));
                    }
                    else if (newVal && !isFinesse.isSelected()) {
                        hitMod.setText(fmt.format(calcMod(c.getStr()) + c.getProficiencyBonus()));
                    }
                    else if (!newVal && isFinesse.isSelected()) {
                        hitMod.setText(fmt.format(calcMod(c.getDex())));
                    }
                    else {
                        hitMod.setText(fmt.format(calcMod(c.getStr())));
                    }

                }
            });

            isFinesse.selectedProperty().addListener(new ChangeListener<Boolean>() {
                public void changed(ObservableValue<? extends Boolean> ov, Boolean oldVal, Boolean newVal) {
                    weaponsList.set(currIndex,weaponsTf.getText() + "--" + damage.getText() + "--" + Boolean.toString(isProficient.isSelected()) + "--" + Boolean.toString(newVal));
                    c.setWeapons(weaponsList);
                    if (newVal) {
                        damageMod.setText(fmt.format(calcMod(c.getDex())));
                    }
                    else {
                        damageMod.setText(fmt.format(calcMod(c.getStr())));
                    }
                    
                    if (newVal && isProficient.isSelected()) {
                        hitMod.setText(fmt.format(calcMod(c.getDex()) + c.getProficiencyBonus()));
                    }
                    else if (newVal && !isProficient.isSelected()) {
                        hitMod.setText(fmt.format(calcMod(c.getDex())));
                    }
                    else if (!newVal && isProficient.isSelected()) {
                        hitMod.setText(fmt.format(calcMod(c.getStr()) + c.getProficiencyBonus()));
                    }
                    else {
                        hitMod.setText(fmt.format(calcMod(c.getStr())));
                    }
                }
            });


            ////// Remove button ///////
            Stage confirmRm = new Stage();
            rm.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    confirmRm.close();
                    confirmRm.setTitle("Are you sure?");
                    GridPane rmgrid = new GridPane();
                    rmgrid.setAlignment(Pos.CENTER);
                    rmgrid.setHgap(10);
                    rmgrid.setVgap(10);
                    Scene rmscene = new Scene(rmgrid,400,150);
                    confirmRm.setScene(rmscene);
                    confirmRm.show();

                    Label rmLabel = new Label("remove " + nxtItem + ". Are you sure?");
                    rmgrid.add(rmLabel,0,0);
                    Button yesRm = new Button("Yes");
                    Button noRm = new Button("Cancel");
                    HBox hbynrm = new HBox(10);
                    hbynrm.getChildren().addAll(yesRm,noRm);
                    rmgrid.add(hbynrm,0,1);

                    yesRm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            vbWeapons.getChildren().remove(hbWeaponsList);
                            weaponsList.remove(currIndex);
                            c.setWeapons(weaponsList);
                            confirmRm.close();
                        }
                    });
                    noRm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            confirmRm.close();
                        }
                    });
                }
            });
            index++;

        }

        TextField addWeaponsTf = new TextField();
        addWeaponsTf.setPromptText("Add a Weapon");
        Button addWeapons = new Button("Add Weapon");
        Button doneWeapons = new Button("Done");
        HBox hbaddWeapons = new HBox(10);
        hbaddWeapons.getChildren().addAll(addWeaponsTf,addWeapons);
        VBox vbweaponsBtns = new VBox(10);
        vbweaponsBtns.getChildren().addAll(hbaddWeapons,doneWeapons);

        ScrollPane weaponsSp = new ScrollPane();
        weaponsSp.setContent(vbWeapons);

        BorderPane bpWeapons = new BorderPane();
        bpWeapons.setPadding(new Insets(20));
        bpWeapons.setMargin(weaponsTitle,new Insets(12,12,12,12));
        bpWeapons.setMargin(weaponsSp,new Insets(10,10,10,10));
        bpWeapons.setTop(weaponsHeader);
        bpWeapons.setCenter(weaponsSp);
        bpWeapons.setBottom(vbweaponsBtns);


        Scene weaponsscene = new Scene(bpWeapons);
        weaponsscene.getStylesheets().add("lib/ListPage.css");

        weaponsStage.setScene(weaponsscene);

        //////// Add Weapons //////////
        addWeapons.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (!addWeaponsTf.getText().isEmpty()) {
                    weaponsList.add(addWeaponsTf.getText());
                    int newRow = weaponsList.size() - 1;
                    c.setWeapons(weaponsList);

                    TextField newWeapons = new TextField(addWeaponsTf.getText());
                    newWeapons.setEditable(false);
                    newWeapons.setPromptText("Weapon Name");
                    newWeapons.setId("locked-tf");

                    CheckBox isProficient = new CheckBox();
                    isProficient.setAllowIndeterminate(false);

                    CheckBox isFinesse = new CheckBox();
                    isFinesse.setAllowIndeterminate(false);
                    
                    Button rm = new Button("remove");

                    TextField damage = new TextField();
                    damage.setEditable(false);
                    damage.setPromptText("Damage");
                    damage.setId("locked-tf");

                    ToggleButton editBtn = new ToggleButton("edit");

                    Label damageMod = new Label();
                    damageMod.setText(fmt.format(calcMod(c.getStr())));

                    Label hitMod = new Label();
                    hitMod.setText(fmt.format(calcMod(c.getStr())));

                    addWeaponsTf.clear();

                    HBox hbWeaponsList = new HBox(10);

                    hbWeaponsList.getChildren().addAll(isProficient,hitMod,newWeapons,damage,damageMod,isFinesse,rm,editBtn);
                    vbWeapons.getChildren().add(hbWeaponsList);

                    ////// Edit button //////

                    editBtn.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent e) {
                            if (editBtn.isSelected()) {
                                damage.setEditable(true);
                                damage.setId("unlocked-tf");
                                newWeapons.setEditable(true);
                                newWeapons.setId("unlocked-tf");
                            }
                            else {
                                damage.setEditable(false);
                                damage.setId("locked-tf");
                                newWeapons.setEditable(false);
                                newWeapons.setId("locked-tf");
                                weaponsList.set(newRow,newWeapons.getText() + "--" + damage.getText() + "--" + Boolean.toString(isProficient.isSelected()) + "--" + Boolean.toString(isFinesse.isSelected()));
                                c.setWeapons(weaponsList);
                            }
                        }
                    });

                    isProficient.selectedProperty().addListener(new ChangeListener<Boolean>() {
                        public void changed(ObservableValue<? extends Boolean> ov, Boolean oldVal, Boolean newVal) {

                            weaponsList.set(newRow,newWeapons.getText() + "--" + damage.getText() + "--" + Boolean.toString(newVal) + "--" + Boolean.toString(isFinesse.isSelected()));
                            c.setWeapons(weaponsList);

                            if (newVal && isFinesse.isSelected()) {
                                hitMod.setText(fmt.format(calcMod(c.getDex()) + c.getProficiencyBonus()));
                            }
                            else if (newVal && !isFinesse.isSelected()) {
                                hitMod.setText(fmt.format(calcMod(c.getStr()) + c.getProficiencyBonus()));
                            }
                            else if (!newVal && isFinesse.isSelected()) {
                                hitMod.setText(fmt.format(calcMod(c.getDex())));
                            }
                            else {
                                hitMod.setText(fmt.format(calcMod(c.getStr())));
                            }

                        }
                    });

                    isFinesse.selectedProperty().addListener(new ChangeListener<Boolean>() {
                        public void changed(ObservableValue<? extends Boolean> ov, Boolean oldVal, Boolean newVal) {
                            weaponsList.set(newRow,newWeapons.getText() + "--" + damage.getText() + "--" + Boolean.toString(isProficient.isSelected()) + "--" + Boolean.toString(newVal));
                            c.setWeapons(weaponsList);
                            if (newVal) {
                                damageMod.setText(fmt.format(calcMod(c.getDex())));
                            }
                            else {
                                damageMod.setText(fmt.format(calcMod(c.getStr())));
                            }

                            if (newVal && isProficient.isSelected()) {
                                hitMod.setText(fmt.format(calcMod(c.getDex()) + c.getProficiencyBonus()));
                            }
                            else if (newVal && !isProficient.isSelected()) {
                                hitMod.setText(fmt.format(calcMod(c.getDex())));
                            }
                            else if (!newVal && isProficient.isSelected()) {
                                hitMod.setText(fmt.format(calcMod(c.getStr()) + c.getProficiencyBonus()));
                            }
                            else {
                                hitMod.setText(fmt.format(calcMod(c.getStr())));
                            }
                        }
                    });

                    ////// Remove button ///////
                    Stage confirmRm = new Stage();
                    rm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            confirmRm.close();
                            confirmRm.setTitle("Are you sure?");
                            GridPane rmgrid = new GridPane();
                            rmgrid.setAlignment(Pos.CENTER);
                            rmgrid.setHgap(10);
                            rmgrid.setVgap(10);
                            Scene rmscene = new Scene(rmgrid,400,150);
                            confirmRm.setScene(rmscene);
                            confirmRm.show();

                            Label rmLabel = new Label("remove " + newWeapons.getText() + ". Are you sure?");
                            rmgrid.add(rmLabel,0,0);
                            Button yesRm = new Button("Yes");
                            Button noRm = new Button("Cancel");
                            HBox hbynrm = new HBox(10);
                            hbynrm.getChildren().addAll(yesRm,noRm);
                            rmgrid.add(hbynrm,0,1);

                            yesRm.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent e) {
                                    vbWeapons.getChildren().remove(hbWeaponsList);
                                    weaponsList.remove(newRow);
                                    c.setWeapons(weaponsList);
                                    confirmRm.close();
                                }
                            });
                            noRm.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent e) {
                                    confirmRm.close();
                                }
                            });
                        }
                    });

                }

            }
        });




        doneWeapons.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                c.setWeapons(weaponsList);
                weaponsStage.close();
            }
        });

        weaponsStage.show();

    }

    public void IdealsPage(Stage idealsStage) {
        idealsStage.close();
        idealsStage.setTitle("Ideals Page");

        Label idealsTitle = new Label("Ideals");
        idealsTitle.setId("title");
        VBox vbIdeals = new VBox(10);

        HashSet<String> idealsList = c.getIdeals();
        Iterator<String> itr = idealsList.iterator();

        while (itr.hasNext()) {
            String nxtItem = itr.next();
            Label idealslabel = new Label(nxtItem);
            HBox hbIdealsList = new HBox(10);

            Button rm = new Button("remove");
            hbIdealsList.getChildren().addAll(idealslabel,rm);

            vbIdeals.getChildren().add(hbIdealsList);

            ////// Remove button ///////
            Stage confirmRm = new Stage();
            rm.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    confirmRm.close();
                    confirmRm.setTitle("Are you sure?");
                    GridPane rmgrid = new GridPane();
                    rmgrid.setAlignment(Pos.CENTER);
                    rmgrid.setHgap(10);
                    rmgrid.setVgap(10);
                    Scene rmscene = new Scene(rmgrid,400,150);
                    confirmRm.setScene(rmscene);
                    confirmRm.show();

                    Label rmLabel = new Label("remove " + nxtItem + ". Are you sure?");
                    rmgrid.add(rmLabel,0,0);
                    Button yesRm = new Button("Yes");
                    Button noRm = new Button("Cancel");
                    HBox hbynrm = new HBox(10);
                    hbynrm.getChildren().addAll(yesRm,noRm);
                    rmgrid.add(hbynrm,0,1);

                    yesRm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            vbIdeals.getChildren().remove(hbIdealsList);
                            idealsList.remove(idealslabel.getText());
                            c.setIdeals(idealsList);
                            confirmRm.close();
                        }
                    });
                    noRm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            confirmRm.close();
                        }
                    });
                }
            });

        }

        TextField addIdealsTf = new TextField();
        addIdealsTf.setPromptText("Add an Ideal ");
        Button addIdeals = new Button("Add Ideal");
        Button doneIdeals = new Button("Done");
        HBox hbaddIdeals = new HBox(10);
        hbaddIdeals.getChildren().addAll(addIdealsTf,addIdeals);
        VBox vbidealsBtns = new VBox(10);
        vbidealsBtns.getChildren().addAll(hbaddIdeals,doneIdeals);

        ScrollPane idealsSp = new ScrollPane();
        idealsSp.setContent(vbIdeals);

        BorderPane bpIdeals = new BorderPane();
        bpIdeals.setPadding(new Insets(20));
        bpIdeals.setMargin(idealsTitle,new Insets(12,12,12,12));
        bpIdeals.setMargin(idealsSp,new Insets(10,10,10,10));
        bpIdeals.setTop(idealsTitle);
        bpIdeals.setCenter(idealsSp);
        bpIdeals.setBottom(vbidealsBtns);


        Scene idealsscene = new Scene(bpIdeals);
        idealsscene.getStylesheets().add("lib/ListPage.css");

        idealsStage.setScene(idealsscene);

        //////// Add Ideals //////////
        addIdeals.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (!addIdealsTf.getText().isEmpty()) {
                    idealsList.add(addIdealsTf.getText());
                    int newRow = idealsList.size() - 1;
                    c.setIdeals(idealsList);
                    Label newIdeals = new Label(addIdealsTf.getText());
                    Button rm = new Button("remove");
                    HBox hbnewIdeals = new HBox(10);
                    hbnewIdeals.getChildren().addAll(newIdeals,rm);
                    vbIdeals.getChildren().add(hbnewIdeals);
                    addIdealsTf.clear();

                    ////// Remove button ///////
                    Stage confirmRm = new Stage();
                    rm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            confirmRm.close();
                            confirmRm.setTitle("Are you sure?");
                            GridPane rmgrid = new GridPane();
                            rmgrid.setAlignment(Pos.CENTER);
                            rmgrid.setHgap(10);
                            rmgrid.setVgap(10);
                            Scene rmscene = new Scene(rmgrid,400,150);
                            confirmRm.setScene(rmscene);
                            confirmRm.show();

                            Label rmLabel = new Label("remove " +newIdeals.getText()+ ". Are you sure?");
                            rmgrid.add(rmLabel,0,0);
                            Button yesRm = new Button("Yes");
                            Button noRm = new Button("Cancel");
                            HBox hbynrm = new HBox(10);
                            hbynrm.getChildren().addAll(yesRm,noRm);
                            rmgrid.add(hbynrm,0,1);

                            yesRm.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent e) {
                                    vbIdeals.getChildren().remove(hbnewIdeals);
                                    idealsList.remove(newIdeals.getText());
                                    c.setIdeals(idealsList);
                                    confirmRm.close();
                                }
                            });
                            noRm.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent e) {
                                    confirmRm.close();
                                }
                            });
                        }
                    });

                }

            }
        });

        doneIdeals.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                c.setIdeals(idealsList);
                idealsStage.close();
            }
        });

        idealsStage.show();

    }

    public void FlawsPage(Stage flawsStage) {
        flawsStage.close();
        flawsStage.setTitle("Flaws Page");

        Label flawsTitle = new Label("Flaws");
        flawsTitle.setId("title");
        VBox vbFlaws = new VBox(10);

        HashSet<String> flawsList = c.getFlaws();
        Iterator<String> itr = flawsList.iterator();

        while (itr.hasNext()) {
            String nxtItem = itr.next();
            Label flawslabel = new Label(nxtItem);
            HBox hbFlawsList = new HBox(10);

            Button rm = new Button("remove");
            hbFlawsList.getChildren().addAll(flawslabel,rm);

            vbFlaws.getChildren().add(hbFlawsList);

            ////// Remove button ///////
            Stage confirmRm = new Stage();
            rm.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    confirmRm.close();
                    confirmRm.setTitle("Are you sure?");
                    GridPane rmgrid = new GridPane();
                    rmgrid.setAlignment(Pos.CENTER);
                    rmgrid.setHgap(10);
                    rmgrid.setVgap(10);
                    Scene rmscene = new Scene(rmgrid,400,150);
                    confirmRm.setScene(rmscene);
                    confirmRm.show();

                    Label rmLabel = new Label("remove " + nxtItem + ". Are you sure?");
                    rmgrid.add(rmLabel,0,0);
                    Button yesRm = new Button("Yes");
                    Button noRm = new Button("Cancel");
                    HBox hbynrm = new HBox(10);
                    hbynrm.getChildren().addAll(yesRm,noRm);
                    rmgrid.add(hbynrm,0,1);

                    yesRm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            vbFlaws.getChildren().remove(hbFlawsList);
                            flawsList.remove(flawslabel.getText());
                            c.setFlaws(flawsList);
                            confirmRm.close();
                        }
                    });
                    noRm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            confirmRm.close();
                        }
                    });
                }
            });

        }

        TextField addFlawsTf = new TextField();
        addFlawsTf.setPromptText("Add a Flaw");
        Button addFlaws = new Button("Add Flaw");
        Button doneFlaws = new Button("Done");
        HBox hbaddFlaws = new HBox(10);
        hbaddFlaws.getChildren().addAll(addFlawsTf,addFlaws);
        VBox vbflawsBtns = new VBox(10);
        vbflawsBtns.getChildren().addAll(hbaddFlaws,doneFlaws);

        ScrollPane flawsSp = new ScrollPane();
        flawsSp.setContent(vbFlaws);

        BorderPane bpFlaws = new BorderPane();
        bpFlaws.setPadding(new Insets(20));
        bpFlaws.setMargin(flawsTitle,new Insets(12,12,12,12));
        bpFlaws.setMargin(flawsSp,new Insets(10,10,10,10));
        bpFlaws.setTop(flawsTitle);
        bpFlaws.setCenter(flawsSp);
        bpFlaws.setBottom(vbflawsBtns);


        Scene flawsscene = new Scene(bpFlaws);
        flawsscene.getStylesheets().add("lib/ListPage.css");

        flawsStage.setScene(flawsscene);

        //////// Add Flaws //////////
        addFlaws.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (!addFlawsTf.getText().isEmpty()) {
                    flawsList.add(addFlawsTf.getText());
                    int newRow = flawsList.size() - 1;
                    c.setFlaws(flawsList);
                    Label newFlaws = new Label(addFlawsTf.getText());
                    Button rm = new Button("remove");
                    HBox hbnewFlaws = new HBox(10);
                    hbnewFlaws.getChildren().addAll(newFlaws,rm);
                    vbFlaws.getChildren().add(hbnewFlaws);
                    addFlawsTf.clear();

                    ////// Remove button ///////
                    Stage confirmRm = new Stage();
                    rm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            confirmRm.close();
                            confirmRm.setTitle("Are you sure?");
                            GridPane rmgrid = new GridPane();
                            rmgrid.setAlignment(Pos.CENTER);
                            rmgrid.setHgap(10);
                            rmgrid.setVgap(10);
                            Scene rmscene = new Scene(rmgrid,400,150);
                            confirmRm.setScene(rmscene);
                            confirmRm.show();

                            Label rmLabel = new Label("remove " +newFlaws.getText()+ ". Are you sure?");
                            rmgrid.add(rmLabel,0,0);
                            Button yesRm = new Button("Yes");
                            Button noRm = new Button("Cancel");
                            HBox hbynrm = new HBox(10);
                            hbynrm.getChildren().addAll(yesRm,noRm);
                            rmgrid.add(hbynrm,0,1);

                            yesRm.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent e) {
                                    vbFlaws.getChildren().remove(hbnewFlaws);
                                    flawsList.remove(newFlaws.getText());
                                    c.setFlaws(flawsList);
                                    confirmRm.close();
                                }
                            });
                            noRm.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent e) {
                                    confirmRm.close();
                                }
                            });
                        }
                    });
                }
            }
        });




        doneFlaws.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                c.setFlaws(flawsList);
                flawsStage.close();
            }
        });

        flawsStage.show();

    }

    public void BondsPage(Stage bondsStage) {
        bondsStage.close();
        bondsStage.setTitle("Bonds Page");

        Label bondsTitle = new Label("Bonds");
        bondsTitle.setId("title");
        VBox vbBonds = new VBox(10);

        HashSet<String> bondsList = c.getBonds();
        Iterator<String> itr = bondsList.iterator();

        while (itr.hasNext()) {
            String nxtItem = itr.next();
            Label bondslabel = new Label(nxtItem);
            HBox hbBondsList = new HBox(10);

            Button rm = new Button("remove");
            hbBondsList.getChildren().addAll(bondslabel,rm);

            vbBonds.getChildren().add(hbBondsList);

            ////// Remove button ///////
            Stage confirmRm = new Stage();
            rm.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    confirmRm.close();
                    confirmRm.setTitle("Are you sure?");
                    GridPane rmgrid = new GridPane();
                    rmgrid.setAlignment(Pos.CENTER);
                    rmgrid.setHgap(10);
                    rmgrid.setVgap(10);
                    Scene rmscene = new Scene(rmgrid,400,150);
                    confirmRm.setScene(rmscene);
                    confirmRm.show();

                    Label rmLabel = new Label("remove " + nxtItem + ". Are you sure?");
                    rmgrid.add(rmLabel,0,0);
                    Button yesRm = new Button("Yes");
                    Button noRm = new Button("Cancel");
                    HBox hbynrm = new HBox(10);
                    hbynrm.getChildren().addAll(yesRm,noRm);
                    rmgrid.add(hbynrm,0,1);

                    yesRm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            vbBonds.getChildren().remove(hbBondsList);
                            bondsList.remove(bondslabel.getText());
                            c.setBonds(bondsList);
                            confirmRm.close();
                        }
                    });
                    noRm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            confirmRm.close();
                        }
                    });
                }
            });

        }

        TextField addBondsTf = new TextField();
        addBondsTf.setPromptText("Add a Bond");
        Button addBonds = new Button("Add Bond");
        Button doneBonds = new Button("Done");
        HBox hbaddBonds = new HBox(10);
        hbaddBonds.getChildren().addAll(addBondsTf,addBonds);
        VBox vbbondsBtns = new VBox(10);
        vbbondsBtns.getChildren().addAll(hbaddBonds,doneBonds);

        ScrollPane bondsSp = new ScrollPane();
        bondsSp.setContent(vbBonds);

        BorderPane bpBonds = new BorderPane();
        bpBonds.setPadding(new Insets(20));
        bpBonds.setMargin(bondsTitle,new Insets(12,12,12,12));
        bpBonds.setMargin(bondsSp,new Insets(10,10,10,10));
        bpBonds.setTop(bondsTitle);
        bpBonds.setCenter(bondsSp);
        bpBonds.setBottom(vbbondsBtns);


        Scene bondsscene = new Scene(bpBonds);
        bondsscene.getStylesheets().add("lib/ListPage.css");

        bondsStage.setScene(bondsscene);

        //////// Add Bonds //////////
        addBonds.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (!addBondsTf.getText().isEmpty()) {
                    bondsList.add(addBondsTf.getText());
                    int newRow = bondsList.size() - 1;
                    c.setBonds(bondsList);
                    Label newBonds = new Label(addBondsTf.getText());
                    Button rm = new Button("remove");
                    HBox hbnewBonds = new HBox(10);
                    hbnewBonds.getChildren().addAll(newBonds,rm);
                    vbBonds.getChildren().add(hbnewBonds);
                    addBondsTf.clear();

                    ////// Remove button ///////
                    Stage confirmRm = new Stage();
                    rm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            confirmRm.close();
                            confirmRm.setTitle("Are you sure?");
                            GridPane rmgrid = new GridPane();
                            rmgrid.setAlignment(Pos.CENTER);
                            rmgrid.setHgap(10);
                            rmgrid.setVgap(10);
                            Scene rmscene = new Scene(rmgrid,400,150);
                            confirmRm.setScene(rmscene);
                            confirmRm.show();

                            Label rmLabel = new Label("remove " +newBonds.getText()+ ". Are you sure?");
                            rmgrid.add(rmLabel,0,0);
                            Button yesRm = new Button("Yes");
                            Button noRm = new Button("Cancel");
                            HBox hbynrm = new HBox(10);
                            hbynrm.getChildren().addAll(yesRm,noRm);
                            rmgrid.add(hbynrm,0,1);

                            yesRm.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent e) {
                                    vbBonds.getChildren().remove(hbnewBonds);
                                    bondsList.remove(newBonds.getText());
                                    c.setBonds(bondsList);
                                    confirmRm.close();
                                }
                            });
                            noRm.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent e) {
                                    confirmRm.close();
                                }
                            });
                        }
                    });
                }
            }
        });




        doneBonds.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                c.setBonds(bondsList);
                bondsStage.close();
            }
        });

        bondsStage.show();

    }

    public void FeaturesPage(Stage featuresStage) {
        featuresStage.close();
        featuresStage.setTitle("Features Page");

        Label featuresTitle = new Label("Features");
        featuresTitle.setId("title");
        VBox vbFeatures = new VBox(10);

        ArrayList<String> featuresList = c.getFeatures();
        Iterator<String> itr = featuresList.iterator();
        int index = 0;

        while (itr.hasNext()) {
            final int currIndex = index;
            String nxtItem = itr.next();
            String[] splitItem = nxtItem.split("--");
            TextArea description = new TextArea();
            description.setWrapText(true);
            description.setPromptText("Item Description");
            try {
                description.setText(splitItem[1]);
            }
            catch (ArrayIndexOutOfBoundsException ioe) {
                description.setText("");
            }


            Label featuresLabel = new Label(splitItem[0]);
            HBox hbFeaturesList = new HBox(10);

            Button info = new Button("info");
            Button rm = new Button("remove");
            hbFeaturesList.getChildren().addAll(featuresLabel,info,rm);

            vbFeatures.getChildren().add(hbFeaturesList);
            index++;
            ////// Remove button ///////
            Stage confirmRm = new Stage();
            rm.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    confirmRm.close();
                    confirmRm.setTitle("Are you sure?");
                    GridPane rmgrid = new GridPane();
                    rmgrid.setAlignment(Pos.CENTER);
                    rmgrid.setHgap(10);
                    rmgrid.setVgap(10);
                    Scene rmscene = new Scene(rmgrid,400,150);
                    confirmRm.setScene(rmscene);
                    confirmRm.show();

                    Label rmLabel = new Label("remove " + splitItem[0] + ". Are you sure?");
                    rmgrid.add(rmLabel,0,0);
                    Button yesRm = new Button("Yes");
                    Button noRm = new Button("Cancel");
                    HBox hbynrm = new HBox(10);
                    hbynrm.getChildren().addAll(yesRm,noRm);
                    rmgrid.add(hbynrm,0,1);

                    yesRm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            vbFeatures.getChildren().remove(hbFeaturesList);
                            featuresList.remove(nxtItem);
                            c.setFeatures(featuresList);
                            confirmRm.close();
                        }
                    });
                    noRm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            confirmRm.close();
                        }
                    });
                }
            });

            Stage infoStage = new Stage();
            info.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    infoStage.close();
                    GridPane infoGrid = new GridPane();
                    infoGrid.setAlignment(Pos.CENTER);
                    infoGrid.setHgap(10);
                    infoGrid.setVgap(10);
                    Scene infoScene = new Scene(infoGrid);
                    infoStage.setScene(infoScene);

                    infoScene.getStylesheets().add("lib/TextAreaPage.css");

                    Label itemName = new Label();
                    itemName.setText(featuresLabel.getText());

                    Button done = new Button("Done");
                    Button saveDesc = new Button("Save");
                    HBox descBtns = new HBox(10);
                    descBtns.getChildren().addAll(done,saveDesc);

                    infoGrid.add(itemName,0,0);
                    infoGrid.add(description,0,1);
                    infoGrid.add(descBtns,0,2);

                    done.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent e) {
                            String newItem = itemName.getText() + "--" + description.getText();
                            featuresList.set(currIndex,newItem);
                            c.setFeatures(featuresList);
                            infoStage.close();
                        }
                    });
                    saveDesc.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent e) {
                            String newItem = itemName.getText() + "--" + description.getText();
                            featuresList.set(currIndex,newItem);
                            c.setFeatures(featuresList);
                            Text msg = new Text("Saved");
                            msg.setFill(Color.FIREBRICK);
                            descBtns.getChildren().remove(msg);
                            descBtns.getChildren().add(msg);
                        }
                    });
                    infoStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        public void handle(WindowEvent we) {
                            String newItem = itemName.getText() + "--" + description.getText();
                            featuresList.set(currIndex,newItem);
                            c.setFeatures(featuresList);
                        }
                    });
                    infoStage.show();
                }
            });



        }

        TextField addFeaturesTf = new TextField();
        addFeaturesTf.setPromptText("Add a Feature");
        Button addFeatures = new Button("Add Feature");
        Button doneFeatures = new Button("Done");
        HBox hbaddFeatures = new HBox(10);
        hbaddFeatures.getChildren().addAll(addFeaturesTf,addFeatures);
        VBox vbfeaturesBtns = new VBox(10);
        vbfeaturesBtns.getChildren().addAll(hbaddFeatures,doneFeatures);

        ScrollPane featuresSp = new ScrollPane();
        featuresSp.setContent(vbFeatures);

        BorderPane bpFeatures = new BorderPane();
        bpFeatures.setPadding(new Insets(20));
        bpFeatures.setMargin(featuresTitle,new Insets(12,12,12,12));
        bpFeatures.setMargin(featuresSp,new Insets(10,10,10,10));
        bpFeatures.setTop(featuresTitle);
        bpFeatures.setCenter(featuresSp);
        bpFeatures.setBottom(vbfeaturesBtns);


        Scene featuresscene = new Scene(bpFeatures);
        featuresscene.getStylesheets().add("lib/ListPage.css");

        featuresStage.setScene(featuresscene);

        //////// Add Features //////////
        addFeatures.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (!addFeaturesTf.getText().isEmpty()) {
                    featuresList.add(addFeaturesTf.getText());
                    int newRow = featuresList.size() - 1;
                    c.setFeatures(featuresList);
                    Label newFeatures = new Label(addFeaturesTf.getText());

                    TextArea description = new TextArea();
                    description.setWrapText(true);
                    description.setPromptText("Item Description");

                    Button rm = new Button("remove");
                    Button info = new Button("info");
                    HBox hbnewFeatures = new HBox(10);
                    hbnewFeatures.getChildren().addAll(newFeatures,info,rm);
                    vbFeatures.getChildren().add(hbnewFeatures);
                    addFeaturesTf.clear();

                    ////// Remove button ///////
                    Stage confirmRm = new Stage();
                    rm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            confirmRm.close();
                            confirmRm.setTitle("Are you sure?");
                            GridPane rmgrid = new GridPane();
                            rmgrid.setAlignment(Pos.CENTER);
                            rmgrid.setHgap(10);
                            rmgrid.setVgap(10);
                            Scene rmscene = new Scene(rmgrid,400,150);
                            confirmRm.setScene(rmscene);
                            confirmRm.show();

                            Label rmLabel = new Label("remove " +newFeatures.getText()+ ". Are you sure?");
                            rmgrid.add(rmLabel,0,0);
                            Button yesRm = new Button("Yes");
                            Button noRm = new Button("Cancel");
                            HBox hbynrm = new HBox(10);
                            hbynrm.getChildren().addAll(yesRm,noRm);
                            rmgrid.add(hbynrm,0,1);

                            yesRm.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent e) {
                                    vbFeatures.getChildren().remove(hbnewFeatures);
                                    featuresList.remove(newFeatures.getText());
                                    c.setFeatures(featuresList);
                                    confirmRm.close();
                                }
                            });
                            noRm.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent e) {
                                    confirmRm.close();
                                }
                            });
                        }
                    });
                    Stage infoStage = new Stage();
                    info.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            infoStage.close();
                            GridPane infoGrid = new GridPane();
                            infoGrid.setAlignment(Pos.CENTER);
                            infoGrid.setHgap(10);
                            infoGrid.setVgap(10);
                            Scene infoScene = new Scene(infoGrid);
                            infoStage.setScene(infoScene);

                            infoScene.getStylesheets().add("lib/TextAreaPage.css");

                            Label itemName = new Label();
                            itemName.setText(newFeatures.getText());

                            Button done = new Button("Done");
                            Button saveDesc = new Button("Save");
                            HBox descBtns = new HBox(10);
                            descBtns.getChildren().addAll(done,saveDesc);

                            infoGrid.add(itemName,0,0);
                            infoGrid.add(description,0,1);
                            infoGrid.add(descBtns,0,2);

                            done.setOnAction(new EventHandler<ActionEvent>() {
                                public void handle(ActionEvent e) {
                                    String newItem = itemName.getText() + "--" + description.getText();
                                    featuresList.set(newRow,newItem);
                                    c.setFeatures(featuresList);
                                    infoStage.close();
                                }
                            });
                            saveDesc.setOnAction(new EventHandler<ActionEvent>() {
                                public void handle(ActionEvent e) {
                                    String newItem = itemName.getText() + "--" + description.getText();
                                    featuresList.set(newRow,newItem);
                                    c.setFeatures(featuresList);
                                    Text msg = new Text("Saved");
                                    msg.setFill(Color.FIREBRICK);
                                    descBtns.getChildren().remove(msg);
                                    descBtns.getChildren().add(msg);
                                }
                            });
                            infoStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                                public void handle(WindowEvent we) {
                                    String newItem = itemName.getText() + "--" + description.getText();
                                    featuresList.set(newRow,newItem);
                                    c.setFeatures(featuresList);
                                }
                            });
                            infoStage.show();
                        }
                    });
                }
            }
        });




        doneFeatures.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                c.setFeatures(featuresList);
                featuresStage.close();
            }
        });

        featuresStage.show();

    }

    public void NotesPage(Stage notesStage) {
        notesStage.close();
        notesStage.setTitle("Notes Page");

        Label notesTitle = new Label("Notes");
        notesTitle.setId("title");
        VBox vbNotes = new VBox(10);

        String notesList = c.getNotes();

        TextArea notes = new TextArea(c.getNotes());
        Button saveNotes = new Button("Save Notes");
        Button doneNotes = new Button("Done");
        HBox hbNotes = new HBox(10);
        hbNotes.getChildren().addAll(saveNotes,doneNotes);


        ScrollPane notesSp = new ScrollPane();
        notesSp.setContent(vbNotes);

        BorderPane bpNotes = new BorderPane();
        bpNotes.setPadding(new Insets(20));
        bpNotes.setMargin(notesTitle,new Insets(12,12,12,12));
        bpNotes.setMargin(notes,new Insets(10,10,10,10));
        bpNotes.setTop(notesTitle);
        bpNotes.setCenter(notes);
        bpNotes.setBottom(hbNotes);


        Scene notesscene = new Scene(bpNotes);
        notesscene.getStylesheets().add("lib/TextAreaPage.css");

        notesStage.setScene(notesscene);

        //////// Save Notes //////////
        saveNotes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                c.setNotes(notes.getText());
                Text msg = new Text("Notes Saved");
                msg.setFill(Color.FIREBRICK);
                hbNotes.getChildren().remove(msg);
                hbNotes.getChildren().add(msg);

            }
        });


        doneNotes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                c.setNotes(notes.getText());
                notesStage.close();
            }
        });
        notesStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                c.setNotes(notes.getText());
            }
        });
        notesStage.show();

    }

    public void DescriptionPage(Stage descriptionStage) {
        descriptionStage.close();
        descriptionStage.setTitle("Description Page");

        Label descriptionTitle = new Label("Description");
        descriptionTitle.setId("title");
        VBox vbDescription = new VBox(10);

        String descriptionList = c.getDescription();

        TextArea description = new TextArea(c.getDescription());
        description.setWrapText(true);
        Button saveDescription = new Button("Save Description");
        Button doneDescription = new Button("Done");
        HBox hbDescription = new HBox(10);
        hbDescription.getChildren().addAll(saveDescription,doneDescription);


        ScrollPane descriptionSp = new ScrollPane();
        descriptionSp.setContent(vbDescription);

        BorderPane bpDescription = new BorderPane();
        bpDescription.setPadding(new Insets(20));
        bpDescription.setMargin(descriptionTitle,new Insets(12,12,12,12));
        bpDescription.setMargin(description,new Insets(10,10,10,10));
        bpDescription.setTop(descriptionTitle);
        bpDescription.setCenter(description);
        bpDescription.setBottom(hbDescription);


        Scene descriptionscene = new Scene(bpDescription);
        descriptionscene.getStylesheets().add("lib/TextAreaPage.css");

        descriptionStage.setScene(descriptionscene);

        //////// Save Description //////////
        saveDescription.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                c.setDescription(description.getText());
                Text msg = new Text("Description Saved");
                msg.setFill(Color.FIREBRICK);
                hbDescription.getChildren().remove(msg);
                hbDescription.getChildren().add(msg);

            }
        });


        doneDescription.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                c.setDescription(description.getText());
                descriptionStage.close();
            }
        });
        descriptionStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                c.setDescription(description.getText());
            }
        });

        descriptionStage.show();

    }

    public void SpellsPage(Stage spellsStage, Button spellsBtn) {
        String[] subClssSplit = c.getSubClss().split("--");
        spellsStage.close();
        spellsStage.setTitle("Spells Page");

        Label spellsTitle = new Label("Spells");
        spellsTitle.setId("title");


        VBox rootVb = new VBox(10);


        // Load the Spells
        Gson gson = new Gson();
        Spell[] spellArr;

        InputStream in = this.getClass().getClassLoader().getResourceAsStream("lib/spells.json");

        try (Reader reader = new InputStreamReader(in,"UTF-8")) {
            spellArr = gson.fromJson(reader,Spell[].class);
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            spellArr = new Spell[0];
        }

        // Get only spells associated with your class
        HashSet<Spell> classSpells = new HashSet<Spell>();

        HashSet<String> spellCasters = new HashSet<String>(Arrays.asList("Bard","Cleric","Druid","Paladin","Ranger","Sorcerer","Warlock","Wizard"));

        boolean inClassOptions = classes.contains(c.getClss());

        if (spellCasters.contains(c.getClss()) || !inClassOptions) {
            for (int i = 0; i < spellArr.length; i++) {
                String currclss = c.getClss();
                if (spellArr[i].getClss().contains(currclss) && inClassOptions) {
                    classSpells.add(spellArr[i]);
                }
                else if (!inClassOptions) {
                    classSpells.add(spellArr[i]);
                }
            }
        }

        // For sub classes
        if (subClssSplit[0] != null && !subClssSplit[0].isEmpty()) {
            if (subClssSplit[0].equalsIgnoreCase("Eldritch Knight") || subClssSplit[0].equalsIgnoreCase("Arcane Trickster")) {
                for (int i = 0; i < spellArr.length; i++) {
                    if (spellArr[i].getClss().contains("Wizard")) {
                        classSpells.add(spellArr[i]);
                    }
                }
            }
        }
        if (subClssSplit[0] != null && !subClssSplit[0].isEmpty()) {
            for (int i = 0; i < spellArr.length; i++) {
                String currsubclss = subClssSplit[0];
                if (spellArr[i].getSubClss().contains(currsubclss)) {
                    classSpells.add(spellArr[i]);
                }
            }
        }

        ////////////////////
        ///// CANTRIPS /////
        ////////////////////
        Label titleSpellSlots0 = new Label("Cantrips");
        titleSpellSlots0.setId("spellLevelTitle");

        Button addSpells0Btn = new Button("Add Cantrip");

        ComboBox<Spell> spellBox0 = new ComboBox<Spell>();
        spellBox0.setPromptText("add a cantrip");

        VBox vbSpells0 = new VBox(10);
        ScrollPane spells0Sp = new ScrollPane();
        spells0Sp.setContent(vbSpells0);

        for (Spell s : classSpells) {
            if (s.getLevel() == 0) {
                spellBox0.getItems().add(s);
            }
        }

        HashSet<Spell> spells0List = c.getCantrips();
        Iterator<Spell> itr0 = spells0List.iterator();

        ScrollPane spSpells0 = new ScrollPane();
        spSpells0.setContent(vbSpells0);

        while (itr0.hasNext()) {
            Spell nxtItem = itr0.next();
            String spellName = nxtItem.getName();
            Label spellLabel = new Label(spellName);
            HBox hbSpell = new HBox(10);

            Button rm = new Button("remove");
            Button info = new Button("info");
            hbSpell.getChildren().addAll(spellLabel,info,rm);

            vbSpells0.getChildren().add(hbSpell);

            ////// Remove Button ///////
            Stage confirmRm = new Stage();
            rm.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    confirmRm.close();
                    confirmRm.setTitle("Are you sure?");
                    GridPane rmgrid = new GridPane();
                    rmgrid.setAlignment(Pos.CENTER);
                    rmgrid.setHgap(10);
                    rmgrid.setVgap(10);
                    Scene rmscene = new Scene(rmgrid,400,150);
                    confirmRm.setScene(rmscene);
                    confirmRm.show();

                    Label rmLabel = new Label("remove " + spellName + ". Are you sure?");
                    rmgrid.add(rmLabel,0,0);
                    Button yesRm = new Button("Yes");
                    Button noRm = new Button("Cancel");
                    HBox hbynrm = new HBox(10);
                    hbynrm.getChildren().addAll(yesRm,noRm);
                    rmgrid.add(hbynrm,0,1);

                    yesRm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            vbSpells0.getChildren().remove(hbSpell);
                            spells0List.remove(nxtItem);
                            c.setCantrips(spells0List);
                            confirmRm.close();
                        }
                    });
                    noRm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            confirmRm.close();
                        }
                    });
                }
            });
            /////// Info Button ///////
            Stage infoStage = new Stage();
            info.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    SpellInfoPage(infoStage,nxtItem);
                }
            }); // close info button
        } // close while loop






        VBox spells0Root = new VBox(10);
        HBox spellSlots0Hb = new HBox(10);
        spellSlots0Hb.getChildren().addAll(titleSpellSlots0,spellBox0, addSpells0Btn);
        spells0Root.getChildren().addAll(spellSlots0Hb,spSpells0);

        addSpells0Btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Spell newSpell = spellBox0.getValue();
                spells0List.add(newSpell);
                int newRow = spells0List.size() - 1;
                c.setCantrips(spells0List);

                Label newSpellName = new Label(newSpell.getName());
                Button rm = new Button("remove");
                Button info = new Button("info");
                HBox hbNewSpell = new HBox(10);
                hbNewSpell.getChildren().addAll(newSpellName,info,rm);
                vbSpells0.getChildren().add(hbNewSpell);
                spellBox0.setValue(null);

                ////// Remove Button ///////
                Stage confirmRm = new Stage();
                rm.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        confirmRm.close();
                        confirmRm.setTitle("Are you sure?");
                        GridPane rmgrid = new GridPane();
                        rmgrid.setAlignment(Pos.CENTER);
                        rmgrid.setHgap(10);
                        rmgrid.setVgap(10);
                        Scene rmscene = new Scene(rmgrid,400,150);
                        confirmRm.setScene(rmscene);
                        confirmRm.show();

                        Label rmLabel = new Label("remove " + newSpellName + ". Are you sure?");
                        rmgrid.add(rmLabel,0,0);
                        Button yesRm = new Button("Yes");
                        Button noRm = new Button("Cancel");
                        HBox hbynrm = new HBox(10);
                        hbynrm.getChildren().addAll(yesRm,noRm);
                        rmgrid.add(hbynrm,0,1);

                        yesRm.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                vbSpells0.getChildren().remove(hbNewSpell);
                                spells0List.remove(newSpell);
                                c.setCantrips(spells0List);
                                confirmRm.close();
                            }
                        });
                        noRm.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                confirmRm.close();
                            }
                        });
                    }
                });
                /////// Info Button ///////
                Stage infoStage = new Stage();
                info.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        SpellInfoPage(infoStage, newSpell);
                    }
                }); // close info button
            }
        }); //close addSpells0btn


        ///////////////////
        ///// LEVEL 1 /////
        ///////////////////

        // for copy paste - spells#, spellslots#, itr#
        Label titleSpellslots1 = new Label("Level 1 Spells");
        titleSpellslots1.setId("spellLevelTitle");

        Button addSpells1Btn = new Button("Add Level 1 Spell");

        ComboBox<Spell> spells1Box = new ComboBox<Spell>();
        spells1Box.setPromptText("add a spell");

        VBox vbSpells1 = new VBox(10);
        ScrollPane spells1Sp = new ScrollPane();
        spells1Sp.setContent(vbSpells1);

        //spell slots
        ObservableList<Integer> spellSlotsNumbers = FXCollections.observableArrayList();
        for (int i = 0; i < 100; i++) {
            spellSlotsNumbers.add(i);
        }

        ComboBox<Integer> spellSlots1Box = new ComboBox<Integer>(spellSlotsNumbers);
        spellSlots1Box.setValue(c.getSpellSlots1());
        spellSlots1Box.valueProperty().addListener(new ChangeListener<Integer>() {
            public void changed(ObservableValue ov, Integer oldVal, Integer newVal) {
                c.setSpellSlots1(newVal);
            }
        });
        Button useSpellSlots1 = new Button("Use Spell Slot");
        useSpellSlots1.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                int currSlot = c.getSpellSlots1();
                if (currSlot != 0) {		          
                    c.setSpellSlots1(c.getSpellSlots1() - 1);
                    spellSlots1Box.setValue(spellSlots1Box.getValue() - 1);
                }
            }
        });

        for (Spell s : classSpells) {
            if (s.getLevel() == 1) {
                spells1Box.getItems().add(s);
            }
        }

        HashSet<Spell> spells1List = c.getSpells1();
        Iterator<Spell> itr1 = spells1List.iterator();



        ScrollPane spSpells1 = new ScrollPane();
        spSpells1.setContent(vbSpells1);

        while (itr1.hasNext()) {
            Spell nxtItem = itr1.next();
            String spellName = nxtItem.getName();
            Label spellLabel = new Label(spellName);
            HBox hbSpell = new HBox(10);

            Button rm = new Button("remove");
            Button info = new Button("info");
            hbSpell.getChildren().addAll(spellLabel,info,rm);

            vbSpells1.getChildren().add(hbSpell);

            ////// Remove Button ///////
            Stage confirmRm = new Stage();
            rm.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    confirmRm.close();
                    confirmRm.setTitle("Are you sure?");
                    GridPane rmgrid = new GridPane();
                    rmgrid.setAlignment(Pos.CENTER);
                    rmgrid.setHgap(10);
                    rmgrid.setVgap(10);
                    Scene rmscene = new Scene(rmgrid,400,150);
                    confirmRm.setScene(rmscene);
                    confirmRm.show();

                    Label rmLabel = new Label("remove " + spellName + ". Are you sure?");
                    rmgrid.add(rmLabel,0,0);
                    Button yesRm = new Button("Yes");
                    Button noRm = new Button("Cancel");
                    HBox hbynrm = new HBox(10);
                    hbynrm.getChildren().addAll(yesRm,noRm);
                    rmgrid.add(hbynrm,0,1);

                    yesRm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            vbSpells1.getChildren().remove(hbSpell);
                            spells1List.remove(nxtItem);
                            c.setSpells1(spells1List);
                            confirmRm.close();
                        }
                    });
                    noRm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            confirmRm.close();
                        }
                    });
                }
            });
            /////// Info Button ///////
            Stage infoStage = new Stage();
            info.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    SpellInfoPage(infoStage, nxtItem);
                }
            }); // close info button
        } // close while loop



                       


        VBox spells1Root = new VBox(10);
        HBox spells1Hb = new HBox(10);
        HBox spellSlots1Hb = new HBox(10);
        spells1Hb.getChildren().addAll(titleSpellslots1,spells1Box, addSpells1Btn);
        spellSlots1Hb.getChildren().addAll(spellSlots1Box,useSpellSlots1);
        spells1Root.getChildren().addAll(spells1Hb,spellSlots1Hb,spSpells1);


        ////// add spells ///////
        addSpells1Btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Spell newSpell = spells1Box.getValue();
                spells1List.add(newSpell);
                int newRow = spells1List.size() - 1;
                c.setSpells1(spells1List);

                Label newSpellName = new Label(newSpell.getName());
                Button rm = new Button("remove");
                Button info = new Button("info");
                HBox hbNewSpell = new HBox(10);
                hbNewSpell.getChildren().addAll(newSpellName,info,rm);
                vbSpells1.getChildren().add(hbNewSpell);
                spells1Box.setValue(null);

                ////// Remove Button ///////
                Stage confirmRm = new Stage();
                rm.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        confirmRm.close();
                        confirmRm.setTitle("Are you sure?");
                        GridPane rmgrid = new GridPane();
                        rmgrid.setAlignment(Pos.CENTER);
                        rmgrid.setHgap(10);
                        rmgrid.setVgap(10);
                        Scene rmscene = new Scene(rmgrid,400,150);
                        confirmRm.setScene(rmscene);
                        confirmRm.show();

                        Label rmLabel = new Label("remove " + newSpellName + ". Are you sure?");
                        rmgrid.add(rmLabel,0,0);
                        Button yesRm = new Button("Yes");
                        Button noRm = new Button("Cancel");
                        HBox hbynrm = new HBox(10);
                        hbynrm.getChildren().addAll(yesRm,noRm);
                        rmgrid.add(hbynrm,0,1);

                        yesRm.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                vbSpells1.getChildren().remove(hbNewSpell);
                                spells1List.remove(newSpell);
                                c.setSpells1(spells1List);
                                confirmRm.close();
                            }
                        });
                        noRm.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                confirmRm.close();
                            }
                        });
                    }
                });
                /////// Info Button ///////
                Stage infoStage = new Stage();
                info.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        SpellInfoPage(infoStage, newSpell);
                    }
                }); // close info button
            }
        }); //close addSpells1btn


        ///////////////////
        ///// LEVEL 2 /////
        ///////////////////

        // for copy paste - spells#, spellslots#, itr#
        Label titleSpellslots2 = new Label("Level 2 Spells");
        titleSpellslots2.setId("spellLevelTitle");

        Button addSpells2Btn = new Button("Add Level 2 Spell");

        ComboBox<Spell> spells2Box = new ComboBox<Spell>();
        spells2Box.setPromptText("add a spell");

        VBox vbSpells2 = new VBox(10);
        ScrollPane spells2Sp = new ScrollPane();
        spells2Sp.setContent(vbSpells2);

        ComboBox<Integer> spellslots2Box = new ComboBox<Integer>(spellSlotsNumbers);
        spellslots2Box.setValue(c.getSpellSlots2());
        spellslots2Box.valueProperty().addListener(new ChangeListener<Integer>() {
            public void changed(ObservableValue ov, Integer oldVal, Integer newVal) {
                c.setSpellSlots2(newVal);
            }
        });
        Button useSpellslots2 = new Button("Use Spell Slot");
        useSpellslots2.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                int currSlot = c.getSpellSlots2();
                if (currSlot != 0) {		          
                    c.setSpellSlots2(c.getSpellSlots2() - 1);
                    spellslots2Box.setValue(spellslots2Box.getValue() - 1);
                }
            }
        });

        for (Spell s : classSpells) {
            if (s.getLevel() == 2) {
                spells2Box.getItems().add(s);
            }
        }

        HashSet<Spell> spells2List = c.getSpells2();
        Iterator<Spell> itr2 = spells2List.iterator();



        ScrollPane spSpells2 = new ScrollPane();
        spSpells2.setContent(vbSpells2);

        while (itr2.hasNext()) {
            Spell nxtItem = itr2.next();
            String spellName = nxtItem.getName();
            Label spellLabel = new Label(spellName);
            HBox hbSpell = new HBox(10);

            Button rm = new Button("remove");
            Button info = new Button("info");
            hbSpell.getChildren().addAll(spellLabel,info,rm);

            vbSpells2.getChildren().add(hbSpell);

            ////// Remove Button ///////
            Stage confirmRm = new Stage();
            rm.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    confirmRm.close();
                    confirmRm.setTitle("Are you sure?");
                    GridPane rmgrid = new GridPane();
                    rmgrid.setAlignment(Pos.CENTER);
                    rmgrid.setHgap(10);
                    rmgrid.setVgap(10);
                    Scene rmscene = new Scene(rmgrid,400,150);
                    confirmRm.setScene(rmscene);
                    confirmRm.show();

                    Label rmLabel = new Label("remove " + spellName + ". Are you sure?");
                    rmgrid.add(rmLabel,0,0);
                    Button yesRm = new Button("Yes");
                    Button noRm = new Button("Cancel");
                    HBox hbynrm = new HBox(10);
                    hbynrm.getChildren().addAll(yesRm,noRm);
                    rmgrid.add(hbynrm,0,1);

                    yesRm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            vbSpells2.getChildren().remove(hbSpell);
                            spells2List.remove(nxtItem);
                            c.setSpells2(spells2List);
                            confirmRm.close();
                        }
                    });
                    noRm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            confirmRm.close();
                        }
                    });
                }
            });
            /////// Info Button ///////
            Stage infoStage = new Stage();
            info.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    SpellInfoPage(infoStage, nxtItem);
                }
            }); // close info button
        } // close while loop

        VBox spells2Root = new VBox(10);
        HBox spells2Hb = new HBox(10);
        HBox spellslots2Hb = new HBox(10);
        spells2Hb.getChildren().addAll(titleSpellslots2,spells2Box, addSpells2Btn);
        spellslots2Hb.getChildren().addAll(spellslots2Box,useSpellslots2);
        spells2Root.getChildren().addAll(spells2Hb,spellslots2Hb,spSpells2);


        ////// add spells ///////
        addSpells2Btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Spell newSpell = spells2Box.getValue();
                spells2List.add(newSpell);
                int newRow = spells2List.size() - 1;
                c.setSpells2(spells2List);

                Label newSpellName = new Label(newSpell.getName());
                Button rm = new Button("remove");
                Button info = new Button("info");
                HBox hbNewSpell = new HBox(10);
                hbNewSpell.getChildren().addAll(newSpellName,info,rm);
                vbSpells2.getChildren().add(hbNewSpell);
                spells2Box.setValue(null);

                ////// Remove Button ///////
                Stage confirmRm = new Stage();
                rm.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        confirmRm.close();
                        confirmRm.setTitle("Are you sure?");
                        GridPane rmgrid = new GridPane();
                        rmgrid.setAlignment(Pos.CENTER);
                        rmgrid.setHgap(10);
                        rmgrid.setVgap(10);
                        Scene rmscene = new Scene(rmgrid,400,150);
                        confirmRm.setScene(rmscene);
                        confirmRm.show();

                        Label rmLabel = new Label("remove " + newSpellName + ". Are you sure?");
                        rmgrid.add(rmLabel,0,0);
                        Button yesRm = new Button("Yes");
                        Button noRm = new Button("Cancel");
                        HBox hbynrm = new HBox(10);
                        hbynrm.getChildren().addAll(yesRm,noRm);
                        rmgrid.add(hbynrm,0,1);

                        yesRm.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                vbSpells2.getChildren().remove(hbNewSpell);
                                spells2List.remove(newSpell);
                                c.setSpells2(spells2List);
                                confirmRm.close();
                            }
                        });
                        noRm.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                confirmRm.close();
                            }
                        });
                    }
                });
                /////// Info Button ///////
                Stage infoStage = new Stage();
                info.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        SpellInfoPage(infoStage, newSpell);
                    }
                }); // close info button
            }
        }); //close addSpells2btn

        ///////////////////
        ///// LEVEL 3 /////
        ///////////////////

        // for copy paste - spells#, spellslots#, itr#
        Label titleSpellslots3 = new Label("Level 3 Spells");
        titleSpellslots3.setId("spellLevelTitle");

        Button addSpells3Btn = new Button("Add Level 3 Spell");

        ComboBox<Spell> spells3Box = new ComboBox<Spell>();
        spells3Box.setPromptText("add a spell");

        VBox vbSpells3 = new VBox(10);
        ScrollPane spells3Sp = new ScrollPane();
        spells3Sp.setContent(vbSpells3);

        ComboBox<Integer> spellslots3Box = new ComboBox<Integer>(spellSlotsNumbers);
        spellslots3Box.setValue(c.getSpellSlots3());
        spellslots3Box.valueProperty().addListener(new ChangeListener<Integer>() {
            public void changed(ObservableValue ov, Integer oldVal, Integer newVal) {
                c.setSpellSlots3(newVal);
            }
        });
        Button useSpellslots3 = new Button("Use Spell Slot");
        useSpellslots3.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                int currSlot = c.getSpellSlots3();
                if (currSlot != 0) {		          
                    c.setSpellSlots3(c.getSpellSlots3() - 1);
                    spellslots3Box.setValue(spellslots3Box.getValue() - 1);
                }
            }
        });


        for (Spell s : classSpells) {
            if (s.getLevel() == 3) {
                spells3Box.getItems().add(s);
            }
        }

        HashSet<Spell> spells3List = c.getSpells3();
        Iterator<Spell> itr3 = spells3List.iterator();



        ScrollPane spSpells3 = new ScrollPane();
        spSpells3.setContent(vbSpells3);

        while (itr3.hasNext()) {
            Spell nxtItem = itr3.next();
            String spellName = nxtItem.getName();
            Label spellLabel = new Label(spellName);
            HBox hbSpell = new HBox(10);

            Button rm = new Button("remove");
            Button info = new Button("info");
            hbSpell.getChildren().addAll(spellLabel,info,rm);

            vbSpells3.getChildren().add(hbSpell);

            ////// Remove Button ///////
            Stage confirmRm = new Stage();
            rm.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    confirmRm.close();
                    confirmRm.setTitle("Are you sure?");
                    GridPane rmgrid = new GridPane();
                    rmgrid.setAlignment(Pos.CENTER);
                    rmgrid.setHgap(10);
                    rmgrid.setVgap(10);
                    Scene rmscene = new Scene(rmgrid,400,150);
                    confirmRm.setScene(rmscene);
                    confirmRm.show();

                    Label rmLabel = new Label("remove " + spellName + ". Are you sure?");
                    rmgrid.add(rmLabel,0,0);
                    Button yesRm = new Button("Yes");
                    Button noRm = new Button("Cancel");
                    HBox hbynrm = new HBox(10);
                    hbynrm.getChildren().addAll(yesRm,noRm);
                    rmgrid.add(hbynrm,0,1);

                    yesRm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            vbSpells3.getChildren().remove(hbSpell);
                            spells3List.remove(nxtItem);
                            c.setSpells3(spells3List);
                            confirmRm.close();
                        }
                    });
                    noRm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            confirmRm.close();
                        }
                    });
                }
            });
            /////// Info Button ///////
            Stage infoStage = new Stage();
            info.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    SpellInfoPage(infoStage, nxtItem);
                }
            }); // close info button
        } // close while loop


        VBox spells3Root = new VBox(10);
        HBox spells3Hb = new HBox(10);
        HBox spellslots3Hb = new HBox(10);
        spells3Hb.getChildren().addAll(titleSpellslots3,spells3Box, addSpells3Btn);
        spellslots3Hb.getChildren().addAll(spellslots3Box,useSpellslots3);
        spells3Root.getChildren().addAll(spells3Hb,spellslots3Hb,spSpells3);


        ////// add spells ///////
        addSpells3Btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Spell newSpell = spells3Box.getValue();
                spells3List.add(newSpell);
                int newRow = spells3List.size() - 1;
                c.setSpells3(spells3List);

                Label newSpellName = new Label(newSpell.getName());
                Button rm = new Button("remove");
                Button info = new Button("info");
                HBox hbNewSpell = new HBox(10);
                hbNewSpell.getChildren().addAll(newSpellName,info,rm);
                vbSpells3.getChildren().add(hbNewSpell);
                spells3Box.setValue(null);

                ////// Remove Button ///////
                Stage confirmRm = new Stage();
                rm.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        confirmRm.close();
                        confirmRm.setTitle("Are you sure?");
                        GridPane rmgrid = new GridPane();
                        rmgrid.setAlignment(Pos.CENTER);
                        rmgrid.setHgap(10);
                        rmgrid.setVgap(10);
                        Scene rmscene = new Scene(rmgrid,400,150);
                        confirmRm.setScene(rmscene);
                        confirmRm.show();

                        Label rmLabel = new Label("remove " + newSpellName + ". Are you sure?");
                        rmgrid.add(rmLabel,0,0);
                        Button yesRm = new Button("Yes");
                        Button noRm = new Button("Cancel");
                        HBox hbynrm = new HBox(10);
                        hbynrm.getChildren().addAll(yesRm,noRm);
                        rmgrid.add(hbynrm,0,1);

                        yesRm.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                vbSpells3.getChildren().remove(hbNewSpell);
                                spells3List.remove(newSpell);
                                c.setSpells3(spells3List);
                                confirmRm.close();
                            }
                        });
                        noRm.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                confirmRm.close();
                            }
                        });
                    }
                });
                /////// Info Button ///////
                Stage infoStage = new Stage();
                info.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        SpellInfoPage(infoStage, newSpell);
                    }
                }); // close info button
            }
        }); //close addSpells3btn

        ///////////////////
        ///// LEVEL 4 /////
        ///////////////////

        // for copy paste - spells#, spellslots#, itr#
        Label titleSpellslots4 = new Label("Level 4 Spells");
        titleSpellslots4.setId("spellLevelTitle");

        Button addSpells4Btn = new Button("Add Level 4 Spell");

        ComboBox<Spell> spells4Box = new ComboBox<Spell>();
        spells4Box.setPromptText("add a spell");

        VBox vbSpells4 = new VBox(10);
        ScrollPane spells4Sp = new ScrollPane();
        spells4Sp.setContent(vbSpells4);

        ComboBox<Integer> spellslots4Box = new ComboBox<Integer>(spellSlotsNumbers);
        spellslots4Box.setValue(c.getSpellSlots4());
        spellslots4Box.valueProperty().addListener(new ChangeListener<Integer>() {
            public void changed(ObservableValue ov, Integer oldVal, Integer newVal) {
                c.setSpellSlots4(newVal);
            }
        });
        Button useSpellslots4 = new Button("Use Spell Slot");
        useSpellslots4.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                int currSlot = c.getSpellSlots4();
                if (currSlot != 0) {		          
                    c.setSpellSlots4(c.getSpellSlots4() - 1);
                    spellslots4Box.setValue(spellslots4Box.getValue() - 1);
                }
            }
        });

        for (Spell s : classSpells) {
            if (s.getLevel() == 4) {
                spells4Box.getItems().add(s);
            }
        }

        HashSet<Spell> spells4List = c.getSpells4();
        Iterator<Spell> itr4 = spells4List.iterator();



        ScrollPane spSpells4 = new ScrollPane();
        spSpells4.setContent(vbSpells4);

        while (itr4.hasNext()) {
            Spell nxtItem = itr4.next();
            String spellName = nxtItem.getName();
            Label spellLabel = new Label(spellName);
            HBox hbSpell = new HBox(10);

            Button rm = new Button("remove");
            Button info = new Button("info");
            hbSpell.getChildren().addAll(spellLabel,info,rm);

            vbSpells4.getChildren().add(hbSpell);

            ////// Remove Button ///////
            Stage confirmRm = new Stage();
            rm.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    confirmRm.close();
                    confirmRm.setTitle("Are you sure?");
                    GridPane rmgrid = new GridPane();
                    rmgrid.setAlignment(Pos.CENTER);
                    rmgrid.setHgap(10);
                    rmgrid.setVgap(10);
                    Scene rmscene = new Scene(rmgrid,400,150);
                    confirmRm.setScene(rmscene);
                    confirmRm.show();

                    Label rmLabel = new Label("remove " + spellName + ". Are you sure?");
                    rmgrid.add(rmLabel,0,0);
                    Button yesRm = new Button("Yes");
                    Button noRm = new Button("Cancel");
                    HBox hbynrm = new HBox(10);
                    hbynrm.getChildren().addAll(yesRm,noRm);
                    rmgrid.add(hbynrm,0,1);

                    yesRm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            vbSpells4.getChildren().remove(hbSpell);
                            spells4List.remove(nxtItem);
                            c.setSpells4(spells4List);
                            confirmRm.close();
                        }
                    });
                    noRm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            confirmRm.close();
                        }
                    });
                }
            });
            /////// Info Button ///////
            Stage infoStage = new Stage();
            info.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    SpellInfoPage(infoStage, nxtItem);
                }
            }); // close info button
        } // close while loop


        VBox spells4Root = new VBox(10);
        HBox spells4Hb = new HBox(10);
        HBox spellslots4Hb = new HBox(10);
        spells4Hb.getChildren().addAll(titleSpellslots4,spells4Box, addSpells4Btn);
        spellslots4Hb.getChildren().addAll(spellslots4Box,useSpellslots4);
        spells4Root.getChildren().addAll(spells4Hb,spellslots4Hb,spSpells4);


        ////// add spells ///////
        addSpells4Btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Spell newSpell = spells4Box.getValue();
                spells4List.add(newSpell);
                int newRow = spells4List.size() - 1;
                c.setSpells4(spells4List);

                Label newSpellName = new Label(newSpell.getName());
                Button rm = new Button("remove");
                Button info = new Button("info");
                HBox hbNewSpell = new HBox(10);
                hbNewSpell.getChildren().addAll(newSpellName,info,rm);
                vbSpells4.getChildren().add(hbNewSpell);
                spells4Box.setValue(null);

                ////// Remove Button ///////
                Stage confirmRm = new Stage();
                rm.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        confirmRm.close();
                        confirmRm.setTitle("Are you sure?");
                        GridPane rmgrid = new GridPane();
                        rmgrid.setAlignment(Pos.CENTER);
                        rmgrid.setHgap(10);
                        rmgrid.setVgap(10);
                        Scene rmscene = new Scene(rmgrid,400,150);
                        confirmRm.setScene(rmscene);
                        confirmRm.show();

                        Label rmLabel = new Label("remove " + newSpellName + ". Are you sure?");
                        rmgrid.add(rmLabel,0,0);
                        Button yesRm = new Button("Yes");
                        Button noRm = new Button("Cancel");
                        HBox hbynrm = new HBox(10);
                        hbynrm.getChildren().addAll(yesRm,noRm);
                        rmgrid.add(hbynrm,0,1);

                        yesRm.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                vbSpells4.getChildren().remove(hbNewSpell);
                                spells4List.remove(newSpell);
                                c.setSpells4(spells4List);
                                confirmRm.close();
                            }
                        });
                        noRm.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                confirmRm.close();
                            }
                        });
                    }
                });
                /////// Info Button ///////
                Stage infoStage = new Stage();
                info.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        SpellInfoPage(infoStage, newSpell);
                    }
                }); // close info button
            }
        }); //close addSpells4Btn

        ///////////////////
        ///// LEVEL 5 /////
        ///////////////////

        // for copy paste - spells#, spellslots#, itr#
        Label titleSpellslots5 = new Label("Level 5 Spells");
        titleSpellslots5.setId("spellLevelTitle");

        Button addSpells5Btn = new Button("Add Level 5 Spell");

        ComboBox<Spell> spells5Box = new ComboBox<Spell>();
        spells5Box.setPromptText("add a spell");

        VBox vbSpells5 = new VBox(10);
        ScrollPane spells5Sp = new ScrollPane();
        spells5Sp.setContent(vbSpells5);

        ComboBox<Integer> spellslots5Box = new ComboBox<Integer>(spellSlotsNumbers);
        spellslots5Box.setValue(c.getSpellSlots5());
        spellslots5Box.valueProperty().addListener(new ChangeListener<Integer>() {
            public void changed(ObservableValue ov, Integer oldVal, Integer newVal) {
                c.setSpellSlots5(newVal);
            }
        });
        Button useSpellslots5 = new Button("Use Spell Slot");
        useSpellslots5.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                int currSlot = c.getSpellSlots5();
                if (currSlot != 0) {		          
                    c.setSpellSlots5(c.getSpellSlots5() - 1);
                    spellslots5Box.setValue(spellslots5Box.getValue() - 1);
                }
            }
        });


        for (Spell s : classSpells) {
            if (s.getLevel() == 5) {
                spells5Box.getItems().add(s);
            }
        }

        HashSet<Spell> spells5List = c.getSpells5();
        Iterator<Spell> itr5 = spells5List.iterator();



        ScrollPane spSpells5 = new ScrollPane();
        spSpells5.setContent(vbSpells5);

        while (itr5.hasNext()) {
            Spell nxtItem = itr5.next();
            String spellName = nxtItem.getName();
            Label spellLabel = new Label(spellName);
            HBox hbSpell = new HBox(10);

            Button rm = new Button("remove");
            Button info = new Button("info");
            hbSpell.getChildren().addAll(spellLabel,info,rm);

            vbSpells5.getChildren().add(hbSpell);

            ////// Remove Button ///////
            Stage confirmRm = new Stage();
            rm.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    confirmRm.close();
                    confirmRm.setTitle("Are you sure?");
                    GridPane rmgrid = new GridPane();
                    rmgrid.setAlignment(Pos.CENTER);
                    rmgrid.setHgap(10);
                    rmgrid.setVgap(10);
                    Scene rmscene = new Scene(rmgrid,400,150);
                    confirmRm.setScene(rmscene);
                    confirmRm.show();

                    Label rmLabel = new Label("remove " + spellName + ". Are you sure?");
                    rmgrid.add(rmLabel,0,0);
                    Button yesRm = new Button("Yes");
                    Button noRm = new Button("Cancel");
                    HBox hbynrm = new HBox(10);
                    hbynrm.getChildren().addAll(yesRm,noRm);
                    rmgrid.add(hbynrm,0,1);

                    yesRm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            vbSpells5.getChildren().remove(hbSpell);
                            spells5List.remove(nxtItem);
                            c.setSpells5(spells5List);
                            confirmRm.close();
                        }
                    });
                    noRm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            confirmRm.close();
                        }
                    });
                }
            });
            /////// Info Button ///////
            Stage infoStage = new Stage();
            info.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    SpellInfoPage(infoStage, nxtItem);
                }
            }); // close info button
        } // close while loop


        VBox spells5Root = new VBox(10);
        HBox spells5Hb = new HBox(10);
        HBox spellslots5Hb = new HBox(10);
        spells5Hb.getChildren().addAll(titleSpellslots5,spells5Box, addSpells5Btn);
        spellslots5Hb.getChildren().addAll(spellslots5Box,useSpellslots5);
        spells5Root.getChildren().addAll(spells5Hb,spellslots5Hb,spSpells5);


        ////// add spells ///////
        addSpells5Btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Spell newSpell = spells5Box.getValue();
                spells5List.add(newSpell);
                int newRow = spells5List.size() - 1;
                c.setSpells5(spells5List);

                Label newSpellName = new Label(newSpell.getName());
                Button rm = new Button("remove");
                Button info = new Button("info");
                HBox hbNewSpell = new HBox(10);
                hbNewSpell.getChildren().addAll(newSpellName,info,rm);
                vbSpells5.getChildren().add(hbNewSpell);
                spells5Box.setValue(null);

                ////// Remove Button ///////
                Stage confirmRm = new Stage();
                rm.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        confirmRm.close();
                        confirmRm.setTitle("Are you sure?");
                        GridPane rmgrid = new GridPane();
                        rmgrid.setAlignment(Pos.CENTER);
                        rmgrid.setHgap(10);
                        rmgrid.setVgap(10);
                        Scene rmscene = new Scene(rmgrid,400,150);
                        confirmRm.setScene(rmscene);
                        confirmRm.show();

                        Label rmLabel = new Label("remove " + newSpellName + ". Are you sure?");
                        rmgrid.add(rmLabel,0,0);
                        Button yesRm = new Button("Yes");
                        Button noRm = new Button("Cancel");
                        HBox hbynrm = new HBox(10);
                        hbynrm.getChildren().addAll(yesRm,noRm);
                        rmgrid.add(hbynrm,0,1);

                        yesRm.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                vbSpells5.getChildren().remove(hbNewSpell);
                                spells5List.remove(newSpell);
                                c.setSpells5(spells5List);
                                confirmRm.close();
                            }
                        });
                        noRm.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                confirmRm.close();
                            }
                        });
                    }
                });
                /////// Info Button ///////
                Stage infoStage = new Stage();
                info.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        SpellInfoPage(infoStage, newSpell);
                    }
                }); // close info button
            }
        }); //close addSpells5btn

        ///////////////////
        ///// LEVEL 6 /////
        ///////////////////

        Label titleSpellslots6 = new Label("Level 6 Spells");
        titleSpellslots6.setId("spellLevelTitle");

        Button addSpells6Btn = new Button("Add Level 6 Spell");

        ComboBox<Spell> spells6Box = new ComboBox<Spell>();
        spells6Box.setPromptText("add a spell");

        VBox vbSpells6 = new VBox(10);
        ScrollPane spells6Sp = new ScrollPane();
        spells6Sp.setContent(vbSpells6);

        ComboBox<Integer> spellslots6Box = new ComboBox<Integer>(spellSlotsNumbers);
        spellslots6Box.setValue(c.getSpellSlots6());
        spellslots6Box.valueProperty().addListener(new ChangeListener<Integer>() {
            public void changed(ObservableValue ov, Integer oldVal, Integer newVal) {
                c.setSpellSlots6(newVal);
            }
        });
        Button useSpellslots6 = new Button("Use Spell Slot");
        useSpellslots6.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                int currSlot = c.getSpellSlots6();
                if (currSlot != 0) {		          
                    c.setSpellSlots6(c.getSpellSlots6() - 1);
                    spellslots6Box.setValue(spellslots6Box.getValue() - 1);
                }
            }
        });


        for (Spell s : classSpells) {
            if (s.getLevel() == 6) {
                spells6Box.getItems().add(s);
            }
        }

        HashSet<Spell> spells6List = c.getSpells6();
        Iterator<Spell> itr6 = spells6List.iterator();



        ScrollPane spSpells6 = new ScrollPane();
        spSpells6.setContent(vbSpells6);

        while (itr6.hasNext()) {
            Spell nxtItem = itr6.next();
            String spellName = nxtItem.getName();
            Label spellLabel = new Label(spellName);
            HBox hbSpell = new HBox(10);

            Button rm = new Button("remove");
            Button info = new Button("info");
            hbSpell.getChildren().addAll(spellLabel,info,rm);

            vbSpells6.getChildren().add(hbSpell);

            ////// Remove Button ///////
            Stage confirmRm = new Stage();
            rm.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    confirmRm.close();
                    confirmRm.setTitle("Are you sure?");
                    GridPane rmgrid = new GridPane();
                    rmgrid.setAlignment(Pos.CENTER);
                    rmgrid.setHgap(10);
                    rmgrid.setVgap(10);
                    Scene rmscene = new Scene(rmgrid,400,150);
                    confirmRm.setScene(rmscene);
                    confirmRm.show();

                    Label rmLabel = new Label("remove " + spellName + ". Are you sure?");
                    rmgrid.add(rmLabel,0,0);
                    Button yesRm = new Button("Yes");
                    Button noRm = new Button("Cancel");
                    HBox hbynrm = new HBox(10);
                    hbynrm.getChildren().addAll(yesRm,noRm);
                    rmgrid.add(hbynrm,0,1);

                    yesRm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            vbSpells6.getChildren().remove(hbSpell);
                            spells6List.remove(nxtItem);
                            c.setSpells6(spells6List);
                            confirmRm.close();
                            }
                        });
                    noRm.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                confirmRm.close();
                            }
                    });
                }
            });
            /////// Info Button ///////
            Stage infoStage = new Stage();
            info.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    SpellInfoPage(infoStage, nxtItem);
                }
            }); // close info button
        } // close while loop


        VBox spells6Root = new VBox(10);
        HBox spells6Hb = new HBox(10);
	    HBox spellslots6Hb = new HBox(10);
        spells6Hb.getChildren().addAll(titleSpellslots6,spells6Box, addSpells6Btn);
        spellslots6Hb.getChildren().addAll(spellslots6Box,useSpellslots6);
        spells6Root.getChildren().addAll(spells6Hb,spellslots6Hb,spSpells6);


        ////// add spells ///////
        addSpells6Btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Spell newSpell = spells6Box.getValue();
                spells6List.add(newSpell);
                int newRow = spells6List.size() - 1;
                c.setSpells6(spells6List);

                Label newSpellName = new Label(newSpell.getName());
                Button rm = new Button("remove");
                Button info = new Button("info");
                HBox hbNewSpell = new HBox(10);
                hbNewSpell.getChildren().addAll(newSpellName,info,rm);
                vbSpells6.getChildren().add(hbNewSpell);
                spells6Box.setValue(null);

                ////// Remove Button ///////
                Stage confirmRm = new Stage();
                rm.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        confirmRm.close();
                        confirmRm.setTitle("Are you sure?");
                        GridPane rmgrid = new GridPane();
                        rmgrid.setAlignment(Pos.CENTER);
                        rmgrid.setHgap(10);
                        rmgrid.setVgap(10);
                        Scene rmscene = new Scene(rmgrid,400,150);
                        confirmRm.setScene(rmscene);
                        confirmRm.show();

                        Label rmLabel = new Label("remove " + newSpellName + ". Are you sure?");
                        rmgrid.add(rmLabel,0,0);
                        Button yesRm = new Button("Yes");
                        Button noRm = new Button("Cancel");
                        HBox hbynrm = new HBox(10);
                        hbynrm.getChildren().addAll(yesRm,noRm);
                        rmgrid.add(hbynrm,0,1);

                        yesRm.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                vbSpells6.getChildren().remove(hbNewSpell);
                                spells6List.remove(newSpell);
                                c.setSpells6(spells6List);
                                confirmRm.close();
                            }
                        });
                        noRm.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                confirmRm.close();
                            }
                        });
                    }
                });
                /////// Info Button ///////
                Stage infoStage = new Stage();
                info.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        SpellInfoPage(infoStage, newSpell);
                    }
                }); // close info button
            }
        }); //close addSpells6btn	   

        ///////////////////
        ///// LEVEL 7 /////
        ///////////////////

        // for copy paste - spells#, spellslots#, itr#
        Label titleSpellslots7 = new Label("Level 7 Spells");
        titleSpellslots7.setId("spellLevelTitle");

        Button addSpells7Btn = new Button("Add Level 7 Spell");

        ComboBox<Spell> spells7Box = new ComboBox<Spell>();
        spells7Box.setPromptText("add a spell");

        VBox vbSpells7 = new VBox(10);
        ScrollPane spells7Sp = new ScrollPane();
        spells7Sp.setContent(vbSpells7);

        ComboBox<Integer> spellslots7Box = new ComboBox<Integer>(spellSlotsNumbers);
        spellslots7Box.setValue(c.getSpellSlots7());
        spellslots7Box.valueProperty().addListener(new ChangeListener<Integer>() {
            public void changed(ObservableValue ov, Integer oldVal, Integer newVal) {
                c.setSpellSlots7(newVal);
            }
        });
        Button useSpellslots7 = new Button("Use Spell Slot");
        useSpellslots7.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                int currSlot = c.getSpellSlots7();
                if (currSlot != 0) {		          
                    c.setSpellSlots7(c.getSpellSlots7() - 1);
                    spellslots7Box.setValue(spellslots7Box.getValue() - 1);
                }
            }
        });


        for (Spell s : classSpells) {
            if (s.getLevel() == 7) {
                spells7Box.getItems().add(s);
            }
        }

        HashSet<Spell> spells7List = c.getSpells7();
        Iterator<Spell> itr7 = spells7List.iterator();



        ScrollPane spSpells7 = new ScrollPane();
        spSpells7.setContent(vbSpells7);

        while (itr7.hasNext()) {
            Spell nxtItem = itr7.next();
            String spellName = nxtItem.getName();
            Label spellLabel = new Label(spellName);
            HBox hbSpell = new HBox(10);

            Button rm = new Button("remove");
            Button info = new Button("info");
            hbSpell.getChildren().addAll(spellLabel,info,rm);

            vbSpells7.getChildren().add(hbSpell);

            ////// Remove Button ///////
            Stage confirmRm = new Stage();
            rm.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    confirmRm.close();
                    confirmRm.setTitle("Are you sure?");
                    GridPane rmgrid = new GridPane();
                    rmgrid.setAlignment(Pos.CENTER);
                    rmgrid.setHgap(10);
                    rmgrid.setVgap(10);
                    Scene rmscene = new Scene(rmgrid,400,150);
                    confirmRm.setScene(rmscene);
                    confirmRm.show();

                    Label rmLabel = new Label("remove " + spellName + ". Are you sure?");
                    rmgrid.add(rmLabel,0,0);
                    Button yesRm = new Button("Yes");
                    Button noRm = new Button("Cancel");
                    HBox hbynrm = new HBox(10);
                    hbynrm.getChildren().addAll(yesRm,noRm);
                    rmgrid.add(hbynrm,0,1);

                    yesRm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            vbSpells7.getChildren().remove(hbSpell);
                            spells7List.remove(nxtItem);
                            c.setSpells7(spells7List);
                            confirmRm.close();
                        }
                    });
                    noRm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            confirmRm.close();
                        }
                    });
                }
            });
            /////// Info Button ///////
            Stage infoStage = new Stage();
            info.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    SpellInfoPage(infoStage, nxtItem);
                }
            }); // close info button
        } // close while loop


        VBox spells7Root = new VBox(10);
        HBox spells7Hb = new HBox(10);
        HBox spellslots7Hb = new HBox(10);
        spells7Hb.getChildren().addAll(titleSpellslots7,spells7Box, addSpells7Btn);
        spellslots7Hb.getChildren().addAll(spellslots7Box,useSpellslots7);
        spells7Root.getChildren().addAll(spells7Hb,spellslots7Hb,spSpells7);


        ////// add spells ///////
        addSpells7Btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Spell newSpell = spells7Box.getValue();
                spells7List.add(newSpell);
                int newRow = spells7List.size() - 1;
                c.setSpells7(spells7List);

                Label newSpellName = new Label(newSpell.getName());
                Button rm = new Button("remove");
                Button info = new Button("info");
                HBox hbNewSpell = new HBox(10);
                hbNewSpell.getChildren().addAll(newSpellName,info,rm);
                vbSpells7.getChildren().add(hbNewSpell);
                spells7Box.setValue(null);

                ////// Remove Button ///////
                Stage confirmRm = new Stage();
                rm.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        confirmRm.close();
                        confirmRm.setTitle("Are you sure?");
                        GridPane rmgrid = new GridPane();
                        rmgrid.setAlignment(Pos.CENTER);
                        rmgrid.setHgap(10);
                        rmgrid.setVgap(10);
                        Scene rmscene = new Scene(rmgrid,400,150);
                        confirmRm.setScene(rmscene);
                        confirmRm.show();

                        Label rmLabel = new Label("remove " + newSpellName + ". Are you sure?");
                        rmgrid.add(rmLabel,0,0);
                        Button yesRm = new Button("Yes");
                        Button noRm = new Button("Cancel");
                        HBox hbynrm = new HBox(10);
                        hbynrm.getChildren().addAll(yesRm,noRm);
                        rmgrid.add(hbynrm,0,1);

                        yesRm.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                vbSpells7.getChildren().remove(hbNewSpell);
                                spells7List.remove(newSpell);
                                c.setSpells7(spells7List);
                                confirmRm.close();
                            }
                        });
                        noRm.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                confirmRm.close();
                            }
                        });
                    }
                });
                /////// Info Button ///////
                Stage infoStage = new Stage();
                info.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        SpellInfoPage(infoStage, newSpell);
                    }
                }); // close info button
            }
        }); //close addSpells7btn

        ///////////////////
        ///// LEVEL 8 /////
        ///////////////////

        // for copy paste - spells#, spellslots#, itr#
        Label titleSpellslots8 = new Label("Level 8 Spells");
        titleSpellslots8.setId("spellLevelTitle");

        Button addSpells8Btn = new Button("Add Level 8 Spell");

        ComboBox<Spell> spells8Box = new ComboBox<Spell>();
        spells8Box.setPromptText("add a spell");

        VBox vbSpells8 = new VBox(10);
        ScrollPane spells8Sp = new ScrollPane();
        spells8Sp.setContent(vbSpells8);

        ComboBox<Integer> spellslots8Box = new ComboBox<Integer>(spellSlotsNumbers);
        spellslots8Box.setValue(c.getSpellSlots8());
        spellslots8Box.valueProperty().addListener(new ChangeListener<Integer>() {
            public void changed(ObservableValue ov, Integer oldVal, Integer newVal) {
                c.setSpellSlots8(newVal);
            }
        });
        Button useSpellslots8 = new Button("Use Spell Slot");
        useSpellslots8.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                int currSlot = c.getSpellSlots8();
                if (currSlot != 0) {		          
                    c.setSpellSlots8(c.getSpellSlots8() - 1);
                    spellslots8Box.setValue(spellslots8Box.getValue() - 1);
                }
            }
        });


        for (Spell s : classSpells) {
            if (s.getLevel() == 8) {
                spells8Box.getItems().add(s);
            }
        }

        HashSet<Spell> spells8List = c.getSpells8();
        Iterator<Spell> itr8 = spells8List.iterator();



        ScrollPane spSpells8 = new ScrollPane();
        spSpells8.setContent(vbSpells8);

        while (itr8.hasNext()) {
            Spell nxtItem = itr8.next();
            String spellName = nxtItem.getName();
            Label spellLabel = new Label(spellName);
            HBox hbSpell = new HBox(10);

            Button rm = new Button("remove");
            Button info = new Button("info");
            hbSpell.getChildren().addAll(spellLabel,info,rm);

            vbSpells8.getChildren().add(hbSpell);

            ////// Remove Button ///////
            Stage confirmRm = new Stage();
            rm.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    confirmRm.close();
                    confirmRm.setTitle("Are you sure?");
                    GridPane rmgrid = new GridPane();
                    rmgrid.setAlignment(Pos.CENTER);
                    rmgrid.setHgap(10);
                    rmgrid.setVgap(10);
                    Scene rmscene = new Scene(rmgrid,400,150);
                    confirmRm.setScene(rmscene);
                    confirmRm.show();

                    Label rmLabel = new Label("remove " + spellName + ". Are you sure?");
                    rmgrid.add(rmLabel,0,0);
                    Button yesRm = new Button("Yes");
                    Button noRm = new Button("Cancel");
                    HBox hbynrm = new HBox(10);
                    hbynrm.getChildren().addAll(yesRm,noRm);
                    rmgrid.add(hbynrm,0,1);

                    yesRm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            vbSpells8.getChildren().remove(hbSpell);
                            spells8List.remove(nxtItem);
                            c.setSpells8(spells8List);
                            confirmRm.close();
                        }
                    });
                    noRm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            confirmRm.close();
                        }
                    });
                }
            });
            /////// Info Button ///////
            Stage infoStage = new Stage();
            info.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    SpellInfoPage(infoStage, nxtItem);
                }
            }); // close info button
        } // close while loop


        VBox spells8Root = new VBox(10);
        HBox spells8Hb = new HBox(10);
        HBox spellslots8Hb = new HBox(10);
        spells8Hb.getChildren().addAll(titleSpellslots8,spells8Box, addSpells8Btn);
        spellslots8Hb.getChildren().addAll(spellslots8Box,useSpellslots8);
        spells8Root.getChildren().addAll(spells8Hb,spellslots8Hb,spSpells8);


        ////// add spells ///////
        addSpells8Btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Spell newSpell = spells8Box.getValue();
                spells8List.add(newSpell);
                int newRow = spells8List.size() - 1;
                c.setSpells8(spells8List);

                Label newSpellName = new Label(newSpell.getName());
                Button rm = new Button("remove");
                Button info = new Button("info");
                HBox hbNewSpell = new HBox(10);
                hbNewSpell.getChildren().addAll(newSpellName,info,rm);
                vbSpells8.getChildren().add(hbNewSpell);
                spells8Box.setValue(null);

                ////// Remove Button ///////
                Stage confirmRm = new Stage();
                rm.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        confirmRm.close();
                        confirmRm.setTitle("Are you sure?");
                        GridPane rmgrid = new GridPane();
                        rmgrid.setAlignment(Pos.CENTER);
                        rmgrid.setHgap(10);
                        rmgrid.setVgap(10);
                        Scene rmscene = new Scene(rmgrid,400,150);
                        confirmRm.setScene(rmscene);
                        confirmRm.show();

                        Label rmLabel = new Label("remove " + newSpellName + ". Are you sure?");
                        rmgrid.add(rmLabel,0,0);
                        Button yesRm = new Button("Yes");
                        Button noRm = new Button("Cancel");
                        HBox hbynrm = new HBox(10);
                        hbynrm.getChildren().addAll(yesRm,noRm);
                        rmgrid.add(hbynrm,0,1);

                        yesRm.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                vbSpells8.getChildren().remove(hbNewSpell);
                                spells8List.remove(newSpell);
                                c.setSpells8(spells8List);
                                confirmRm.close();
                            }
                        });
                        noRm.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                confirmRm.close();
                            }
                        });
                    }
                });
                /////// Info Button ///////
                Stage infoStage = new Stage();
                info.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        SpellInfoPage(infoStage, newSpell);
                    }
                }); // close info button
            }
        }); //close addSpells8btn

        ///////////////////
        ///// LEVEL 9 /////
        ///////////////////

        // for copy paste - spells#, spellslots#, itr#
        Label titleSpellslots9 = new Label("Level 9 Spells");
        titleSpellslots9.setId("spellLevelTitle");

        Button addSpells9Btn = new Button("Add Level 9 Spell");

        ComboBox<Spell> spells9Box = new ComboBox<Spell>();
        spells9Box.setPromptText("add a spell");

        VBox vbSpells9 = new VBox(10);
        ScrollPane spells9Sp = new ScrollPane();
        spells9Sp.setContent(vbSpells9);

        ComboBox<Integer> spellslots9Box = new ComboBox<Integer>(spellSlotsNumbers);
        spellslots9Box.setValue(c.getSpellSlots9());
        spellslots9Box.valueProperty().addListener(new ChangeListener<Integer>() {
            public void changed(ObservableValue ov, Integer oldVal, Integer newVal) {
                c.setSpellSlots9(newVal);
            }
        });
        Button useSpellslots9 = new Button("Use Spell Slot");
        useSpellslots9.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                int currSlot = c.getSpellSlots9();
                if (currSlot != 0) {		          
                    c.setSpellSlots9(c.getSpellSlots9() - 1);
                    spellslots9Box.setValue(spellslots9Box.getValue() - 1);
                }
            }
        });


        for (Spell s : classSpells) {
            if (s.getLevel() == 9) {
                spells9Box.getItems().add(s);
            }
        }

        HashSet<Spell> spells9List = c.getSpells9();
        Iterator<Spell> itr9 = spells9List.iterator();



        ScrollPane spSpells9 = new ScrollPane();
        spSpells9.setContent(vbSpells9);

        while (itr9.hasNext()) {
            Spell nxtItem = itr9.next();
            String spellName = nxtItem.getName();
            Label spellLabel = new Label(spellName);
            HBox hbSpell = new HBox(10);

            Button rm = new Button("remove");
            Button info = new Button("info");
            hbSpell.getChildren().addAll(spellLabel,info,rm);

            vbSpells9.getChildren().add(hbSpell);

            ////// Remove Button ///////
            Stage confirmRm = new Stage();
            rm.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    confirmRm.close();
                    confirmRm.setTitle("Are you sure?");
                    GridPane rmgrid = new GridPane();
                    rmgrid.setAlignment(Pos.CENTER);
                    rmgrid.setHgap(10);
                    rmgrid.setVgap(10);
                    Scene rmscene = new Scene(rmgrid,400,150);
                    confirmRm.setScene(rmscene);
                    confirmRm.show();

                    Label rmLabel = new Label("remove " + spellName + ". Are you sure?");
                    rmgrid.add(rmLabel,0,0);
                    Button yesRm = new Button("Yes");
                    Button noRm = new Button("Cancel");
                    HBox hbynrm = new HBox(10);
                    hbynrm.getChildren().addAll(yesRm,noRm);
                    rmgrid.add(hbynrm,0,1);

                    yesRm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            vbSpells9.getChildren().remove(hbSpell);
                            spells9List.remove(nxtItem);
                            c.setSpells9(spells9List);
                            confirmRm.close();
                        }
                    });
                    noRm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            confirmRm.close();
                        }
                    });
                }
            });
            /////// Info Button ///////
            Stage infoStage = new Stage();
            info.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    SpellInfoPage(infoStage,nxtItem);
                }
            }); // close info button
        } // close while loop

        VBox spells9Root = new VBox(10);
        HBox spells9Hb = new HBox(10);
        HBox spellslots9Hb = new HBox(10);
        spells9Hb.getChildren().addAll(titleSpellslots9,spells9Box, addSpells9Btn);
        spellslots9Hb.getChildren().addAll(spellslots9Box,useSpellslots9);
        spells9Root.getChildren().addAll(spells9Hb,spellslots9Hb,spSpells9);


        ////// add spells ///////
        addSpells9Btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Spell newSpell = spells9Box.getValue();
                spells9List.add(newSpell);
                int newRow = spells9List.size() - 1;
                c.setSpells9(spells9List);

                Label newSpellName = new Label(newSpell.getName());
                Button rm = new Button("remove");
                Button info = new Button("info");
                HBox hbNewSpell = new HBox(10);
                hbNewSpell.getChildren().addAll(newSpellName,info,rm);
                vbSpells9.getChildren().add(hbNewSpell);
                spells9Box.setValue(null);

                ////// Remove Button ///////
                Stage confirmRm = new Stage();
                rm.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        confirmRm.close();
                        confirmRm.setTitle("Are you sure?");
                        GridPane rmgrid = new GridPane();
                        rmgrid.setAlignment(Pos.CENTER);
                        rmgrid.setHgap(10);
                        rmgrid.setVgap(10);
                        Scene rmscene = new Scene(rmgrid,400,150);
                        confirmRm.setScene(rmscene);
                        confirmRm.show();

                        Label rmLabel = new Label("remove " + newSpellName + ". Are you sure?");
                        rmgrid.add(rmLabel,0,0);
                        Button yesRm = new Button("Yes");
                        Button noRm = new Button("Cancel");
                        HBox hbynrm = new HBox(10);
                        hbynrm.getChildren().addAll(yesRm,noRm);
                        rmgrid.add(hbynrm,0,1);

                        yesRm.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                vbSpells9.getChildren().remove(hbNewSpell);
                                spells9List.remove(newSpell);
                                c.setSpells9(spells9List);
                                confirmRm.close();
                            }
                        });
                        noRm.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                confirmRm.close();
                            }
                        });
                    }
                });
                /////// Info Button ///////
                Stage infoStage = new Stage();
                info.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        SpellInfoPage(infoStage, newSpell);
                    }
                }); // close info button
            }
        }); //close addSpells9btn


        ///////////////////////////////////
        ////// Bottom of spells page //////
        ///////////////////////////////////

        VBox botSpellsVb = new VBox(10);
        ComboBox<Spell> allSpellsBox = new ComboBox<Spell>();
        Button addAllSpellsBtn = new Button("Add Spell");
        allSpellsBox.setPromptText("All Spells");

        for (int i = 0; i < spellArr.length; i++) {
            allSpellsBox.getItems().add(spellArr[i]);
        }

        HBox addAllSpellsHb = new HBox();
        addAllSpellsHb.getChildren().addAll(allSpellsBox,addAllSpellsBtn);

        addAllSpellsBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Spell newSpell = allSpellsBox.getValue();
                int lvl = newSpell.getLevel();
                if (lvl == 0) {
                    spells0List.add(newSpell);
                    c.setCantrips(spells0List);
                }
                else if (lvl == 1) {
                    spells1List.add(newSpell);
                    c.setSpells1(spells1List);
                }
                else if (lvl == 2) {
                    spells2List.add(newSpell);
                    c.setSpells2(spells2List);
                }
                else if (lvl == 3) {
                    spells3List.add(newSpell);
                    c.setSpells3(spells3List);
                }
                else if (lvl == 4) {
                    spells4List.add(newSpell);
                    c.setSpells4(spells4List);
                }
                else if (lvl == 5) {
                    spells1List.add(newSpell);
                    c.setSpells5(spells5List);
                }
                else if (lvl == 6) {
                    spells6List.add(newSpell);
                    c.setSpells6(spells6List);
                }
                else if (lvl == 7) {
                    spells7List.add(newSpell);
                    c.setSpells7(spells7List);
                }
                else if (lvl == 8) {
                    spells8List.add(newSpell);
                    c.setSpells8(spells8List);
                }
                else if (lvl == 9) {
                    spells9List.add(newSpell);
                    c.setSpells9(spells9List);
                }
                spellsStage.close();
                spellsBtn.fire();
            }
        });

        Button addCustomSpell = new Button("Add Custom Spell");
        Stage customStage = new Stage();
        addCustomSpell.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                customStage.close();
                GridPane customGrid = new GridPane();
                customGrid.setAlignment(Pos.CENTER);
                customGrid.setHgap(10);
                customGrid.setVgap(10);
                customGrid.setPadding(new Insets(15,15,15,15));
                Scene customScene = new Scene(customGrid);
                customStage.setScene(customScene);

                customScene.getStylesheets().add("lib/SpellDescriptionPage.css");
                Label itemName = new Label("Spell Name:");
                TextField newName = new TextField();

                Label levelLabel = new Label("Level:");
                ObservableList<Integer> levels = FXCollections.observableArrayList();
                for (int i = 0; i< 10; i++) {
                    levels.add(i);
                }
                ComboBox<Integer> levelBox = new ComboBox<Integer>(levels);
                levelBox.setValue(0);

                Label school = new Label("School:");
                TextField customSchool = new TextField();

                Label ritual = new Label("Ritual:");
                ObservableList<String> ritualOptions = FXCollections.observableArrayList();
                ritualOptions.add("yes");
                ritualOptions.add("no");
                ComboBox<String> ritualBox = new ComboBox<String>(ritualOptions);
                ritualBox.setValue("no");

                Label ctLabel = new Label("Casting Time:");
                TextField ct = new TextField();

                Label rangeLabel = new Label("Range:");
                TextField range = new TextField();

                Label compLabel = new Label("Components:");
                TextField comp = new TextField();

                Label durationLabel = new Label("Duration:");
                TextField duration = new TextField();

                Label concentrationLabel = new Label("Concentration:");
                ObservableList<String> concentrationOptions = FXCollections.observableArrayList();
                concentrationOptions.add("yes");
                concentrationOptions.add("no");
                ComboBox<String> concentrationBox = new ComboBox<String>(concentrationOptions);
                concentrationBox.setValue("no");

                Label descLabel = new Label("Description:");
                TextArea desc = new TextArea();
                desc.setEditable(true);
                desc.setWrapText(true);

                customGrid.add(itemName,0,0);
                customGrid.add(newName,1,0);
                customGrid.add(levelLabel,0,1);
                customGrid.add(levelBox,1,1);
                customGrid.add(school,0,2);
                customGrid.add(customSchool,1,2);
                customGrid.add(ritual, 0,3);
                customGrid.add(ritualBox,1,3);
                customGrid.add(ctLabel,0,4);
                customGrid.add(ct,1,4);
                customGrid.add(rangeLabel,0,5);
                customGrid.add(range,1,5);
                customGrid.add(compLabel,0,6);
                customGrid.add(comp,1,6);
                customGrid.add(durationLabel,0,7);
                customGrid.add(duration,1,7);
                customGrid.add(concentrationLabel,0,8);
                customGrid.add(concentrationBox,1,8);

                customGrid.add(descLabel,0,9);
                customGrid.add(desc,1,9);

                Button done = new Button("Add Spell");
                Button cancel = new Button("Cancel");
                HBox customBtnsHb = new HBox(10);
                customBtnsHb.getChildren().addAll(done,cancel);
                customGrid.add(customBtnsHb,0,10);

                customStage.show();

                done.setOnAction(new EventHandler<ActionEvent>() {
                    Text errMsg = new Text("The spell needs a name!");
                    @Override
                    public void handle(ActionEvent e) {
                        if ( newName.getText().isEmpty()) {
                            errMsg.setFill(Color.FIREBRICK);
                            customGrid.getChildren().remove(errMsg);
                            customGrid.add(errMsg,1,10);
                        }
                        else {
                            Spell customSpell = new Spell();

                            customSpell.setName(newName.getText());
                            customSpell.setRange(range.getText());
                            customSpell.setRitual(ritualBox.getValue());
                            customSpell.setDuration(duration.getText());
                            customSpell.setCastingTime(ct.getText());
                            customSpell.setLevel(levelBox.getValue());
                            customSpell.setSchool(customSchool.getText());
                            customSpell.setConcentration(concentrationBox.getValue());

                            ArrayList<String> customDesc = new ArrayList<String>();
                            customDesc.add(desc.getText());
                            customSpell.setDesc(customDesc);

                            ArrayList<String> customComp = new ArrayList<String>();
                            customComp.add(comp.getText());
                            customSpell.setComponents(customComp);

                            int lvl = levelBox.getValue();
                            if (lvl == 0) {
                                spells0List.add(customSpell);
                                c.setCantrips(spells0List);
                            }
                            else if (lvl == 1) {
                                spells1List.add(customSpell);
                                c.setSpells1(spells1List);
                            }
                            else if (lvl == 2) {
                                spells2List.add(customSpell);
                                c.setSpells2(spells2List);
                            }
                            else if (lvl == 3) {
                                spells3List.add(customSpell);
                                c.setSpells3(spells3List);
                            }
                            else if (lvl == 4) {
                                spells4List.add(customSpell);
                                c.setSpells4(spells4List);
                            }
                            else if (lvl == 5) {
                                spells1List.add(customSpell);
                                c.setSpells5(spells5List);
                            }
                            else if (lvl == 6) {
                                spells6List.add(customSpell);
                                c.setSpells6(spells6List);
                            }
                            else if (lvl == 7) {
                                spells7List.add(customSpell);
                                c.setSpells7(spells7List);
                            }
                            else if (lvl == 8) {
                                spells8List.add(customSpell);
                                c.setSpells8(spells8List);
                            }
                            else if (lvl == 9) {
                                spells9List.add(customSpell);
                                c.setSpells9(spells9List);
                            }

                            customStage.close();
                            spellsStage.close();
                            spellsBtn.fire();
                        }
                    }
                }); // close done spell

                cancel.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e) {
                        customStage.close();
                    }
                });

            }
        }); // close add custom spell button


        Button doneSpells = new Button("Done");
        doneSpells.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                spellsStage.close();
            }
        });

        botSpellsVb.getChildren().addAll(addAllSpellsHb,addCustomSpell,doneSpells);
        /////////////////////////////
        ///// Spells page setup /////
        /////////////////////////////
        BorderPane spellsBp = new BorderPane();
        spellsBp.setPadding(new Insets(20));
        spellsBp.setMargin(spellsTitle,new Insets(12,12,12,12));
        spellsBp.setMargin(botSpellsVb,new Insets(12,12,12,12));
        spellsBp.setTop(spellsTitle);
        spellsBp.setCenter(rootVb);
        spellsBp.setBottom(botSpellsVb);

        ScrollPane spellsSp = new ScrollPane();
        spellsSp.setContent(spellsBp);

        Scene spellsscene = new Scene(spellsSp,500,800);

        spellsStage.setScene(spellsscene);
        spellsscene.getStylesheets().add("lib/SpellsPage.css");

        rootVb.getChildren().addAll(spells0Root,spells1Root,spells2Root,spells3Root,spells4Root,spells5Root,spells6Root,spells7Root,spells8Root,spells9Root);
        spellsStage.show();


    }

    public void SpellInfoPage(Stage infoStage, Spell spell) {
        infoStage.close();
        GridPane infoGrid = new GridPane();
        infoGrid.setAlignment(Pos.CENTER);
        infoGrid.setHgap(10);
        infoGrid.setVgap(10);
        infoGrid.setPadding(new Insets(15,15,15,15));
        Scene infoScene = new Scene(infoGrid);
        infoStage.setScene(infoScene);

        infoScene.getStylesheets().add("lib/SpellDescriptionPage.css");
        Label itemName = new Label(spell.getName());
        itemName.setId("spellName");

        Label lvlSchool = new Label();
        if (spell.getRitualTruth()) {
            lvlSchool.setText("level " + Integer.toString(spell.getLevel()) + " " + spell.getSchool() + " (ritual)");
        }
        else {
            lvlSchool.setText("level " + Integer.toString(spell.getLevel()) + " " + spell.getSchool());
        }

        Label ctLabel = new Label("Casting Time:");
        Text ct = new Text(spell.getCastingTime());

        Label rangeLabel = new Label("Range:");
        Text range = new Text(spell.getRange());

        Label compLabel = new Label("Components:");
        Text comp = new Text();
        for (int i = 0; i < spell.getComponents().size(); i++) {
            if (i == 0) {
                comp.setText(spell.getComponents().get(i));
            }
            else if (i == spell.getComponents().size() - 1 && spell.getComponents().get(i).length() > 1) {
                comp.setText(comp.getText() + " (" + spell.getComponents().get(i) + ")");
            }
            else {
                comp.setText(comp.getText() + " " + spell.getComponents().get(i));
            }
        }

        Label durationLabel = new Label("Duration:");
        Text duration = new Text();
        if (spell.getConcentrationTruth()) {
            duration.setText("Concentration, " + spell.getDuration());
        }
        else {
            duration.setText(spell.getDuration());
        }

        TextArea desc = new TextArea();
        desc.setEditable(false);
        desc.setWrapText(true);
        for (int i = 0; i < spell.getDesc().size(); i++) {
            if (i == 0) {
                desc.setText(spell.getDesc().get(i));
            }
            else {
                desc.setText(desc.getText() + "\n" + spell.getDesc().get(i));
            }
        }

        infoGrid.add(itemName,0,0);
        infoGrid.add(lvlSchool,0,1);
        infoGrid.add(ctLabel,0,2);
        infoGrid.add(ct,1,2);
        infoGrid.add(rangeLabel,0,3);
        infoGrid.add(range,1,3);
        infoGrid.add(compLabel,0,4);
        infoGrid.add(comp,1,4);
        infoGrid.add(durationLabel,0,5);
        infoGrid.add(duration,1,5);
        infoGrid.add(desc,0,6);

        Button done = new Button("OK");
        infoGrid.add(done,0,7);

        infoStage.show();

        done.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                infoStage.close();
            }
        });

    }

    //Load a previously made character file
    public static String LoadCharacterFile(Stage primaryStage) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open Character Sheet");
        File initDir = new File("."); //Start in current directory
        fc.setInitialDirectory(initDir);


        //show only json files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("D&D files (*.dnd)","*.dnd");
        fc.getExtensionFilters().add(extFilter);
        File file = fc.showOpenDialog(primaryStage);
        if (file != null) {
            String filePath = file.getPath();
            return filePath;
        }
        else {
            return "";
        }
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        }
        catch (NumberFormatException ex) {
            return false;
        }
    }

    public static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        }
        catch (NumberFormatException ex) {
            return false;
        }
    }

    public static int calcMod(int stat) {
        double weightedVal = (stat - 10.0) / 2.0;
        int returnVal = (int) Math.floor(weightedVal);
        return returnVal;
    }
    
    public static String saveCharacterAs(Stage stage) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Save Character As");
        File initDir = new File(".");
        fc.setInitialDirectory(initDir);
        //FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)","*.json");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("D&D files (*.dnd)","*.dnd");

        fc.getExtensionFilters().add(extFilter);
        File file = fc.showSaveDialog(stage);
        if (file != null) {
            String filePath = file.getPath();
            return filePath;
        }
        else {
            return "";
        }
    }
    
}
