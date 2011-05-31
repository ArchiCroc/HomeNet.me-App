/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * HomeNetAppGui.java
 *
 * Created on May 11, 2011, 3:14:05 PM
 */
package homenetapp;

import java.util.*;
//import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.*;
import java.io.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.net.*;
import java.security.KeyStore;
import javax.net.ssl.*;
//import java.security.*;
import java.security.cert.*;
import org.apache.commons.codec.binary.Base64;

import homenet.XmlrpcClient;

/**
 *
 * @author mdoll
 */
public class HomeNetAppGui extends javax.swing.JFrame {
    
    public HashMap<String,JCheckBoxMenuItem> menuItems = new HashMap();
    public HomeNetApp homenetapp;
    
    private LinkedList<homenet.Packet> packetList = new LinkedList();

    /** Creates new form HomeNetAppGui */
    public HomeNetAppGui() {

        homenetapp = new HomeNetApp();

        initComponents();

        //if not configured
        //if(true){
        if (homenetapp.config.getBoolean("config.done") == false) {
            System.out.println("HomeNet App Not Setup");
            System.out.println("Launching Setup Wizard");
            javax.swing.JPanel wizardPanel = new SetupWizardGui(this);

            cardPanel.add(wizardPanel, "wizard");
            //validate();

            pack();
            wizardPanel.setVisible(true);


            ((java.awt.CardLayout) cardPanel.getLayout()).next(cardPanel);
            // ((java.awt.CardLayout) cardPanel.getLayout()).show(cardPanel,"wizard");

        } else {
            startMainGui();
        }

        //redirect console to gui
        //redirectSystemStreams();
    }

    public void startMainGui() {
        System.out.append("startMainGui");
        startHomeNetApp();
        topMenuBar.setVisible(true);
        ((java.awt.CardLayout) cardPanel.getLayout()).first(cardPanel);
    }
    
    public void exit(){
        saveConfig();
        System.out.println("Exiting");
        homenetapp.exit();
        System.exit(0);
    }

