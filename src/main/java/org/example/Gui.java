package org.example;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;



public class Gui {
    private final JFrame jFrame;
    private JButton firstNicButton;
    private JButton secondNicButton;
    private JButton sendButton;
    private JButton chooseFile;
    private ImageIcon buttonIcon;
    private ImageIcon chooseFileIcon;
    private ImageIcon fileLabelIcon;
    private ImageIcon sendIcon;
    private ImageIcon ethernetIcon;
    private ImageIcon WifiIcon;
    private JLabel firstNicNameLabel;
    private JLabel secondNicNameLabel;
    private JLabel fileNameLabel;
    private JProgressBar firstHalfProgressBar;
    private JProgressBar secondHalfProgressBar;
    private FileHandler fileHandler;
    private TcpClient tcpClient;
    private NetworkInterface firstNic;
    private NetworkInterface secondNic;
    private HashMap<String,String> networkInterfaces;

    public Gui(){


        jFrame = new JFrame();

        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        flowLayout.setHgap(100);
        flowLayout.setVgap(15);

        jFrame.setLayout(flowLayout);


        initIcons();
        initFileNameLabel();
        initNicButtons();
        initNicLabels();
        initSendAndChooseFileButtons();
        initProgressBars();

        initFrame();

    }

    private void initProgressBars() {
        firstHalfProgressBar = new JProgressBar(0,100);
        secondHalfProgressBar = new JProgressBar(0,100);
        JProgressBar[] jProgressBars = {firstHalfProgressBar,secondHalfProgressBar};

        for(JProgressBar bar : jProgressBars){
            bar.setValue(0);
            bar.setPreferredSize(new Dimension(300,80));
            bar.setStringPainted(true);
            bar.setFont(new Font("Comic sans", Font.BOLD, 20));
            bar.setForeground(new Color (25, 25, 112));

        }

    }

    private void initSendAndChooseFileButtons() {

        chooseFile = new JButton("choose new file! ");
        sendButton = new JButton("Send! ");

        JButton[] fileButtons = {chooseFile,sendButton};

        for(JButton button: fileButtons){
            button.setFont(new Font("Roboto",Font.BOLD,23));
            button.setBackground(Color.white);
            button.setForeground(Color.BLACK);
            button.setFocusable(false);
            button.setPreferredSize(new Dimension(300,80));
            button.setHorizontalTextPosition(JButton.TRAILING);
            button.setIconTextGap(10);
        }

        chooseFile.setIcon(chooseFileIcon);
        sendButton.setIcon(sendIcon);


        chooseFile.addActionListener(e -> {
            if (getFile()){
                fileNameLabel.setText("File name: " + fileHandler.getFile().getName() + "   |   File size: " + fileHandler.getFile().length() + " Bytes");
                fileNameLabel.setVisible(true);
            }else {
                fileNameLabel.setVisible(false);
            }});



        sendButton.addActionListener(e -> {

            if (Objects.nonNull(firstNic)&& Objects.nonNull(secondNic) && Objects.nonNull(fileHandler.getFile())){
                try {
                   tcpClient =  new TcpClient(fileHandler.getFileDataFirstHalf(), fileHandler.getFileDataSecondHalf(),  firstNic,  secondNic);
                }catch (IOException exception){exception.printStackTrace();}

            }
    });

    }

    private void initIcons() {

        chooseFileIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/fileIcon.jpg")));
        sendIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/sendIcon.png")));
        ethernetIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/ethIcon.png")));
        WifiIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/wifiIcon.png")));
        buttonIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/nicIcon.png")));
        fileLabelIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/fileLabelIcon.png")));

    }

