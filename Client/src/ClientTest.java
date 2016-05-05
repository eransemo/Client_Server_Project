import javax.swing.JFrame;
public class ClientTest {
	public static void main(String[] args) {
		Client someone = new Client("127.0.0.1");
		someone.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		someone.startRunning();
	}

}
