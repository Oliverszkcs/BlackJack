import java.io.*;
import java.util.ArrayList;
/**
 * A Player osztály egy játékost reprezentál a Blackjack játékban.
 */
public class Player implements Serializable {
    /**
     * A játékos egyenlegét tároló egész szám.
     */
    private int balance;
    /**
     * A játékos nevét tároló karakterlánc.
     */
    private final String name;
    /**
     * Azt jelzi hogy hány kézzel indul a játékos.
     */
    private final int numberofhands;
    /**
     * A játékos kezeinek listája.
     */
    private ArrayList<Hand> hands;
    /**
     * Azt jelzi, hogy adott játékos hányadik kezénél járunk.
     */
    private int actualhand;

    /**
     * Player osztály konstruktora. Inicializálja a játékost az adott paraméterekkel.
     * @param balance      a játékos egyenlege
     * @param name         a játékos neve
     * @param numberofhands hány kézzel játszik
     */
    public Player(int balance, String name, int numberofhands){
        this.balance=balance;
        this.name=name;
        this.hands=new ArrayList<>(numberofhands);
        this.numberofhands=numberofhands;
        for(int i=0;i<numberofhands;i++){
            Hand hand=new Hand();
            hands.add(hand);
        }
        actualhand=0;
    }


    /**
     * Visszaadja a játékos egyenlegét.
     * @return a játékos egyenlege
     */
    public int getBalance() {
        return balance;
    }
    /**
     * Visszaadja a játékos nevét.
     * @return a játékos neve
     */
    public String getName() {
        return name;
    }
    /**
     * Visszaadja a játékos kezeinek listáját.
     * @return a játékos kezeinek listája
     */
    public ArrayList<Hand> getHands() {
        return hands;
    }
    /**
     * Visszaadja, hogy hányadik kezénél járunk adott játékosnál.
     * @return hányadik kéz
     */
    public int getActualhand() {return actualhand;}
    /**
     * Beállítja a játékos egyenlegét.
     * @param balance a beállítandó egyenleg
     */
   public void setBalance(int balance){
        this.balance=balance;
   }
    /**
     * Beállítja, hogy hányadik kezénél járunk adott játékosnál.
     * @param actualhand a beállítandó kéz sorszáma
     */
    public void setActualhand(int actualhand) {
        this.actualhand = actualhand;
    }
    /**
     * A játékos húz (azaz kártyát ad hozzá) az aktuális kézéhez.
     * @param deck a pakli, ahonnan a kártyát veszi
     */
    public void Hit(Deck deck) {
        this.getHands().get(actualhand).addcard(deck.Deal());
    }
    /**
     * Ment egy listát a játékosokról egy fájlba.
     * @param players  a menteni kívánt játékosok listája
     * @param filename a fájl neve, amibe menteni szeretnénk
     */
    public static void savePlayers(ArrayList<Player> players, String filename) {//fájlba mentjük a játékosokat
        for(Player player:players){
            for(int i=0;i<player.getHands().size();i++){
                player.getHands().get(i).getHand().clear();
                player.setActualhand(0);
            }
        }
        try (FileOutputStream fileOut = new FileOutputStream(filename);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(players);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Saved to: "+filename);
    }
    /**
     * Betölti a játékosok listáját egy fájlból.
     * @param filename a fájl neve, ahonnan betöltjük a játékosokat
     * @return a betöltött játékosok listája
     */
    public static ArrayList<Player> loadPlayers(String filename) {//betöltjük őket
        ArrayList<Player> players = new ArrayList<>();
        try (FileInputStream fileIn = new FileInputStream(filename);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            players = (ArrayList<Player>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Loaded players from: "+filename);
        return players;
    }
}
