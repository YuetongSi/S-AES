package UI;
import CBCMode.CBC;
import MultipleEncryptionAndDecryption.MultipleEncryptandDecrypt;
import SAES.SAES_Decrypt;
import SAES.SAES_encrypt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.sun.javafx.fxml.expression.Expression.add;

/**
 * UI 类用于创建SAES加密解密系统的图形用户界面。
 * 该界面包括三个主要功能：S-AES基本加密解密、多重加密解密和CBC模式加密解密。
 */
public class UI {
    JFrame f;
    JPanel saesPanel,MultiPanel,CBCPanel;
    JLabel jlab1, jlab2,jlab3,jlab4,jlab12,jlab121,jlab13,jlab22,jlab23,jlab32,jlab33,jlab42,jlab43;
    JTextField keytext,keytext2,keytext3,text,text2,text3,output,output2,output3, initialvectortext;
    //    JTextArea output;
    JButton btn1,btn2,btn3,btn12,btn22,btn32,btn13,btn23,btn33;
    JRadioButton bitButton, asciiButton,DoubleButton,TripleButton;
    public JTabbedPane tabbedPane;
    /**
     * UI类的构造方法，初始化界面组件。
     */
    public UI() {
        tabbedPane = new JTabbedPane();
//        jp = panel;
        f = new JFrame("SAES加密解密系统");
//        jp.setBackground(new Color(235, 245, 241));

        saesPanel=new JPanel();
        saesPanel.setBackground(new Color(235, 245, 241));

        MultiPanel=new JPanel();
        MultiPanel.setBackground(new Color(235, 245, 241));

        CBCPanel =new JPanel();
        CBCPanel.setBackground(new Color(235, 245, 241));

        jlab1 = new JLabel("请输入加解密使用的4位十六进制密钥");
        jlab1.setFont(new Font("楷体", 10, 18));
        jlab12 = new JLabel("若使用双重加密，请输入8位十六进制密钥");
        jlab12.setFont(new Font("楷体", 10, 18));
        jlab121 = new JLabel("若使用三重加密，请输入16位十六进制密钥");
        jlab121.setFont(new Font("楷体", 10, 18));
        jlab13 = new JLabel("请输入加解密使用的4位十六进制密钥");
        jlab13.setFont(new Font("楷体", 10, 18));

        keytext = new JTextField(45);
        keytext.setPreferredSize(new Dimension (400,35));
        keytext2 = new JTextField(45);
        keytext2.setPreferredSize(new Dimension (400,35));
        keytext3 = new JTextField(45);
        keytext3.setPreferredSize(new Dimension (400,35));

        text = new JTextField(55);
        text.setPreferredSize(new Dimension (400,35));
        text2 = new JTextField(55);
        text2.setPreferredSize(new Dimension (400,35));
        text3 = new JTextField(55);
        text3.setPreferredSize(new Dimension (400,35));
        initialvectortext = new JTextField(55);
        initialvectortext.setPreferredSize(new Dimension (400,35));


        jlab2 = new JLabel("请输入加解密使用的原文或密文");
        jlab2.setFont(new Font("楷体", 20, 18));
        jlab22 = new JLabel("请输入加解密使用的原文或密文");
        jlab22.setFont(new Font("楷体", 20, 18));
        jlab23 = new JLabel("请输入加解密使用的64bits原文或密文");
        jlab23.setFont(new Font("楷体", 20, 18));

        jlab3 = new JLabel("请选择编码类型");
        jlab3.setFont(new Font("楷体", 10, 18));
        jlab32 = new JLabel("请选择多重加密类型");
        jlab32.setFont(new Font("楷体", 10, 18));
        jlab33 = new JLabel("请输入16bits初始向量");
        jlab33.setFont(new Font("楷体", 10, 18));

        ButtonGroup group = new ButtonGroup();
        bitButton = new JRadioButton("Bit", false);
        bitButton.setBackground(new Color(168, 203, 195));
        asciiButton = new JRadioButton("ASCII", false);
        asciiButton.setBackground(new Color(168, 203, 195));
        group.add(bitButton);
        group.add(asciiButton);
        JPanel northPanel = new JPanel();
        northPanel.add(bitButton);
        northPanel.add(asciiButton);
        add(northPanel, BorderLayout.NORTH);

        ButtonGroup group2 = new ButtonGroup();
        DoubleButton = new JRadioButton("双重加密", false);
        DoubleButton.setBackground(new Color(168, 203, 195));
        DoubleButton.setFont(new Font("楷体", Font.PLAIN, 16));
        TripleButton = new JRadioButton("三重加密", false);
        TripleButton.setBackground(new Color(168, 203, 195));
        TripleButton.setFont(new Font("楷体", Font.PLAIN, 16));
        group2.add(DoubleButton);
        group2.add(TripleButton);
        JPanel northPanel2 = new JPanel();
        northPanel2.add(DoubleButton);
        northPanel2.add(TripleButton);
        add(northPanel2, BorderLayout.NORTH);

        btn1 = new JButton("解密");
        btn1.setFont(new java.awt.Font("楷体", 1, 18));
        btn1.setBackground(new Color(168, 203, 195));
        btn1.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn2 = new JButton("加密");
        btn2.setFont(new java.awt.Font("楷体", 1, 18));
        btn2.setBackground(new Color(168, 203, 195));
        btn2.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn3 = new JButton("全部重置");
        btn3.setFont(new java.awt.Font("楷体", 1, 18));
        btn3.setBackground(new Color(168, 203, 195));
        btn3.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        btn12 = new JButton("解密");
        btn12.setFont(new java.awt.Font("楷体", 1, 18));
        btn12.setBackground(new Color(168, 203, 195));
        btn12.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn22 = new JButton("加密");
        btn22.setFont(new java.awt.Font("楷体", 1, 18));
        btn22.setBackground(new Color(168, 203, 195));
        btn22.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn32 = new JButton("全部重置");
        btn32.setFont(new java.awt.Font("楷体", 1, 18));
        btn32.setBackground(new Color(168, 203, 195));
        btn32.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        btn13 = new JButton("解密");
        btn13.setFont(new java.awt.Font("楷体", 1, 18));
        btn13.setBackground(new Color(168, 203, 195));
        btn13.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn23 = new JButton("加密");
        btn23.setFont(new java.awt.Font("楷体", 1, 18));
        btn23.setBackground(new Color(168, 203, 195));
        btn23.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn33 = new JButton("全部重置");
        btn33.setFont(new java.awt.Font("楷体", 1, 18));
        btn33.setBackground(new Color(168, 203, 195));
        btn33.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        jlab4 = new JLabel("结果");
        jlab4.setFont(new Font("楷体", 10, 19));
        jlab42 = new JLabel("结果");
        jlab42.setFont(new Font("楷体", 10, 19));
        jlab43 = new JLabel("结果");
        jlab43.setFont(new Font("楷体", 10, 19));
//        output = new JTextArea(5, 7);
        output=new JTextField();
        output.setPreferredSize(new Dimension (400,80));
        output2=new JTextField();
        output2.setPreferredSize(new Dimension (400,80));
        output3=new JTextField();
        output3.setPreferredSize(new Dimension (400,80));
//        jp.setLayout(null);
    }

