/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auto_ecole_application;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author yazid
 */
public class groupe extends javax.swing.JFrame {

    Candidat c1 = new Candidat();
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    DefaultTableModel model, model2;

    /**
     * Creates new form groupe
     */
    public groupe() {
        initComponents();
        selectionner_candidats();
        id_c.setVisible(false);
        id_c2.setVisible(false);
        liste_groupe();
        //  list.addActionListener(new ActionListener() {
        //    @Override
        //  public void actionPerformed(ActionEvent e) {
        //    selectionner_candidats_groupe();
        //}
        // });
        recherche.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                rechercher_candidat();
            }

            public void removeUpdate(DocumentEvent e) {
                rechercher_candidat();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }

    public void selectionner_candidats() {
        try {
            con = c1.connection_db();
            PreparedStatement st = con.prepareStatement("select * from candidat where groupe is null");
            rs = st.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id_candidat");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                Date date_naissance = rs.getDate("date_naissance");
                Object[] obj = {id, nom, prenom, date_naissance};
                model = (DefaultTableModel) tableau_candidat.getModel();
                model.addRow(obj);
            }
            tableau_candidat.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    table1MouseReleased(evt);
                }

                private void table1MouseReleased(MouseEvent evt) {
                    int selectionner = tableau_candidat.getSelectedRow();
                    String string_selectionner = String.valueOf(selectionner);
                    if (c1.est_chiffre(string_selectionner) == false) {
                        JOptionPane.showMessageDialog(null, "Aucun Candidat n'est selectionné !:", null, JOptionPane.ERROR_MESSAGE);
                    } else {
                        Object ids = model.getValueAt(selectionner, 0);
                        id_c.setText("" + ids);
                    }
                }
            });
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur 404 :" + e.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        }
    }

    public void mise_a_jour1(String ids) {
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).equals(ids)) {
                model.removeRow(i);
                id_c.setText("");
                id_c2.setText("");
            }
        }
    }

    public void mise_a_jour2(String ids) {
        for (int i = 0; i < model2.getRowCount(); i++) {
            if (model2.getValueAt(i, 1).equals(ids)) {
                model2.removeRow(i);
                id_c.setText("");
                id_c2.setText("");
            }
        }
    }

    public void ajouter_liste(String id) {
        String nom = "";
        String prenom = "";
        String date = "";
        model = (DefaultTableModel) tableau_candidat.getModel();
        model2 = (DefaultTableModel) tableau_candidat2.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).toString().equals(id)) {
                nom = model.getValueAt(i, 1).toString();
                prenom = model.getValueAt(i, 2).toString();
                date = model.getValueAt(i, 3).toString();
                Object[] obj = {model2.getRowCount() + 1, id, nom, prenom, date};
                model2.addRow(obj);
                mise_a_jour1(id);
                tableau_2();
            }
        }
    }

    public void tableau_2() {
        tableau_candidat2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                table1MouseReleased(evt);
            }

            private void table1MouseReleased(MouseEvent evt) {
                int selectionner = tableau_candidat2.getSelectedRow();
                String string_selectionner = String.valueOf(selectionner);
                if (c1.est_chiffre(string_selectionner) == false) {
                    JOptionPane.showMessageDialog(null, "Aucun Candidat n'est selectionné ! ", null, JOptionPane.ERROR_MESSAGE);
                } else {
                    Object ids = model2.getValueAt(selectionner, 1);
                    id_c2.setText("" + ids);
                }
            }
        });
    }

    public void supprimer_liste(String id) {
        String nom = "";
        String prenom = "";
        String date = "";
        model = (DefaultTableModel) tableau_candidat.getModel();
        model2 = (DefaultTableModel) tableau_candidat2.getModel();
        for (int i = 0; i < model2.getRowCount(); i++) {
            if (model2.getValueAt(i, 1).toString().equals(id)) {
                nom = model2.getValueAt(i, 2).toString();
                prenom = model2.getValueAt(i, 3).toString();
                date = model2.getValueAt(i, 4).toString();
                Object[] obj = {id, nom, prenom, date};
                model.addRow(obj);
                mise_a_jour2(id);
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
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        date_actuel_var = new javax.swing.JLabel();
        jour_semaine = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        list = new rojerusan.RSComboMetro();
        jLabel11 = new javax.swing.JLabel();
        rSButtonHover3 = new rojeru_san.complementos.RSButtonHover();
        jPanel3 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        id_veh = new javax.swing.JLabel();
        titre_veh = new javax.swing.JLabel();
        groupe = new app.bolivia.swing.JCTextField();
        jLabel23 = new javax.swing.JLabel();
        id_c2 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableau_candidat2 = new rojerusan.RSTableMetro();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableau_candidat = new rojerusan.RSTableMetro();
        jPanel15 = new javax.swing.JPanel();
        type_rech = new rojerusan.RSComboMetro();
        recherche = new app.bolivia.swing.JCTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        id_c = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 3, new java.awt.Color(0, 102, 153)));

        jPanel2.setBackground(new java.awt.Color(0, 102, 153));
        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel2MouseClicked(evt);
            }
        });

        date_actuel_var.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        date_actuel_var.setForeground(new java.awt.Color(255, 255, 255));

        jour_semaine.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        jour_semaine.setForeground(new java.awt.Color(255, 255, 255));

        jLabel6.setIcon(new javax.swing.ImageIcon("C:\\Users\\yazid\\OneDrive\\Bureau\\lib\\AddNewBookIcons\\icons8_Rewind_48px.png")); // NOI18N
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Gestion des groupes");

        list.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Aucun" }));
        list.setColorBorde(new java.awt.Color(255, 255, 255));
        list.setColorFondo(new java.awt.Color(0, 102, 153));
        list.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        list.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                listItemStateChanged(evt);
            }
        });
        list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listMouseClicked(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Les groupes");

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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(407, 407, 407)
                        .addComponent(jour_semaine)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(date_actuel_var)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1084, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addGap(235, 235, 235))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)
                        .addGap(130, 130, 130)
                        .addComponent(jLabel11)
                        .addGap(18, 18, 18)
                        .addComponent(list, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(rSButtonHover3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5)
                                .addComponent(date_actuel_var)
                                .addComponent(jour_semaine))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel10)
                                        .addComponent(list, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel11)
                                        .addComponent(rSButtonHover3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(21, 21, 21))))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createMatteBorder(10, 3, 0, 3, new java.awt.Color(0, 102, 153)));

        jLabel19.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel19.setText("Nom de Groupe");

        id_veh.setFont(new java.awt.Font("Arial", 3, 24)); // NOI18N

        titre_veh.setFont(new java.awt.Font("Arial", 3, 24)); // NOI18N

        groupe.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));
        groupe.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        groupe.setOpaque(false);
        groupe.setPlaceholder("Taper le nom de groupe");
        groupe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                groupeActionPerformed(evt);
            }
        });

        jLabel23.setBackground(new java.awt.Color(255, 255, 255));
        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Blue-Add-Button-PNG.png"))); // NOI18N
        jLabel23.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel23MouseClicked(evt);
            }
        });

        id_c2.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N

        jLabel9.setIcon(new javax.swing.ImageIcon("C:\\Users\\yazid\\OneDrive\\Bureau\\Auto_Ecole\\delete.png")); // NOI18N
        jLabel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel9MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(id_c2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 217, Short.MAX_VALUE)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(titre_veh)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(id_veh)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel23)
                        .addGap(185, 185, 185)
                        .addComponent(jLabel9)))
                .addGap(220, 220, 220))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(201, 201, 201)
                    .addComponent(groupe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(202, 202, 202)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addComponent(id_c2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(id_veh)
                                    .addComponent(titre_veh))
                                .addGap(40, 40, 40))
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel23))))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(48, 48, 48)
                    .addComponent(groupe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(48, Short.MAX_VALUE)))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 2, 10, 2, new java.awt.Color(0, 102, 153)));

        tableau_candidat2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "Id candidat", "Nom", "Prénom", "Date naissance"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableau_candidat2.setToolTipText("");
        tableau_candidat2.setColorBackgoundHead(new java.awt.Color(0, 102, 153));
        tableau_candidat2.setColorFilasBackgound2(new java.awt.Color(255, 255, 255));
        tableau_candidat2.setColorFilasForeground1(new java.awt.Color(0, 0, 0));
        tableau_candidat2.setColorFilasForeground2(new java.awt.Color(0, 0, 0));
        tableau_candidat2.setColorSelBackgound(new java.awt.Color(0, 102, 153));
        tableau_candidat2.setFillsViewportHeight(true);
        tableau_candidat2.setFocusCycleRoot(true);
        tableau_candidat2.setFuenteFilas(new java.awt.Font("Arial", 1, 15)); // NOI18N
        tableau_candidat2.setFuenteFilasSelect(new java.awt.Font("Arial", 1, 15)); // NOI18N
        tableau_candidat2.setFuenteHead(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tableau_candidat2.setRowHeight(50);
        jScrollPane3.setViewportView(tableau_candidat2);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 694, Short.MAX_VALUE)
                .addContainerGap())
        );

        tableau_candidat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID candidat", "Nom", "Prenom", "Date de naissance"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableau_candidat.setColorBackgoundHead(new java.awt.Color(0, 102, 153));
        tableau_candidat.setColorFilasBackgound2(new java.awt.Color(255, 255, 255));
        tableau_candidat.setColorFilasForeground1(new java.awt.Color(0, 0, 0));
        tableau_candidat.setColorFilasForeground2(new java.awt.Color(0, 0, 0));
        tableau_candidat.setColorSelBackgound(new java.awt.Color(0, 102, 153));
        tableau_candidat.setFillsViewportHeight(true);
        tableau_candidat.setFocusCycleRoot(true);
        tableau_candidat.setFuenteFilas(new java.awt.Font("Arial", 1, 15)); // NOI18N
        tableau_candidat.setFuenteFilasSelect(new java.awt.Font("Arial", 1, 15)); // NOI18N
        tableau_candidat.setFuenteHead(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tableau_candidat.setRowHeight(50);
        jScrollPane1.setViewportView(tableau_candidat);

        jPanel15.setBackground(new java.awt.Color(0, 102, 153));

        type_rech.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Nom", "Prenom", "Adresse", "Email", "Groupe" }));
        type_rech.setColorBorde(new java.awt.Color(255, 255, 255));
        type_rech.setColorFondo(new java.awt.Color(0, 102, 153));
        type_rech.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N

        recherche.setBackground(new java.awt.Color(0, 102, 153));
        recherche.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        recherche.setForeground(new java.awt.Color(255, 255, 255));
        recherche.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        recherche.setOpaque(false);
        recherche.setPhColor(new java.awt.Color(255, 255, 255));
        recherche.setPlaceholder("Tapez votre recherche Ici ..");
        recherche.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rechercheActionPerformed(evt);
            }
        });

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ser.png"))); // NOI18N
        jLabel18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel18MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(type_rech, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(recherche, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 76, Short.MAX_VALUE)
                .addComponent(jLabel18)
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel18)
                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(type_rech, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(recherche, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(27, 27, 27))
        );

        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/images.jpg"))); // NOI18N
        jLabel20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel20MouseClicked(evt);
            }
        });

        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/auto_ecole_application/images2.jpg"))); // NOI18N
        jLabel22.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel22MouseClicked(evt);
            }
        });

        id_c.setFont(new java.awt.Font("Algerian", 1, 18)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel20)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel22)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 852, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(80, 80, 80))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(id_c)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(238, 238, 238))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(20, 20, 20)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(id_c)
                        .addGap(47, 47, 47)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 731, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(152, 152, 152)
                        .addComponent(jLabel20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(45, 45, 45)
                                .addComponent(jLabel21))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel22)))))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel6MouseClicked

    private void jPanel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel2MouseClicked

    private void groupeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_groupeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_groupeActionPerformed

    private void rechercheActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rechercheActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rechercheActionPerformed

    private void jLabel18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel18MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel18MouseClicked

    private void jLabel20MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel20MouseClicked
        // TODO add your handling code here:
        if (id_c.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Selectionner un candidat à  ajouter ! ", null, JOptionPane.ERROR_MESSAGE);
        } else {
            ajouter_liste(id_c.getText());
        }
    }//GEN-LAST:event_jLabel20MouseClicked

    private void jLabel22MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel22MouseClicked
        // TODO add your handling code here:
        if (id_c2.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Selectionner un candidat à retirer  ! ", null, JOptionPane.ERROR_MESSAGE);
        } else {
            supprimer_liste(id_c2.getText());
        }
    }//GEN-LAST:event_jLabel22MouseClicked

    private void jLabel23MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel23MouseClicked
        // TODO add your handling code here:
        model2 = (DefaultTableModel) tableau_candidat2.getModel();
        model = (DefaultTableModel) tableau_candidat.getModel();
        int id_groupe = 0;
        if (groupe.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, " Veuillez saisir un nom de groupe ! ", null, JOptionPane.ERROR_MESSAGE);
        } else if (model2.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, " Veuillez selectionner au moins un candidat ! ", null, JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                con = c1.connection_db();
                String sql = "insert into groupe (nom) values (?)";
                PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                st.setString(1, groupe.getText());
                st.executeUpdate();
                rs = st.getGeneratedKeys();
                if (rs.next()) {
                    id_groupe = rs.getInt(1);
                }
                for (int i = 0; i < model2.getRowCount(); i++) {
                    String id_candidat = model2.getValueAt(i, 1).toString();
                    String sql2 = "update candidat set groupe =? where id_candidat =?";
                    PreparedStatement st2 = con.prepareStatement(sql2);
                    st2.setInt(1, id_groupe);
                    st2.setString(2, id_candidat);
                    st2.executeUpdate();
                }
                JOptionPane.showMessageDialog(null, "Groupe inseré avec succès ! ", null, JOptionPane.INFORMATION_MESSAGE);
                groupe.setText("");
                model2.setRowCount(0);
                liste_groupe();
                con.close();
            } catch (SQLIntegrityConstraintViolationException unique) {
                JOptionPane.showMessageDialog(null, "Un nom déjâ existant ! ", null, JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
                // System.out.println("" + ex.getClass().getSimpleName());
            }
        }
    }//GEN-LAST:event_jLabel23MouseClicked

    private void jLabel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseClicked
        if (groupe.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Aucun groupe n'est selectionné:", null, JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                con = c1.connection_db();
                String delete = "delete from groupe where nom=?";
                PreparedStatement st = con.prepareStatement(delete);
                st.setString(1, groupe.getText());
                int row_deleted = st.executeUpdate();
                if (row_deleted > 0) {
                    JOptionPane.showMessageDialog(null, "Le groupe " + groupe.getText() + " est supprimé avec succès ", null, JOptionPane.INFORMATION_MESSAGE);
                    groupe.setText("");
                    model2.setRowCount(0);
                    liste_groupe();
                } else {
                    JOptionPane.showMessageDialog(null, "Le nom de groupe n'existe pas ! ", null, JOptionPane.ERROR_MESSAGE);
                }
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erreur 404 :" + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jLabel9MouseClicked

    private void listItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listItemStateChanged
        selectionner_candidats_groupe();
    }//GEN-LAST:event_listItemStateChanged

    private void listMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_listMouseClicked

    private void rSButtonHover3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rSButtonHover3MouseClicked
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_rSButtonHover3MouseClicked
    public void selectionner_candidats_groupe() {
        if (list.getSelectedItem().equals("Aucun")) {
            JOptionPane.showMessageDialog(null, "Vous devez selectionner un groupe !", null, JOptionPane.ERROR_MESSAGE);
            model2.setRowCount(0);
            groupe.setText("");
        } else {
            String nom_groupe = list.getSelectedItem().toString();
            model2 = (DefaultTableModel) tableau_candidat2.getModel();
            model2.setRowCount(0);
            groupe.setText(nom_groupe);
            try {
                con = c1.connection_db();
                PreparedStatement st = con.prepareStatement("select id_candidat,c.nom,prenom,date_naissance from candidat c join groupe g on c.groupe = g.id  where g.nom = ? ");
                st.setString(1, nom_groupe);
                rs = st.executeQuery();
                while (rs.next()) {
                    String id_candidat = rs.getString("id_candidat");
                    String nom = rs.getString("nom");
                    String prenom = rs.getString("prenom");
                    Date date = rs.getDate("date_naissance");
                    Object[] obj = {model2.getRowCount() + 1, id_candidat, nom, prenom, date};
                    model2.addRow(obj);
                }
                tableau_2();
                con.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur  :" + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void liste_groupe() {
        try {
            con = c1.connection_db();
            PreparedStatement st = con.prepareStatement("select nom from groupe");
            rs = st.executeQuery();
            while (rs.next()) {
                for (int i = 0; i < list.getItemCount(); i++) {
                    Object nom = rs.getString("nom");
                    if (!list.getItemAt(i).toString().equals(nom.toString())) {
                        list.addItem(nom);
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erreur 404 :" + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        }
    }

    public void rechercher_candidat() {
        String type_recherche = type_rech.getSelectedItem().toString();
        String la_recherche = recherche.getText();
        if (la_recherche.isEmpty()) {
            model.setRowCount(0);
            selectionner_candidats();
        } else {
            model.setRowCount(0);
            try {
                con = c1.connection_db();
                PreparedStatement st = con.prepareStatement("select * from candidat where LOWER(" + type_recherche + ") LIKE ? and groupe is null");
                st.setString(1, la_recherche + "%");
                rs = st.executeQuery();
                int trouve = 0;
                while (rs.next()) {
                    trouve++;
                    String id = rs.getString("id_candidat");
                    String nom = rs.getString("nom");
                    String prenom = rs.getString("prenom");
                    Date date_naissance = rs.getDate("date_naissance");
                    Object[] obj = {id, nom, prenom, date_naissance};
                    model.addRow(obj);
                }
                if (trouve == 0) {
                    model.setRowCount(0);
                }
                con.close();
            } catch (Exception e) {

            }
        }
    }

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
            java.util.logging.Logger.getLogger(groupe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(groupe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(groupe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(groupe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new groupe().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel date_actuel_var;
    private app.bolivia.swing.JCTextField groupe;
    private javax.swing.JLabel id_c;
    private javax.swing.JLabel id_c2;
    private javax.swing.JLabel id_veh;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel jour_semaine;
    private rojerusan.RSComboMetro list;
    private rojeru_san.complementos.RSButtonHover rSButtonHover3;
    private app.bolivia.swing.JCTextField recherche;
    private rojerusan.RSTableMetro tableau_candidat;
    private rojerusan.RSTableMetro tableau_candidat2;
    private javax.swing.JLabel titre_veh;
    private rojerusan.RSComboMetro type_rech;
    // End of variables declaration//GEN-END:variables
}
