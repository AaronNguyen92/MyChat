import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket; 
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;


public class Server extends javax.swing.JFrame {
    ServerSocket ss;
    HashMap clientcoll = new HashMap();
    /**
     * Creates new form Server
     */
    public Server() {
        try {
            initComponents();
            ss = new ServerSocket(2089);
            this.sStatus.setText("Server Started.");
            new ClientAccept().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   
    class ClientAccept extends Thread{
        public void run(){
            while(true){
                try {
                    Socket s = ss.accept();
                                      
                    String i1 = new DataInputStream(s.getInputStream()).readUTF();
                    if(clientcoll.containsKey(i1)){
                        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                        dout.writeUTF("You Are Already Registered ....!!");
                    }
                    else{
                        clientcoll.put(i1,s);
                        msgBox.append(i1 + " Joined!\n");
                        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                        dout.writeUTF("");
                        new MsgRead(s, i1).start();
                        new PrepareClientList().start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    class MsgRead extends Thread{
        Socket s;
        String ID;
        
        MsgRead(Socket s, String ID) {
            this.s = s;
            this.ID = ID;
        }
        
        public void run(){
            while(!clientcoll.isEmpty()){
                try {
                    String i = new DataInputStream(s.getInputStream()).readUTF();
                    if(i.equals("hjgfshudssbjj89098AFCB")){
                        clientcoll.remove(ID);
                        msgBox.append(ID + ": removed!\n");
                        new PrepareClientList().start();
                        
                        Set<String> k = clientcoll.keySet();
                        Iterator itr = k.iterator();
                        while(itr.hasNext()){
                            String key = (String)itr.next();
                            if(!key.equalsIgnoreCase(ID)){
                                try {
                                    new DataOutputStream(((Socket)clientcoll.get(key)).getOutputStream()).writeUTF(ID + " " + i);
                                } catch (Exception e) {
                                    clientcoll.remove(key);
                                    msgBox.append(key + ": removed!\n");
                                    new PrepareClientList().start();
                                }
                            }
                        } 
                    }
                    else if(i.contains("#434556@@@@@554999@@")){
                        i = i.substring(20);
                        StringTokenizer st = new StringTokenizer(i,":");
                        String id = st.nextToken();
                        i = st.nextToken();
                        try {
                            new DataOutputStream(((Socket)clientcoll.get(id)).getOutputStream()).writeUTF("< "+ID + " to "+id+" > " + i);
                        } catch (Exception e) {
                            clientcoll.remove(id);
                            msgBox.append(id + ": removed!\n");
                            new PrepareClientList().start();
                        }
                    }
                    else{
                        Set k = clientcoll.keySet();
                        Iterator itr = k.iterator();
                        while(itr.hasNext()){
                            String key = (String)itr.next();
                            if(!key.equalsIgnoreCase(ID)){
                                try {
                                    new DataOutputStream(((Socket)clientcoll.get(key)).getOutputStream()).writeUTF("< "+ID + " to All > " + i);
                                } catch (Exception e) {
                                    clientcoll.remove(key);
                                    msgBox.append(key + ": removed!\n");
                                    new PrepareClientList().start();
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    class PrepareClientList extends Thread{
        public void run(){
            try {
                String ids = "";
                Set k = clientcoll.keySet();
                Iterator itr = k.iterator();
                while(itr.hasNext()){
                    String key = (String)itr.next();
                    ids += key +",";
                }
                if(ids.length()!= 0){
                    ids = ids.substring(0, ids.length()-1);
                    itr = k.iterator();
                    while(itr.hasNext()){
                        String key = (String)itr.next();
                        try {
                            new DataOutputStream(((Socket)clientcoll.get(key)).getOutputStream()).writeUTF(":;.,/=" + ids);
                        } catch (Exception e) {
                            clientcoll.remove(key);
                            msgBox.append(key + ": removed!\n");
                        }
                    }
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        msgBox = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        sStatus = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("My Server");

        jPanel1.setBackground(new java.awt.Color(204, 255, 255));

        msgBox.setColumns(20);
        msgBox.setRows(5);
        jScrollPane1.setViewportView(msgBox);

        jLabel1.setBackground(new java.awt.Color(204, 204, 0));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel1.setText("Server Status:");

        sStatus.setText(".............................");

        jLabel2.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jLabel2.setText("Log of clients' status:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
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
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Server().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea msgBox;
    private javax.swing.JLabel sStatus;
    // End of variables declaration//GEN-END:variables
}
