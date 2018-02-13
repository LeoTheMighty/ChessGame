import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComponent;

public class GraphicsTest {

    public class MyGraphics extends JComponent {

        private static final long serialVersionUID = 1L;

        MyGraphics() {
            setPreferredSize(new Dimension(500, 100));
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.fillRect(200, 62, 30, 10);
        }
    }

    public void createGUI() {
        final JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        panel.add(new MyGraphics());
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
            	GraphicsTest GUI = new GraphicsTest();
                GUI.createGUI();
            }
        });
    }
}

//public class GraphicsTest extends JFrame {
//	
//	private class Panel extends JComponent {
//		
//		public Panel() {
//			setPreferredSize(new Dimension(640, 480));
//		}
//		
//		public void paintComponent(Graphics g) {
//			super.paintComponent(g);
//			g.fillRect(0, 0, 50, 50);
//		}
//	}
//	
//	public GraphicsTest() {
//		setTitle("GraphicsTest");
//		setSize(640, 480);
//	}
//	
//	public void doTheStuff() {
//		
//	}
//
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//		GraphicsTest gt = new GraphicsTest();
//		gt.setDefaultCloseOperation(EXIT_ON_CLOSE);
//		gt.setVisible(true);
//		
//		JPanel p = new JPanel();
//		p.add(new Panel());
//		
//	}
//}