    private void startHomeNetApp() {
        System.out.append("startHomeNetApp");
        try {
            homenetapp.start();
        } catch (Exception e) {
            //show popup and exit program
            System.err.println(e.getMessage());
        }

        this.addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent winEvt) {
                // Perhaps ask user if they want to save any unsaved files first.
                exit();
            }
        });

        loadSettings();
        SendPacketFrame.setLocationRelativeTo(null);

        homenetapp.homenet.addListener(new homenet.Listener() {

            public void packetRecieved(homenet.Packet packet) {
                addPacketToList(packet);
            }
        });
        
        homenetapp.serialmanager.addListener(new SerialListener(){
             public void portAdded(String name){
                System.out.println("portAddedEvent");
                
                javax.swing.JCheckBoxMenuItem serialPortCheckBox = new javax.swing.JCheckBoxMenuItem(name);
 
                serialPortCheckBox.addActionListener(new java.awt.event.ActionListener() {

                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                         JCheckBoxMenuItem checkbox = (JCheckBoxMenuItem) evt.getSource();
                         if(checkbox.isSelected()){
                            if(homenetapp.serialmanager.activatePort(checkbox.getText()) != true){
                                javax.swing.JOptionPane.showMessageDialog(null, "Can't Connect to Port "+checkbox.getText()+". Try again in a few seconds", "Error", javax.swing.JOptionPane.ERROR_MESSAGE); 
                            }
                         } else {
                             homenetapp.serialmanager.deactivatePort(checkbox.getText());
                         }
                    }
                });

                // serialPortCheckBox.setMnemonic(java.awt.event.KeyEvent.VK_1);
                menuItems.put(name, serialPortCheckBox);
                menuSerialPorts.add(serialPortCheckBox);
 
            }

            public void portRemoved(String name){
                System.out.println("portRemovedEvent");
                menuSerialPorts.remove(menuItems.get(name));
            }
            public void portActivated(String name){
                System.out.println("portActivatedEvent");
                 menuItems.get(name).setSelected(true);
                
            }

            public void portDeactivated(String name){
                System.out.println("portDeactivatedEvent");
                menuItems.get(name).setSelected(false);
            }
        
        });


        homenetapp.serialmanager.start();
    }

    class setupFrameEventHandler extends WindowAdapter {

        public void windowClosing(WindowEvent evt) {
            //check to make sure all the settings are good
            System.out.println("Checking New Config");
            //then reboot the app
            System.out.println("rebooting app with new config");
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    startHomeNetApp();
                }
            });
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        SendPacketFrame = new javax.swing.JFrame();
        sendPacketButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        toNodeLabel = new javax.swing.JLabel();
        toNodeSpinner = new javax.swing.JSpinner();
        toDeviceLabel = new javax.swing.JLabel();
        toDeviceSpinner = new javax.swing.JSpinner();
        fromNodeLabel = new javax.swing.JLabel();
        fromNodeSpinner = new javax.swing.JSpinner();
        fromDeviceLabel = new javax.swing.JLabel();
        commandLabel = new javax.swing.JLabel();
        commandComboBox = new javax.swing.JComboBox();
        payloadLabel = new javax.swing.JLabel();
        payloadTextField = new javax.swing.JTextField();
        fromDeviceSpinner = new javax.swing.JSpinner();
        jSplitPane1 = new javax.swing.JSplitPane();
        SettingsDialog = new javax.swing.JDialog();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        serverLabel = new javax.swing.JLabel();
        apiKeyLabel = new javax.swing.JLabel();
        serverTextField = new javax.swing.JTextField();
        apiKeyTextField = new javax.swing.JTextField();
        testButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        certPropertiesLabel = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        serverPortLabel = new javax.swing.JLabel();
        serverPortTextField = new javax.swing.JTextField();
        enableUPnPCheckBox = new javax.swing.JCheckBox();
        enableServerCheckBox = new javax.swing.JCheckBox();
        jSeparator1 = new javax.swing.JSeparator();
        settingsSaveButton = new javax.swing.JButton();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        cardPanel = new javax.swing.JPanel();
        mainPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        packetTextArea = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        packetListModel = new DefaultListModel();
        jList1 = new javax.swing.JList();
        autoUpdateToggleButton = new javax.swing.JToggleButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        consoleTextPane = new javax.swing.JTextPane();
        statusPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        topMenuBar = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        menuTools = new javax.swing.JMenu();
        menuToolsSendPacket = new javax.swing.JMenuItem();
        menuToolsSettings = new javax.swing.JMenuItem();
        menuSerialPorts = new javax.swing.JMenu();
        menuHelp = new javax.swing.JMenu();
        menuHelpOnline = new javax.swing.JMenuItem();
        menuHelpAbout = new javax.swing.JMenuItem();

        SendPacketFrame.setTitle("Send Packet");
        SendPacketFrame.setMinimumSize(new java.awt.Dimension(380, 240));
        SendPacketFrame.setResizable(false);

        sendPacketButton.setText("Send");
        sendPacketButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendPacketButtonActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Packet"));

        toNodeLabel.setText("To Node: ");

        toNodeSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 4095, 1));

        toDeviceLabel.setText("To Device:");

        toDeviceSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 15, 1));

        fromNodeLabel.setText("From Node:");

        fromNodeSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 4095, 1));

        fromDeviceLabel.setText("From Device:");

        commandLabel.setText("Command:");

        commandComboBox.setModel(new javax.swing.DefaultComboBoxModel(homenetapp.getCommandKeys()));
        commandComboBox.setRenderer(new CommandRenderer());

        payloadLabel.setText("Payload:");

        fromDeviceSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 15, 1));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(payloadLabel)
                    .addComponent(commandLabel)
                    .addComponent(fromNodeLabel)
                    .addComponent(toNodeLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(commandComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(toNodeSpinner)
                            .addComponent(fromNodeSpinner))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(fromDeviceLabel)
                            .addComponent(toDeviceLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(toDeviceSpinner, 0, 0, Short.MAX_VALUE)
                            .addComponent(fromDeviceSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 37, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 2, Short.MAX_VALUE))
                    .addComponent(payloadTextField))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(toNodeLabel)
                    .addComponent(toNodeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(toDeviceLabel)
                    .addComponent(toDeviceSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fromNodeLabel)
                    .addComponent(fromNodeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fromDeviceLabel)
                    .addComponent(fromDeviceSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(commandComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(commandLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(payloadTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(payloadLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout SendPacketFrameLayout = new javax.swing.GroupLayout(SendPacketFrame.getContentPane());
        SendPacketFrame.getContentPane().setLayout(SendPacketFrameLayout);
        SendPacketFrameLayout.setHorizontalGroup(
            SendPacketFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SendPacketFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SendPacketFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(sendPacketButton)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        SendPacketFrameLayout.setVerticalGroup(
            SendPacketFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SendPacketFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sendPacketButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        SettingsDialog.setTitle("HomeNet Settings");
        SettingsDialog.setMinimumSize(new java.awt.Dimension(400, 300));

        serverLabel.setText("Server:");

        apiKeyLabel.setText("API Key:");

        serverTextField.setEditable(false);
        serverTextField.setText("homenet.me");

        apiKeyTextField.setText("reallylongstringofchars");

        testButton.setText("Test");
        testButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("SSL Cert:");

        certPropertiesLabel.setText("Loading...");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(apiKeyLabel)
                        .addComponent(serverLabel))
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(certPropertiesLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(testButton))
                    .addComponent(apiKeyTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
                    .addComponent(serverTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(serverTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(serverLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(apiKeyTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(apiKeyLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(testButton)
                    .addComponent(jLabel1)
                    .addComponent(certPropertiesLabel))
                .addContainerGap(81, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Client", jPanel4);

        serverPortLabel.setText("Port:");

        serverPortTextField.setEditable(false);
        serverPortTextField.setText("2443");

        enableUPnPCheckBox.setText("Enable UPnP Portforwarding");

        enableServerCheckBox.setSelected(true);
        enableServerCheckBox.setText("Enable Local Server");
        enableServerCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enableServerCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(serverPortLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(serverPortTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(294, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(enableServerCheckBox)
                .addContainerGap(250, Short.MAX_VALUE))
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(enableUPnPCheckBox)
                .addContainerGap(208, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(enableServerCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(serverPortTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(serverPortLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(enableUPnPCheckBox)
                .addContainerGap(77, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Server", jPanel5);

        settingsSaveButton.setText("Save");
        settingsSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsSaveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout SettingsDialogLayout = new javax.swing.GroupLayout(SettingsDialog.getContentPane());
        SettingsDialog.getContentPane().setLayout(SettingsDialogLayout);
        SettingsDialogLayout.setHorizontalGroup(
            SettingsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SettingsDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SettingsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(settingsSaveButton, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE))
                .addContainerGap())
        );
        SettingsDialogLayout.setVerticalGroup(
            SettingsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SettingsDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(settingsSaveButton)
                .addContainerGap())
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName("Server");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("HomeNet.me Desktop App");

        cardPanel.setLayout(new java.awt.CardLayout());

        mainPanel.setLayout(new javax.swing.BoxLayout(mainPanel, javax.swing.BoxLayout.Y_AXIS));

        jSplitPane2.setDividerLocation(200);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jLabel9.setText("Packets Received:");

        packetTextArea.setColumns(20);
        packetTextArea.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        packetTextArea.setRows(5);
        jScrollPane2.setViewportView(packetTextArea);

        jList1.setModel(packetListModel);
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.setCellRenderer(new PacketListRenderer());
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(jList1);

        autoUpdateToggleButton.setSelected(true);
        autoUpdateToggleButton.setText("Auto Update");
        autoUpdateToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoUpdateToggleButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(autoUpdateToggleButton)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(autoUpdateToggleButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE))
                .addContainerGap())
        );

        jSplitPane2.setTopComponent(jPanel2);

        consoleTextPane.setBackground(new java.awt.Color(0, 0, 0));
        consoleTextPane.setEditable(false);
        consoleTextPane.setFont(new java.awt.Font("Consolas", 0, 10)); // NOI18N
        consoleTextPane.setForeground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setViewportView(consoleTextPane);

        jSplitPane2.setRightComponent(jScrollPane1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 530, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 320, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE))
        );

        mainPanel.add(jPanel3);

        statusPanel.setBackground(new java.awt.Color(204, 204, 204));
        statusPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel7.setText("Status:");

        jLabel8.setText("Connected");

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addContainerGap(427, Short.MAX_VALUE))
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel8))
        );

        mainPanel.add(statusPanel);

        cardPanel.add(mainPanel, "card2");

        topMenuBar.setVisible(false);

        menuFile.setMnemonic('f');
        menuFile.setText("File");

        jMenuItem3.setMnemonic('e');
        jMenuItem3.setText("Exit");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        menuFile.add(jMenuItem3);

        topMenuBar.add(menuFile);

        menuTools.setMnemonic('t');
        menuTools.setText("Tools");

        menuToolsSendPacket.setMnemonic('p');
        menuToolsSendPacket.setText("Send Packet");
        menuToolsSendPacket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuToolsSendPacketActionPerformed(evt);
            }
        });
        menuTools.add(menuToolsSendPacket);

        menuToolsSettings.setMnemonic('s');
        menuToolsSettings.setText("Settings");
        menuToolsSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuToolsSettingsActionPerformed(evt);
            }
        });
        menuTools.add(menuToolsSettings);

        menuSerialPorts.setMnemonic('e');
        menuSerialPorts.setText("Select Ports");
        menuSerialPorts.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }
            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }
            public void menuSelected(javax.swing.event.MenuEvent evt) {
                menuSerialPortsMenuSelected(evt);
            }
        });
        menuSerialPorts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSerialPortsActionPerformed(evt);
            }
        });
        menuTools.add(menuSerialPorts);

        topMenuBar.add(menuTools);

        menuHelp.setMnemonic('h');
        menuHelp.setText("Help");

        menuHelpOnline.setText("Online Help");
        menuHelpOnline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuHelpOnlineActionPerformed(evt);
            }
        });
        menuHelp.add(menuHelpOnline);

        menuHelpAbout.setText("About");
        menuHelpAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuHelpAboutActionPerformed(evt);
            }
        });
        menuHelp.add(menuHelpAbout);

        topMenuBar.add(menuHelp);

        setJMenuBar(topMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        exit();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void menuHelpAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuHelpAboutActionPerformed
        // new jDialog1();
        // AboutDialog.setVisible(true);
        JOptionPane.showMessageDialog(null, "<html>Developed by Matthew Doll<br>Licenced Under GPLv3<br>For more info visit HomeNet.me</html>", "About", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_menuHelpAboutActionPerformed

    private void menuToolsSendPacketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuToolsSendPacketActionPerformed
        SendPacketFrame.setVisible(true);
    }//GEN-LAST:event_menuToolsSendPacketActionPerformed

    private void sendPacketButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendPacketButtonActionPerformed
        
       // homenetapp.homenet.addUdpPacket((int) fromDeviceSpinner.getValue(), (int) toNodeSpinner.getValue(), 
       //         (int) toDeviceSpinner.getValue(), 2, new Payload("test"));
        
     //   int fromDevice = ;
        
        
        homenet.Packet p = homenetapp.homenet.addUdpPacket((Integer) fromDeviceSpinner.getValue(), (Integer) toNodeSpinner.getValue(), 
                (Integer) toDeviceSpinner.getValue(), (Integer)commandComboBox.getSelectedItem(), new homenet.Payload("test"));
        
        displayPacket(p);
        

    }//GEN-LAST:event_sendPacketButtonActionPerformed

    private void menuHelpOnlineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuHelpOnlineActionPerformed

        if (!java.awt.Desktop.isDesktopSupported()) {
            System.err.println("Desktop is not supported (fatal)");
          //  System.exit(1);
        }

        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

        if (!desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
            System.err.println("Desktop doesn't support the browse action (fatal)");
           // System.exit(1);
        }

        try {
            java.net.URI uri = new java.net.URI("http://homenet.me");
            desktop.browse(uri);
        } catch (Exception e) {

            System.err.println(e.getMessage());
        }
    }//GEN-LAST:event_menuHelpOnlineActionPerformed

    private void loadSettings() {
        serverTextField.setText(homenetapp.config.getString("client.server"));
        apiKeyTextField.setText(homenetapp.config.getString("client.apikey"));
        enableServerCheckBox.setSelected(homenetapp.config.getBoolean("server.enabled")); 
        enableUPnPCheckBox.setSelected(homenetapp.config.getBoolean("server.upnp")); 
        serverPortTextField.setText(homenetapp.config.getString("server.port"));
        
        if(!homenetapp.config.getBoolean("server.enabled")){
            enableUPnPCheckBox.setEnabled(false);
            serverPortLabel.setEnabled(false);
            serverPortTextField.setEnabled(false);
        }
        
        //send packet
        toNodeSpinner.setValue(homenetapp.config.getInt("packet.tonode"));
        toDeviceSpinner.setValue(homenetapp.config.getInt("packet.todevice"));
        fromNodeSpinner.setValue(homenetapp.config.getInt("packet.fromnode"));
        fromDeviceSpinner.setValue(homenetapp.config.getInt("packet.fromdevice"));
        commandComboBox.setSelectedIndex(homenetapp.config.getInt("packet.command"));
        payloadTextField.setText(homenetapp.config.getString("packet.payload"));
        
        
        
    }
    
    

    class CommandRenderer extends BasicComboBoxRenderer {

        public java.awt.Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            Integer item = (Integer) value;
            if (value != null) {
                // Integer item = (Integer) value;
                setText(homenetapp.commands.get(item)[1]);
                return this;
            }

            if (index == -1) {
                //  setText("Select Command");
                // Item item = (Item) value;
                //  setText("index -1");// + item.getId()
                //  System.out.println(homenetapp.commands.get(item)[1]);
            }


            return this;
        }
    }
    


    private void SetupMenuSerialPorts() {
//        System.out.println("SetupSerialMenuPorts");
//        List<String> savedPorts = homenetapp.config.getList("ports.serial");
//
//        ArrayList<String> ports = homenet.Serial.listPorts();
//        for(String element : ports){
//            System.out.println("String Setup: "+element);
//            System.out.println(element);
//            javax.swing.JCheckBoxMenuItem serialPortCheckBox = new javax.swing.JCheckBoxMenuItem(element);
//            if (savedPorts.contains(element)) {
//                serialPortCheckBox.setSelected(true);
//                //serialPortCheckBox.
//            }
//            serialPortCheckBox.addActionListener(new java.awt.event.ActionListener() {
//
//                public void actionPerformed(java.awt.event.ActionEvent evt) {
//                    menuSerialPortCheckBoxAction(evt);
//                }
//            });
//
//            // serialPortCheckBox.setMnemonic(java.awt.event.KeyEvent.VK_1);
//            menuSerialPorts.add(serialPortCheckBox);
//        }
    }



    private void menuSerialPortsMenuSelected(javax.swing.event.MenuEvent evt) {//GEN-FIRST:event_menuSerialPortsMenuSelected
        System.out.println("Get Ports");        
        for(String s : homenetapp.serialmanager.portList){
            System.out.println("Port: "+s);
        }
        
        //javax.swing.JCheckBoxMenuItem cbMenuItem = new javax.swing.JCheckBoxMenuItem("A check box menu item");
        //cbMenuItem.setMnemonic(KeyEvent.VK_C);
        //menuSerialPorts.add(cbMenuItem);


        //decide which ones to add

        //decide which ones to remove



    }//GEN-LAST:event_menuSerialPortsMenuSelected

    private void menuToolsSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuToolsSettingsActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                checkClientCert();
            }
        });

        SettingsDialog.setVisible(true);
    }//GEN-LAST:event_menuToolsSettingsActionPerformed

    public void saveConfig(){
         //save properties
        //page1
        homenetapp.config.setProperty("client.server", serverTextField.getText());
        homenetapp.config.setProperty("client.apikey", apiKeyTextField.getText());
        homenetapp.config.setProperty("server.enabled",enableServerCheckBox.isSelected()); 
        homenetapp.config.setProperty("server.upnp",enableUPnPCheckBox.isSelected()); 
        homenetapp.config.setProperty("server.port",serverPortTextField.getText()); 
        
        homenetapp.config.setProperty("packet.tonode",toNodeSpinner.getValue());
        homenetapp.config.setProperty("packet.todevice",toDeviceSpinner.getValue());
        homenetapp.config.setProperty("packet.fromnode",fromNodeSpinner.getValue());
        homenetapp.config.setProperty("packet.fromdevice",fromDeviceSpinner.getValue());
        homenetapp.config.setProperty("packet.command",commandComboBox.getSelectedIndex());
        homenetapp.config.setProperty("packet.payload",payloadTextField.getText());
        
        try {
            homenetapp.config.save();
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Could not save config", "Error", javax.swing.JOptionPane.ERROR_MESSAGE); 
                            
        }
    }
    
    
    
    private void settingsSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsSaveButtonActionPerformed
       
        saveConfig();
        SettingsDialog.setVisible(false);
    }//GEN-LAST:event_settingsSaveButtonActionPerformed

    private void enableServerCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enableServerCheckBoxActionPerformed
        javax.swing.JCheckBox checkbox = (javax.swing.JCheckBox) evt.getSource();
        if (checkbox.isSelected()) {
            enableUPnPCheckBox.setEnabled(true);
            serverPortLabel.setEnabled(true);
            serverPortTextField.setEnabled(true);
            System.out.println("Selected");
        } else {
            enableUPnPCheckBox.setEnabled(false);
            serverPortLabel.setEnabled(false);
            serverPortTextField.setEnabled(false);
            System.out.println("Not Selected");
        }
}//GEN-LAST:event_enableServerCheckBoxActionPerformed

    private void testButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_testButtonActionPerformed
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                try {
                    //

                    XmlrpcClient xmlrpcClient = new XmlrpcClient("homenet.me", apiKeyTextField.getText());
                    String reply = (String) xmlrpcClient.execute("HomeNet.validateApikey", apiKeyTextField.getText());

                    if (!reply.equals("true")) {
                        JOptionPane.showMessageDialog(null, reply, "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }







                    //             KeyStore mykeystore = KeyStore.getInstance("JKS");
                    //
                    //            java.security.KeyFactory rSAKeyFactory = java.security.KeyFactory.getInstance("RSA");
                    //
                    //            java.security.PublicKey pk = xc.getPublicKey();;
                    //
                    //            java.security.spec.PKCS8EncodedKeySpec keySpec = new
                    //            java.security.spec.PKCS8EncodedKeySpec(pk.getEncoded());
                    //            java.security.KeyFactory keyFactory = null;
                    //            keyFactory = keyFactory.getInstance("RSA");
                    //            java.security.PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
                    //
                    ////            mykeystore.setEntry("HomeNet",
                    ////                    new KeyStore.PrivateKeyEntry(privateKey, certs),
                    ////                    new KeyStore.PasswordProtection("keypassword".toCharArray()));
                    //
                    //             FileOutputStream sigfos = new FileOutputStream("test2.txt");
                    //
                    //
                    //             mykeystore.setKeyEntry("HomeNet", privateKey.getEncoded(), certs);
                    //             mykeystore.store(sigfos, "mysecret".toCharArray());
                    //             sigfos.close();



                    //if(URLConnection.class.getName() == )
                    //conn.
                } catch (Exception e) {

                    certPropertiesLabel.setText("Invalid Server");
                    // System.out.println(e.getMessage());
                    e.printStackTrace();
                }

            }
        });
        //
        //// Sending information through HTTPS: POST
        //OutputStream ostream = conn.getOutputStream();
        //ostream.write(....);
        //......
        //ostream.close();
        //
    }//GEN-LAST:event_testButtonActionPerformed

    private void menuSerialPortsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSerialPortsActionPerformed
        // Update Serial Port List
    }//GEN-LAST:event_menuSerialPortsActionPerformed

    private void autoUpdateToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoUpdateToggleButtonActionPerformed
        //if selected
         javax.swing.JToggleButton button = ( javax.swing.JToggleButton)evt.getSource();
         if(button.isSelected()){
             System.out.println("Button Toggled");
         } else {
             System.out.println("Button UnToggled");
         }
        //else
    }//GEN-LAST:event_autoUpdateToggleButtonActionPerformed

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
        int index = evt.getFirstIndex();
        JList j = (JList) evt.getSource();
        homenet.Packet p = (homenet.Packet) j.getSelectedValue();
        
        //homenet.Packet p = (homenet.Packet)packetListModel.getElementAt(index);
         displayPacket(p);
    }//GEN-LAST:event_jList1ValueChanged

    class NodeVerifier extends javax.swing.InputVerifier {

        public boolean verify(javax.swing.JComponent input) {
            javax.swing.JTextField tf = (javax.swing.JTextField) input;
            int value = Integer.parseInt(tf.getText());

            if (value >= 0 && value < 16) {
                return true;
            }
            return false;
        }
    }

    private void updateTextPane(final String text) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                javax.swing.text.Document doc = consoleTextPane.getDocument();
                try {
                    doc.insertString(doc.getLength(), text, null);
                } catch (javax.swing.text.BadLocationException e) {
                    throw new RuntimeException(e);
                }
                consoleTextPane.setCaretPosition(doc.getLength() - 1);
            }
        });
    }

    private void redirectSystemStreams() {
        OutputStream out = new OutputStream() {

            @Override
            public void write(final int b) throws IOException {
                updateTextPane(String.valueOf((char) b));
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                updateTextPane(new String(b, off, len));
            }

            @Override
            public void write(byte[] b) throws IOException {
                write(b, 0, b.length);
            }
        };

        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
    }

    public void addPacketToList(homenet.Packet packet) {
        System.out.println("Adding Packet to GUI");
       // packetList.addLast(packet);
        packetListModel.addElement(packet);
  
        if(packetListModel.size() > 10){
           // packetList.removeFirst();
            packetListModel.remove(0);
        }
        //    jList1.validate();
        
        if(autoUpdateToggleButton.isSelected()){
            jList1.setSelectedValue(packet, true);
        }
       // jScrollPane3.
    }
    
    
    
