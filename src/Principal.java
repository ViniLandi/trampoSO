import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.JTree;
import javax.swing.JScrollPane;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Principal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTree tree = new JTree();
	private DefaultMutableTreeNode nodePai;
	private DefaultMutableTreeNode selectedNode;
	private CriptografiaUtils criptografia;
	

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
		setBounds(100, 100, 641, 741);
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
		scrollPane.setBounds(10, 33, 605, 349);
		contentPane.add(scrollPane);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				 selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				
                if (selectedNode != null) {
                    String selectedItem = selectedNode.toString();
                    System.out.println("Você selecionou: " + selectedItem);

                    if (selectedItem.equals("Arquivo 1")) {
                        System.out.println("Ação para Arquivo 1");
                    }
                }
			}
		});
		scrollPane.setViewportView(tree);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 625, 22);
		contentPane.add(menuBar);
		
		JMenu mnNewMenu = new JMenu("Compactação");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem_3 = new JMenuItem("Compactar");
		mnNewMenu.add(mntmNewMenuItem_3);
		
		JMenuItem mntmNewMenuItem_4 = new JMenuItem("Descompactar");
		mnNewMenu.add(mntmNewMenuItem_4);
		
		JMenu mnNewMenu_1 = new JMenu("Criptografia");
		menuBar.add(mnNewMenu_1);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Criptografar");
		mntmNewMenuItem.addActionListener(e -> {
			
		    if (selectedNode != null) {
		        Object userObject = selectedNode.getUserObject();
		        if (userObject instanceof File) {
		            File selectedFile = (File) userObject;
		            System.out.println("Arquivo selecionado para criptografar: " + selectedFile.getAbsolutePath());
		            try {
						CriptografiaUtils.criptografarArquivo(selectedFile);
					} catch (Exception e1) {
						
						e1.printStackTrace();
					}
		        } else {
		            System.out.println("O nó selecionado não é um arquivo.");
		        }
		    } else {
		        System.out.println("Nenhum nó foi selecionado.");
		    }
		});
	
		mnNewMenu_1.add(mntmNewMenuItem);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Descriptografar");
		mntmNewMenuItem_1.addActionListener(e -> {
			
		    if (selectedNode != null) {
		        Object userObject = selectedNode.getUserObject();
		        if (userObject instanceof File) {
		            File selectedFile = (File) userObject;
		            System.out.println("Arquivo selecionado para descriptografar: " + selectedFile.getAbsolutePath());
		            try {
						CriptografiaUtils.descriptografarArquivo(selectedFile);
					} catch (Exception e1) {
						
						e1.printStackTrace();
					}
		        } else {
		            System.out.println("O nó selecionado não é um arquivo.");
		        }
		    } else {
		        System.out.println("Nenhum nó foi selecionado.");
		    }
		});
		
		mnNewMenu_1.add(mntmNewMenuItem_1);
		
		JPanel chartPanel = createPieChart();
		chartPanel.setBounds(10, 393, 605, 300);
		contentPane.add(chartPanel);
	}
	
	private DefaultMutableTreeNode buildTree(File file) {
        long size = calculateSize(file);
	    DefaultMutableTreeNode node = new DefaultMutableTreeNode(file);
	    
	    if (file.isDirectory()) {
	    	if (file.listFiles() != null)
	        for (File child : file.listFiles()) {
	            node.add(buildTree(child));
	        }
	    }
	    
	    return node;	    
	}
	
    private String getFileExtension(File file) {
        String fileName = file.getName();
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1);
        }
        return null;
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

	private String unidadeMedida(long tamanho) {
		String retorno;
		
		int x = 0;
		for (int i = 0; i < 4; i++) {
			if (tamanho >= 1024) {
				tamanho = tamanho / 1024;
				x++;
			} else {
				break;
			}
		}
		
		switch (x) {
		case 0: {
			retorno = "Bytes";
			break;
		}
		case 1: {
			retorno = "KB";
			break;
		}
		case 2: {
			retorno = "MB";
			break;
		}
		case 3: {
			retorno = "GB";
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + x);
		}
		
		return tamanho + " " + retorno;
	}

	private void loadDirectoryTree(File root) {
	    nodePai = buildTree(root);
	    tree.setModel(new DefaultTreeModel(nodePai));
	}
	
    public ChartPanel createPieChart() {
        // Dados do gráfico
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Categoria A", 40);
        dataset.setValue("Categoria B", 30);
        dataset.setValue("Categoria C", 20);
        dataset.setValue("Categoria D", 10);

        // Criar o gráfico de pizza
        JFreeChart chart = ChartFactory.createPieChart(
            "Porcentagem por arquivos no diretório", // Título
            dataset,                      // Dados
            true,                         // Mostrar legenda
            true,                         // Usar tooltips
            false                         // Não gerar URL
        );

        // Painel do gráfico
        return new ChartPanel(chart);
    }
}
