/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auto_ecole_application;

import java.awt.Color;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author yazid
 */
public class planning extends javax.swing.JFrame {

    Candidat c1 = new Candidat();
    Connection con = null;
    ResultSet rs = null;
    Statement st = null;

    /**
     * Creates new form planning
     */
    public planning() {
        initComponents();
        label1.setText("");
        label2.setText("");
        label3.setText("");
        label4.setText("");
        label5.setText("");
        label6.setText("");
        label7.setText("");
        label8.setText("");
        label9.setText("");
        label10.setText("");
        moniteur1.setText("");
        moniteur2.setText("");
        moniteur3.setText("");
        moniteur4.setText("");
        moniteur5.setText("");
        moniteur6.setText("");
        moniteur7.setText("");
        moniteur8.setText("");
        moniteur9.setText("");
        moniteur10.setText("");
        groupe1.setText("");
        groupe2.setText("");
        groupe3.setText("");
        groupe4.setText("");
        groupe5.setText("");
        groupe6.setText("");
        groupe7.setText("");
        groupe8.setText("");
        groupe9.setText("");
        groupe10.setText("");
        candidat1.setText("");
        candidat2.setText("");
        candidat3.setText("");
        candidat4.setText("");
        candidat5.setText("");
        candidat6.setText("");
        candidat7.setText("");
        candidat8.setText("");
        candidat9.setText("");
        candidat10.setText("");
    }

    public void mise_ajour() {
        les_dates.removeAllItems();
        les_dates.addItem("Aucune");
    }

