package hi;
import javax.swing.JFrame;

public class PaintTest {
	
	public static void main(String[] args) {

			Board board = new Board();
			JFrame application = new JFrame();
			
			application.add(board);
			application.setSize(1000,1000);
			application.setVisible(true);
			application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
	}
}
