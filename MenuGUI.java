import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
/**
 * A MenuGUI osztály egy egyszerű menüfelületet valósít meg a Blackjack játékhoz.
 */
public class MenuGUI extends JFrame {
    /**
     * A "Start a game" gomb a menüfelületen.
     */
    private JButton Startgame;
    /**
     * A "Show leaderboard" gomb a menüfelületen.
     */
    private JButton Leaderboard;
    /**
     * A háttérképet tartalmazó címkével rendelkező JLabel a menüfelületen.
     */
    private JLabel backgroundlabel;

    /**
     * Konstruktor: inicializálja a menüfelületet és hozzáadja a szükséges komponenseket.
     */
    MenuGUI(){
        JFrame main = new JFrame();
        this.setTitle("Blackjack menu");
        setResizable(false);
        main.setSize(640, 480);
        this.setSize(640, 480);
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        ImageIcon background=new ImageIcon("menubackground.jpg");
        Image image=background.getImage();
        Image smallbackground=image.getScaledInstance(640,480, java.awt.Image.SCALE_SMOOTH);
        background= new ImageIcon(smallbackground);
        backgroundlabel=new JLabel(background);
        backgroundlabel.setBounds(0,0,640,480);

        Startgame=new JButton("Start a game");
        Startgame.setBounds(50,150,150,30);
        Startgame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Game startedgame=new Game();
                    main.dispose();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


        Leaderboard=new JButton("Show leaderboard");
        Leaderboard.setBounds(50,200,150,30);
        Leaderboard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Játékosok betöltése és rendezése egyenleg alapján
                ArrayList<Player> players=Player.loadPlayers("mentes.txt");
                players.sort(new Comparator<Player>() {
                    @Override
                    public int compare(Player o1, Player o2) {
                        return Integer.compare(o2.getBalance(),o1.getBalance());
                    }
                });
                Leaderboard showleaderboard=new Leaderboard(players);
            }
        });
        JLayeredPane menu=new JLayeredPane();
        menu.setPreferredSize(new Dimension(640,480));

        menu.add(backgroundlabel,JLayeredPane.DEFAULT_LAYER);
        menu.add(Startgame,JLayeredPane.PALETTE_LAYER);
        menu.add(Leaderboard,JLayeredPane.PALETTE_LAYER);
        main.setContentPane(menu);

        main.setVisible(true);
    }

}
