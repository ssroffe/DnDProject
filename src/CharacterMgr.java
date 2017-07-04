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
    //start the first page - new or load character
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
        scene1.getStylesheets().add(this.getClass().getResource("NewLoadScreen.css").toExternalForm());
        primaryStage.setScene(scene1);


        Text welcome = new Text("Welcome to DnD!");
        //grid.add(welcome, 0,0,3,1);
        //grid.add(welcome,0,0);
        welcome.setId("welcome");

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
                    Character c = Character.LoadCharacter(fileName);
                    CharacterSheet(c);
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
                    Character c = new Character(nameTf.getText());
                    stage.close();
                    CharacterSheet(c);
                }
                else {
                    Character c = new Character(nameTf.getText(),clssTf.getValue().toString());
                    stage.close();
                    CharacterSheet(c);
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
        

        


    public void CharacterSheet(Character c) {

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

        Scene scene = new Scene(scroll,600,800);
        
        scene.getStylesheets().add(this.getClass().getResource("CharacterSheet.css").toExternalForm());

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

        addLevel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                c.setLevel(c.getLevel() + 1);
                level.setText(Integer.toString(c.getLevel()));
            }
        });
 
        lowLevel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                c.setLevel(c.getLevel() - 1);
                level.setText(Integer.toString(c.getLevel()));
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
        clssTf.setId("locked-tf");
        clssTf.setEditable(true);
        clssTf.setValue(c.getClss());
        grid.add(clssTf,1,row);

        clssTf.valueProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue ov, String oldVal, String newVal) {
                c.setClss(newVal.toString());
            }
        });

        /*
        Button testbtn = new Button("test");
        grid.add(testbtn,3,row);
        testbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println(c.getClss());
            }
        });
        */
	row++;

        ///////////// Race //////////////
        Label raceLabel = new Label("Race:");
        grid.add(raceLabel,0,row);
        TextField raceTf = new TextField();
        raceTf.setId("locked-tf");
        raceTf.setText(c.getRace());
        raceTf.setEditable(false);
        grid.add(raceTf,1,row);

        ToggleButton racebtn = new ToggleButton("edit");
        racebtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (racebtn.isSelected()) {
                    raceTf.setEditable(true);
                    raceTf.setId("unlocked-tf");
                }
                else {
                    raceTf.setEditable(false);
                    raceTf.setId("locked-tf");
                    c.setRace(raceTf.getText());
                }
            }
        });
        grid.add(racebtn,2,row);
	row++;

        ///////////// SubRace //////////////
        Label subRaceLabel = new Label("SubRace:");
        grid.add(subRaceLabel,0,row);
        TextField subRaceTf = new TextField();
        subRaceTf.setId("locked-tf");
        subRaceTf.setText(c.getSubrace());
        subRaceTf.setEditable(false);
        grid.add(subRaceTf,1,row);

        ToggleButton subRacebtn = new ToggleButton("edit");
        subRacebtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (subRacebtn.isSelected()) {
                    subRaceTf.setEditable(true);
                    subRaceTf.setId("unlocked-tf");
                }
                else {
                    subRaceTf.setEditable(false);
                    subRaceTf.setId("locked-tf");
                    c.setSubrace(subRaceTf.getText());
                }
            }
        });
        grid.add(subRacebtn,2,row);

        row++;

        ///////////// Background //////////////
        Label backgroundLabel = new Label("Background:");
        grid.add(backgroundLabel,0,row);
        TextField backgroundTf = new TextField();
        backgroundTf.setId("locked-tf");
        backgroundTf.setText(c.getBackground());
        backgroundTf.setEditable(false);
        grid.add(backgroundTf,1,row);

        ToggleButton backgroundbtn = new ToggleButton("edit");
        backgroundbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (backgroundbtn.isSelected()) {
                    backgroundTf.setEditable(true);
                    backgroundTf.setId("unlocked-tf");
                }
                else {
                    backgroundTf.setEditable(false);
                    backgroundTf.setId("locked-tf");
                    c.setBackground(backgroundTf.getText());
                }
            }
        });
        grid.add(backgroundbtn,2,row);
	row++;

    stage.show();

    ///////////// Gold /////////////////
    Label goldLabel = new Label("Gold:");
    grid.add(goldLabel,0,row);
    Label gold = new Label(Double.toString(c.getGold()));
    grid.add(gold,1,row);
    gold.setId("gold");

    row++;

    ////////// ADD/LOSE GOLD ///////////
    TextField pmGoldTf = new TextField();
    pmGoldTf.setPromptText("Add or Subtract Gold");
    grid.add(pmGoldTf,1,row);

    Button plusGold = new Button("Add Gold");
    Button minusGold = new Button("Spend Gold");
    HBox hbpmGold = new HBox(10);
    hbpmGold.getChildren().addAll(plusGold,minusGold);
    grid.add(hbpmGold,2,row);

    final int addGoldRow = row;

    plusGold.setOnAction(new EventHandler<ActionEvent>() {
        Text errMsg = new Text("Inputted Value was not a number!");
        @Override
        public void handle(ActionEvent e) {
            boolean isDbl = isDouble(pmGoldTf.getText());
            if (isDbl) {
                double addGoldVal = Double.parseDouble(pmGoldTf.getText()) + c.getGold();
                c.setGold(addGoldVal);
                gold.setText(Double.toString(c.getGold()));
                grid.getChildren().remove(errMsg);
            }
            else {
                errMsg.setFill(Color.FIREBRICK);
                grid.add(errMsg,3,addGoldRow);
            }

        }
    });

    minusGold.setOnAction(new EventHandler<ActionEvent>() {
        Text errMsg = new Text("Inputted Value was not a number!");
        @Override
        public void handle(ActionEvent e) {
            boolean isDbl = isDouble(pmGoldTf.getText());
            if (isDbl) {
                double loseGoldVal = c.getGold() - Double.parseDouble(pmGoldTf.getText());
                if (loseGoldVal > 0) {
                    c.setGold(loseGoldVal);
                    gold.setText(Double.toString(c.getGold()));
                }
                else {
                    c.setGold(0);
                    gold.setText(Double.toString(c.getGold()));
                }
                grid.getChildren().remove(errMsg);
            }
            else {
                errMsg.setFill(Color.FIREBRICK);
                grid.add(errMsg,3,addGoldRow);
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

    TextField profTf = new TextField();
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
                   c.setInitiative(Integer.parseInt(initTf.getText()));
                   initTf.setText(fmt.format(c.getInitiative()));
                   c.setSpeed(Integer.parseInt(speedTf.getText()));
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
            if (fileName.isEmpty()) {
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

    skillsBtn.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            Stage skillsStage = new Stage();
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
        skillsscene.getStylesheets().add(this.getClass().getResource("ListPage.css").toExternalForm());

        skillsStage.setScene(skillsscene);

        doneSkills.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                skillsStage.close();
            }
        });

        skillsStage.show();

        }
    });

    ///////////////////////////////
    ///////// SAVINGTHROWS PAGE /////////
    ///////////////////////////////

    savingThrowsBtn.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            Stage savingThrowsStage = new Stage();
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
        savingThrowsscene.getStylesheets().add(this.getClass().getResource("ListPage.css").toExternalForm());

        savingThrowsStage.setScene(savingThrowsscene);

        doneSavingThrows.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                savingThrowsStage.close();
            }
        });

        savingThrowsStage.show();

        }
    });
    //////////////////////////////////////
    ////////// Inventory Page ////////////
    //////////////////////////////////////

    inventoryBtn.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            Stage inventoryStage = new Stage();
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
                rm.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        Stage confirmRm = new Stage();
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
                                vbInventory.getChildren().remove(hbInventoryList);
                                inventoryList.remove(inventorylabel.getText());
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

                info.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        Stage infoStage = new Stage();
                        GridPane infoGrid = new GridPane();
                        infoGrid.setAlignment(Pos.CENTER);
                        infoGrid.setHgap(10);
                        infoGrid.setVgap(10);
                        Scene infoScene = new Scene(infoGrid);
                        infoStage.setScene(infoScene);

                        infoScene.getStylesheets().add(this.getClass().getResource("TextAreaPage.css").toExternalForm());
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
                                Text msg = new Text("Save");
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
        inventoryscene.getStylesheets().add(this.getClass().getResource("ListPage.css").toExternalForm());

        inventoryStage.setScene(inventoryscene);

        //////// Add Inventory //////////
        addInventory.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                inventoryList.add(addInventoryTf.getText());
                int newRow = inventoryList.size() - 1;
                c.setInventory(inventoryList);

                Label newInventory = new Label(addInventoryTf.getText());
                String newItem = newInventory.getText();

                TextArea description = new TextArea();
                description.setPromptText("Item Description");

                Button rm = new Button("remove");
                Button info = new Button("info");
                HBox hbNewInventory = new HBox(10);
                hbNewInventory.getChildren().addAll(newInventory,info,rm);
                vbInventory.getChildren().add(hbNewInventory);
                addInventoryTf.clear();

                ////// Remove button ///////
                rm.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        Stage confirmRm = new Stage();
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

                info.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {

                        Stage infoStage = new Stage();
                        GridPane infoGrid = new GridPane();
                        infoGrid.setAlignment(Pos.CENTER);
                        infoGrid.setHgap(10);
                        infoGrid.setVgap(10);
                        Scene infoScene = new Scene(infoGrid);
                        infoStage.setScene(infoScene);

                        infoScene.getStylesheets().add(this.getClass().getResource("TextAreaPage.css").toExternalForm());

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
    });

    //////////////////////////////////
    ///////// LANGUAGES PAGE /////////
    //////////////////////////////////

    languagesBtn.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            Stage languagesStage = new Stage();
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
                rm.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        Stage confirmRm = new Stage();
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
        languagesscene.getStylesheets().add(this.getClass().getResource("ListPage.css").toExternalForm());

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
                    rm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            Stage confirmRm = new Stage();
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
    });

    ///////////////////////////////
    //////// WEAPONS PAGE /////////
    ///////////////////////////////

    weaponsBtn.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            Stage weaponsStage = new Stage();
            weaponsStage.setTitle("Weapons Page");

            Label weaponsTitle = new Label("Weapons");
            weaponsTitle.setId("title");
            
            VBox weaponsHeader = new VBox(10);
            
            VBox vbWeapons = new VBox(10);

            ArrayList<String> weaponsList = c.getWeapons();
            Iterator<String> itr = weaponsList.iterator();

            HBox labelsHb = new HBox(10);
            Label proficient = new Label("Proficient");
            Label weaponName = new Label("Weapon");
            Label damageLabel = new Label("Damage");
            Label hitLabel = new Label("Modifier");
            labelsHb.getChildren().addAll(proficient,hitLabel,weaponName,damageLabel);
            
            weaponsHeader.getChildren().addAll(weaponsTitle,labelsHb);
            
            int index = 0;

            while (itr.hasNext()) {
                final int currIndex = index;
                String nxtItem = itr.next();
                String[] splitItem = nxtItem.split("--");

                CheckBox isProficient = new CheckBox();
                isProficient.setAllowIndeterminate(false);

                TextField weaponsTf = new TextField(splitItem[0]);
                weaponsTf.setEditable(false);
                weaponsTf.setId("locked-tf");

                TextField damage = new TextField();
                damage.setEditable(false);
                damage.setId("locked-tf");

                Label damageMod = new Label();
                ToggleButton editBtn = new ToggleButton("edit");

                Label hitMod = new Label();

                try {
                    damage.setText(splitItem[1]);
                    isProficient.setSelected(Boolean.parseBoolean(splitItem[2]));
                }
                catch (ArrayIndexOutOfBoundsException ioe) {
                    damage.setText("");
                    isProficient.setSelected(false);
                }    


                if (isProficient.isSelected()) {
                    hitMod.setText(fmt.format(calcMod(c.getStr()) + c.getProficiencyBonus()));
                }
                else {
                    hitMod.setText(fmt.format(calcMod(c.getStr())));

                }

                damageMod.setText(fmt.format(calcMod(c.getStr())));

                HBox hbWeaponsList = new HBox(10);

                Button rm = new Button("remove");
                hbWeaponsList.getChildren().addAll(isProficient,hitMod,weaponsTf,damage,damageMod,rm,editBtn);

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
                            weaponsList.set(currIndex,weaponsTf.getText() + "--" + damage.getText() + "--" + Boolean.toString(isProficient.isSelected()));
                            c.setWeapons(weaponsList);
                        }
                    }
                });

                isProficient.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    public void changed(ObservableValue<? extends Boolean> ov, Boolean oldVal, Boolean newVal) {
                        
                        weaponsList.set(currIndex,weaponsTf.getText() + "--" + damage.getText() + "--" + Boolean.toString(newVal));
                        c.setWeapons(weaponsList);
                        if (newVal) {
                            hitMod.setText(fmt.format(calcMod(c.getStr()) + c.getProficiencyBonus()));
                        }
                        else {
                            hitMod.setText(fmt.format(calcMod(c.getStr())));
                        }

                    }
                });

                ////// Remove button ///////
                rm.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        Stage confirmRm = new Stage();
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
        weaponsscene.getStylesheets().add(this.getClass().getResource("ListPage.css").toExternalForm());

        weaponsStage.setScene(weaponsscene);

        //////// Add Weapons //////////
        addWeapons.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                weaponsList.add(addWeaponsTf.getText());
                int newRow = weaponsList.size() - 1;
                c.setWeapons(weaponsList);

                TextField newWeapons = new TextField(addWeaponsTf.getText());
                newWeapons.setEditable(false);
                newWeapons.setId("locked-tf");

                CheckBox isProficient = new CheckBox();
                Button rm = new Button("remove");

                TextField damage = new TextField();
                damage.setEditable(false);
                damage.setId("locked-tf");
                
                ToggleButton editBtn = new ToggleButton("edit");

                Label damageMod = new Label();
                damageMod.setText(fmt.format(calcMod(c.getStr())));

                Label hitMod = new Label();
                hitMod.setText(fmt.format(calcMod(c.getStr())));
                
                addWeaponsTf.clear();

                HBox hbWeaponsList = new HBox(10);

                hbWeaponsList.getChildren().addAll(isProficient,hitMod,newWeapons,damage,damageMod,rm,editBtn);
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
                            weaponsList.set(newRow,newWeapons.getText() + "--" + damage.getText() + "--" + Boolean.toString(isProficient.isSelected()));
                            c.setWeapons(weaponsList);
                        }
                    }
                });

                isProficient.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    public void changed(ObservableValue<? extends Boolean> ov, Boolean oldVal, Boolean newVal) {
                        
                        weaponsList.set(newRow,newWeapons.getText() + "--" + damage.getText() + "--" + Boolean.toString(newVal));
                        c.setWeapons(weaponsList);
                        if (newVal) {
                            hitMod.setText(fmt.format(calcMod(c.getStr()) + c.getProficiencyBonus()));
                        }
                        else {
                            hitMod.setText(fmt.format(calcMod(c.getStr())));
                        }

                    }
                });

                ////// Remove button ///////
                rm.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        Stage confirmRm = new Stage();
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
    });




    //////////////////////////////////
    ///////// IDEALS PAGE ////////////
    //////////////////////////////////

    idealsBtn.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            Stage idealsStage = new Stage();
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
                rm.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        Stage confirmRm = new Stage();
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
        idealsscene.getStylesheets().add(this.getClass().getResource("ListPage.css").toExternalForm());

        idealsStage.setScene(idealsscene);

        //////// Add Ideals //////////
        addIdeals.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
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
                rm.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        Stage confirmRm = new Stage();
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
    });

    //////////////////////////////////
    ///////// FLAWS PAGE /////////////
    //////////////////////////////////

    flawsBtn.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            Stage flawsStage = new Stage();
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
                rm.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        Stage confirmRm = new Stage();
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
        flawsscene.getStylesheets().add(this.getClass().getResource("ListPage.css").toExternalForm());

        flawsStage.setScene(flawsscene);

        //////// Add Flaws //////////
        addFlaws.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
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
                rm.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        Stage confirmRm = new Stage();
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
    });

    //////////////////////////////////
    ///////// BONDS PAGE /////////////
    //////////////////////////////////

    bondsBtn.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            Stage bondsStage = new Stage();
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
                rm.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        Stage confirmRm = new Stage();
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
        bondsscene.getStylesheets().add(this.getClass().getResource("ListPage.css").toExternalForm());

        bondsStage.setScene(bondsscene);

        //////// Add Bonds //////////
        addBonds.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
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
                rm.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        Stage confirmRm = new Stage();
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
    });
    
    /////////////////////////////////////
    ///////// FEATURES PAGE /////////////
    /////////////////////////////////////

    featuresBtn.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            Stage featuresStage = new Stage();
            featuresStage.setTitle("Features Page");



            Label featuresTitle = new Label("Features");
            featuresTitle.setId("title");
            VBox vbFeatures = new VBox(10);

            HashSet<String> featuresList = c.getFeatures();
            Iterator<String> itr = featuresList.iterator();

            while (itr.hasNext()) {
                String nxtItem = itr.next();
                Label featureslabel = new Label(nxtItem);
                HBox hbFeaturesList = new HBox(10);

                Button rm = new Button("remove");
                hbFeaturesList.getChildren().addAll(featureslabel,rm);

                vbFeatures.getChildren().add(hbFeaturesList);

                ////// Remove button ///////
                rm.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        Stage confirmRm = new Stage();
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
                                vbFeatures.getChildren().remove(hbFeaturesList);
                                featuresList.remove(featureslabel.getText());
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
        featuresscene.getStylesheets().add(this.getClass().getResource("ListPage.css").toExternalForm());

        featuresStage.setScene(featuresscene);

        //////// Add Features //////////
        addFeatures.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                featuresList.add(addFeaturesTf.getText());
                int newRow = featuresList.size() - 1;
                c.setFeatures(featuresList);
                Label newFeatures = new Label(addFeaturesTf.getText());
                Button rm = new Button("remove");
                HBox hbnewFeatures = new HBox(10);
                hbnewFeatures.getChildren().addAll(newFeatures,rm);
                vbFeatures.getChildren().add(hbnewFeatures);
                addFeaturesTf.clear();

                ////// Remove button ///////
                rm.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        Stage confirmRm = new Stage();
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
    });
    
    ////////////////////////////////////
    ////////////// NOTES ///////////////
    ////////////////////////////////////
    
    notesBtn.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            Stage notesStage = new Stage();
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
            notesscene.getStylesheets().add(this.getClass().getResource("TextAreaPage.css").toExternalForm());

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
    });
    
    //////////////////////////////////////////
    ////////////// DESCRIPTION ///////////////
    //////////////////////////////////////////
    
    descriptionBtn.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            Stage descriptionStage = new Stage();
            descriptionStage.setTitle("Description Page");
	    
	    
	    
            Label descriptionTitle = new Label("Description");
            descriptionTitle.setId("title");
            VBox vbDescription = new VBox(10);
	    
            String descriptionList = c.getDescription();
	    
            TextArea description = new TextArea(c.getDescription());
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
            descriptionscene.getStylesheets().add(this.getClass().getResource("TextAreaPage.css").toExternalForm());

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
    });


    ////////////////////////////////////
    /////////// SPELLS PAGE ////////////
    ////////////////////////////////////
    
    spellsBtn.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            Stage spellsStage = new Stage();
            spellsStage.setTitle("Spells Page");

            Label spellsTitle = new Label("Spells");
            spellsTitle.setId("title");


            VBox rootVb = new VBox(10);



            // Load the Spells
            Gson gson = new Gson();
            Spell[] spellArr;
            
            try (Reader reader = new FileReader("spells.json")) {
                spellArr = gson.fromJson(reader,Spell[].class);
            }
            catch (IOException ioe) {
                ioe.printStackTrace();
                spellArr = new Spell[0];
            }

            // Get only spells associated with your class
            ArrayList<Spell> classSpells = new ArrayList<Spell>();

            boolean inClassOptions = classes.contains(c.getClss());

            for (int i = 0; i < spellArr.length; i++) {
                String currclss = c.getClss();
                if (spellArr[i].getClss().contains(currclss) && inClassOptions) {
                    classSpells.add(spellArr[i]);
                }
                else if (!inClassOptions) {
                    classSpells.add(spellArr[i]);
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

            for (int i = 0; i < classSpells.size(); i++) {
                if (classSpells.get(i).getLevel() == 0) {
                    spellBox0.getItems().add(classSpells.get(i));
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
                rm.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        Stage confirmRm = new Stage();
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
                info.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        Stage infoStage = new Stage();
                        GridPane infoGrid = new GridPane();
                        infoGrid.setAlignment(Pos.CENTER);
                        infoGrid.setHgap(10);
                        infoGrid.setVgap(10);
                        infoGrid.setPadding(new Insets(15,15,15,15));
                        Scene infoScene = new Scene(infoGrid);
                        infoStage.setScene(infoScene);

                        infoScene.getStylesheets().add(this.getClass().getResource("SpellDescriptionPage.css").toExternalForm());
                        Label itemName = new Label(nxtItem.getName());
                        itemName.setId("spellName");

                        Label lvlSchool = new Label();
                        if (nxtItem.getRitualTruth()) {
                            lvlSchool.setText("level " + Integer.toString(nxtItem.getLevel()) + " " + nxtItem.getSchool() + " (ritual)");
                        }
                        else {
                            lvlSchool.setText("level " + Integer.toString(nxtItem.getLevel()) + " " + nxtItem.getSchool());
                        }

                        Label ctLabel = new Label("Casting Time:");
                        Text ct = new Text(nxtItem.getCastingTime());

                        Label rangeLabel = new Label("Range:");
                        Text range = new Text(nxtItem.getRange());
                        
                        Label compLabel = new Label("Components:");
                        Text comp = new Text();
                        for (int i = 0; i < nxtItem.getComponents().size(); i++) {
                            if (i == 0) {
                                comp.setText(nxtItem.getComponents().get(i));
                            }
                            else if (i == nxtItem.getComponents().size() - 1) {
                                comp.setText(comp.getText() + " (" + nxtItem.getComponents().get(i) + ")");
                            }
                            else {
                                comp.setText(comp.getText() + " " + nxtItem.getComponents().get(i));
                            }
                        }

                        Label durationLabel = new Label("Duration:");
                        Text duration = new Text();
                        if (nxtItem.getConcentrationTruth()) {
                            duration.setText("Concentration, " + nxtItem.getDuration());
                        }
                        else {
                            duration.setText(nxtItem.getDuration());
                        }

                        TextArea desc = new TextArea();
                        desc.setEditable(false);
                        desc.setWrapText(true);
                        for (int i = 0; i < nxtItem.getDesc().size(); i++) {
                            if (i == 0) {
                                desc.setText(nxtItem.getDesc().get(i));
                            }
                            else {
                                desc.setText(desc.getText() + "\n" + nxtItem.getDesc().get(i));
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
                    rm.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            Stage confirmRm = new Stage();
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
                    info.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            Stage infoStage = new Stage();
                            GridPane infoGrid = new GridPane();
                            infoGrid.setAlignment(Pos.CENTER);
                            infoGrid.setHgap(10);
                            infoGrid.setPadding(new Insets(15,15,15,15));
                            infoGrid.setVgap(10);
                            Scene infoScene = new Scene(infoGrid);
                            infoStage.setScene(infoScene);

                            infoScene.getStylesheets().add(this.getClass().getResource("SpellDescriptionPage.css").toExternalForm());
                            Label itemName = new Label(newSpell.getName());
                            itemName.setId("spellName");

                            Label lvlSchool = new Label();
                            if (newSpell.getRitualTruth()) {
                                lvlSchool.setText("level " + Integer.toString(newSpell.getLevel()) + " " + newSpell.getSchool() + " (ritual)");
                            }
                            else {
                                lvlSchool.setText("level " + Integer.toString(newSpell.getLevel()) + " " + newSpell.getSchool());
                            }

                            Label ctLabel = new Label("Casting Time:");
                            Text ct = new Text(newSpell.getCastingTime());

                            Label rangeLabel = new Label("Range:");
                            Text range = new Text(newSpell.getRange());

                            Label compLabel = new Label("Components:");
                            Text comp = new Text();
                            for (int i = 0; i < newSpell.getComponents().size(); i++) {
                                if (i == 0) {
                                    comp.setText(newSpell.getComponents().get(i));
                                }
                                else if (i == newSpell.getComponents().size() - 1) {
                                    comp.setText(comp.getText() + " (" + newSpell.getComponents().get(i) + ")");
                                }
                                else {
                                    comp.setText(comp.getText() + " " + newSpell.getComponents().get(i));
                                }
                            }

                            Label durationLabel = new Label("Duration:");
                            Text duration = new Text();
                            if (newSpell.getConcentrationTruth()) {
                                duration.setText("Concentration, " + newSpell.getDuration());
                            }
                            else {
                                duration.setText(newSpell.getDuration());
                            }

                            TextArea desc = new TextArea();
                            desc.setEditable(false);
                            desc.setWrapText(true);
                            for (int i = 0; i < newSpell.getDesc().size(); i++) {
                                if (i == 0) {
                                    desc.setText(newSpell.getDesc().get(i));
                                }
                                else {
                                    desc.setText(desc.getText() + "\n" + newSpell.getDesc().get(i));
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
                            infoGrid.add(desc,0,6,2,1);

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
                    }); // close info button
                }
            }); //close addSpells0btn


            ///////////////////
            ///// LEVEL 1 /////
            ///////////////////
            
            

            ///////////////////
            ///// LEVEL 2 /////
            ///////////////////




            /////////////////////////////
            ///// Spells page setup /////
            /////////////////////////////
            BorderPane spellsBp = new BorderPane();
            spellsBp.setPadding(new Insets(20));
            spellsBp.setMargin(spellsTitle,new Insets(12,12,12,12));
            spellsBp.setTop(spellsTitle);
            spellsBp.setCenter(rootVb);

            ScrollPane spellsSp = new ScrollPane();
            spellsSp.setContent(spellsBp);
            
            Scene spellsscene = new Scene(spellsSp);

            spellsStage.setScene(spellsscene);
            
            rootVb.getChildren().addAll(spells0Root);
            spellsStage.show();                 

            

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
    
    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
        public void handle(WindowEvent we) {
            we.consume();
            Stage saveExitStage = new Stage();
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
                }
            });

            noSE.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    saveExitStage.close();
                    stage.close();
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