//    class PacketListModel extends DefaultListModel {
//         //   String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
//            public int getSize() { return packetList.size(); }
//            public Object getElementAt(int i) { return packetList.get(i); }
//    }
    
    class PacketListRenderer extends DefaultListCellRenderer {

        public java.awt.Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    System.out.println("PacketListRenderer");
            if (value != null) {
                homenet.Packet p = (homenet.Packet) value;
                setText(p.getFromPort()+"->"+p.getToPort()+" "+p.getFromNode()+":"+p.getFromDevice()+ ">"+p.getToNode()+":"+p.getToDevice()+" "+p.getId());
                return this;
            }

            if (index == -1) {
                //  setText("Select Command");
                // Item item = (Item) value;
                //  setText("index -1");// + item.getId()
                //  System.out.println(homenetapp.commands.get(item)[1]);
            }
            return this;
        }
    }
    
    private void displayPacket(homenet.Packet packet){
        
    String message = "";
    String message2 = "";
    for(int i=0; i < packet.getPayloadLength(); i++){
      message += (int)packet.getPayloadAt(i);
      message += ",";
      message2 += (char)packet.getPayloadAt(i);
    } 
    
    //lookup command;
   String c =  ""+packet.getCommand();
    
   if(homenetapp.commands.containsKey(packet.getCommand()) == true){
    // String[] c = commandMap.get(int(packet[4]));
   //  println(c);
     c = homenetapp.commands.get(packet.getCommand())[1];

   }
          packetTextArea.setText(
    "  Received: " + packet.getTimestamp() + "\n" +
    "  fromPort: " + packet.getFromPort() + "\n" +
    "    toPort: " + packet.getToPort() + "\n" +
    "    status: " + packet.getStatus() + "\n" +
    "    length: " + packet.getLength() + "\n" +
    "  settings: " + packet.getSettings() + "\n" +
    "      type: " + packet.getType() + "\n" +
    "  fromNode: " + packet.getFromNode() + "\n" +
    "fromDevice: " + packet.getFromDevice() + "\n" +
    "    toNode: " + packet.getToNode() + "\n" +
    "  toDevice: " + packet.getToDevice() + "\n" +
    //"       ttl: " + packet.getTtl() + "\n" +
    "        id: " + packet.getId() + "\n" +
    "   command: " + c  + "\n" +
    "   payload: " + message + "\n" +
    "   payload: " + message2 + "\n" +
    "  checksum: " + packet.getChecksum());
    }
    
    private void checkClientCert() {
        try {
            URL url = new URL("https://mail.google.com/");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.connect();
            Certificate[] certs = conn.getServerCertificates();

            //System.out.println("Cert Chain Length: "+certs.length);

            Certificate c = certs[0];
            X509Certificate xc = (X509Certificate) c;

            String[] from = homenetapp.splitTokens(xc.getIssuerX500Principal().getName(), "=, ");
            String[] to = homenetapp.splitTokens(xc.getSubjectX500Principal().getName(), "=, ");

            certPropertiesLabel.setText("<html>Issued by: " + from[1] + "<br>For: " + to[1] + "<br>Expires: " + xc.getNotAfter() + "</html>");


            System.out.println("Cert: " + c.getType());

            System.out.println("Not After: " + xc.getNotAfter());
            System.out.println("Subject DN: " + xc.getSubjectX500Principal());
            System.out.println("Issuer DN: " + xc.getIssuerX500Principal());
            System.out.println("getSigAlgName: " + xc.getSigAlgName());

        } catch (Exception e) {
            certPropertiesLabel.setText("Failed Load Certficate");
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new HomeNetAppGui().setVisible(true);

            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFrame SendPacketFrame;
    private javax.swing.JDialog SettingsDialog;
    private javax.swing.JLabel apiKeyLabel;
    private javax.swing.JTextField apiKeyTextField;
    private javax.swing.JToggleButton autoUpdateToggleButton;
    private javax.swing.JPanel cardPanel;
    private javax.swing.JLabel certPropertiesLabel;
    private javax.swing.JComboBox commandComboBox;
    private javax.swing.JLabel commandLabel;
    private javax.swing.JTextPane consoleTextPane;
    private javax.swing.JCheckBox enableServerCheckBox;
    private javax.swing.JCheckBox enableUPnPCheckBox;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel fromDeviceLabel;
    private javax.swing.JSpinner fromDeviceSpinner;
    private javax.swing.JLabel fromNodeLabel;
    private javax.swing.JSpinner fromNodeSpinner;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenu menuHelp;
    private javax.swing.JMenuItem menuHelpAbout;
    private javax.swing.JMenuItem menuHelpOnline;
    private javax.swing.JMenu menuSerialPorts;
    private javax.swing.JMenu menuTools;
    private javax.swing.JMenuItem menuToolsSendPacket;
    private javax.swing.JMenuItem menuToolsSettings;
    private javax.swing.JTextArea packetTextArea;
    private javax.swing.JLabel payloadLabel;
    private javax.swing.JTextField payloadTextField;
    private javax.swing.JButton sendPacketButton;
    private javax.swing.JLabel serverLabel;
    private javax.swing.JLabel serverPortLabel;
    private javax.swing.JTextField serverPortTextField;
    private javax.swing.JTextField serverTextField;
    private javax.swing.JButton settingsSaveButton;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JButton testButton;
    private javax.swing.JLabel toDeviceLabel;
    private javax.swing.JSpinner toDeviceSpinner;
    private javax.swing.JLabel toNodeLabel;
    private javax.swing.JSpinner toNodeSpinner;
    private javax.swing.JMenuBar topMenuBar;
    // End of variables declaration//GEN-END:variables
    //hash map ports
    private DefaultListModel packetListModel;
//selected ports    
}
