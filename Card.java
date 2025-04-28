import java.io.Serial;
import java.io.Serializable;

/**
 * A kártya megvalósításához használt osztály.
 */
public class Card implements Serializable {
    /**
     * A kártya rangja (pl. Dáma).
     */
    private Rank rank;
    /**
     * A kártya színe.
     */
    private Suit suit;

    /**
     * Konstruktor egy adott rangú és színű kártya létrehozásához.
     * @param rank a kártya rangja
     * @param suit a kártya színe
     */
    public Card(Rank rank,Suit suit){
        this.rank=rank;
        this.suit=suit;
    }
    /**
     * Visszaadja a kártya rangját.
     * @return a kártya rangja
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * Visszaadja a kártya színét.
     * @return a kártya színe
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     * Visszaadja az adott kártya értékét.
     * @return a kártya értéke
     */
    public int getValue(){
        return  this.getRank().getValue();
    }

    /**
     * Visszaadja az adott kártya megjelenítéséhez szükséges nevet (szín + név nagybetűkkel).
     *
     * @return a kártya megjelenítéséhez szükséges név
     */
    public String nameforpng(){
        return (String.valueOf(suit)+String.valueOf(rank));
    }

}
