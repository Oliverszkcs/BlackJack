import org.junit.jupiter.api.Assertions;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
/**
 * Tesztek az alkalmazás részeihez.
 */

class Tests {
    Player test=new Player(100,"test",2);
    Deck testdeck=new Deck();

    @org.junit.jupiter.api.Test
    /**
     * Ellenőrzi, hogy a pakli megfelelő méretű-e.
     */
    void generatedeck(){
        Assertions.assertEquals(testdeck.getsize(),52);
    }

    /**
     * Ellenőrzi, hogy a pakli tartalmaz-e egy adott lapot.
     */
    @org.junit.jupiter.api.Test
    void deckcontainsacard(){
        Card testcard3=new Card(Rank.KING,Suit.SPADES);
        int contains = 0;
        for (Card itercard:testdeck.Deck){
            if(itercard.getRank().equals(testcard3.getRank()) && itercard.getSuit().equals(testcard3.getSuit())){
               contains=1;
            }
        }
        Assertions.assertEquals(contains, 1);

    }

    /**
     * Ellenőrzi, hogy a játékos húzásának eredményeként létrejön-e egy kártya a kézben.
     */
    @org.junit.jupiter.api.Test
    void playerhit() {
        test.getHands().get(0).addcard(testdeck.Deal());
        Assertions.assertNotNull(test.getHands().get(0).getHand());
    }

    /**
     * Ellenőrzi, hogy a kéz értéke helyesen számolódik-e, ha van ász.
     */
    @org.junit.jupiter.api.Test
    void handvaluewithace(){
        Hand testhand=new Hand();
        testhand.addcard(new Card(Rank.TEN,Suit.CLUBS));
        testhand.addcard(new Card(Rank.THREE,Suit.CLUBS));
        testhand.addcard(new Card(Rank.ACE,Suit.CLUBS));
        Assertions.assertEquals(testhand.getvalue(),14);
    }

    /**
     * Ellenőrzi, hogy a kéz eredménye helyesen számolódik.
     */
    @org.junit.jupiter.api.Test
    void handresults(){
       Hand testhand2=new Hand();
       Hand testhand3=new Hand();
       testhand2.addcard(new Card(Rank.TEN,Suit.CLUBS));
       testhand3.addcard (new Card(Rank.THREE,Suit.CLUBS));
        Assertions.assertEquals(testhand2.results(testhand3),1);
    }

    /**
     * Ellenőrzi, hogy a kártya rangja helyesen kerül beállításra.
     */
    @org.junit.jupiter.api.Test
    void cardrank(){
        Card testcard=new Card(Rank.TWO,Suit.DIAMONDS);
        Assertions.assertEquals(testcard.getRank(),Rank.TWO);
    }

    /**
     * Ellenőrzi, hogy a kártya névgenerálása helyesen működik-e.
     */
    @org.junit.jupiter.api.Test
    void generatenameforpng(){//név generálása kép-hez
        Card testcard2=new Card(Rank.KING,Suit.SPADES);
        Assertions.assertEquals(testcard2.nameforpng(),"SPADESKING");
    }

    /**
     * Ellenőrzi, hogy az adott indexen lévő kártya helyesen kerül-e visszaadásra.
     */
    @org.junit.jupiter.api.Test
    void getcardat(){//adott indexen van-e a kártya
           Card testcard=new Card(Rank.TWO,Suit.DIAMONDS);
           Card testcard2=new Card(Rank.KING,Suit.SPADES);
           Hand testhand=new Hand();
           testhand.addcard(testcard);
           testhand.addcard(testcard2);
           Assertions.assertEquals(testhand.getcardat(1),testcard2);
    }

    /**
     * Ellenőrzi, hogy a menüablak elindul-e megfelelően.
     */
    @org.junit.jupiter.api.Test
    void menuguistart(){
        JFrame testmenu=new MenuGUI();
        Assertions.assertEquals(testmenu.getTitle(),"Blackjack menu");
        Assertions.assertNotNull(testmenu.getLayeredPane());
        Assertions.assertNotNull(testmenu.getComponents());
        Assertions.assertEquals(testmenu.getBounds(),new Rectangle(0,0,640,480));
    }

    /**
     * Ellenőrzi, hogy a játék elindul-e megfelelően.
     */
     @org.junit.jupiter.api.Test
    void Gamestart() throws Exception {
        JFrame testgame=new Game();
        Assertions.assertNotNull(testgame.getLayeredPane());
        Assertions.assertNotNull(testgame.getContentPane());
        Assertions.assertNotNull(testgame.getComponents());
         Assertions.assertNotEquals(testgame.getBounds(),new Rectangle(0,0,640,480));
     }

    /**
     * Ellenőrzi, hogy a játékosok mentése és betöltése helyesen működik-e.
     */
    @org.junit.jupiter.api.Test
    void saveandloadplayers(){
        ArrayList<Player> testplayerstosave=new ArrayList<>();
        Player testplayer=new Player(1000,"TestPlayer",1);
        testplayerstosave.add(testplayer);
        ArrayList<Player> testplayersloaded;
        Player.savePlayers(testplayerstosave,"savetest.save");
        testplayersloaded=Player.loadPlayers("savetest.save");
        Assertions.assertEquals(testplayersloaded.get(0).getName(),testplayer.getName());
        Assertions.assertEquals(testplayersloaded.get(0).getBalance(),testplayer.getBalance());
        Assertions.assertEquals(testplayersloaded.get(0).getActualhand(),testplayer.getActualhand());
        Assertions.assertEquals(testplayersloaded.get(0).getHands().size(),testplayer.getHands().size());
    }
    }
