package carrera_caballo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.EmptyBorder;

public class CarreraDeCaballos extends javax.swing.JFrame {

    private JProgressBar[] progressBars;
    private JButton startButton;
    private JButton resetButton;
    private boolean raceStarted = false;
    private JLabel[] percentageLabels;
    private volatile int winnerIndex = -1;
    private final Object lock = new Object();
    private final Object winnerLock = new Object();
    
    public CarreraDeCaballos() {
        setTitle("Carrera de Caballos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);

        initializeUI();
    
        pack();
        setLocationRelativeTo(null);
    }

   private void initializeUI() {
    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(5, 1));
    
    int margenSuperior = 20;
    int margenIzquierdo = 100; 
        EmptyBorder margen = new EmptyBorder(margenSuperior, margenIzquierdo, 0, 0);
        panel.setBorder(margen);

    progressBars = new JProgressBar[4];
    percentageLabels = new JLabel[4];

    String[] nombresCaballos = {"Caballo De Ruben", "Caballo De Ezequiel", "Caballo De Hector", "Caballo De Carlos"};
    Color[] horseColors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW}; // Colores para los caballos

    for (int i = 0; i < 4; i++) {
        progressBars[i] = new JProgressBar(0, 100);
        progressBars[i].setValue(0);

        // Asignar el color al fondo de la barra de progreso
        progressBars[i].setForeground(horseColors[i]);

        panel.add(new JLabel(nombresCaballos[i])); // Asigna el nombre del caballo
        panel.add(progressBars[i]);

        percentageLabels[i] = new JLabel("0%");
        panel.add(percentageLabels[i]);
    }
    
   
        startButton = new JButton("Iniciar Carrera");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!raceStarted) {
                    raceStarted = true;
                    winnerIndex = -1; // Reiniciar el índice del ganador
                    startRace();
                }
            }
        });

        resetButton = new JButton("Reiniciar Carrera");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetRace();
            }
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(resetButton);

       
        double margenSuperiorBotones = 0.5; 
        int margenINFERIORBotones = 10;
        EmptyBorder buttonPanelMargin = new EmptyBorder((int) margenSuperiorBotones, 0,margenINFERIORBotones, 0);
        buttonPanel.setBorder(buttonPanelMargin);

        
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(panel, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);    
    }

    private synchronized void startRace() {
        Thread[] horseThreads = new Thread[4];
        for (int i = 0; i < 4; i++) {
            final int horseIndex = i;
            horseThreads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    simulateHorseRace(horseIndex);
                }
            });
            horseThreads[i].start();
        }
    }

    private static final int MAX_PROGRESS = 100;
    
    private void simulateHorseRace(int horseIndex) {
        while (true) {
            int currentProgress;

            synchronized (lock) {
                if (winnerIndex != -1) {
                    return;
                }
                currentProgress = progressBars[horseIndex].getValue();
            }

            int increment = (int) (Math.random() * 15) + 1;
            int newProgress = currentProgress + increment;

            if (newProgress >= MAX_PROGRESS) {
                newProgress = MAX_PROGRESS;
                synchronized (lock) {
                    if (winnerIndex == -1) {
                        winnerIndex = horseIndex;
                        SwingUtilities.invokeLater(() -> announceWinner(horseIndex));
                    }
                }
                synchronized (winnerLock) {
                    winnerLock.notifyAll(); // Notificar a otros hilos que deben detenerse
                }
            }

            progressBars[horseIndex].setValue(newProgress);
            final int finalProgress = newProgress;

            SwingUtilities.invokeLater(() -> percentageLabels[horseIndex].setText(finalProgress + "%"));

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void announceWinner(int winnerIndex) {
        JOptionPane.showMessageDialog(this, "Caballo " + (winnerIndex + 1) + " ha ganado la carrera!", "Carrera Terminada", JOptionPane.INFORMATION_MESSAGE);
    }

    private void resetRace() {
        // Reinicia las barras de progreso y las etiquetas de porcentaje
        for (int i = 0; i < 4; i++) {
            progressBars[i].setValue(0);
            percentageLabels[i].setText("0%");
        }
        raceStarted = false;
        winnerIndex = -1; // Reiniciar el índice del ganador
    }
}

