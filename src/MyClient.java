import java.awt.*;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.*;

public class MyClient extends javax.swing.JFrame {

    String iD, clientID = "";
    DataInputStream din;
    DataOutputStream dout;
    DefaultListModel dlm, dlm_removed, empty;
    static Box vertical = Box.createVerticalBox();
    ImageIcon user_login_image;
    String string_remove = "";

    /**
     * Creates new form MyClient
     */
    public MyClient() {
        initComponents();

    }

    MyClient(String i, Socket s, ImageIcon img) {
        iD = i;
        try {
            initComponents();

            user_login_image = img;
            ImageIcon img60x60 = new ImageIcon(img.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
            jLabel_avatar1.setIcon(img60x60);
            jLabel_avatar1.setSize(70, 70);
            dlm = new DefaultListModel();
            idlabel.setText(i);

            din = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());

            new Read().start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    //Tạo luồng để đọc tin nhắn từ server
    class Read extends Thread {

        public void run() {

            while (true) {
                try {
                    //Đọc tin nhắn
                    String m = din.readUTF();
                    if (m.contains(":;.,/=")) {
                        m = m.substring(6);
                        dlm.clear();
                        //Tách chuỗi ra từng phần với dấu phân cách (",")
                        StringTokenizer st = new StringTokenizer(m, ",");
                        while (st.hasMoreTokens()) {
                            String u = st.nextToken();
                            if (!iD.equals(u)) {
                                //Sửa lại đường dẫn cho phù hợp với đường dẫn project trên máy tính của bạn
                                ImageIcon img_selected_show = new ImageIcon("C:\\Users\\DELL\\Documents\\NetBeansProjects\\MyChat\\src\\avatarUser\\" + u + ".png");
                                ImageIcon img50x50_selected_show = new ImageIcon(img_selected_show.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
                                dlm.addElement(new ImgsNText(u, img50x50_selected_show));

                            }

                        }
                        dlm_removed = dlm;
                        //Tạo đối tượng Renderer mới và đặt lại nó trong UL để nó hiển thị các thành phần của UL
                        UL.setCellRenderer(new Renderer());
                        UL.setModel(dlm);
                        //tính toán lại bố cục các thành phần của UL và vẽ nó lại
                        UL.revalidate();
                        UL.repaint();

                    }
                    //Nhận thông báo rồi khỏi nhóm chat
                    else if (m.contains("hjgfshudssbjj89098AFCB")) {
                        String u1 = m.substring(0, m.indexOf("hjgfshudssbjj89098AFCB") - 1);
                        jLabel_avatar2.setIcon(null);
                        idlabel_user_selected.setText("");
                        idlabel_online_status.setText("");
                        int id_remove = 0;
                        for (int i = dlm.getSize() - 1; i >= 0; i--) {
                            String name_index = ((ImgsNText) dlm.getElementAt(i)).getName();
                            if (name_index.equalsIgnoreCase(u1)) {
                                id_remove = i;
                            }
                        }
                        dlm_removed.removeElementAt(id_remove);
                        UL.removeAll();
                        UL.setCellRenderer(new Renderer());
                        UL.setModel(dlm_removed);
                        UL.revalidate();
                        UL.repaint();
                    } else {
                        string_remove = "";
                        JPanel p6 = new JPanel();
                        p6.setBackground(Color.WHITE);
                        p6.setLayout(new FlowLayout(FlowLayout.LEFT));
                        JPanel p7 = new JPanel();
                        p7.setLayout(new BoxLayout(p7, BoxLayout.Y_AXIS));
                        String str1 = m.substring(m.indexOf("> ") + 1) + "\n";//chuỗi tin nhắn
                        String str2 = m.substring(2, m.indexOf("to") - 1);//username của đối tượng nhận
                        JLabel lbl9 = new JLabel();
                        if (str1.length() > 50) {
                            lbl9.setText("<html><p style= \"width: 200px\" >" + str1 + "</p></html>");
                        } else {
                            lbl9.setText(str1);
                        }
                        JLabel lbl10 = new JLabel();
                        lbl10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                        ImageIcon img_selected_respond = new javax.swing.ImageIcon(getClass().getResource("/avatarUser/" + str2 + ".png"));
                        ImageIcon img40x40_respond = new ImageIcon(img_selected_respond.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
                        lbl10.setIcon(img40x40_respond);
                        lbl10.setSize(50, 50);
                        lbl9.setBackground(new Color(153, 255, 153));
                        lbl9.setOpaque(true);
                        lbl9.setForeground(Color.BLACK);
                        lbl9.setBorder(new EmptyBorder(10, 10, 3, 20));
                        //Hiển thị thời gian trong ô tin nhắn
                        Calendar cal1 = Calendar.getInstance();
                        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
                        JLabel lbl8 = new JLabel();
                        lbl8.setText(sdf1.format(cal1.getTime()));
                        lbl8.setBorder(new EmptyBorder(5, 5, 5, 5));
                        p7.setBackground(new Color(153, 255, 153));
                        p6.add(lbl10);
                        p7.add(lbl9);
                        p7.add(lbl8);
                        p6.add(p7);
                        JPanel left1 = new JPanel(new BorderLayout());
                        left1.setBackground(Color.white);
                        left1.add(p6, BorderLayout.LINE_START);
                        vertical.add(left1);
                        panelShowMSG.add(vertical);
                        panelShowMSG.revalidate();
                        panelShowMSG.repaint();
                    }

                } catch (Exception e) {
                    break;
                    //e.printStackTrace();
                }
            }
        }
    }

    //Khai báo lớp ImgsNText bao gồm cả ảnh và Text
    public class ImgsNText {

        private String name;
        private Icon img;

        public ImgsNText(String name, Icon img) {
            this.name = name;
            this.img = img;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Icon getImg() {
            return img;
        }

        public void setImg(Icon img) {
            this.img = img;
        }
    }
    
    //Cài đặt lại đối tượng Renderer để hiển thị nội dung bao gồm Text và ảnh bên trong 1 UL
    //Ghi đè lại phương thức getListCellRendererComponent để hiển thị theo mong muốn
    public class Renderer extends DefaultListCellRenderer implements ListCellRenderer<Object> {

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            //return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus); //To change body of generated methods, choose Tools | Templates.

            ImgsNText is = (ImgsNText) value;
            setText(is.getName());
            setIcon(is.getImg());

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            setEnabled(true);
            setFont(list.getFont());
            setBorder(BorderFactory.createRaisedBevelBorder());

            return this;
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        sendButton = new javax.swing.JButton();
        sendText = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        UL = new javax.swing.JList();
        jPanel2 = new javax.swing.JPanel();
        jLabel_avatar1 = new javax.swing.JLabel();
        idlabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel_avatar2 = new javax.swing.JLabel();
        idlabel_online_status = new javax.swing.JLabel();
        idlabel_user_selected = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        panelShowMSG = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));

        sendButton.setBackground(new java.awt.Color(255, 255, 255));
        sendButton.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        sendButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/send (1).png"))); // NOI18N
        sendButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        sendButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });

        sendText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendTextActionPerformed(evt);
            }
        });

        UL.setSelectionForeground(new java.awt.Color(0, 0, 0));
        UL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ULMouseClicked(evt);
            }
        });
        UL.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                ULValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(UL);

        jPanel2.setBackground(new java.awt.Color(0, 0, 153));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel_avatar1.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jLabel_avatar1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_avatar1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_avatar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons8-user-70.png"))); // NOI18N
        jPanel2.add(jLabel_avatar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 60, 60));

        idlabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        idlabel.setForeground(new java.awt.Color(255, 255, 255));
        idlabel.setText("......................");
        jPanel2.add(idlabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, 106, 30));

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel2.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 0, 10, 80));

        jLabel_avatar2.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jLabel_avatar2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_avatar2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel2.add(jLabel_avatar2, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 10, 70, 60));

        idlabel_online_status.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        idlabel_online_status.setForeground(new java.awt.Color(102, 255, 102));
        jPanel2.add(idlabel_online_status, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 50, 130, 20));

        idlabel_user_selected.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        idlabel_user_selected.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(idlabel_user_selected, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 20, 130, 20));

        panelShowMSG.setBackground(new java.awt.Color(255, 255, 255));
        panelShowMSG.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelShowMSG.setAutoscrolls(true);
        panelShowMSG.setLayout(new javax.swing.BoxLayout(panelShowMSG, javax.swing.BoxLayout.LINE_AXIS));
        jScrollPane1.setViewportView(panelShowMSG);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Chat Room");
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(sendText, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(sendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sendText, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sendButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 517, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>                        


    private void sendTextActionPerformed(java.awt.event.ActionEvent evt) {                                         

    }                                        

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {                                           
        try {
            String m = sendText.getText(), mm = m;
            String CI = clientID;

            m = "#434556@@@@@554999@@" + CI + ":" + mm;
            dout.writeUTF(m);
            sendText.setText("");

            JPanel p3 = new JPanel();
            p3.setBackground(Color.WHITE);
            p3.setLayout(new FlowLayout(FlowLayout.LEFT));
            JPanel p4 = new JPanel();
            p4.setLayout(new BoxLayout(p4, BoxLayout.Y_AXIS));
            String str3 = "" + mm + "\n";
            JLabel lbl5 = new JLabel();
            if (str3.length() > 50) {
                lbl5.setText("<html><p style= \"width: 200px\" >" + str3 + "</p></html>");
            } else {
                lbl5.setText(str3);
            }
            JLabel lbl6 = new JLabel();
            lbl6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            ImageIcon img40x40_chat = new ImageIcon(user_login_image.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
            lbl6.setIcon(img40x40_chat);
            lbl6.setSize(50, 50);
            lbl5.setBackground(new Color(0, 153, 51));
            lbl5.setOpaque(true);
            lbl5.setForeground(Color.white);
            lbl5.setBorder(new EmptyBorder(10, 10, 3, 20));
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            JLabel lbl7 = new JLabel();
            lbl7.setText(sdf.format(cal.getTime()));
            lbl7.setBorder(new EmptyBorder(5, 5, 5, 5));
            lbl7.setForeground(Color.white);
            p4.setBackground(new Color(0, 153, 51));
            p4.add(lbl5);
            p4.add(lbl7);
            p3.add(p4);
            p3.add(lbl6);
            JPanel right = new JPanel(new BorderLayout());
            right.setBackground(Color.WHITE);
            right.add(p3, BorderLayout.LINE_END);
            vertical.add(right);
            panelShowMSG.add(vertical, BorderLayout.PAGE_START);
            panelShowMSG.revalidate();
            panelShowMSG.repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "User doesn't exist anymore!");
        }
    }                                          

    private void ULValueChanged(javax.swing.event.ListSelectionEvent evt) {                                
        clientID = ((ImgsNText) (UL.getSelectedValue())).getName();

        idlabel_user_selected.setText(clientID);
        ImageIcon img_selected = (ImageIcon) ((ImgsNText) (UL.getSelectedValue())).getImg();
        ImageIcon img60x60_online = new ImageIcon(img_selected.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
        jLabel_avatar2.setIcon(img60x60_online);
        jLabel_avatar2.setSize(70, 70);

        idlabel_online_status.setText("online");

    }                               

    private void formWindowClosing(java.awt.event.WindowEvent evt) {                                   
        String i = "hjgfshudssbjj89098AFCB";

        try {
            dout.writeUTF(i);
            this.dispose();
        } catch (IOException e) {
            Logger.getLogger(MyClient.class.getName()).log(Level.SEVERE, null, e);
        }
    }                                  

    private void ULMouseClicked(java.awt.event.MouseEvent evt) {                                

    }                               

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify                     
    private javax.swing.JList UL;
    private javax.swing.JLabel idlabel;
    private javax.swing.JLabel idlabel_online_status;
    private javax.swing.JLabel idlabel_user_selected;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel_avatar1;
    private javax.swing.JLabel jLabel_avatar2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel panelShowMSG;
    private javax.swing.JButton sendButton;
    private javax.swing.JTextField sendText;
    // End of variables declaration                   
}
