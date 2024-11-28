import java.awt.EventQueue;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

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

public class Principal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTree tree;
	private DefaultMutableTreeNode selectedNode;
	private JPanel chartPanel;
	private DefaultMutableTreeNode nodePai;
	private TreeUtils treeUtils = new TreeUtils();
	

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
		setLocationRelativeTo(null);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 33, 605, 349);
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
                    	Map<String, Integer> extensionCounts = new HashMap<>();
                        readFilesRecursively(arquivoSelecionado, extensionCounts);
                        contentPane.remove(chartPanel);
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
		menuBar.setBounds(0, 0, 625, 22);
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
				nodePai = treeUtils.atualizar(nodePai);
				tree.setModel(new DefaultTreeModel(nodePai));
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
						CompactacaoUtils.compactar(selectedFile);
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
						CompactacaoUtils.descompactar(selectedFile);
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
		
		createPieChart(new HashMap<>());
	}
	
	private void trocarDiretorio() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setBounds(10, 11, 117, 104);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File diretorioRaiz = fileChooser.getSelectedFile();
    		nodePai = treeUtils.loadDirectoryTree(nodePai, diretorioRaiz);
//			Thread thread = new Thread() {
//			    @Override
//			    public void run() {
//			        for (int i = 1; i <= 10; i++) {
//			        	if (i == 1)
//			            System.out.println("Thread: " + TreeUtils.leitura);
//			            try {
//			                Thread.sleep(500);
//			            } catch (InterruptedException e) {
//			                e.printStackTrace();
//			            }
//			        }
//			    }
//			};
//			thread.run();
			tree.setModel(new DefaultTreeModel(nodePai));
		}
		tree.setRootVisible(true);
	}
	
    private String getFileExtension(File file) {
        String fileName = file.getName();
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1);
        }
        return "sem_extensao";
    }
	
    // Método recursivo para ler arquivos e subpastas
    private void readFilesRecursively(File folder, Map<String, Integer> extensionCounts) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // Chamada recursiva para subpastas
                    readFilesRecursively(file, extensionCounts);
                } else {
                    // Processar arquivo
                    String extension = getFileExtension(file);
                    extensionCounts.put(extension, extensionCounts.getOrDefault(extension, 0) + 1);
                }
            }
        }
    }
	
    public void createPieChart(Map<String, Integer> extensionCounts) {
        // Dados do gráfico
    	int totalFiles = extensionCounts.values().stream().mapToInt(Integer::intValue).sum();
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<String, Integer> entry : extensionCounts.entrySet()) {
            int count = entry.getValue();
            double percentage = (count * 100.0) / totalFiles;
        	dataset.setValue(entry.getKey() + " (" + String.format("%.2f", percentage) + "%)", count);
		}

        // Criar o gráfico de pizza
        JFreeChart chart = ChartFactory.createPieChart3D(
            "Porcentagem por arquivos no diretório", // Título
            dataset,                      // Dados
            true,                         // Mostrar legenda
            true,                         // Usar tooltips
            false                         // Não gerar URL
        );
        
        // Painel do gráfico
        chartPanel = new ChartPanel(chart);
		chartPanel.setBounds(10, 393, 605, 300);
		contentPane.add(chartPanel);
        chartPanel.revalidate();
        chartPanel.repaint();
    }
}
