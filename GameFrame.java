import javax.swing.JFrame;

public class GameFrame extends JFrame{
    GameFrame(){

        this.add(new Gamepanel());
        this.setTitle("Snake");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        
    }
}
