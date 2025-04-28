import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * A Leaderboard osztály egy ranglistát megjelenítő ablakot valósít meg.
 */
public class Leaderboard extends JFrame {
    /**
     * A ranglistát tartalmazó JTable komponens.
     */
    JTable leaderboardgui=new JTable();

    /**
     * Konstruktor: létrehoz egy Leaderboard ablakot az adott játékosok alapján.
     * @param players A játékosok listája, akiknek az adatai alapján a ranglista készül.
     */
    public Leaderboard(ArrayList<Player> players){
        this.setTitle("Leaderboard");
        this.setSize(300,300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DefaultTableModel leaderboardmodel=new DefaultTableModel(new Object[]{"Player's name", "Balance"}, 0);//2 oszlop kell nekünk a név és az egyenleg


        for(Player player:players){
            Object[] sor={player.getName(),player.getBalance()};
            leaderboardmodel.addRow(sor);   //új sorok hozzáadása minden mentett játékos értékével
        }
        leaderboardgui=new JTable(leaderboardmodel){
            /**
             * Felülírjok a függvényt hogy egyik mező se lehessen szerkeszthető
             * @param row      adott sor száma
             * @param column   adott oszlop száma
             * @return   minden esetben false
             */
            @Override
            public boolean isCellEditable(int row,int column){
                return false;   //felülírjuk a függvényt azért hogy ne lehessen átírni egyik cella értékét se
            }
        };
        JScrollPane spane=new JScrollPane(leaderboardgui);//egy görgethető  táblázatot csinálok
        this.getContentPane().add(spane, BorderLayout.CENTER);
        this.add(spane);
        this.setVisible(true);
    }
}
