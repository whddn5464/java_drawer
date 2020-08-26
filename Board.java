package hi;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.awt.*;

public class Board extends JPanel implements Serializable {
   Figure[] point;
   BufferedImage img=null;
   private JButton backbt, glassbt;
   private int choice, x, y, m, n, width, height;
   private int state,glassX, glassY,glassed,glass;
   private List<Figure>  figures = new ArrayList<Figure>();
   private JRadioButton fillButton;
   private JRadioButton unfillButton;
   private ButtonGroup fillGroup;
   private Color color;
   private int fill, thick;//채우기 굵기 설정
   private JTextField ThickText;
   private static final String[] colors = {"Black", "Red", "Green", "Blue","Yellow","Pink"};
   private JComboBox colorComboBox;
   private Color[] colorSet = {Color.BLACK, Color.RED, Color.GREEN, Color.BLUE,Color.YELLOW,Color.PINK};
   private JComboBox shapeBox;
   private static final String[] shapes = {"brush", "line", "rect", "oval","tirangle","roundrect",
         "pentagon","hexagon","rhombus","trapezoid","star","erase"};
   private JPanel menu;
   public Board()
   {
      menu = new JPanel();
      menu.setBackground(Color.CYAN);
      choice = 0; x = 0; y = 0; width = 0; height = 0; glassed=0;//0이면 확대x
      color = Color.BLACK; fill = 0; thick = 5;
      this.setBackground(Color.WHITE);
      //메뉴바
       JMenuBar menuBar = new JMenuBar();
        JMenu mnFile = new JMenu("Function");
        mnFile.setMnemonic('f');
        menuBar.add(mnFile);
        JMenuItem mntmNew = new JMenuItem("New");
        mntmNew.setMnemonic('n');
         mntmNew.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
               figures.clear();
                x = 0; y = 0; width = 0; height = 0;//
            }
         });
         JMenuItem mntmErase = new JMenuItem("Image erase");
         mntmErase.setMnemonic('e');
         mntmErase.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {   
               
              img=null;
               
               repaint();
           }
         });
         mnFile.add(mntmErase);
         JMenuItem mntmImage = new JMenuItem("Image");
         mntmImage.setMnemonic('i');
         mntmImage.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
               
               
               JFileChooser jfc = new JFileChooser("C://Users//김종우//workspace//그래픽");
               jfc.showDialog(null, "확인");
               File file = jfc.getSelectedFile(); //파일 선택창 선언
               try {
                   img = ImageIO.read(file);// 윈도우에선 경로가 \라서 \\라고 입력해줘야 한다.
               } catch (IOException e1) {// ImageIO이것을 적으면 catch문으로 예외처리를 해야 한다. 예외처리를 하도록 클래스 설계자가 적어두어서 반드시 해줘야 한다.
                   // 입력과 출력이 rfid, 소켓통신, 윈도우, 임베디드 등등 입출력이 너무 다양하기 때문에..
                   e1.printStackTrace();
                   JOptionPane.showMessageDialog(null, e1.getMessage());
                   JOptionPane.showMessageDialog(null, "잘못된 입력입니다");
                   return;
                   // 아이폰은 예외처리를 표시도 안하고(공백, 오류나도 안알려줌), 윈도우는 블루스크린을 띄워라는 것이 담겨있다.
               }
               
               repaint();
           }
         });
         mnFile.add(mntmImage);
         mnFile.addSeparator();
         mnFile.add(mntmNew);
         mnFile.addSeparator();
         JMenuItem mntmre = new JMenuItem("Resize");
         mntmre.setMnemonic('z');
         mntmre.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
              String st1=JOptionPane.showInputDialog(" 몇번째 도형을 바꾸실건가요? brush는지원되지않습니다.");
              int num=Integer.parseInt(st1);
              if(num>figures.size()||num<=0) {
                 JOptionPane.showMessageDialog(null, "잘못된 입력입니다");
                 return;
              }
              String st2=JOptionPane.showInputDialog("크기를 늘릴려면 양수를 입력하시고 줄이려면 음수를 입력해주세요.");
              int size =Integer.parseInt(st2);
              if(size>800) {
                 JOptionPane.showMessageDialog(null, "잘못된 입력입니다");
                 return;
              }
              Figure s=figures.get(num-1);
              
              if(s.choice==1) {
              s.myZ=s.myZ+size;
              s.myW=s.myW+size;
             
              }else {
                 s.myZ=s.myZ+size;
                  s.myW=s.myW+size;
                  s.myX=s.myX-size;
                  s.myY=s.myY-size;
              }
              
              repaint();
            }
         });
         //파일 메뉴바
         mnFile.add(mntmre);
         JMenuItem mntmco = new JMenuItem("Copy");
         mntmco.setMnemonic('c');
         mntmco.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
              String st1=JOptionPane.showInputDialog(" 카피할 도형을 선택해주세요 brush는지원되지않습니다.");
              int num=Integer.parseInt(st1);
              String st2=JOptionPane.showInputDialog(" 카피시킬 도형을 선택해주세요 brush는지원되지않습니다.");
              int num1=Integer.parseInt(st2);
              if(num>figures.size()||num<=0||num1>figures.size()||num1<=0) {
                 JOptionPane.showMessageDialog(null, "잘못된 입력입니다");
                 return;
              }
              
             
              Figure s=figures.get(num-1);
              Figure s1=figures.get(num1-1);
            if(s.choice==2||s.choice==3) {
                 s1.choice=s.choice;
                  s1.fill=s.fill;
                  s1.mycolor=s.mycolor;
                  s1.thick=s.thick;
                s1.myZ=s.myZ;
                s1.myW=s.myW;
            }else {
               s1.choice=s.choice;
                s1.fill=s.fill;
                s1.mycolor=s.mycolor;
                s1.thick=s.thick;
                s1.myZ=s1.myX+s.myZ-s.myX;
                s1.myW=s1.myY+s.myW-s.myY;
            }
              
              repaint();
            }
         });
         //파일 메뉴바
         mnFile.add(mntmco);
         JMenuItem mntmlo = new JMenuItem("Relocate");
         mntmlo.setMnemonic('o');
         mntmlo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
              String st1=JOptionPane.showInputDialog(" 몇번째 도형을 옮기실건가요? brush는지원되지않습니다.\n좌표를 기준으로 움직입니다");
              int num=Integer.parseInt(st1);
              if(num>figures.size()||num<=0) {
                 JOptionPane.showMessageDialog(null, "잘못된 입력입니다");
                 return;
              }
              String st2=JOptionPane.showInputDialog("어느정도 옮길지 입력해주세요.\n좌표를 기준으로 움직입니다");
              int size =Integer.parseInt(st2);
              if(size>800) {
                 JOptionPane.showMessageDialog(null, "잘못된 입력입니다");
                 return;
              }
              Figure s=figures.get(num-1);
              
              if(s.choice==1) {
              s.myZ=s.myZ+size;
              s.myW=s.myW+size;
              s.myX=s.myX+size;
              s.myY=s.myY+size;
             
              }else if(s.choice==2||s.choice==3||s.choice==5){
                  s.myX=s.myX+size;
                  s.myY=s.myY-size;
              }else {
                 s.myZ=s.myZ+size;
                  s.myW=s.myW+size;
                  s.myX=s.myX+size;
                  s.myY=s.myY+size;
              }
              
              repaint();
            }
         });
         //파일 메뉴바
         mnFile.add(mntmlo);
         JMenuItem mntmth = new JMenuItem("Rethick");
         mntmth.setMnemonic('t');
         mntmth.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
              String st1=JOptionPane.showInputDialog(" 몇번째 도형을 바꾸실건가요? brush는지원되지않습니다.");
              int num=Integer.parseInt(st1);
              if(num>figures.size()||num<=0) {
                 JOptionPane.showMessageDialog(null, "잘못된 입력입니다");
                 return;
              }
              String st2=JOptionPane.showInputDialog("크기를설정해주세요 양수입력만됩니다.");
              int size =Integer.parseInt(st2);
              if(size<=0) {
                 JOptionPane.showMessageDialog(null, "잘못된 입력입니다");
                 return;
              }
              Figure s=figures.get(num-1);
              
             s.thick=size;
              
              repaint();
            }
         });
         //파일 메뉴바
         mnFile.add(mntmth);
         mnFile.addSeparator();
        
        JMenuItem mntmSave = new JMenuItem("Save");
        mntmSave.setMnemonic('s');
         mntmSave.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent e) {
                 try
                 {
                    String resultStr;
                    resultStr = JOptionPane.showInputDialog(null,
                       "저장할 파일의 이름을 지정하세요.\r\n(확장자포함)", "저장",
                       JOptionPane.INFORMATION_MESSAGE);
                    String a = resultStr;
                 FileOutputStream f = new FileOutputStream(a); //a이름을 가지는 outputStream 생성
                 ObjectOutputStream of = new ObjectOutputStream(f); //ObjectOutputStream 생성
                 
                 for(int i = 0 ;i < figures.size(); i++)
                 {
                    Figure s = figures.get(i);
                    of.writeObject(s);
                 }
                 of.close();
              } 
              catch(IOException e1)
              { //파일 오류에 관한 예외처리
                 System.out.println(e1.getMessage());
              }
              }});
         mnFile.add(mntmSave);
         JMenuItem mntmLoad = new JMenuItem("Load");
         mntmLoad.setMnemonic('l');
         mntmLoad.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent e) 
            {
               for(int q = 0 ; q < figures.size();)
               {
                  figures.remove(q);
               }//이전에있는 객체들 모두 초기화 ( 그림판 화면 초기화 시키는 부분 )
               int i = 0;
            JFileChooser jfc = new JFileChooser("C://Users//김종우//workspace//그래픽");
            jfc.showDialog(null, "확인");
            File file = jfc.getSelectedFile(); //파일 선택창 선언
            try  
            {
               FileInputStream f = new FileInputStream(file);               
               ObjectInputStream of = new ObjectInputStream(f);
               
               
               Figure li = (Figure) of.readObject();
               while(li != null)
               {
                  
                  figures.add(i, li);
                  li = (Figure) of.readObject();
               
               }
               of.close();
            } 
              catch(IOException e1)
              {
                 repaint();
                  
                 return;
              } catch (ClassNotFoundException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            }


            }});
         mnFile.add(mntmLoad);
         mnFile.addSeparator();
         JMenuItem mntmExit = new JMenuItem("Exit");
         mntmExit.setMnemonic('x');
         mntmExit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
               System.exit(0);
            }
         });
         
         mnFile.add(mntmExit);
         menu.add(menuBar);
        
        JLabel thicknessInfo_label = new JLabel("굵기입력"); 
        // 도구굴기 라벨 지정 / 밑에서 나올 텍스트필드의 역할을 설명
        thicknessInfo_label.setFont(new Font("함초롬돋움", Font.BOLD, 20));
        menu.add(thicknessInfo_label);
        ThickText = new JTextField("5",5);
        menu.add(ThickText);
        ThickText.addActionListener(new TextFieldHandler());
      
        //----------------------------------색설정박스-----------------------------------------
        JLabel colorLabel = new JLabel("색 설정"); 
        colorLabel.setFont(new Font("함초롬돋움", Font.BOLD, 20));
        menu.add(colorLabel);
        
        colorComboBox = new JComboBox(colors);
        colorComboBox.setMaximumRowCount(5);
        ColorHandler colorHandler = new ColorHandler();
        colorComboBox.addItemListener(colorHandler);
        menu.add(colorComboBox);
        //----------------------------------색설정박스 끝----------------------------------------
      
        //----------------------------------모양박스-----------------------------------------
        JLabel shapeLabel = new JLabel("도구"); 
        shapeLabel.setFont(new Font("함초롬돋움", Font.BOLD, 20));
        menu.add(shapeLabel);
        
        shapeBox = new JComboBox(shapes);
        shapeBox.setMaximumRowCount(10);
        shapeHandler shapeHandler = new shapeHandler();
        shapeBox.addItemListener(shapeHandler);
        menu.add(shapeBox);
        //----------------------------------모양박스 끝----------------------------------------
        
        backbt = new JButton("되돌리기");
        backbt.setFont(new Font("함초롱돋움", Font.BOLD, 25)); //
        backbt.setBackground(Color.LIGHT_GRAY); //
        
        
        //---------------------------------------------------------------------------
        fillButton = new JRadioButton("fill", false);
        unfillButton = new JRadioButton("unfill", true);
        
        menu.add(unfillButton);
        menu.add(fillButton);
        
        fillGroup = new ButtonGroup();
        
        fillGroup.add(unfillButton);
        fillGroup.add(fillButton);
        
        fillButton.addItemListener(new FillRadioButtonHandler(1));
        unfillButton.addItemListener(new FillRadioButtonHandler(0));
        //------------------------------------------------------------------------------

        ActionListener listenerBack = new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
              figures.remove(figures.size()-1);
           }
        };
        backbt.addActionListener(listenerBack);
        
        MouseHandler handler = new MouseHandler();
        this.addMouseListener(handler);
      this.addMouseMotionListener(handler);
      
        menu.add(backbt);
        this.add(menu);
   }
   
   

   private class ColorHandler implements ItemListener
   {
      @Override
      public void itemStateChanged(ItemEvent e) {
         if (e.getStateChange() == ItemEvent.SELECTED) {
            color = colorSet[colorComboBox.getSelectedIndex()];
         }
      }
   }
   
   private class shapeHandler implements ItemListener
   {
      @Override
      public void itemStateChanged(ItemEvent e) {
         if (e.getStateChange() == ItemEvent.SELECTED) {
            choice = shapeBox.getSelectedIndex();
         }
      }
   }
   
   private class TextFieldHandler implements ActionListener
   {
      public void actionPerformed(ActionEvent event) {
         thick = Integer.parseInt(event.getActionCommand());
      }
      
   }
   
   private class FillRadioButtonHandler implements ItemListener//
   {
      int f;
      public FillRadioButtonHandler(int set)
      {
         f = set;
      }
      @Override
      public void itemStateChanged(ItemEvent e) {
         fill = f;
      }
   }
   private class MouseHandler implements MouseListener, MouseMotionListener
   {
   
      int pointcnt;
      public void mouseClicked(MouseEvent event)
      {
         glassX = event.getX();
          glassY = event.getY();
          if(glassed == 0)
          {
           for(Figure figure : figures)
             {
                figure.glass(glassX,glassY);
             }
           glassed = 1;
          }
          else if(glassed == 1)
          {
             for(Figure figure : figures)
             {
                figure.glassOut(glassX,glassY);
             }
           glassed = 0;
          }
      }
      public void mousePressed(MouseEvent event)
      {
         pointcnt = 0;
         point = new Figure[500];
         x = event.getX();
         y = event.getY();
         m = event.getX();
         n = event.getY();
      }
      public void mouseEntered(MouseEvent event) {}

      public void mouseExited(MouseEvent event){}

      public void mouseMoved(MouseEvent event){}
      
      public void mouseDragged(MouseEvent event){
         m = event.getX();
         n = event.getY();
         if(choice == 0||choice==11)
         {
            if (pointcnt < point.length-1)
            {
               Figure figure;
               if(choice==0) {
               figure = new Figure(3, m,n,thick,thick,color,1,thick);//
               }else {
                  figure=new Figure(3,m,n,thick,thick,Color.WHITE,1,thick);
               }
               point[pointcnt] = figure;
               pointcnt++;
               state = 1;
               repaint();
            }
            x = m;
            y = n;
         }
      }
      @Override
      public void mouseReleased(MouseEvent event) {
         m = event.getX();
         n = event.getY();
         if(choice == 2 || choice == 3|| choice==5)
         {   
            height = Math.abs(n-y);
            width = Math.abs(m-x);
            Figure figure;
            if(x > m && y > n){
               figure = new Figure(choice,m,n,height,width,color,fill,thick);
            }
            
            else if(x < m && y > n){
               figure = new Figure(choice,x,n,height,width,color,fill,thick);
            }
            
            else if(x > m && y < n){
               figure = new Figure(choice,m,y,height,width,color,fill,thick);
            }
            
            else{
               figure = new Figure(choice,x,y,height,width,color,fill,thick);
            }
            
            figures.add(figure);
          }
         if(choice == 1 || choice == 6 ||choice==4|| choice == 9 || choice == 10 || choice ==8 || choice ==7)
         {
            Figure figure = new Figure(choice,x,y,n,m,color,fill,thick);
            figures.add(figure);
         }
         if(choice == 0||choice==11)
         {
            Figure brush;
            state =2;
            if(choice==0) {
                 brush = new Figure(choice,0,0,0,0,color,0,0);
            }else {
               brush= new Figure(11,0,0,0,0,Color.WHITE,0,0);
            }
            brush.addDot(point);
            figures.add(brush);
         }
         repaint();
         x=-50; y=-50; m=-50; n=-50;
      }
   }
   
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D)g;
      g.drawImage(img,250,250,null);
      
      for(Figure figure : figures)
      {
       
         switch(figure.getChoice())
         {
         case 0:
            Figure[] points = figure.getdot().clone();
            for(int i = 0; points[i] != null; i++)
            {
               g2.setColor(points[i].getColor());
               g2.fillOval(points[i].x(),points[i].y(),points[i].z(),points[i].w());
            }
         case 1:
            g2.setColor(figure.getColor());
            g2.setStroke(new BasicStroke(figure.thick,BasicStroke.CAP_ROUND,0));      
            g2.drawLine(figure.x(),figure.y(),figure.z(),figure.w());
            break;
         case 2:
            g2.setColor(figure.getColor());
            if(figure.fill() == 0) {
               g2.setStroke(new BasicStroke(figure.thick,BasicStroke.CAP_ROUND,0));
               g2.drawRect(figure.x(), figure.y(), figure.z(), figure.w());
            }
            else if(figure.fill() == 1) {
               g2.fillRect(figure.x(),figure.y(),figure.z(),figure.w());
            }
            break;
         case 3:
            g2.setColor(figure.getColor());
            if(figure.fill() == 0) {
               g2.setStroke(new BasicStroke(figure.thick));
               g2.drawOval(figure.x(),figure.y(),figure.z(),figure.w());
            }
            else if(figure.fill() == 1) {
               g2.fillOval(figure.x(),figure.y(),figure.z(),figure.w());
            }
            break;
         case 4:   //triangle 
             g2.setColor(figure.getColor());
             int[] polX4 = {figure.x()+(figure.z()-figure.x())/2,figure.x(),figure.x()+(figure.z()-figure.x())};
             int[] polY4 = {figure.y(),figure.y()+(figure.w()-figure.y()),figure.y()+(figure.w()-figure.y()),figure.y()+(figure.w()-figure.y())};
             
              if(figure.fill() == 0) {
                 g2.setStroke(new BasicStroke(figure.thick,BasicStroke.CAP_ROUND,0));
                 g2.drawPolygon(polX4, polY4, 3);
                 
              }
              else if(figure.fill() == 1) {
                 g2.fillPolygon(polX4, polY4, 3);
              }
             break;
         case 5:
            g2.setColor(figure.getColor());
            if(figure.fill() == 0) {
               g2.setStroke(new BasicStroke(figure.thick,BasicStroke.CAP_ROUND,0));
               g2.drawRoundRect(figure.x(), figure.y(),figure.z(), figure.w(),20,20);
            }
            else if(figure.fill() == 1) {
               g2.fillRoundRect(figure.x(),figure.y(),figure.z(),figure.w(),20,20);
            }
            break;
         case 6:
            g2.setColor(figure.getColor());
            int[] polX = {figure.x(),figure.x()+(figure.z()-figure.x())/4,figure.x()+3*(figure.z()-figure.x())/4
                     ,figure.x()+(figure.z()-figure.x()),figure.x()+(figure.z()-figure.x())/2};
            int[] polY = {figure.y()+(figure.w()-figure.y())/3,figure.y()+(figure.w()-figure.y()),
                      figure.y()+(figure.w()-figure.y()),figure.y()+(figure.w()-figure.y())/3,figure.y()};
            if(figure.fill() == 0) {
                 g2.setStroke(new BasicStroke(figure.thick,BasicStroke.CAP_ROUND,0));
                 g2.drawPolygon(polX, polY, 5);
              }
              else if(figure.fill() == 1) {
                 g2.fillPolygon(polX, polY, 5);                
              }
              break;
         case 7://==============================================================================
            g2.setColor(figure.getColor());
            int[] polX5 = {figure.x()+(figure.z()-figure.x())/2,figure.x(),figure.x(),figure.x()+(figure.z()-figure.x())/2,figure.x()+(figure.z()-figure.x()),figure.x()+(figure.z()-figure.x())};
            int[] polY5 = {figure.y(),figure.y()+(figure.w()-figure.y())/3,figure.y()+(figure.w()-figure.y())*2/3
                  ,figure.y()+(figure.w()-figure.y()),figure.y()+(figure.w()-figure.y())*2/3,figure.y()+(figure.w()-figure.y())/3};
               if(figure.fill() == 0) {
                 g2.setStroke(new BasicStroke(figure.thick,BasicStroke.CAP_ROUND,0));
                 g2.drawPolygon(polX5, polY5, 6);
              }
              else if(figure.fill() == 1) {
                 g2.fillPolygon(polX5, polY5, 6);                
              }
              break;
         case 8:
             g2.setColor(figure.getColor());
             int[] polX3 = {figure.x()+(figure.z()-figure.x())/2,figure.x(),
                   figure.x()+(figure.z()-figure.x())/2,figure.x()+(figure.z()-figure.x())};
             int[] polY3 = {figure.y(),figure.y()+(figure.w()-figure.y())/2,figure.y()+(figure.w()-figure.y()),figure.y()+(figure.w()-figure.y())/2};
             if(figure.fill() == 0) {
                g2.setStroke(new BasicStroke(figure.thick,BasicStroke.CAP_ROUND,0));
                g2.drawPolygon(polX3, polY3, 4);
             }
             else if(figure.fill() == 1) {
                g2.fillPolygon(polX3, polY3, 4);  //==============================================================================              
             }
            break;
         case 9:
            g2.setColor(figure.getColor());
            int[] polX2 = {figure.x()+(figure.z()-figure.x())/4, figure.x(), figure.x()
                  +(figure.z()-figure.x()),figure.x()+3*(figure.z()-figure.x())/4};
            int[] polY2 = {figure.y(),figure.y()+(figure.w()-figure.y()),figure.y()+(figure.w()-figure.y()),figure.y()};
            if(figure.fill() == 0) {
                 g2.setStroke(new BasicStroke(figure.thick,BasicStroke.CAP_ROUND,0));
                 g2.drawPolygon(polX2, polY2, 4);
              }
              else if(figure.fill() == 1) {
                 g2.fillPolygon(polX2, polY2, 4);              
              }
            break;
         case 10:
            g2.setColor(figure.getColor());
            g2.setStroke(new BasicStroke(figure.thick,BasicStroke.CAP_ROUND,0));
               int[] polX6 = {figure.x(),figure.x()+5*(figure.z()-figure.x())/16,figure.x()+(figure.z()-figure.x())/4,
                     figure.x()+(figure.z()-figure.x())/2,figure.x()+3*(figure.z()-figure.x())/4,figure.x()+11*(figure.z()-figure.x())/16
                     ,figure.x()+(figure.z()-figure.x()),figure.x()+10*(figure.z()-figure.x())/16,figure.x()+(figure.z()-figure.x())/2,figure.x()+6*(figure.z()-figure.x())/16};
               int[] polY6 = {figure.y()+(figure.w()-figure.y())/3,figure.y()+7*(figure.w()-figure.y())/12, figure.y()+(figure.w()-figure.y()),
                     figure.y()+3*(figure.w()-figure.y())/4,figure.y()+(figure.w()-figure.y()),figure.y()+7*(figure.w()-figure.y())/12,figure.y()+(figure.w()-figure.y())/3
                     ,figure.y()+(figure.w()-figure.y())/3,figure.y(),figure.y()+(figure.w()-figure.y())/3};
               if(figure.fill() == 0)
               {
                  g2.drawPolygon(polX6, polY6, 10);
               }
               else if(figure.fill() == 1)
               {
                  g2.fillPolygon(polX6, polY6, 10);
               }
         break;
         case 11:
            Figure[] points1 = figure.getdot().clone();
             for(int i = 0; points1[i] != null; i++)
             {
                g2.setColor(points1[i].getColor());
                g2.fillOval(points1[i].x(),points1[i].y(),points1[i].z(),points1[i].w());
             }
         } 
      }g2.setStroke(new BasicStroke(1));
      g2.setColor(color);
      if(choice == 1)
      {
        g2.setStroke(new BasicStroke(thick,BasicStroke.CAP_ROUND,0));
         g2.drawLine(x,y,m,n);
      }
      else if(choice == 2)
      {   
         if(x > m && y > n){
            if(fill == 0){
               g2.setStroke(new BasicStroke(thick,BasicStroke.CAP_ROUND,0));
               g2.drawRect(m,n,Math.abs(x-m),Math.abs(y-n));
            }
            else if(fill == 1){
               g2.fillRect(m,n,Math.abs(x-m),Math.abs(y-n));
            }
         }
         
         else if(x < m && y > n){
            if(fill == 0){
               g2.setStroke(new BasicStroke(thick,BasicStroke.CAP_ROUND,0));
               g2.drawRect(x,n,Math.abs(x-m),Math.abs(y-n));
            }
            else if(fill == 1){
               g2.fillRect(x,n,Math.abs(x-m),Math.abs(y-n));
            }
         }
         
         else if(x > m && y < n){
            if(fill == 0) {
               g2.setStroke(new BasicStroke(thick,BasicStroke.CAP_ROUND,0));
               g2.drawRect(m,y,Math.abs(x-m),Math.abs(y-n));
            }
            else if(fill == 1) {
               g2.fillRect(m,y,Math.abs(x-m),Math.abs(y-n));
            }
         }
         
         else{
            if(fill == 0) {
               g2.setStroke(new BasicStroke(thick,BasicStroke.CAP_ROUND,0));
               g2.drawRect(x,y,m-x,n-y);
            }
            else if(fill == 1) {
               g2.fillRect(x,y,m-x,n-y);
            }
         }      
      }
      else if(choice == 3)
      {
        g2.setStroke(new BasicStroke(thick));
         if(x > m && y > n){
            if(fill == 0){
               g2.drawOval(m,n,Math.abs(x-m),Math.abs(y-n));
            }
            else if(fill == 1){
               g2.fillOval(m,n,Math.abs(x-m),Math.abs(y-n));
            }
         }
         
         else if(x < m && y > n){
            if(fill == 0){
               g2.drawOval(x,n,Math.abs(x-m),Math.abs(y-n));
            }
            else if(fill == 1){
               g2.fillOval(x,n,Math.abs(x-m),Math.abs(y-n));
            }
         }
         
         else if(x > m && y < n){
            if(fill == 0) {
               g2.drawOval(m,y,Math.abs(x-m),Math.abs(y-n));
            }
            else if(fill == 1) {
               g2.fillOval(m,y,Math.abs(x-m),Math.abs(y-n));
            }
         }
         
         else{
            if(fill == 0) {
               g2.drawOval(x,y,m-x,n-y);
            }
            else if(fill == 1) {
               g2.fillOval(x,y,m-x,n-y);
            }
         }
      }
      else if(choice == 4) //  triangle
      {
         int[] polX4 = {x+(m-x)/2,x,x+(m-x)};
           int[] polY4 = {y,y+(n-y),y+(n-y),y+(n-y)};
              if(fill == 0){
               
                 g2.setStroke(new BasicStroke(thick,BasicStroke.CAP_ROUND,0));
                // g2.drawRect(m,n,Math.abs(x-m),Math.abs(y-n));
                 g2.drawPolygon(polX4, polY4, 3);

              }
              else if(fill == 1){
                 g2.fillPolygon(polX4, polY4, 3);
              }
           
      }
      else if(choice ==5)
      {
         g2.setStroke(new BasicStroke(thick,BasicStroke.CAP_ROUND,0));
         if(x > m && y > n){
              if(fill == 0){
                 g2.drawRoundRect(m,n,Math.abs(x-m),Math.abs(y-n),20,20);
              }
              else if(fill == 1){
                 g2.fillRoundRect(m,n,Math.abs(x-m),Math.abs(y-n),20,20);
              }
           }
           
           else if(x < m && y > n){
              if(fill == 0){
                 g2.drawRoundRect(x,n,Math.abs(x-m),Math.abs(y-n),20,20);
              }
              else if(fill == 1){
                 g2.fillRoundRect(x,n,Math.abs(x-m),Math.abs(y-n),20,20);
              }
           }
           
           else if(x > m && y < n){
              if(fill == 0) {
                 g2.drawRoundRect(m,y,Math.abs(x-m),Math.abs(y-n),20,20);
              }
              else if(fill == 1) {
                 g2.fillRoundRect(m,y,Math.abs(x-m),Math.abs(y-n),20,20);
              }
           }
           else{
              if(fill == 0) {
                 g2.drawRoundRect(x,y,m-x,n-y,20,20);
              }
              else if(fill == 1) {
                 g2.fillRoundRect(x,y,m-x,n-y,20,20);
              }
           }
      }
      else if (choice == 6)
      {
         g2.setStroke(new BasicStroke(thick,BasicStroke.CAP_ROUND,0));
         int[] polX = {x,x+(m-x)/4,x+3*(m-x)/4,x+(m-x),x+(m-x)/2};
         int[] polY = {y+(n-y)/3,y+(n-y), y+(n-y),y+(n-y)/3,y};
         if(fill == 0)
         {
            g2.drawPolygon(polX, polY, 5);
         }
         else if(fill == 1)
         {
            g2.fillPolygon(polX, polY, 5);
         }
         
      }
      else if(choice  == 7) // hexagon
      {
         int[] polX5 = {x+(m-x)/2,x,x,x+(m-x)/2,x+(m-x),x+(m-x)};
           int[] polY5 = {y,y+(n-y)/3,y+(n-y)*2/3,y+(n-y),y+(n-y)*2/3,y+(n-y)/3};
              if(fill == 0){
               
                 g2.setStroke(new BasicStroke(thick,BasicStroke.CAP_ROUND,0));
                // g2.drawRect(m,n,Math.abs(x-m),Math.abs(y-n));
                 g2.drawPolygon(polX5, polY5, 6);

              }
              else if(fill == 1){
                 g2.fillPolygon(polX5, polY5,6);
              }
      }
      else if(choice  == 8) // rhombus
      {
         int[] polX3 = {x+(m-x)/2,x,x+(m-x)/2,x+(m-x)};
           int[] polY3 = {y,y+(n-y)/2,y+(n-y),y+(n-y)/2};
              if(fill == 0){
               
                 g2.setStroke(new BasicStroke(thick,BasicStroke.CAP_ROUND,0));
                // g2.drawRect(m,n,Math.abs(x-m),Math.abs(y-n));
                 g2.drawPolygon(polX3, polY3, 4);

              }
              else if(fill == 1){
                 g2.fillPolygon(polX3, polY3, 4);
              }
      }
      else if (choice == 9)
      {
         g2.setStroke(new BasicStroke(thick,BasicStroke.CAP_ROUND,0));
         int[] polX2 = {x+(m-x)/4,x,x+(m-x),x+3*(m-x)/4};
         int[] polY2 = {y,y+(n-y),y+(n-y),y};
         if(fill == 0)
         {
            g2.drawPolygon(polX2, polY2, 4);
         }
         else if(fill == 1)
         {
            g2.fillPolygon(polX2, polY2, 4);
         }
        
      }
      else if (choice == 10)
      {
         g2.setStroke(new BasicStroke(thick,BasicStroke.CAP_ROUND,0));
         int[] polX = {x,x+5*(m-x)/16,x+(m-x)/4,x+(m-x)/2,x+3*(m-x)/4,x+11*(m-x)/16,x+(m-x),x+10*(m-x)/16,x+(m-x)/2,x+6*(m-x)/16};
         int[] polY = {y+(n-y)/3,y+7*(n-y)/12, y+(n-y),y+3*(n-y)/4,y+(n-y),y+7*(n-y)/12,y+(n-y)/3,y+(n-y)/3,y,y+(n-y)/3};
         if(fill == 0)
         {
            g2.drawPolygon(polX, polY, 10);
         }
         else if(fill == 1)
         {
            g2.fillPolygon(polX, polY, 10);
         }
      }
      if(state  == 1)
      {  
         for(int i = 0; point[i] != null; i++)
         {
            g2.setColor(point[i].getColor());
            g2.fillOval(point[i].x(),point[i].y(),point[i].z(),point[i].w());
         }
      }
      repaint();
   }
}