    public void cliquer_sur_ok() {
        String type_seance = type_s.getSelectedItem().toString();
        if (type_seance.equals("Aucun")) {
            JOptionPane.showMessageDialog(null, "Vous devez choisir d'abord le type de la séance !", null, JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Les dates concernant :" + type_seance + " sont selectionnée", null, JOptionPane.INFORMATION_MESSAGE);
            try {
                con = c1.connection_db();
                PreparedStatement st = con.prepareStatement("select distinct date from seance where type=?");
                st.setString(1, type_seance);
                rs = st.executeQuery();
                while (rs.next()) {
                    Object date_seance = rs.getDate("date");
                    les_dates.addItem(date_seance);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erreur: " + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void remplir_planning() {
        String date = les_dates.getSelectedItem().toString();
        String type = type_s.getSelectedItem().toString();
        try {
            if (type.equals("Code")) {
                moniteur1.setText("");
                moniteur2.setText("");
                moniteur3.setText("");
                moniteur4.setText("");
                moniteur5.setText("");
                moniteur6.setText("");
                moniteur7.setText("");
                moniteur8.setText("");
                moniteur9.setText("");
                moniteur10.setText("");
                groupe1.setText("");
                groupe2.setText("");
                groupe3.setText("");
                groupe4.setText("");
                groupe5.setText("");
                groupe6.setText("");
                groupe7.setText("");
                groupe8.setText("");
                groupe9.setText("");
                groupe10.setText("");
                candidat1.setText("");
                candidat2.setText("");
                candidat3.setText("");
                candidat4.setText("");
                candidat5.setText("");
                candidat6.setText("");
                candidat7.setText("");
                candidat8.setText("");
                candidat9.setText("");
                candidat10.setText("");
                con = c1.connection_db();
                PreparedStatement st = con.prepareStatement("select * from seance where date=? and type=?");
                st.setString(1, date);
                st.setString(2, type);
                rs = st.executeQuery();
                while (rs.next() && type.equals("Code")) {
                    String heure = rs.getString("heure");
                    String moniteur = rs.getString("moniteur_employe_id");
                    String groupe = rs.getString("groupe_id");
                    switch (heure) {
                        case "08:00 -- 08:45":
                            panel1.setBackground(Color.green);
                            PreparedStatement st1 = con.prepareStatement("select nom,prenom from employe where id_employe=?");
                            st1.setString(1, moniteur);
                            ResultSet rs1 = st1.executeQuery();
                            if (rs1.next()) {
                                moniteur1.setText(rs1.getString("nom") + " " + rs1.getString("prenom"));
                            }
                            PreparedStatement st_g1 = con.prepareStatement("select nom from groupe where id=?");
                            st_g1.setString(1, groupe);
                            ResultSet rs_g1 = st_g1.executeQuery();
                            if (rs_g1.next()) {
                                groupe1.setText(rs_g1.getString("nom"));
                            }
                            break;
                        case "08:45 -- 09:30":
                            panel2.setBackground(Color.green);
                            PreparedStatement st2 = con.prepareStatement("select nom,prenom from employe where id_employe=?");
                            st2.setString(1, moniteur);
                            ResultSet rs2 = st2.executeQuery();
                            if (rs2.next()) {
                                moniteur2.setText(rs2.getString("nom") + " " + rs2.getString("prenom"));
                            }
                            PreparedStatement st_g2 = con.prepareStatement("select nom from groupe where id=?");
                            st_g2.setString(1, groupe);
                            ResultSet rs_g2 = st_g2.executeQuery();
                            if (rs_g2.next()) {
                                groupe2.setText(rs_g2.getString("nom"));
                            }
                            break;
                        case "09:45 -- 10:30":
                            panel3.setBackground(Color.green);
                            PreparedStatement st3 = con.prepareStatement("select nom,prenom from employe where id_employe=?");
                            st3.setString(1, moniteur);
                            ResultSet rs3 = st3.executeQuery();
                            if (rs3.next()) {
                                moniteur3.setText(rs3.getString("nom") + " " + rs3.getString("prenom"));
                            }
                            PreparedStatement st_g3 = con.prepareStatement("select nom from groupe where id=?");
                            st_g3.setString(1, groupe);
                            ResultSet rs_g3 = st_g3.executeQuery();
                            if (rs_g3.next()) {
                                groupe3.setText(rs_g3.getString("nom"));
                            }
                            break;
                        case "10:30 -- 11:15":
                            panel4.setBackground(Color.green);
                            PreparedStatement st4 = con.prepareStatement("select nom,prenom from employe where id_employe=?");
                            st4.setString(1, moniteur);
                            ResultSet rs4 = st4.executeQuery();
                            if (rs4.next()) {
                                moniteur4.setText(rs4.getString("nom") + " " + rs4.getString("prenom"));
                            }
                            PreparedStatement st_g4 = con.prepareStatement("select nom from groupe where id=?");
                            st_g4.setString(1, groupe);
                            ResultSet rs_g4 = st_g4.executeQuery();
                            if (rs_g4.next()) {
                                groupe4.setText(rs_g4.getString("nom"));
                            }
                            break;
                        case "11:15 -- 12:00":
                            panel5.setBackground(Color.green);
                            PreparedStatement st5 = con.prepareStatement("select nom,prenom from employe where id_employe=?");
                            st5.setString(1, moniteur);
                            ResultSet rs5 = st5.executeQuery();
                            if (rs5.next()) {
                                moniteur5.setText(rs5.getString("nom") + " " + rs5.getString("prenom"));
                            }
                            PreparedStatement st_g5 = con.prepareStatement("select nom from groupe where id=?");
                            st_g5.setString(1, groupe);
                            ResultSet rs_g5 = st_g5.executeQuery();
                            if (rs_g5.next()) {
                                groupe5.setText(rs_g5.getString("nom"));
                            }

                            break;
                        case "13:00 -- 13:45":
                            panel6.setBackground(Color.green);
                            PreparedStatement st6 = con.prepareStatement("select nom,prenom from employe where id_employe=?");
                            st6.setString(1, moniteur);
                            ResultSet rs6 = st6.executeQuery();
                            if (rs6.next()) {
                                moniteur6.setText(rs6.getString("nom") + " " + rs6.getString("prenom"));
                            }
                            PreparedStatement st_g6 = con.prepareStatement("select nom from groupe where id=?");
                            st_g6.setString(1, groupe);
                            ResultSet rs_g6 = st_g6.executeQuery();
                            if (rs_g6.next()) {
                                groupe6.setText(rs_g6.getString("nom"));
                            }
                            break;
                        case "13:45 -- 14:30":
                            panel7.setBackground(Color.green);
                            PreparedStatement st7 = con.prepareStatement("select nom,prenom from employe where id_employe=?");
                            st7.setString(1, moniteur);
                            ResultSet rs7 = st7.executeQuery();
                            if (rs7.next()) {
                                moniteur7.setText(rs7.getString("nom") + " " + rs7.getString("prenom"));
                            }
                            PreparedStatement st_g7 = con.prepareStatement("select nom from groupe where id=?");
                            st_g7.setString(1, groupe);
                            ResultSet rs_g7 = st_g7.executeQuery();
                            if (rs_g7.next()) {
                                groupe7.setText(rs_g7.getString("nom"));
                            }
                            break;
                        case "14:30 -- 15:15":
                            panel8.setBackground(Color.green);
                            PreparedStatement st8 = con.prepareStatement("select nom,prenom from employe where id_employe=?");
                            st8.setString(1, moniteur);
                            ResultSet rs8 = st8.executeQuery();
                            if (rs8.next()) {
                                moniteur8.setText(rs8.getString("nom") + " " + rs8.getString("prenom"));
                            }
                            PreparedStatement st_g8 = con.prepareStatement("select nom from groupe where id=?");
                            st_g8.setString(1, groupe);
                            ResultSet rs_g8 = st_g8.executeQuery();
                            if (rs_g8.next()) {
                                groupe8.setText(rs_g8.getString("nom"));
                            }
                            break;
                        case "15:15 -- 16:00":
                            panel9.setBackground(Color.green);
                            PreparedStatement st9 = con.prepareStatement("select nom,prenom from employe where id_employe=?");
                            st9.setString(1, moniteur);
                            ResultSet rs9 = st9.executeQuery();
                            if (rs9.next()) {
                                moniteur9.setText(rs9.getString("nom") + " " + rs9.getString("prenom"));
                            }
                            PreparedStatement st_g9 = con.prepareStatement("select nom from groupe where id=?");
                            st_g9.setString(1, groupe);
                            ResultSet rs_g9 = st_g9.executeQuery();
                            if (rs_g9.next()) {
                                groupe9.setText(rs_g9.getString("nom"));
                            }
                            break;
                        case "16:00 -- 16:45":
                            panel9.setBackground(Color.green);
                            PreparedStatement st10 = con.prepareStatement("select nom,prenom from employe where id_employe=?");
                            st10.setString(1, moniteur);
                            ResultSet rs10 = st10.executeQuery();
                            if (rs10.next()) {
                                moniteur10.setText(rs10.getString("nom") + " " + rs10.getString("prenom"));
                            }
                            PreparedStatement st_g10 = con.prepareStatement("select nom from groupe where id=?");
                            st_g10.setString(1, groupe);
                            ResultSet rs_g10 = st_g10.executeQuery();
                            if (rs_g10.next()) {
                                groupe10.setText(rs_g10.getString("nom"));
                            }
                            break;
                    }
                }
            } else {
                moniteur1.setText("");
                moniteur2.setText("");
                moniteur3.setText("");
                moniteur4.setText("");
                moniteur5.setText("");
                moniteur6.setText("");
                moniteur7.setText("");
                moniteur8.setText("");
                moniteur9.setText("");
                moniteur10.setText("");
                candidat1.setText("");
                candidat2.setText("");
                candidat3.setText("");
                candidat4.setText("");
                candidat5.setText("");
                candidat6.setText("");
                candidat7.setText("");
                candidat8.setText("");
                candidat9.setText("");
                candidat10.setText("");
                panel1.setBackground(Color.white);
                panel2.setBackground(Color.white);
                panel3.setBackground(Color.white);
                panel4.setBackground(Color.white);
                panel5.setBackground(Color.red);
                panel6.setBackground(Color.white);
                panel7.setBackground(Color.white);
                panel8.setBackground(Color.white);
                panel9.setBackground(Color.white);
                panel10.setBackground(Color.white);

                PreparedStatement st20 = con.prepareStatement("select * from seance where date=? and type=?");
                st20.setString(1, date);
                st20.setString(2, type);
                ResultSet rs10 = st20.executeQuery();
                while (rs10.next()) {
                    String heure = rs10.getString("heure");
                    String moniteur = rs10.getString("moniteur_employe_id");
                    String candidat = rs10.getString("candidat_id_candidat");

                    switch (heure) {
                        case "08:00 -- 10:00":
                            panel1.setBackground(Color.green);
                            panel2.setBackground(Color.green);
                            PreparedStatement st1 = con.prepareStatement("select nom,prenom from employe where id_employe=?");
                            st1.setString(1, moniteur);
                            ResultSet rs1 = st1.executeQuery();
                            if (rs1.next()) {
                                moniteur1.setText(rs1.getString("nom") + " " + rs1.getString("prenom"));
                                moniteur2.setText(rs1.getString("nom") + " " + rs1.getString("prenom"));
                            }
                            PreparedStatement st_g1 = con.prepareStatement("select nom,prenom from candidat where id_candidat=?");
                            st_g1.setString(1, candidat);
                            ResultSet rs_g1 = st_g1.executeQuery();
                            if (rs_g1.next()) {
                                candidat1.setText(rs_g1.getString("nom") + " " + rs_g1.getString("prenom"));
                                candidat2.setText(rs_g1.getString("nom") + " " + rs_g1.getString("prenom"));
                            }
                            break;
                        case "10:00 -- 12:00":
                            panel3.setBackground(Color.green);
                            panel4.setBackground(Color.green);
                            PreparedStatement st2 = con.prepareStatement("select nom,prenom from employe where id_employe=?");
                            st2.setString(1, moniteur);
                            ResultSet rs2 = st2.executeQuery();
                            if (rs2.next()) {
                                moniteur3.setText(rs2.getString("nom") + " " + rs2.getString("prenom"));
                                moniteur4.setText(rs2.getString("nom") + " " + rs2.getString("prenom"));
                            }
                            PreparedStatement st_g2 = con.prepareStatement("select nom,prenom from candidat where id_candidat=?");
                            st_g2.setString(1, candidat);
                            ResultSet rs_g2 = st_g2.executeQuery();
                            if (rs_g2.next()) {
                                candidat3.setText(rs_g2.getString("nom") + " " + rs_g2.getString("prenom"));
                                candidat4.setText(rs_g2.getString("nom") + " " + rs_g2.getString("prenom"));
                            }
                            break;

                        case "13:00 -- 15:00":
                            panel6.setBackground(Color.green);
                            panel7.setBackground(Color.green);
                            PreparedStatement st3 = con.prepareStatement("select nom,prenom from employe where id_employe=?");
                            st3.setString(1, moniteur);
                            ResultSet rs3 = st3.executeQuery();
                            if (rs3.next()) {
                                moniteur6.setText(rs3.getString("nom") + " " + rs3.getString("prenom"));
                                moniteur7.setText(rs3.getString("nom") + " " + rs3.getString("prenom"));
                            }
                            PreparedStatement st_g3 = con.prepareStatement("select nom,prenom from candidat where id_candidat=?");
                            st_g3.setString(1, candidat);
                            ResultSet rs_g3 = st_g3.executeQuery();
                            if (rs_g3.next()) {
                                candidat6.setText(rs_g3.getString("nom") + " " + rs_g3.getString("prenom"));
                                candidat7.setText(rs_g3.getString("nom") + " " + rs_g3.getString("prenom"));
                            }
                            break;
                        case "15:00 -- 17:00":
                            panel8.setBackground(Color.green);
                            panel9.setBackground(Color.green);
                            PreparedStatement st4 = con.prepareStatement("select nom,prenom from employe where id_employe=?");
                            st4.setString(1, moniteur);
                            ResultSet rs4 = st4.executeQuery();
                            if (rs4.next()) {
                                moniteur8.setText(rs4.getString("nom") + " " + rs4.getString("prenom"));
                                moniteur9.setText(rs4.getString("nom") + " " + rs4.getString("prenom"));
                            }
                            PreparedStatement st_g4 = con.prepareStatement("select nom,prenom from candidat where id_candidat=?");
                            st_g4.setString(1, candidat);
                            ResultSet rs_g4 = st_g4.executeQuery();
                            if (rs_g4.next()) {
                                candidat8.setText(rs_g4.getString("nom") + " " + rs_g4.getString("prenom"));
                                candidat9.setText(rs_g4.getString("nom") + " " + rs_g4.getString("prenom"));
                            }
                            break;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur: " + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
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
        jPanel10 = new javax.swing.JPanel();
        les_dates = new rojerusan.RSComboMetro();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        panel1 = new javax.swing.JPanel();
        moniteur1 = new javax.swing.JLabel();
        candidat1 = new javax.swing.JLabel();
        groupe1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        type_s = new rojerusan.RSComboMetro();
        jLabel10 = new javax.swing.JLabel();
        rSButtonHover1 = new rojeru_san.complementos.RSButtonHover();
        panel2 = new javax.swing.JPanel();
        moniteur2 = new javax.swing.JLabel();
        candidat2 = new javax.swing.JLabel();
        groupe2 = new javax.swing.JLabel();
        panel3 = new javax.swing.JPanel();
        moniteur3 = new javax.swing.JLabel();
        candidat3 = new javax.swing.JLabel();
        groupe3 = new javax.swing.JLabel();
        panel4 = new javax.swing.JPanel();
        moniteur4 = new javax.swing.JLabel();
        candidat4 = new javax.swing.JLabel();
        groupe4 = new javax.swing.JLabel();
        panel5 = new javax.swing.JPanel();
        moniteur5 = new javax.swing.JLabel();
        candidat5 = new javax.swing.JLabel();
        groupe5 = new javax.swing.JLabel();
        panel6 = new javax.swing.JPanel();
        moniteur6 = new javax.swing.JLabel();
        candidat6 = new javax.swing.JLabel();
        groupe6 = new javax.swing.JLabel();
        panel7 = new javax.swing.JPanel();
        moniteur7 = new javax.swing.JLabel();
        candidat7 = new javax.swing.JLabel();
        groupe7 = new javax.swing.JLabel();
        panel8 = new javax.swing.JPanel();
        moniteur8 = new javax.swing.JLabel();
        candidat8 = new javax.swing.JLabel();
        groupe8 = new javax.swing.JLabel();
        panel9 = new javax.swing.JPanel();
        moniteur9 = new javax.swing.JLabel();
        candidat9 = new javax.swing.JLabel();
        groupe9 = new javax.swing.JLabel();
        panel10 = new javax.swing.JPanel();
        moniteur10 = new javax.swing.JLabel();
        candidat10 = new javax.swing.JLabel();
        groupe10 = new javax.swing.JLabel();
        label2 = new javax.swing.JLabel();
        label3 = new javax.swing.JLabel();
        label4 = new javax.swing.JLabel();
        label5 = new javax.swing.JLabel();
        label6 = new javax.swing.JLabel();
        label7 = new javax.swing.JLabel();
        label8 = new javax.swing.JLabel();
        label9 = new javax.swing.JLabel();
        label10 = new javax.swing.JLabel();
        label1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 102, 153)));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel10.setBackground(new java.awt.Color(0, 102, 153));

        les_dates.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Aucune" }));
        les_dates.setColorBorde(new java.awt.Color(255, 255, 255));
        les_dates.setColorFondo(new java.awt.Color(0, 102, 153));
        les_dates.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        les_dates.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                les_datesItemStateChanged(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Date de séance");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addComponent(les_dates, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(les_dates, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(58, 58, 58))
        );

        jPanel1.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 2, -1, 50));

        jPanel2.setBackground(new java.awt.Color(0, 102, 153));

        jLabel12.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Horaire");

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 70, Short.MAX_VALUE)
        );

        jLabel14.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Moniteur");

        jLabel15.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Groupe");

        jLabel16.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Candidat");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel12)
                .addGap(31, 31, 31)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(193, 193, 193)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 539, Short.MAX_VALUE)
                .addComponent(jLabel15)
                .addGap(449, 449, 449)
                .addComponent(jLabel16)
                .addGap(337, 337, 337))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel14)
                        .addComponent(jLabel15)
                        .addComponent(jLabel16)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 59, 1870, 50));

        panel1.setBackground(new java.awt.Color(255, 255, 255));
        panel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 102, 153)));
        panel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        moniteur1.setBackground(new java.awt.Color(0, 0, 0));
        moniteur1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        moniteur1.setText("Moniteur1");
        panel1.add(moniteur1, new org.netbeans.lib.awtextra.AbsoluteConstraints(74, 13, -1, -1));

        candidat1.setBackground(new java.awt.Color(0, 0, 0));
        candidat1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        candidat1.setText("Candidat1");
        panel1.add(candidat1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1223, 13, -1, -1));

        groupe1.setBackground(new java.awt.Color(0, 0, 0));
        groupe1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        groupe1.setText("groupe1");
        panel1.add(groupe1, new org.netbeans.lib.awtextra.AbsoluteConstraints(703, 13, -1, -1));

        jPanel1.add(panel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 170, 1609, -1));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/close2.jpg"))); // NOI18N
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1838, 2, -1, -1));

        jPanel11.setBackground(new java.awt.Color(0, 102, 153));

        type_s.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Aucun", "Code", "Conduite", "Créneau" }));
        type_s.setColorBorde(new java.awt.Color(255, 255, 255));
        type_s.setColorFondo(new java.awt.Color(0, 102, 153));
        type_s.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        type_s.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                type_sItemStateChanged(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Type de la séance");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addComponent(type_s, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(type_s, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(58, 58, 58))
        );

        jPanel1.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(425, 2, -1, 50));

        rSButtonHover1.setBackground(new java.awt.Color(0, 102, 153));
        rSButtonHover1.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 102, 153)));
        rSButtonHover1.setText("OK ");
        rSButtonHover1.setColorHover(new java.awt.Color(255, 255, 255));
        rSButtonHover1.setColorTextHover(new java.awt.Color(0, 102, 153));
        rSButtonHover1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rSButtonHover1ItemStateChanged(evt);
            }
        });
        rSButtonHover1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rSButtonHover1MouseClicked(evt);
            }
        });
        jPanel1.add(rSButtonHover1, new org.netbeans.lib.awtextra.AbsoluteConstraints(872, 2, 61, 50));

        panel2.setBackground(new java.awt.Color(255, 255, 255));
        panel2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 102, 153)));
        panel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        moniteur2.setBackground(new java.awt.Color(0, 0, 0));
        moniteur2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        moniteur2.setText("Moniteur1");
        panel2.add(moniteur2, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 13, -1, -1));

        candidat2.setBackground(new java.awt.Color(0, 0, 0));
        candidat2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        candidat2.setText("Candidat1");
        panel2.add(candidat2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1223, 13, -1, -1));

        groupe2.setBackground(new java.awt.Color(0, 0, 0));
        groupe2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        groupe2.setText("groupe1");
        panel2.add(groupe2, new org.netbeans.lib.awtextra.AbsoluteConstraints(703, 13, -1, -1));

        jPanel1.add(panel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 240, 1609, -1));

        panel3.setBackground(new java.awt.Color(255, 255, 255));
        panel3.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 102, 153)));
        panel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        moniteur3.setBackground(new java.awt.Color(0, 0, 0));
        moniteur3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        moniteur3.setText("Moniteur1");
        panel3.add(moniteur3, new org.netbeans.lib.awtextra.AbsoluteConstraints(76, 13, -1, -1));

        candidat3.setBackground(new java.awt.Color(0, 0, 0));
        candidat3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        candidat3.setText("Candidat1");
        panel3.add(candidat3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1223, 13, -1, -1));

        groupe3.setBackground(new java.awt.Color(0, 0, 0));
        groupe3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        groupe3.setText("groupe1");
        panel3.add(groupe3, new org.netbeans.lib.awtextra.AbsoluteConstraints(703, 13, -1, -1));

        jPanel1.add(panel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 320, 1609, -1));

        panel4.setBackground(new java.awt.Color(255, 255, 255));
        panel4.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 102, 153)));
        panel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        moniteur4.setBackground(new java.awt.Color(0, 0, 0));
        moniteur4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        moniteur4.setText("Moniteur1");
        panel4.add(moniteur4, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 13, -1, -1));

        candidat4.setBackground(new java.awt.Color(0, 0, 0));
        candidat4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        candidat4.setText("Candidat1");
        panel4.add(candidat4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1223, 13, -1, -1));

        groupe4.setBackground(new java.awt.Color(0, 0, 0));
        groupe4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        groupe4.setText("groupe1");
        panel4.add(groupe4, new org.netbeans.lib.awtextra.AbsoluteConstraints(703, 13, -1, -1));

        jPanel1.add(panel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 413, 1609, -1));

        panel5.setBackground(new java.awt.Color(255, 255, 255));
        panel5.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 102, 153)));
        panel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        moniteur5.setBackground(new java.awt.Color(0, 0, 0));
        moniteur5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        moniteur5.setText("Moniteur1");
        panel5.add(moniteur5, new org.netbeans.lib.awtextra.AbsoluteConstraints(78, 13, -1, -1));

        candidat5.setBackground(new java.awt.Color(0, 0, 0));
        candidat5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        candidat5.setText("Candidat1");
        panel5.add(candidat5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1223, 13, -1, -1));

        groupe5.setBackground(new java.awt.Color(0, 0, 0));
        groupe5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        groupe5.setText("groupe1");
        panel5.add(groupe5, new org.netbeans.lib.awtextra.AbsoluteConstraints(703, 13, -1, -1));

        jPanel1.add(panel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 497, 1609, -1));

        panel6.setBackground(new java.awt.Color(255, 255, 255));
        panel6.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 102, 153)));
        panel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        moniteur6.setBackground(new java.awt.Color(0, 0, 0));
        moniteur6.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        moniteur6.setText("Moniteur1");
        panel6.add(moniteur6, new org.netbeans.lib.awtextra.AbsoluteConstraints(79, 13, -1, -1));

        candidat6.setBackground(new java.awt.Color(0, 0, 0));
        candidat6.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        candidat6.setText("Candidat1");
        panel6.add(candidat6, new org.netbeans.lib.awtextra.AbsoluteConstraints(1223, 13, -1, -1));

        groupe6.setBackground(new java.awt.Color(0, 0, 0));
        groupe6.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        groupe6.setText("groupe1");
        panel6.add(groupe6, new org.netbeans.lib.awtextra.AbsoluteConstraints(703, 13, -1, -1));

        jPanel1.add(panel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 583, 1609, -1));

        panel7.setBackground(new java.awt.Color(255, 255, 255));
        panel7.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 102, 153)));
        panel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        moniteur7.setBackground(new java.awt.Color(0, 0, 0));
        moniteur7.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        moniteur7.setText("Moniteur1");
        panel7.add(moniteur7, new org.netbeans.lib.awtextra.AbsoluteConstraints(78, 13, -1, -1));

        candidat7.setBackground(new java.awt.Color(0, 0, 0));
        candidat7.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        candidat7.setText("Candidat1");
        panel7.add(candidat7, new org.netbeans.lib.awtextra.AbsoluteConstraints(1223, 13, -1, -1));

        groupe7.setBackground(new java.awt.Color(0, 0, 0));
        groupe7.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        groupe7.setText("groupe1");
        panel7.add(groupe7, new org.netbeans.lib.awtextra.AbsoluteConstraints(703, 13, -1, -1));

        jPanel1.add(panel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 659, 1609, -1));

        panel8.setBackground(new java.awt.Color(255, 255, 255));
        panel8.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 102, 153)));
        panel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        moniteur8.setBackground(new java.awt.Color(0, 0, 0));
        moniteur8.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        moniteur8.setText("Moniteur1");
        panel8.add(moniteur8, new org.netbeans.lib.awtextra.AbsoluteConstraints(77, 13, -1, -1));

        candidat8.setBackground(new java.awt.Color(0, 0, 0));
        candidat8.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        candidat8.setText("Candidat1");
        panel8.add(candidat8, new org.netbeans.lib.awtextra.AbsoluteConstraints(1223, 13, -1, -1));

        groupe8.setBackground(new java.awt.Color(0, 0, 0));
        groupe8.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        groupe8.setText("groupe1");
        panel8.add(groupe8, new org.netbeans.lib.awtextra.AbsoluteConstraints(703, 13, -1, -1));

        jPanel1.add(panel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 740, 1609, -1));

        panel9.setBackground(new java.awt.Color(255, 255, 255));
        panel9.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 102, 153)));
        panel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        moniteur9.setBackground(new java.awt.Color(0, 0, 0));
        moniteur9.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        moniteur9.setText("Moniteur1");
        panel9.add(moniteur9, new org.netbeans.lib.awtextra.AbsoluteConstraints(77, 13, -1, -1));

        candidat9.setBackground(new java.awt.Color(0, 0, 0));
        candidat9.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        candidat9.setText("Candidat1");
        panel9.add(candidat9, new org.netbeans.lib.awtextra.AbsoluteConstraints(1223, 13, -1, -1));

        groupe9.setBackground(new java.awt.Color(0, 0, 0));
        groupe9.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        groupe9.setText("groupe1");
        panel9.add(groupe9, new org.netbeans.lib.awtextra.AbsoluteConstraints(703, 13, -1, -1));

        jPanel1.add(panel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 820, 1609, -1));

        panel10.setBackground(new java.awt.Color(255, 255, 255));
        panel10.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 102, 153)));
        panel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        moniteur10.setBackground(new java.awt.Color(0, 0, 0));
        moniteur10.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        moniteur10.setText("Moniteur1");
        panel10.add(moniteur10, new org.netbeans.lib.awtextra.AbsoluteConstraints(77, 13, -1, -1));

        candidat10.setBackground(new java.awt.Color(0, 0, 0));
        candidat10.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        candidat10.setText("Candidat1");
        panel10.add(candidat10, new org.netbeans.lib.awtextra.AbsoluteConstraints(1223, 13, -1, -1));

        groupe10.setBackground(new java.awt.Color(0, 0, 0));
        groupe10.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        groupe10.setText("groupe1");
        panel10.add(groupe10, new org.netbeans.lib.awtextra.AbsoluteConstraints(703, 13, -1, -1));

        jPanel1.add(panel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 880, 1609, -1));

        label2.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        label2.setText("08:00");
        jPanel1.add(label2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 260, -1, -1));

        label3.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        label3.setText("08:00");
        jPanel1.add(label3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 340, -1, -1));

        label4.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        label4.setText("08:00");
        jPanel1.add(label4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 430, -1, -1));

        label5.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        label5.setText("08:00");
        jPanel1.add(label5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 520, -1, -1));

        label6.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        label6.setText("08:00");
        jPanel1.add(label6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 610, -1, -1));

        label7.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        label7.setText("08:00");
        jPanel1.add(label7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 680, -1, -1));

        label8.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        label8.setText("08:00");
        jPanel1.add(label8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 760, -1, -1));

        label9.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        label9.setText("08:00");
        jPanel1.add(label9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 840, -1, -1));

        label10.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        label10.setText("08:00");
        jPanel1.add(label10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 900, -1, -1));

        label1.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        label1.setText("08:00");
        jPanel1.add(label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 935, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jLabel2MouseClicked

    private void les_datesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_les_datesItemStateChanged
        // TODO add your hand
        panel1.setBackground(Color.white);
        panel2.setBackground(Color.white);
        panel3.setBackground(Color.white);
        panel4.setBackground(Color.white);
        panel5.setBackground(Color.white);
        panel6.setBackground(Color.white);
        panel7.setBackground(Color.white);
        panel8.setBackground(Color.white);
        panel9.setBackground(Color.white);
        panel10.setBackground(Color.white);
        remplir_planning();
    }//GEN-LAST:event_les_datesItemStateChanged

    private void type_sItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_type_sItemStateChanged
        // TODO add your handling code here:
        String items = type_s.getSelectedItem().toString();
        if (items.equals("Aucun")) {
            JOptionPane.showMessageDialog(null, "Vous devez choisir le type !", null, JOptionPane.ERROR_MESSAGE);
            label1.setText("");
            label2.setText("");
            label3.setText("");
            label4.setText("");
            label5.setText("");
            label6.setText("");
            label7.setText("");
            label8.setText("");
            label9.setText("");
            label10.setText("");
            panel1.setBackground(Color.white);
            panel2.setBackground(Color.white);
            panel3.setBackground(Color.white);
            panel4.setBackground(Color.white);
            panel5.setBackground(Color.white);
            panel6.setBackground(Color.white);
            panel7.setBackground(Color.white);
            panel8.setBackground(Color.white);
            panel9.setBackground(Color.white);
            panel10.setBackground(Color.white);

        } else {
            if (items.equals("Code")) {
                label1.setText("08:00 -- 08:45");
                label2.setText("08:45 -- 09:30");
                label3.setText("09:45 -- 10:30");
                label4.setText("10:30 -- 11:15");
                label5.setText("11:15 -- 12:00");
                label6.setText("13:00 -- 13:45");
                label7.setText("13:45 -- 14:30");
                label8.setText("14:30 -- 15:15");
                label9.setText("15:15 -- 16:00");
                label10.setText("16:00 -- 16:45");
            } else {
                if (items.equals("Conduite") || items.equals("Créneau")) {
                    label1.setText("08:00 -- 09:00");
                    label2.setText("09:00 -- 10:00");
                    label3.setText("10:00 -- 11:00");
                    label4.setText("11:00 -- 12:00");
                    label5.setText("12:00 -- 13:00");
                    label6.setText("13:00 -- 14:00");
                    label7.setText("14:00 -- 15:00");
                    label8.setText("15:00 -- 16:00");
                    label9.setText("16:00 -- 17:00");
                    label10.setText("");
                }
            }
        }
    }//GEN-LAST:event_type_sItemStateChanged

    private void rSButtonHover1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rSButtonHover1ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_rSButtonHover1ItemStateChanged

    private void rSButtonHover1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rSButtonHover1MouseClicked
        // TODO add your handling code here:$
        cliquer_sur_ok();
    }//GEN-LAST:event_rSButtonHover1MouseClicked

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
            java.util.logging.Logger.getLogger(planning.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(planning.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(planning.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(planning.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new planning().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel candidat1;
    private javax.swing.JLabel candidat10;
    private javax.swing.JLabel candidat2;
    private javax.swing.JLabel candidat3;
    private javax.swing.JLabel candidat4;
    private javax.swing.JLabel candidat5;
    private javax.swing.JLabel candidat6;
    private javax.swing.JLabel candidat7;
    private javax.swing.JLabel candidat8;
    private javax.swing.JLabel candidat9;
    private javax.swing.JLabel groupe1;
    private javax.swing.JLabel groupe10;
    private javax.swing.JLabel groupe2;
    private javax.swing.JLabel groupe3;
    private javax.swing.JLabel groupe4;
    private javax.swing.JLabel groupe5;
    private javax.swing.JLabel groupe6;
    private javax.swing.JLabel groupe7;
    private javax.swing.JLabel groupe8;
    private javax.swing.JLabel groupe9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel label1;
    private javax.swing.JLabel label10;
    private javax.swing.JLabel label2;
    private javax.swing.JLabel label3;
    private javax.swing.JLabel label4;
    private javax.swing.JLabel label5;
    private javax.swing.JLabel label6;
    private javax.swing.JLabel label7;
    private javax.swing.JLabel label8;
    private javax.swing.JLabel label9;
    private rojerusan.RSComboMetro les_dates;
    private javax.swing.JLabel moniteur1;
    private javax.swing.JLabel moniteur10;
    private javax.swing.JLabel moniteur2;
    private javax.swing.JLabel moniteur3;
    private javax.swing.JLabel moniteur4;
    private javax.swing.JLabel moniteur5;
    private javax.swing.JLabel moniteur6;
    private javax.swing.JLabel moniteur7;
    private javax.swing.JLabel moniteur8;
    private javax.swing.JLabel moniteur9;
    private javax.swing.JPanel panel1;
    private javax.swing.JPanel panel10;
    private javax.swing.JPanel panel2;
    private javax.swing.JPanel panel3;
    private javax.swing.JPanel panel4;
    private javax.swing.JPanel panel5;
    private javax.swing.JPanel panel6;
    private javax.swing.JPanel panel7;
    private javax.swing.JPanel panel8;
    private javax.swing.JPanel panel9;
    private rojeru_san.complementos.RSButtonHover rSButtonHover1;
    private rojerusan.RSComboMetro type_s;
    // End of variables declaration//GEN-END:variables
}
