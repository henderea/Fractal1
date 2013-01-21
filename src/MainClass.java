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
    private final double inc = 0.5;

    public MainClass()
    {
        models = new ArrayList<Model>();
        models.add(new Model("Weed").dad(25).seg(3).m('F', "F[-F]F[+F]F"));
        models.add(new Model("Weed 2").it(5).dad(90).seg(3).m('F', "FF[-F-F][+F+F]F"));
        models.add(new Model("Weed 3").it(6).dad(25).seg(2.4).init("X").m('X', "F-[[X]+X]+F[+FX]-X").m('F', "FF").ia(60).ip(0.2, 0.9));
        models.add(new Model("Vine").dad(70).seg(3).m('F', "FF+F+F+FF+F+F-F"));
        models.add(new Model("Peano").dad(90).seg(3).init("X").ip(0, 1).m('X', "XFYFX+F+YFXFY-F-XFYFX").m('Y', "YFXFY-F-XFYFX+F+YFXFY"));
        models.add(new Model("Space-filling 2").it(6).ia(-90).dad(90).seg(2).init("X").ip(0, 0).m('X', "-YF+XFX+FY-").m('Y', "+XF-YFY-FX+"));
        models.add(new Model("Koch").dad(60).seg(3).m('F', "F-F++F-F"));
        models.add(new Model("Koch 2").dad(90).seg(3).m('F', "F-F+F+F-F"));
        models.add(new Model("Sierpinski").it(8).dad(60).seg(2).init("A").ia(-60).ip(0.5, 0.05).m('A', "B-A-B").m('B', "A+B+A").fm('A', "F").fm('B', "F"));
        models.add(new Model("Carpet").dad(90).seg(3).m('F', "F+F-F-F-f+F+F+F-F").m('f', "fff"));
        models.add(new Model("Median").it(8).dad(45).seg(1.645).init("L--F--L--F").m('L', "+R-F-R+").m('R', "-L+F+L-").fm('L', "F").fm('R', "F"));
        models.add(new Model("Dragon").it(10).dad(90).seg(1.5).init("FX").ip(0.3, 0.6).m('X', "X+YF").m('Y', "FX-Y"));
        models.add(new Model("Gosper").ia(30).dad(60).seg(3).init("XF").ip(0.4, 0.2).m('X', "X+YF++YF-FX--FXFX-YF+").m('Y', "-FX+YFYF++YF+FX--FX-Y"));
        models.add(new Model("Penrose").it(4).dad(36).seg(2).init("[7]++[7]++[7]++[7]++[7]").ip(0.5, 0.5).m('6', "81++91----71[-81----61]++").m('7', "+81--91[---61--71]+").m('8', "-61++71[+++81++91]-").m('9', "--81++++61[+91++++71]--71").m('1', "").fm('1', "F"));
        models.add(new Model("Pleasant Error").ia(18).dad(72).seg(3.05).init("F-F-F-F-F").ip(0.375, 0.075).m('F', "F-F++F+F-F-F"));
        models.add(new Model("Lace").it(6).dad(30).seg(2.11).init("W").ip(0, 1).m('W', "+++X--F--ZFX+").m('X', "---W++F++YFW-").m('Y', "+ZFX--F--Z+++").m('Z', "-YFW++F++Y---").fm('W', "F").fm('X', "F").fm('Y', "F").fm('Z', "F"));
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
                    dp.angleDiff += inc * (e.isControlDown() ? 10 : 1);
                    if(dp.angleDiff > 120)
                        dp.angleDiff = 120;
                }
                else if(e.getKeyCode() == KeyEvent.VK_DOWN)
                {
                    dp.angleDiff -= inc * (e.isControlDown() ? 10 : 1);
                    if(dp.angleDiff < 5)
                        dp.angleDiff = 5;
                }
                else if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    System.exit(0);
                setTitle("Angle Diff: "+dp.angleDiff+"; Model: "+models.get(mind).name);
                repaint();
            }
        });
        this.setVisible(true);
        setTitle("Angle Diff: "+dp.angleDiff+"; Model: "+models.get(mind).name);
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

        public Model ia(double angle)
        {
            this.initialAngle = angle;
            return this;
        }

        public Model ip(double initX, double initY)
        {
            this.initX = initX;
            this.initY = initY;
            return this;
        }

        public Model it(int iterations)
        {
            this.iterations = iterations;
            return this;
        }

        public Model init(String init)
        {
            this.init = init;
            return this;
        }

        public Model dad(double defaultAngleDiff)
        {
            this.defaultAngleDiff = defaultAngleDiff;
            return this;
        }

        public Model seg(double segments)
        {
            this.segments = segments;
            return this;
        }

        public Model m(char from, String to)
        {
            this.mappings.put(from, to);
            return this;
        }

        public Model fm(char from, String to)
        {
            this.finalMappings.put(from, to);
            return this;
        }

        public Model(String name)
        {
            this.name = name;
            this.mappings = new HashMap<Character, String>();
            this.finalMappings = new HashMap<Character, String>();
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
            //g2.drawString("Angle Diff: "+angleDiff, 10, 20);
            //g2.drawString("Model: "+curModel.name, 10, 40);
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