    private void initFrame() {
        jFrame.getContentPane().removeAll();
        jFrame.getContentPane().repaint();
        jFrame.getContentPane().validate();


        jFrame.getContentPane().add(fileNameLabel);
        jFrame.getContentPane().add(firstNicButton);
        jFrame.getContentPane().add(secondNicButton);
        jFrame.getContentPane().add(firstNicNameLabel);
        jFrame.getContentPane().add(secondNicNameLabel);

        jFrame.getContentPane().add(firstHalfProgressBar);
        firstHalfProgressBar.setValue(0);
        jFrame.getContentPane().add(secondHalfProgressBar);
        secondHalfProgressBar.setValue(0);

        jFrame.getContentPane().add(sendButton);
        jFrame.getContentPane().add(chooseFile);




        jFrame.setTitle("File Share using two network interfaces");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(900, 700);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);

    }

    private void initNicLabels() {
        firstNicNameLabel = new JLabel("");
        secondNicNameLabel = new JLabel("");

        JLabel[] labels = {firstNicNameLabel, secondNicNameLabel};

        for(JLabel label : labels) {
            label.setFont(new Font("Comic sans", Font.BOLD, 14));
            label.setForeground(Color.BLACK);
            label.setPreferredSize(new Dimension(300, 30));
        }
    }
    public void initNicButtons(){
        firstNicButton = new JButton("First network interface ");
        secondNicButton = new JButton("Second network interface ");
        firstNicButton.addActionListener(this::displayNetworkInterfaces);
        secondNicButton.addActionListener(this::displayNetworkInterfaces);

        Color buttonsColor = new Color(135, 195, 205);
        Color buttonHoverColor = new Color(176, 224, 230);

        JButton[] buttons = {firstNicButton, secondNicButton};


        for(JButton button : buttons){
            button.setFont(new Font("Comic sans",Font.PLAIN,23));
            button.setBackground(buttonsColor);
            button.setForeground(Color.BLACK);
            button.setFocusable(false);
            button.setBorder(new LineBorder(Color.BLACK, 3));
            button.setVerticalTextPosition(JButton.BOTTOM);
            button.setHorizontalTextPosition(JButton.CENTER);
            button.setIconTextGap(10);
            button.setPreferredSize(new Dimension(300,300));
            button.setIcon(buttonIcon);
            button.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }
                @Override
                public void mouseReleased(MouseEvent e) {
                    button.setBackground(buttonsColor);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(buttonHoverColor);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(buttonsColor);
                }
            });
        }
    }

    private void displayNetworkInterfaces(ActionEvent source) {
        jFrame.removeAll();

        getAvailableNetworkInterfaces();
        networkInterfaces.forEach((networkName, portName) ->{
            JButton nicButton = new JButton("  " + networkName + "  ");
            if (portName.toLowerCase().contains("wlan")) {
                nicButton.setIcon(WifiIcon);
            }else {nicButton.setIcon(ethernetIcon);}
            nicButton.addActionListener(e -> {
                try {
                    if (source.getSource().equals(firstNicButton)){
                        firstNic = NetworkInterface.getByName(portName);
                        firstNicNameLabel.setText(firstNic.getDisplayName());
                    }else {
                        secondNic = NetworkInterface.getByName(portName);
                        secondNicNameLabel.setText(secondNic.getDisplayName());
                    }
                    initFrame();
                } catch (SocketException ex) {
                    ex.printStackTrace();
                }
            });

            nicButton.setFont(new Font("Comic sans",Font.PLAIN,23));
            nicButton.setBackground(new Color(135, 195, 205));
            nicButton.setForeground(Color.BLACK);
            nicButton.setFocusable(false);
            nicButton.setBorder(new LineBorder(Color.BLACK, 2));
            nicButton.setIconTextGap(10);
            jFrame.add(nicButton);
            jFrame.repaint();
            jFrame.validate();

        });
    }

    private void getAvailableNetworkInterfaces() {
        networkInterfaces = new HashMap<>();
        try {
            NetworkInterface
                    .getNetworkInterfaces()
                    .asIterator()
                    .forEachRemaining(networkInterface -> {
                        try {
                            if (networkInterface.isUp() && !networkInterface.isLoopback() && !networkInterface.getDisplayName().contains("Virtual")) {
                                networkInterfaces.put(networkInterface.getDisplayName(), networkInterface.getName());
                            }
                        } catch (SocketException e) {
                            e.printStackTrace();
                        }
                    });
        }catch (SocketException e){e.printStackTrace();}
    }

    private void initFileNameLabel() {
        fileNameLabel = new JLabel("");
        fileNameLabel.setPreferredSize(new Dimension(1920, 90));
        fileNameLabel.setFont(new Font("Comic sans", Font.BOLD, 20));
        fileNameLabel.setForeground(Color.BLACK);
        fileNameLabel.setHorizontalAlignment(JLabel.CENTER);
        fileNameLabel.setHorizontalTextPosition(JLabel.TRAILING);
        fileNameLabel.setIconTextGap(10);
        fileNameLabel.setIcon(fileLabelIcon);
        fileNameLabel.setVisible(false);
    }





    private boolean getFile() {

        JFileChooser jFileChooser = new JFileChooser();
        String userHomeFolder = System.getProperty("user.home");
        jFileChooser.setCurrentDirectory(new File(userHomeFolder + "\\desktop"));
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int action = jFileChooser.showOpenDialog(jFrame);
        if (action == JFileChooser.APPROVE_OPTION) {
            fileHandler = new FileHandler(jFileChooser.getSelectedFile());
            return true;
        }
        return false;
    }


}
