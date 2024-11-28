import java.awt.EventQueue;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;
import javax.swing.JLabel;
import java.awt.Font;

public class Principal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTree tree;
	private DefaultMutableTreeNode selectedNode;
	private JPanel chartPanel;
	private DefaultMutableTreeNode nodePai;
	private TreeUtils treeUtils = new TreeUtils();
	private JScrollPane scrollPaneChart = new JScrollPane();
	private JLabel lblNewLabel = new JLabel("Carregando...");
	

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
		setBounds(100, 100, 1520, 777);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(18, 69, 697, 658);
		contentPane.add(scrollPane);
		tree = new JTree(new DefaultMutableTreeNode());
		tree.setRootVisible(false);
		tree.setCellRenderer(new CustomTreeCellRenderer(treeUtils));
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				
                if (selectedNode != null) {
                    System.out.println("Você selecionou: " + treeUtils.getFileByNode(selectedNode));
                    File arquivoSelecionado = treeUtils.getFileByNode(selectedNode);
                    if (arquivoSelecionado.isDirectory()) {
//                    	É diretório
//                    	Map<String, Integer> extensionCounts = new HashMap<>();
                    	Map<String, Long> extensionCounts = new HashMap<>();
                        readFilesRecursively(arquivoSelecionado, extensionCounts);
                        scrollPaneChart.remove(chartPanel);
                        createPieChart(extensionCounts);
                    } else {
//                    	Não é diretório
                    	System.out.println("Não é diretório");
                    }
                }
			}
		});
		scrollPane.setViewportView(tree);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 1412, 22);
		contentPane.add(menuBar);
		
		JMenu mnNewMenu_2 = new JMenu("Arquivo");
		menuBar.add(mnNewMenu_2);
		
		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Selecionar diretório");
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				trocarDiretorio();
			}
		});
		mnNewMenu_2.add(mntmNewMenuItem_2);
		
		JMenuItem mntmNewMenuItem_5 = new JMenuItem("Atualizar");
		mntmNewMenuItem_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				atualizar();
			}
		});
		mnNewMenu_2.add(mntmNewMenuItem_5);
		
		JMenu mnNewMenu = new JMenu("Compactação");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem_3 = new JMenuItem("Compactar");
		mntmNewMenuItem_3.addActionListener(e -> {
			
		    if (selectedNode != null) {
		        Object userObject = treeUtils.getFileByNode(selectedNode);
		        if (userObject instanceof File) {
		            File selectedFile = (File) userObject;
		            System.out.println("Arquivo selecionado para compactar: " + selectedFile.getAbsolutePath());
		            try {
		            	Boolean sucesso = CompactacaoUtils.compactar(selectedFile);
						if (sucesso)
							atualizar();
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
		mnNewMenu.add(mntmNewMenuItem_3);
		
		JMenuItem mntmNewMenuItem_4 = new JMenuItem("Descompactar");
		mntmNewMenuItem_4.addActionListener(e -> {
			
		    if (selectedNode != null) {
		        Object userObject = treeUtils.getFileByNode(selectedNode);
		        if (userObject instanceof File) {
		            File selectedFile = (File) userObject;
		            System.out.println("Arquivo selecionado para descompactar: " + selectedFile.getAbsolutePath());
		            try {
		            	Boolean sucesso = CompactacaoUtils.descompactar(selectedFile);
						if (sucesso)
							atualizar();
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
		mnNewMenu.add(mntmNewMenuItem_4);
		
		JMenu mnNewMenu_1 = new JMenu("Criptografia");
		menuBar.add(mnNewMenu_1);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Criptografar");
		mntmNewMenuItem.addActionListener(e -> {
			
		    if (selectedNode != null) {
		        Object userObject = treeUtils.getFileByNode(selectedNode);
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
		        Object userObject = treeUtils.getFileByNode(selectedNode);
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
		scrollPaneChart.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		scrollPaneChart.setBounds(725, 69, 769, 658);
		contentPane.add(scrollPaneChart);
		
		
		lblNewLabel.setVisible(false);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel.setBounds(18, 33, 204, 25);
		contentPane.add(lblNewLabel);
		
		createPieChart(new HashMap<>());
	}
	
	private void atualizar() {
		lblNewLabel.setVisible(true);
		nodePai = treeUtils.atualizar(nodePai);
		tree.setModel(new DefaultTreeModel(nodePai));
		lblNewLabel.setVisible(false);
	}
	
	private void trocarDiretorio() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setBounds(10, 11, 117, 104);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File diretorioRaiz = fileChooser.getSelectedFile();
			lblNewLabel.setVisible(true);
			SwingWorker<Void, Void> worker = new SwingWorker<>() {
			    @Override
			    protected Void doInBackground() {
			        // Executa o método pesado em uma thread separada
			        nodePai = treeUtils.loadDirectoryTree(nodePai, diretorioRaiz);
			        return null;
			    }

			    @Override
			    protected void done() {
			        try {
			            // Atualiza a interface gráfica após o término do processamento
			            lblNewLabel.setVisible(false); // Esconde o label
			            tree.setModel(new DefaultTreeModel(nodePai)); // Atualiza o JTree
			    		tree.setRootVisible(true);
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
			    }
			};

			// Inicia o SwingWorker
			worker.execute();
		}
	}
	
    private void readFilesRecursively(File folder, 
//    		Map<String, Integer> extensionCounts
    		Map<String, Long> extensionCounts
    		) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    readFilesRecursively(file, extensionCounts);
                } else {
                    String extension = treeUtils.getFileExtension(file);
//                    extensionCounts.put(extension, extensionCounts.getOrDefault(extension, 0) + 1);
                    extensionCounts.put(extension, extensionCounts.getOrDefault(extension, 0L) + file.length());
                }
            }
        }
    }

//    public void createPieChart(Map<String, Integer> extensionCounts) {
    public void createPieChart(Map<String, Long> extensionCounts) {
        // Dados do gráfico
//    	int totalFiles = extensionCounts.values().stream().mapToInt(Integer::intValue).sum();
    	Long totalSize = extensionCounts.values().stream().mapToLong(Long::longValue).sum();
        DefaultPieDataset dataset = new DefaultPieDataset();
//        for (Map.Entry<String, Integer> entry : extensionCounts.entrySet()) {
        for (Map.Entry<String, Long> entry : extensionCounts.entrySet()) {
//            int count = entry.getValue();
        	Long size = entry.getValue();
//            double percentage = (size * 100.0) / totalFiles;
            double percentage = (size * 100.0) / totalSize;
//        	dataset.setValue(entry.getKey() + " (" + String.format("%.2f", percentage) + "%)", count);
        	dataset.setValue(entry.getKey() + " (" + treeUtils.formatFileSize(size) + ") (" + String.format("%.2f", percentage) + "%) ", size);
		}


        JFreeChart chart = ChartFactory.createPieChart3D(
            "Porcentagem por arquivos no diretório",
            dataset,                      
            true,                         
            true,                         
            false                        
        );


		chartPanel = new ChartPanel(chart);
		scrollPaneChart.setViewportView(chartPanel);
        chartPanel.revalidate();
        chartPanel.repaint();
    }
}
