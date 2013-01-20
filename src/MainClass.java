import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;

public class MainClass extends JFrame
{
    public static void main(String[] args)
    {
        new MainClass();
    }

    private DrawPanel dp;
    private ArrayList<Model> models;
    private int mind = 0;
    private final double inc = 2.5;

    public MainClass()
    {
        models = new ArrayList<Model>();
        models.add(new Model(25, 3, "F[-F]F[+F]F", "Weed"));
        models.add(new Model(5, 90, 3, "FF[-F-F][+F+F]F", "Weed 2"));
        Map<Character, String> weed3Mappings = new HashMap<Character, String>();
        weed3Mappings.put('X', "F-[[X]+X]+F[+FX]-X");
        weed3Mappings.put('F', "FF");
        models.add(new Model(6, 25, 2.4, "X", "Weed 3", weed3Mappings, null).setInitAngle(60).setInitPos(0.2, 0.9));
        models.add(new Model(90, 3, "FF+F+F+FF+F+F-F", "Peano"));
        models.add(new Model(60, 3, "F-F++F-F", "Koch"));
        models.add(new Model(90, 3, "F-F+F+F-F", "Koch 2"));
        Map<Character, String> stMappings = new HashMap<Character, String>();
        stMappings.put('A', "B-A-B");
        stMappings.put('B', "A+B+A");
        Map<Character, String> stFMappings = new HashMap<Character, String>();
        stFMappings.put('A', "F");
        stFMappings.put('B', "F");
        models.add(new Model(8, 60, 2, "A", "Sierpinski", stMappings, stFMappings).setInitAngle(-60).setInitPos(0.5, 0.05));
        Map<Character, String> drMappings = new HashMap<Character, String>();
        drMappings.put('X', "X+YF");
        drMappings.put('Y', "FX-Y");
        models.add(new Model(10, 90, 1.5, "FX", "Dragon", drMappings, null).setInitPos(0.3, 0.6));
        this.setSize(500, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("Fractal Test");
        this.setLayout(new GridLayout(1, 1));
        dp = new DrawPanel();
        this.add(dp);
        this.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_LEFT)
                {
                    if(mind > 0)
                        mind--;
                    dp.setModel(models.get(mind));
                }
                else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
                {
                    if(mind < models.size() - 1)
                        mind++;
                    dp.setModel(models.get(mind));
                }
                else if(e.getKeyCode() == KeyEvent.VK_UP)
                {
                    dp.angleDiff += inc;
                    if(dp.angleDiff > 120)
                        dp.angleDiff = 120;
                }
                else if(e.getKeyCode() == KeyEvent.VK_DOWN)
                {
                    dp.angleDiff -= inc;
                    if(dp.angleDiff < 5)
                        dp.angleDiff = 5;
                }
                else if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    System.exit(0);
                repaint();
            }
        });
        this.setVisible(true);
    }

    private class Model
    {
        private int iterations = 4;
        private double initialAngle = 90;
        private double defaultAngleDiff;
        private double segments;
        private String init = "F";
        private String name;
        private Map<Character, String> mappings;
        private Map<Character, String> finalMappings;
        private double initX = 0.5;
        private double initY = 1;

        public Model setInitAngle(double angle)
        {
            this.initialAngle = angle;
            return this;
        }

        public Model setInitPos(double initX, double initY)
        {
            this.initX = initX;
            this.initY = initY;
            return this;
        }

        public Model(double defaultAngleDiff, double segments, String fReplace, String name)
        {
            this.defaultAngleDiff = defaultAngleDiff;
            this.segments = segments;
            this.name = name;
            this.mappings = new HashMap<Character, String>();
            this.mappings.put('F', fReplace);
            this.finalMappings = new HashMap<Character, String>();
        }

        public Model(int iterations, double defaultAngleDiff, double segments, String fReplace, String name)
        {
            this.iterations = iterations;
            this.defaultAngleDiff = defaultAngleDiff;
            this.segments = segments;
            this.name = name;
            this.mappings = new HashMap<Character, String>();
            this.mappings.put('F', fReplace);
            this.finalMappings = new HashMap<Character, String>();
        }

        public Model(double defaultAngleDiff, double segments, String init, String fReplace, String name)
        {
            this.defaultAngleDiff = defaultAngleDiff;
            this.segments = segments;
            this.init = init;
            this.name = name;
            this.mappings = new HashMap<Character, String>();
            this.mappings.put('F', fReplace);
            this.finalMappings = new HashMap<Character, String>();
        }

        public Model(int iterations, double defaultAngleDiff, double segments, String init, String fReplace, String name)
        {
            this.iterations = iterations;
            this.defaultAngleDiff = defaultAngleDiff;
            this.segments = segments;
            this.init = init;
            this.name = name;
            this.mappings = new HashMap<Character, String>();
            this.mappings.put('F', fReplace);
            this.finalMappings = new HashMap<Character, String>();
        }

        public Model(double defaultAngleDiff, double segments, String name, Map<Character, String> mappings, Map<Character, String> finalMappings)
        {
            this.defaultAngleDiff = defaultAngleDiff;
            this.segments = segments;
            this.name = name;
            this.mappings = mappings;
            if(finalMappings == null)
                this.finalMappings = new HashMap<Character, String>();
            else
                this.finalMappings = finalMappings;
        }

        public Model(int iterations, double defaultAngleDiff, double segments, String name, Map<Character, String> mappings, Map<Character, String> finalMappings)
        {
            this.iterations = iterations;
            this.defaultAngleDiff = defaultAngleDiff;
            this.segments = segments;
            this.name = name;
            this.mappings = mappings;
            if(finalMappings == null)
                this.finalMappings = new HashMap<Character, String>();
            else
                this.finalMappings = finalMappings;
        }

        public Model(double defaultAngleDiff, double segments, String init, String name, Map<Character, String> mappings, Map<Character, String> finalMappings)
        {
            this.defaultAngleDiff = defaultAngleDiff;
            this.segments = segments;
            this.init = init;
            this.name = name;
            this.mappings = mappings;
            if(finalMappings == null)
                this.finalMappings = new HashMap<Character, String>();
            else
                this.finalMappings = finalMappings;
        }

        public Model(int iterations, double defaultAngleDiff, double segments, String init, String name, Map<Character, String> mappings, Map<Character, String> finalMappings)
        {
            this.iterations = iterations;
            this.defaultAngleDiff = defaultAngleDiff;
            this.segments = segments;
            this.init = init;
            this.name = name;
            this.mappings = mappings;
            if(finalMappings == null)
                this.finalMappings = new HashMap<Character, String>();
            else
                this.finalMappings = finalMappings;
        }

        public String permute(String input)
        {
            //return input.replace("F", fReplace);
            String rval = "";
            for(int i = 0; i < input.length(); i++)
            {
                char c = input.charAt(i);
                if(mappings.containsKey(c)) rval += mappings.get(c);
                else rval += c;
            }
            return rval;
        }

        public String genPattern()
        {
            String pattern = init;
            for(int i = 0; i < iterations; i++)
            {
                pattern = permute(pattern);
            }
            if(finalMappings.size() > 0)
            {
                String pattern2 = "";
                for(int i = 0; i < pattern.length(); i++)
                {
                    char c = pattern.charAt(i);
                    if(finalMappings.containsKey(c)) pattern2 += finalMappings.get(c);
                    else pattern2 += c;
                }
                pattern = pattern2;
            }
            return pattern;
        }
    }

    private class DrawPanel extends JPanel
    {
        private Model curModel;
        private double angleDiff;

        public DrawPanel()
        {
            setModel(models.get(mind));
        }

        public void setModel(Model m)
        {
            curModel = m;
            angleDiff = curModel.defaultAngleDiff;
        }

        @Override
        public void paint(Graphics g)
        {
            String pattern = curModel.genPattern();
            Graphics2D g2 = (Graphics2D)g;
            g2.drawString("Angle Diff: "+angleDiff, 10, 20);
            g2.drawString("Model: "+curModel.name, 10, 40);
            g2.setColor(Color.white);
            g2.drawRect(0, 0, this.getWidth(), this.getHeight());
            g2.setColor(Color.black);
            Stack<StackData> stk = new Stack<StackData>();
            StackData curData = new StackData(new Point2D.Double((this.getWidth()-20) * curModel.initX + 10, (this.getHeight()-20) * curModel.initY + 10), curModel.initialAngle);
            double lineLength = (this.getHeight()-20) / Math.pow(curModel.segments, curModel.iterations);
            for(int i = 0; i < pattern.length(); i++)
            {
                char c = pattern.charAt(i);
                if(c == 'F')
                {
                    Point2D.Double newPoint = new Point2D.Double(curData.pos.x + Math.cos(Math.toRadians(curData.angle))*lineLength, curData.pos.y - Math.sin(Math.toRadians(curData.angle))*lineLength);
                    g2.draw(new Line2D.Double(curData.pos, newPoint));
                    curData.pos = newPoint;
                }
                else if(c == 'f')
                    curData.pos = new Point2D.Double(curData.pos.x + Math.cos(Math.toRadians(curData.angle))*lineLength, curData.pos.y - Math.sin(Math.toRadians(curData.angle))*lineLength);
                else if(c == '-')
                    curData.angle += angleDiff;
                else if(c == '+')
                    curData.angle -= angleDiff;
                else if(c == '[')
                    stk.push(curData.clone());
                else if(c == ']')
                    curData = stk.pop();
            }
        }

        private class StackData
        {
            public Point2D.Double pos;
            public double angle;

            public StackData(Point2D.Double pos, double angle)
            {
                this.pos = pos;
                this.angle = angle;
            }

            public StackData clone()
            {
                return new StackData(pos, angle);
            }
        }
    }
}
