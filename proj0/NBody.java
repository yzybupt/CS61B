public class NBody{
    private static String imageToDraw = "images/starfield.jpg";

    public static double readRadius(String file){
        In in = new In(file);
        int firstItemInFile = in.readInt();
		double secondItemInFile = in.readDouble();
        return secondItemInFile;
    }

    public static Planet[] readPlanets(String file){
        In in = new In(file);
        int N = in.readInt();
		double radius = in.readDouble();
        Planet [] p = new Planet[N];
        for(int i=0; i<N; i++){
            p[i]= new Planet(in.readDouble(),in.readDouble(),in.readDouble(),in.readDouble(),in.readDouble(),in.readString());
        }
        return p;

    }

    public static void main(String[] args) {
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];
        Planet[] planets = readPlanets(filename);
        double radius = readRadius(filename);    


        StdDraw.enableDoubleBuffering();
        StdDraw.setCanvasSize(512,512);
        StdDraw.setScale(-radius,radius);
        
    
        double time = 0;

        while(time < T){
            double[] xForces = new double[planets.length];
            double[] yForces = new double[planets.length];
            for(int i = 0; i< planets.length;i++){
                xForces[i]=planets[i].calcNetForceExertedByX(planets);
                yForces[i]=planets[i].calcNetForceExertedByY(planets);
            }
            for(int i = 0; i< planets.length;i++){
                planets[i].update(dt, xForces[i], yForces[i]);
            }

            StdDraw.picture(0, 0, imageToDraw);
            for(int i = 0; i< planets.length;i++){
                planets[i].draw();
            }

            StdDraw.show();
            StdDraw.pause(10);
            
            time=time+dt;
        }

        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < planets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,planets[i].yyVel, planets[i].mass, planets[i].imgFileName);   
        }

    }

}