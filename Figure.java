package hi;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.Serializable;
public class Figure implements Serializable{
   public int myX;
   public int myY;
   public  int myZ;
   public  int myW;
   public  Color mycolor;
   int choice;
   int fill;
   int thick;
   Figure[] dot;
   BufferedImage img=null;
   public Figure(int userchoice,int x, int y, int w, int z, Color c, int f, int t)
   {
      choice = userchoice;
      myX = x;
      myY = y;
      myZ = z;
      myW = w;
      mycolor = c;
      fill = f;
      thick = t;
   }
   
   public void setChoice(int c)
   {
      choice = c;
   }
   
   public void delete()
   {
      myX = 0;
      myY = 0;
      myZ = 0;
      myW = 0;
   }
   public void addDot(Figure[] arr)
   {
      dot = arr.clone();
   }
   public int getChoice()
   {
      return choice;
   }
   
   public int x()
   {
      return myX;
   }
   
   public int y()
   {
      return myY;
   }
   
   public int z()
   {
      return myZ;
   }
   
   public int w()
   {
      return myW;
   }
   
   public Color getColor()
   {
      return mycolor;
   }
   public int fill()
   {
      return fill;
   }
   public int getthick()
   {
      return thick;
   }

   public Figure[] getdot() {
      return dot;
   }
   public void glass(int x, int y)
   {
      myX = myX - (x - myX);
      myY = myY - (y - myY);
      if(choice == 2 || choice == 3||choice==5)
      {
         myZ *= 2;
         myW *= 2;
      }
      else if(choice == 0 || choice == 11)
      {
         for(int i = 0; dot[i] != null; i++ )
         {
            dot[i].glass(x,y);
         }
      }
      else
      {
         myZ = myZ - (x - myZ);
         myW = myW - (y - myW);
      }
   }
   public void glassOut(int x, int y)
   {
      myX = myX + (x - myX)/2;
      myY = myY + (y - myY)/2;
      if(choice == 2 || choice == 3||choice==5)
      {
         myZ /= 2;
         myW /= 2;
      }
      else if(choice == 0 || choice == 11)
      {
         for(int i = 0; dot[i] != null; i++ )
         {
            dot[i].glassOut(x,y);
         }
      }
      else
      {
         myZ = myZ + (x - myZ)/2;
         myW = myW + (y - myW)/2;
      }
   }
}
