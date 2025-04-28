import java.io.Serializable;
import java.util.ArrayList;

/**
 * Egy kéz megvalósításához szükséges osztály a Blackjack játékban.
 * Ez az osztály felelős a kártyák tárolásáért, az érték kiszámításáért és a tét kezeléséért.
 */
public class Hand implements Serializable {
    /**
     * Egy kéz(ami kártyákat tartalmazó tömb)
     */
    private ArrayList<Card> Hand=new ArrayList<>();
    /**
     * A tét
     */
    private int bet;

    /**
     * Kéz konstruktora, létrehoz egy üres kezet
     */
    public Hand(){

    }

    /**
     * Adott kéz értékének kiszámolása
     * @return adott kéz értéke, egy egész szám
     */
    public int getvalue(){//adott kéz értékének kiszámolása, ha >21 akkor ász értéke=1
        int value=0;
        for(Card card:Hand){
            value+=card.getValue();
            if(value > 21){
                for(Card ace:Hand){
                    if(ace.getRank()==Rank.ACE && ace.getValue()==11 && value > 21){
                        ace.getRank().setValue(1);
                        value=getvalue();
                    }/*else if(ace.getRank()==Rank.ACE && ace.getValue()==1 && value < 21){
                        ace.getRank().setValue(11);
                        value=getvalue();
                    }*/
                }
            }
        }

        return value;
    }

    /**
     * Kártya hozzáadása az adott kézhez
     * @param card adott kártya
     */
    public void addcard(Card card){
        Hand.add(card);
    }

    /**
     * Megadott indexen levő kártyát adja vissza
     * @param index index
     * @return indexen levő kártya
     */
    public Card getcardat(int index){
        return Hand.get(index);
    }

    /**
     * Visszadja az adott kezet
     * @return az adott kéz
     */
    public ArrayList<Card> getHand(){
        return this.Hand;
    }

    /**
     * Tét lekérdezése
     * @return a tét
     */
    public int getBet(){
        return bet;
    }

    /**
     * Tét beállítása
     * @param bet egész szám,ez lesz a tét
     */
    public void setBet(int bet) {
        this.bet = bet;
    }

    /**
     * Egy adott kéz összehasonlítása egy paraméterként megadott másik kézzel
     * @param hand Ehhez a kézhez hasonlít (ez majd az osztó keze lesz)
     * @return -1 ha veszített, 0 ha döntetlen , 1 ha nyert
     */
    public int results(Hand hand){//2 kéz összehasonlítása ( ez lesz használva az osztóval való összehasonlításhoz)
        if(this.getvalue() == hand.getvalue() || (this.getvalue() >21 && hand.getvalue() >21 )){
            return 0;
        }else if((hand.getvalue() <= 21 && this.getvalue() < hand.getvalue()) || (hand.getvalue()<=21 && this.getvalue()>21)){
            return -1;
        }else {
            return 1;
        }
    }
}