    public void displayWindow(){

        Box vBox = Box.createVerticalBox();
        vBox.add(Box.createVerticalStrut(30));
        vBox.add(jlab1);
        vBox.add(Box.createVerticalStrut(7));
        vBox.add(keytext);
        vBox.add(Box.createVerticalStrut(25));
        vBox.add(jlab2);
        vBox.add(Box.createVerticalStrut(7));
        vBox.add(text);
        vBox.add(Box.createVerticalStrut(23));
        //请选择编码类型
        vBox.add(jlab3);

        vBox.add(Box.createVerticalStrut(9));
        vBox.add(bitButton);
        vBox.add(Box.createVerticalStrut(10));
        vBox.add(asciiButton);


        vBox.add(Box.createVerticalStrut(20));
        vBox.add(jlab4);
        vBox.add(Box.createVerticalStrut(7));
        vBox.add(output);
        vBox.add(Box.createVerticalStrut(60));

        Box hBox = Box.createHorizontalBox();
        hBox.add(btn1);
        hBox.add(Box.createHorizontalStrut(20));
        hBox.add(btn2);
        hBox.add(Box.createHorizontalStrut(20));
        hBox.add(btn3);

        saesPanel.add(vBox);
        saesPanel.add(hBox);

        //多重加密
        Box vBox2 = Box.createVerticalBox();
        vBox2.add(Box.createVerticalStrut(30));
        vBox2.add(jlab12);
        vBox2.add(jlab121);
        vBox2.add(Box.createVerticalStrut(7));
        vBox2.add(keytext2);
        vBox2.add(Box.createVerticalStrut(25));
        vBox2.add(jlab22);
        vBox2.add(Box.createVerticalStrut(7));
        vBox2.add(text2);
        vBox2.add(Box.createVerticalStrut(23));
        //请选择加密类型
        vBox2.add(jlab32);

        vBox2.add(Box.createVerticalStrut(9));
        vBox2.add(DoubleButton);
        vBox2.add(Box.createVerticalStrut(10));
        vBox2.add(TripleButton);


        vBox2.add(Box.createVerticalStrut(20));
        vBox2.add(jlab42);
        vBox2.add(Box.createVerticalStrut(7));
        vBox2.add(output2);
        vBox2.add(Box.createVerticalStrut(60));

        Box hBox2 = Box.createHorizontalBox();
        hBox2.add(btn12);
        hBox2.add(Box.createHorizontalStrut(20));
        hBox2.add(btn22);
        hBox2.add(Box.createHorizontalStrut(20));
        hBox2.add(btn32);

        MultiPanel.add(vBox2);
        MultiPanel.add(hBox2);

        //cbc
        Box vBox3 = Box.createVerticalBox();
        vBox3.add(Box.createVerticalStrut(30));
        vBox3.add(jlab13);
        vBox3.add(Box.createVerticalStrut(7));
        vBox3.add(keytext3);
        vBox3.add(Box.createVerticalStrut(25));
        vBox3.add(jlab23);
        vBox3.add(Box.createVerticalStrut(7));
        vBox3.add(text3);
        vBox3.add(Box.createVerticalStrut(23));
        //请选择加密类型
        vBox3.add(jlab33);
        vBox3.add(Box.createVerticalStrut(9));
        vBox3.add(initialvectortext);


        vBox3.add(Box.createVerticalStrut(20));
        vBox3.add(jlab43);
        vBox3.add(Box.createVerticalStrut(7));
        vBox3.add(output3);
        vBox3.add(Box.createVerticalStrut(60));

        Box hBox3 = Box.createHorizontalBox();
        hBox3.add(btn13);
        hBox3.add(Box.createHorizontalStrut(20));
        hBox3.add(btn23);
        hBox3.add(Box.createHorizontalStrut(20));
        hBox3.add(btn33);

        CBCPanel.add(vBox3);
        CBCPanel.add(hBox3);


        tabbedPane.addTab("S-AES基本加密", saesPanel);//添加选项卡进选项卡面板
        tabbedPane.setFont(new  java.awt.Font("黑体",  1,  15));
        tabbedPane.addTab("多重加密", MultiPanel);//添加选项卡进选项卡面板
        tabbedPane.setFont(new  java.awt.Font("黑体",  1,  15));
        tabbedPane.addTab("CBC模式加密", CBCPanel);//添加选项卡进选项卡面板
        tabbedPane.setFont(new  java.awt.Font("黑体",  1,  15));

        f.setContentPane(tabbedPane);
        f.setSize(750, 630);

        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

        //解密按钮监听器
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = text.getText();
                String key = keytext.getText();

                if (key.length() != 4) {
                    JOptionPane.showMessageDialog(null, "请输入4位16进制密钥！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (input.length() != 16) {
                    JOptionPane.showMessageDialog(null, "请输入16bits明密文！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean isASCII = asciiButton.isSelected();
                if (isASCII) {
                    String plaintext= SAES_Decrypt.decryptAsciiText(input, key);
                    output.setText("ASCII类型解密输出结果: " + plaintext);
                } else {
                    String Bplaintext = SAES_Decrypt.decryptBinary(input, key);
                    output.setText("数组类型解密输出结果: " + Bplaintext + "\n");
                }

            }
        });
        //多重解密按钮监听器
        btn12.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = text2.getText();
                String key = keytext2.getText();

//                if (key.length() != 4) {
//                    JOptionPane.showMessageDialog(null, "请输入10bits密钥！", "错误", JOptionPane.ERROR_MESSAGE);
//                    return;
//                }

                boolean isDouble = DoubleButton.isSelected();
                if (isDouble) {
                    String plaintext= MultipleEncryptandDecrypt.doubleDecrypt(input, key);
                    output2.setText("双重解密输出结果: " + plaintext);
                } else {
                    String Bplaintext = MultipleEncryptandDecrypt.tripleDecrypt(input, key);
                    output2.setText("三重解密输出结果: " + Bplaintext + "\n");
                }

            }
        });
        //cbc解密按钮监听器
        btn13.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = text3.getText();
                String key = keytext3.getText();
                String initialVector=initialvectortext.getText();

                if (key.length() != 4) {
                    JOptionPane.showMessageDialog(null, "请输入4位密钥！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (initialVector.length() != 16) {
                    JOptionPane.showMessageDialog(null, "请输入16bits初始向量！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (input.length() != 64) {
                    JOptionPane.showMessageDialog(null, "请输入64bits明密文！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String ciphertext = CBC.CBCModeDecrypt(input, initialVector,key);
                output3.setText("CBC模式下解密输出结果:" + ciphertext);

            }
        });


        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = text.getText();
                String key = keytext.getText();

                if (key.length() != 4) {
                    JOptionPane.showMessageDialog(null, "请输入4位16进制密钥！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (input.length() != 16) {
                    JOptionPane.showMessageDialog(null, "请输入16bits明密文！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                StringBuilder ciphertext = new StringBuilder();
                boolean isASCII = asciiButton.isSelected();
                if (isASCII) {
                    String encrypted = SAES_encrypt.encryptASCII(input, key);
                    output.setText("ASCII类型加密输出结果: " + encrypted);
                } else {
                    String Bciphertext=SAES_encrypt.encryptText(input,key);
                    output.setText("数组类型加密输出结果: " + Bciphertext + "\n");

                }


            }
        });

        btn22.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = text2.getText();
                String key = keytext2.getText();

                boolean isDouble = DoubleButton.isSelected();
                if (isDouble) {
                    String plaintext= MultipleEncryptandDecrypt.doubleEncrypt(input, key);
                    output2.setText("双重加密输出结果: " + plaintext);
                } else {
                    String Bplaintext = MultipleEncryptandDecrypt.tripleEncrypt(input, key);
                    output2.setText("三重加密输出结果: " + Bplaintext + "\n");
                }

            }
        });

        //cbc加密
        btn23.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = text3.getText();
                String key = keytext3.getText();
                String initialVector=initialvectortext.getText();

                if (key.length() != 4) {
                    JOptionPane.showMessageDialog(null, "请输入4位密钥！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (initialVector.length() != 16) {
                    JOptionPane.showMessageDialog(null, "请输入16bits初始向量！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (input.length() != 64) {
                    JOptionPane.showMessageDialog(null, "请输入64bits明密文！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String ciphertext = CBC.CBCModeEncrypt(input, initialVector,key);
                output3.setText("CBC模式下加密输出结果:" + ciphertext);

            }
        });

        btn3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keytext.setText("");
                text.setText("");
//                .setText("");
                output.setText("");


            }
        });
        btn32.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keytext2.setText("");
                text2.setText("");
//                .setText("");
                output2.setText("");


            }
        });
        btn33.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keytext3.setText("");
                text3.setText("");
//                .setText("");
                output3.setText("");


            }
        });

    }

    /**
     * 主方法，程序入口，设置界面风格并显示窗口。
     * @param args 命令行参数
     */
    public static void main(String []args)
    {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }catch(Exception e) {
            System.out.println(e);
        }
        UI win=new UI();
        win.displayWindow();


    }

}
