import java.util.Stack;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all6 the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class Game 
{
    // Alunos: Bruno Saint Clair Silva Oliveira e Joao Victor Oliveira Moura

    private Parser parser;
    private Room currentRoom;
    private Stack<Room> roomHistory;

        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
        roomHistory = new Stack<>();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room center, foodPark, movieTheather, bank, store, parkingLot, exit;
      
        // create the rooms
        center = new Room("in the middle of the mall");
        foodPark = new Room("in the food park");
        movieTheather = new Room("in the movie theather");
        bank = new Room("in the bank");
        store = new Room("in a store");
        parkingLot = new Room("in the parking lot");
        exit = new Room("in the exit! ");

        // initialise room exits
        center.setExit("north", foodPark);
        center.setExit("east", store);
        center.setExit("south", bank);
        center.setExit("west", movieTheather);
        foodPark.setExit("east",parkingLot);
        foodPark.setExit("south",center);
        movieTheather.setExit("east", center);
        bank.setExit("north", center);
        store.setExit("north", parkingLot);
        store.setExit("west",center);
        parkingLot.setExit("north", exit);
        parkingLot.setExit("south", store);
        parkingLot.setExit("west", foodPark);
        exit.setExit("south", parkingLot);

        foodPark.addItem("Mug","you can use it to drink water",1.5);
        movieTheather.addItem("shirt","you can wear it to keep warm",0.8);
        store.addItem("knife","you can use it to defend yourself",2);
        parkingLot.addItem("Water","you can put it in the mug",0.5);


        currentRoom = center;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play()
    {
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */

    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        printLocationInfo();
    }


    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if(commandWord.equals("help")) {
            printHelp();
        }
        else if(commandWord.equals("go")) {
            goRoom(command);
        }
        else if(commandWord.equals("look")) {
            look();
        }
        else if(commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if(commandWord.equals("back")) {
            if (!command.hasSecondWord()) {
                goBack();
                }
            else {
                    System.out.println("Voltar para?");
                }
        }

        return wantToQuit;
    }

    private void goBack() {
        if (!roomHistory.isEmpty()) {
            currentRoom = roomHistory.pop();
            System.out.println(currentRoom.getLongDescription());
        } else {
            System.out.println("Você não pode voltar.");
        }
    }
    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println(parser.showCommands());

    }

    /** 
     * Try to go in one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    private void goRoom(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();
        roomHistory.push(currentRoom);

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {

            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
            currentRoom.printItems();
        }
    }
    private void printLocationInfo(){

        System.out.println(currentRoom.getLongDescription());
        currentRoom.printItems();
    }
    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }

    private void look()
    {
        System.out.println(currentRoom.getLongDescription());
    }

}
