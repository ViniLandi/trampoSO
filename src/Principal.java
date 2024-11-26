import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.JTree;
import javax.swing.JScrollPane;

public class Principal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTree tree = new JTree();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Principal frame = new Principal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Principal() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 641, 410);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setBounds(10, 11, 117, 104);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
		    File selectedDirectory = fileChooser.getSelectedFile();
		    loadDirectoryTree(selectedDirectory);
		}
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 605, 349);
		contentPane.add(scrollPane);
		scrollPane.setViewportView(tree);
	}
	
	private DefaultMutableTreeNode buildTree(File file) {
        long size = calculateSize(file);
	    DefaultMutableTreeNode node = new DefaultMutableTreeNode(file.getName() + " | Size: " + size + " bytes");
	    if (file.isDirectory() && file.listFiles() != null) {
	        for (File child : file.listFiles()) {
	            node.add(buildTree(child));
	        }
	    }
	    return node;
	}
	
	private long calculateSize(File file) {
	    if (file.isFile()) {
	        return file.length();
	    }
	    long size = 0;
	    if (file.listFiles() != null) {
		    for (File child : file.listFiles()) {
		        size += calculateSize(child);
		    }
	    }
	    return size;
	}


	private void loadDirectoryTree(File root) {
	    DefaultMutableTreeNode rootNode = buildTree(root);
	    tree.setModel(new DefaultTreeModel(rootNode));
	}
}
