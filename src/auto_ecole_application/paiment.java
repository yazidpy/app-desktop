/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auto_ecole_application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author yazid
 */
public class paiment extends javax.swing.JFrame {

    Candidat c1 = new Candidat();
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    DefaultTableModel model;
    /**
     * Creates new form paiment
     */
    public paiment() {
        initComponents();
        label_id_candidat.setVisible(false);
        label_id_formation.setVisible(false);
        selectionner_les_payment();
    }
    public void afficher_sur_input(Object nom, Object prenom, Object id) {
        label_id_candidat.setText(id.toString());
        inp1.setText(nom.toString());
        inp2.setText(prenom.toString());
        try {
            con = c1.connection_db();
            PreparedStatement st = con.prepareStatement("SELECT formation_id from candidat_inscrie_formation where candidat_id_candidat=?");
            st.setString(1, label_id_candidat.getText());
            rs = st.executeQuery();
            if (rs.next()) {
                String formation = rs.getString("formation_id");
                label_id_formation.setText(formation);
                PreparedStatement st2 = con.prepareStatement("select permis from formation where id_formation=?");
                st2.setString(1, formation);
                rs = st2.executeQuery();
                if (rs.next()) {
                    input_formation.setText(rs.getString("permis"));
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "" + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean valider_formulaire() {
        boolean valide = true;
        if (input_montant.getText().isEmpty() || inp1.getText().isEmpty() || inp2.getText().isEmpty() || input_formation.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vous devez remplir tout les champs !", null, JOptionPane.ERROR_MESSAGE);
            valide = false;
        } else {
            if (!c1.est_chiffre(input_montant.getText())) {
                JOptionPane.showMessageDialog(null, "Le champs montant doit être un chiffre !", null, JOptionPane.ERROR_MESSAGE);
                valide = false;
            }
        }
        return valide;
    }
    public void inserer_payment() {
        if (valider_formulaire() == true) {
            try {
                con = c1.connection_db();
                PreparedStatement st = con.prepareStatement("SELECT cout from formation where id_formation=?");
                st.setString(1, label_id_formation.getText());
                rs = st.executeQuery();
                while (rs.next()) {
                    int cout = rs.getInt("cout");
                    int montant = Integer.parseInt(input_montant.getText());
                    int reste = cout - montant;
                    String insert = "insert into paiment (montant,candidat_inscrie_formation_candidat_id_candidat,candidat_inscrie_formation_formation_id,reste) values(?,?,?,?)";
                    PreparedStatement st2 = con.prepareStatement(insert);
                    st2.setInt(1, montant);
                    st2.setString(2, label_id_candidat.getText());
                    st2.setString(3, label_id_formation.getText());
                    st2.setInt(4, reste);
                    st2.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Un paiment effectué avec succèes  ", null, JOptionPane.INFORMATION_MESSAGE);
                    input_formation.setText("");
                    inp1.setText("");
                    inp2.setText("");
                    input_montant.setText("");
                    model.setRowCount(0);
                    selectionner_les_payment();
                }
            } 
            catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "" + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    public void modifer_paiment() {
        if (valider_formulaire() == true && a_payer_tout() == false) {
            try {
                con = c1.connection_db();
                PreparedStatement st = con.prepareStatement("select reste,montant from paiment where candidat_inscrie_formation_candidat_id_candidat=?");
                st.setString(1, label_id_candidat.getText());
                rs = st.executeQuery();
                while (rs.next()) {
                    int an_reste = rs.getInt("reste");
                    int an_montant = rs.getInt("montant");
                    int montant = Integer.parseInt(input_montant.getText());
                    int nv_reste = an_reste - montant;
                    int nv_montant = montant + an_montant;
                    PreparedStatement st2 = con.prepareStatement("update paiment set montant=?,reste=? where candidat_inscrie_formation_candidat_id_candidat=?");
                    st2.setInt(1, nv_montant);
                    st2.setInt(2, nv_reste);
                    st2.setString(3, label_id_candidat.getText());
                    st2.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Paiment effectuée avec succès", null, JOptionPane.INFORMATION_MESSAGE);
                    model.setRowCount(0);
                    selectionner_les_payment();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "" + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void selectionner_les_payment() {
        model = (DefaultTableModel) tableau_paiment.getModel();
        try {
            con = c1.connection_db();
            PreparedStatement st = con.prepareStatement("SELECT * from paiment");
            rs = st.executeQuery();
            while (rs.next()) {
                String id_paiment = rs.getString("id_paiment");
                String montant = rs.getString("montant");
                String date = rs.getString("date").substring(0, 16);
                String candidat = rs.getString("candidat_inscrie_formation_candidat_id_candidat");
                String formation = rs.getString("candidat_inscrie_formation_formation_id");
                String reste = rs.getString("reste");
                PreparedStatement st2 = con.prepareStatement("SELECT nom,prenom from candidat where id_candidat=?");
                st2.setString(1, candidat);
                ResultSet rs2 = st2.executeQuery();
                while (rs2.next()) {
                    String nom_prenom = rs2.getString("nom") + " " + rs2.getString("prenom");
                    PreparedStatement st3 = con.prepareStatement("SELECT permis from formation where id_formation=?");
                    st3.setString(1, formation);
                    ResultSet rs3 = st3.executeQuery();
                    while (rs3.next()) {
                        String permis = rs3.getString("permis");
                        Object[] obj = {id_paiment, montant, nom_prenom, permis, date, reste};
                        model.addRow(obj);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "" + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean deja_paye() {
        boolean paye = false;
        try {
            con = c1.connection_db();
            PreparedStatement st = con.prepareStatement("Select * from paiment where candidat_inscrie_formation_candidat_id_candidat=? ");
            st.setString(1, label_id_candidat.getText());
            rs = st.executeQuery();
            if (rs.next()) {
                paye = true;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "" + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        }
        return paye;
    }

    public boolean a_payer_tout() {
        boolean paye = false;
        try {
            con = c1.connection_db();
            PreparedStatement st = con.prepareStatement("Select reste from paiment where candidat_inscrie_formation_candidat_id_candidat=? ");
            st.setString(1, label_id_candidat.getText());
            rs = st.executeQuery();
            if (rs.next()) {
                int reste = rs.getInt("reste");
                if (reste == 0) {
                    paye = true;
                    JOptionPane.showMessageDialog(null, "Vous pouvez pas ajouter un paiment pour ce candidat", null, JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "" + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        }
        return paye;
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
        jPanel3 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableau_paiment = new rojerusan.RSTableMetro();
        kGradientPanel2 = new keeptoo.KGradientPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        rSButtonHover3 = new rojeru_san.complementos.RSButtonHover();
        jPanel2 = new javax.swing.JPanel();
        inp2 = new app.bolivia.swing.JCTextField();
        jLabel12 = new javax.swing.JLabel();
        panel = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        input_montant = new app.bolivia.swing.JCTextField();
        inp1 = new app.bolivia.swing.JCTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        input_formation = new app.bolivia.swing.JCTextField();
        btn_ajouter = new rojeru_san.complementos.RSButtonHover();
        btn_ajouter1 = new rojeru_san.complementos.RSButtonHover();
        label_id_candidat = new javax.swing.JLabel();
        label_id_formation = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 3, 3, 3, new java.awt.Color(0, 102, 153)));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createMatteBorder(10, 3, 0, 3, new java.awt.Color(0, 102, 153)));

        jLabel19.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel19.setText("Liste des Paiments effectués");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(559, Short.MAX_VALUE)
                .addComponent(jLabel19)
                .addGap(393, 393, 393))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19)
                .addContainerGap(103, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 2, 10, 2, new java.awt.Color(0, 102, 153)));

        tableau_paiment.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "N° Paiment", "Montant(DA)", "Candidat", "Formation", "Date de paiment", "Reste à payer"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableau_paiment.setToolTipText("");
        tableau_paiment.setColorBackgoundHead(new java.awt.Color(0, 102, 153));
        tableau_paiment.setColorFilasBackgound2(new java.awt.Color(255, 255, 255));
        tableau_paiment.setColorFilasForeground1(new java.awt.Color(0, 0, 0));
        tableau_paiment.setColorFilasForeground2(new java.awt.Color(0, 0, 0));
        tableau_paiment.setColorSelBackgound(new java.awt.Color(0, 102, 153));
        tableau_paiment.setFillsViewportHeight(true);
        tableau_paiment.setFocusCycleRoot(true);
        tableau_paiment.setFuenteFilas(new java.awt.Font("Arial", 1, 15)); // NOI18N
        tableau_paiment.setFuenteFilasSelect(new java.awt.Font("Arial", 1, 15)); // NOI18N
        tableau_paiment.setFuenteHead(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tableau_paiment.setRowHeight(50);
        jScrollPane3.setViewportView(tableau_paiment);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 623, Short.MAX_VALUE)
        );

        kGradientPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(3, 3, 0, 3, new java.awt.Color(255, 255, 255)));
        kGradientPanel2.setkEndColor(new java.awt.Color(0, 0, 0));
        kGradientPanel2.setkGradientFocus(2500);
        kGradientPanel2.setkStartColor(new java.awt.Color(0, 102, 153));

        jLabel6.setIcon(new javax.swing.ImageIcon("C:\\Users\\yazid\\OneDrive\\Bureau\\lib\\AddNewBookIcons\\icons8_Rewind_48px.png")); // NOI18N
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("La page des paiment");

        rSButtonHover3.setBackground(new java.awt.Color(0, 102, 153));
        rSButtonHover3.setText("X");
        rSButtonHover3.setColorHover(new java.awt.Color(255, 255, 255));
        rSButtonHover3.setColorTextHover(new java.awt.Color(0, 102, 153));
        rSButtonHover3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        rSButtonHover3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rSButtonHover3MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout kGradientPanel2Layout = new javax.swing.GroupLayout(kGradientPanel2);
        kGradientPanel2.setLayout(kGradientPanel2Layout);
        kGradientPanel2Layout.setHorizontalGroup(
            kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(rSButtonHover3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        kGradientPanel2Layout.setVerticalGroup(
            kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel10)
                        .addComponent(rSButtonHover3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(0, 102, 153));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        inp2.setEditable(false);
        inp2.setBackground(new java.awt.Color(0, 102, 153));
        inp2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        inp2.setForeground(new java.awt.Color(255, 255, 255));
        inp2.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        inp2.setOpaque(false);
        inp2.setPhColor(new java.awt.Color(255, 255, 255));
        inp2.setPlaceholder("Prènom du candidat");
        inp2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inp2ActionPerformed(evt);
            }
        });
        jPanel2.add(inp2, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 330, 165, -1));

        jLabel12.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Candidat");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(191, 249, -1, -1));

        panel.setBackground(new java.awt.Color(255, 255, 255));
        panel.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 0, 2, new java.awt.Color(0, 102, 153)));

        jLabel15.setBackground(new java.awt.Color(0, 0, 0));
        jLabel15.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel15.setText("Ajouter un paiement");

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel15)
                .addGap(113, 113, 113))
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel15)
                .addContainerGap())
        );

        jPanel2.add(panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 475, -1));

        jLabel13.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Montant");
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(207, 133, -1, -1));

        input_montant.setBackground(new java.awt.Color(0, 102, 153));
        input_montant.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        input_montant.setForeground(new java.awt.Color(255, 255, 255));
        input_montant.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        input_montant.setOpaque(false);
        input_montant.setPhColor(new java.awt.Color(255, 255, 255));
        input_montant.setPlaceholder("Montant payé (DA)");
        input_montant.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                input_montantActionPerformed(evt);
            }
        });
        jPanel2.add(input_montant, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 162, 425, -1));

        inp1.setEditable(false);
        inp1.setBackground(new java.awt.Color(0, 102, 153));
        inp1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        inp1.setForeground(new java.awt.Color(255, 255, 255));
        inp1.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        inp1.setOpaque(false);
        inp1.setPhColor(new java.awt.Color(255, 255, 255));
        inp1.setPlaceholder("Nom du candidat");
        inp1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inp1ActionPerformed(evt);
            }
        });
        jPanel2.add(inp1, new org.netbeans.lib.awtextra.AbsoluteConstraints(34, 332, 165, -1));

        jLabel14.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Nom");
        jPanel2.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(34, 306, -1, -1));

        jLabel16.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Prénom");
        jPanel2.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 310, -1, -1));

        jLabel17.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Formation");
        jPanel2.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(192, 438, -1, -1));

        input_formation.setEditable(false);
        input_formation.setBackground(new java.awt.Color(0, 102, 153));
        input_formation.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        input_formation.setForeground(new java.awt.Color(255, 255, 255));
        input_formation.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        input_formation.setOpaque(false);
        input_formation.setPhColor(new java.awt.Color(255, 255, 255));
        input_formation.setPlaceholder("La formation (catègorie) ");
        input_formation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                input_formationActionPerformed(evt);
            }
        });
        jPanel2.add(input_formation, new org.netbeans.lib.awtextra.AbsoluteConstraints(34, 496, 425, -1));

        btn_ajouter.setBackground(new java.awt.Color(0, 102, 153));
        btn_ajouter.setText("+");
        btn_ajouter.setColorHover(new java.awt.Color(255, 255, 255));
        btn_ajouter.setColorTextHover(new java.awt.Color(0, 102, 153));
        btn_ajouter.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btn_ajouter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_ajouterMouseClicked(evt);
            }
        });
        btn_ajouter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ajouterActionPerformed(evt);
            }
        });
        jPanel2.add(btn_ajouter, new org.netbeans.lib.awtextra.AbsoluteConstraints(298, 241, 41, -1));

        btn_ajouter1.setBackground(new java.awt.Color(0, 102, 153));
        btn_ajouter1.setText("VALIDER LE PAIMENT");
        btn_ajouter1.setColorHover(new java.awt.Color(255, 255, 255));
        btn_ajouter1.setColorTextHover(new java.awt.Color(0, 102, 153));
        btn_ajouter1.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        btn_ajouter1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_ajouter1MouseClicked(evt);
            }
        });
        jPanel2.add(btn_ajouter1, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 705, 451, -1));

        label_id_candidat.setText("jLabel1");
        jPanel2.add(label_id_candidat, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 90, -1, -1));

        label_id_formation.setText("jLabel1");
        jPanel2.add(label_id_formation, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 90, -1, -1));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(kGradientPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(69, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel6MouseClicked

    private void rSButtonHover3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rSButtonHover3MouseClicked
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_rSButtonHover3MouseClicked

    private void inp2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inp2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inp2ActionPerformed

    private void input_montantActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_input_montantActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_input_montantActionPerformed

    private void inp1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inp1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inp1ActionPerformed

    private void input_formationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_input_formationActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_input_formationActionPerformed

    private void btn_ajouterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ajouterMouseClicked
        // TODO add your handling code here:
        liste_candidat_tableau liste_c = new liste_candidat_tableau(this);
        liste_c.setVisible(true);
        liste_c.selectionner_candidats_sans_paiment();

    }//GEN-LAST:event_btn_ajouterMouseClicked

    private void btn_ajouter1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ajouter1MouseClicked
        // TODO add your handling code here:
        if (deja_paye() == true) {
            modifer_paiment();
        } else {
            inserer_payment();
        }
    }//GEN-LAST:event_btn_ajouter1MouseClicked

    private void btn_ajouterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ajouterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_ajouterActionPerformed

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
            java.util.logging.Logger.getLogger(paiment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(paiment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(paiment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(paiment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new paiment().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private rojeru_san.complementos.RSButtonHover btn_ajouter;
    private rojeru_san.complementos.RSButtonHover btn_ajouter1;
    private app.bolivia.swing.JCTextField inp1;
    private app.bolivia.swing.JCTextField inp2;
    private app.bolivia.swing.JCTextField input_formation;
    private app.bolivia.swing.JCTextField input_montant;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane3;
    private keeptoo.KGradientPanel kGradientPanel2;
    private javax.swing.JLabel label_id_candidat;
    private javax.swing.JLabel label_id_formation;
    private javax.swing.JPanel panel;
    private rojeru_san.complementos.RSButtonHover rSButtonHover3;
    private rojerusan.RSTableMetro tableau_paiment;
    // End of variables declaration//GEN-END:variables
}
