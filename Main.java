import java.awt.*;
import java.util.*;
import java.awt.event.*;    
import javax.swing.*; 
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;


class Main {  
    public static void main(String[] args) throws IOException { 
        // Data storage
        Map<String, Batch> B = new HashMap<>();
        Map<String, Course> CO = new HashMap<>();
        Map<String, Professor> PR = new HashMap<>();

        // Main frames
        JFrame f = new JFrame("Timetable Generator");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        JFrame s = new JFrame();  
        
        // ========== HOME PANEL ==========
        JPanel home = new JPanel();
        home.setLayout(null); // Keep null layout as in original code
        home.setBackground(new Color(255, 222, 173)); // Lighter orange
        
        JLabel l1 = new JLabel("Welcome to NIST Timetable Generator");
        l1.setBounds(250, 50, 850, 200);
        l1.setFont(new Font("Arial", Font.BOLD, 36));
        l1.setForeground(new Color(70, 131, 180)); // Steel blue
        
        // Fixed image loading with error handling
        BufferedImage image;
        try {
            image = ImageIO.read(new File("timetable.jpeg"));
        } catch (IOException e) {
            System.out.println("Could not load timetable image: " + e.getMessage());
            // Create a blank image with text as a fallback
            image = new BufferedImage(500, 300, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.getGraphics();
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 500, 300);
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Timetable Image Not Found", 100, 150);
            g.dispose();
        }
        
        JLabel label = new JLabel(new ImageIcon(image));
        label.setBounds(300, 20, 500, 600);
        
        JLabel instructionLabel = new JLabel("Please use the tabs above to navigate");
        instructionLabel.setBounds(400, 500, 400, 30);
        instructionLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        
        home.add(l1);
        home.add(label);
        home.add(instructionLabel);

        // ========== VIEW PANEL ==========
        JPanel view = new JPanel();
        view.setLayout(null); // Keep null layout
        view.setBackground(new Color(230, 230, 250)); // Lavender
        
        String[] Timet = {"Select"};
        String[] profs = {"Select"};  
        String[] credits = {"1", "2", "3", "4", "5", "6"};
        String[] course = {"Select"};
        JComboBox cr = new JComboBox(credits);
        JComboBox cb = new JComboBox(Timet); 
        JComboBox pf = new JComboBox(profs);
        JComboBox co = new JComboBox(course);
        
        JLabel vl = new JLabel("Timetable View");
        vl.setBounds(530, 5, 250, 70);
        vl.setFont(new Font("Arial", Font.BOLD, 24)); 
        vl.setForeground(new Color(25, 25, 112)); // Midnight blue
        
        JLabel batchLabel = new JLabel("Select Batch:");
        batchLabel.setBounds(400, 70, 100, 20);
        batchLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        cb.setBounds(475, 70, 100, 20);
        
        JLabel profLabel = new JLabel("Select Professor:");
        profLabel.setBounds(600, 70, 120, 20);
        profLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        pf.setBounds(700, 70, 100, 20);
        
        // Table setup
        final String timet[][] = { 
            {"Monday", "", "", "", "", "", "", "", ""},    
            {"Tuesday", "", "", "", "", "", "", "", ""},    
            {"Wednesday", "", "", "", "", "", "", "", ""},
            {"Thursday", "", "", "", "", "", "", "", ""}, 
            {"Friday", "", "", "", "", "", "", "", ""}, 
            {"Saturday", "", "", "", "", "", "", "", ""}
        };
        String column[] = {"", "1", "2", "3", "4", "5", "6", "7", "8"};
        
        JTable jt = new JTable(timet, column);
        jt.setRowHeight(35);
        jt.getTableHeader().setFont(new Font("Arial", Font.BOLD, 25));
        jt.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JScrollPane sp = new JScrollPane(jt);
        sp.setBounds(100, 150, 1000, 255);
     
        
        // ComboBox action listeners - keeping the same as original
        cb.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {  
                if (cb.getSelectedIndex() > 0) {
                    for (int i = 0; i < 6; i++) {
                        for (int j = 0; j < 8; j++) {
                            timet[i][j+1] = B.get(cb.getItemAt(cb.getSelectedIndex()).toString()).a[i][j];
                        }
                    }
                    view.revalidate();
                    view.repaint();
                }
            }
        });
        
        pf.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {  
                if (pf.getSelectedIndex() > 0) {
                    for (int i = 0; i < 6; i++) {
                        for (int j = 0; j < 8; j++) {
                            timet[i][j+1] = PR.get(pf.getItemAt(pf.getSelectedIndex()).toString()).a[i][j];
                        }
                    }
                    view.revalidate();
                    view.repaint();
                }
            }
        });
        
        view.add(vl);
        view.add(batchLabel);
        view.add(cb);
        view.add(profLabel);
        view.add(pf);
        view.add(sp);

        // ========== DELETE PANEL ==========
        JPanel delete = new JPanel();
        delete.setLayout(null); // Keep null layout
        delete.setBackground(new Color(255, 228, 225)); // Misty rose
        
        // Course deletion section
        JLabel d1 = new JLabel("Delete Course");
        d1.setBounds(50, 50, 500, 50);
        d1.setFont(new Font("Arial", Font.BOLD, 30));
        d1.setForeground(new Color(178, 34, 34)); // Firebrick
        
        JLabel d2 = new JLabel("Course");
        d2.setBounds(50, 125, 200, 50);
        d2.setFont(new Font("Arial", Font.BOLD, 20));
        
        JComboBox d3 = new JComboBox(course);
        d3.setBounds(250, 125, 200, 50);
        
        JButton d4 = new JButton("Delete");
        d4.setBounds(550, 125, 200, 50);
        d4.setBackground(new Color(220, 20, 60)); // Crimson
        d4.setForeground(Color.BLACK);
        d4.setFont(new Font("Arial", Font.BOLD, 16));
        
        // Professor deletion section
        JLabel d5 = new JLabel("Delete Professor");
        d5.setBounds(50, 200, 500, 50);
        d5.setFont(new Font("Arial", Font.BOLD, 30));
        d5.setForeground(new Color(178, 34, 34));
        
        JLabel d6 = new JLabel("Professor");
        d6.setBounds(50, 275, 200, 50);
        d6.setFont(new Font("Arial", Font.BOLD, 20));
        
        JComboBox d7 = new JComboBox(profs);
        d7.setBounds(250, 275, 200, 50);
        
        JButton d8 = new JButton("Delete");
        d8.setBounds(550, 275, 200, 50);
        d8.setBackground(new Color(220, 20, 60));
        d8.setForeground(Color.BLACK);
        d8.setFont(new Font("Arial", Font.BOLD, 16));
        
        // Batch deletion section
        JLabel d9 = new JLabel("Delete Batch");
        d9.setBounds(50, 350, 500, 50);
        d9.setFont(new Font("Arial", Font.BOLD, 30));
        d9.setForeground(new Color(178, 34, 34));
        
        JLabel d10 = new JLabel("Batch");
        d10.setBounds(50, 425, 200, 50);
        d10.setFont(new Font("Arial", Font.BOLD, 20));
        
        JComboBox d11 = new JComboBox(Timet);
        d11.setBounds(250, 425, 200, 50);
        
        JButton d12 = new JButton("Delete");
        d12.setBounds(550, 425, 200, 50);
        d12.setBackground(new Color(220, 20, 60));
        d12.setForeground(Color.BLACK);
        d12.setFont(new Font("Arial", Font.BOLD, 16));
        
        delete.add(d1);
        delete.add(d2);
        delete.add(d3);
        delete.add(d4);
        delete.add(d5);
        delete.add(d6);
        delete.add(d7);
        delete.add(d8);
        delete.add(d9);
        delete.add(d10);
        delete.add(d11);
        delete.add(d12);

        // ========== CREATE BATCH PANEL ==========
        JPanel createBatch = new JPanel();
        createBatch.setLayout(null); // Keep null layout
        createBatch.setBackground(new Color(240, 255, 240)); // Honeydew
        
        JLabel batchTitle = new JLabel("Create New Batch");
        batchTitle.setBounds(50, 20, 300, 50);
        batchTitle.setFont(new Font("Arial", Font.BOLD, 30));
        batchTitle.setForeground(new Color(34, 139, 34)); // Forest green
        createBatch.add(batchTitle);
        
        JLabel cb1 = new JLabel("Name");
        cb1.setBounds(50, 100, 200, 50);
        cb1.setFont(new Font("Arial", Font.BOLD, 20)); 
        
        JTextField cb2 = new JTextField();
        cb2.setBounds(250, 100, 200, 50);
        cb2.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JButton cb6 = new JButton("Create");
        cb6.setBounds(550, 100, 200, 50);
        cb6.setBackground(new Color(46, 139, 87)); // Sea green
        cb6.setForeground(Color.BLACK);
        cb6.setFont(new Font("Arial", Font.BOLD, 16));
        
        JLabel cb3 = new JLabel("Courses");
        cb3.setBounds(50, 275, 200, 50);
        cb3.setFont(new Font("Arial", Font.BOLD, 20)); 
        
        JComboBox cb4 = new JComboBox(course);
        cb4.setBounds(250, 275, 200, 50);
        
        JComboBox cb7 = new JComboBox(profs);
        cb7.setBounds(550, 275, 200, 50);
        
        JButton cb5 = new JButton("ADD");
        cb5.setBounds(850, 275, 200, 50);
        cb5.setBackground(new Color(46, 139, 87));
        cb5.setForeground(Color.BLACK);
        cb5.setFont(new Font("Arial", Font.BOLD, 16));
        
        // Batch action listeners - keeping the same as original
        cb6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cb.addItem(cb2.getText());
                d11.addItem(cb2.getText());
                B.put(cb2.getText(), new Batch());
                B.get(cb2.getText()).setName(cb2.getText());
                JOptionPane.showMessageDialog(s, "A new batch " + cb2.getText() + " has been created");
            }
        });
        
        cb5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cb4.getSelectedIndex() != 0 && cb7.getSelectedIndex() != 0) {
                    if (B.get(cb2.getText()).add(CO.get(cb4.getItemAt(cb4.getSelectedIndex()).toString()), 
                                               PR.get(cb7.getItemAt(cb7.getSelectedIndex()).toString()))) {
                        JOptionPane.showMessageDialog(s, "A new course has been added to the batch " + cb2.getText() + 
                                                     "\n" + "Course: " + cb4.getItemAt(cb4.getSelectedIndex()).toString() + 
                                                     "\n" + "Professor: " + cb7.getItemAt(cb7.getSelectedIndex()).toString());
                    } else {
                        JOptionPane.showMessageDialog(s, "Periods are insufficient for adding this course to the batch");
                    }
                } else {
                    JOptionPane.showMessageDialog(s, "Select a valid course and valid professor");
                }
            }
        });
        
        cb4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cb4.getSelectedIndex() > 0) {
                    cb7.removeAllItems();
                    cb7.addItem("Select");
                    for (int i = 0; i < CO.get(cb4.getItemAt(cb4.getSelectedIndex()).toString()).getProfsSize(); i++) {
                        cb7.addItem(CO.get(cb4.getItemAt(cb4.getSelectedIndex()).toString()).getProfsItem(i));
                    }
                }
            }
        });
        
        createBatch.add(cb1);
        createBatch.add(cb2);
        createBatch.add(cb3);
        createBatch.add(cb4);
        createBatch.add(cb5);
        createBatch.add(cb6);
        createBatch.add(cb7);

        // ========== CREATE PROFESSOR PANEL ==========
        JPanel createProfessor = new JPanel();
        createProfessor.setLayout(null); // Keep null layout
        createProfessor.setBackground(new Color(240, 248, 255)); // Alice blue
        
        JLabel profTitle = new JLabel("Create New Professor");
        profTitle.setBounds(50, 20, 400, 50);
        profTitle.setFont(new Font("Arial", Font.BOLD, 30));
        profTitle.setForeground(new Color(70, 130, 180)); // Steel blue
        createProfessor.add(profTitle);
        
        JLabel cp1 = new JLabel("Name");
        cp1.setBounds(50, 100, 200, 50);
        cp1.setFont(new Font("Arial", Font.BOLD, 20));
        
        JTextField cp2 = new JTextField();
        cp2.setBounds(250, 100, 200, 50);
        cp2.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JButton cp6 = new JButton("Create");
        cp6.setBounds(550, 100, 200, 50);
        cp6.setBackground(new Color(70, 130, 180));
        cp6.setForeground(Color.BLACK);
        cp6.setFont(new Font("Arial", Font.BOLD, 16));
        
        JLabel cp3 = new JLabel("Course");
        cp3.setBounds(50, 275, 200, 50);
        cp3.setFont(new Font("Arial", Font.BOLD, 20));
        
        JComboBox cp4 = new JComboBox(course);
        cp4.setBounds(250, 275, 200, 50);
        
        JButton cp5 = new JButton("Add");
        cp5.setBounds(550, 275, 200, 50);
        cp5.setBackground(new Color(70, 130, 180));
        cp5.setForeground(Color.BLACK);
        cp5.setFont(new Font("Arial", Font.BOLD, 16));
        
        // Professor action listeners - keeping the same as original
        cp6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pf.addItem(cp2.getText());
                cb7.addItem(cp2.getText());
                d7.addItem(cp2.getText());
                PR.put(cp2.getText(), new Professor());
                PR.get(cp2.getText()).setName(cp2.getText());
                JOptionPane.showMessageDialog(s, "A new professor " + cp2.getText() + " has been created");
            }
        });
        
        cp5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cp4.getSelectedIndex() != 0) {
                    CO.get(cp4.getItemAt(cp4.getSelectedIndex()).toString()).addProfs(cp2.getText());
                    PR.get(cp2.getText()).addCourses(cp4.getItemAt(cp4.getSelectedIndex()).toString());
                    JOptionPane.showMessageDialog(s, "The course " + cp4.getItemAt(cp4.getSelectedIndex()).toString() + 
                                                 " has been assigned to " + cp2.getText());
                } else {
                    JOptionPane.showMessageDialog(s, "Select a valid course!");
                }
            }
        });
        
        createProfessor.add(cp1);
        createProfessor.add(cp2);
        createProfessor.add(cp3);
        createProfessor.add(cp4);
        createProfessor.add(cp5);
        createProfessor.add(cp6);

        // ========== CREATE COURSE PANEL ==========
        JPanel createCourse = new JPanel();
        createCourse.setLayout(null); // Keep null layout
        createCourse.setBackground(new Color(255, 250, 240)); // Floral white
        
        JLabel courseTitle = new JLabel("Create New Course");
        courseTitle.setBounds(50, 20, 400, 50);
        courseTitle.setFont(new Font("Arial", Font.BOLD, 30));
        courseTitle.setForeground(new Color(210, 105, 30)); // Chocolate
        createCourse.add(courseTitle);
        
        JLabel cc1 = new JLabel("Name");
        cc1.setBounds(50, 100, 200, 50);
        cc1.setFont(new Font("Arial", Font.BOLD, 20));
        
        JTextField cc2 = new JTextField();
        cc2.setBounds(250, 100, 200, 50);
        cc2.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel cc3 = new JLabel("Credits");
        cc3.setBounds(50, 275, 200, 50);
        cc3.setFont(new Font("Arial", Font.BOLD, 20));
        
        JComboBox cc4 = new JComboBox(credits);
        cc4.setBounds(250, 275, 200, 50);
        
        JButton cc5 = new JButton("Create");
        cc5.setBounds(250, 450, 200, 50);
        cc5.setBackground(new Color(210, 105, 30));
        cc5.setForeground(Color.BLACK);
        cc5.setFont(new Font("Arial", Font.BOLD, 16));
        
        // Course action listener - keeping the same as original
        cc5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { 
                d3.addItem(cc2.getText());
                cb4.addItem(cc2.getText());
                cp4.addItem(cc2.getText());
                CO.put(cc2.getText(), new Course());
                CO.get(cc2.getText()).setName(cc2.getText());
                CO.get(cc2.getText()).setCredits(Integer.parseInt(cc4.getItemAt(cc4.getSelectedIndex()).toString()));
                JOptionPane.showMessageDialog(s, "A new course has been created" + "\n" + 
                                             "Course: " + cc2.getText() + "\n" + 
                                             "Credits: " + Integer.parseInt(cc4.getItemAt(cc4.getSelectedIndex()).toString()));        
            }
        });
        
        createCourse.add(cc1);
        createCourse.add(cc2);
        createCourse.add(cc3);
        createCourse.add(cc4);
        createCourse.add(cc5);

        // Add all panels to tabbed pane with improved titles
        JTabbedPane t = new JTabbedPane();
        t.setFont(new Font("Arial", Font.BOLD, 14));
        t.setBackground(new Color(200, 255, 200));
        t.setForeground(new Color(50, 50, 50));
        
        t.add("Home", home);  
        t.add("View Timetable", view);  
        t.add("Create Batch", createBatch);
        t.add("Create Course", createCourse);
        t.add("Create Professor", createProfessor);
        t.add("Delete Items", delete); 
        
        t.setBounds(50, 50, 1200, 600); 
        
        // Set up main frame
        f.add(t);  
        f.setSize(1300, 700);  
        f.setLayout(null); 
        f.getContentPane().setBackground(new Color(245, 245, 245));
        f.setVisible(true);
    }
}