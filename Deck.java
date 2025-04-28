import java.util.ArrayList;
import java.util.Collections;

/**
 * A pakli megvalósításához használt osztály.
 */
public class Deck {
    /**
     * A paklit tartalmazó lista.
     */
    public ArrayList<Card> Deck=new ArrayList<>(52);
    /**
     * Konstruktor egy új pakli létrehozásához és inicializálásához.
     * Az ACE kártya értékét beállítja 11-re, ha az értéke 1(ez akkor lehet probléma ha több játékot játszunk egymás után)
     */
   public Deck(){
       for(Suit suit: Suit.values()){
           for(Rank rank:Rank.values()){
               Card card=new Card(rank, suit);
               if(card.getRank()== Rank.ACE && card.getValue()==1){
                   card.getRank().setValue(11);
               }
               Deck.add(card);
           }
       }
   }

    /**
     * A pakli megkeverése.
     */
   public void shuffle(){
       Collections.shuffle(Deck);
   }
    /**
     * Visszaadja a pakli méretét.
     *
     * @return a pakli mérete
     */
   public int getsize(){
       return Deck.size();
   }
    /**
     * Kártya kivétele a pakliból(osztás).
     *
     * @return a kivett kártya
     */
   public Card Deal(){
       return Deck.remove(Deck.size()-1);
   }


}
