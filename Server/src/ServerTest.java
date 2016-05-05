import javax.swing.JFrame;
public class ServerTest {
	
	public static void main(String[] args){
		Server suger = new Server();
		suger.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		suger.startRunning();
	}
}
